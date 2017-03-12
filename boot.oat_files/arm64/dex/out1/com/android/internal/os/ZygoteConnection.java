package com.android.internal.os;

import android.net.Credentials;
import android.net.LocalSocket;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import com.android.internal.os.ZygoteInit.MethodAndArgsCaller;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.smartface.SmartFaceManager;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import libcore.io.IoUtils;

class ZygoteConnection {
    private static final int CONNECTION_TIMEOUT_MILLIS = 1000;
    private static final int MAX_ZYGOTE_ARGC = 1024;
    private static final String TAG = "Zygote";
    private static final int[][] intArray2d = ((int[][]) Array.newInstance(Integer.TYPE, new int[]{0, 0}));
    private final String abiList;
    private final LocalSocket mSocket;
    private final DataOutputStream mSocketOutStream;
    private final BufferedReader mSocketReader;
    private final Credentials peer;

    static class Arguments {
        boolean abiListQuery;
        int accessInfo;
        String appDataDir;
        boolean capabilitiesSpecified;
        int category;
        int debugFlags;
        long effectiveCapabilities;
        int gid = 0;
        boolean gidSpecified;
        int[] gids;
        String instructionSet;
        String invokeWith;
        int mountExternal = 0;
        String niceName;
        long permittedCapabilities;
        String[] remainingArgs;
        ArrayList<int[]> rlimits;
        String seInfo;
        boolean seInfoSpecified;
        int targetSdkVersion;
        boolean targetSdkVersionSpecified;
        int uid = 0;
        boolean uidSpecified;

        Arguments(String[] args) throws IllegalArgumentException {
            parseArgs(args);
        }

        private void parseArgs(String[] args) throws IllegalArgumentException {
            int curArg = 0;
            boolean seenRuntimeArgs = false;
            while (curArg < args.length) {
                String arg = args[curArg];
                if (arg.equals("--")) {
                    curArg++;
                    break;
                }
                if (!arg.startsWith("--setuid=")) {
                    if (!arg.startsWith("--setgid=")) {
                        if (!arg.startsWith("--target-sdk-version=")) {
                            if (!arg.equals("--enable-debugger")) {
                                if (!arg.equals("--enable-safemode")) {
                                    if (!arg.equals("--enable-checkjni")) {
                                        if (!arg.equals("--enable-jit")) {
                                            if (!arg.equals("--generate-debug-info")) {
                                                if (!arg.equals("--enable-jni-logging")) {
                                                    if (!arg.equals("--enable-assert")) {
                                                        if (!arg.equals("--runtime-args")) {
                                                            if (!arg.startsWith("--seinfo=")) {
                                                                if (!arg.startsWith("--category=")) {
                                                                    if (!arg.startsWith("--accessInfo=")) {
                                                                        if (!arg.startsWith("--capabilities=")) {
                                                                            int i;
                                                                            if (!arg.startsWith("--rlimit=")) {
                                                                                if (!arg.startsWith("--setgroups=")) {
                                                                                    if (!arg.equals("--invoke-with")) {
                                                                                        if (!arg.startsWith("--nice-name=")) {
                                                                                            if (!arg.equals("--mount-external-default")) {
                                                                                                if (!arg.equals("--mount-external-read")) {
                                                                                                    if (!arg.equals("--mount-external-write")) {
                                                                                                        if (!arg.equals("--query-abi-list")) {
                                                                                                            if (!arg.startsWith("--instruction-set=")) {
                                                                                                                if (!arg.startsWith("--app-data-dir=")) {
                                                                                                                    break;
                                                                                                                }
                                                                                                                this.appDataDir = arg.substring(arg.indexOf(61) + 1);
                                                                                                            } else {
                                                                                                                this.instructionSet = arg.substring(arg.indexOf(61) + 1);
                                                                                                            }
                                                                                                        } else {
                                                                                                            this.abiListQuery = true;
                                                                                                        }
                                                                                                    } else {
                                                                                                        this.mountExternal = 3;
                                                                                                    }
                                                                                                } else {
                                                                                                    this.mountExternal = 2;
                                                                                                }
                                                                                            } else {
                                                                                                this.mountExternal = 1;
                                                                                            }
                                                                                        } else if (this.niceName != null) {
                                                                                            throw new IllegalArgumentException("Duplicate arg specified");
                                                                                        } else {
                                                                                            this.niceName = arg.substring(arg.indexOf(61) + 1);
                                                                                        }
                                                                                    } else if (this.invokeWith != null) {
                                                                                        throw new IllegalArgumentException("Duplicate arg specified");
                                                                                    } else {
                                                                                        curArg++;
                                                                                        try {
                                                                                            this.invokeWith = args[curArg];
                                                                                        } catch (IndexOutOfBoundsException e) {
                                                                                            throw new IllegalArgumentException("--invoke-with requires argument");
                                                                                        }
                                                                                    }
                                                                                } else if (this.gids != null) {
                                                                                    throw new IllegalArgumentException("Duplicate arg specified");
                                                                                } else {
                                                                                    String[] params = arg.substring(arg.indexOf(61) + 1).split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                                                                                    this.gids = new int[params.length];
                                                                                    for (i = params.length - 1; i >= 0; i--) {
                                                                                        this.gids[i] = Integer.parseInt(params[i]);
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                String[] limitStrings = arg.substring(arg.indexOf(61) + 1).split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                                                                                if (limitStrings.length != 3) {
                                                                                    throw new IllegalArgumentException("--rlimit= should have 3 comma-delimited ints");
                                                                                }
                                                                                int[] rlimitTuple = new int[limitStrings.length];
                                                                                for (i = 0; i < limitStrings.length; i++) {
                                                                                    rlimitTuple[i] = Integer.parseInt(limitStrings[i]);
                                                                                }
                                                                                if (this.rlimits == null) {
                                                                                    this.rlimits = new ArrayList();
                                                                                }
                                                                                this.rlimits.add(rlimitTuple);
                                                                            }
                                                                        } else if (this.capabilitiesSpecified) {
                                                                            throw new IllegalArgumentException("Duplicate arg specified");
                                                                        } else {
                                                                            this.capabilitiesSpecified = true;
                                                                            String[] capStrings = arg.substring(arg.indexOf(61) + 1).split(FingerprintManager.FINGER_PERMISSION_DELIMITER, 2);
                                                                            if (capStrings.length == 1) {
                                                                                this.effectiveCapabilities = Long.decode(capStrings[0]).longValue();
                                                                                this.permittedCapabilities = this.effectiveCapabilities;
                                                                            } else {
                                                                                this.permittedCapabilities = Long.decode(capStrings[0]).longValue();
                                                                                this.effectiveCapabilities = Long.decode(capStrings[1]).longValue();
                                                                            }
                                                                        }
                                                                    } else {
                                                                        this.accessInfo = Integer.parseInt(arg.substring(arg.indexOf(61) + 1));
                                                                    }
                                                                } else {
                                                                    this.category = Integer.parseInt(arg.substring(arg.indexOf(61) + 1));
                                                                }
                                                            } else if (this.seInfoSpecified) {
                                                                throw new IllegalArgumentException("Duplicate arg specified");
                                                            } else {
                                                                this.seInfoSpecified = true;
                                                                this.seInfo = arg.substring(arg.indexOf(61) + 1);
                                                            }
                                                        } else {
                                                            seenRuntimeArgs = true;
                                                        }
                                                    } else {
                                                        this.debugFlags |= 4;
                                                    }
                                                } else {
                                                    this.debugFlags |= 16;
                                                }
                                            } else {
                                                this.debugFlags |= 64;
                                            }
                                        } else {
                                            this.debugFlags |= 32;
                                        }
                                    } else {
                                        this.debugFlags |= 2;
                                    }
                                } else {
                                    this.debugFlags |= 8;
                                }
                            } else {
                                this.debugFlags |= 1;
                            }
                        } else if (this.targetSdkVersionSpecified) {
                            throw new IllegalArgumentException("Duplicate target-sdk-version specified");
                        } else {
                            this.targetSdkVersionSpecified = true;
                            this.targetSdkVersion = Integer.parseInt(arg.substring(arg.indexOf(61) + 1));
                        }
                    } else if (this.gidSpecified) {
                        throw new IllegalArgumentException("Duplicate arg specified");
                    } else {
                        this.gidSpecified = true;
                        this.gid = Integer.parseInt(arg.substring(arg.indexOf(61) + 1));
                    }
                } else if (this.uidSpecified) {
                    throw new IllegalArgumentException("Duplicate arg specified");
                } else {
                    this.uidSpecified = true;
                    this.uid = Integer.parseInt(arg.substring(arg.indexOf(61) + 1));
                }
                curArg++;
            }
            if (this.abiListQuery) {
                if (args.length - curArg > 0) {
                    throw new IllegalArgumentException("Unexpected arguments after --query-abi-list.");
                }
            } else if (seenRuntimeArgs) {
                this.remainingArgs = new String[(args.length - curArg)];
                System.arraycopy(args, curArg, this.remainingArgs, 0, this.remainingArgs.length);
            } else {
                throw new IllegalArgumentException("Unexpected argument : " + args[curArg]);
            }
        }
    }

    ZygoteConnection(LocalSocket socket, String abiList) throws IOException {
        this.mSocket = socket;
        this.abiList = abiList;
        this.mSocketOutStream = new DataOutputStream(socket.getOutputStream());
        this.mSocketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()), 256);
        this.mSocket.setSoTimeout(1000);
        try {
            this.peer = this.mSocket.getPeerCredentials();
        } catch (IOException ex) {
            Log.e(TAG, "Cannot read peer credentials", ex);
            throw ex;
        }
    }

    FileDescriptor getFileDesciptor() {
        return this.mSocket.getFileDescriptor();
    }

    boolean runOnce() throws MethodAndArgsCaller {
        boolean handleParentProc;
        Throwable ex;
        Arguments parsedArgs = null;
        try {
            String[] args = readArgumentList();
            FileDescriptor[] descriptors = this.mSocket.getAncillaryFileDescriptors();
            if (args == null) {
                closeSocket();
                return true;
            }
            PrintStream newStderr = null;
            if (descriptors != null && descriptors.length >= 3) {
                PrintStream printStream = new PrintStream(new FileOutputStream(descriptors[2]));
            }
            int pid = -1;
            FileDescriptor childPipeFd = null;
            FileDescriptor serverPipeFd = null;
            try {
                Arguments arguments = new Arguments(args);
                try {
                    if (arguments.abiListQuery) {
                        parsedArgs = arguments;
                        return handleAbiListQuery();
                    } else if (arguments.permittedCapabilities == 0 && arguments.effectiveCapabilities == 0) {
                        applyUidSecurityPolicy(arguments, this.peer);
                        applyInvokeWithSecurityPolicy(arguments, this.peer);
                        applyDebuggerSystemProperty(arguments);
                        applyInvokeWithSystemProperty(arguments);
                        int[][] rlimits = null;
                        if (arguments.rlimits != null) {
                            rlimits = (int[][]) arguments.rlimits.toArray(intArray2d);
                        }
                        if (arguments.invokeWith != null) {
                            FileDescriptor[] pipeFds = Os.pipe2(OsConstants.O_CLOEXEC);
                            childPipeFd = pipeFds[1];
                            serverPipeFd = pipeFds[0];
                            Os.fcntlInt(childPipeFd, OsConstants.F_SETFD, 0);
                        }
                        int[] iArr = new int[2];
                        iArr = new int[]{-1, -1};
                        FileDescriptor fd = this.mSocket.getFileDescriptor();
                        if (fd != null) {
                            iArr[0] = fd.getInt$();
                        }
                        fd = ZygoteInit.getServerSocketFileDescriptor();
                        if (fd != null) {
                            iArr[1] = fd.getInt$();
                        }
                        pid = Zygote.forkAndSpecialize(arguments.uid, arguments.gid, arguments.gids, arguments.debugFlags, rlimits, arguments.mountExternal, arguments.seInfo, arguments.category, arguments.accessInfo, arguments.niceName, iArr, arguments.instructionSet, arguments.appDataDir);
                        parsedArgs = arguments;
                        if (pid == 0) {
                            try {
                                IoUtils.closeQuietly(serverPipeFd);
                                serverPipeFd = null;
                                handleChildProc(parsedArgs, descriptors, childPipeFd, newStderr);
                            } finally {
                                IoUtils.closeQuietly(childPipeFd);
                                IoUtils.closeQuietly(serverPipeFd);
                            }
                        } else {
                            IoUtils.closeQuietly(childPipeFd);
                            childPipeFd = null;
                            handleParentProc = handleParentProc(pid, descriptors, serverPipeFd, parsedArgs);
                            IoUtils.closeQuietly(null);
                            IoUtils.closeQuietly(serverPipeFd);
                            return handleParentProc;
                        }
                        return true;
                    } else {
                        throw new ZygoteSecurityException("Client may not specify capabilities: permitted=0x" + Long.toHexString(arguments.permittedCapabilities) + ", effective=0x" + Long.toHexString(arguments.effectiveCapabilities));
                    }
                } catch (ErrnoException e) {
                    ex = e;
                    parsedArgs = arguments;
                    logAndPrintError(newStderr, "Exception creating pipe", ex);
                    if (pid == 0) {
                        IoUtils.closeQuietly(childPipeFd);
                        childPipeFd = null;
                        handleParentProc = handleParentProc(pid, descriptors, serverPipeFd, parsedArgs);
                        IoUtils.closeQuietly(null);
                        IoUtils.closeQuietly(serverPipeFd);
                        return handleParentProc;
                    }
                    IoUtils.closeQuietly(serverPipeFd);
                    serverPipeFd = null;
                    handleChildProc(parsedArgs, descriptors, childPipeFd, newStderr);
                    return true;
                } catch (IllegalArgumentException e2) {
                    ex = e2;
                    parsedArgs = arguments;
                    logAndPrintError(newStderr, "Invalid zygote arguments", ex);
                    if (pid == 0) {
                        IoUtils.closeQuietly(serverPipeFd);
                        serverPipeFd = null;
                        handleChildProc(parsedArgs, descriptors, childPipeFd, newStderr);
                    } else {
                        IoUtils.closeQuietly(childPipeFd);
                        childPipeFd = null;
                        handleParentProc = handleParentProc(pid, descriptors, serverPipeFd, parsedArgs);
                        IoUtils.closeQuietly(null);
                        IoUtils.closeQuietly(serverPipeFd);
                        return handleParentProc;
                    }
                    return true;
                } catch (ZygoteSecurityException e3) {
                    ex = e3;
                    parsedArgs = arguments;
                    logAndPrintError(newStderr, "Zygote security policy prevents request: ", ex);
                    if (pid == 0) {
                        IoUtils.closeQuietly(childPipeFd);
                        childPipeFd = null;
                        handleParentProc = handleParentProc(pid, descriptors, serverPipeFd, parsedArgs);
                        IoUtils.closeQuietly(null);
                        IoUtils.closeQuietly(serverPipeFd);
                        return handleParentProc;
                    }
                    IoUtils.closeQuietly(serverPipeFd);
                    serverPipeFd = null;
                    handleChildProc(parsedArgs, descriptors, childPipeFd, newStderr);
                    return true;
                }
            } catch (ErrnoException e4) {
                ex = e4;
                logAndPrintError(newStderr, "Exception creating pipe", ex);
                if (pid == 0) {
                    IoUtils.closeQuietly(serverPipeFd);
                    serverPipeFd = null;
                    handleChildProc(parsedArgs, descriptors, childPipeFd, newStderr);
                } else {
                    IoUtils.closeQuietly(childPipeFd);
                    childPipeFd = null;
                    handleParentProc = handleParentProc(pid, descriptors, serverPipeFd, parsedArgs);
                    IoUtils.closeQuietly(null);
                    IoUtils.closeQuietly(serverPipeFd);
                    return handleParentProc;
                }
                return true;
            } catch (IllegalArgumentException e5) {
                ex = e5;
                logAndPrintError(newStderr, "Invalid zygote arguments", ex);
                if (pid == 0) {
                    IoUtils.closeQuietly(childPipeFd);
                    childPipeFd = null;
                    handleParentProc = handleParentProc(pid, descriptors, serverPipeFd, parsedArgs);
                    IoUtils.closeQuietly(null);
                    IoUtils.closeQuietly(serverPipeFd);
                    return handleParentProc;
                }
                IoUtils.closeQuietly(serverPipeFd);
                serverPipeFd = null;
                handleChildProc(parsedArgs, descriptors, childPipeFd, newStderr);
                return true;
            } catch (ZygoteSecurityException e6) {
                ex = e6;
                logAndPrintError(newStderr, "Zygote security policy prevents request: ", ex);
                if (pid == 0) {
                    IoUtils.closeQuietly(serverPipeFd);
                    serverPipeFd = null;
                    handleChildProc(parsedArgs, descriptors, childPipeFd, newStderr);
                } else {
                    IoUtils.closeQuietly(childPipeFd);
                    childPipeFd = null;
                    handleParentProc = handleParentProc(pid, descriptors, serverPipeFd, parsedArgs);
                    IoUtils.closeQuietly(null);
                    IoUtils.closeQuietly(serverPipeFd);
                    return handleParentProc;
                }
                return true;
            }
        } catch (IOException ex2) {
            Log.w(TAG, "IOException on command socket " + ex2.getMessage());
            closeSocket();
            return true;
        }
    }

    private boolean handleAbiListQuery() {
        try {
            byte[] abiListBytes = this.abiList.getBytes(StandardCharsets.US_ASCII);
            this.mSocketOutStream.writeInt(abiListBytes.length);
            this.mSocketOutStream.write(abiListBytes);
            return false;
        } catch (IOException ioe) {
            Log.e(TAG, "Error writing to command socket", ioe);
            return true;
        }
    }

    void closeSocket() {
        try {
            this.mSocket.close();
        } catch (IOException ex) {
            Log.e(TAG, "Exception while closing command socket in parent", ex);
        }
    }

    private String[] readArgumentList() throws IOException {
        try {
            String s = this.mSocketReader.readLine();
            if (s == null) {
                return null;
            }
            int argc = Integer.parseInt(s);
            if (argc > 1024) {
                throw new IOException("max arg count exceeded");
            }
            String[] result = new String[argc];
            for (int i = 0; i < argc; i++) {
                result[i] = this.mSocketReader.readLine();
                if (result[i] == null) {
                    throw new IOException("truncated request");
                }
            }
            return result;
        } catch (NumberFormatException e) {
            Log.e(TAG, "invalid Zygote wire format: non-int at argc");
            throw new IOException("invalid wire format");
        }
    }

    private static void applyUidSecurityPolicy(Arguments args, Credentials peer) throws ZygoteSecurityException {
        if (peer.getUid() == 1000) {
            String factoryTest = SystemProperties.get("ro.factorytest");
            boolean uidRestricted = (factoryTest.equals(SmartFaceManager.PAGE_BOTTOM) || factoryTest.equals("2")) ? false : true;
            if (uidRestricted && args.uidSpecified && args.uid < 1000) {
                throw new ZygoteSecurityException("System UID may not launch process with UID < 1000");
            }
        }
        if (!args.uidSpecified) {
            args.uid = peer.getUid();
            args.uidSpecified = true;
        }
        if (!args.gidSpecified) {
            args.gid = peer.getGid();
            args.gidSpecified = true;
        }
    }

    public static void applyDebuggerSystemProperty(Arguments args) {
        if (SmartFaceManager.PAGE_BOTTOM.equals(SystemProperties.get("ro.debuggable"))) {
            args.debugFlags |= 1;
        }
    }

    private static void applyInvokeWithSecurityPolicy(Arguments args, Credentials peer) throws ZygoteSecurityException {
        int peerUid = peer.getUid();
        if (args.invokeWith != null && peerUid != 0) {
            throw new ZygoteSecurityException("Peer is not permitted to specify an explicit invoke-with wrapper command");
        }
    }

    public static void applyInvokeWithSystemProperty(Arguments args) {
        if (args.invokeWith == null && args.niceName != null) {
            String property = "wrap." + args.niceName;
            if (property.length() > 31) {
                if (property.charAt(30) != '.') {
                    property = property.substring(0, 31);
                } else {
                    property = property.substring(0, 30);
                }
            }
            args.invokeWith = SystemProperties.get(property);
            if (args.invokeWith != null && args.invokeWith.length() == 0) {
                args.invokeWith = null;
            }
        }
    }

    private void handleChildProc(Arguments parsedArgs, FileDescriptor[] descriptors, FileDescriptor pipeFd, PrintStream newStderr) throws MethodAndArgsCaller {
        closeSocket();
        ZygoteInit.closeServerSocket();
        if (descriptors != null) {
            try {
                Os.dup2(descriptors[0], OsConstants.STDIN_FILENO);
                Os.dup2(descriptors[1], OsConstants.STDOUT_FILENO);
                Os.dup2(descriptors[2], OsConstants.STDERR_FILENO);
                for (FileDescriptor fd : descriptors) {
                    IoUtils.closeQuietly(fd);
                }
                newStderr = System.err;
            } catch (ErrnoException ex) {
                Log.e(TAG, "Error reopening stdio", ex);
            }
        }
        if (parsedArgs.niceName != null) {
            Process.setArgV0(parsedArgs.niceName);
        }
        Trace.traceEnd(64);
        if (parsedArgs.invokeWith != null) {
            WrapperInit.execApplication(parsedArgs.invokeWith, parsedArgs.niceName, parsedArgs.targetSdkVersion, VMRuntime.getCurrentInstructionSet(), pipeFd, parsedArgs.remainingArgs);
            return;
        }
        RuntimeInit.zygoteInit(parsedArgs.targetSdkVersion, parsedArgs.remainingArgs, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean handleParentProc(int r13, java.io.FileDescriptor[] r14, java.io.FileDescriptor r15, com.android.internal.os.ZygoteConnection.Arguments r16) {
        /*
        r12 = this;
        if (r13 <= 0) goto L_0x0005;
    L_0x0002:
        r12.setChildPgid(r13);
    L_0x0005:
        if (r14 == 0) goto L_0x0014;
    L_0x0007:
        r0 = r14;
        r6 = r0.length;
        r3 = 0;
    L_0x000a:
        if (r3 >= r6) goto L_0x0014;
    L_0x000c:
        r2 = r0[r3];
        libcore.io.IoUtils.closeQuietly(r2);
        r3 = r3 + 1;
        goto L_0x000a;
    L_0x0014:
        r8 = 0;
        if (r15 == 0) goto L_0x0066;
    L_0x0017:
        if (r13 <= 0) goto L_0x0066;
    L_0x0019:
        r5 = new java.io.DataInputStream;
        r9 = new java.io.FileInputStream;
        r9.<init>(r15);
        r5.<init>(r9);
        r4 = -1;
        r4 = r5.readInt();	 Catch:{ IOException -> 0x0037 }
        r5.close();	 Catch:{ IOException -> 0x009f }
    L_0x002b:
        if (r4 <= 0) goto L_0x0066;
    L_0x002d:
        r7 = r4;
    L_0x002e:
        if (r7 <= 0) goto L_0x004a;
    L_0x0030:
        if (r7 == r13) goto L_0x004a;
    L_0x0032:
        r7 = android.os.Process.getParentPid(r7);
        goto L_0x002e;
    L_0x0037:
        r1 = move-exception;
        r9 = "Zygote";
        r10 = "Error reading pid from wrapped process, child may have died";
        android.util.Log.w(r9, r10, r1);	 Catch:{ all -> 0x0045 }
        r5.close();	 Catch:{ IOException -> 0x0043 }
        goto L_0x002b;
    L_0x0043:
        r9 = move-exception;
        goto L_0x002b;
    L_0x0045:
        r9 = move-exception;
        r5.close();	 Catch:{ IOException -> 0x00a1 }
    L_0x0049:
        throw r9;
    L_0x004a:
        if (r7 <= 0) goto L_0x0072;
    L_0x004c:
        r9 = "Zygote";
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Wrapped process has pid ";
        r10 = r10.append(r11);
        r10 = r10.append(r4);
        r10 = r10.toString();
        android.util.Log.i(r9, r10);
        r13 = r4;
        r8 = 1;
    L_0x0066:
        r9 = r12.mSocketOutStream;	 Catch:{ IOException -> 0x0095 }
        r9.writeInt(r13);	 Catch:{ IOException -> 0x0095 }
        r9 = r12.mSocketOutStream;	 Catch:{ IOException -> 0x0095 }
        r9.writeBoolean(r8);	 Catch:{ IOException -> 0x0095 }
        r9 = 0;
    L_0x0071:
        return r9;
    L_0x0072:
        r9 = "Zygote";
        r10 = new java.lang.StringBuilder;
        r10.<init>();
        r11 = "Wrapped process reported a pid that is not a child of the process that we forked: childPid=";
        r10 = r10.append(r11);
        r10 = r10.append(r13);
        r11 = " innerPid=";
        r10 = r10.append(r11);
        r10 = r10.append(r4);
        r10 = r10.toString();
        android.util.Log.w(r9, r10);
        goto L_0x0066;
    L_0x0095:
        r1 = move-exception;
        r9 = "Zygote";
        r10 = "Error writing to command socket";
        android.util.Log.e(r9, r10, r1);
        r9 = 1;
        goto L_0x0071;
    L_0x009f:
        r9 = move-exception;
        goto L_0x002b;
    L_0x00a1:
        r10 = move-exception;
        goto L_0x0049;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteConnection.handleParentProc(int, java.io.FileDescriptor[], java.io.FileDescriptor, com.android.internal.os.ZygoteConnection$Arguments):boolean");
    }

    private void setChildPgid(int pid) {
        try {
            Os.setpgid(pid, Os.getpgid(this.peer.getPid()));
        } catch (ErrnoException e) {
            Log.i(TAG, "Zygote: setpgid failed. This is normal if peer is not in our session");
        }
    }

    private static void logAndPrintError(PrintStream newStderr, String message, Throwable ex) {
        Log.e(TAG, message, ex);
        if (newStderr != null) {
            StringBuilder append = new StringBuilder().append(message);
            if (ex == null) {
                ex = "";
            }
            newStderr.println(append.append(ex).toString());
        }
    }
}
