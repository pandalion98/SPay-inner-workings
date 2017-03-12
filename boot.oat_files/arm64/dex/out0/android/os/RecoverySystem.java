package android.os;

import android.Manifest.permission;
import android.app.backup.FullBackup;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sec.enterprise.auditlog.AuditLog;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.harmony.security.asn1.BerInputStream;
import org.apache.harmony.security.pkcs7.ContentInfo;
import org.apache.harmony.security.pkcs7.SignedData;
import org.apache.harmony.security.pkcs7.SignerInfo;
import org.apache.harmony.security.x509.Certificate;

public class RecoverySystem {
    private static File COMMAND_FILE = new File(RECOVERY_DIR, "command");
    private static final File DEFAULT_KEYSTORE = new File("/system/etc/security/otacerts.zip");
    private static String LAST_PREFIX = "last_";
    private static File LOG_FILE = new File(RECOVERY_DIR, "log");
    private static int LOG_FILE_MAX_LENGTH = 65536;
    private static final long PUBLISH_PROGRESS_INTERVAL_MS = 500;
    private static File RECOVERY_DIR = new File("/cache/recovery");
    private static final String TAG = "RecoverySystem";
    private static File UNCRYPT_FILE = new File(RECOVERY_DIR, "uncrypt_file");
    private static Boolean mShutdownIsInProgress = new Boolean(false);
    private static final Object mShutdownIsInProgressLock = new Object();

    public interface ProgressListener {
        void onProgress(int i);
    }

    private static HashSet<X509Certificate> getTrustedCerts(File keystore) throws IOException, GeneralSecurityException {
        InputStream is;
        HashSet<X509Certificate> trusted = new HashSet();
        if (keystore == null) {
            keystore = DEFAULT_KEYSTORE;
        }
        ZipFile zip = new ZipFile(keystore);
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                is = zip.getInputStream((ZipEntry) entries.nextElement());
                trusted.add((X509Certificate) cf.generateCertificate(is));
                is.close();
            }
            zip.close();
            return trusted;
        } catch (Throwable th) {
            zip.close();
        }
    }

    public static void verifyPackage(File packageFile, ProgressListener listener, File deviceCertsZipFile) throws IOException, GeneralSecurityException {
        long fileLen = packageFile.length();
        RandomAccessFile randomAccessFile = new RandomAccessFile(packageFile, FullBackup.ROOT_TREE_TOKEN);
        int lastPercent = 0;
        try {
            long lastPublishTime = System.currentTimeMillis();
            if (listener != null) {
                listener.onProgress(0);
            }
            randomAccessFile.seek(fileLen - 6);
            byte[] footer = new byte[6];
            randomAccessFile.readFully(footer);
            if (footer[2] == (byte) -1 && footer[3] == (byte) -1) {
                int commentSize = (footer[4] & 255) | ((footer[5] & 255) << 8);
                int signatureStart = (footer[0] & 255) | ((footer[1] & 255) << 8);
                byte[] eocd = new byte[(commentSize + 22)];
                randomAccessFile.seek(fileLen - ((long) (commentSize + 22)));
                randomAccessFile.readFully(eocd);
                if (eocd[0] == (byte) 80 && eocd[1] == (byte) 75 && eocd[2] == (byte) 5 && eocd[3] == (byte) 6) {
                    int i = 4;
                    while (i < eocd.length - 3) {
                        if (eocd[i] == (byte) 80 && eocd[i + 1] == (byte) 75 && eocd[i + 2] == (byte) 5 && eocd[i + 3] == (byte) 6) {
                            throw new SignatureException("EOCD marker found after start of EOCD");
                        }
                        i++;
                    }
                    SignedData signedData = ((ContentInfo) ContentInfo.ASN1.decode(new BerInputStream(new ByteArrayInputStream(eocd, (commentSize + 22) - signatureStart, signatureStart)))).getSignedData();
                    if (signedData == null) {
                        throw new IOException("signedData is null");
                    }
                    List<Certificate> encCerts = signedData.getCertificates();
                    if (encCerts.isEmpty()) {
                        throw new IOException("encCerts is empty");
                    }
                    Iterator<Certificate> it = encCerts.iterator();
                    if (it.hasNext()) {
                        X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(((Certificate) it.next()).getEncoded()));
                        List<SignerInfo> sigInfos = signedData.getSignerInfos();
                        if (sigInfos.isEmpty()) {
                            throw new IOException("no signer infos!");
                        }
                        SignerInfo sigInfo = (SignerInfo) sigInfos.get(0);
                        if (deviceCertsZipFile == null) {
                            deviceCertsZipFile = DEFAULT_KEYSTORE;
                        }
                        HashSet<X509Certificate> trusted = getTrustedCerts(deviceCertsZipFile);
                        PublicKey signatureKey = cert.getPublicKey();
                        boolean verified = false;
                        Iterator i$ = trusted.iterator();
                        while (i$.hasNext()) {
                            if (((X509Certificate) i$.next()).getPublicKey().equals(signatureKey)) {
                                verified = true;
                                break;
                            }
                        }
                        if (verified) {
                            String alg;
                            String da = sigInfo.getDigestAlgorithm();
                            String dea = sigInfo.getDigestEncryptionAlgorithm();
                            if (da == null || dea == null) {
                                alg = cert.getSigAlgName();
                            } else {
                                alg = da + "with" + dea;
                            }
                            Signature sig = Signature.getInstance(alg);
                            sig.initVerify(cert);
                            long toRead = (fileLen - ((long) commentSize)) - 2;
                            long soFar = 0;
                            randomAccessFile.seek(0);
                            byte[] buffer = new byte[4096];
                            boolean interrupted = false;
                            while (soFar < toRead) {
                                interrupted = Thread.interrupted();
                                if (interrupted) {
                                    break;
                                }
                                int size = buffer.length;
                                if (((long) size) + soFar > toRead) {
                                    size = (int) (toRead - soFar);
                                }
                                int read = randomAccessFile.read(buffer, 0, size);
                                sig.update(buffer, 0, read);
                                soFar += (long) read;
                                if (listener != null) {
                                    long now = System.currentTimeMillis();
                                    int p = (int) ((100 * soFar) / toRead);
                                    if (p > lastPercent && now - lastPublishTime > PUBLISH_PROGRESS_INTERVAL_MS) {
                                        lastPercent = p;
                                        lastPublishTime = now;
                                        listener.onProgress(lastPercent);
                                    }
                                }
                            }
                            if (listener != null) {
                                listener.onProgress(100);
                            }
                            if (interrupted) {
                                throw new SignatureException("verification was interrupted");
                            } else if (!sig.verify(sigInfo.getEncryptedDigest())) {
                                throw new SignatureException("signature digest verification failed");
                            } else {
                                return;
                            }
                        }
                        throw new SignatureException("signature doesn't match any trusted key");
                    }
                    throw new SignatureException("signature contains no certificates");
                }
                throw new SignatureException("no signature in file (bad footer)");
            }
            throw new SignatureException("no signature in file (no footer)");
        } finally {
            randomAccessFile.close();
        }
    }

    public static void installPackage(Context context, File packageFile) throws IOException {
        String filename = packageFile.getCanonicalPath();
        FileWriter uncryptFile = new FileWriter(UNCRYPT_FILE);
        try {
            uncryptFile.write(filename + "\n");
            Log.w(TAG, "!!! REBOOTING TO INSTALL " + filename + " !!!");
            if (filename.startsWith("/data/")) {
                filename = "@/cache/recovery/block.map";
            }
            String filenameArg = "--update_package=" + filename;
            String carryoutArg = "--carry_out=open_fota";
            String localeArg = "--locale=" + Locale.getDefault().toString();
            bootCommand(context, filenameArg, "--carry_out=open_fota", localeArg);
        } finally {
            uncryptFile.close();
        }
    }

    public static void rebootWipeUserData(Context context) throws IOException {
        rebootWipeUserData(context, false, context.getPackageName());
    }

    public static void rebootWipeUserData(Context context, String reason) throws IOException {
        rebootWipeUserData(context, false, reason);
    }

    public static void rebootWipeUserData(Context context, boolean shutdown) throws IOException {
        rebootWipeUserData(context, shutdown, context.getPackageName());
    }

    public static void rebootWipeUserData(Context context, boolean shutdown, String reason) throws IOException {
        if (((UserManager) context.getSystemService(Context.USER_SERVICE)).hasUserRestriction(UserManager.DISALLOW_FACTORY_RESET)) {
            AuditLog.log(5, 1, false, Process.myPid(), TAG, "Wiping data (factory reset) is not allowed for this user.");
            throw new SecurityException("Wiping data is not allowed for this user.");
        }
        final ConditionVariable condition = new ConditionVariable();
        Intent intent = new Intent("android.intent.action.MASTER_CLEAR_NOTIFICATION");
        intent.addFlags(268435456);
        context.sendOrderedBroadcastAsUser(intent, UserHandle.OWNER, permission.MASTER_CLEAR, new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                condition.open();
            }
        }, null, 0, null, null);
        condition.block();
        String cryptState1 = SystemProperties.get("ro.crypto.state");
        String cryptState2 = SystemProperties.get("vold.decrypt");
        String shutdownArg = null;
        if (shutdown) {
            shutdownArg = "--shutdown_after";
        }
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String localeArg = "--locale=" + Locale.getDefault().toString();
        try {
            AuditLog.log(5, 1, true, Process.myPid(), TAG, "Starting user data Wipe ");
            bootCommand(context, shutdownArg, "--wipe_data", reasonArg, localeArg);
            if (cryptState1.equals("encrypted") && cryptState2.equals("trigger_restart_min_framework")) {
                bootCommand(context, shutdownArg, "--wipe_data", "--wipe_data_crypto", reasonArg, localeArg);
                return;
            }
            bootCommand(context, shutdownArg, "--wipe_data", reasonArg, localeArg);
        } catch (IOException ioE) {
            AuditLog.log(5, 1, false, Process.myPid(), TAG, "Failed to wipe user data (factory reset). " + ioE.getMessage());
            throw ioE;
        }
    }

    public static void rebootWipeCache(Context context) throws IOException {
        rebootWipeCache(context, context.getPackageName());
        Log.i(TAG, ":::: command -> wipe_cache");
    }

    public static void rebootWipeCache(Context context, String reason) throws IOException {
        String reasonArg = null;
        if (!TextUtils.isEmpty(reason)) {
            reasonArg = "--reason=" + sanitizeArg(reason);
        }
        String localeArg = "--locale=" + Locale.getDefault().toString();
        bootCommand(context, "--wipe_cache", reasonArg, localeArg);
    }

    public static void rebootWipeCustomerPartition(Context context, String arg) throws IOException {
        bootCommand(context, arg);
    }

    private static void bootCommand(Context context, String... args) throws IOException {
        if (SystemProperties.get("sys.recoverysystem.shutdown", "none").equals("running")) {
            Log.i(TAG, "!@RecoverySystem bootCommand disabled, as shutdown already in progress");
            return;
        }
        String reason;
        SystemProperties.set("sys.recoverysystem.shutdown", "running");
        Log.i(TAG, "!@RecoverySystem sys.recoverysystem.shutdown " + SystemProperties.get("sys.recoverysystem.shutdown", "none"));
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder sb = new StringBuilder();
        sb.append("!@RecoverySystem");
        for (StackTraceElement st : stackTraceElements) {
            sb.append(st.toString() + "\n");
        }
        Log.i(TAG, sb.toString());
        RECOVERY_DIR.mkdirs();
        COMMAND_FILE.delete();
        LOG_FILE.delete();
        int retry_count = 3;
        loop4:
        while (true) {
            PowerManager pm;
            RandomAccessFile command = new RandomAccessFile(COMMAND_FILE, "rwd");
            try {
                for (String arg : args) {
                    if (!TextUtils.isEmpty(arg)) {
                        command.writeBytes(arg);
                        command.writeBytes("\n");
                    }
                }
                Log.i(TAG, "!@RecoverySystem before fsync syscall!!");
                command.getFD().sync();
                Log.i(TAG, "!@RecoverySystem after fsync syscall!!");
                retry_count--;
                if (!COMMAND_FILE.exists()) {
                    Log.i(TAG, "Retry_count : %d" + retry_count);
                    if (retry_count == 0) {
                        break loop4;
                    }
                }
                break loop4;
                if (COMMAND_FILE.exists()) {
                    Log.i(TAG, "!@ command file absent, throw exception");
                    throw new IOException("Reboot failed (unable to create command file)");
                }
                pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                reason = SystemProperties.get("persist.sys.reboot.reason");
                if ("nvrecovery".equals(reason)) {
                    Log.i(TAG, "FactoryTest ->nvrecovery ");
                    pm.reboot("nvrecovery");
                } else if (Context.DOWNLOAD_SERVICE.equals(reason)) {
                    pm.reboot(PowerManager.REBOOT_RECOVERY);
                } else {
                    Log.i(TAG, "FactoryTest ->download ");
                    pm.reboot(Context.DOWNLOAD_SERVICE);
                }
                throw new IOException("Reboot failed (no permissions?)");
            } finally {
                command.close();
            }
        }
        Log.i(TAG, "COMMAND_FILE is already exist!!");
        if (COMMAND_FILE.exists()) {
            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            reason = SystemProperties.get("persist.sys.reboot.reason");
            if ("nvrecovery".equals(reason)) {
                Log.i(TAG, "FactoryTest ->nvrecovery ");
                pm.reboot("nvrecovery");
            } else if (Context.DOWNLOAD_SERVICE.equals(reason)) {
                pm.reboot(PowerManager.REBOOT_RECOVERY);
            } else {
                Log.i(TAG, "FactoryTest ->download ");
                pm.reboot(Context.DOWNLOAD_SERVICE);
            }
            throw new IOException("Reboot failed (no permissions?)");
        }
        Log.i(TAG, "!@ command file absent, throw exception");
        throw new IOException("Reboot failed (unable to create command file)");
    }

    private static void copyFile(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            Os.chmod(dest.getPath(), 420);
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error close FileChannel : " + e.getMessage());
                    return;
                }
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        } catch (IOException e2) {
            Log.e(TAG, "Error copy recovery logs : " + e2.getMessage());
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e22) {
                    Log.e(TAG, "Error close FileChannel : " + e22.getMessage());
                    return;
                }
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        } catch (ErrnoException e3) {
            Log.e(TAG, "Error chmod recovery logs : " + e3.getMessage());
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e222) {
                    Log.e(TAG, "Error close FileChannel : " + e222.getMessage());
                    return;
                }
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        } catch (Throwable th) {
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e2222) {
                    Log.e(TAG, "Error close FileChannel : " + e2222.getMessage());
                }
            }
            if (outputChannel != null) {
                outputChannel.close();
            }
        }
    }

    public static String handleAftermath() {
        if (SystemProperties.get("sys.recoverysystem.shutdown", "none").equals("running")) {
            Log.i(TAG, "!@RecoverySystem handleAftermath disabled, as shutdown in progress");
            return null;
        }
        Log.i(TAG, "!@RecoverySystem handleAftermath");
        String log = null;
        try {
            log = FileUtils.readTextFile(LOG_FILE, -LOG_FILE_MAX_LENGTH, "...\n");
        } catch (FileNotFoundException e) {
            Log.i(TAG, "No recovery log file");
        } catch (IOException e2) {
            Log.e(TAG, "Error reading recovery log", e2);
        }
        copyFile(new File(RECOVERY_DIR, "last_recovery"), new File("/data/log/recovery.log"));
        copyFile(new File(RECOVERY_DIR, "last_kernel"), new File("/data/log/recovery_kernel.log"));
        copyFile(new File(RECOVERY_DIR, "last_last_kernel"), new File("/data/log/recovery_last_kernel.log"));
        copyFile(new File(RECOVERY_DIR, "last_recovery_contents"), new File("/data/log/recovery_contents.list"));
        copyFile(new File(RECOVERY_DIR, "last_history"), new File("/data/log/recovery_history.log"));
        copyFile(new File(RECOVERY_DIR, "last_emmc_checksum"), new File("/data/log/emmc_checksum.log"));
        copyFile(new File(RECOVERY_DIR, "last_avc_msg_recovery"), new File("/data/log/avc_msg_recovery.log"));
        copyFile(new File(RECOVERY_DIR, "last_tzdbg_log_recovery"), new File("/data/log/tzdbg_log_recovery.log"));
        copyFile(new File(RECOVERY_DIR, "last_qsee_log_recovery"), new File("/data/log/qsee_log_recovery.log"));
        copyFile(new File(RECOVERY_DIR, "last_meminfo"), new File("/data/log/meminfo"));
        copyFile(new File(RECOVERY_DIR, "block.map"), new File("/data/log/block.map"));
        copyFile(new File(RECOVERY_DIR, "last_meminfo"), new File("/data/log/meminfo"));
        String[] names = RECOVERY_DIR.list();
        int i = 0;
        while (names != null && i < names.length) {
            if (!names[i].startsWith(LAST_PREFIX)) {
                File f = new File(RECOVERY_DIR, names[i]);
                if (f.delete()) {
                    Log.i(TAG, "Deleted: " + f);
                } else {
                    Log.e(TAG, "Can't delete: " + f);
                }
            }
            i++;
        }
        return log;
    }

    private static String sanitizeArg(String arg) {
        return arg.replace('\u0000', '?').replace('\n', '?');
    }
}
