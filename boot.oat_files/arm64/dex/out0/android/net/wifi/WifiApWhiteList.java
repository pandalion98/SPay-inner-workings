package android.net.wifi;

import android.net.ProxyInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Pattern;

public class WifiApWhiteList {
    public static final int WL_ALREADY_IN_TABLE = 4;
    public static final int WL_FAIL = 2;
    public static final int WL_NOT_IN_TABLE = 5;
    public static final int WL_NOT_MAC = 3;
    public static final int WL_SUCCESS = 1;
    private static volatile WifiApWhiteList uniqueInstance;
    private final int BUFFER_SIZE = 64;
    private final String HOSTAPD_ACCEPT = "/data/misc/wifi_hostapd/hostapd.accept";
    private final String HOSTAPD_ACCEPT_OLD = "/data/misc/wifi/hostapd.accept";
    private Vector<WhiteList> mWhiteList = new Vector();

    public static class WhiteList {
        private String mMac;
        private String mName;

        WhiteList(String mac, String name) {
            this.mMac = mac;
            this.mName = name;
        }

        public void setName(String name) {
            this.mName = name;
        }

        public String getMac() {
            return this.mMac;
        }

        public String getName() {
            return this.mName;
        }
    }

    private WifiApWhiteList() {
        createOrChangePermission();
        readWhiteListFile();
    }

    public static WifiApWhiteList getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new WifiApWhiteList();
        }
        return uniqueInstance;
    }

    private void createOrChangePermission() {
        Exception e;
        Throwable th;
        Process p;
        File file = new File("/data/misc/wifi_hostapd/hostapd.accept");
        File fileold = new File("/data/misc/wifi/hostapd.accept");
        if (!file.exists()) {
            if (fileold.exists()) {
                FileInputStream in = null;
                FileOutputStream out = null;
                try {
                    byte[] buffer = new byte[1024];
                    FileInputStream in2 = new FileInputStream("/data/misc/wifi/hostapd.accept");
                    try {
                        FileOutputStream out2 = new FileOutputStream("/data/misc/wifi_hostapd/hostapd.accept");
                        while (true) {
                            try {
                                int n = in2.read(buffer);
                                if (n == -1) {
                                    break;
                                }
                                out2.write(buffer, 0, n);
                            } catch (Exception e2) {
                                e = e2;
                                out = out2;
                                in = in2;
                            } catch (Throwable th2) {
                                th = th2;
                                out = out2;
                                in = in2;
                            }
                        }
                        in2.close();
                        out2.close();
                        if (in2 != null) {
                            try {
                                in2.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        if (out2 != null) {
                            try {
                                out2.close();
                            } catch (IOException e32) {
                                e32.printStackTrace();
                            }
                        }
                    } catch (Exception e4) {
                        e = e4;
                        in = in2;
                        try {
                            e.printStackTrace();
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e322) {
                                    e322.printStackTrace();
                                }
                            }
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e3222) {
                                    e3222.printStackTrace();
                                }
                            }
                            p = Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c", "/system/bin/chmod 665 /data/misc/wifi_hostapd/hostapd.accept"});
                            p.waitFor();
                            p.destroy();
                        } catch (Throwable th3) {
                            th = th3;
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e32222) {
                                    e32222.printStackTrace();
                                }
                            }
                            if (out != null) {
                                try {
                                    out.close();
                                } catch (IOException e322222) {
                                    e322222.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        in = in2;
                        if (in != null) {
                            in.close();
                        }
                        if (out != null) {
                            out.close();
                        }
                        throw th;
                    }
                } catch (Exception e5) {
                    e = e5;
                    e.printStackTrace();
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    p = Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c", "/system/bin/chmod 665 /data/misc/wifi_hostapd/hostapd.accept"});
                    p.waitFor();
                    p.destroy();
                }
            }
            try {
                file.createNewFile();
            } catch (IOException e3222222) {
                e3222222.printStackTrace();
            }
            try {
                p = Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c", "/system/bin/chmod 665 /data/misc/wifi_hostapd/hostapd.accept"});
                try {
                    p.waitFor();
                    p.destroy();
                } catch (InterruptedException e6) {
                    e6.printStackTrace();
                }
            } catch (IOException e32222222) {
                e32222222.printStackTrace();
            }
        }
    }

    private void readWhiteListFile() {
        IOException e;
        Throwable th;
        this.mWhiteList.clear();
        BufferedReader bufferedReader = null;
        try {
            BufferedReader buf = new BufferedReader(new FileReader("/data/misc/wifi_hostapd/hostapd.accept"), 64);
            while (true) {
                try {
                    String bufReadLine = buf.readLine();
                    if (bufReadLine == null) {
                        break;
                    } else if (bufReadLine.startsWith("#")) {
                        String name = bufReadLine.substring(1);
                        this.mWhiteList.add(new WhiteList(buf.readLine(), name));
                    }
                } catch (IOException e2) {
                    e = e2;
                    bufferedReader = buf;
                } catch (Throwable th2) {
                    th = th2;
                    bufferedReader = buf;
                }
            }
            if (buf != null) {
                try {
                    buf.close();
                    bufferedReader = buf;
                    return;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    bufferedReader = buf;
                    return;
                }
            }
        } catch (IOException e4) {
            e3 = e4;
            try {
                e3.printStackTrace();
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e32) {
                        e32.printStackTrace();
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e322) {
                        e322.printStackTrace();
                    }
                }
                throw th;
            }
        }
    }

    private void writeWhiteListFile() {
        IOException e;
        Throwable th;
        FileWriter fileWriter = null;
        try {
            FileWriter fw = new FileWriter("/data/misc/wifi_hostapd/hostapd.accept");
            try {
                Iterator<WhiteList> it = this.mWhiteList.iterator();
                while (it.hasNext()) {
                    WhiteList wl = (WhiteList) it.next();
                    fw.write("#");
                    if (wl.getName() != null) {
                        fw.write(wl.getName());
                    }
                    fw.write("\n");
                    fw.write(wl.getMac());
                    fw.write("\n");
                }
                if (fw != null) {
                    try {
                        fw.close();
                        fileWriter = fw;
                        return;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        fileWriter = fw;
                        return;
                    }
                }
            } catch (IOException e3) {
                e2 = e3;
                fileWriter = fw;
                try {
                    e2.printStackTrace();
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fileWriter = fw;
                if (fileWriter != null) {
                    fileWriter.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            e222.printStackTrace();
            if (fileWriter != null) {
                fileWriter.close();
            }
        }
    }

    public int addWhiteList(String mac, String name) {
        if (!isMacAddress(mac)) {
            return 3;
        }
        Iterator<WhiteList> it = this.mWhiteList.iterator();
        while (it.hasNext()) {
            if (((WhiteList) it.next()).getMac().equalsIgnoreCase(mac)) {
                return 4;
            }
        }
        this.mWhiteList.add(new WhiteList(mac, name));
        writeWhiteListFile();
        return 1;
    }

    public int removeWhiteList(String mac) {
        Iterator<WhiteList> it = this.mWhiteList.iterator();
        while (it.hasNext()) {
            WhiteList wl = (WhiteList) it.next();
            if (wl.getMac().equalsIgnoreCase(mac)) {
                this.mWhiteList.remove(wl);
                writeWhiteListFile();
                return 1;
            }
        }
        return 2;
    }

    public int modifyWhiteList(String mac, String name) {
        Iterator<WhiteList> it = this.mWhiteList.iterator();
        while (it.hasNext()) {
            WhiteList wl = (WhiteList) it.next();
            if (wl.getMac().equalsIgnoreCase(mac)) {
                wl.setName(name);
                writeWhiteListFile();
                return 1;
            }
        }
        return 2;
    }

    public String getDeviceName(String mac) {
        Iterator<WhiteList> it = this.mWhiteList.iterator();
        while (it.hasNext()) {
            WhiteList wl = (WhiteList) it.next();
            if (wl.getMac().equalsIgnoreCase(mac)) {
                return wl.getName();
            }
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }

    public boolean isContains(String mac) {
        Iterator<WhiteList> it = this.mWhiteList.iterator();
        while (it.hasNext()) {
            if (((WhiteList) it.next()).getMac().equalsIgnoreCase(mac)) {
                return true;
            }
        }
        return false;
    }

    public Iterator<WhiteList> getIterator() {
        if (this.mWhiteList.isEmpty()) {
            return null;
        }
        return this.mWhiteList.iterator();
    }

    public int getSize() {
        return this.mWhiteList.size();
    }

    private boolean isMacAddress(String macAddressCandidate) {
        return Pattern.compile("[0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0-9a-fA-F]{2}[-:][0 -9a-fA-F]{2}[-:][0-9a-fA-F]{2}").matcher(macAddressCandidate).matches();
    }
}
