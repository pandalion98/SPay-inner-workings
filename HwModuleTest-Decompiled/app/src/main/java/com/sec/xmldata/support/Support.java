package com.sec.xmldata.support;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Environment;
import android.os.SystemProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Support {
    private static final String CLASS_NAME = "Support";

    public static class CommandFilter {
        public static final String TAG = "AtCommand";
        static ArrayList<String> filteredCommands = null;

        /* JADX WARNING: Unknown top exception splitter block from list: {B:45:0x00f5=Splitter:B:45:0x00f5, B:33:0x00d9=Splitter:B:33:0x00d9} */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static synchronized java.lang.String[] getFilteredCommands() {
            /*
                java.lang.Class<com.sec.xmldata.support.Support$CommandFilter> r1 = com.sec.xmldata.support.Support.CommandFilter.class
                monitor-enter(r1)
                r2 = 0
                r3 = 0
                r4 = 0
                r5 = 0
                java.util.ArrayList<java.lang.String> r0 = filteredCommands     // Catch:{ all -> 0x0115 }
                if (r0 != 0) goto L_0x00fd
                java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ all -> 0x0115 }
                r0.<init>()     // Catch:{ all -> 0x0115 }
                filteredCommands = r0     // Catch:{ all -> 0x0115 }
                com.sec.xmldata.support.XMLDataStorage r0 = com.sec.xmldata.support.XMLDataStorage.instance()     // Catch:{ Exception -> 0x00f1 }
                java.lang.String r8 = "CommandFilter"
                org.w3c.dom.Element[] r0 = r0.getChildElementSet(r8)     // Catch:{ Exception -> 0x00f1 }
                if (r0 == 0) goto L_0x00d9
                int r8 = r0.length     // Catch:{ Exception -> 0x00f1 }
                r9 = r4
                r4 = r3
                r3 = r2
                r2 = 0
            L_0x0023:
                if (r2 >= r8) goto L_0x00d6
                r10 = r0[r2]     // Catch:{ Exception -> 0x00d2 }
                java.lang.String r11 = "enable"
                java.lang.String r11 = r10.getAttribute(r11)     // Catch:{ Exception -> 0x00d2 }
                boolean r11 = java.lang.Boolean.parseBoolean(r11)     // Catch:{ Exception -> 0x00d2 }
                r9 = r11
                java.lang.String r11 = "id"
                java.lang.String r11 = r10.getAttribute(r11)     // Catch:{ Exception -> 0x00d2 }
                r5 = r11
                if (r9 == 0) goto L_0x00ce
                java.lang.String r11 = r10.getNodeName()     // Catch:{ Exception -> 0x00d2 }
                java.lang.String r12 = "item-group"
                boolean r11 = r11.equals(r12)     // Catch:{ Exception -> 0x00d2 }
                if (r11 == 0) goto L_0x00ce
                org.w3c.dom.NodeList r11 = r10.getChildNodes()     // Catch:{ Exception -> 0x00d2 }
                r12 = r4
                r4 = r3
                r3 = 0
            L_0x004e:
                int r13 = r11.getLength()     // Catch:{ Exception -> 0x00cc }
                if (r3 >= r13) goto L_0x00c9
                org.w3c.dom.Node r13 = r11.item(r3)     // Catch:{ Exception -> 0x00cc }
                short r14 = r13.getNodeType()     // Catch:{ Exception -> 0x00cc }
                r15 = 1
                if (r14 != r15) goto L_0x00c6
                org.w3c.dom.NamedNodeMap r14 = r13.getAttributes()     // Catch:{ Exception -> 0x00cc }
                java.lang.String r15 = "para"
                org.w3c.dom.Node r14 = r14.getNamedItem(r15)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r14 = r14.getNodeValue()     // Catch:{ Exception -> 0x00cc }
                r4 = r14
                org.w3c.dom.NamedNodeMap r14 = r13.getAttributes()     // Catch:{ Exception -> 0x00cc }
                java.lang.String r15 = "count"
                org.w3c.dom.Node r14 = r14.getNamedItem(r15)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r14 = r14.getNodeValue()     // Catch:{ Exception -> 0x00cc }
                r12 = r14
                java.util.ArrayList<java.lang.String> r14 = filteredCommands     // Catch:{ Exception -> 0x00cc }
                java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cc }
                r15.<init>()     // Catch:{ Exception -> 0x00cc }
                r15.append(r5)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r7 = "/"
                r15.append(r7)     // Catch:{ Exception -> 0x00cc }
                r15.append(r4)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r7 = "/"
                r15.append(r7)     // Catch:{ Exception -> 0x00cc }
                r15.append(r12)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r7 = r15.toString()     // Catch:{ Exception -> 0x00cc }
                r14.add(r7)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r7 = "Support"
                java.lang.String r14 = "CommandFilter"
                java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00cc }
                r15.<init>()     // Catch:{ Exception -> 0x00cc }
                java.lang.String r6 = "filtername-"
                r15.append(r6)     // Catch:{ Exception -> 0x00cc }
                r15.append(r5)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r6 = "/"
                r15.append(r6)     // Catch:{ Exception -> 0x00cc }
                r15.append(r4)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r6 = "/"
                r15.append(r6)     // Catch:{ Exception -> 0x00cc }
                r15.append(r12)     // Catch:{ Exception -> 0x00cc }
                java.lang.String r6 = r15.toString()     // Catch:{ Exception -> 0x00cc }
                com.sec.xmldata.support.XmlUtil.log_d(r7, r14, r6)     // Catch:{ Exception -> 0x00cc }
            L_0x00c6:
                int r3 = r3 + 1
                goto L_0x004e
            L_0x00c9:
                r3 = r4
                r4 = r12
                goto L_0x00ce
            L_0x00cc:
                r0 = move-exception
                goto L_0x00f5
            L_0x00ce:
                int r2 = r2 + 1
                goto L_0x0023
            L_0x00d2:
                r0 = move-exception
                r12 = r4
                r4 = r3
                goto L_0x00f5
            L_0x00d6:
                r2 = r3
                r3 = r4
                r4 = r9
            L_0x00d9:
                java.util.ArrayList<java.lang.String> r0 = filteredCommands     // Catch:{ all -> 0x0115 }
                boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x0115 }
                if (r0 == 0) goto L_0x00e4
                monitor-exit(r1)
                r1 = 0
                return r1
            L_0x00e4:
                java.util.ArrayList<java.lang.String> r0 = filteredCommands     // Catch:{ all -> 0x0115 }
                r6 = 0
                java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ all -> 0x0115 }
                java.lang.Object[] r0 = r0.toArray(r6)     // Catch:{ all -> 0x0115 }
                java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ all -> 0x0115 }
                monitor-exit(r1)
                return r0
            L_0x00f1:
                r0 = move-exception
                r12 = r3
                r9 = r4
                r4 = r2
            L_0x00f5:
                r0.printStackTrace()     // Catch:{ all -> 0x0115 }
                r2 = 0
                filteredCommands = r2     // Catch:{ all -> 0x0115 }
                monitor-exit(r1)
                return r2
            L_0x00fd:
                java.util.ArrayList<java.lang.String> r0 = filteredCommands     // Catch:{ all -> 0x0115 }
                boolean r0 = r0.isEmpty()     // Catch:{ all -> 0x0115 }
                if (r0 != 0) goto L_0x0112
                java.util.ArrayList<java.lang.String> r0 = filteredCommands     // Catch:{ all -> 0x0115 }
                r6 = 0
                java.lang.String[] r6 = new java.lang.String[r6]     // Catch:{ all -> 0x0115 }
                java.lang.Object[] r0 = r0.toArray(r6)     // Catch:{ all -> 0x0115 }
                java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ all -> 0x0115 }
                monitor-exit(r1)
                return r0
            L_0x0112:
                monitor-exit(r1)
                r1 = 0
                return r1
            L_0x0115:
                r0 = move-exception
                monitor-exit(r1)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.xmldata.support.Support.CommandFilter.getFilteredCommands():java.lang.String[]");
        }
    }

    public static class FactoryTestMenu {
        public static String TAG = "FactoryTestMenu";

        public static String getFactoryTestMenuElemName() {
            String strFactoryTestMenu = TAG;
            if (Feature.getBoolean(Feature.SUPPORT_DUAL_LCD_FOLDER, false)) {
                if (!XmlUtil.isFolderOpen()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(strFactoryTestMenu);
                    sb.append("Sub");
                    strFactoryTestMenu = sb.toString();
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("strFactoryTestMenu : ");
                sb2.append(strFactoryTestMenu);
                XmlUtil.log_d(Support.CLASS_NAME, "getFactoryTestMenuElemName", sb2.toString());
            }
            return strFactoryTestMenu;
        }

        public static String[] getFactoryTestInfo(String XMLRootTAG) {
            TAG = XMLRootTAG;
            return getFactoryTestInfo();
        }

        public static String[] getFactoryTestInfo() {
            Element item;
            boolean parent_Enable;
            Element[] items;
            String str;
            String parent_ID;
            String parent_ID2;
            String parent_Name;
            String parent_ID3;
            String str2;
            String str3;
            String parent_Order;
            String str4;
            String str5;
            String parent_Name2;
            StringBuilder sb;
            String parent_Extra;
            String parent_NV;
            String str6;
            Element[] items2 = XMLDataStorage.instance().getChildElementSet(getFactoryTestMenuElemName());
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            String parent_ID4 = null;
            String parent_ID5 = null;
            String parent_Name3 = null;
            String parent_NV2 = null;
            String parent_Extra2 = null;
            String child_ID = null;
            String child_Name = null;
            String child_NV = null;
            String child_Extra = null;
            String child_Extra2 = null;
            if (items2 != null) {
                try {
                    int length = items2.length;
                    String child_ParentID = null;
                    String child_ParentID2 = null;
                    String child_Extra3 = null;
                    String child_NV2 = null;
                    String child_Name2 = null;
                    String child_ID2 = null;
                    String parent_Extra3 = null;
                    String parent_NV3 = null;
                    String parent_Name4 = null;
                    String parent_NV4 = null;
                    int i = 0;
                    while (i < length) {
                        try {
                            item = items2[i];
                            parent_Enable = Boolean.parseBoolean(item.getAttribute("enable"));
                            items = items2;
                            str = Support.CLASS_NAME;
                            parent_ID = parent_NV4;
                            parent_ID2 = "getFact";
                            parent_Name = parent_Name4;
                        } catch (Exception e) {
                            e = e;
                            Element[] elementArr = items2;
                            String str7 = parent_NV3;
                            String str8 = parent_Extra3;
                            String str9 = child_ID2;
                            String str10 = parent_NV4;
                            String str11 = parent_Name4;
                            e.printStackTrace();
                            return null;
                        }
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            String parent_NV5 = parent_NV3;
                            try {
                                sb2.append("name=");
                                sb2.append(item.getAttribute("name"));
                                sb2.append(", enable=");
                                sb2.append(parent_Enable);
                                XmlUtil.log_v(str, parent_ID2, sb2.toString());
                                if (parent_Enable) {
                                    String parent_Name5 = item.getAttribute("name");
                                    try {
                                        parent_ID3 = item.getAttribute("action");
                                    } catch (Exception e2) {
                                        e = e2;
                                        String str12 = parent_Name5;
                                        String str13 = parent_Extra3;
                                        String str14 = child_ID2;
                                        String str15 = parent_ID;
                                        e.printStackTrace();
                                        return null;
                                    }
                                    try {
                                        if (item.getAttribute("extra") != null) {
                                            try {
                                                str2 = item.getAttribute("extra");
                                            } catch (Exception e3) {
                                                e = e3;
                                                String str16 = parent_Name5;
                                                String parent_Order2 = child_ID2;
                                                e.printStackTrace();
                                                return null;
                                            }
                                        } else {
                                            str2 = "default";
                                        }
                                        String parent_Extra4 = str2;
                                        try {
                                            if (item.getAttribute("order").isEmpty()) {
                                                str3 = "9999";
                                            } else {
                                                str3 = item.getAttribute("order");
                                            }
                                            parent_Order = str3;
                                            str4 = Support.CLASS_NAME;
                                            str5 = "getFact";
                                            parent_Name2 = parent_Name5;
                                            try {
                                                sb = new StringBuilder();
                                                parent_Extra = parent_Extra4;
                                            } catch (Exception e4) {
                                                e = e4;
                                                String str17 = parent_Extra4;
                                                String str18 = parent_Order;
                                                e.printStackTrace();
                                                return null;
                                            }
                                        } catch (Exception e5) {
                                            e = e5;
                                            String str19 = parent_Name5;
                                            String str20 = parent_Extra4;
                                            String str21 = child_ID2;
                                            e.printStackTrace();
                                            return null;
                                        }
                                        try {
                                            sb.append("name=");
                                            sb.append(item.getAttribute("order").length());
                                            sb.append(" ");
                                            XmlUtil.log_v(str4, str5, sb.toString());
                                            if (item.getNodeName().equals("item-group")) {
                                                String parent_NV6 = "0x1F4";
                                                try {
                                                    NodeList childNodes = item.getChildNodes();
                                                    child_ID = child_Name2;
                                                    child_Name = child_NV2;
                                                    child_NV = child_Extra3;
                                                    child_Extra = child_ParentID2;
                                                    String child_ParentID3 = child_ParentID;
                                                    int i2 = 0;
                                                    while (true) {
                                                        parent_NV = parent_NV6;
                                                        try {
                                                            if (i2 >= childNodes.getLength()) {
                                                                break;
                                                            }
                                                            Node child = childNodes.item(i2);
                                                            NodeList childNodes2 = childNodes;
                                                            String parent_Order3 = parent_Order;
                                                            if (child.getNodeType() == 1) {
                                                                try {
                                                                    boolean child_Enable = Boolean.parseBoolean(((Element) child).getAttribute("enable"));
                                                                    if (child_Enable) {
                                                                        boolean z = child_Enable;
                                                                        child_Name = child.getAttributes().getNamedItem("name").getNodeValue();
                                                                        child_ID = child.getAttributes().getNamedItem("action").getNodeValue();
                                                                        child_NV = child.getAttributes().getNamedItem("nv").getNodeValue();
                                                                        if (((Element) child).getAttribute("extra") != null) {
                                                                            StringBuilder sb3 = new StringBuilder();
                                                                            Node node = child;
                                                                            sb3.append(((Element) child).getAttribute("extra"));
                                                                            sb3.append("|invisibility");
                                                                            str6 = sb3.toString();
                                                                        } else {
                                                                            str6 = "invisibility";
                                                                        }
                                                                        child_Extra = str6;
                                                                        child_ParentID3 = parent_ID3;
                                                                        StringBuilder sb4 = new StringBuilder();
                                                                        sb4.append(child_ID);
                                                                        sb4.append(",");
                                                                        sb4.append(child_Name);
                                                                        sb4.append(",");
                                                                        sb4.append(child_NV);
                                                                        sb4.append(",");
                                                                        sb4.append(child_Extra);
                                                                        sb4.append(",");
                                                                        sb4.append(child_ParentID3);
                                                                        sb4.append(",null,");
                                                                        arrayList2.add(sb4.toString());
                                                                    }
                                                                } catch (Exception e6) {
                                                                    e = e6;
                                                                    String str22 = child_ParentID3;
                                                                    String str23 = parent_NV;
                                                                    e.printStackTrace();
                                                                    return null;
                                                                }
                                                            }
                                                            i2++;
                                                            parent_NV6 = parent_NV;
                                                            childNodes = childNodes2;
                                                            parent_Order = parent_Order3;
                                                        } catch (Exception e7) {
                                                            e = e7;
                                                            String str24 = parent_Order;
                                                            String str25 = child_ParentID3;
                                                            String str26 = parent_NV;
                                                            String child_ParentID4 = child_Extra;
                                                            String child_Extra4 = child_NV;
                                                            String child_NV3 = child_Name;
                                                            String child_Name3 = child_ID;
                                                            e.printStackTrace();
                                                            return null;
                                                        }
                                                    }
                                                    String parent_Order4 = parent_Order;
                                                    if (arrayList2.isEmpty()) {
                                                        parent_Enable = false;
                                                    }
                                                    parent_NV4 = parent_ID3;
                                                    child_ParentID = child_ParentID3;
                                                    parent_Name4 = parent_Name2;
                                                    parent_Extra3 = parent_Extra;
                                                    parent_NV3 = parent_NV;
                                                    child_ParentID2 = child_Extra;
                                                    child_Extra3 = child_NV;
                                                    child_NV2 = child_Name;
                                                    child_Name2 = child_ID;
                                                    child_ID2 = parent_Order4;
                                                } catch (Exception e8) {
                                                    e = e8;
                                                    String str27 = parent_Order;
                                                    String str28 = parent_NV6;
                                                    e.printStackTrace();
                                                    return null;
                                                }
                                            } else {
                                                String parent_Order5 = parent_Order;
                                                try {
                                                    parent_NV3 = item.getAttribute("nv");
                                                    parent_Name4 = parent_Name2;
                                                    parent_Extra3 = parent_Extra;
                                                    child_ID2 = parent_Order5;
                                                    parent_NV4 = parent_ID3;
                                                } catch (Exception e9) {
                                                    e = e9;
                                                    e.printStackTrace();
                                                    return null;
                                                }
                                            }
                                        } catch (Exception e10) {
                                            e = e10;
                                            String str29 = parent_Order;
                                            e.printStackTrace();
                                            return null;
                                        }
                                    } catch (Exception e11) {
                                        e = e11;
                                        String str30 = parent_Name5;
                                        String str31 = parent_Extra3;
                                        String str32 = child_ID2;
                                        e.printStackTrace();
                                        return null;
                                    }
                                } else {
                                    parent_NV4 = parent_ID;
                                    parent_Name4 = parent_Name;
                                    parent_NV3 = parent_NV5;
                                }
                                if (parent_Enable) {
                                    try {
                                        StringBuilder sb5 = new StringBuilder();
                                        sb5.append(parent_NV4);
                                        Element element = item;
                                        sb5.append(",");
                                        sb5.append(parent_Name4);
                                        sb5.append(",");
                                        sb5.append(parent_NV3);
                                        sb5.append(",");
                                        sb5.append(parent_Extra3);
                                        sb5.append(",null,");
                                        sb5.append(child_ID2);
                                        sb5.append(",");
                                        arrayList.add(sb5.toString());
                                    } catch (Exception e12) {
                                        e = e12;
                                        String str33 = parent_NV4;
                                        String str34 = parent_Name4;
                                        String str35 = parent_NV3;
                                    }
                                }
                                i++;
                                items2 = items;
                            } catch (Exception e13) {
                                e = e13;
                                String str36 = parent_Extra3;
                                String str37 = child_ID2;
                                String str38 = parent_ID;
                                String str39 = parent_Name;
                                e.printStackTrace();
                                return null;
                            }
                        } catch (Exception e14) {
                            e = e14;
                            String str40 = parent_NV3;
                            String str41 = parent_Extra3;
                            String str42 = child_ID2;
                            String str43 = parent_ID;
                            String str44 = parent_Name;
                            e.printStackTrace();
                            return null;
                        }
                    }
                    String str45 = parent_NV3;
                    parent_NV2 = parent_Extra3;
                    parent_Extra2 = child_ID2;
                    child_ID = child_Name2;
                    child_Name = child_NV2;
                    child_NV = child_Extra3;
                    child_Extra = child_ParentID2;
                    child_Extra2 = child_ParentID;
                    parent_ID4 = parent_NV4;
                    parent_ID5 = parent_Name4;
                    parent_Name3 = str45;
                } catch (Exception e15) {
                    e = e15;
                    Element[] elementArr2 = items2;
                    e.printStackTrace();
                    return null;
                }
            }
            try {
                arrayList.addAll(arrayList2);
                return (String[]) arrayList.toArray(new String[0]);
            } catch (Exception e16) {
                e = e16;
                String str46 = parent_ID4;
                String str47 = parent_ID5;
                String str48 = parent_Name3;
                String str49 = parent_NV2;
                String str50 = parent_Extra2;
                String str51 = child_Extra2;
                e.printStackTrace();
                return null;
            }
        }

        public static boolean isExistFactoryTestItem(String action) {
            return getFactoryTestName(action, "dummy") == null;
        }

        public static String getFactoryTestName(String field, String value) {
            String name = XMLDataStorage.instance().getAttributeValueByAttribute(field, value, "name");
            StringBuilder sb = new StringBuilder();
            sb.append("name=");
            sb.append(name);
            XmlUtil.log_d(Support.CLASS_NAME, "FactoryTestMenu.getFactoryTestName", sb.toString());
            return name;
        }

        public static String getEnable(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "enable");
        }

        public static String getTestItemNv(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "nv");
        }

        public static String getTestCase(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "testcase");
        }

        public static String getTestName(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "testname");
        }

        public static String getOrientation(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "orientation");
        }

        public static float getUIRate(String action) {
            String rate = XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "uirate");
            if (rate == null || rate.isEmpty()) {
                return 0.0f;
            }
            return Float.parseFloat(rate);
        }

        public static int getLogLevel(String action) {
            String level = XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "loglevel");
            if (level == null || level.isEmpty()) {
                return 1;
            }
            return Integer.parseInt(level);
        }

        public static String getGammaPath(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "gammapath");
        }

        public static String getKeywordPath(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "keywordpath");
        }

        public static String getSupportMatrix(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "supportMatrix");
        }

        public static String getMargin(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "matrixMargin");
        }

        public static String getOuterRect(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "outerRect");
        }

        public static String getCrossRect(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "crossRect");
        }

        public static String getSupportDevice(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "supportDevice");
        }

        public static String getSpec(String action) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", action, "spec");
        }

        public static String getExtra(int itemId) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", String.valueOf(itemId), "extra");
        }

        public static String getExtra(String itemId) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", itemId, "extra");
        }

        public static String getCount(int itemId) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", String.valueOf(itemId), "count");
        }

        public static String getAudioPath(String itemId) {
            return XMLDataStorage.instance().getAttributeValueByAttribute("action", itemId, "audioPath");
        }
    }

    public static class Feature {
        public static final String ALWAYS_ON_MIC_MAIN_SUB_SWITCH = "ALWAYS_ON_MIC_MAIN_SUB_SWITCH";
        public static final String ANYWAY_JIG_CABLE_TYPE = "ANYWAY_JIG_CABLE_TYPE";
        public static final String ATD_RECONNECT_FROM_SSRM = "ATD_RECONNECT_FROM_SSRM";
        public static final String AUDIO_AMP_TYPE = "AUDIO_AMP_TYPE";
        public static final String AUDIO_PATH_OFF_WHEN_15_LOOPBACK = "AUDIO_PATH_OFF_WHEN_15_LOOPBACK";
        public static final String BATT_TYPE = "BATT_TYPE";
        public static final String BLOCK_HALLIC_IN_KEYTEST = "BLOCK_HALLIC_IN_KEYTEST";
        public static final String BOOT_CHECK_SMART_GLOW_LED = "BOOT_CHECK_SMART_GLOW_LED";
        public static final String BOOT_CHECK_TOUCHKEY_WO_SVCLED = "BOOT_CHECK_TOUCHKEY_WO_SVCLED";
        public static final String BOOT_COMPLETED_CHECK_FLASH_LED = "BOOT_COMPLETED_CHECK_FLASH_LED";
        public static final String CAL_AP_TEMP = "CAL_AP_TEMP";
        public static final String CHARGER_SUSPEND_MODE_ENABLE = "CHARGER_SUSPEND_MODE_ENABLE";
        public static final String CHIPSET_CP_MANUFACTURE = "CHIPSET_CP_MANUFACTURE";
        public static final String CHIPSET_MANUFACTURE = "CHIPSET_MANUFACTURE";
        public static final String CHIPSET_NAME = "CHIPSET_NAME";
        public static final String CHIPSET_NAME_CP = "CHIPSET_NAME_CP";
        public static final String CHIPSET_NAME_FORCIBLY = "CHIPSET_NAME_FORCIBLY";
        public static final String CONVERT_FROM_HOTPLUG_TO_HMP = "CONVERT_FROM_HOTPLUG_TO_HMP";
        public static final String DC_MOTER = "DC_MOTER";
        public static final String DEVICE_TYPE = "DEVICE_TYPE";
        public static final String DISABLE_OFFSET_IN_IRISPROX_TEST = "DISABLE_OFFSET_IN_IRISPROX_TEST";
        public static final String DISABLE_TOUCH_SOUND = "DISABLE_TOUCH_SOUND";
        public static final String DISABLE_TSP_SENSORHUB_FW_BUTTON = "DISABLE_TSP_SENSORHUB_FW_BUTTON";
        public static final String DISPLAY_INPUT_CURRENT_VERSION = "DISPLAY_INPUT_CURRENT_VERSION";
        public static final String ENABLE_JIG_B_SIGNAL = "ENABLE_JIG_B_SIGNAL";
        public static final String ENABLE_RCV_PLAY_START_DELAY = "ENABLE_RCV_PLAY_START_DELAY";
        public static final String ENABLE_SPK_AUDIOPATH_WRITE = "ENABLE_SPK_AUDIOPATH_WRITE";
        public static final String EPEN_SAVINGMODE_ENBLE = "EPEN_SAVINGMODE_ENBLE";
        public static final String FACTORY_TEST_MAIN_DIMMING_TIME_OUT = "FACTORY_TEST_MAIN_DIMMING_TIME_OUT";
        public static final String FACTORY_TEST_MAIN_LCD_OFF_TIME_OUT = "FACTORY_TEST_MAIN_LCD_OFF_TIME_OUT";
        public static final String FAILHIST_VERSION = "FAILHIST_VERSION";
        public static final String FEATURE_ENABLE_DYNAMIC_GRIP = "FEATURE_ENABLE_DYNAMIC_GRIP";
        public static final String FEATURE_ENABLE_DYNAMIC_MULTI_SIM = "FEATURE_ENABLE_DYNAMIC_MULTI_SIM";
        public static final String FEATURE_ENABLE_DYNAMIC_NFC = "FEATURE_ENABLE_DYNAMIC_NFC";
        public static final String FEATURE_ENABLE_GRIP_CAL_BUTTON_DECTECTED_HALLIC = "FEATURE_ENABLE_GRIP_CAL_BUTTON_DECTECTED_HALLIC";
        public static final String FINGERPRINT_EGISTEC_SUPPORT_NEWITEMS = "FINGERPRINT_EGISTEC_SUPPORT_NEWITEMS";
        public static final String FONT_SIZE = "FONT_SIZE";
        public static final String FORCED_PACKET_EAR_LOOPBACK_TYPE = "FORCED_PACKET_EAR_LOOPBACK_TYPE";
        public static final String FORCE_ENABLE_TSP_SELFTEST = "FORCE_ENABLE_TSP_SELFTEST";
        public static final String FORCE_HRM_CLOUD_UV_IR_RED_TEST = "FORCE_HRM_CLOUD_UV_IR_RED_TEST";
        public static final String FORCE_HRM_IR_RED_TEST = "FORCE_HRM_IR_RED_TEST";
        public static final String FORCE_LANDSCAPE_MODE = "FORCE_LANDSCAPE_MODE";
        public static final String FRONT_CAMERA_TYPE = "FRONT_CAMERA_TYPE";
        public static final String FTA_HW_VER = "FTA_HW_VER";
        public static final String FTA_SW_VER = "FTA_SW_VER";
        public static final String GRIPSENSOR_2ND_TYPE = "GRIPSENSOR_2ND_TYPE";
        public static final String GRIPSENSOR_TYPE = "GRIPSENSOR_TYPE";
        public static final String GRIPSENSOR_UI_OLD_CONCEPT = "GRIPSENSOR_UI_OLD_CONCEPT";
        public static final String GRIPSENS_DELAY_BEFORE_MODE_CHECK = "GRIPSENS_DELAY_BEFORE_MODE_CHECK";
        public static final String GRIP_SENSOR_CHANNEL_SEPARATION = "GRIP_SENSOR_CHANNEL_SEPARATION";
        public static final String GRIP_SENSOR_FONT_SIZE = "GRIP_SENSOR_FONT_SIZE";
        public static final String GRIP_SENSOR_IC_A96T3X6 = "GRIP_SENSOR_IC_A96T3X6";
        public static final String GRIP_SENSOR_IC_TC3XXK = "GRIP_SENSOR_IC_TC3XXK";
        public static final String GRIP_SENSOR_LIMB_BODY = "GRIP_SENSOR_LIMB_BODY";
        public static final String GRIP_TEST_DISABLE_TOTALCAP_VALUE = "GRIP_TEST_DISABLE_TOTALCAP_VALUE";
        public static final String GRIP_TEST_UI_NEW_CONCEPT = "GRIP_TEST_UI_NEW_CONCEPT";
        public static final String GRIP_TEST_UI_OLD_CONCEPT = "GRIP_TEST_UI_OLD_CONCEPT";
        public static final String GRIP_TOUCHIC_ENABLE = "GRIP_TOUCHIC_ENABLE";
        public static final String HALLIC_WITH_SENSORHUB = "HALLIC_WITH_SENSORHUB";
        public static final String HEXA_CORE_ACTIVE_MARK = "HEXA_CORE_ACTIVE_MARK";
        public static final String HRM_NEW_EOL_TEST = "HRM_NEW_EOL_TEST";
        public static final String HW_VER_EFS = "HW_VER_EFS";
        public static final String HW_VER_EFS_NO_ANSWER = "HW_VER_EFS_NO_ANSWER";
        public static final String IGNORE_EARPHONE_IN_ECHO_TEST = "IGNORE_EARPHONE_IN_ECHO_TEST";
        public static final String INBATT_SAVE_SOC = "INBATT_SAVE_SOC";
        public static final String IRLED_CONCEPT = "IRLED_CONCEPT";
        public static final String IS_AUTOBRIGHTNESS = "IS_AUTOBRIGHTNESS";
        public static final String IS_DBLC_FUNCTION = "IS_DBLC_FUNCTION";
        public static final String IS_DUALSTANDBY = "IS_DUALSTANDBY";
        public static final String IS_DUAL_CLOCK_AUDIO = "IS_DUAL_CLOCK_AUDIO";
        public static final String IS_DUMMYKEY_FACTORY_MODE = "IS_DUMMYKEY_FACTORY_MODE";
        public static final String IS_FIVE_KEY = "IS_FIVE_KEY";
        public static final String IS_FOUR_KEY = "IS_FOUR_KEY";
        public static final String IS_HIDDEN_HOLE_NO_PROXIMITY = "IS_HIDDEN_HOLE_NO_PROXIMITY";
        public static final String IS_INNER_OUTER_TOUCHKEY = "IS_INNER_OUTER_TOUCHKEY";
        public static final String IS_LIGHT_SENSOR_LEVEL5 = "IS_LIGHT_SENSOR_LEVEL5";
        public static final String IS_MULTI_SIM_FRAMEWORK = "IS_MULTI_SIM_FRAMEWORK";
        public static final String IS_PROXIMITY_AUTO_CALIBRATION = "IS_PROXIMITY_AUTO_CALIBRATION";
        public static final String IS_PROXIOFFSET_JB_CONCEPT = "IS_PROXIOFFSET_JB_CONCEPT";
        public static final String IS_QUALCOMM_SSC = "IS_QUALCOMM_SSC";
        public static final String IS_SEPARATE_IRIS_PROXIMITY_SENSOR = "IS_SEPARATE_IRIS_PROXIMITY_SENSOR";
        public static final String IS_SIX_KEY = "IS_SIX_KEY";
        public static final String IS_TOUCHKEY_GRAPH = "IS_TOUCHKEY_GRAPH";
        public static final String IS_TSP_SUPPORT_CONFIG_VERSION = "IS_TSP_SUPPORT_CONFIG_VERSION";
        public static final String IS_USING_2CPS = "IS_USING_2CPS";
        public static final String IS_VIBRATION_FINISH_BY_TOUCH = "IS_VIBRATION_FINISH_BY_TOUCH";
        public static final String JIG_CHECK = "JIG_CHECK";
        public static final String KEYTEST_VOLUME_INDEX = "KEYTEST_VOLUME_INDEX";
        public static final String KEY_LED_BRIGHTNESS_FOR_FAC = "KEY_LED_BRIGHTNESS_FOR_FAC";
        public static final String KEY_TEST_AUDIO_PATH_OFF = "KEY_TEST_AUDIO_PATH_OFF";
        public static final String LCD_FACING_UNDERSIDE_SMDFT = "LCD_FACING_UNDERSIDE_SMDFT";
        public static final String LCD_TYPE = "LCD_TYPE";
        public static final String LEAKAGE_CURRENT_NEED_PASS_BTN = "LEAKAGE_CURRENT_NEED_PASS_BTN";
        public static final String LIVE_DEMO_MODEL = "LIVE_DEMO_MODEL";
        public static final String LOOPTEST_COMMAND_DROP_COUNT = "LOOPTEST_COMMAND_DROP_COUNT";
        public static final String LOOPTEST_COMMAND_DUALMIC_EAR = "LOOPTEST_COMMAND_DUALMIC_EAR";
        public static final String MAGNETIC_ROTATE_DEGREE = "MAGNETIC_ROTATE_DEGREE";
        public static final String MIC_COUNT = "MIC_COUNT";
        public static final String MODEL_COMMUNICATION_MODE = "MODEL_COMMUNICATION_MODE";
        public static final String MODEL_NAME = "MODEL_NAME";
        public static final String MODEL_NUMBER = "MODEL_NUMBER";
        public static final String MULTICORE_THERMISTER_CELCIUS_DIVIDER = "MULTICORE_THERMISTER_CELCIUS_DIVIDER";
        public static final String MULTI_TSK_THD = "MULTI_TSK_THD";
        public static final String NEED_CAMDRIVER_OPEN = "NEED_CAMDRIVER_OPEN";
        public static final String NEED_CAMDRIVER_OPEN_FRONT = "NEED_CAMDRIVER_OPEN_FRONT";
        public static final String NEED_CURRENT_HRM_EOL = "NEED_CURRENT_HRM_EOL";
        public static final String NEED_IMEM_CHECK_DEVICESIZE = "NEED_IMEM_CHECK_DEVICESIZE";
        public static final String NEED_LPA_MODE_SET = "NEED_LPA_MODE_SET";
        public static final String NEED_LPM_MODE_SET = "NEED_LPM_MODE_SET";
        public static final String NEED_NOTI_AUDIO_MANAGER = "NEED_NOTI_AUDIO_MANAGER";
        public static final String NOT_USING_STEREO_IN_SPKMIC1_RMS = "NOT_USING_STEREO_IN_SPKMIC1_RMS";
        public static final String NUMBER_OF_AP_CORE = "NUMBER_OF_AP_CORE";
        public static final String NUMBER_OF_AP_THERMISTOR = "NUMBER_OF_AP_THERMISTOR";
        public static final String OCTA_CORE_ACTIVE_MARK = "OCTA_CORE_ACTIVE_MARK";
        public static final String OCTA_QC_CORE_ACTIVE_MARK = "OCTA_QC_CORE_ACTIVE_MARK";
        public static final String PANEL_TYPE = "PANEL_TYPE";
        public static final String PROX_CHECK_INT_SUPPORT = "PROX_CHECK_INT_SUPPORT";
        public static final String PROX_SENSOR_NOT_SUPPORT_ADC = "PROX_SENSOR_NOT_SUPPORT_ADC";
        public static final String QUAD_CORE_ACTIVE_MARK = "QUAD_CORE_ACTIVE_MARK";
        public static final String RAM_SIZE_IF = "RAM_SIZE_IF";
        public static final String REAR_CAMERA_TYPE = "REAR_CAMERA_TYPE";
        public static final String REAR_FLASH_COUNT = "REAR_FLASH_COUNT";
        public static final String SELFTEST_MENU_LIST = "SELFTEST_MENU_LIST";
        public static final String SEMI_FUNCTION_FONT_SIZE = "SEMI_FUNCTION_FONT_SIZE";
        public static final String SEMI_FUNCTION_TEST_UI_ORIENTATION = "SEMI_FUNCTION_TEST_UI_ORIENTATION";
        public static final String SENSOR_MANAGER = "SENSOR_MANAGER";
        public static final String SENSOR_NAME_ACCELEROMETER = "SENSOR_NAME_ACCELEROMETER";
        public static final String SENSOR_NAME_ACCELEROMETER_2ND = "SENSOR_NAME_ACCELEROMETER_2ND";
        public static final String SENSOR_NAME_GESTURE = "SENSOR_NAME_GESTURE";
        public static final String SENSOR_NAME_GYROSCOPE = "SENSOR_NAME_GYROSCOPE";
        public static final String SENSOR_NAME_GYROSCOPE_2ND = "SENSOR_NAME_GYROSCOPE_2ND";
        public static final String SENSOR_NAME_LIGHT = "SENSOR_NAME_LIGHT";
        public static final String SENSOR_NAME_MAGNETIC = "SENSOR_NAME_MAGNETIC";
        public static final String SENSOR_NAME_MAGNETIC_2ND = "SENSOR_NAME_MAGNETIC_2ND";
        public static final String SENSOR_NAME_PROXIMITY = "SENSOR_NAME_PROXIMITY";
        public static final String SENSOR_NAME_UV = "SENSOR_NAME_UV";
        public static final String SEPARATE_SPK_PATH = "SEPARATE_SPK_PATH";
        public static final String SETUPWIZARD_INDEX_LIST = "SETUPWIZARD_INDEX_LIST";
        public static final String SET_ACCELEROMETER_ROTATION = "SET_ACCELEROMETER_ROTATION";
        public static final String SET_FOREGROUND_BEFORE_FTC = "SET_FOREGROUND_BEFORE_FTC";
        public static final String SKIP_TRANSITION_ANIMATION_SCALE = "SKIP_TRANSITION_ANIMATION_SCALE";
        public static final String SKIP_Z_AXIS_ACCELERATION = "SKIP_Z_AXIS_ACCELERATION";
        public static final String SPEAKER_COUNT = "SPEAKER_COUNT";
        public static final String SPEAKER_RESOURCE_ADD_6DB = "SPEAKER_RESOURCE_ADD_6DB";
        public static final String SPK_TEST_CHECK_FINGER_SENSOR_CONNECTION = "SPK_TEST_CHECK_FINGER_SENSOR_CONNECTION";
        public static final String STANDARD_JB_SYSFS = "STANDARD_JB_SYSFS";
        public static final String START_NAD_AFTER_ERASE = "START_NAD_AFTER_ERASE";
        public static final String SUBKEY_DIFFERENT_VALUE = "SUBKEY_DIFFERENT_VALUE";
        public static final String SUBKEY_FIVE_KEY = "SUBKEY_FIVE_KEY";
        public static final String SUBKEY_FOUR_KEY = "SUBKEY_FOUR_KEY";
        public static final String SUBKEY_SIX_KEY = "SUBKEY_SIX_KEY";
        public static final String SUPPORT_15TEST_STATUS_PROPERTY = "SUPPORT_15TEST_STATUS_PROPERTY";
        public static final String SUPPORT_15_QUICK_TEST = "SUPPORT_15_QUICK_TEST";
        public static final String SUPPORT_2LINE_STATUS_BAR = "SUPPORT_2LINE_STATUS_BAR";
        public static final String SUPPORT_2ND_CP = "SUPPORT_2ND_CP";
        public static final String SUPPORT_2ND_RIL = "SUPPORT_2ND_RIL";
        public static final String SUPPORT_3X4_KEY = "SUPPORT_3X4_KEY";
        public static final String SUPPORT_ADD_TSP_ENTRY = "SUPPORT_ADD_TSP_ENTRY";
        public static final String SUPPORT_AFCHARGE_TOAST_TEST = "SUPPORT_AFCHARGE_TOAST_TEST";
        public static final String SUPPORT_APCHIP_DATA_EXYNOS = "SUPPORT_APCHIP_DATA_EXYNOS";
        public static final String SUPPORT_AP_EX_THERM_ADC = "SUPPORT_AP_EX_THERM_ADC";
        public static final String SUPPORT_ASSEMBLED_TSP_TSK = "SUPPORT_ASSEMBLED_TSP_TSK";
        public static final String SUPPORT_AT_NCAMTEST = "SUPPORT_AT_NCAMTEST";
        public static final String SUPPORT_AUDIO_NEW_CIRRUS = "SUPPORT_AUDIO_NEW_CIRRUS";
        public static final String SUPPORT_AUDIO_NEW_NXP = "SUPPORT_AUDIO_NEW_NXP";
        public static final String SUPPORT_BARCODE_EMULATOR = "SUPPORT_BARCODE_EMULATOR";
        public static final String SUPPORT_BATTTYPE_CHECK = "SUPPORT_BATTTYPE_CHECK";
        public static final String SUPPORT_BOOK_COVER = "SUPPORT_BOOK_COVER";
        public static final String SUPPORT_BOOST_MEDIASCAN = "SUPPORT_BOOST_MEDIASCAN";
        public static final String SUPPORT_BOOTING_CHECK = "SUPPORT_BOOTING_CHECK";
        public static final String SUPPORT_BT_FINAL_MODE = "SUPPORT_BT_FINAL_MODE";
        public static final String SUPPORT_CABLE_CONNECT_DETECT = "SUPPORT_CABLE_CONNECT_DETECT";
        public static final String SUPPORT_CHECKING_GHOST_TSK = "SUPPORT_CHECKING_GHOST_TSK";
        public static final String SUPPORT_CHECK_SDCARD_TRAY = "SUPPORT_CHECK_SDCARD_TRAY";
        public static final String SUPPORT_CHG_DET = "SUPPORT_CHG_DET";
        public static final String SUPPORT_CKD_TEST = "SUPPORT_CKD_TEST";
        public static final String SUPPORT_CNLABEL_TEST = "SUPPORT_CNLABEL_TEST";
        public static final String SUPPORT_CP_DATA_DISPLAY = "SUPPORT_CP_DATA_DISPLAY";
        public static final String SUPPORT_CX_DATA_ALL_DISPLAY = "SUPPORT_CX_DATA_ALL_DISPLAY";
        public static final String SUPPORT_CX_DATA_GAP_DISPLAY = "SUPPORT_CX_DATA_GAP_DISPLAY";
        public static final String SUPPORT_DEADZONE_ON_GRIP = "SUPPORT_DEADZONE_ON_GRIP";
        public static final String SUPPORT_DELAY_PREVOLTAGE_CHECK = "SUPPORT_DELAY_PREVOLTAGE_CHECK";
        public static final String SUPPORT_DIFFERENT_SYSFS_PDCHARGE = "SUPPORT_DIFFERENT_SYSFS_PDCHARGE";
        public static final String SUPPORT_DISABLE_SCROLLING_CACHE = "SUPPORT_DISABLE_SCROLLING_CACHE";
        public static final String SUPPORT_DISABLE_SEMICAMERA_FLASH = "SUPPORT_DISABLE_SEMICAMERA_FLASH";
        public static final String SUPPORT_DISABLE_TSP_DEADZONE = "SUPPORT_DISABLE_TSP_DEADZONE";
        public static final String SUPPORT_DP_PRESENTATION_TEST = "SUPPORT_DP_PRESENTATION_TEST";
        public static final String SUPPORT_DUALMODE = "SUPPORT_DUALMODE";
        public static final String SUPPORT_DUALMODE_MARVELL = "SUPPORT_DUALMODE_MARVELL";
        public static final String SUPPORT_DUAL_EDGE = "SUPPORT_DUAL_EDGE";
        public static final String SUPPORT_DUAL_FLASH_LED = "SUPPORT_DUAL_FLASH_LED";
        public static final String SUPPORT_DUAL_LCD_FOLDER = "SUPPORT_DUAL_LCD_FOLDER";
        public static final String SUPPORT_DUAL_MOTOR = "SUPPORT_DUAL_MOTOR";
        public static final String SUPPORT_DUAL_STANBY = "SUPPORT_DUAL_STANBY";
        public static final String SUPPORT_DUAL_STANBY_ONE_CP = "SUPPORT_DUAL_STANBY_ONE_CP";
        public static final String SUPPORT_DUMMY_KEY_FOR_RECENT = "SUPPORT_DUMMY_KEY_FOR_RECENT";
        public static final String SUPPORT_ECHO_PLAY_MUTE = "SUPPORT_ECHO_PLAY_MUTE";
        public static final String SUPPORT_ECHO_SMALL_BUFFER = "SUPPORT_ECHO_SMALL_BUFFER";
        public static final String SUPPORT_ECHO_TEST_JIG_CHECK = "SUPPORT_ECHO_TEST_JIG_CHECK";
        public static final String SUPPORT_ECHO_TEST_JIG_CHECK_PROX = "SUPPORT_ECHO_TEST_JIG_CHECK_PROX";
        public static final String SUPPORT_ECHO_TEST_LEFT_ONLY = "SUPPORT_ECHO_TEST_LEFT_ONLY";
        public static final String SUPPORT_ECHO_TEST_NOSOUND = "SUPPORT_ECHO_TEST_NOSOUND";
        public static final String SUPPORT_ECHO_TEST_NOSOUND_SET_PATH = "SUPPORT_ECHO_TEST_NOSOUND_SET_PATH";
        public static final String SUPPORT_ECHO_TEST_RIGHT_ONLY = "SUPPORT_ECHO_TEST_RIGHT_ONLY";
        public static final String SUPPORT_ENABLE_IRISBTN_IN_AUTOCAL = "SUPPORT_ENABLE_IRISBTN_IN_AUTOCAL";
        public static final String SUPPORT_ENABLE_TSP_DEADZONE = "SUPPORT_ENABLE_TSP_DEADZONE";
        public static final String SUPPORT_EPEN = "SUPPORT_EPEN";
        public static final String SUPPORT_EPEN_INSERT_SYSFS = "SUPPORT_EPEN_INSERT_SYSFS";
        public static final String SUPPORT_EPEN_WITHOUT_INSERT = "SUPPORT_EPEN_WITHOUT_INSERT";
        public static final String SUPPORT_EXIT_15_WHEN_NFC_IS_NOT_READY = "SUPPORT_EXIT_15_WHEN_NFC_IS_NOT_READY";
        public static final String SUPPORT_FINAL_LTE = "SUPPORT_FINAL_LTE";
        public static final String SUPPORT_FINAL_TD_LTE = "SUPPORT_FINAL_TD_LTE";
        public static final String SUPPORT_FINAL_TD_MASTER_SLAVE = "SUPPORT_FINAL_TD_MASTER_SLAVE";
        public static final String SUPPORT_FLASH_LED_THERM = "SUPPORT_FLASH_LED_THERM";
        public static final String SUPPORT_FM_RADIO = "SUPPORT_FM_RADIO";
        public static final String SUPPORT_FRONT_CAMERA = "SUPPORT_FRONT_CAMERA";
        public static final String SUPPORT_GALILEO_FOR_GPS = "SUPPORT_GALILEO_FOR_GPS";
        public static final String SUPPORT_GLOVE_MODE = "SUPPORT_GLOVE_MODE";
        public static final String SUPPORT_GPU_TEST = "SUPPORT_GPU_TEST";
        public static final String SUPPORT_GRAPHIC_ACCLETOR = "SUPPORT_GRAPHIC_ACCLETOR";
        public static final String SUPPORT_GRIPSENSOR_TEMP_CAL = "SUPPORT_GRIPSENSOR_TEMP_CAL";
        public static final String SUPPORT_GRIPSENS_ALWAYS_ON = "SUPPORT_GRIPSENS_ALWAYS_ON";
        public static final String SUPPORT_GRIP_SENSOR_FW_UPDATE_INTERNAL = "SUPPORT_GRIP_SENSOR_FW_UPDATE_INTERNAL";
        public static final String SUPPORT_HARDKEY_LIGHT_MODE = "SUPPORT_HARDKEY_LIGHT_MODE";
        public static final String SUPPORT_HARDWARE_ACCELERATED = "SUPPORT_HARDWARE_ACCELERATED";
        public static final String SUPPORT_HBM_MODE_TEST = "SUPPORT_HBM_MODE_TEST";
        public static final String SUPPORT_HDMI_16_10_RATIO = "SUPPORT_HDMI_16_10_RATIO";
        public static final String SUPPORT_HDMI_43_RATIO = "SUPPORT_HDMI_43_RATIO";
        public static final String SUPPORT_HEXA_CORE = "SUPPORT_HEXA_CORE";
        public static final String SUPPORT_HMT_USB_FTCLIENT = "SUPPORT_HMT_USB_FTCLIENT";
        public static final String SUPPORT_HRM_CONCEPT_ONCE = "SUPPORT_HRM_CONCEPT_ONCE";
        public static final String SUPPORT_HRM_R = "SUPPORT_HRM_R";
        public static final String SUPPORT_HRM_SPO2 = "SUPPORT_HRM_SPO2";
        public static final String SUPPORT_HYBRID_DUAL_SOCKET_VERSION = "SUPPORT_HYBRID_DUAL_SOCKET_VERSION";
        public static final String SUPPORT_IDCHIPTT_OLD_CONCEPT = "SUPPORT_IDCHIPTT_OLD_CONCEPT";
        public static final String SUPPORT_IDT = "SUPPORT_IDT";
        public static final String SUPPORT_IMAGIS_WET_MODE = "SUPPORT_IMAGIS_WET_MODE";
        public static final String SUPPORT_INTERFERENCE_METRIC_DATA = "SUPPORT_INTERFERENCE_METRIC_DATA";
        public static final String SUPPORT_IN_BATTERY_OCV = "SUPPORT_IN_BATTERY_OCV";
        public static final String SUPPORT_IRIS_CAMERA = "SUPPORT_IRIS_CAMERA";
        public static final String SUPPORT_IRIS_FOR_AADHAR_ONLY = "SUPPORT_IRIS_FOR_AADHAR_ONLY";
        public static final String SUPPORT_IRLED_CONCEPT = "SUPPORT_IRLED_CONCEPT";
        public static final String SUPPORT_IRLED_FEEDBACK_IC = "SUPPORT_IRLED_FEEDBACK_IC";
        public static final String SUPPORT_IRLED_REPEAT_MODE = "SUPPORT_IRLED_REPEAT_MODE";
        public static final String SUPPORT_JIG_ON = "SUPPORT_JIG_ON";
        public static final String SUPPORT_JITTER = "SUPPORT_JITTER";
        public static final String SUPPORT_KEYTEST_HOME_HARDKEY_SEPARATION = "SUPPORT_KEYTEST_HOME_HARDKEY_SEPARATION";
        public static final String SUPPORT_KEYTEST_POWER_KEY_NAMED_HOLD = "SUPPORT_KEYTEST_POWER_KEY_NAMED_HOLD";
        public static final String SUPPORT_LCD_FAST_DISCHARGE = "SUPPORT_LCD_FAST_DISCHARGE";
        public static final String SUPPORT_LIGHT_CCT = "SUPPORT_LIGHT_CCT";
        public static final String SUPPORT_LOCK_CMD = "SUPPORT_LOCK_CMD";
        public static final String SUPPORT_LTE = "SUPPORT_LTE";
        public static final String SUPPORT_MAIN_NAD = "SUPPORT_MAIN_NAD";
        public static final String SUPPORT_MESSAGE_KEY_FOR_DFT = "SUPPORT_MESSAGE_KEY_FOR_DFT";
        public static final String SUPPORT_MIC2_ECHO_TEST = "SUPPORT_MIC2_ECHO_TEST";
        public static final String SUPPORT_MIS_CALIBRATION = "SUPPORT_MIS_CALIBRATION";
        public static final String SUPPORT_MOOD_LED = "SUPPORT_MOOD_LED";
        public static final String SUPPORT_MULTI_THERMISTOR = "SUPPORT_MULTI_THERMISTOR";
        public static final String SUPPORT_NAD_TEST = "SUPPORT_NAD_TEST";
        public static final String SUPPORT_NCR = "SUPPORT_NCR";
        public static final String SUPPORT_NFC_SELF_CAL_APP = "SUPPORT_NFC_SELF_CAL_APP";
        public static final String SUPPORT_NO_GPIO_SSDS_SIMPLE_VERSION = "SUPPORT_NO_GPIO_SSDS_SIMPLE_VERSION";
        public static final String SUPPORT_OCTA_CORE = "SUPPORT_OCTA_CORE";
        public static final String SUPPORT_OCTA_SPI_SELF_MASK = "SUPPORT_OCTA_SPI_SELF_MASK";
        public static final String SUPPORT_ONETIME_INFO_FOR_MF = "SUPPORT_ONETIME_INFO_FOR_MF";
        public static final String SUPPORT_ONE_BINARY_FOR_DYNAMIC_NFC = "SUPPORT_ONE_BINARY_FOR_DYNAMIC_NFC";
        public static final String SUPPORT_ONE_CP_VERSION = "SUPPORT_ONE_CP_VERSION";
        public static final String SUPPORT_OUTSIDE_MODE_TEST = "SUPPORT_OUTSIDE_MODE_TEST";
        public static final String SUPPORT_PALM_TOUCH = "SUPPORT_PALM_TOUCH";
        public static final String SUPPORT_PARTIAL_WAKELOCK = "SUPPORT_PARTIAL_WAKELOCK";
        public static final String SUPPORT_PBA = "SUPPORT_PBA";
        public static final String SUPPORT_PIEZO_MOTOR = "SUPPORT_PIEZO_MOTOR";
        public static final String SUPPORT_POC_TEST = "SUPPORT_POC_TEST";
        public static final String SUPPORT_PRESSURE_TOUCH = "SUPPORT_PRESSURE_TOUCH";
        public static final String SUPPORT_PREWATERPROOF_TEST = "SUPPORT_PREWATERPROOF_TEST";
        public static final String SUPPORT_PROXIMITY_TRIM_CHANGE = "SUPPORT_PROXIMITY_TRIM_CHANGE";
        public static final String SUPPORT_QC_HEXA_CORE = "SUPPORT_QC_HEXA_CORE";
        public static final String SUPPORT_QC_HMAC_PROP = "SUPPORT_QC_HMAC_PROP";
        public static final String SUPPORT_QC_OCTA_CORE = "SUPPORT_QC_OCTA_CORE";
        public static final String SUPPORT_QUAD_CORE = "SUPPORT_QUAD_CORE";
        public static final String SUPPORT_QUICK_READING_CX_DATA = "SUPPORT_QUICK_READING_CX_DATA";
        public static final String SUPPORT_QWERTY_KEY = "SUPPORT_QWERTY_KEY";
        public static final String SUPPORT_RCV_AS_PSEUDO_SPK = "SUPPORT_RCV_AS_PSEUDO_SPK";
        public static final String SUPPORT_RCV_SPK_COMBINANTION = "SUPPORT_RCV_SPK_COMBINANTION";
        public static final String SUPPORT_REALTIME_TYPE_FOR_WIFI = "SUPPORT_REALTIME_TYPE_FOR_WIFI";
        public static final String SUPPORT_REAR_CAMERA = "SUPPORT_REAR_CAMERA";
        public static final String SUPPORT_REAR_CAM_OIS = "SUPPORT_REAR_CAM_OIS";
        public static final String SUPPORT_REFERENCE_MAX_DIFF_DISPLAY = "SUPPORT_REFERENCE_MAX_DIFF_DISPLAY";
        public static final String SUPPORT_REF_VALUE = "SUPPORT_REF_VALUE";
        public static final String SUPPORT_REORDER_MFT_LIST = "SUPPORT_REORDER_MFT_LIST";
        public static final String SUPPORT_RING_BUFFER_MODE = "SUPPORT_RING_BUFFER_MODE";
        public static final String SUPPORT_RUN_REF = "SUPPORT_RUN_REF";
        public static final String SUPPORT_SELFTEST_PWC_DISPLAY = "SUPPORT_SELFTEST_PWC_DISPLAY";
        public static final String SUPPORT_SELF_PRE_DELTA_DISPLAY = "SUPPORT_SELF_PRE_DELTA_DISPLAY";
        public static final String SUPPORT_SEMI_FUNCTION_TEST = "SUPPORT_SEMI_FUNCTION_TEST";
        public static final String SUPPORT_SEMI_MAXCLOCK_LOCK = "SUPPORT_SEMI_MAXCLOCK_LOCK";
        public static final String SUPPORT_SENSORHUB = "SUPPORT_SENSORHUB";
        public static final String SUPPORT_SEPARATED_RECORD_PATH = "SUPPORT_SEPARATED_RECORD_PATH";
        public static final String SUPPORT_SHOW_MD05_REGISTER = "SUPPORT_SHOW_MD05_REGISTER";
        public static final String SUPPORT_SICD_OFF = "SUPPORT_SICD_OFF";
        public static final String SUPPORT_SKIP_GHOST_TSK = "SUPPORT_SKIP_GHOST_TSK";
        public static final String SUPPORT_SKIP_GHOST_TSK_SEMI = "SUPPORT_SKIP_GHOST_TSK_SEMI";
        public static final String SUPPORT_SKIP_RID_CHECK = "SUPPORT_SKIP_RID_CHECK";
        public static final String SUPPORT_SKIP_TSP_SELFTEST = "SUPPORT_SKIP_TSP_SELFTEST";
        public static final String SUPPORT_SPEN_BLE_TEST = "SUPPORT_SPEN_BLE_TEST";
        public static final String SUPPORT_SPK_ON_ENTER_SEMI = "SUPPORT_SPK_ON_ENTER_SEMI";
        public static final String SUPPORT_STATUS_BAR_MARGIN_LR = "SUPPORT_STATUS_BAR_MARGIN_LR";
        public static final String SUPPORT_STATUS_BAR_MARGIN_UD = "SUPPORT_STATUS_BAR_MARGIN_UD";
        public static final String SUPPORT_STM_TOUCHKEY_DISPLAY = "SUPPORT_STM_TOUCHKEY_DISPLAY";
        public static final String SUPPORT_SUB_TSK = "SUPPORT_SUB_TSK";
        public static final String SUPPORT_SUB_TSP = "SUPPORT_SUB_TSP";
        public static final String SUPPORT_THIRD_STATUS_BAR = "SUPPORT_THIRD_STATUS_BAR";
        public static final String SUPPORT_TOUCHKEY_CX2_DISPLAY = "SUPPORT_TOUCHKEY_CX2_DISPLAY";
        public static final String SUPPORT_TOUCHKEY_IC_ON = "SUPPORT_TOUCHKEY_IC_ON";
        public static final String SUPPORT_TOUCHKEY_RAW_DATA_DISPLAY = "SUPPORT_TOUCHKEY_RAW_DATA_DISPLAY";
        public static final String SUPPORT_TSK_BACKLIGHT_TURN_OFF = "SUPPORT_TSK_BACKLIGHT_TURN_OFF";
        public static final String SUPPORT_TSK_FW_UPDATE_INTERNAL = "SUPPORT_TSK_FW_UPDATE_INTERNAL";
        public static final String SUPPORT_TSK_FW_UPDATE_INTERNAL_FOR_H = "SUPPORT_TSK_FW_UPDATE_INTERNAL_FOR_H";
        public static final String SUPPORT_TSK_PATH_AUTOCAL_SKIP = "SUPPORT_TSK_PATH_AUTOCAL_SKIP";
        public static final String SUPPORT_TSPCAL_APP = "SUPPORT_TSPCAL_APP";
        public static final String SUPPORT_TSPCAL_WHITE = "SUPPORT_TSPCAL_WHITE";
        public static final String SUPPORT_TSP_CAL_FLAG_CHECK = "SUPPORT_TSP_CAL_FLAG_CHECK";
        public static final String SUPPORT_TSP_DATA_RUN_ALL_READ = "SUPPORT_TSP_DATA_RUN_ALL_READ";
        public static final String SUPPORT_TSP_OPENSHORT_COMMON_CONCEPT = "SUPPORT_TSP_OPENSHORT_COMMON_CONCEPT";
        public static final String SUPPORT_TSP_PIN_STATE = "SUPPORT_TSP_PIN_STATE";
        public static final String SUPPORT_TSP_SELFTEST_ABS_CAP = "SUPPORT_TSP_SELFTEST_ABS_CAP";
        public static final String SUPPORT_TSP_SELFTEST_ABS_CAP_ONLY_RUN_CMD = "SUPPORT_TSP_SELFTEST_ABS_CAP_ONLY_RUN_CMD";
        public static final String SUPPORT_TSP_SELFTEST_AVERAGE = "SUPPORT_TSP_SELFTEST_AVERAGE";
        public static final String SUPPORT_TSP_SELFTEST_FILESPEC = "SUPPORT_TSP_SELFTEST_FILESPEC";
        public static final String SUPPORT_TSP_SELFTEST_ONECMD = "SUPPORT_TSP_SELFTEST_ONECMD";
        public static final String SUPPORT_TSP_SELFTEST_SLOPE = "SUPPORT_TSP_SELFTEST_SLOPE";
        public static final String SUPPORT_TSP_SET_FAC_LEVEL = "SUPPORT_TSP_SET_FAC_LEVEL";
        public static final String SUPPORT_TSP_SHORT_TEST = "SUPPORT_TSP_SHORT_TEST";
        public static final String SUPPORT_TSP_UMS_UPDATE = "SUPPORT_TSP_UMS_UPDATE";
        public static final String SUPPORT_UFS = "SUPPORT_UFS";
        public static final String SUPPORT_VIBRATOR = "SUPPORT_VIBRATOR";
        public static final String SUPPORT_VOICE_CALL_FORCIBLY = "SUPPORT_VOICE_CALL_FORCIBLY";
        public static final String SUPPORT_VOLTAGE_CHECK_IN_CHARGING_TEST = "SUPPORT_VOLTAGE_CHECK_IN_CHARGING_TEST";
        public static final String SUPPORT_VOLTAGE_CHECK_IN_PD_CHARGING_TEST = "SUPPORT_VOLTAGE_CHECK_IN_PD_CHARGING_TEST";
        public static final String SUPPORT_VOLTAGE_DROP_CHECK = "SUPPORT_VOLTAGE_DROP_CHECK";
        public static final String SUPPORT_VOLUME_DOWN_KEY_ORIENTATION = "SUPPORT_VOLUME_DOWN_KEY_ORIENTATION";
        public static final String SUPPORT_WATERPROOF_TEST = "SUPPORT_WATERPROOF_TEST";
        public static final String SUPPORT_WATER_INT_SYSFS = "SUPPORT_WATER_INT_SYSFS";
        public static final String SUPPORT_WET_MODE_CHECK = "SUPPORT_WET_MODE_CHECK";
        public static final String SUPPORT_WIFI_NFC_UFS_VER = "SUPPORT_WIFI_NFC_UFS_VER";
        public static final String SUPPORT_WIFI_STRESS_TEST = "SUPPORT_WIFI_STRESS_TEST";
        public static final String SUPPORT_WTR_CX2_DATA_DISPLAY = "SUPPORT_WTR_CX2_DATA_DISPLAY";
        public static final String SUPPORT_WTR_IX_DATA_DISPLAY = "SUPPORT_WTR_IX_DATA_DISPLAY";
        public static final String SUPPORT_WTR_SELF_RAW_DISPLAY = "SUPPORT_WTR_SELF_RAW_DISPLAY";
        public static final String SUPPORT_XO_OSC_THERMISTOR = "SUPPORT_XO_OSC_THERMISTOR";
        public static final String SUPPORT_ZINITIX_DND_DIFF_DISPLAY = "SUPPORT_ZINITIX_DND_DIFF_DISPLAY";
        public static final String SUPPORT_ZINITIX_HF_DND_DISPLAY = "SUPPORT_ZINITIX_HF_DND_DISPLAY";
        public static final String SUPPORT_ZINITIX_HF_DND_H_V_GAP_DISPLAY = "SUPPORT_ZINITIX_HF_DND_H_V_GAP_DISPLAY";
        public static final String SUPPORT_ZINITIX_H_V_GAP_DISPLAY = "SUPPORT_ZINITIX_H_V_GAP_DISPLAY";
        public static final String SUPPORT_ZINITIX_SELF_DND = "SUPPORT_ZINITIX_SELF_DND";
        public static final String SUPPORT_ZINITIX_SELF_DND_H_GAP = "SUPPORT_ZINITIX_SELF_DND_H_GAP";
        public static final String SUPPORT_ZINITIX_SELF_DND_TX = "SUPPORT_ZINITIX_SELF_DND_TX";
        public static final String SUPPORT_ZINITIX_SELF_DND_V_GAP = "SUPPORT_ZINITIX_SELF_DND_V_GAP";
        public static final String SUPPORT_ZINITIX_SELF_SATURATION_RX = "SUPPORT_ZINITIX_SELF_SATURATION_RX";
        public static final String SUPPORT_ZINITIX_SELF_SATURATION_TX = "SUPPORT_ZINITIX_SELF_SATURATION_TX";
        public static final String SUPPORT_ZINITIX_TSP_CONNECTION_REFERENCE_VALUE = "SUPPORT_ZINITIX_TSP_CONNECTION_REFERENCE_VALUE";
        public static final String SUPPORT_ZINITIX_TXRX_SHORT_BY_BIT_DISPLAY = "SUPPORT_ZINITIX_TXRX_SHORT_BY_BIT_DISPLAY";
        public static final String SUPPORT_ZINITIX_WET_MODE = "SUPPORT_ZINITIX_WET_MODE";
        public static final String SVC_LED_TEST_BRIGHTNESS = "SVC_LED_TEST_BRIGHTNESS";
        public static final String SYS_INFO_LTE_FINAL = "SYS_INFO_LTE_FINAL";
        public static final String TABLET_DEFAULT_ORIENTATION = "TABLET_DEFAULT_ORIENTATION";
        public static final String TAG = "Feature";
        public static final String THRETHOLD_CHECK = "THRETHOLD_CHECK";
        public static final String TORCH_MODE_FLASH_1_OFF = "TORCH_MODE_FLASH_1_OFF";
        public static final String TORCH_MODE_FLASH_1_ON = "TORCH_MODE_FLASH_1_ON";
        public static final String TORCH_MODE_FLASH_ON_CURRENT = "TORCH_MODE_FLASH_ON_CURRENT";
        public static final String TOUCHKEY_FW_INFO = "TOUCHKEY_FW_INFO";
        public static final String TSK_DUALIZE = "TSK_DUALIZE";
        public static final String TSK_MANUFACTURE = "TSK_MANUFACTURE";
        public static final String TSP_RESET_FOR_KEY_TEST = "TSP_RESET_FOR_KEY_TEST";
        public static final String TSP_SELFTEST_FOR_TDDI_CHECK_BOTH_TOP_BOTTOM = "TSP_SELFTEST_FOR_TDDI_CHECK_BOTH_TOP_BOTTOM";
        public static final String TWOSPEAKER = "TWOSPEAKER";
        public static final String UNSUPPORT_HOME_FINGER = "UNSUPPORT_HOME_FINGER";
        public static final String USE_AFC_SET_VOLTAGE = "USE_AFC_SET_VOLTAGE";
        public static final String USE_CHARGE_MODE = "USE_CHARGE_MODE";
        public static final String USE_DIFFERENT_THD = "USE_DIFFERENT_THD";
        public static final String USE_DUAL_CHARGE_CHIP = "USE_DUAL_CHARGE_CHIP";
        public static final String USE_DUAL_SENSOR_ACC = "USE_DUAL_SENSOR_ACC";
        public static final String USE_DUAL_SENSOR_HRM = "USE_DUAL_SENSOR_HRM";
        public static final String USE_DUAL_SENSOR_MAGNETIC = "USE_DUAL_SENSOR_MAGNETIC";
        public static final String USE_MODEL_NUMBER = "USE_MODEL_NUMBER";
        public static final String USE_SYSFS_FOR_SENSOR_NAME = "USE_SYSFS_FOR_SENSOR_NAME";
        public static final String USE_USERDATA_VERSION = "USE_USERDATA_VERSION";
        public static final String USE_WIRELESS_CHARGE_MODE = "USE_WIRELESS_CHARGE_MODE";
        public static final String VOLUME_MUTE_WHEN_SPK_CAL = "VOLUME_MUTE_WHEN_SPK_CAL";
        public static final String WHAT_LOOPBACK_PATH_SUPPORTED = "WHAT_LOOPBACK_PATH_SUPPORTED";
        public static final String WPC_OFF_WHEN_MST = "WPC_OFF_WHEN_MST";

        public static boolean getBoolean(String id) {
            return Values.getBoolean(id, "value");
        }

        public static boolean getBoolean(String id, boolean defaultValue) {
            return Values.getBoolean(id, "value", defaultValue);
        }

        public static byte getByte(String id) {
            return Values.getByte(id, "value");
        }

        public static String getString(String id) {
            return Values.getString(id, "value");
        }

        public static int getInt(String id) {
            return Values.getInt(id, "value");
        }

        public static float getFloat(String id) {
            return Values.getFloat(id, "value");
        }

        public static double getDouble(String id) {
            return Values.getDouble(id, "value");
        }

        public static float getFactoryTextSize() {
            String fontsize = Values.getString(FONT_SIZE, "value");
            StringBuilder sb = new StringBuilder();
            sb.append("FONT_SIZE : ");
            sb.append(fontsize);
            XmlUtil.log_d(Support.CLASS_NAME, "getFactoryTextSize", sb.toString());
            if (fontsize == null || fontsize.isEmpty() || fontsize == Properties.PROPERTIES_DEFAULT_STRING) {
                return 0.0f;
            }
            return Float.parseFloat(fontsize);
        }
    }

    public static class HwTestMenu {
        public static final String HDMI_TYPE = "18";
        public static final int ID_GRIP = 15;
        public static final int ID_MLC = 31;
        public static final int ID_TSP_HOVERING = 26;
        public static final String LCD_TEST_FRONT_CAM = "06";
        public static final String LCD_TEST_GRIP = "15";
        public static final String LCD_TEST_HOVERING = "26";
        public static final String LCD_TEST_MEGA_CAM = "05";
        public static final String LCD_TEST_TOUCH = "04";
        public static final String LCD_TEST_TOUCH_2 = "27";
        public static final String TAG = "HwTestMenu";
        public static final String TSP_CMD_X_COUNT = "get_x_num";
        public static final String TSP_CMD_Y_COUNT = "get_y_num";
        public static final String TSP_RETURN_VALUE_NA = "NA";
        public static final String TSP_RETURN_VALUE_NG = "NG";
        public static final String TSP_RETURN_VALUE_OK = "OK";

        public static String[] getTestMenu() {
            String str;
            Element[] items = XMLDataStorage.instance().getChildElementSet(TAG);
            ArrayList<String> testList = new ArrayList<>();
            for (Element item : items) {
                boolean parent_Enable = Boolean.parseBoolean(item.getAttribute("enable"));
                StringBuilder sb = new StringBuilder();
                sb.append("name=");
                sb.append(item.getAttribute("name"));
                sb.append(", enable=");
                sb.append(parent_Enable);
                XmlUtil.log_v(Support.CLASS_NAME, "getFact", sb.toString());
                if (parent_Enable) {
                    String parent_Name = item.getAttribute("name");
                    String parent_ID = item.getAttribute("action");
                    if (item.getAttribute("extra") != null) {
                        str = item.getAttribute("extra");
                    } else {
                        str = "default";
                    }
                    String parent_Extra = str;
                    if (Integer.parseInt(parent_ID) != 15) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(parent_ID);
                        sb2.append(",");
                        sb2.append(parent_Name);
                        sb2.append(",");
                        sb2.append(parent_Extra);
                        sb2.append(",null");
                        testList.add(sb2.toString());
                    } else if (checkHwRevision()) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(parent_ID);
                        sb3.append(",");
                        sb3.append(parent_Name);
                        sb3.append(",");
                        sb3.append(parent_Extra);
                        sb3.append(",null");
                        testList.add(sb3.toString());
                    }
                }
            }
            return (String[]) testList.toArray(new String[0]);
        }

        private static boolean checkHwRevision() {
            XmlUtil.log_v(Support.CLASS_NAME, "checkHwRevision", "");
            if ("SM-N910V".equals(Feature.getString(Feature.MODEL_NAME))) {
                int hwRev = Integer.parseInt(SystemProperties.get("ro.revision"));
                StringBuilder sb = new StringBuilder();
                sb.append("ro.revision=");
                sb.append(hwRev);
                XmlUtil.log_v(Support.CLASS_NAME, "checkHwRevision", sb.toString());
                if (hwRev < 13) {
                    return false;
                }
            }
            return true;
        }

        public static String getTestCase(String action) {
            Element[] items;
            String result = null;
            for (Element item : XMLDataStorage.instance().getChildElementSet(TAG)) {
                if (action.equals(item.getAttribute("action"))) {
                    result = item.getAttribute("testcase");
                    StringBuilder sb = new StringBuilder();
                    sb.append("action =");
                    sb.append(action);
                    sb.append(", testcase =");
                    sb.append(result);
                    XmlUtil.log_v(Support.CLASS_NAME, "getTestCase", sb.toString());
                }
            }
            return result;
        }

        public static String getEnabled(int id) {
            Element[] items;
            String result = null;
            for (Element item : XMLDataStorage.instance().getChildElementSet(TAG)) {
                if (Integer.toString(id).equals(item.getAttribute("action"))) {
                    result = item.getAttribute("enable");
                    StringBuilder sb = new StringBuilder();
                    sb.append("action =");
                    sb.append(Integer.toString(id));
                    sb.append(", enable =");
                    sb.append(result);
                    XmlUtil.log_v(Support.CLASS_NAME, "getEnabled", sb.toString());
                }
            }
            return result;
        }

        public static float getUIRate(int action) {
            Element[] items;
            String rate = null;
            for (Element item : XMLDataStorage.instance().getChildElementSet(TAG)) {
                if (String.format("%02d", new Object[]{Integer.valueOf(action)}).equals(item.getAttribute("action"))) {
                    rate = item.getAttribute("uirate");
                    StringBuilder sb = new StringBuilder();
                    sb.append("action =");
                    sb.append(String.format("%02d", new Object[]{Integer.valueOf(action)}));
                    sb.append(", uirate = ");
                    sb.append(rate);
                    XmlUtil.log_v(Support.CLASS_NAME, "getUIRate", sb.toString());
                }
            }
            if (rate == null || rate.isEmpty()) {
                return 0.0f;
            }
            return Float.parseFloat(rate);
        }
    }

    public static class Kernel {
        public static final String ABCD_RAW = "ABCD_RAW";
        public static final String ACCELEROMETER_SENSOR_TYPE = "ACCELEROMETER_SENSOR_TYPE";
        public static final String ACCEL_SENSOR_CAL = "ACCEL_SENSOR_CAL";
        public static final String ACCEL_SENSOR_INTPIN = "ACCEL_SENSOR_INTPIN";
        public static final String ACCEL_SENSOR_NAME = "ACCEL_SENSOR_NAME";
        public static final String ACCEL_SENSOR_RAW = "ACCEL_SENSOR_RAW";
        public static final String ACCEL_SENSOR_SELFTEST = "ACCEL_SENSOR_SELFTEST";
        public static final String ACCEL_SENSOR_VENDOR = "ACCEL_SENSOR_VENDOR";
        public static final String ACC_COVER_PWR_LSI = "ACC_COVER_PWR_LSI";
        public static final String ACC_COVER_PWR_QCOM = "ACC_COVER_PWR_QCOM";
        public static final String ACC_COVER_UART_LSI = "ACC_COVER_UART_LSI";
        public static final String ACC_COVER_UART_QCOM = "ACC_COVER_UART_QCOM";
        public static final String ADC_ERR_CONTROL = "ADC_ERR_CONTROL";
        public static final String AFC_DISABLE = "AFC_DISABLE";
        public static final String AFC_SENSING_INT = "AFC_SENSING_INT";
        public static final String AFC_SET_VOLTAGE = "AFC_SET_VOLTAGE";
        public static final String AIR_COMMAND_WIFI = "AIR_COMMAND_WIFI";
        public static final String ANYWAY_JIG = "ANYWAY_JIG";
        public static final String ANYWAY_JIG_30PIN = "ANYWAY_JIG_30PIN";
        public static final String APCHIP_TEMP_ADC = "APCHIP_TEMP_ADC";
        public static final String APCHIP_TEMP_POWER_POLICY = "APCHIP_TEMP_POWER_POLICY";
        public static final String APCHIP_TEMP_THERMISTOR0 = "APCHIP_TEMP_THERMISTOR0";
        public static final String APCHIP_TEMP_THERMISTOR1 = "APCHIP_TEMP_THERMISTOR1";
        public static final String APCHIP_TEMP_THERMISTOR2 = "APCHIP_TEMP_THERMISTOR2";
        public static final String APCHIP_TEMP_THERMISTOR3 = "APCHIP_TEMP_THERMISTOR3";
        public static final String APCHIP_TEMP_THERMISTOR4 = "APCHIP_TEMP_THERMISTOR4";
        public static final String APCHIP_TEMP_THERMISTOR5 = "APCHIP_TEMP_THERMISTOR5";
        public static final String APCHIP_TEMP_THERMISTOR6 = "APCHIP_TEMP_THERMISTOR6";
        public static final String APCHIP_TEMP_THERMISTOR7 = "APCHIP_TEMP_THERMISTOR7";
        public static final String AP_CHIP_CHECK = "AP_CHIP_CHECK";
        public static final String AP_CHIP_DOU_FUSING_BINS = "AP_CHIP_DOU_FUSING_BINS";
        public static final String AP_CHIP_DOU_FUSING_REV = "AP_CHIP_DOU_FUSING_REV";
        public static final String AP_CHIP_EVT_REVISION = "AP_CHIP_EVT_REVISION";
        public static final String AP_CHIP_FUSING_BIT = "AP_CHIP_FUSING_BIT";
        public static final String AP_CHIP_ID = "AP_CHIP_ID";
        public static final String AP_CHIP_ID2 = "AP_CHIP_ID2";
        public static final String AP_CHIP_ID_REVISION = "AP_CHIP_ID_REVISION";
        public static final String AP_CHIP_LOT_ID = "AP_CHIP_LOT_ID";
        public static final String AP_GROUP = "AP_GROUP";
        public static final String AUDIO_CODEC_IC_DRIVER = "AUDIO_CODEC_IC_DRIVER";
        public static final String AUDIO_CODEC_VA_WDSP_COUNT = "AUDIO_CODEC_VA_WDSP_COUNT";
        public static final String AUDIO_CODEC_VA_WDSP_DELAY = "AUDIO_CODEC_VA_WDSP_DELAY";
        public static final String AUDIO_CODEC_VA_WDSP_STATE = "AUDIO_CODEC_VA_WDSP_STATE";
        public static final String AVAILABLE_PIN_CHECK = "AVAILABLE_PIN_CHECK";
        public static final String BARCODE_EMUL_FIRMWARE_VERSION = "BARCODE_EMUL_FIRMWARE_VERSION";
        public static final String BAROMETER_SENSOR_NAME = "BAROMETER_SENSOR_NAME";
        public static final String BAROMETE_CALIBRATION = "BAROMETE_CALIBRATION";
        public static final String BAROMETE_DELTA = "BAROMETE_DELTA";
        public static final String BAROMETE_NAME = "BAROMETE_NAME";
        public static final String BAROMETE_SELFTEST = "BAROMETE_SELFTEST";
        public static final String BAROMETE_TEMPERATURE = "BAROMETE_TEMPERATURE";
        public static final String BAROMETE_VENDOR = "BAROMETE_VENDOR";
        public static final String BAROME_EEPROM = "BAROME_EEPROM";
        public static final String BATTERY_ADC_NOTSUPPORT = "BATTERY_ADC_NOTSUPPORT";
        public static final String BATTERY_AP_LOCKUP = "BATTERY_AP_LOCKUP";
        public static final String BATTERY_CAPACITY = "BATTERY_CAPACITY";
        public static final String BATTERY_CISD_DATA = "BATTERY_CISD_DATA";
        public static final String BATTERY_CISD_MAXCAP_NOW = "BATTERY_CISD_MAXCAP_NOW";
        public static final String BATTERY_CISD_REMCAP = "BATTERY_CISD_REMCAP";
        public static final String BATTERY_CYCLE = "BATTERY_CYCLE";
        public static final String BATTERY_DISCHARGING_CHECK_ADC = "BATTERY_DISCHARGING_CHECK_ADC";
        public static final String BATTERY_DISCHARGING_FORCE_SWITCH = "BATTERY_DISCHARGING_FORCE_SWITCH";
        public static final String BATTERY_DISCHARGING_NTC_CHECK = "BATTERY_DISCHARGING_NTC_CHECK";
        public static final String BATTERY_DISCHARGING_SWITCH_CHECK = "BATTERY_DISCHARGING_SWITCH_CHECK";
        public static final String BATTERY_INBAT_VOLTAGE = "BATTERY_INBAT_VOLTAGE";
        public static final String BATTERY_INBAT_VOLTAGE_ADC = "BATTERY_INBAT_VOLTAGE_ADC";
        public static final String BATTERY_INBAT_VOLTAGE_OCV = "BATTERY_INBAT_VOLTAGE_OCV";
        public static final String BATTERY_MAIN_CURRENT = "BATTERY_MAIN_CURRENT";
        public static final String BATTERY_MAIN_ENB = "BATTERY_MAIN_ENB";
        public static final String BATTERY_MAIN_VOLTAGE = "BATTERY_MAIN_VOLTAGE";
        public static final String BATTERY_SAVE_SOC_DATA = "BATTERY_SAVE_SOC_DATA";
        public static final String BATTERY_SUB_CURRENT = "BATTERY_SUB_CURRENT";
        public static final String BATTERY_SUB_ENB = "BATTERY_SUB_ENB";
        public static final String BATTERY_SUB_TEMP = "BATTERY_SUB_TEMP";
        public static final String BATTERY_SUB_TEMP_ADC = "BATTERY_SUB_TEMP_ADC";
        public static final String BATTERY_SUB_VOLTAGE = "BATTERY_SUB_VOLTAGE";
        public static final String BATTERY_TEMP = "BATTERY_TEMP";
        public static final String BATTERY_TEMP_ADC = "BATTERY_TEMP_ADC";
        public static final String BATTERY_TEMP_ADC_AVER = "BATTERY_TEMP_ADC_AVER";
        @Deprecated
        public static final String BATTERY_TEMP_ADC_CAL = "BATTERY_TEMP_ADC_CAL";
        public static final String BATTERY_TEMP_AVER = "BATTERY_TEMP_AVER";
        public static final String BATTERY_TEMP_CHARGE = "BATTERY_TEMP_CHARGE";
        public static final String BATTERY_TYPE = "BATTERY_TYPE";
        public static final String BATTERY_UPDATE_BEFORE_READ = "BATTERY_UPDATE_BEFORE_READ";
        public static final String BATTERY_VF_ADC = "BATTERY_VF_ADC";
        public static final String BATTERY_VF_OCV = "BATTERY_VF_OCV";
        public static final String BATTERY_VOLT = "BATTERY_VOLT";
        public static final String BATTERY_VOLT_ADC = "BATTERY_VOLT_ADC";
        public static final String BATTERY_VOLT_ADC_AVER = "BATTERY_VOLT_ADC_AVER";
        public static final String BATTERY_VOLT_ADC_CAL = "BATTERY_VOLT_ADC_CAL";
        public static final String BATTERY_VOLT_AVER = "BATTERY_VOLT_AVER";
        public static final String BATTERY_VOLT_DROP = "BATTERY_VOLT_DROP";
        public static final String BATTERY_WPC_CHARGE = "BATTERY_WPC_CHARGE";
        public static final String BATTERY_WPC_CTRL = "BATTERY_WPC_CTRL";
        public static final String BATTERY_WPC_TEMP = "BATTERY_WPC_TEMP";
        public static final String BATTERY_WPC_TEMP_ADC = "BATTERY_WPC_TEMP_ADC";
        public static final String BATTRY_FACTORYMODE = "BATTRY_FACTORYMODE";
        public static final String BATT_HV_WIRELESS_STATUS = "BATT_HV_WIRELESS_STATUS";
        public static final String BLANKET_THERMISTER_TEMPERATURE = "BLANKET_THERMISTER_TEMPERATURE";
        public static final String BROME_SENSOR_SEAR_LEVEL_PRESSURE = "BROME_SENSOR_SEAR_LEVEL_PRESSURE";
        public static final String BYPASS_MODE = "BYPASS_MODE";
        public static final String CABC_MODE = "CABC_MODE";
        public static final String CABLE_DETECT_CONN = "CABLE_DETECT_CONN";
        public static final String CABLE_DETECT_COUNT_CAMERA = "CABLE_DETECT_COUNT_CAMERA";
        public static final String CABLE_DETECT_COUNT_LOWERPBA = "CABLE_DETECT_COUNT_LOWERPBA";
        public static final String CABLE_DETECT_COUNT_MAINBATT = "CABLE_DETECT_COUNT_MAINBATT";
        public static final String CABLE_DETECT_COUNT_OCTA = "CABLE_DETECT_COUNT_OCTA";
        public static final String CABLE_DETECT_COUNT_SUBBATT = "CABLE_DETECT_COUNT_SUBBATT";
        public static final String CABLE_DETECT_COUNT_SUBPBA = "CABLE_DETECT_COUNT_SUBPBA";
        public static final String CABLE_DETECT_COUNT_TSP = "CABLE_DETECT_COUNT_TSP";
        public static final String CABLE_DETECT_COUNT_UPPERPBA = "CABLE_DETECT_COUNT_UPPERPBA";
        public static final String CAMERA_ALL_FW_VER = "CAMERA_ALL_FW_VER";
        public static final String CAMERA_FLASH_TEMP_ADC = "CAMERA_FLASH_TEMP_ADC";
        public static final String CAMERA_FRONT2_FW_VER = "CAMERA_FRONT2_FW_VER";
        public static final String CAMERA_FRONT2_TILT = "CAMERA_FRONT2_TILT";
        public static final String CAMERA_FRONT_AF_CAL_CHECK = "CAMERA_FRONT_AF_CAL_CHECK";
        public static final String CAMERA_FRONT_DATA_DUMP = "CAMERA_FRONT_DATA_DUMP";
        public static final String CAMERA_FRONT_DATA_DUMP_SIZE = "CAMERA_FRONT_DATA_DUMP_SIZE";
        public static final String CAMERA_FRONT_DUAL_INFO = "CAMERA_FRONT_DUAL_INFO";
        public static final String CAMERA_FRONT_FW_VER = "CAMERA_FRONT_FW_VER";
        public static final String CAMERA_FRONT_MODULE_ID = "CAMERA_FRONT_MODULE_ID";
        public static final String CAMERA_FRONT_TYPE = "CAMERA_FRONT_TYPE";
        public static final String CAMERA_MODULE_ID = "CAMERA_MODULE_ID";
        public static final String CAMERA_OIS_AUTO = "CAMERA_OIS_AUTO";
        public static final String CAMERA_OIS_AUTO_2ND = "CAMERA_OIS_AUTO_2ND";
        public static final String CAMERA_REAR2_AF_CAL_CHECK = "CAMERA_REAR2_AF_CAL_CHECK";
        public static final String CAMERA_REAR2_FW_VER = "CAMERA_REAR2_FW_VER";
        public static final String CAMERA_REAR2_TILT = "CAMERA_REAR2_TILT";
        public static final String CAMERA_REAR4_TILT = "CAMERA_REAR4_TILT";
        public static final String CAMERA_REAR_AF_CAL_CHECK = "CAMERA_REAR_AF_CAL_CHECK";
        public static final String CAMERA_REAR_DATA_DUMP = "CAMERA_REAR_DATA_DUMP";
        public static final String CAMERA_REAR_DATA_DUMP_SIZE = "CAMERA_REAR_DATA_DUMP_SIZE";
        public static final String CAMERA_REAR_FW_VER = "CAMERA_REAR_FW_VER";
        public static final String CAMERA_REAR_OIS_FW_VER = "CAMERA_REAR_OIS_FW_VER";
        public static final String CAMERA_REAR_TYPE = "CAMERA_REAR_TYPE";
        public static final String CAMERA_SUPPORT_TYPE2 = "CAMERA_SUPPORT_TYPE2";
        public static final String CCD_STATUS_NODE = "CCD_STATUS_NODE";
        public static final String CCIC_CONTROL_OPTION = "CCIC_CONTROL_OPTION";
        public static final String CCIC_FIRMWARE_UPDATE = "CCIC_FIRMWARE_UPDATE";
        public static final String CCIC_FIRMWARE_UPDATE_STATUS = "CCIC_FIRMWARE_UPDATE_STATUS";
        public static final String CCIC_JIG_ON = "CCIC_JIG_ON";
        public static final String CCIC_LPM_MODE = "CCIC_LPM_MODE";
        public static final String CCIC_RID = "CCIC_RID";
        public static final String CHARGER_2ND_THERMISTER_TEMPERATURE = "CHARGER_2ND_THERMISTER_TEMPERATURE";
        public static final String CHARGING_STATE = "CHARGING_STATE";
        public static final String CHECK_CHARGING_PORT = "CHECK_CHARGING_PORT";
        public static final String CHECK_KEY_PRESSED = "CHECK_KEY_PRESSED";
        public static final String CHECK_REQUESTED_GPIO = "CHECK_REQUESTED_GPIO";
        public static final String CHG_CURRENT_ADC = "CHG_CURRENT_ADC";
        public static final String CHG_TEMP = "CHG_TEMP";
        public static final String CHG_TEMP_ADC = "CHG_TEMP_ADC";
        public static final String COIL_TEMP = "COIL_TEMP";
        public static final String COIL_TEMP_ADC = "COIL_TEMP_ADC";
        public static final String COLOUR_BALANCE_EFS = "COLOUR_BALANCE_EFS";
        public static final String COLOUR_BALANCE_EFS_2 = "COLOUR_BALANCE_EFS_2";
        public static final String COLOUR_BALANCE_EFS_VER = "COLOUR_BALANCE_EFS_VER";
        public static final String COLOUR_BALANCE_KERNEL = "COLOUR_BALANCE_KERNEL";
        public static final String COLOUR_BALANCE_KERNEL_2 = "COLOUR_BALANCE_KERNEL_2";
        public static final String COMPANION_IC_CHECK = "COMPANION_IC_CHECK";
        public static final String COMPARE_HIDDEN_HOLE_OCTA_COLOR = "COMPARE_HIDDEN_HOLE_OCTA_COLOR";
        public static final String COMPARE_TSK_LIGHT_OCTA_COLOR = "COMPARE_TSK_LIGHT_OCTA_COLOR";
        public static final String CONTROL_NO = "CONTROL_NO";
        public static final String COUNTRY_PRODUCTION = "COUNTRY_PRODUCTION";
        public static final String CPU0_IDLE_COLLAPSE = "CPU0_IDLE_COLLAPSE";
        public static final String CPU0_MAX_FREQ = "CPU0_MAX_FREQ";
        public static final String CPU0_MIN_FREQ = "CPU0_MIN_FREQ";
        public static final String CPU0_STANDALONE_COLLAPSE = "CPU0_STANDALONE_COLLAPSE";
        public static final String CPU1_MAX_FREQ = "CPU1_MAX_FREQ";
        public static final String CPU1_MIN_FREQ = "CPU1_MIN_FREQ";
        public static final String CPU1_STANDALONE_COLLAPSE = "CPU1_STANDALONE_COLLAPSE";
        public static final String CPU4_MAX_FREQ = "CPU4_MAX_FREQ";
        public static final String CPU5_MAX_FREQ = "CPU5_MAX_FREQ";
        public static final String CPU6_MAX_FREQ = "CPU6_MAX_FREQ";
        public static final String CPU7_MAX_FREQ = "CPU7_MAX_FREQ";
        public static final String CPU_BIG_LITTLE_STATUS = "CPU_BIG_LITTLE_STATUS";
        public static final String CPU_CPU0_ONLINE = "CPU_CPU0_ONLINE";
        public static final String CPU_CPU1_ONLINE = "CPU_CPU1_ONLINE";
        public static final String CPU_CPU2_ONLINE = "CPU_CPU2_ONLINE";
        public static final String CPU_CPU3_ONLINE = "CPU_CPU3_ONLINE";
        public static final String CPU_CPU4_ONLINE = "CPU_CPU4_ONLINE";
        public static final String CPU_HOTPLUG_DISABLE = "CPU_HOTPLUG_DISABLE";
        public static final String CPU_MAX_FREQ_SET = "CPU_MAX_FREQ_SET";
        public static final String CPU_MIN_FREQ_SET = "CPU_MIN_FREQ_SET";
        public static final String CPU_ONLINE = "CPU_ONLINE";
        public static final String CURRENT_AVG = "CURRENT_AVG";
        public static final String CURRENT_MAX = "CURRENT_MAX";
        public static final String CURRENT_NOW = "CURRENT_NOW";
        public static final String DDI_MIPI_CONTROL = "DDI_MIPI_CONTROL";
        public static final String DEVICE_RAM_SIZE = "DEVICE_RAM_SIZE";
        public static final String DEVICE_RAM_SIZE_FILE = "DEVICE_RAM_SIZE_FILE";
        public static final String DIRECT_CHARGER_THERMISTER_MODE = "DIRECT_CHARGER_THERMISTER_MODE";
        public static final String DIRECT_CHARGER_THERMISTER_TEMPERATURE = "DIRECT_CHARGER_THERMISTER_TEMPERATURE";
        public static final String DIRECT_CHARGE_STATUS = "DIRECT_CHARGE_STATUS";
        public static final String DIRECT_CHARGE_STEP = "DIRECT_CHARGE_STEP";
        public static final String DISPLAY_INPUT_CURRENT_EFS = "DISPLAY_INPUT_CURRENT_EFS";
        public static final String DISPLAY_INPUT_CURRENT_MODE = "DISPLAY_INPUT_CURRENT_MODE";
        public static final String DISPLAY_INPUT_CURRENT_VALIDITY = "DISPLAY_INPUT_CURRENT_VALIDITY";
        public static final String DISPLAY_INPUT_CURRENT_VALUE = "DISPLAY_INPUT_CURRENT_VALUE";
        public static final String DISPLAY_POC_DATA_EFS = "DISPLAY_POC_DATA_EFS";
        public static final String DMBD_MIC_MODE = "DMBD_MIC_MODE";
        public static final String DPSWITCH_CCIC_CONTROL = "DPSWITCH_CCIC_CONTROL";
        public static final String DPSWITCH_CCIC_SBU_ADC = "DPSWITCH_CCIC_SBU_ADC";
        public static final String DPSWITCH_CHECK_SBU_ADC = "DPSWITCH_CHECK_SBU_ADC";
        public static final String DPSWITCH_CHECK_SBU_DRY = "DPSWITCH_CHECK_SBU_DRY";
        public static final String DPSWITCH_SET_MODE = "DPSWITCH_SET_MODE";
        public static final String DSM_AMP_STATUS = "DSM_AMP_STATUS";
        public static final String DSM_AMP_VALIDATION = "DSM_AMP_VALIDATION";
        public static final String DSM_AMP_VALIDATION_2 = "DSM_AMP_VALIDATION_2";
        public static final String DUAL_CHARGER_SLAVE_ERDY = "DUAL_CHARGER_SLAVE_ERDY";
        public static final String DUAL_CHARGE_MODE = "DUAL_CHARGE_MODE";
        public static final String DVFS_PATH = "DVFS_PATH";
        public static final String DYNAMIC_HOTPLUG = "DYNAMIC_HOTPLUG";
        public static final String EARJACK_PLUGGED = "EARJACK_PLUGGED";
        public static final String EARJACK_SWITCH_STATE = "EARJACK_SWITCH_STATE";
        public static final String EFS_CALDATE_PATH = "EFS_CALDATE_PATH";
        public static final String EFS_FACTORYAPP_ROOT_PATH = "EFS_FACTORYAPP_ROOT_PATH";
        public static final String EFS_FACTORY_ODE_PATH = "EFS_FACTORY_ODE_PATH";
        public static final String EFS_HW_PATH = "EFS_HW_PATH";
        public static final String EFS_MAGNETIC_DATA = "EFS_MAGNETIC_DATA";
        public static final String EFS_PRE_WATERPROOF_LEAK_PATH = "EFS_PRE_WATERPROOF_LEAK_PATH";
        public static final String EFS_PRE_WATERPROOF_LEAK_RESULT = "EFS_PRE_WATERPROOF_LEAK_RESULT";
        public static final String EFS_WATERPROOF_LLEAK_PATH = "EFS_WATERPROOF_LLEAK_PATH";
        public static final String EFS_WATERPROOF_SLEAK_PATH = "EFS_WATERPROOF_SLEAK_PATH";
        public static final String EMMC_CID = "EMMC_CID";
        public static final String EMMC_FW_VERSION = "EMMC_FW_VERSION";
        public static final String EMMC_NAME = "EMMC_NAME";
        public static final String EMMC_UN = "EMMC_UN";
        public static final String ENABLE_SLIDING_MOTOR = "ENABLE_SLIDING_MOTOR";
        public static final String EPEN_BLE_CHARGING_MODE = "EPEN_BLE_CHARGING_MODE";
        public static final String EPEN_BLE_PAIRING_TIME = "EPEN_BLE_PAIRING_TIME";
        public static final String EPEN_CHECKSUM_CHECK = "EPEN_CHECKSUM_CHECK";
        public static final String EPEN_CHECKSUM_RESULT = "EPEN_CHECKSUM_RESULT";
        public static final String EPEN_DIGITIZER_CHECK = "EPEN_DIGITIZER_CHECK";
        public static final String EPEN_DISABLE_MODE = "EPEN_DISABLE_MODE";
        public static final String EPEN_FIRMWARE_UPDATE = "EPEN_FIRMWARE_UPDATE";
        public static final String EPEN_FIRMWARE_UPDATE_STATUS = "EPEN_FIRMWARE_UPDATE_STATUS";
        public static final String EPEN_FIRMWARE_VERSION = "EPEN_FIRMWARE_VERSION";
        public static final String EPEN_GARAGE_FIRMWARE_UPDATE = "EPEN_GARAGE_FIRMWARE_UPDATE";
        public static final String EPEN_GARAGE_FIRMWARE_UPDATE_STATUS = "EPEN_GARAGE_FIRMWARE_UPDATE_STATUS";
        public static final String EPEN_GARAGE_MODE = "EPEN_GARAGE_MODE";
        public static final String EPEN_GARAGE_RAWDATA = "EPEN_GARAGE_RAWDATA";
        public static final String EPEN_INSERT = "EPEN_INSERT";
        public static final String EPEN_SAVINGMODE = "EPEN_SAVINGMODE";
        public static final String EPEN_TUNING_VERSION = "EPEN_TUNING_VERSION";
        public static final String ETHERNET_CONNECTION = "ETHERNET_CONNECTION";
        public static final String ETHERNET_MAC_ADDRESS = "ETHERNET_MAC_ADDRESS";
        public static final String ETHERNET_MAC_ADDRESS_EFS = "ETHERNET_MAC_ADDRESS_EFS";
        public static final String EXTERNAL_MEMORY_INSERTED = "EXTERNAL_MEMORY_INSERTED";
        public static final String EXT_CSD_READ = "EXT_CSD_READ";
        public static final String EXT_SUB_THM = "EXT_SUB_THM";
        public static final String EXT_UP_THM = "EXT_UP_THM";
        public static final String FACTORYMODE_RELEASE_VBUS2BAT = "FACTORYMODE_RELEASE_VBUS2BAT";
        public static final String FACTORYMODE_SET_BYPASS = "FACTORYMODE_SET_BYPASS";
        public static final String FACTORY_BATT_TYPE = "FACTORY_BATT_TYPE";
        public static final String FACTORY_FAILHIST = "FACTORY_FAILHIST";
        public static final String FACTORY_FAILHIST_FLS = "FACTORY_FAILHIST_FLS";
        public static final String FACTORY_MODE = "FACTORY_MODE";
        public static final String FAC_HW_PARAM_BATT_QR = "FAC_HW_PARAM_BATT_QR";
        public static final String FAC_HW_PARAM_DATA = "FAC_HW_PARAM_DATA";
        public static final String FAC_HW_PART_INFORM_DATA = "FAC_HW_PART_INFORM_DATA";
        public static final String FAC_HW_PART_PROTOTYPE_SERIAL_NO = "FAC_HW_PART_PROTOTYPE_SERIAL_NO";
        public static final String FAC_HW_PART_SMD_DATE = "FAC_HW_PART_SMD_DATE";
        public static final String FG_CYCLE = "FG_CYCLE";
        public static final String FG_FIRMWARE = "FG_FIRMWARE";
        public static final String FG_FULLCAPNOM = "FG_FULLCAPNOM";
        public static final String FINGERPRINT_CHIP_NAME = "FINGERPRINT_CHIP_NAME";
        public static final String FINGERPRINT_HBM_STATUS = "FINGERPRINT_HBM_STATUS";
        public static final String FINGERPRINT_OPTICAL_CAL_PATH = "FINGERPRINT_OPTICAL_CAL_PATH";
        public static final String FINGERPRINT_TYPE_CHECK = "FINGERPRINT_TYPE_CHECK";
        public static final String FINGERPRINT_VENDOR = "FINGERPRINT_VENDOR";
        public static final String FINGERPRINT_WOG_RETAIN = "FINGERPRINT_WOG_RETAIN";
        public static final String FINGERPRINT_WOG_SUPPORT = "FINGERPRINT_WOG_SUPPORT";
        public static final String FIVE_BUTTON = "FIVE_BUTTON";
        public static final String FLASH_LED_THERM = "FLASH_LED_THERM";
        public static final String FLICKER_SENSOR_DATA = "FLICKER_SENSOR_DATA";
        public static final String FLICKER_SENSOR_EOL_MODE = "FLICKER_SENSOR_EOL_MODE";
        public static final String FLICKER_SENSOR_EOL_SPEC = "FLICKER_SENSOR_EOL_SPEC";
        public static final String FLICKER_SENSOR_FACTORY_TEST_RESULT = "FLICKER_SENSOR_FACTORY_TEST_RESULT";
        public static final String FLICKER_SENSOR_ID = "FLICKER_SENSOR_ID";
        public static final String FLICKER_SENSOR_POWER = "FLICKER_SENSOR_POWER";
        public static final String FLICKER_SENSOR_SELFTEST = "FLICKER_SENSOR_SELFTEST";
        public static final String FLICKER_SENSOR_TRIM_CHECK = "FLICKER_SENSOR_TRIM_CHECK";
        public static final String FUEL_GAUGE_CURRENT_NOW = "FUEL_GAUGE_CURRENT_NOW";
        public static final String FUEL_GAUGE_INIT_CHECK = "FUEL_GAUGE_INIT_CHECK";
        public static final String FUEL_GAUGE_RESET = "FUEL_GAUGE_RESET";
        public static final String FULL_VOLTAGE = "FULL_VOLTAGE";
        public static final String GAMMA_CODE_FLASH = "GAMMA_CODE_FLASH";
        public static final String GEOMAGNETIC_SENSOR_ADC = "GEOMAGNETIC_SENSOR_ADC";
        public static final String GEOMAGNETIC_SENSOR_DAC = "GEOMAGNETIC_SENSOR_DAC";
        public static final String GEOMAGNETIC_SENSOR_NAME = "GEOMAGNETIC_SENSOR_NAME";
        @Deprecated
        public static final String GEOMAGNETIC_SENSOR_POWER = "GEOMAGNETIC_SENSOR_POWER";
        public static final String GEOMAGNETIC_SENSOR_SELFTEST = "GEOMAGNETIC_SENSOR_SELFTEST";
        public static final String GEOMAGNETIC_SENSOR_STATUS = "GEOMAGNETIC_SENSOR_STATUS";
        @Deprecated
        public static final String GEOMAGNETIC_SENSOR_TEMP = "GEOMAGNETIC_SENSOR_TEMP";
        public static final String GEOMAGNETIC_SENSOR_VENDOR = "GEOMAGNETIC_SENSOR_VENDOR";
        public static final String GESTURE_SENSOR_VENDOR = "GESTURE_SENSOR_VENDOR";
        public static final String GRIP_ADC_FILT = "GRIP_ADC_FILT";
        public static final String GRIP_ADC_FILT_2 = "GRIP_ADC_FILT_2";
        public static final String GRIP_ADC_FILT_3 = "GRIP_ADC_FILT_3";
        public static final String GRIP_ANALOG_GAIN = "GRIP_ANALOG_GAIN";
        public static final String GRIP_ANALOG_GAIN_2 = "GRIP_ANALOG_GAIN_2";
        public static final String GRIP_ANALOG_GAIN_3 = "GRIP_ANALOG_GAIN_3";
        public static final String GRIP_AVG_NEGFILT = "GRIP_AVG_NEGFILT";
        public static final String GRIP_AVG_NEGFILT_2 = "GRIP_AVG_NEGFILT_2";
        public static final String GRIP_AVG_NEGFILT_3 = "GRIP_AVG_NEGFILT_3";
        public static final String GRIP_AVG_POSFILT = "GRIP_AVG_POSFILT";
        public static final String GRIP_AVG_POSFILT_2 = "GRIP_AVG_POSFILT_2";
        public static final String GRIP_AVG_POSFILT_3 = "GRIP_AVG_POSFILT_3";
        public static final String GRIP_AVG_THRESH = "GRIP_AVG_THRESH";
        public static final String GRIP_AVG_THRESH_2 = "GRIP_AVG_THRESH_2";
        public static final String GRIP_AVG_THRESH_3 = "GRIP_AVG_THRESH_3";
        public static final String GRIP_CHECK_BODY_THD = "GRIP_CHECK_BODY_THD";
        public static final String GRIP_HYSTERESIS = "GRIP_HYSTERESIS";
        public static final String GRIP_HYSTERESIS_2 = "GRIP_HYSTERESIS_2";
        public static final String GRIP_HYSTERESIS_3 = "GRIP_HYSTERESIS_3";
        public static final String GRIP_IRQ_COUNT = "GRIP_IRQ_COUNT";
        public static final String GRIP_IRQ_COUNT2 = "GRIP_IRQ_COUNT2";
        public static final String GRIP_IRQ_COUNT3 = "GRIP_IRQ_COUNT3";
        public static final String GRIP_IRQ_COUNT4 = "GRIP_IRQ_COUNT4";
        public static final String GRIP_PHASE = "GRIP_PHASE";
        public static final String GRIP_PHASE_2 = "GRIP_PHASE_2";
        public static final String GRIP_PHASE_3 = "GRIP_PHASE_3";
        public static final String GRIP_RAWFILT = "GRIP_RAWFILT";
        public static final String GRIP_RAWFILT_2 = "GRIP_RAWFILT_2";
        public static final String GRIP_RAWFILT_3 = "GRIP_RAWFILT_3";
        public static final String GRIP_REFERENCE_CAP = "GRIP_REFERENCE_CAP";
        public static final String GRIP_RESET_READY = "GRIP_RESET_READY";
        public static final String GRIP_RESOLUTION = "GRIP_RESOLUTION";
        public static final String GRIP_RESOLUTION_2 = "GRIP_RESOLUTION_2";
        public static final String GRIP_RESOLUTION_3 = "GRIP_RESOLUTION_3";
        public static final String GRIP_SAMPLING_FREQ = "GRIP_SAMPLING_FREQ";
        public static final String GRIP_SAMPLING_FREQ_2 = "GRIP_SAMPLING_FREQ_2";
        public static final String GRIP_SAMPLING_FREQ_3 = "GRIP_SAMPLING_FREQ_3";
        public static final String GRIP_SCAN_PERIOD = "GRIP_SCAN_PERIOD";
        public static final String GRIP_SCAN_PERIOD_2 = "GRIP_SCAN_PERIOD_2";
        public static final String GRIP_SCAN_PERIOD_3 = "GRIP_SCAN_PERIOD_3";
        public static final String GRIP_SENSOR_CALIBRATION = "GRIP_SENSOR_CALIBRATION";
        public static final String GRIP_SENSOR_CALIBRATION_2 = "GRIP_SENSOR_CALIBRATION_2";
        public static final String GRIP_SENSOR_CALIBRATION_3 = "GRIP_SENSOR_CALIBRATION_3";
        public static final String GRIP_SENSOR_CALIBRATION_SLOPE = "GRIP_SENSOR_CALIBRATION_SLOPE";
        public static final String GRIP_SENSOR_CAPMAIN = "GRIP_SENSOR_CAPMAIN";
        public static final String GRIP_SENSOR_CH_COUNT = "GRIP_SENSOR_CH_COUNT";
        public static final String GRIP_SENSOR_CH_COUNT_2 = "GRIP_SENSOR_CH_COUNT_2";
        public static final String GRIP_SENSOR_CH_COUNT_3 = "GRIP_SENSOR_CH_COUNT_3";
        public static final String GRIP_SENSOR_CH_STATE = "GRIP_SENSOR_CH_STATE";
        public static final String GRIP_SENSOR_CH_STATE_2 = "GRIP_SENSOR_CH_STATE_2";
        public static final String GRIP_SENSOR_CH_STATE_3 = "GRIP_SENSOR_CH_STATE_3";
        public static final String GRIP_SENSOR_DIFF_AVG = "GRIP_SENSOR_DIFF_AVG";
        public static final String GRIP_SENSOR_DIFF_AVG_2 = "GRIP_SENSOR_DIFF_AVG_2";
        public static final String GRIP_SENSOR_DIFF_AVG_3 = "GRIP_SENSOR_DIFF_AVG_3";
        public static final String GRIP_SENSOR_ERASECAL = "GRIP_SENSOR_ERASECAL";
        public static final String GRIP_SENSOR_FIRMWARE_UPDATE = "GRIP_SENSOR_FIRMWARE_UPDATE";
        public static final String GRIP_SENSOR_FIRMWARE_UPDATE_2 = "GRIP_SENSOR_FIRMWARE_UPDATE_2";
        public static final String GRIP_SENSOR_FIRMWARE_UPDATE_3 = "GRIP_SENSOR_FIRMWARE_UPDATE_3";
        public static final String GRIP_SENSOR_FIRMWARE_UPDATE_STATUS = "GRIP_SENSOR_FIRMWARE_UPDATE_STATUS";
        public static final String GRIP_SENSOR_FIRMWARE_UPDATE_STATUS_2 = "GRIP_SENSOR_FIRMWARE_UPDATE_STATUS_2";
        public static final String GRIP_SENSOR_FIRMWARE_UPDATE_STATUS_3 = "GRIP_SENSOR_FIRMWARE_UPDATE_STATUS_3";
        public static final String GRIP_SENSOR_FIRMWARE_VERSION_PANEL = "GRIP_SENSOR_FIRMWARE_VERSION_PANEL";
        public static final String GRIP_SENSOR_FIRMWARE_VERSION_PANEL_2 = "GRIP_SENSOR_FIRMWARE_VERSION_PANEL_2";
        public static final String GRIP_SENSOR_FIRMWARE_VERSION_PANEL_3 = "GRIP_SENSOR_FIRMWARE_VERSION_PANEL_3";
        public static final String GRIP_SENSOR_FIRMWARE_VERSION_PHONE = "GRIP_SENSOR_FIRMWARE_VERSION_PHONE";
        public static final String GRIP_SENSOR_FIRMWARE_VERSION_PHONE_2 = "GRIP_SENSOR_FIRMWARE_VERSION_PHONE_2";
        public static final String GRIP_SENSOR_FIRMWARE_VERSION_PHONE_3 = "GRIP_SENSOR_FIRMWARE_VERSION_PHONE_3";
        public static final String GRIP_SENSOR_GAIN = "GRIP_SENSOR_GAIN";
        public static final String GRIP_SENSOR_GAIN_2 = "GRIP_SENSOR_GAIN_2";
        public static final String GRIP_SENSOR_GAIN_3 = "GRIP_SENSOR_GAIN_3";
        public static final String GRIP_SENSOR_MODE = "GRIP_SENSOR_MODE";
        public static final String GRIP_SENSOR_MODE_2 = "GRIP_SENSOR_MODE_2";
        public static final String GRIP_SENSOR_MODE_3 = "GRIP_SENSOR_MODE_3";
        public static final String GRIP_SENSOR_NAME = "GRIP_SENSOR_NAME";
        public static final String GRIP_SENSOR_NAME_2ND = "GRIP_SENSOR_NAME_2ND";
        public static final String GRIP_SENSOR_NAME_3RD = "GRIP_SENSOR_NAME_3RD";
        public static final String GRIP_SENSOR_NORMAL_THD = "GRIP_SENSOR_NORMAL_THD";
        public static final String GRIP_SENSOR_NORMAL_THD_2 = "GRIP_SENSOR_NORMAL_THD_2";
        public static final String GRIP_SENSOR_NORMAL_THD_3 = "GRIP_SENSOR_NORMAL_THD_3";
        public static final String GRIP_SENSOR_ONOFF = "GRIP_SENSOR_ONOFF";
        public static final String GRIP_SENSOR_ONOFF_2 = "GRIP_SENSOR_ONOFF_2";
        public static final String GRIP_SENSOR_ONOFF_3 = "GRIP_SENSOR_ONOFF_3";
        public static final String GRIP_SENSOR_RANGE = "GRIP_SENSOR_RANGE";
        public static final String GRIP_SENSOR_RANGE_2 = "GRIP_SENSOR_RANGE_2";
        public static final String GRIP_SENSOR_RANGE_3 = "GRIP_SENSOR_RANGE_3";
        public static final String GRIP_SENSOR_RAWDATA = "GRIP_SENSOR_RAWDATA";
        public static final String GRIP_SENSOR_RAWDATA_2 = "GRIP_SENSOR_RAWDATA_2";
        public static final String GRIP_SENSOR_RAWDATA_3 = "GRIP_SENSOR_RAWDATA_3";
        public static final String GRIP_SENSOR_RESET = "GRIP_SENSOR_RESET";
        public static final String GRIP_SENSOR_RESET_2 = "GRIP_SENSOR_RESET_2";
        public static final String GRIP_SENSOR_RESET_2ND = "GRIP_SENSOR_RESET_2ND";
        public static final String GRIP_SENSOR_RESET_3 = "GRIP_SENSOR_RESET_3";
        public static final String GRIP_SENSOR_RESET_3RD = "GRIP_SENSOR_RESET_3RD";
        public static final String GRIP_SENSOR_TEMP_CAL = "GRIP_SENSOR_TEMP_CAL";
        public static final String GRIP_SENSOR_TEMP_CAL_2 = "GRIP_SENSOR_TEMP_CAL_2";
        public static final String GRIP_SENSOR_TEMP_REAL = "GRIP_SENSOR_TEMP_REAL";
        public static final String GRIP_SENSOR_TEMP_REAL_2 = "GRIP_SENSOR_TEMP_REAL_2";
        public static final String GRIP_SENSOR_TEMP_REAL_3 = "GRIP_SENSOR_TEMP_REAL_3";
        public static final String GRIP_SENSOR_THRESHOLD = "GRIP_SENSOR_THRESHOLD";
        public static final String GRIP_SENSOR_THRESHOLD_2 = "GRIP_SENSOR_THRESHOLD_2";
        public static final String GRIP_SENSOR_THRESHOLD_3 = "GRIP_SENSOR_THRESHOLD_3";
        public static final String GRIP_SENSOR_USEFUL_AVG = "GRIP_SENSOR_USEFUL_AVG";
        public static final String GRIP_SENSOR_USEFUL_AVG_2 = "GRIP_SENSOR_USEFUL_AVG_2";
        public static final String GRIP_SENSOR_USEFUL_AVG_3 = "GRIP_SENSOR_USEFUL_AVG_3";
        public static final String GRIP_SENSOR_VENDOR = "GRIP_SENSOR_VENDOR";
        public static final String GRIP_TOUCH_SENSOR_BASELINE = "GRIP_TOUCH_SENSOR_BASELINE";
        public static final String GRIP_TOUCH_SENSOR_BASELINE_2 = "GRIP_TOUCH_SENSOR_BASELINE_2";
        public static final String GRIP_TOUCH_SENSOR_BASELINE_3 = "GRIP_TOUCH_SENSOR_BASELINE_3";
        public static final String GRIP_TOUCH_SENSOR_CHECK = "GRIP_TOUCH_SENSOR_CHECK";
        public static final String GRIP_TOUCH_SENSOR_CHECK_2 = "GRIP_TOUCH_SENSOR_CHECK_2";
        public static final String GRIP_TOUCH_SENSOR_CHECK_3 = "GRIP_TOUCH_SENSOR_CHECK_3";
        public static final String GRIP_TOUCH_SENSOR_CRC_CHECK = "GRIP_TOUCH_SENSOR_CRC_CHECK";
        public static final String GRIP_TOUCH_SENSOR_CRC_CHECK_2 = "GRIP_TOUCH_SENSOR_CRC_CHECK_2";
        public static final String GRIP_TOUCH_SENSOR_CRC_CHECK_3 = "GRIP_TOUCH_SENSOR_CRC_CHECK_3";
        public static final String GRIP_TOUCH_SENSOR_DIFF_DATA = "GRIP_TOUCH_SENSOR_DIFF_DATA";
        public static final String GRIP_TOUCH_SENSOR_DIFF_DATA_2 = "GRIP_TOUCH_SENSOR_DIFF_DATA_2";
        public static final String GRIP_TOUCH_SENSOR_DIFF_DATA_3 = "GRIP_TOUCH_SENSOR_DIFF_DATA_3";
        public static final String GRIP_TOUCH_SENSOR_ENABLE = "GRIP_TOUCH_SENSOR_ENABLE";
        public static final String GRIP_TOUCH_SENSOR_ENABLE_2 = "GRIP_TOUCH_SENSOR_ENABLE_2";
        public static final String GRIP_TOUCH_SENSOR_ENABLE_3 = "GRIP_TOUCH_SENSOR_ENABLE_3";
        public static final String GRIP_TOUCH_SENSOR_FW_VERSION = "GRIP_TOUCH_SENSOR_FW_VERSION";
        public static final String GRIP_TOUCH_SENSOR_FW_VERSION_2 = "GRIP_TOUCH_SENSOR_FW_VERSION_2";
        public static final String GRIP_TOUCH_SENSOR_FW_VERSION_3 = "GRIP_TOUCH_SENSOR_FW_VERSION_3";
        public static final String GRIP_TOUCH_SENSOR_GAIN = "GRIP_TOUCH_SENSOR_GAIN";
        public static final String GRIP_TOUCH_SENSOR_GAIN_2 = "GRIP_TOUCH_SENSOR_GAIN_2";
        public static final String GRIP_TOUCH_SENSOR_GAIN_3 = "GRIP_TOUCH_SENSOR_GAIN_3";
        public static final String GRIP_TOUCH_SENSOR_RAWDATA = "GRIP_TOUCH_SENSOR_RAWDATA";
        public static final String GRIP_TOUCH_SENSOR_RAWDATA_2 = "GRIP_TOUCH_SENSOR_RAWDATA_2";
        public static final String GRIP_TOUCH_SENSOR_RAWDATA_3 = "GRIP_TOUCH_SENSOR_RAWDATA_3";
        public static final String GRIP_TOUCH_SENSOR_RESET = "GRIP_TOUCH_SENSOR_RESET";
        public static final String GRIP_TOUCH_SENSOR_RESET_2 = "GRIP_TOUCH_SENSOR_RESET_2";
        public static final String GRIP_TOUCH_SENSOR_RESET_3 = "GRIP_TOUCH_SENSOR_RESET_3";
        public static final String GRIP_TOUCH_SENSOR_THD = "GRIP_TOUCH_SENSOR_THD";
        public static final String GRIP_TOUCH_SENSOR_THD_2 = "GRIP_TOUCH_SENSOR_THD_2";
        public static final String GRIP_TOUCH_SENSOR_THD_3 = "GRIP_TOUCH_SENSOR_THD_3";
        public static final String GRIP_TOUCH_SENSOR_TOTAL_CAP = "GRIP_TOUCH_SENSOR_TOTAL_CAP";
        public static final String GRIP_TOUCH_SENSOR_TOTAL_CAP_2 = "GRIP_TOUCH_SENSOR_TOTAL_CAP_2";
        public static final String GRIP_TOUCH_SENSOR_TOTAL_CAP_3 = "GRIP_TOUCH_SENSOR_TOTAL_CAP_3";
        public static final String GRIP_USEFUL_FILT = "GRIP_USEFUL_FILT";
        public static final String GRIP_USEFUL_FILT_2 = "GRIP_USEFUL_FILT_2";
        public static final String GRIP_USEFUL_FILT_3 = "GRIP_USEFUL_FILT_3";
        public static final String GYRO_OIS_CALTEST = "GYRO_OIS_CALTEST";
        public static final String GYRO_OIS_DIFF = "GYRO_OIS_DIFF";
        public static final String GYRO_OIS_POWER = "GYRO_OIS_POWER";
        public static final String GYRO_OIS_RAWDATA = "GYRO_OIS_RAWDATA";
        public static final String GYRO_OIS_SELFTEST = "GYRO_OIS_SELFTEST";
        public static final String GYRO_SENSOR_DPS_SELECT = "GYRO_SENSOR_DPS_SELECT";
        public static final String GYRO_SENSOR_NAME = "GYRO_SENSOR_NAME";
        public static final String GYRO_SENSOR_REVISED = "GYRO_SENSOR_REVISED";
        public static final String GYRO_SENSOR_SELFTEST = "GYRO_SENSOR_SELFTEST";
        public static final String GYRO_SENSOR_TEMP = "GYRO_SENSOR_TEMP";
        public static final String GYRO_SENSOR_VENDOR = "GYRO_SENSOR_VENDOR";
        public static final String HDCP_CHECK_2_0 = "HDCP_CHECK_2_0";
        public static final String HDCP_CHECK_WIDEVINE_KEYBOX = "HDCP_CHECK_WIDEVINE_KEYBOX";
        public static final String HDMI_FHD_MODE = "HDMI_FHD_MODE";
        public static final String HDMI_TEST_RESULT = "HDMI_TEST_RESULT";
        public static final String HIDDEN_HOLE_DATA = "HIDDEN_HOLE_DATA";
        public static final String HIDDEN_HOLE_VER = "HIDDEN_HOLE_VER";
        public static final String HIDDEN_MENU_BLOCK = "HIDDEN_MENU_BLOCK";
        public static final String HMAC_RESULT = "HMAC_RESULT";
        public static final String HOST_SPEED_MODE = "HOST_SPEED_MODE";
        public static final String HOT_PLUG_LOCK = "HOT_PLUG_LOCK";
        public static final String HRM_DEVICE_ID = "HRM_DEVICE_ID";
        public static final String HRM_DRIVER_VER = "HRM_DRIVER_VER";
        public static final String HRM_EOL_TEST = "HRM_EOL_TEST";
        public static final String HRM_EOL_TEST_RESULT = "HRM_EOL_TEST_RESULT";
        public static final String HRM_EOL_TEST_STATUS = "HRM_EOL_TEST_STATUS";
        public static final String HRM_FACTORY_CMD = "HRM_FACTORY_CMD";
        public static final String HRM_HR_RANGE = "HRM_HR_RANGE";
        public static final String HRM_HR_RANGE2 = "HRM_HR_RANGE2";
        public static final String HRM_IDAC_RANGE = "HRM_IDAC_RANGE";
        public static final String HRM_INT_PIN_CHECK = "HRM_INT_PIN_CHECK";
        public static final String HRM_LED_CURRENT = "HRM_LED_CURRENT";
        public static final String HRM_LED_CURRENT2 = "HRM_LED_CURRENT2";
        public static final String HRM_LIBRARY_ELF_VER = "HRM_LIBRARY_ELF_VER";
        public static final String HRM_LIBRARY_EOL_VER = "HRM_LIBRARY_EOL_VER";
        public static final String HRM_LIBRARY_VER = "HRM_LIBRARY_VER";
        public static final String HRM_MODE_IR = "HRM_MODE_IR";
        public static final String HRM_MODE_R = "HRM_MODE_R";
        public static final String HRM_PART_TYPE = "HRM_PART_TYPE";
        public static final String HRM_SELFTEST_RESULT = "HRM_SELFTEST_RESULT";
        public static final String HRM_SENSOR_NAME = "HRM_SENSOR_NAME";
        public static final String HRM_THD = "HRM_THD";
        public static final String HRM_VENDOR = "HRM_VENDOR";
        public static final String HRM_XTALK_CODE = "HRM_XTALK_CODE";
        public static final String HUMITEMP_THERMISTER_BATT = "HUMITEMP_THERMISTER_BATT";
        public static final String HUMITEMP_THERMISTER_BATT_CELCIUS = "HUMITEMP_THERMISTER_BATT_CELCIUS";
        public static final String HUMITEMP_THERMISTER_PAM = "HUMITEMP_THERMISTER_PAM";
        public static final String HUMITEMP_THERMISTER_S_HUB_BATT = "HUMITEMP_THERMISTER_S_HUB_BATT";
        public static final String HUMITEMP_THERMISTER_S_HUB_BATT_CELCIUS = "HUMITEMP_THERMISTER_S_HUB_BATT_CELCIUS";
        public static final String HUMI_TEMP_SENSOR_CAL = "HUMI_TEMP_SENSOR_CAL";
        public static final String HUMI_TEMP_SENSOR_RESET = "HUMI_TEMP_SENSOR_RESET";
        public static final String HV_CHARGE_CHIP = "HV_CHARGE_CHIP";
        public static final String HV_CHARGE_CURRENT = "HV_CHARGE_CURRENT";
        public static final String HV_CHARGE_STATUS = "HV_CHARGE_STATUS";
        public static final String HV_CHARGE_TYPE = "HV_CHARGE_TYPE";
        public static final String HV_POGO_INPUT = "HV_POGO_INPUT";
        public static final String HV_WC_CHARGER_STATUS = "HV_WC_CHARGER_STATUS";
        public static final String HWPARAM_PCB_INFO_PATH = "HWPARAM_PCB_INFO_PATH";
        public static final String HWPARAM_SMD_DATE_PATH = "HWPARAM_SMD_DATE_PATH";
        public static final String IDT_IC_GRADE = "IDT_IC_GRADE";
        public static final String IDT_MST_SWITCH = "IDT_MST_SWITCH";
        public static final String IDT_OTP_FIRMWARE_UPDATE = "IDT_OTP_FIRMWARE_UPDATE";
        public static final String IDT_OTP_FIRMWARE_UPDATE_STATUS = "IDT_OTP_FIRMWARE_UPDATE_STATUS";
        public static final String IDT_OTP_FIRMWARE_VERSION = "IDT_OTP_FIRMWARE_VERSION";
        public static final String IDT_OTP_ON_OFF = "IDT_OTP_ON_OFF";
        public static final String IDT_PACKAGING_FIRMWARE_VERSION = "IDT_PACKAGING_FIRMWARE_VERSION";
        public static final String IDT_TX_FIRMWARE_OFF = "IDT_TX_FIRMWARE_OFF";
        public static final String IDT_TX_FIRMWARE_ON = "IDT_TX_FIRMWARE_ON";
        public static final String IDT_TX_FIRMWARE_UPDATE_STATUS = "IDT_TX_FIRMWARE_UPDATE_STATUS";
        public static final String IDT_TX_FIRMWARE_VERSION = "IDT_TX_FIRMWARE_VERSION";
        public static final String ID_CHIP_VERIFY = "ID_CHIP_VERIFY";
        public static final String ID_DETECT_TEST = "ID_DETECT_TEST";
        public static final String IMEM_DEVICE_SIZE = "IMEM_DEVICE_SIZE";
        public static final String IRIS_CAMERA_FW_VER = "IRIS_CAMERA_FW_VER";
        public static final String IRIS_CHECK_FW_FACTORY = "IRIS_CHECK_FW_FACTORY";
        public static final String IRIS_PROXI_SENSOR_ADC = "IRIS_PROXI_SENSOR_ADC";
        public static final String IRIS_PROXI_SENSOR_DEFAULT_TRIM = "IRIS_PROXI_SENSOR_DEFAULT_TRIM";
        public static final String IRIS_PROXI_SENSOR_DETECT_HIGH_THRESHOLD = "IRIS_PROXI_SENSOR_DETECT_HIGH_THRESHOLD";
        public static final String IRIS_PROXI_SENSOR_DETECT_LOW_THRESHOLD = "IRIS_PROXI_SENSOR_DETECT_LOW_THRESHOLD";
        public static final String IRIS_PROXI_SENSOR_OFFSET = "IRIS_PROXI_SENSOR_OFFSET";
        public static final String IRIS_PROXI_SENSOR_SET_HIGH_THRESHOLD = "IRIS_PROXI_SENSOR_SET_HIGH_THRESHOLD";
        public static final String IRIS_PROXI_SENSOR_SET_LOW_THRESHOLD = "IRIS_PROXI_SENSOR_SET_LOW_THRESHOLD";
        public static final String IR_CURRENT = "IR_CURRENT";
        public static final String IR_LED_RESULT_TX = "IR_LED_RESULT_TX";
        public static final String IR_LED_SEND = "IR_LED_SEND";
        public static final String IR_LED_SEND_TEST = "IR_LED_SEND_TEST";
        public static final String JIG_B_SIGNAL = "JIG_B_SIGNAL";
        public static final String JIG_CONNECTION_CHECK = "JIG_CONNECTION_CHECK";
        public static final String JIG_CONNECTION_CHECK_ADC = "JIG_CONNECTION_CHECK_ADC";
        public static final String KEYPAD_BACKLIGHT_FAC = "KEYPAD_BACKLIGHT_FAC";
        public static final String KEYSTRING_BLOCK = "KEYSTRING_BLOCK";
        public static final String KEY_PRESSED = "KEY_PRESSED";
        public static final String KEY_PRESSED_3X4 = "KEY_PRESSED_3X4";
        public static final String KEY_PRESSED_POWER = "KEY_PRESSED_POWER";
        public static final String LCD_ALPM = "LCD_ALPM";
        public static final String LCD_ALPM_2 = "LCD_ALPM_2";
        public static final String LCD_AUTO_BRIGHTNESS = "LCD_AUTO_BRIGHTNESS";
        public static final String LCD_BACKLIGHT_BRIGHTNESS_LSI = "LCD_BACKLIGHT_BRIGHTNESS_LSI";
        public static final String LCD_BACKLIGHT_BRIGHTNESS_QUALCOMM = "LCD_BACKLIGHT_BRIGHTNESS_QUALCOMM";
        public static final String LCD_DDI_TEST = "LCD_DDI_TEST";
        public static final String LCD_FAST_DISCHARGE = "LCD_FAST_DISCHARGE";
        public static final String LCD_ID = "LCD_ID";
        public static final String LCD_TYPE_PATH = "LCD_TYPE_PATH";
        public static final String LED_BLINK = "LED_BLINK";
        public static final String LED_BLUE = "LED_BLUE";
        public static final String LED_CONTROL = "LED_CONTROL";
        public static final String LED_GREEN = "LED_GREEN";
        public static final String LED_LOWPOWER = "LED_LOWPOWER";
        public static final String LED_RED = "LED_RED";
        public static final String LIGHT_SENSOR_ADC = "LIGHT_SENSOR_ADC";
        public static final String LIGHT_SENSOR_CIRCLE = "LIGHT_SENSOR_CIRCLE";
        public static final String LIGHT_SENSOR_IMAGE_TEST_RESULT = "LIGHT_SENSOR_IMAGE_TEST_RESULT";
        public static final String LIGHT_SENSOR_LUX = "LIGHT_SENSOR_LUX";
        public static final String LIGHT_SENSOR_NAME = "LIGHT_SENSOR_NAME";
        public static final String LIGHT_SENSOR_RAW = "LIGHT_SENSOR_RAW";
        public static final String LINE_SPEC = "LINE_SPEC";
        public static final String LOGO_LIGHT_TEST_LED_PATTERN = "LOGO_LIGHT_TEST_LED_PATTERN";
        public static final int LOG_LEVEL_NEVER_SHOW = 0;
        public static final String LPA_MODE_SET = "LPA_MODE_SET";
        public static final String LPM_MODE_SET = "LPM_MODE_SET";
        public static final String MAGNT_SENSOR_RAW = "MAGNT_SENSOR_RAW";
        public static final String MAIN_NAD = "MAIN_NAD";
        public static final String MAIN_NAD_RUN = "MAIN_NAD_RUN";
        public static final String MAIN_NAD_STATUS = "MAIN_NAD_STATUS";
        public static final String MAIN_NAD_TIMEOUT = "MAIN_NAD_TIMEOUT";
        public static final String MAIN_PMIC_DET = "MAIN_PMIC_DET";
        public static final String MASKING_HBM_STATUS = "MASKING_HBM_STATUS";
        public static final String MCD_STATUS_NODE = "MCD_STATUS_NODE";
        public static final String MCD_STATUS_NODE_2 = "MCD_STATUS_NODE_2";
        public static final String MD05_REGISTER_PATH = "MD05_REGISTER_PATH";
        public static final String MDNIE_BYPASS_NODE = "MDNIE_BYPASS_NODE";
        public static final String MMCBLK_DEVICE_CSD = "MMCBLK_DEVICE_CSD";
        public static final String MOOD_LED1 = "MOOD_LED1";
        public static final String MOOD_LED2 = "MOOD_LED2";
        public static final String MOOD_LED3 = "MOOD_LED3";
        public static final String MOOD_LED4 = "MOOD_LED4";
        public static final String MOOD_LED_NODE = "MOOD_LED_NODE";
        public static final String MOTOR_LPF = "MOTOR_LPF";
        public static final String MST_DATA_TRANSMIT_PATH = "MST_DATA_TRANSMIT_PATH";
        public static final String MTK_HPS_CONTROL = "MTK_HPS_CONTROL";
        public static final String MUIC_JIG_ON = "MUIC_JIG_ON";
        public static final String NADX_RESULT = "NADX_RESULT";
        public static final String NADX_RUN = "NADX_RUN";
        public static final String NAD_ACAT = "NAD_ACAT";
        public static final String NAD_AP_INTERFACE = "NAD_AP_INTERFACE";
        public static final String NAD_CHECK = "NAD_CHECK";
        public static final String NAD_CHECK_CAL = "NAD_CHECK_CAL";
        public static final String NAD_CHECK_FINAL = "NAD_CHECK_FINAL";
        public static final String NAD_CHECK_LCIA = "NAD_CHECK_LCIA";
        public static final String NAD_DDRTEST_REMAIN_COUNT = "NAD_DDRTEST_REMAIN_COUNT";
        public static final String NAD_DRAM = "NAD_DRAM";
        public static final String NAD_ERASE = "NAD_ERASE";
        public static final String NAD_MTK_REMAIN_COUNT = "NAD_MTK_REMAIN_COUNT";
        public static final String NAD_QDAF_CONTROL = "NAD_QDAF_CONTROL";
        public static final String NAD_QDAF_RESULT = "NAD_QDAF_RESULT";
        public static final String NAD_QMVS_REMAIN_COUNT = "NAD_QMVS_REMAIN_COUNT";
        public static final String NAD_SUPPORT = "NAD_SUPPORT";
        public static final String NEW_FACTORY_MODE_DISABLE = "NEW_FACTORY_MODE_DISABLE";
        public static final String NR_SIOP_THERMISTER_ADC = "NR_SIOP_THERMISTER_ADC";
        public static final String NR_SIOP_THERMISTER_TEMPERATURE = "NR_SIOP_THERMISTER_TEMPERATURE";
        public static final String OCTA_CONNECTION_CHECK = "OCTA_CONNECTION_CHECK";
        public static final String OCTA_ID = "OCTA_ID";
        public static final String OCTA_MANUFACTURE_CODE = "OCTA_MANUFACTURE_CODE";
        public static final String OCTA_SPI_SELF_MASK_NODE = "OCTA_SPI_SELF_MASK_NODE";
        public static final String OTG_MUIC_SET = "OTG_MUIC_SET";
        public static final String OTG_ONLINE = "OTG_ONLINE";
        public static final String OTG_TEST_MODE = "OTG_TEST_MODE";
        public static final String PA0_THERMISTER_ADC = "PA0_THERMISTER_ADC";
        public static final String PA0_THERMISTER_CELCIUS = "PA0_THERMISTER_CELCIUS";
        public static final String PA1_THERMISTER_ADC = "PA1_THERMISTER_ADC";
        public static final String PA1_THERMISTER_CELCIUS = "PA1_THERMISTER_CELCIUS";
        public static final String PAN_PAF_OFFSET = "PAN_PAF_OFFSET";
        public static final String PAN_PAF_OFFSET_MID = "PAN_PAF_OFFSET_MID";
        public static final String PATH_AUTOCAL = "PATH_AUTOCAL";
        public static final String PATH_BACKLIGHT_ON_FOR_GREEN_LCD = "PATH_BACKLIGHT_ON_FOR_GREEN_LCD";
        public static final String PATH_BATTERY_ASOC = "PATH_BATTERY_ASOC";
        public static final String PATH_BATTERY_CHARGE_COUNT = "PATH_BATTERY_CHARGE_COUNT";
        public static final String PATH_BATTERY_DISCON_COUNT = "PATH_BATTERY_DISCON_COUNT";
        public static final String PATH_BLOCK_HALLIC = "PATH_BLOCK_HALLIC";
        public static final String PATH_EARPHONE_KEY_ADC = "PATH_EARPHONE_KEY_ADC";
        public static final String PATH_EARPHONE_KEY_CODE = "PATH_EARPHONE_KEY_CODE";
        public static final String PATH_FORCE_CAL_DATA_EFS = "PATH_FORCE_CAL_DATA_EFS";
        public static final String PATH_FUEL_GAUGE_ASOC = "PATH_FUEL_GAUGE_ASOC";
        public static final String PATH_HALLIC_STATE = "PATH_HALLIC_STATE";
        public static final String PATH_HALLIC_STATE2 = "PATH_HALLIC_STATE2";
        public static final String PATH_HIGH_SENSITIVITY_MODE = "PATH_HIGH_SENSITIVITY_MODE";
        public static final String PATH_IFPMIC_RAM_BIST = "PATH_IFPMIC_RAM_BIST";
        public static final String PATH_MAIN_CHIP_FEATURE_ID = "PATH_MAIN_CHIP_FEATURE_ID";
        public static final String PATH_NOISE_TSK_BACK_IDAC = "PATH_NOISE_TSK_BACK_IDAC";
        public static final String PATH_NOISE_TSK_BACK_IDAC_INNER = "PATH_NOISE_TSK_BACK_IDAC_INNER";
        public static final String PATH_NOISE_TSK_BACK_IDAC_OUTER = "PATH_NOISE_TSK_BACK_IDAC_OUTER";
        public static final String PATH_NOISE_TSK_BACK_RAWDATA = "PATH_NOISE_TSK_BACK_RAWDATA";
        public static final String PATH_NOISE_TSK_BACK_RAWDATA_INNER = "PATH_NOISE_TSK_BACK_RAWDATA_INNER";
        public static final String PATH_NOISE_TSK_BACK_RAWDATA_OUTER = "PATH_NOISE_TSK_BACK_RAWDATA_OUTER";
        public static final String PATH_NOISE_TSK_BACK_RAWDATA_REFERENCE = "PATH_NOISE_TSK_BACK_RAWDATA_REFERENCE";
        public static final String PATH_NOISE_TSK_DUMMY4 = "PATH_NOISE_TSK_DUMMY4";
        public static final String PATH_NOISE_TSK_HIDDEN1 = "PATH_NOISE_TSK_HIDDEN1";
        public static final String PATH_NOISE_TSK_HIDDEN1_RAWDATA = "PATH_NOISE_TSK_HIDDEN1_RAWDATA";
        public static final String PATH_NOISE_TSK_HIDDEN2 = "PATH_NOISE_TSK_HIDDEN2";
        public static final String PATH_NOISE_TSK_HIDDEN2_RAWDATA = "PATH_NOISE_TSK_HIDDEN2_RAWDATA";
        public static final String PATH_NOISE_TSK_HOME_IDAC = "PATH_NOISE_TSK_HOME_IDAC";
        public static final String PATH_NOISE_TSK_HOME_RAWDATA = "PATH_NOISE_TSK_HOME_RAWDATA";
        public static final String PATH_NOISE_TSK_MD_PANEL = "PATH_NOISE_TSK_MD_PANEL";
        public static final String PATH_NOISE_TSK_MD_PHONE = "PATH_NOISE_TSK_MD_PHONE";
        public static final String PATH_NOISE_TSK_MENU_IDAC = "PATH_NOISE_TSK_MENU_IDAC";
        public static final String PATH_NOISE_TSK_MENU_RAWDATA = "PATH_NOISE_TSK_MENU_RAWDATA";
        public static final String PATH_NOISE_TSK_RECENT_IDAC = "PATH_NOISE_TSK_RECENT_IDAC";
        public static final String PATH_NOISE_TSK_RECENT_IDAC_INNER = "PATH_NOISE_TSK_RECENT_IDAC_INNER";
        public static final String PATH_NOISE_TSK_RECENT_IDAC_OUTER = "PATH_NOISE_TSK_RECENT_IDAC_OUTER";
        public static final String PATH_NOISE_TSK_RECENT_RAWDATA = "PATH_NOISE_TSK_RECENT_RAWDATA";
        public static final String PATH_NOISE_TSK_RECENT_RAWDATA_INNER = "PATH_NOISE_TSK_RECENT_RAWDATA_INNER";
        public static final String PATH_NOISE_TSK_RECENT_RAWDATA_OUTER = "PATH_NOISE_TSK_RECENT_RAWDATA_OUTER";
        public static final String PATH_NOISE_TSK_RECENT_RAWDATA_REFERENCE = "PATH_NOISE_TSK_RECENT_RAWDATA_REFERENCE";
        public static final String PATH_NOISE_TSK_SUB_BACK_RAWDATA = "PATH_NOISE_TSK_SUB_BACK_RAWDATA";
        public static final String PATH_NOISE_TSK_SUB_RECENT_RAWDATA = "PATH_NOISE_TSK_SUB_RECENT_RAWDATA";
        public static final String PATH_POC_INFO_EFS = "PATH_POC_INFO_EFS";
        public static final String PATH_TSP_ALL_NODE_SPEC = "PATH_TSP_ALL_NODE_SPEC";
        public static final String PATH_TSP_OPENSHORT_CM2_MAIN = "PATH_TSP_OPENSHORT_CM2_MAIN";
        public static final String PATH_TSP_OPENSHORT_CM2_OUT = "PATH_TSP_OPENSHORT_CM2_OUT";
        public static final String PATH_TSP_OPENSHORT_CM2_SDC = "PATH_TSP_OPENSHORT_CM2_SDC";
        public static final String PATH_TSP_OPENSHORT_CM2_SUB = "PATH_TSP_OPENSHORT_CM2_SUB";
        public static final String PATH_TSP_OPENSHORT_CM3_MAIN = "PATH_TSP_OPENSHORT_CM3_MAIN";
        public static final String PATH_TSP_OPENSHORT_CM3_OUT = "PATH_TSP_OPENSHORT_CM3_OUT";
        public static final String PATH_TSP_OPENSHORT_CM3_SDC = "PATH_TSP_OPENSHORT_CM3_SDC";
        public static final String PATH_TSP_OPENSHORT_CM3_SUB = "PATH_TSP_OPENSHORT_CM3_SUB";
        public static final String PATH_TSP_PROX_STATE = "PATH_TSP_PROX_STATE";
        public static final String PDIC_CHIP_NAME = "PDIC_CHIP_NAME";
        public static final String PDIC_FW_BINARY = "PDIC_FW_BINARY";
        public static final String PDIC_FW_MODULE = "PDIC_FW_MODULE";
        public static final String PD_CHECK_PS_READY = "PD_CHECK_PS_READY";
        public static final String POGO_CHARGING_MODE = "POGO_CHARGING_MODE";
        public static final String POGO_DOCK_INT_PIN1 = "POGO_DOCK_INT_PIN1";
        public static final String POGO_DOCK_INT_PIN2 = "POGO_DOCK_INT_PIN2";
        public static final String POGO_PIN_ADC_READ = "POGO_PIN_ADC_READ";
        public static final String PRESSURE_TOUCH_STATUS = "PRESSURE_TOUCH_STATUS";
        public static final String PRE_PAY = "PRE_PAY";
        public static final String PROXI_LIGHT_SELECT_FLIP_STATE = "PROXI_LIGHT_SELECT_FLIP_STATE";
        public static final String PROXI_SENSOR_ADC = "PROXI_SENSOR_ADC";
        public static final String PROXI_SENSOR_ADC_AVG = "PROXI_SENSOR_ADC_AVG";
        public static final String PROXI_SENSOR_ALERT_THRESHOLD = "PROXI_SENSOR_ALERT_THRESHOLD";
        public static final String PROXI_SENSOR_DEFAULT_TRIM = "PROXI_SENSOR_DEFAULT_TRIM";
        public static final String PROXI_SENSOR_DETECT_HIGH_THRESHOLD = "PROXI_SENSOR_DETECT_HIGH_THRESHOLD";
        public static final String PROXI_SENSOR_DETECT_LOW_THRESHOLD = "PROXI_SENSOR_DETECT_LOW_THRESHOLD";
        public static final String PROXI_SENSOR_MODIFY_SETTING = "PROXI_SENSOR_MODIFY_SETTING";
        public static final String PROXI_SENSOR_NAME = "PROXI_SENSOR_NAME";
        public static final String PROXI_SENSOR_OFFSET = "PROXI_SENSOR_OFFSET";
        public static final String PROXI_SENSOR_OFFSET_PASS = "PROXI_SENSOR_OFFSET_PASS";
        public static final String PROXI_SENSOR_SETTINGS_THD_HIGH = "PROXI_SENSOR_SETTINGS_THD_HIGH";
        public static final String PROXI_SENSOR_SETTINGS_THD_LOW = "PROXI_SENSOR_SETTINGS_THD_LOW";
        public static final String PROXI_SENSOR_SET_HIGH_THRESHOLD = "PROXI_SENSOR_SET_HIGH_THRESHOLD";
        public static final String PROXI_SENSOR_SET_LOW_THRESHOLD = "PROXI_SENSOR_SET_LOW_THRESHOLD";
        public static final String RAM_VERSION = "RAM_VERSION";
        public static final String READ_COPR_NODE = "READ_COPR_NODE";
        public static final String RESET_ENABLED = "RESET_ENABLED";
        public static final String RFBACKOFF_STATUS_WIFI = "RFBACKOFF_STATUS_WIFI";
        public static final String SD_SPEED_MODE = "SD_SPEED_MODE";
        public static final String SECEFS_BACK_COVER_COUNT_FILE = "SECEFS_BACK_COVER_COUNT_FILE";
        public static final String SECEFS_COVER_COUNT_FILE = "SECEFS_COVER_COUNT_FILE";
        public static final String SECEFS_CPLOG_DIRECTORY = "SECEFS_CPLOG_DIRECTORY";
        public static final String SECEFS_DISPLAY_PMIC_CURRENT_FOR_BIGDATA = "SECEFS_DISPLAY_PMIC_CURRENT_FOR_BIGDATA";
        public static final String SECEFS_DISPLAY_PMIC_POWER_VALUE = "SECEFS_DISPLAY_PMIC_POWER_VALUE";
        public static final String SECEFS_ETH_DIRECTORY = "SECEFS_ETH_DIRECTORY";
        public static final String SECEFS_FLICKER_DATA_FILE = "SECEFS_FLICKER_DATA_FILE";
        public static final String SECEFS_GRIP_TEMP_CAL_FILE = "SECEFS_GRIP_TEMP_CAL_FILE";
        public static final String SECEFS_HRM_DATA_FILE = "SECEFS_HRM_DATA_FILE";
        public static final String SECEFS_IMS_SETTING_BACKUP_FILE = "SECEFS_IMS_SETTING_BACKUP_FILE";
        public static final String SECEFS_IMS_SETTING_FILE = "SECEFS_IMS_SETTING_FILE";
        public static final String SECEFS_MD5LOG_PATH = "SECEFS_MD5LOG_PATH";
        public static final String SECEFS_MPS_CODE_FILE = "SECEFS_MPS_CODE_FILE";
        public static final String SECEFS_OQCSBFTT_DIRECTORY = "SECEFS_OQCSBFTT_DIRECTORY";
        public static final String SECEFS_PATH_ACT_NV = "SECEFS_PATH_ACT_NV";
        public static final String SECEFS_PATH_EARJACK_COUNTER = "SECEFS_PATH_EARJACK_COUNTER";
        public static final String SECEFS_PATH_EPEN_COUNTER = "SECEFS_PATH_EPEN_COUNTER";
        public static final String SECEFS_PATH_FLASHPOPUP_COUNTER = "SECEFS_PATH_FLASHPOPUP_COUNTER";
        public static final String SECEFS_PATH_POC_CREATE_DONE = "SECEFS_PATH_POC_CREATE_DONE";
        public static final String SECEFS_PATH_POC_FAIL_COUNT = "SECEFS_PATH_POC_FAIL_COUNT";
        public static final String SECEFS_PATH_POC_ORIGINAL = "SECEFS_PATH_POC_ORIGINAL";
        public static final String SECEFS_PATH_POC_SETTING_INFO = "SECEFS_PATH_POC_SETTING_INFO";
        public static final String SECEFS_PATH_POC_TRY_COUNT = "SECEFS_PATH_POC_TRY_COUNT";
        public static final String SECEFS_PATH_QWERTY_COUNTER = "SECEFS_PATH_QWERTY_COUNTER";
        public static final String SECEFS_PATH_TA_COUNTER = "SECEFS_PATH_TA_COUNTER";
        public static final String SECEFS_PATH_TDMB_COUNTER = "SECEFS_PATH_TDMB_COUNTER";
        public static final String SECEFS_PATH_TEST_NV = "SECEFS_PATH_TEST_NV";
        public static final String SECEFS_RESET_FLAG_FILE = "SECEFS_RESET_FLAG_FILE";
        public static final String SECEFS_TDMBTEST_RSSI_FILE = "SECEFS_TDMBTEST_RSSI_FILE";
        public static final String SECEFS_TDMBTEST_RSSI_LOG_FILE = "SECEFS_TDMBTEST_RSSI_LOG_FILE";
        public static final String SECEFS_WINDOW_COLOR_GROUP_FILE = "SECEFS_WINDOW_COLOR_GROUP_FILE";
        public static final String SEC_BLANKET_THERMISTER_ADC = "SEC_BLANKET_THERMISTER_ADC";
        public static final String SEC_BLANKET_THERMISTER_TEMP = "SEC_BLANKET_THERMISTER_TEMP";
        public static final String SEC_CAMERA_THERMISTER_ADC = "SEC_CAMERA_THERMISTER_ADC";
        public static final String SEC_CAMERA_THERMISTER_TEMP = "SEC_CAMERA_THERMISTER_TEMP";
        public static final String SEC_CHARGING_VOLTAGE = "SEC_CHARGING_VOLTAGE";
        public static final String SEC_CHARGING_VOLTAGE_PD = "SEC_CHARGING_VOLTAGE_PD";
        public static final String SEC_EXT_THERMISTER_ADC = "SEC_EXT_THERMISTER_ADC";
        public static final String SEC_EXT_THERMISTER_TEMP = "SEC_EXT_THERMISTER_TEMP";
        public static final String SEC_MULTI_CHARGER_MODE = "SEC_MULTI_CHARGER_MODE";
        public static final String SEC_QUICK_CHARGE_VOLTAGE = "SEC_QUICK_CHARGE_VOLTAGE";
        public static final String SENSORHUB_CRASHED_FIRMWARE_UPDATE = "SENSORHUB_CRASHED_FIRMWARE_UPDATE";
        public static final String SENSORHUB_DDI_SPI_CHECK = "SENSORHUB_DDI_SPI_CHECK";
        public static final String SENSORHUB_FIRMWARE_UPDATE = "SENSORHUB_FIRMWARE_UPDATE";
        public static final String SENSORHUB_FIRMWARE_VERSION = "SENSORHUB_FIRMWARE_VERSION";
        public static final String SENSORHUB_MCU = "SENSORHUB_MCU";
        public static final String SERIAL_NO = "SERIAL_NO";
        public static final String SET_IFPMIC_POWER = "SET_IFPMIC_POWER";
        public static final String SET_JIGB_NODE = "SET_JIGB_NODE";
        public static final String SET_PARAM_PATH = "SET_PARAM_PATH";
        public static final String SET_QUICK_CHARGE_MODE = "SET_QUICK_CHARGE_MODE";
        public static final String SHIP_MODE = "SHIP_MODE";
        public static final String SHOULD_SHUT_DOWN = "SHOULD_SHUT_DOWN";
        public static final String SIM_TRAY_COUNT = "SIM_TRAY_COUNT";
        public static final String SLIDING_DOWN_HALL_DETECT = "SLIDING_DOWN_HALL_DETECT";
        public static final String SLIDING_UP_HALL_DETECT = "SLIDING_UP_HALL_DETECT";
        public static final String SPK_CAL_CIRRUS_CAL = "SPK_CAL_CIRRUS_CAL";
        public static final String SPK_CAL_CIRRUS_REREF = "SPK_CAL_CIRRUS_REREF";
        public static final String SPK_CAL_CIRRUS_REREF_LEFT = "SPK_CAL_CIRRUS_REREF_LEFT";
        public static final String SPK_CAL_CIRRUS_REREF_RIGHT = "SPK_CAL_CIRRUS_REREF_RIGHT";
        public static final String SPK_CAL_CIRRUS_TEMP = "SPK_CAL_CIRRUS_TEMP";
        public static final String SPK_CAL_MAXIM_CAL = "SPK_CAL_MAXIM_CAL";
        public static final String SPK_CAL_MAXIM_IMPEDANCE = "SPK_CAL_MAXIM_IMPEDANCE";
        public static final String SPK_CAL_MAXIM_IMPEDANCE_2 = "SPK_CAL_MAXIM_IMPEDANCE_2";
        public static final String SPK_CAL_MAXIM_TEMP = "SPK_CAL_MAXIM_TEMP";
        public static final String SPK_CAL_NEW_NXP_CAL = "SPK_CAL_NEW_NXP_CAL";
        public static final String SPK_CAL_NEW_NXP_RDC = "SPK_CAL_NEW_NXP_RDC";
        public static final String SPK_CAL_NEW_NXP_TEMP = "SPK_CAL_NEW_NXP_TEMP";
        public static final String SPK_CAL_RICHTEK_CAL = "SPK_CAL_RICHTEK_CAL";
        public static final String SPK_CAL_RICHTEK_IMPEDANCE = "SPK_CAL_RICHTEK_IMPEDANCE";
        public static final String SPRD_HOT_PLUG_LOCK = "SPRD_HOT_PLUG_LOCK";
        public static final String STATE_SENSOR_RESET = "STATE_SENSOR_RESET";
        public static final String SUBFPCB_TYPE = "SUBFPCB_TYPE";
        public static final String SUBLCD_TYPE_PATH = "SUBLCD_TYPE_PATH";
        public static final String SUBOCTA_ID = "SUBOCTA_ID";
        public static final String SUBOCTA_MANUFACTURE_CODE = "SUBOCTA_MANUFACTURE_CODE";
        public static final String SUB_ACCEL_SENSOR_CAL = "SUB_ACCEL_SENSOR_CAL";
        public static final String SUB_ACCEL_SENSOR_RAW = "SUB_ACCEL_SENSOR_RAW";
        public static final String SUB_ACCEL_SENSOR_SELFTEST = "SUB_ACCEL_SENSOR_SELFTEST";
        public static final String SUB_GYRO_SENSOR_REVISED = "SUB_GYRO_SENSOR_REVISED";
        public static final String SUB_GYRO_SENSOR_SELFTEST = "SUB_GYRO_SENSOR_SELFTEST";
        public static final String SUB_MOTOR_LPF = "SUB_MOTOR_LPF";
        public static final String SUB_TSK_FIRMWARE_UPDATE = "SUB_TSK_FIRMWARE_UPDATE";
        public static final String SUB_TSK_FIRMWARE_UPDATE_STATUS = "SUB_TSK_FIRMWARE_UPDATE_STATUS";
        public static final String SUPPORT_MST_GPIO = "SUPPORT_MST_GPIO";
        public static final String SWITCH_FACTORY = "SWITCH_FACTORY";
        public static final String SWITCH_KERNEL_FTM = "SWITCH_KERNEL_FTM";
        public static final String SYSFS_IFWI_VERSION = "SYSFS_IFWI_VERSION";
        public static final String SYSFS_OCTA_X_TALK_MODE = "SYSFS_OCTA_X_TALK_MODE";
        public static final String SYSTEM_CURRENT = "SYSTEM_CURRENT";
        public static final String SYSTEM_CURRENT_AVG = "SYSTEM_CURRENT_AVG";
        public static final String SYS_POWER_FTM_SLEEP = "SYS_POWER_FTM_SLEEP";
        public static final String TAG = "Sysfs";
        public static final String TEMP_HUMID_ENGINE_VER = "TEMP_HUMID_ENGINE_VER";
        public static final String TORCH_MODE_FLASH_1 = "TORCH_MODE_FLASH_1";
        public static final String TORCH_MODE_FLASH_2 = "TORCH_MODE_FLASH_2";
        public static final String TORCH_MODE_FLASH_3 = "TORCH_MODE_FLASH_3";
        public static final String TORCH_MODE_FLASH_OLD = "TORCH_MODE_FLASH_OLD";
        public static final String TORCH_MODE_FRONT_FLASH = "TORCH_MODE_FRONT_FLASH";
        public static final String TOUCH_FIRMWARE_UPDATE = "TOUCH_FIRMWARE_UPDATE";
        public static final String TOUCH_FIRMWARE_UPDATE_STATUS = "TOUCH_FIRMWARE_UPDATE_STATUS";
        public static final String TOUCH_FIRMWARE_VERSION = "TOUCH_FIRMWARE_VERSION";
        public static final String TOUCH_KEY_BACK_CP_DATA = "TOUCH_KEY_BACK_CP_DATA";
        public static final String TOUCH_KEY_BRIGHTNESS = "TOUCH_KEY_BRIGHTNESS";
        public static final String TOUCH_KEY_CRC_CHECK = "TOUCH_KEY_CRC_CHECK";
        public static final String TOUCH_KEY_ENABLED = "TOUCH_KEY_ENABLED";
        public static final String TOUCH_KEY_INT_CTRL = "TOUCH_KEY_INT_CTRL";
        public static final String TOUCH_KEY_IRQ_COUNT = "TOUCH_KEY_IRQ_COUNT";
        public static final String TOUCH_KEY_RECENT_CP_DATA = "TOUCH_KEY_RECENT_CP_DATA";
        public static final String TOUCH_KEY_SENSITIVITY_APP_SWITCH = "TOUCH_KEY_SENSITIVITY_APP_SWITCH";
        public static final String TOUCH_KEY_SENSITIVITY_BACK = "TOUCH_KEY_SENSITIVITY_BACK";
        public static final String TOUCH_KEY_SENSITIVITY_BACK_INNER = "TOUCH_KEY_SENSITIVITY_BACK_INNER";
        public static final String TOUCH_KEY_SENSITIVITY_BACK_OUTER = "TOUCH_KEY_SENSITIVITY_BACK_OUTER";
        public static final String TOUCH_KEY_SENSITIVITY_HIDDEN1 = "TOUCH_KEY_SENSITIVITY_HIDDEN1";
        public static final String TOUCH_KEY_SENSITIVITY_HIDDEN2 = "TOUCH_KEY_SENSITIVITY_HIDDEN2";
        public static final String TOUCH_KEY_SENSITIVITY_HIDDEN3 = "TOUCH_KEY_SENSITIVITY_HIDDEN3";
        public static final String TOUCH_KEY_SENSITIVITY_HIDDEN4 = "TOUCH_KEY_SENSITIVITY_HIDDEN4";
        public static final String TOUCH_KEY_SENSITIVITY_HOME = "TOUCH_KEY_SENSITIVITY_HOME";
        public static final String TOUCH_KEY_SENSITIVITY_MENU = "TOUCH_KEY_SENSITIVITY_MENU";
        public static final String TOUCH_KEY_SENSITIVITY_OK = "TOUCH_KEY_SENSITIVITY_OK";
        public static final String TOUCH_KEY_SENSITIVITY_POWER = "TOUCH_KEY_SENSITIVITY_POWER";
        public static final String TOUCH_KEY_SENSITIVITY_RECENT = "TOUCH_KEY_SENSITIVITY_RECENT";
        public static final String TOUCH_KEY_SENSITIVITY_RECENT_INNER = "TOUCH_KEY_SENSITIVITY_RECENT_INNER";
        public static final String TOUCH_KEY_SENSITIVITY_RECENT_OUTER = "TOUCH_KEY_SENSITIVITY_RECENT_OUTER";
        public static final String TOUCH_KEY_SENSITIVITY_REF1 = "TOUCH_KEY_SENSITIVITY_REF1";
        public static final String TOUCH_KEY_SENSITIVITY_REF2 = "TOUCH_KEY_SENSITIVITY_REF2";
        public static final String TOUCH_KEY_SENSITIVITY_SEARCH = "TOUCH_KEY_SENSITIVITY_SEARCH";
        public static final String TOUCH_KEY_SENSITIVITY_SUB_APP_SWITCH = "TOUCH_KEY_SENSITIVITY_SUB_APP_SWITCH";
        public static final String TOUCH_KEY_SENSITIVITY_SUB_BACK = "TOUCH_KEY_SENSITIVITY_SUB_BACK";
        public static final String TOUCH_KEY_THRESHOLD = "TOUCH_KEY_THRESHOLD";
        public static final String TOUCH_KEY_THRESHOLD_BACK_INNER = "TOUCH_KEY_THRESHOLD_BACK_INNER";
        public static final String TOUCH_KEY_THRESHOLD_BACK_OUTER = "TOUCH_KEY_THRESHOLD_BACK_OUTER";
        public static final String TOUCH_KEY_THRESHOLD_RECENT_INNER = "TOUCH_KEY_THRESHOLD_RECENT_INNER";
        public static final String TOUCH_KEY_THRESHOLD_RECENT_OUTER = "TOUCH_KEY_THRESHOLD_RECENT_OUTER";
        public static final String TSK_FACTORY_MODE = "TSK_FACTORY_MODE";
        public static final String TSK_FIRMWARE_UPDATE = "TSK_FIRMWARE_UPDATE";
        public static final String TSK_FIRMWARE_UPDATE_STATUS = "TSK_FIRMWARE_UPDATE_STATUS";
        public static final String TSK_FIRMWARE_VERSION_PANEL = "TSK_FIRMWARE_VERSION_PANEL";
        public static final String TSK_FIRMWARE_VERSION_PHONE = "TSK_FIRMWARE_VERSION_PHONE";
        public static final String TSK_FIRMWARE_VERSION_SUB_PANEL = "TSK_FIRMWARE_VERSION_SUB_PANEL";
        public static final String TSK_FIRMWARE_VERSION_SUB_PHONE = "TSK_FIRMWARE_VERSION_SUB_PHONE";
        public static final String TSK_LIGHT_DATA = "TSK_LIGHT_DATA";
        public static final String TSK_LIGHT_VER = "TSK_LIGHT_VER";
        public static final String TSK_LIGHT_WRITE_DATA = "TSK_LIGHT_WRITE_DATA";
        public static final String TSK_VENDOR_NAME = "TSK_VENDOR_NAME";
        public static final String TSP_BYPASS = "TSP_BYPASS";
        public static final String TSP_COMMAND_CMD = "TSP_COMMAND_CMD";
        public static final String TSP_COMMAND_CONNECTION = "TSP_COMMAND_CONNECTION";
        public static final String TSP_COMMAND_RESULT = "TSP_COMMAND_RESULT";
        public static final String TSP_COMMAND_RESULT_ALL = "TSP_COMMAND_RESULT_ALL";
        public static final String TSP_COMMAND_RESULT_EXPAND = "TSP_COMMAND_RESULT_EXPAND";
        public static final String TSP_COMMAND_STATUS = "TSP_COMMAND_STATUS";
        public static final String TSP_COMMAND_STATUS_ALL = "TSP_COMMAND_STATUS_ALL";
        public static final String TSP_EAR_DETECT_ENABLE = "TSP_EAR_DETECT_ENABLE";
        public static final String TSP_ENABLE = "TSP_ENABLE";
        public static final String TSP_REPORT_FREQUENCY_120HZ = "TSP_REPORT_FREQUENCY_120HZ";
        public static final String TSP_SUPPORT_FEATURE = "TSP_SUPPORT_FEATURE";
        public static final String UART_ON_OFF = "UART_ON_OFF";
        public static final String UART_SELECT = "UART_SELECT";
        public static final String UFS_DEVICE_SIZE = "UFS_DEVICE_SIZE";
        public static final String UFS_FW_VERSION = "UFS_FW_VERSION";
        public static final String UFS_UN = "UFS_UN";
        public static final String ULTRASONIC_ADC_DISTANCE = "ULTRASONIC_ADC_DISTANCE";
        public static final String ULTRASONIC_BGE_CAL_BACKUP_FILE_1 = "ULTRASONIC_BGE_CAL_BACKUP_FILE_1";
        public static final String ULTRASONIC_BGE_CAL_BACKUP_FILE_2 = "ULTRASONIC_BGE_CAL_BACKUP_FILE_2";
        public static final String ULTRASONIC_BGE_CAL_BACKUP_FILE_3 = "ULTRASONIC_BGE_CAL_BACKUP_FILE_3";
        public static final String ULTRASONIC_BGE_CAL_RESULT_FILE_1 = "ULTRASONIC_BGE_CAL_RESULT_FILE_1";
        public static final String ULTRASONIC_BGE_CAL_RESULT_FILE_2 = "ULTRASONIC_BGE_CAL_RESULT_FILE_2";
        public static final String ULTRASONIC_BGE_CAL_RESULT_FILE_3 = "ULTRASONIC_BGE_CAL_RESULT_FILE_3";
        public static final String ULTRASONIC_BGE_CAL_RESULT_PATH = "ULTRASONIC_BGE_CAL_RESULT_PATH";
        public static final String ULTRASONIC_COMP_TEMP_REAL = "ULTRASONIC_COMP_TEMP_REAL";
        public static final String ULTRASONIC_FP_DEAD_MASK_IMAGE = "ULTRASONIC_FP_DEAD_MASK_IMAGE";
        public static final String ULTRASONIC_FP_NRSC_RESULT = "ULTRASONIC_FP_NRSC_RESULT";
        public static final String ULTRASONIC_FP_POSITION = "ULTRASONIC_FP_POSITION";
        public static final String ULTRASONIC_FP_SNR_IMAGE = "ULTRASONIC_FP_SNR_IMAGE";
        public static final String ULTRASONIC_FP_SNR_IMAGE_COPY_PATH = "ULTRASONIC_FP_SNR_IMAGE_COPY_PATH";
        public static final String ULTRASONIC_FP_SNR_RESULT = "ULTRASONIC_FP_SNR_RESULT";
        public static final String ULTRASONIC_INT_CHECK_PATH = "ULTRASONIC_INT_CHECK_PATH";
        public static final String ULTRASONIC_RIGHT_START_BTN_POSITION_X_MM = "ULTRASONIC_RIGHT_START_BTN_POSITION_X_MM";
        public static final String ULTRASONIC_RIGHT_START_BTN_POSITION_Y_MM = "ULTRASONIC_RIGHT_START_BTN_POSITION_Y_MM";
        public static final String ULTRASONIC_RIGHT_START_BTN_SIZE_HEIGHT_MM = "ULTRASONIC_RIGHT_START_BTN_SIZE_HEIGHT_MM";
        public static final String ULTRASONIC_RIGHT_START_BTN_SIZE_WIDTH_MM = "ULTRASONIC_RIGHT_START_BTN_SIZE_WIDTH_MM";
        public static final String ULTRASONIC_SENSOR_ID_SAVE_PATH = "ULTRASONIC_SENSOR_ID_SAVE_PATH";
        public static final String ULTRASONIC_VER_CHECK = "ULTRASONIC_VER_CHECK";
        public static final String ULTRASONIC_WUHB_INT_CMD = "ULTRASONIC_WUHB_INT_CMD";
        public static final String ULTRASONIC_WUHB_INT_RESULT = "ULTRASONIC_WUHB_INT_RESULT";
        public static final String USB3_OTG_TYPE_CHECK = "USB3_OTG_TYPE_CHECK";
        public static final String USB3_TYPE_CHECK = "USB3_TYPE_CHECK";
        public static final String USBGEN2_CONNECTION_CHECK = "USBGEN2_CONNECTION_CHECK";
        public static final String USB_1ST_PORT_CHECK = "USB_1ST_PORT_CHECK";
        public static final String USB_1ST_PORT_SUPER_CHECK = "USB_1ST_PORT_SUPER_CHECK";
        public static final String USB_2ST_PORT_CHECK = "USB_2ST_PORT_CHECK";
        public static final String USB_2ST_PORT_SUPER_CHECK = "USB_2ST_PORT_SUPER_CHECK";
        public static final String USB_CONFIG_CHECK = "USB_CONFIG_CHECK";
        public static final String USB_SELECT = "USB_SELECT";
        public static final String USB_TEMP = "USB_TEMP";
        public static final String USB_TEMP_ADC = "USB_TEMP_ADC";
        public static final String USE_MST_MFC_IC = "USE_MST_MFC_IC";
        public static final String UV_SENSOR_EOL_TEST = "UV_SENSOR_EOL_TEST";
        public static final String UV_SENSOR_NAME = "UV_SENSOR_NAME";
        public static final String VGL_TEST_MODE = "VGL_TEST_MODE";
        public static final String WATER_INT_DETECT = "WATER_INT_DETECT";
        public static final String WC_AUTH_ADT_SENT = "WC_AUTH_ADT_SENT";
        public static final String WC_DUO_RX_POWER = "WC_DUO_RX_POWER";
        public static final String WIFI_TEMP_THERMISTOR = "WIFI_TEMP_THERMISTOR";
        public static final String WIFI_THERMISTER_ADC = "WIFI_THERMISTER_ADC";
        public static final String WIFI_THERMISTER_TEMP = "WIFI_THERMISTER_TEMP";
        public static final String WINDOW_TYPE = "WINDOW_TYPE";
        public static final String WINDOW_TYPE_2 = "WINDOW_TYPE_2";
        public static final String WIRELESS_BATTERY = "WIRELESS_BATTERY";
        public static final String WIRELESS_CHARGING_TX_CONNECTION = "WIRELESS_CHARGING_TX_CONNECTION";
        public static final String WIRELESS_CHARGING_TX_CURRENT = "WIRELESS_CHARGING_TX_CURRENT";
        public static final String WIRELESS_CHARGING_TX_ENABLE = "WIRELESS_CHARGING_TX_ENABLE";
        public static final String WIRELESS_CHARGING_TX_IN_DATA = "WIRELESS_CHARGING_TX_IN_DATA";
        public static final String WIRELESS_CHARGING_TX_OUT_DATA = "WIRELESS_CHARGING_TX_OUT_DATA";
        public static final String WIRELESS_CHARGING_TX_RESULT = "WIRELESS_CHARGING_TX_RESULT";
        public static final String WIRELESS_CHARGING_TX_VOLTAGE = "WIRELESS_CHARGING_TX_VOLTAGE";
        public static final String WIRELESS_CHARGING_TX_VOUT = "WIRELESS_CHARGING_TX_VOUT";
        public static final String WIRELESS_CMD_INFO = "WIRELESS_CMD_INFO";
        public static final String WIRELESS_OP_FREQ = "WIRELESS_OP_FREQ";
        public static final String WIRELESS_VOUT = "WIRELESS_VOUT";
        public static final String WIRELESS_VRECT = "WIRELESS_VRECT";
        public static final String XO_OSC_THERMISTER_TEMP = "XO_OSC_THERMISTER_TEMP";

        public static String getFilePath(String id) {
            return Values.getString(id, "path");
        }

        public static byte getByte(String id) {
            String path = Values.getString(id, "path");
            byte value = 0;
            BufferedReader reader = null;
            try {
                BufferedReader reader2 = new BufferedReader(new FileReader(path));
                value = (byte) reader2.read();
                try {
                    reader2.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (reader != null) {
                    reader.close();
                }
            } catch (Throwable th) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
                throw th;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("path=");
            sb.append(path);
            sb.append(", value=");
            sb.append(value);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.read", sb.toString());
            return value;
        }

        public static String getAccSensorVendor() {
            return read(ACCEL_SENSOR_VENDOR);
        }

        public static String getGyroSensorVendor() {
            return read(GYRO_SENSOR_VENDOR);
        }

        public static String getGyroSensorName() {
            String GyroSensorName = Feature.getString(Feature.SENSOR_NAME_GYROSCOPE);
            if (!Feature.getBoolean(Feature.USE_DUAL_SENSOR_ACC)) {
                return GyroSensorName;
            }
            String readGyroName2nd = Feature.getString(Feature.SENSOR_NAME_GYROSCOPE_2ND);
            String readGyroVendor = getGyroSensorVendor();
            if (readGyroName2nd == null || readGyroVendor == null || !readGyroName2nd.contains(readGyroVendor)) {
                return GyroSensorName;
            }
            return readGyroName2nd;
        }

        public static String getGeoMagneticSensorName() {
            String GeoMagneticSensorName = Feature.getString(Feature.SENSOR_NAME_MAGNETIC);
            if (!Feature.getBoolean(Feature.USE_DUAL_SENSOR_MAGNETIC)) {
                return GeoMagneticSensorName;
            }
            String readGeoMagneticName2nd = Feature.getString(Feature.SENSOR_NAME_MAGNETIC_2ND);
            String readGeoMagneticName = read(GEOMAGNETIC_SENSOR_NAME);
            if (readGeoMagneticName2nd == null || readGeoMagneticName == null || !readGeoMagneticName2nd.equals(readGeoMagneticName)) {
                return GeoMagneticSensorName;
            }
            return readGeoMagneticName2nd;
        }

        public static String readAllLine(String id, boolean isLog) {
            String path;
            if (isLog) {
                path = Values.getString(id, "path");
            } else {
                path = Values.getString(id, "path", false);
            }
            String value = "";
            BufferedReader value2 = null;
            if (path == null || Properties.PROPERTIES_DEFAULT_STRING.equalsIgnoreCase(path)) {
                return value;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                while (true) {
                    String temp_value = reader.readLine();
                    if (temp_value == null) {
                        break;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(value2);
                    sb.append(" ");
                    sb.append(temp_value);
                    value = sb.toString();
                }
                if (value2 != null) {
                    value = value2.trim();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (value2 != null) {
                    value2.close();
                }
            } finally {
                if (value2 != null) {
                    try {
                        value2.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
            }
            if (isLog) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("path=");
                sb2.append(path);
                sb2.append(", value=");
                sb2.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.readAllLine", sb2.toString());
            }
            return value;
        }

        public static String readNsyncContinuous(String id, boolean isLog) {
            String path;
            if (isLog) {
                path = Values.getString(id, "path");
            } else {
                path = Values.getString(id, "path", false);
            }
            String value = "";
            BufferedReader value2 = null;
            if (path == null || Properties.PROPERTIES_DEFAULT_STRING.equalsIgnoreCase(path)) {
                return value;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                while (true) {
                    String temp_value = reader.readLine();
                    if (temp_value == null) {
                        break;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(value2);
                    sb.append("\n");
                    sb.append(temp_value);
                    value = sb.toString();
                }
                if (value2 != null) {
                    value = value2.trim();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (value2 != null) {
                    value2.close();
                }
            } finally {
                if (value2 != null) {
                    try {
                        value2.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
            }
            if (isLog) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("path=");
                sb2.append(path);
                sb2.append(", value=");
                sb2.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.readAllLine", sb2.toString());
            }
            return value;
        }

        public static String read(String id, boolean isLog, boolean isTrim) {
            String path;
            if (isLog) {
                path = Values.getString(id, "path");
            } else {
                path = Values.getString(id, "path", false);
            }
            String value = null;
            BufferedReader value2 = null;
            if (path == null || Properties.PROPERTIES_DEFAULT_STRING.equalsIgnoreCase(path)) {
                return null;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                value2 = reader.readLine();
                if (value2 != null && isTrim) {
                    value = value2.trim();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (value2 != null) {
                    value2.close();
                }
            } finally {
                if (value2 != null) {
                    try {
                        value2.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
            }
            if (isLog) {
                StringBuilder sb = new StringBuilder();
                sb.append("path=");
                sb.append(path);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.read", sb.toString());
            }
            return value;
        }

        public static String read(String id, int logLevel) {
            String path = Values.getString(id, "path", logLevel);
            String value = null;
            BufferedReader value2 = null;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                value2 = reader.readLine();
                if (value2 != null) {
                    value = value2.trim();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (value2 != null) {
                    value2.close();
                }
            } finally {
                if (value2 != null) {
                    try {
                        value2.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
            }
            if (logLevel > 0) {
                StringBuilder sb = new StringBuilder();
                sb.append("path=");
                sb.append(path);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.read", sb.toString());
            }
            return value;
        }

        public static int readFromFile(String id, long offset) {
            int result = -1;
            RandomAccessFile randomAccessFile = null;
            String path = Values.getString(id, "path");
            File file = new File(path);
            StringBuilder sb = new StringBuilder();
            sb.append("path=");
            sb.append(path);
            sb.append(", offset=");
            sb.append(offset);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.readFromFile start", sb.toString());
            if (!file.exists()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("path=[");
                sb2.append(path);
                sb2.append("] is not found");
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.readFromFile", sb2.toString());
                return -1;
            }
            try {
                randomAccessFile = new RandomAccessFile(new File(path), "r");
                try {
                    randomAccessFile.seek(offset);
                    result = randomAccessFile.read();
                    randomAccessFile.close();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("path=");
                    sb3.append(path);
                    sb3.append(", offset=");
                    sb3.append(offset);
                    sb3.append(", result=");
                    sb3.append(result);
                    XmlUtil.log_d(Support.CLASS_NAME, "Kernel.readFromFile end", sb3.toString());
                    return result;
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        randomAccessFile.close();
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                    return result;
                }
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
                return -1;
            }
        }

        public static String readFromPath(String path, boolean isLog) {
            String value = null;
            BufferedReader value2 = null;
            if (path == null || Properties.PROPERTIES_DEFAULT_STRING.equalsIgnoreCase(path)) {
                return null;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                value2 = reader.readLine();
                if (value2 != null) {
                    value = value2.trim();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (value2 != null) {
                    value2.close();
                }
            } finally {
                if (value2 != null) {
                    try {
                        value2.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
            }
            if (isLog) {
                StringBuilder sb = new StringBuilder();
                sb.append("path=");
                sb.append(path);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.read", sb.toString());
            }
            return value;
        }

        public static String readAllFromPath(String path, boolean isLog) {
            String value = "";
            BufferedReader value2 = null;
            if (path == null || Properties.PROPERTIES_DEFAULT_STRING.equalsIgnoreCase(path)) {
                return value;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                while (true) {
                    String temp_value = reader.readLine();
                    if (temp_value == null) {
                        break;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(value2);
                    sb.append(" ");
                    sb.append(temp_value);
                    value = sb.toString();
                }
                if (value2 != null) {
                    value = value2.trim();
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (value2 != null) {
                    value2.close();
                }
            } finally {
                if (value2 != null) {
                    try {
                        value2.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
            }
            if (isLog) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("path=");
                sb2.append(path);
                sb2.append(", value=");
                sb2.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.readAllLine", sb2.toString());
            }
            return value;
        }

        public static String readAllFromID(String id, boolean isLog) {
            return readAllFromPath(Values.getString(id, "path"), isLog);
        }

        public static String readAllFromID(String id) {
            return readAllFromID(id, true);
        }

        public static String read(String id) {
            return read(id, true);
        }

        public static String read(String id, boolean isLog) {
            return read(id, isLog, true);
        }

        public static int readVal(String id) {
            String path = Values.getString(id, "path");
            int retVal = 0;
            String value = null;
            try {
                BufferedReader reader = new BufferedReader(new FileReader(path));
                value = reader.readLine();
                if (value != null) {
                    retVal = Integer.parseInt(value.trim());
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (value != null) {
                    value.close();
                }
            } finally {
                if (value != null) {
                    try {
                        value.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("path=");
            sb.append(path);
            sb.append(", retVal=");
            sb.append(retVal);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.readVal", sb.toString());
            return retVal;
        }

        public static byte[] readByte(String id, int size, boolean isLog) {
            return readByteFromPath(Values.getString(id, "path"), size, isLog);
        }

        public static byte[] readByte(String id, boolean isLog) {
            return readByteFromPath(Values.getString(id, "path"), -1, isLog);
        }

        public static byte[] readByteFromPath(String path, int size, boolean isLog) {
            int mSize;
            File f = new File(path);
            FileInputStream fis = null;
            if (size < 0) {
                mSize = (int) f.length();
            } else {
                mSize = size;
            }
            byte[] result = new byte[mSize];
            try {
                FileInputStream fis2 = new FileInputStream(path);
                int pos = 0;
                int bufferSize = 10;
                while (true) {
                    int read = fis2.read(result, pos, bufferSize);
                    bufferSize = read;
                    if (read <= 0) {
                        break;
                    }
                    pos += bufferSize;
                    int temp = result.length - pos;
                    if (temp < 10) {
                        bufferSize = temp;
                    }
                }
                fis2.close();
                try {
                    fis2.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                if (fis != null) {
                    fis.close();
                }
            } catch (Throwable th) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
                throw th;
            }
            return result;
        }

        public static boolean write(String id, String value) {
            String path = Values.getString(id, "path");
            boolean res = true;
            FileWriter writer = null;
            StringBuilder sb = new StringBuilder();
            sb.append("path=");
            sb.append(path);
            sb.append(", value=");
            sb.append(value);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.write start", sb.toString());
            try {
                writer = new FileWriter(path);
                writer.write(value);
                try {
                    writer.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                    res = false;
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                res = false;
                if (writer != null) {
                    writer.close();
                }
            } catch (Throwable th) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
                throw th;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("path=");
            sb2.append(path);
            sb2.append(", value=");
            sb2.append(value);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.write end", sb2.toString());
            return res;
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=byte[], code=java.io.FileOutputStream, for r8v0, types: [byte[]] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static boolean write(java.lang.String r7, java.io.FileOutputStream r8) {
            /*
                java.lang.String r0 = "path"
                java.lang.String r0 = com.sec.xmldata.support.Support.Values.getString(r7, r0)
                r1 = 1
                r2 = 0
                java.lang.String r3 = "Support"
                java.lang.String r4 = "Kernel.write start"
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                java.lang.String r6 = "path="
                r5.append(r6)
                r5.append(r0)
                java.lang.String r6 = ", value="
                r5.append(r6)
                if (r8 == 0) goto L_0x0025
                java.lang.String r6 = java.util.Arrays.toString(r8)
                goto L_0x0027
            L_0x0025:
                java.lang.String r6 = "null"
            L_0x0027:
                r5.append(r6)
                java.lang.String r5 = r5.toString()
                com.sec.xmldata.support.XmlUtil.log_d(r3, r4, r5)
                java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0044 }
                r3.<init>(r0)     // Catch:{ Exception -> 0x0044 }
                r2 = r3
                r2.write(r8)     // Catch:{ Exception -> 0x0044 }
                r2.flush()     // Catch:{ Exception -> 0x0044 }
                r2.close()     // Catch:{ IOException -> 0x004f }
                goto L_0x0055
            L_0x0042:
                r3 = move-exception
                goto L_0x0080
            L_0x0044:
                r3 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r3)     // Catch:{ all -> 0x0042 }
                r1 = 0
                if (r2 == 0) goto L_0x0055
                r2.close()     // Catch:{ IOException -> 0x004f }
                goto L_0x0055
            L_0x004f:
                r3 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r3)
                r1 = 0
                goto L_0x0056
            L_0x0055:
            L_0x0056:
                java.lang.String r3 = "Support"
                java.lang.String r4 = "Kernel.write end"
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                java.lang.String r6 = "path="
                r5.append(r6)
                r5.append(r0)
                java.lang.String r6 = ", value="
                r5.append(r6)
                if (r8 == 0) goto L_0x0073
                java.lang.String r6 = java.util.Arrays.toString(r8)
                goto L_0x0075
            L_0x0073:
                java.lang.String r6 = "null"
            L_0x0075:
                r5.append(r6)
                java.lang.String r5 = r5.toString()
                com.sec.xmldata.support.XmlUtil.log_d(r3, r4, r5)
                return r1
            L_0x0080:
                if (r2 == 0) goto L_0x008d
                r2.close()     // Catch:{ IOException -> 0x0087 }
                goto L_0x008d
            L_0x0087:
                r4 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r4)
                r1 = 0
            L_0x008d:
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.xmldata.support.Support.Kernel.write(java.lang.String, byte[]):boolean");
        }

        public static boolean writeToPath(String path, byte[] value) {
            return writeToPath(path, value, true);
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=boolean, code=java.io.FileOutputStream, for r8v0, types: [boolean] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static boolean writeToPath(java.lang.String r6, byte[] r7, java.io.FileOutputStream r8) {
            /*
                if (r7 != 0) goto L_0x000d
                java.lang.String r0 = "Support"
                java.lang.String r1 = "Kernel.writeToPath"
                java.lang.String r2 = "value is null, return false"
                com.sec.xmldata.support.XmlUtil.log_d(r0, r1, r2)
                r0 = 0
                return r0
            L_0x000d:
                r0 = 1
                r1 = 0
                if (r8 == 0) goto L_0x0035
                java.lang.String r2 = "Support"
                java.lang.String r3 = "Kernel.writeToPath start"
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "path="
                r4.append(r5)
                r4.append(r6)
                java.lang.String r5 = ", value="
                r4.append(r5)
                java.lang.String r5 = java.util.Arrays.toString(r7)
                r4.append(r5)
                java.lang.String r4 = r4.toString()
                com.sec.xmldata.support.XmlUtil.log_d(r2, r3, r4)
            L_0x0035:
                java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ Exception -> 0x0048 }
                r2.<init>(r6)     // Catch:{ Exception -> 0x0048 }
                r1 = r2
                r1.write(r7)     // Catch:{ Exception -> 0x0048 }
                r1.flush()     // Catch:{ Exception -> 0x0048 }
                r1.close()     // Catch:{ IOException -> 0x0053 }
                goto L_0x0059
            L_0x0046:
                r2 = move-exception
                goto L_0x0089
            L_0x0048:
                r2 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r2)     // Catch:{ all -> 0x0046 }
                r0 = 0
                if (r1 == 0) goto L_0x0059
                r1.close()     // Catch:{ IOException -> 0x0053 }
                goto L_0x0059
            L_0x0053:
                r2 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r2)
                r0 = 0
                goto L_0x005a
            L_0x0059:
            L_0x005a:
                if (r8 == 0) goto L_0x0088
                java.lang.String r2 = "Support"
                java.lang.String r3 = "Kernel.writeToPath end"
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "path="
                r4.append(r5)
                r4.append(r6)
                java.lang.String r5 = ", value="
                r4.append(r5)
                java.lang.String r5 = java.util.Arrays.toString(r7)
                r4.append(r5)
                java.lang.String r5 = ", res="
                r4.append(r5)
                r4.append(r0)
                java.lang.String r4 = r4.toString()
                com.sec.xmldata.support.XmlUtil.log_d(r2, r3, r4)
            L_0x0088:
                return r0
            L_0x0089:
                if (r1 == 0) goto L_0x0096
                r1.close()     // Catch:{ IOException -> 0x0090 }
                goto L_0x0096
            L_0x0090:
                r3 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r3)
                r0 = 0
            L_0x0096:
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.xmldata.support.Support.Kernel.writeToPath(java.lang.String, byte[], boolean):boolean");
        }

        public static boolean writeToPath(String path, String value) {
            return writeToPath(path, value, true);
        }

        /* JADX WARNING: Incorrect type for immutable var: ssa=boolean, code=java.io.FileWriter, for r8v0, types: [boolean] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static boolean writeToPath(java.lang.String r6, java.lang.String r7, java.io.FileWriter r8) {
            /*
                r0 = 1
                r1 = 0
                if (r8 == 0) goto L_0x0024
                java.lang.String r2 = "Support"
                java.lang.String r3 = "Kernel.writeToPath start"
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "path="
                r4.append(r5)
                r4.append(r6)
                java.lang.String r5 = ", value="
                r4.append(r5)
                r4.append(r7)
                java.lang.String r4 = r4.toString()
                com.sec.xmldata.support.XmlUtil.log_d(r2, r3, r4)
            L_0x0024:
                java.io.FileWriter r2 = new java.io.FileWriter     // Catch:{ Exception -> 0x003b }
                r2.<init>(r6)     // Catch:{ Exception -> 0x003b }
                r1 = r2
                r1.write(r7)     // Catch:{ Exception -> 0x003b }
                r1.close()     // Catch:{ IOException -> 0x0032 }
                goto L_0x0038
            L_0x0032:
                r2 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r2)
                r0 = 0
                goto L_0x0046
            L_0x0038:
                goto L_0x0046
            L_0x0039:
                r2 = move-exception
                goto L_0x0069
            L_0x003b:
                r2 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r2)     // Catch:{ all -> 0x0039 }
                r0 = 0
                if (r1 == 0) goto L_0x0038
                r1.close()     // Catch:{ IOException -> 0x0032 }
                goto L_0x0038
            L_0x0046:
                if (r8 == 0) goto L_0x0068
                java.lang.String r2 = "Support"
                java.lang.String r3 = "Kernel.writeToPath end"
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                java.lang.String r5 = "path="
                r4.append(r5)
                r4.append(r6)
                java.lang.String r5 = ", value="
                r4.append(r5)
                r4.append(r7)
                java.lang.String r4 = r4.toString()
                com.sec.xmldata.support.XmlUtil.log_d(r2, r3, r4)
            L_0x0068:
                return r0
            L_0x0069:
                if (r1 == 0) goto L_0x0076
                r1.close()     // Catch:{ IOException -> 0x0070 }
                goto L_0x0076
            L_0x0070:
                r3 = move-exception
                com.sec.xmldata.support.XmlUtil.log_e(r3)
                r0 = 0
            L_0x0076:
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.sec.xmldata.support.Support.Kernel.writeToPath(java.lang.String, java.lang.String, boolean):boolean");
        }

        public static boolean writeToFile(String path, long offset, String value) {
            RandomAccessFile randomAccessFile = null;
            File file = new File(path);
            StringBuilder sb = new StringBuilder();
            sb.append("path=");
            sb.append(path);
            sb.append(", offset=");
            sb.append(offset);
            sb.append(", value=");
            sb.append(value);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeToFile start", sb.toString());
            if (!file.exists()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("path=[");
                sb2.append(path);
                sb2.append("] is not found");
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeToFile", sb2.toString());
                return false;
            }
            try {
                randomAccessFile = new RandomAccessFile(new File(path), "rw");
                try {
                    randomAccessFile.seek(offset);
                    randomAccessFile.write(value.getBytes());
                    randomAccessFile.close();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("path=");
                    sb3.append(path);
                    sb3.append(", offset=");
                    sb3.append(offset);
                    sb3.append(", value=");
                    sb3.append(value);
                    XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeToFile end", sb3.toString());
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        randomAccessFile.close();
                    } catch (Exception err) {
                        err.printStackTrace();
                    }
                    return false;
                }
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
                return false;
            }
        }

        public static boolean writeToPathNsync(String path, String value) {
            return writeToPathNsync(path, value.getBytes());
        }

        public static boolean writeToPathNsync(String path, byte[] value) {
            if (value == null) {
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeToPathNsync", "value is null, return false");
                return false;
            }
            boolean res = true;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path);
                out.write(value);
                out.flush();
                out.getFD().sync();
                try {
                    out.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                    res = false;
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                res = false;
                if (out != null) {
                    out.close();
                }
            } catch (Throwable th) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
                throw th;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("path=");
            sb.append(path);
            sb.append(", value=");
            sb.append(Arrays.toString(value));
            sb.append(", res=");
            sb.append(res);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeToPathNsync", sb.toString());
            return res;
        }

        public static boolean writeToPathNsync(String path, String value, boolean writeType) {
            return writeToPathNsync(path, value.getBytes(), writeType);
        }

        public static boolean writeToPathNsync(String path, byte[] value, boolean writeType) {
            if (value == null) {
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeToPathNsync", "value is null, return false");
                return false;
            }
            boolean res = true;
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path, writeType);
                out.write(value);
                out.flush();
                out.getFD().sync();
                try {
                    out.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                    res = false;
                }
            } catch (Exception e2) {
                XmlUtil.log_e(e2);
                res = false;
                if (out != null) {
                    out.close();
                }
            } catch (Throwable th) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e3) {
                        XmlUtil.log_e(e3);
                    }
                }
                throw th;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("path=");
            sb.append(path);
            sb.append(", value=");
            sb.append(Arrays.toString(value));
            sb.append(", writeType=");
            sb.append(writeType);
            sb.append(", res=");
            sb.append(res);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeToPathNsync", sb.toString());
            return res;
        }

        public static boolean writeNsync(String id, String value) {
            if (id == null) {
                XmlUtil.log_w(Support.CLASS_NAME, "Kernel.writeNsync", "id=null");
                return false;
            } else if (value == null) {
                XmlUtil.log_w(Support.CLASS_NAME, "Kernel.writeNsync", "value=null");
                return false;
            } else {
                String path = Values.getString(id, "path");
                boolean res = true;
                FileOutputStream out = null;
                byte[] bValue = value.getBytes();
                if (bValue == null) {
                    XmlUtil.log_w(Support.CLASS_NAME, "Kernel.writeNsync", "bValue=null");
                    return false;
                }
                try {
                    out = new FileOutputStream(path);
                    out.write(bValue);
                    out.flush();
                    out.getFD().sync();
                    try {
                        out.close();
                    } catch (IOException e) {
                        XmlUtil.log_e(e);
                        res = false;
                    }
                } catch (Exception e2) {
                    XmlUtil.log_e(e2);
                    res = false;
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable th) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e3) {
                            XmlUtil.log_e(e3);
                        }
                    }
                    throw th;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("path=");
                sb.append(path);
                sb.append(", value=");
                sb.append(value);
                sb.append("res=");
                sb.append(res);
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeNsync", sb.toString());
                return res;
            }
        }

        public static boolean writeNsyncContinuous(String id, String value) {
            if (id == null) {
                XmlUtil.log_w(Support.CLASS_NAME, "Kernel.writeNsyncContinuous", "id=null");
                return false;
            } else if (value == null) {
                XmlUtil.log_w(Support.CLASS_NAME, "Kernel.writeNsyncContinuous", "value=null");
                return false;
            } else {
                String path = Values.getString(id, "path");
                boolean res = true;
                FileOutputStream out = null;
                byte[] bValue = value.getBytes();
                if (bValue == null) {
                    XmlUtil.log_w(Support.CLASS_NAME, "Kernel.writeNsyncContinuous", "bValue=null");
                    return false;
                }
                try {
                    out = new FileOutputStream(path, true);
                    out.write(bValue);
                    out.flush();
                    out.getFD().sync();
                    try {
                        out.close();
                    } catch (IOException e) {
                        XmlUtil.log_e(e);
                        res = false;
                    }
                } catch (Exception e2) {
                    XmlUtil.log_e(e2);
                    res = false;
                    if (out != null) {
                        out.close();
                    }
                } catch (Throwable th) {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e3) {
                            XmlUtil.log_e(e3);
                        }
                    }
                    throw th;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("path=");
                sb.append(path);
                sb.append(", value=");
                sb.append(value);
                sb.append("res=");
                sb.append(res);
                XmlUtil.log_d(Support.CLASS_NAME, "Kernel.writeNsyncContinuous", sb.toString());
                return res;
            }
        }

        public static boolean deleteFile(String id) {
            if (id != null) {
                return new File(getFilePath(id)).delete();
            }
            return false;
        }

        public static boolean isExistFile(String id) {
            if (id != null) {
                return new File(getFilePath(id)).exists();
            }
            return false;
        }

        public static boolean isExistFilePath(String path) {
            if (path != null) {
                return new File(path).exists();
            }
            return false;
        }

        public static boolean isExistFileID(String id) {
            return new File(Values.getString(id, "path")).exists();
        }

        public static boolean isReadableFile(String id) {
            if (id != null) {
                return new File(getFilePath(id)).canRead();
            }
            return false;
        }

        public static long getFileLength(String path) {
            return new File(path).length();
        }

        public static boolean mkDir(String path) {
            if (path == null || path.length() <= 0) {
                return false;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("path=");
            sb.append(path);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.mkDir", sb.toString());
            int index = path.lastIndexOf("/");
            StringBuilder sb2 = new StringBuilder();
            sb2.append("index=");
            sb2.append(index);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.mkDir", sb2.toString());
            String dir = path.substring(0, index);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("dir=");
            sb3.append(dir);
            XmlUtil.log_d(Support.CLASS_NAME, "Kernel.mkDir", sb3.toString());
            File f = new File(dir);
            if (!f.exists()) {
                return f.mkdir();
            }
            return true;
        }

        public static boolean setPermission(String path, boolean excutable, boolean owneronlyE, boolean writable, boolean owneronlyW, boolean readable, boolean owneronlyR) {
            File f = new File(path);
            if (f.exists()) {
                f.setExecutable(excutable, owneronlyE);
                f.setWritable(writable, owneronlyW);
                f.setReadable(readable, owneronlyR);
                return true;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("File not found : ");
            sb.append(path);
            XmlUtil.log_e(Support.CLASS_NAME, "setPermission", sb.toString());
            return false;
        }

        public static String getFilePermission(String path) {
            String permission = "";
            int owner = 0;
            int group = 0;
            int others = 0;
            StringBuilder sb = new StringBuilder();
            sb.append("ls -al ");
            sb.append(path);
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"/system/bin/sh", "-c", sb.toString()});
                p.waitFor();
                String strFilePermission = new BufferedReader(new InputStreamReader(p.getInputStream())).readLine().substring(0, 10);
                XmlUtil.log_d(Support.CLASS_NAME, "getFilePermission", strFilePermission);
                if (strFilePermission.charAt(1) == 'r') {
                    owner = 0 + 4;
                }
                if (strFilePermission.charAt(2) == 'w') {
                    owner += 2;
                }
                if (strFilePermission.charAt(3) == 'x') {
                    owner++;
                }
                if (strFilePermission.charAt(4) == 'r') {
                    group = 0 + 4;
                }
                if (strFilePermission.charAt(5) == 'w') {
                    group += 2;
                }
                if (strFilePermission.charAt(6) == 'x') {
                    group++;
                }
                if (strFilePermission.charAt(7) == 'r') {
                    others = 0 + 4;
                }
                if (strFilePermission.charAt(8) == 'w') {
                    others += 2;
                }
                if (strFilePermission.charAt(9) == 'x') {
                    others++;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append(permission);
                sb2.append(owner);
                String permission2 = sb2.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append(permission2);
                sb3.append(group);
                String permission3 = sb3.toString();
                StringBuilder sb4 = new StringBuilder();
                sb4.append(permission3);
                sb4.append(others);
                String permission4 = sb4.toString();
                XmlUtil.log_d(Support.CLASS_NAME, "getFilePermission", permission4);
                return permission4;
            } catch (Exception e) {
                XmlUtil.log_e(e);
                return permission;
            }
        }

        public static String findFLog(String _strRDMKey) {
            String strLogListPath = "NONE";
            StringBuilder sb = new StringBuilder();
            sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            sb.append("/log/LogList");
            File fileLogListFolder = new File(sb.toString());
            if (!fileLogListFolder.exists()) {
                XmlUtil.log_e(Support.CLASS_NAME, "findFLog", "not exist log list folder");
            } else {
                XmlUtil.log_e(Support.CLASS_NAME, "findFLog", "exist log list folder");
                File[] filesLogList = fileLogListFolder.listFiles();
                for (int i = 0; i < filesLogList.length; i++) {
                    if (filesLogList[i].getName().toLowerCase().contains(_strRDMKey.toLowerCase())) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("i find ");
                        sb2.append(filesLogList[i].getName());
                        XmlUtil.log_e(Support.CLASS_NAME, "findFLog", sb2.toString());
                        strLogListPath = filesLogList[i].getName();
                    }
                }
            }
            return strLogListPath;
        }

        public static String getUniqueNumber() {
            String strUN;
            XmlUtil.log_d(Support.CLASS_NAME, "getUniqueNumber");
            String strUN2 = "";
            if (isExistFile(UFS_UN)) {
                strUN = read(UFS_UN);
            } else if (isExistFile(EMMC_UN)) {
                strUN = read(EMMC_UN);
            } else if (!isExistFile(EMMC_CID)) {
                return Properties.PROPERTIES_DEFAULT_STRING;
            } else {
                String cid = read(EMMC_CID);
                String memory_name = read(EMMC_NAME);
                String str = "";
                String PNM = "";
                XmlUtil.log_d(Support.CLASS_NAME, "getUniqueNumber", "cid : \" + cid + \", memory_name : \" + memory_name");
                StringBuilder sb = new StringBuilder();
                sb.append(strUN2);
                sb.append("c");
                strUN = sb.toString();
                if (cid != null) {
                    String eMMC = cid.substring(0, 2);
                    if (memory_name != null) {
                        if (eMMC.equalsIgnoreCase(HwTestMenu.LCD_TEST_GRIP)) {
                            PNM = memory_name.substring(0, 2);
                        } else if (eMMC.equalsIgnoreCase("02") || eMMC.equalsIgnoreCase("45")) {
                            PNM = memory_name.substring(3, 5);
                        } else if (eMMC.equalsIgnoreCase("11") || eMMC.equalsIgnoreCase("90")) {
                            PNM = memory_name.substring(1, 3);
                        } else if (eMMC.equalsIgnoreCase("FE")) {
                            PNM = memory_name.substring(4, 6);
                        }
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(strUN);
                    sb2.append(PNM);
                    String strUN3 = sb2.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(strUN3);
                    sb3.append(cid.substring(18, 20));
                    String strUN4 = sb3.toString();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(strUN4);
                    sb4.append(cid.substring(20, 28));
                    String strUN5 = sb4.toString();
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(strUN5);
                    sb5.append(cid.substring(28, 30));
                    strUN = sb5.toString();
                }
            }
            if (strUN != null) {
                strUN = strUN.toUpperCase();
            }
            String str2 = Support.CLASS_NAME;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("getUniqueNumber, Unique Number : ");
            sb6.append(strUN);
            XmlUtil.log_d(str2, sb6.toString());
            return strUN;
        }
    }

    public static class Properties {
        public static final String ABC_LOG_RUNNING = "ABC_LOG_RUNNING";
        public static final String ABC_LOG_TRIGGER = "ABC_LOG_TRIGGER";
        public static final String AP_FUSING_VALUE = "AP_FUSING_VALUE";
        public static final String BACKUP_INTERNEL_MEMORY_SIZE = "BACKUP_INTERNEL_MEMORY_SIZE";
        public static final String BACK_COVER_TYPE = "BACK_COVER_TYPE";
        public static final String BINARY_TYPE = "BINARY_TYPE";
        public static final String BOOTLOADER_VERSION = "BOOTLOADER_VERSION";
        public static final String BOOT_COMPLETED = "BOOT_COMPLETED";
        public static final String BOOT_VERSION = "BOOT_VERSION";
        public static final String BUILD_TYPE = "BUILD_TYPE";
        public static final String CONTENTS_VERSION = "CONTENTS_VERSION";
        public static final String CPNV_BACKUP_CMD = "CPNV_BACKUP_CMD";
        public static final String CP_RAM_LOGGING = "CP_RAM_LOGGING";
        public static final String CSC_VERSION = "CSC_VERSION";
        public static final String DEVICETEST_STATUS = "DEVICETEST_STATUS";
        public static final String DUALMODE_STATE = "DUALMODE_STATE";
        public static final String EMMC_CHECKSUM = "EMMC_CHECKSUM";
        public static final String FACM_NFC_TEST = "FACM_NFC_TEST";
        public static final String FACM_SECURITY_MODE = "FACM_SECURITY_MODE";
        public static final String FACTORYAPP_FAILHIST = "FACTORYAPP_FAILHIST";
        public static final String FACTORYAPP_FAILHIST_RDMKEY = "FACTORYAPP_FAILHIST_RDMKEY";
        public static final String FACTORYAPP_FAILHIST_TIME = "FACTORYAPP_FAILHIST_TIME";
        public static final String FLS_FOLDER_NAME = "FLS_FOLDER_NAME";
        public static final String FTL_GADGET_MODE = "FTL_GADGET_MODE";
        public static final String HDCP_SN = "HDCP_SN";
        public static final String HW_REVISION = "HW_REVISION";
        public static final String HW_VERSION = "HW_VERSION";
        public static final String ISS_ENABLED_STATE = "ISS_ENABLED_STATE";
        public static final String LTE_HIDDEN_VERSION = "LTE_HIDDEN_VERSION";
        public static final String LTE_VERSION = "LTE_VERSION";
        public static final String MAIN_CHIP_NAME_AP = "MAIN_CHIP_NAME_AP";
        public static final String MAIN_CHIP_NAME_AP_REAL = "MAIN_CHIP_NAME_AP_REAL";
        public static final String MAIN_CHIP_NAME_AP_REAL_OLD = "MAIN_CHIP_NAME_AP_REAL_OLD";
        public static final String MAIN_CHIP_NAME_CP = "MAIN_CHIP_NAME_CP";
        public static final String MAIN_CHIP_NAME_CP2 = "MAIN_CHIP_NAME_CP2";
        public static final String OS_VERSION = "OS_VERSION";
        public static final String PDA_VERSION = "PDA_VERSION";
        public static final String PGM_STAGE = "PGM_STAGE";
        public static final String PHONE_VERSION = "PHONE_VERSION";
        public static final String PRODUCTION_DATE = "PRODUCTION_DATE";
        public static final String PRODUCT_NAME = "PRODUCT_NAME";
        public static final String PROPERTIES_DEFAULT_STRING = "Unknown";
        public static final String PROTOTYPE_SN = "PROTOTYPE_SN";
        public static final String SERIAL_NUMBER = "SERIAL_NUMBER";
        public static final String SW_VERSION = "SW_VERSION";
        public static final String TAG = "Properties";
        public static final String UART_1K_VALUE = "UART_1K_VALUE";
        public static final String WARRANTY_BIT_CHECK = "WARRANTY_BIT_CHECK";
        public static final String WARRANTY_BIT_CHECK_OLD = "WARRANTY_BIT_CHECK_OLD";

        public static String get(String id) {
            String property = Values.getString(id, "key");
            StringBuilder sb = new StringBuilder();
            sb.append("property=");
            sb.append(property);
            XmlUtil.log_d(Support.CLASS_NAME, "Properties.get", sb.toString());
            return SystemProperties.get(property, PROPERTIES_DEFAULT_STRING);
        }

        public static String getProp(String key) {
            StringBuilder sb = new StringBuilder();
            sb.append("property=");
            sb.append(key);
            XmlUtil.log_d(Support.CLASS_NAME, "Properties.get", sb.toString());
            return SystemProperties.get(key, PROPERTIES_DEFAULT_STRING);
        }

        public static void set(String id, String value) {
            String property = Values.getString(id, "key");
            StringBuilder sb = new StringBuilder();
            sb.append("property=");
            sb.append(property);
            sb.append(", value=");
            sb.append(value);
            XmlUtil.log_d(Support.CLASS_NAME, "Properties.set", sb.toString());
            SystemProperties.set(property, value);
        }
    }

    public static class SensorTestMenu {
        public static final String NAME_BAROMETER = "Barometer";
        public static final String NAME_LIGHT_LUX = "Light_LUX";
        public static final String SENSOR_TEST_NAME_HRM = "HRM";
        public static final String SENSOR_TEST_NAME_TEMP_HUMI = "Temp_Humid";
        public static final String SENSOR_TEST_NAME_UV = "UV";
        public static final String TAG = "SensorTestMenu";

        public static String[] getSensorTest() {
            String str;
            Element[] items = XMLDataStorage.instance().getChildElementSet(TAG);
            ArrayList<String> testList = new ArrayList<>();
            for (Element item : items) {
                boolean parent_Enable = Boolean.parseBoolean(item.getAttribute("enable"));
                StringBuilder sb = new StringBuilder();
                sb.append("name=");
                sb.append(item.getAttribute("name"));
                sb.append(", enable=");
                sb.append(parent_Enable);
                XmlUtil.log_v(Support.CLASS_NAME, "getFact", sb.toString());
                if (parent_Enable) {
                    String parent_Name = item.getAttribute("name");
                    String parent_ID = item.getAttribute("action");
                    if (item.getAttribute("testcase") != null) {
                        str = item.getAttribute("testcase");
                    } else {
                        str = "default";
                    }
                    String parent_Testcase = str;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(parent_ID);
                    sb2.append(",");
                    sb2.append(parent_Name);
                    sb2.append(",");
                    sb2.append(parent_Testcase);
                    sb2.append(",null");
                    testList.add(sb2.toString());
                }
            }
            return (String[]) testList.toArray(new String[0]);
        }

        public static String getTestCase(String name) {
            Element[] items;
            String result = null;
            for (Element item : XMLDataStorage.instance().getChildElementSet(TAG)) {
                if (name.equals(item.getAttribute("name"))) {
                    result = item.getAttribute("testcase");
                    StringBuilder sb = new StringBuilder();
                    sb.append("name =");
                    sb.append(name);
                    sb.append(", testcase =");
                    sb.append(result);
                    XmlUtil.log_v(Support.CLASS_NAME, "getTestCase", sb.toString());
                }
            }
            return result;
        }

        public static String getEnabled(String name) {
            Element[] items;
            String result = null;
            for (Element item : XMLDataStorage.instance().getChildElementSet(TAG)) {
                if (name.equals(item.getAttribute("name"))) {
                    result = item.getAttribute("enable");
                    StringBuilder sb = new StringBuilder();
                    sb.append("name =");
                    sb.append(name);
                    sb.append(", enable =");
                    sb.append(result);
                    XmlUtil.log_v(Support.CLASS_NAME, "getEnabled", sb.toString());
                }
            }
            return result;
        }

        public static String getTestCaseFromAction(String action) {
            Element[] items;
            String result = null;
            for (Element item : XMLDataStorage.instance().getChildElementSet(TAG)) {
                if (action.equals(item.getAttribute("action"))) {
                    result = item.getAttribute("testcase");
                    StringBuilder sb = new StringBuilder();
                    sb.append("action =");
                    sb.append(action);
                    sb.append(", testcase =");
                    sb.append(result);
                    XmlUtil.log_v(Support.CLASS_NAME, "getTestCaseFromAction", sb.toString());
                }
            }
            return result;
        }

        public static float getUIRate(String name) {
            Element[] items;
            String rate = null;
            for (Element item : XMLDataStorage.instance().getChildElementSet(HwTestMenu.TAG)) {
                if (name.equals(item.getAttribute("name"))) {
                    rate = item.getAttribute("uirate");
                    StringBuilder sb = new StringBuilder();
                    sb.append("name =");
                    sb.append(name);
                    sb.append(", uirate = ");
                    sb.append(rate);
                    XmlUtil.log_v(Support.CLASS_NAME, "getUIRate", sb.toString());
                }
            }
            if (rate == null || rate.isEmpty()) {
                return 1.0f;
            }
            return Float.parseFloat(rate);
        }
    }

    public static class Spec {
        public static final String AFC2_CURRRENT_SPEC_MAX = "AFC2_CURRRENT_SPEC_MAX";
        public static final String AFC2_CURRRENT_SPEC_MIN = "AFC2_CURRRENT_SPEC_MIN";
        public static final String AFC_CURRRENT_SPEC_MAX = "AFC_CURRRENT_SPEC_MAX";
        public static final String AFC_CURRRENT_SPEC_MIN = "AFC_CURRRENT_SPEC_MIN";
        public static final String AFC_VOLTAGE = "AFC_VOLTAGE";
        public static final String ALWAYSON_MIC_EAR_VOLUME = "ALWAYSON_MIC_EAR_VOLUME";
        public static final String ALWAYSON_MIC_VOLUME = "ALWAYSON_MIC_VOLUME";
        public static final String APERTURE_BIG_CENTER = "APERTURE_BIG_CENTER";
        public static final String APERTURE_BIG_RANGE_MAX = "APERTURE_BIG_RANGE_MAX";
        public static final String APERTURE_BIG_RANGE_MIN = "APERTURE_BIG_RANGE_MIN";
        public static final String APERTURE_SMALL_CENTER = "APERTURE_SMALL_CENTER";
        public static final String APERTURE_SMALL_RANGE_MAX = "APERTURE_SMALL_RANGE_MAX";
        public static final String APERTURE_SMALL_RANGE_MIN = "APERTURE_SMALL_RANGE_MIN";
        public static final String BLACK_LCD_BRIGHTNESS = "BLACK_LCD_BRIGHTNESS";
        public static final String COLOR_BALANCE_DEFAULT_VALUE = "COLOR_BALANCE_DEFAULT_VALUE";
        public static final String COLOR_BALANCE_DEFAULT_VALUE_2 = "COLOR_BALANCE_DEFAULT_VALUE";
        public static final String COLOR_BALANCE_VALUE_LIST = "COLOR_BALANCE_VALUE_LIST";
        public static final String COPR_READ_PASS_VAL = "COPR_READ_PASS_VAL";
        public static final String DDI_TEST_REFERENCE_CHECKSUM = "DDI_TEST_REFERENCE_CHECKSUM";
        public static final String DEFAULT_BATT_TYPE = "DEFAULT_BATT_TYPE";
        public static final String DIGITIZER_HEIGHT_BASIS = "DIGITIZER_HEIGHT_BASIS";
        public static final String DIGITIZER_WIDTH_BASIS = "DIGITIZER_WIDTH_BASIS";
        public static final String DIRECT_CHARGE_CHECK_CURRENT_DELAY = "DIRECT_CHARGE_CHECK_CURRENT_DELAY";
        public static final String DIRECT_CHARGE_CHECK_VOLTAGE_DELAY = "DIRECT_CHARGE_CHECK_VOLTAGE_DELAY";
        public static final String DIRECT_CHARGE_SPEC_STEP1_MAX = "DIRECT_CHARGE_SPEC_STEP1_MAX";
        public static final String DIRECT_CHARGE_SPEC_STEP1_MIN = "DIRECT_CHARGE_SPEC_STEP1_MIN";
        public static final String DIRECT_CHARGE_SPEC_STEP2_MAX = "DIRECT_CHARGE_SPEC_STEP2_MAX";
        public static final String DIRECT_CHARGE_SPEC_STEP2_MIN = "DIRECT_CHARGE_SPEC_STEP2_MIN";
        public static final String DIRECT_CHARGE_SPEC_STEP3_MAX = "DIRECT_CHARGE_SPEC_STEP3_MAX";
        public static final String DIRECT_CHARGE_SPEC_STEP3_MIN = "DIRECT_CHARGE_SPEC_STEP3_MIN";
        public static final String DPSWITCH_CCIC_SPEC_MAX = "DPSWITCH_CCIC_SPEC_MAX";
        public static final String DPSWITCH_CCIC_SPEC_MIN = "DPSWITCH_CCIC_SPEC_MIN";
        public static final String DPSWITCH_SBU_SPEC_MAX = "DPSWITCH_SBU_SPEC_MAX";
        public static final String DPSWITCH_SBU_SPEC_MIN = "DPSWITCH_SBU_SPEC_MIN";
        public static final String FINGERPRINT_AFEBALANCE_MAX_NAMSAN = "FINGERPRINT_AFEBALANCE_MAX_NAMSAN";
        public static final String FINGERPRINT_AFEBALANCE_MIN_NAMSAN = "FINGERPRINT_AFEBALANCE_MIN_NAMSAN";
        public static final String FINGERPRINT_AFE_MAX = "FINGERPRINT_AFE_MAX";
        public static final String FINGERPRINT_AFE_MIN = "FINGERPRINT_AFE_MIN";
        public static final String FINGERPRINT_ALLOWED_COUNTS_MAX = "FINGERPRINT_ALLOWED_COUNTS_MAX";
        public static final String FINGERPRINT_ALLOWED_COUNTS_MIN = "FINGERPRINT_ALLOWED_COUNTS_MIN";
        public static final String FINGERPRINT_AREA_MAX_PIXEL_FAIL_MAX_NAMSAN = "FINGERPRINT_AREA_MAX_PIXEL_FAIL_MAX_NAMSAN";
        public static final String FINGERPRINT_AREA_MAX_PIXEL_FAIL_MIN_NAMSAN = "FINGERPRINT_AREA_MAX_PIXEL_FAIL_MIN_NAMSAN";
        public static final String FINGERPRINT_BAD_PIXEL_BLOCK_MAX_EGIS = "FINGERPRINT_BAD_PIXEL_BLOCK_MAX_EGIS";
        public static final String FINGERPRINT_BAD_PIXEL_BLOCK_MIN_EGIS = "FINGERPRINT_BAD_PIXEL_BLOCK_MIN_EGIS";
        public static final String FINGERPRINT_BAD_PIXEL_CONSECUTIVE_MAX_EGIS = "FINGERPRINT_BAD_PIXEL_CONSECUTIVE_MAX_EGIS";
        public static final String FINGERPRINT_BAD_PIXEL_CONSECUTIVE_MIN_EGIS = "FINGERPRINT_BAD_PIXEL_CONSECUTIVE_MIN_EGIS";
        public static final String FINGERPRINT_BAD_PIXEL_MAX = "FINGERPRINT_BAD_PIXEL_MAX";
        public static final String FINGERPRINT_BAD_PIXEL_MIN = "FINGERPRINT_BAD_PIXEL_MIN";
        public static final String FINGERPRINT_BAD_PIXEL_TOTAL_MAX_EGIS = "FINGERPRINT_BAD_PIXEL_TOTAL_MAX_EGIS";
        public static final String FINGERPRINT_BAD_PIXEL_TOTAL_MIN_EGIS = "FINGERPRINT_BAD_PIXEL_TOTAL_MIN_EGIS";
        public static final String FINGERPRINT_BAD_TOTAL_BAD_PIXEL_MAX = "FINGERPRINT_BAD_TOTAL_BAD_PIXEL_MAX";
        public static final String FINGERPRINT_BAD_TOTAL_BAD_PIXEL_MIN = "FINGERPRINT_BAD_TOTAL_BAD_PIXEL_MIN";
        public static final String FINGERPRINT_BLACK_DOT_MAX = "FINGERPRINT_BLACK_DOT_MAX";
        public static final String FINGERPRINT_BLACK_DOT_MIN = "FINGERPRINT_BLACK_DOT_MIN";
        public static final String FINGERPRINT_BLACK_TOTAL_BAD_PIXEL_MAX = "FINGERPRINT_BLACK_TOTAL_BAD_PIXEL_MAX";
        public static final String FINGERPRINT_BLACK_TOTAL_BAD_PIXEL_MIN = "FINGERPRINT_BLACK_TOTAL_BAD_PIXEL_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_DATA_DIFFERENCE_MAX = "FINGERPRINT_CAP_TOUCH_DATA_DIFFERENCE_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_DATA_DIFFERENCE_MIN = "FINGERPRINT_CAP_TOUCH_DATA_DIFFERENCE_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_LOCAL_MAX = "FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_LOCAL_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_LOCAL_MIN = "FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_LOCAL_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_TOTAL_MAX = "FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_TOTAL_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_TOTAL_MIN = "FINGERPRINT_CAP_TOUCH_DELTA_PIXEL_TOTAL_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_NOISE_MAX = "FINGERPRINT_CAP_TOUCH_NOISE_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_NOISE_MIN = "FINGERPRINT_CAP_TOUCH_NOISE_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_PERFORMANCE_MAX = "FINGERPRINT_CAP_TOUCH_PERFORMANCE_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_PERFORMANCE_MIN = "FINGERPRINT_CAP_TOUCH_PERFORMANCE_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_PIN_OPEN_SHORT_INT = "FINGERPRINT_CAP_TOUCH_PIN_OPEN_SHORT_INT";
        public static final String FINGERPRINT_CAP_TOUCH_PIN_OPEN_SHORT_RST = "FINGERPRINT_CAP_TOUCH_PIN_OPEN_SHORT_RST";
        public static final String FINGERPRINT_CAP_TOUCH_RUBBER_PLACEMENT_MAX = "FINGERPRINT_CAP_TOUCH_RUBBER_PLACEMENT_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_RUBBER_PLACEMENT_MIN = "FINGERPRINT_CAP_TOUCH_RUBBER_PLACEMENT_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_SATURATION_MAX_DOWN_MAX = "FINGERPRINT_CAP_TOUCH_SATURATION_MAX_DOWN_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_SATURATION_MAX_DOWN_MIN = "FINGERPRINT_CAP_TOUCH_SATURATION_MAX_DOWN_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_SATURATION_MAX_UP_MAX = "FINGERPRINT_CAP_TOUCH_SATURATION_MAX_UP_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_SATURATION_MAX_UP_MIN = "FINGERPRINT_CAP_TOUCH_SATURATION_MAX_UP_MIN";
        public static final String FINGERPRINT_CAP_TOUCH_SNR_MAX = "FINGERPRINT_CAP_TOUCH_SNR_MAX";
        public static final String FINGERPRINT_CAP_TOUCH_SNR_MIN = "FINGERPRINT_CAP_TOUCH_SNR_MIN";
        public static final String FINGERPRINT_CAP_UNTOUCH_BAD_PIXEL_STREAK_MAX = "FINGERPRINT_CAP_UNTOUCH_BAD_PIXEL_STREAK_MAX";
        public static final String FINGERPRINT_CAP_UNTOUCH_BAD_PIXEL_STREAK_MIN = "FINGERPRINT_CAP_UNTOUCH_BAD_PIXEL_STREAK_MIN";
        public static final String FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x1_FDT_ZONE_MAX = "FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x1_FDT_ZONE_MAX";
        public static final String FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x1_FDT_ZONE_MIN = "FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x1_FDT_ZONE_MIN";
        public static final String FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x7_FDT_ZONE_MAX = "FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x7_FDT_ZONE_MAX";
        public static final String FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x7_FDT_ZONE_MIN = "FINGERPRINT_CAP_UNTOUCH_FDT_PIXEL_OPEN_SHORT_8x7_FDT_ZONE_MIN";
        public static final String FINGERPRINT_CAP_UNTOUCH_OTP = "FINGERPRINT_CAP_UNTOUCH_OTP";
        public static final String FINGERPRINT_CAP_UNTOUCH_PIN_OPEN_SHORT_SPI = "FINGERPRINT_CAP_UNTOUCH_PIN_OPEN_SHORT_SPI";
        public static final String FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_LOCAL_MAX = "FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_LOCAL_MAX";
        public static final String FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_LOCAL_MIN = "FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_LOCAL_MIN";
        public static final String FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_TOTAL_MAX = "FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_TOTAL_MAX";
        public static final String FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_TOTAL_MIN = "FINGERPRINT_CAP_UNTOUCH_PIXEL_OPEN_SHORT_TOTAL_MIN";
        public static final String FINGERPRINT_CET_MAX_EGIS = "FINGERPRINT_CET_MAX_EGIS";
        public static final String FINGERPRINT_CET_MIN_EGIS = "FINGERPRINT_CET_MIN_EGIS";
        public static final String FINGERPRINT_CON_FLOORED_PIXEL_COL_MAX_NAMSAN = "FINGERPRINT_CON_FLOORED_PIXEL_COL_MAX_NAMSAN";
        public static final String FINGERPRINT_CON_FLOORED_PIXEL_COL_MIN_NAMSAN = "FINGERPRINT_CON_FLOORED_PIXEL_COL_MIN_NAMSAN";
        public static final String FINGERPRINT_CON_PEGGED_PIXEL_COL_MAX_NAMSAN = "FINGERPRINT_CON_PEGGED_PIXEL_COL_MAX_NAMSAN";
        public static final String FINGERPRINT_CON_PEGGED_PIXEL_COL_MIN_NAMSAN = "FINGERPRINT_CON_PEGGED_PIXEL_COL_MIN_NAMSAN";
        public static final String FINGERPRINT_DEFECT_PIXEL_MAX = "FINGERPRINT_DEFECT_PIXEL_MAX";
        public static final String FINGERPRINT_DEFECT_PIXEL_MIN = "FINGERPRINT_DEFECT_PIXEL_MIN";
        public static final String FINGERPRINT_DEFECT_TOTAL_BAD_PIXEL_MAX = "FINGERPRINT_DEFECT_TOTAL_BAD_PIXEL_MAX";
        public static final String FINGERPRINT_DEFECT_TOTAL_BAD_PIXEL_MIN = "FINGERPRINT_DEFECT_TOTAL_BAD_PIXEL_MIN";
        public static final String FINGERPRINT_DELTA_PIXEL_COL_MAX = "FINGERPRINT_DELTA_PIXEL_COL_MAX";
        public static final String FINGERPRINT_DELTA_PIXEL_COL_MIN = "FINGERPRINT_DELTA_PIXEL_COL_MIN";
        public static final String FINGERPRINT_DELTA_PIXEL_ROW_MAX = "FINGERPRINT_DELTA_PIXEL_ROW_MAX";
        public static final String FINGERPRINT_DELTA_PIXEL_ROW_MIN = "FINGERPRINT_DELTA_PIXEL_ROW_MIN";
        public static final String FINGERPRINT_DETECT_SCORE_MAX_NAMSAN = "FINGERPRINT_DETECT_SCORE_MAX_NAMSAN";
        public static final String FINGERPRINT_DETECT_SCORE_MIN_NAMSAN = "FINGERPRINT_DETECT_SCORE_MIN_NAMSAN";
        public static final String FINGERPRINT_FEA_X_MAX_EGIS = "FINGERPRINT_FEA_X_MAX_EGIS";
        public static final String FINGERPRINT_FEA_X_MIN_EGIS = "FINGERPRINT_FEA_X_MIN_EGIS";
        public static final String FINGERPRINT_FEA_Y_MAX_EGIS = "FINGERPRINT_FEA_Y_MAX_EGIS";
        public static final String FINGERPRINT_FEA_Y_MIN_EGIS = "FINGERPRINT_FEA_Y_MIN_EGIS";
        public static final String FINGERPRINT_FLASHCHECKSUM_MAX_NAMSAN = "FINGERPRINT_FLASHCHECKSUM_MAX_NAMSAN";
        public static final String FINGERPRINT_FLASHCHECKSUM_MIN_NAMSAN = "FINGERPRINT_FLASHCHECKSUM_MIN_NAMSAN";
        public static final String FINGERPRINT_FLOORED_PIXEL_COL_MAX = "FINGERPRINT_FLOORED_PIXEL_COL_MAX";
        public static final String FINGERPRINT_FLOORED_PIXEL_COL_MIN = "FINGERPRINT_FLOORED_PIXEL_COL_MIN";
        public static final String FINGERPRINT_FLOORED_PIXEL_MAX = "FINGERPRINT_FLOORED_PIXEL_MAX";
        public static final String FINGERPRINT_FLOORED_PIXEL_MIN = "FINGERPRINT_FLOORED_PIXEL_MIN";
        public static final String FINGERPRINT_FLOORED_PIXEL_ROW_MAX = "FINGERPRINT_FLOORED_PIXEL_ROW_MAX";
        public static final String FINGERPRINT_FLOORED_PIXEL_ROW_MAX_NAMSAN = "FINGERPRINT_FLOORED_PIXEL_ROW_MAX_NAMSAN";
        public static final String FINGERPRINT_FLOORED_PIXEL_ROW_MIN = "FINGERPRINT_FLOORED_PIXEL_ROW_MIN";
        public static final String FINGERPRINT_FLOORED_PIXEL_ROW_MIN_NAMSAN = "FINGERPRINT_FLOORED_PIXEL_ROW_MIN_NAMSAN";
        public static final String FINGERPRINT_IMAGE_DEVIATION_MAX = "FINGERPRINT_IMAGE_DEVIATION_MAX";
        public static final String FINGERPRINT_IMAGE_DEVIATION_MIN = "FINGERPRINT_IMAGE_DEVIATION_MIN";
        public static final String FINGERPRINT_MAXIMUM_DELTA_MAX = "FINGERPRINT_MAXIMUM_DELTA_MAX";
        public static final String FINGERPRINT_MAXIMUM_DELTA_MIN = "FINGERPRINT_MAXIMUM_DELTA_MIN";
        public static final String FINGERPRINT_MAX_NEG_DELTA_MAX = "FINGERPRINT_MAX_NEG_DELTA_MAX";
        public static final String FINGERPRINT_MAX_NEG_DELTA_MIN = "FINGERPRINT_MAX_NEG_DELTA_MIN";
        public static final String FINGERPRINT_MAX_POS_DELTA_MAX = "FINGERPRINT_MAX_POS_DELTA_MAX";
        public static final String FINGERPRINT_MAX_POS_DELTA_MIN = "FINGERPRINT_MAX_POS_DELTA_MIN";
        public static final String FINGERPRINT_MFG_CAL_OVERALL_MAX_NAMSAN = "FINGERPRINT_MFG_CAL_OVERALL_MAX_NAMSAN";
        public static final String FINGERPRINT_MFG_CAL_OVERALL_MIN_NAMSAN = "FINGERPRINT_MFG_CAL_OVERALL_MIN_NAMSAN";
        public static final String FINGERPRINT_MFG_CAL_ZONE_MAX_NAMSAN = "FINGERPRINT_MFG_CAL_ZONE_MAX_NAMSAN";
        public static final String FINGERPRINT_MFG_CAL_ZONE_MIN_NAMSAN = "FINGERPRINT_MFG_CAL_ZONE_MIN_NAMSAN";
        public static final String FINGERPRINT_MINIMUM_DELTA_MAX = "FINGERPRINT_MINIMUM_DELTA_MAX";
        public static final String FINGERPRINT_MINIMUM_DELTA_MIN = "FINGERPRINT_MINIMUM_DELTA_MIN";
        public static final String FINGERPRINT_M_FACTOR_MAX_EGIS = "FINGERPRINT_M_FACTOR_MAX_EGIS";
        public static final String FINGERPRINT_M_FACTOR_MIN_EGIS = "FINGERPRINT_M_FACTOR_MIN_EGIS";
        public static final String FINGERPRINT_NOISE_MAX_EGIS = "FINGERPRINT_NOISE_MAX_EGIS";
        public static final String FINGERPRINT_NOISE_MAX_NAMSAN = "FINGERPRINT_NOISE_MAX_NAMSAN";
        public static final String FINGERPRINT_NOISE_MIN_EGIS = "FINGERPRINT_NOISE_MIN_EGIS";
        public static final String FINGERPRINT_NOISE_MIN_NAMSAN = "FINGERPRINT_NOISE_MIN_NAMSAN";
        public static final String FINGERPRINT_NOISE_SPEC_MAX = "FINGERPRINT_NOISE_SPEC_MAX";
        public static final String FINGERPRINT_NOISE_SPEC_MIN = "FINGERPRINT_NOISE_SPEC_MIN";
        public static final String FINGERPRINT_NOISE_ZONE_MAX_NAMSAN = "FINGERPRINT_NOISE_ZONE_MAX_NAMSAN";
        public static final String FINGERPRINT_NOISE_ZONE_MIN_NAMSAN = "FINGERPRINT_NOISE_ZONE_MIN_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_RX_IMAGE_MAX_NAMSAN = "FINGERPRINT_OPEN_SHORT_RX_IMAGE_MAX_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_RX_IMAGE_MIN_NAMSAN = "FINGERPRINT_OPEN_SHORT_RX_IMAGE_MIN_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_RX_RANGE_MAX_NAMSAN = "FINGERPRINT_OPEN_SHORT_RX_RANGE_MAX_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_RX_RANGE_MIN_NAMSAN = "FINGERPRINT_OPEN_SHORT_RX_RANGE_MIN_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_RX_SHORT_MAX_NAMSAN = "FINGERPRINT_OPEN_SHORT_RX_SHORT_MAX_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_RX_SHORT_MIN_NAMSAN = "FINGERPRINT_OPEN_SHORT_RX_SHORT_MIN_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_TX_SHORT_MAX_NAMSAN = "FINGERPRINT_OPEN_SHORT_TX_SHORT_MAX_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_TX_SHORT_MIN_NAMSAN = "FINGERPRINT_OPEN_SHORT_TX_SHORT_MIN_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_TX_SUM_MAX_NAMSAN = "FINGERPRINT_OPEN_SHORT_TX_SUM_MAX_NAMSAN";
        public static final String FINGERPRINT_OPEN_SHORT_TX_SUM_MIN_NAMSAN = "FINGERPRINT_OPEN_SHORT_TX_SUM_MIN_NAMSAN";
        public static final String FINGERPRINT_PEGGED_PIXEL_COL_MAX = "FINGERPRINT_PEGGED_PIXEL_COL_MAX";
        public static final String FINGERPRINT_PEGGED_PIXEL_COL_MIN = "FINGERPRINT_PEGGED_PIXEL_COL_MIN";
        public static final String FINGERPRINT_PEGGED_PIXEL_MAX = "FINGERPRINT_PEGGED_PIXEL_MAX";
        public static final String FINGERPRINT_PEGGED_PIXEL_MIN = "FINGERPRINT_PEGGED_PIXEL_MIN";
        public static final String FINGERPRINT_PEGGED_PIXEL_ROW_MAX = "FINGERPRINT_PEGGED_PIXEL_ROW_MAX";
        public static final String FINGERPRINT_PEGGED_PIXEL_ROW_MAX_NAMSAN = "FINGERPRINT_PEGGED_PIXEL_ROW_MAX_NAMSAN";
        public static final String FINGERPRINT_PEGGED_PIXEL_ROW_MIN = "FINGERPRINT_PEGGED_PIXEL_ROW_MIN";
        public static final String FINGERPRINT_PEGGED_PIXEL_ROW_MIN_NAMSAN = "FINGERPRINT_PEGGED_PIXEL_ROW_MIN_NAMSAN";
        public static final String FINGERPRINT_PLACEMENT_TEST_MAX = "FINGERPRINT_PLACEMENT_TEST_MAX";
        public static final String FINGERPRINT_PLACEMENT_TEST_MIN = "FINGERPRINT_PLACEMENT_TEST_MIN";
        public static final String FINGERPRINT_PRESENT_MAX_NAMSAN = "FINGERPRINT_PRESENT_MAX_NAMSAN";
        public static final String FINGERPRINT_PRESENT_MIN_NAMSAN = "FINGERPRINT_PRESENT_MIN_NAMSAN";
        public static final String FINGERPRINT_RATIO_ZONE_MAX_NAMSAN = "FINGERPRINT_RATIO_ZONE_MAX_NAMSAN";
        public static final String FINGERPRINT_RATIO_ZONE_MIN_NAMSAN = "FINGERPRINT_RATIO_ZONE_MIN_NAMSAN";
        public static final String FINGERPRINT_RX_DELTA_MAX_NAMSAN = "FINGERPRINT_RX_DELTA_MAX_NAMSAN";
        public static final String FINGERPRINT_RX_DELTA_MIN_NAMSAN = "FINGERPRINT_RX_DELTA_MIN_NAMSAN";
        public static final String FINGERPRINT_RX_MAX_PIXEL_FAIL_MAX_NAMSAN = "FINGERPRINT_RX_MAX_PIXEL_FAIL_MAX_NAMSAN";
        public static final String FINGERPRINT_RX_MAX_PIXEL_FAIL_MIN_NAMSAN = "FINGERPRINT_RX_MAX_PIXEL_FAIL_MIN_NAMSAN";
        public static final String FINGERPRINT_SIGNAL_MAX_EGIS = "FINGERPRINT_SIGNAL_MAX_EGIS";
        public static final String FINGERPRINT_SIGNAL_MAX_NAMSAN = "FINGERPRINT_SIGNAL_MAX_NAMSAN";
        public static final String FINGERPRINT_SIGNAL_MIN_EGIS = "FINGERPRINT_SIGNAL_MIN_EGIS";
        public static final String FINGERPRINT_SIGNAL_MIN_NAMSAN = "FINGERPRINT_SIGNAL_MIN_NAMSAN";
        public static final String FINGERPRINT_SIGNAL_SPEC_MAX = "FINGERPRINT_SIGNAL_SPEC_MAX";
        public static final String FINGERPRINT_SIGNAL_SPEC_MIN = "FINGERPRINT_SIGNAL_SPEC_MIN";
        public static final String FINGERPRINT_SIGNAL_ZONE_MAX_NAMSAN = "FINGERPRINT_SIGNAL_ZONE_MAX_NAMSAN";
        public static final String FINGERPRINT_SIGNAL_ZONE_MIN_NAMSAN = "FINGERPRINT_SIGNAL_ZONE_MIN_NAMSAN";
        public static final String FINGERPRINT_SNR_MAX_EGIS = "FINGERPRINT_SNR_MAX_EGIS";
        public static final String FINGERPRINT_SNR_MAX_NAMSAN = "FINGERPRINT_SNR_MAX_NAMSAN";
        public static final String FINGERPRINT_SNR_MIN_EGIS = "FINGERPRINT_SNR_MIN_EGIS";
        public static final String FINGERPRINT_SNR_MIN_NAMSAN = "FINGERPRINT_SNR_MIN_NAMSAN";
        public static final String FINGERPRINT_SNR_SPEC_MAX = "FINGERPRINT_SNR_SPEC_MAX";
        public static final String FINGERPRINT_SNR_SPEC_MIN = "FINGERPRINT_SNR_SPEC_MIN";
        public static final String FINGERPRINT_SNR_ZONE_MAX_NAMSAN = "FINGERPRINT_SNR_ZONE_MAX_NAMSAN";
        public static final String FINGERPRINT_SNR_ZONE_MIN_NAMSAN = "FINGERPRINT_SNR_ZONE_MIN_NAMSAN";
        public static final String FINGERPRINT_STANDARD_DEVIATION_MAX = "FINGERPRINT_STANDARD_DEVIATION_MAX";
        public static final String FINGERPRINT_STANDARD_DEVIATION_MIN = "FINGERPRINT_STANDARD_DEVIATION_MIN";
        public static final String FINGERPRINT_TOTAL_2D_DELTA_FAIL_MAX = "FINGERPRINT_TOTAL_2D_DELTA_FAIL_MAX";
        public static final String FINGERPRINT_TOTAL_2D_DELTA_FAIL_MIN = "FINGERPRINT_TOTAL_2D_DELTA_FAIL_MIN";
        public static final String FINGERPRINT_TOTAL_BAD_PIXCEL_COUNT_MAX_NAMSAN = "FINGERPRINT_TOTAL_BAD_PIXCEL_COUNT_MAX_NAMSAN";
        public static final String FINGERPRINT_TOTAL_BAD_PIXCEL_COUNT_MIN_NAMSAN = "FINGERPRINT_TOTAL_BAD_PIXCEL_COUNT_MIN_NAMSAN";
        public static final String FINGERPRINT_TOTAL_BAD_PIXEL_MAX_NAMSAN = "FINGERPRINT_TOTAL_BAD_PIXEL_MAX_NAMSAN";
        public static final String FINGERPRINT_TOTAL_BAD_PIXEL_MIN_NAMSAN = "FINGERPRINT_TOTAL_BAD_PIXEL_MIN_NAMSAN";
        public static final String FINGERPRINT_TX_MAX_PIXEL_FAIL_MAX_NAMSAN = "FINGERPRINT_TX_MAX_PIXEL_FAIL_MAX_NAMSAN";
        public static final String FINGERPRINT_TX_MAX_PIXEL_FAIL_MIN_NAMSAN = "FINGERPRINT_TX_MAX_PIXEL_FAIL_MIN_NAMSAN";
        public static final String FINGERPRINT_VARIANCE_MAX = "FINGERPRINT_VARIANCE_MAX";
        public static final String FINGERPRINT_VARIANCE_MAX_DC = "FINGERPRINT_VARIANCE_MAX_DC";
        public static final String FINGERPRINT_VARIANCE_MIN = "FINGERPRINT_VARIANCE_MIN";
        public static final String FINGERPRINT_VARIANCE_MIN_DC = "FINGERPRINT_VARIANCE_MIN_DC";
        public static final String FINGERPRINT_WHITE_DOT_MAX = "FINGERPRINT_WHITE_DOT_MAX";
        public static final String FINGERPRINT_WHITE_DOT_MIN = "FINGERPRINT_WHITE_DOT_MIN";
        public static final String FINGERPRINT_WHITE_TOTAL_BAD_PIXEL_MAX = "FINGERPRINT_WHITE_TOTAL_BAD_PIXEL_MAX";
        public static final String FINGERPRINT_WHITE_TOTAL_BAD_PIXEL_MIN = "FINGERPRINT_WHITE_TOTAL_BAD_PIXEL_MIN";
        public static final String FORCE_TOUCH_CAL_SPEC_MAX = "FORCE_TOUCH_CAL_SPEC_MAX";
        public static final String FORCE_TOUCH_CAL_SPEC_MIN = "FORCE_TOUCH_CAL_SPEC_MIN";
        public static final String FORCE_TOUCH_IX_MAX_STM = "FORCE_TOUCH_IX_MAX_STM";
        public static final String FORCE_TOUCH_IX_MIN_STM = "FORCE_TOUCH_IX_MIN_STM";
        public static final String FORCE_TOUCH_JITTER_MAX = "FORCE_TOUCH_JITTER_MAX";
        public static final String FORCE_TOUCH_LOWER_WEIGHT = "FORCE_TOUCH_LOWER_WEIGHT";
        public static final String FORCE_TOUCH_OFFSET_MAX_SEC = "FORCE_TOUCH_OFFSET_MAX_SEC";
        public static final String FORCE_TOUCH_OFFSET_MIN_SEC = "FORCE_TOUCH_OFFSET_MIN_SEC";
        public static final String FORCE_TOUCH_PRESS_SPEC_MAX = "FORCE_TOUCH_PRESS_SPEC_MAX";
        public static final String FORCE_TOUCH_PRESS_SPEC_MIN = "FORCE_TOUCH_PRESS_SPEC_MIN";
        public static final String FORCE_TOUCH_PRE_DELTA_MAX_SEC = "FORCE_TOUCH_PRE_DELTA_MAX_SEC";
        public static final String FORCE_TOUCH_PRE_DELTA_MIN_SEC = "FORCE_TOUCH_PRE_DELTA_MIN_SEC";
        public static final String FORCE_TOUCH_RAW_CAP_MAX_STM = "FORCE_TOUCH_RAW_CAP_MAX_STM";
        public static final String FORCE_TOUCH_RAW_CAP_MIN_STM = "FORCE_TOUCH_RAW_CAP_MIN_STM";
        public static final String FORCE_TOUCH_REF_VALUE_FOR_WEIGHT = "FORCE_TOUCH_REF_VALUE_FOR_WEIGHT";
        public static final String FORCE_TOUCH_REF_VALUE_FOR_WEIGHT_SECOND = "FORCE_TOUCH_REF_VALUE_FOR_WEIGHT_SECOND";
        public static final String FORCE_TOUCH_UPPER_WEIGHT = "FORCE_TOUCH_UPPER_WEIGHT";
        public static final String GEOMAGNETIC_POWERNOISE_MAX = "GEOMAGNETIC_POWERNOISE_MAX";
        public static final String GEOMAGNETIC_SPEC_ADC = "GEOMAGNETIC_SPEC_ADC";
        public static final String GESTURESENSOR_IR_CURRENT = "GESTURESENSOR_IR_CURRENT";
        public static final String GESTURESENSOR_PEEK_TO_PEEK_SPEC_MAX = "GESTURESENSOR_PEEK_TO_PEEK_SPEC_MAX";
        public static final String GESTURESENSOR_PEEK_TO_PEEK_SPEC_MIN = "GESTURESENSOR_PEEK_TO_PEEK_SPEC_MIN";
        public static final String GESTURE_CROSSTALK_RAW = "GESTURE_CROSSTALK_RAW";
        public static final String GRIP_MARGIN_SPEC_FOR_CAL_CHECK = "GRIP_MARGIN_SPEC_FOR_CAL_CHECK";
        public static final String GRIP_PROX_DIFF_INDEX = "GRIP_PROX_DIFF_INDEX";
        public static final String GYROSCOPE_HW_SELFTEST = "GYROSCOPE_HW_SELFTEST";
        public static final String GYROSCOPE_SELFTEST_INVENSENSE_MAX = "GYROSCOPE_SELFTEST_INVENSENSE_MAX";
        public static final String GYROSCOPE_SELFTEST_INVENSENSE_MIN = "GYROSCOPE_SELFTEST_INVENSENSE_MIN";
        public static final String GYROSCOPE_SELFTEST_MAX = "GYROSCOPE_SELFTEST_MAX";
        public static final String GYROSCOPE_SELFTEST_MAXIM_MAX = "GYROSCOPE_SELFTEST_BOSCH_MAX";
        public static final String GYROSCOPE_SELFTEST_MAXIM_MIN = "GYROSCOPE_SELFTEST_BOSCH_MIN";
        public static final String GYROSCOPE_SELFTEST_MIN = "GYROSCOPE_SELFTEST_MIN";
        public static final String GYROSCOPE_SELFTEST_STM_MAX = "GYROSCOPE_SELFTEST_STM_MAX";
        public static final String GYROSCOPE_SELFTEST_STM_MIN = "GYROSCOPE_SELFTEST_STM_MIN";
        public static final String HRM_86900A_FREQUENCY_DC_IR = "HRM_86900A_FREQUENCY_DC_IR";
        public static final String HRM_86900A_FREQUENCY_DC_RED = "HRM_86900A_FREQUENCY_DC_RED";
        public static final String HRM_86900A_PEAK_TO_PEAK_DC_IR = "HRM_86900A_PEAK_TO_PEAK_DC_IR";
        public static final String HRM_86900A_PEAK_TO_PEAK_PEAK_IR = "HRM_86900A_PEAK_TO_PEAK_PEAK_IR";
        public static final String HRM_86900A_PEAK_TO_PEAK_PEAK_RED = "HRM_86900A_PEAK_TO_PEAK_PEAK_RED";
        public static final String HRM_BLUE_HIGH_DC = "HRM_BLUE_HIGH_DC";
        public static final String HRM_BLUE_LOW_DC = "HRM_BLUE_LOW_DC";
        public static final String HRM_BLUE_MEDIUM_DC = "HRM_BLUE_MEDIUM_DC";
        public static final String HRM_BLUE_NOISE = "HRM_BLUE_NOISE";
        public static final String HRM_BLUE_XTC_DC = "HRM_BLUE_XTC_DC";
        public static final String HRM_CLOUD_UV_RATIO = "HRM_CLOUD_UV_RATIO";
        public static final String HRM_FREQUENCY = "HRM_FREQUENCY";
        public static final String HRM_FREQUENCY_DC_IR = "HRM_FREQUENCY_DC_IR";
        public static final String HRM_FREQUENCY_DC_RED = "HRM_FREQUENCY_DC_RED";
        public static final String HRM_FREQUENCY_NOISE_IR = "HRM_FREQUENCY_NOISE_IR";
        public static final String HRM_FREQUENCY_NOISE_RED = "HRM_FREQUENCY_NOISE_RED";
        public static final String HRM_FREQUENCY_SAMPLE_NO = "HRM_FREQUENCY_SAMPLE_NO";
        public static final String HRM_GREEN_HIGH_DC = "HRM_GREEN_HIGH_DC";
        public static final String HRM_GREEN_LOW_DC = "HRM_GREEN_LOW_DC";
        public static final String HRM_GREEN_MEDIUM_DC = "HRM_GREEN_MEDIUM_DC";
        public static final String HRM_GREEN_NOISE = "HRM_GREEN_NOISE";
        public static final String HRM_GREEN_XTC_DC = "HRM_GREEN_XTC_DC";
        public static final String HRM_HR_SPO2 = "HRM_HR_SPO2";
        public static final String HRM_IR_AC = "HRM_IR_AC";
        public static final String HRM_IR_AC_DC_RATIO = "HRM_IR_AC_DC_RATIO";
        public static final String HRM_IR_FREQUENCY = "HRM_IR_FREQUENCY";
        public static final String HRM_IR_HIGH_DC = "HRM_IR_HIGH_DC";
        public static final String HRM_IR_HI_DC = "HRM_IR_HI_DC";
        public static final String HRM_IR_LOW_DC = "HRM_IR_LOW_DC";
        public static final String HRM_IR_MEDIUM_DC = "HRM_IR_MEDIUM_DC";
        public static final String HRM_IR_MEDIUM_SQUARED_NOISE = "HRM_IR_MEDIUM_SQUARED_NOISE";
        public static final String HRM_IR_NOISE = "HRM_IR_NOISE";
        public static final String HRM_IR_RED_R_RATIO = "HRM_IR_RED_R_RATIO";
        public static final String HRM_IR_XTC_DC = "HRM_IR_XTC_DC";
        public static final String HRM_PEAK_TO_PEAK_DC_IR = "HRM_PEAK_TO_PEAK_DC_IR";
        public static final String HRM_PEAK_TO_PEAK_DC_RED = "HRM_PEAK_TO_PEAK_DC_RED";
        public static final String HRM_PEAK_TO_PEAK_PEAK_IR = "HRM_PEAK_TO_PEAK_PEAK_IR";
        public static final String HRM_PEAK_TO_PEAK_PEAK_RED = "HRM_PEAK_TO_PEAK_PEAK_RED";
        public static final String HRM_PEAK_TO_PEAK_RATIO_IR = "HRM_PEAK_TO_PEAK_RATIO_IR";
        public static final String HRM_PEAK_TO_PEAK_RATIO_RED = "HRM_PEAK_TO_PEAK_RATIO_RED";
        public static final String HRM_RED_AC = "HRM_RED_AC";
        public static final String HRM_RED_AC_DC_RATIO = "HRM_RED_AC_DC_RATIO";
        public static final String HRM_RED_HIGH_DC = "HRM_RED_HIGH_DC";
        public static final String HRM_RED_HI_DC = "HRM_RED_HI_DC";
        public static final String HRM_RED_LOW_DC = "HRM_RED_LOW_DC";
        public static final String HRM_RED_LOW_NOISE = "HRM_RED_LOW_NOISE";
        public static final String HRM_RED_MEDIUM_DC = "HRM_RED_MEDIUM_DC";
        public static final String HRM_RED_MEDIUM_SQUARED_NOISE = "HRM_RED_MEDIUM_SQUARED_NOISE";
        public static final String HRM_RED_NOISE = "HRM_RED_NOISE";
        public static final String HRM_RED_XTC_DC = "HRM_RED_XTC_DC";
        public static final String HRM_SYSTEM_NOISE = "HRM_SYSTEM_NOISE";
        public static final String HRM_WINDOW_XTALK_IR_ADC_MAX = "HRM_WINDOW_XTALK_IR_ADC_MAX";
        public static final String HRM_WINDOW_XTALK_IR_ADC_MIN = "HRM_WINDOW_XTALK_IR_ADC_MIN";
        public static final String IRIS_LED_TEST_ADC_THRESHOLD = "IRIS_LED_TEST_ADC_THRESHOLD";
        public static final String IRIS_LED_TEST_SPEC_MAX = "IRIS_LED_TEST_SPEC_MAX";
        public static final String IRIS_LED_TEST_SPEC_MIN = "IRIS_LED_TEST_SPEC_MIN";
        public static final String IRIS_PROX_ADC_SPEC_MAX = "IRIS_PROX_ADC_SPEC_MAX";
        public static final String IRIS_PROX_ADC_SPEC_MIN = "IRIS_PROX_ADC_SPEC_MIN";
        public static final String IRIS_PROX_OFFSET_SPEC_MAX = "IRIS_PROX_OFFSET_SPEC_MAX";
        public static final String IRIS_PROX_OFFSET_SPEC_MIN = "IRIS_PROX_OFFSET_SPEC_MIN";
        public static final String LED_POWER_MAX = "LED_POWER_MAX";
        public static final String LED_POWER_MIN = "LED_POWER_MIN";
        public static final String LIGHT_SENSOR_DOWN_LEVEL_1 = "LIGHT_SENSOR_DOWN_LEVEL_1";
        public static final String LIGHT_SENSOR_DOWN_LEVEL_2 = "LIGHT_SENSOR_DOWN_LEVEL_2";
        public static final String LIGHT_SENSOR_DOWN_LEVEL_3 = "LIGHT_SENSOR_DOWN_LEVEL_3";
        public static final String LIGHT_SENSOR_DOWN_LEVEL_4 = "LIGHT_SENSOR_DOWN_LEVEL_4";
        public static final String LIGHT_SENSOR_DOWN_LEVEL_5 = "LIGHT_SENSOR_DOWN_LEVEL_5";
        public static final String LIGHT_SENSOR_DOWN_LEVEL_6 = "LIGHT_SENSOR_DOWN_LEVEL_6";
        public static final String LIGHT_SENSOR_DOWN_LEVEL_7 = "LIGHT_SENSOR_DOWN_LEVEL_7";
        public static final String LIGHT_SENSOR_UP_LEVEL_1 = "LIGHT_SENSOR_UP_LEVEL_1";
        public static final String LIGHT_SENSOR_UP_LEVEL_2 = "LIGHT_SENSOR_UP_LEVEL_2";
        public static final String LIGHT_SENSOR_UP_LEVEL_3 = "LIGHT_SENSOR_UP_LEVEL_3";
        public static final String LIGHT_SENSOR_UP_LEVEL_4 = "LIGHT_SENSOR_UP_LEVEL_4";
        public static final String LIGHT_SENSOR_UP_LEVEL_5 = "LIGHT_SENSOR_UP_LEVEL_5";
        public static final String LIGHT_SENSOR_UP_LEVEL_6 = "LIGHT_SENSOR_UP_LEVEL_6";
        public static final String LIGHT_SENSOR_UP_LEVEL_7 = "LIGHT_SENSOR_UP_LEVEL_7";
        public static final String LOAD_CHARGE_SPEC_MAX = "LOAD_CHARGE_SPEC_MAX";
        public static final String LOAD_CHARGE_SPEC_MIN = "LOAD_CHARGE_SPEC_MIN";
        public static final String LOOPBACK_VOLUME = "LOOPBACK_VOLUME";
        public static final String MAGNETIC_SENSOR_SELFTEST_X_MAX = "MAGNETIC_SENSOR_SELFTEST_X_MAX";
        public static final String MAGNETIC_SENSOR_SELFTEST_X_MIN = "MAGNETIC_SENSOR_SELFTEST_X_MIN";
        public static final String MIC_DIFF_TYPE_VER = "MIC_DIFF_TYPE_VER";
        public static final String MOOD_LED_RGB_COLOR_1 = "MOOD_LED_RGB_COLOR_1";
        public static final String MOOD_LED_RGB_COLOR_2 = "MOOD_LED_RGB_COLOR_2";
        public static final String MOOD_LED_RGB_COLOR_3 = "MOOD_LED_RGB_COLOR_3";
        public static final String MOOD_LED_UX5_COLOR_1 = "MOOD_LED_UX5_COLOR_1";
        public static final String MOOD_LED_UX5_COLOR_2 = "MOOD_LED_UX5_COLOR_2";
        public static final String MOOD_LED_UX5_COLOR_3 = "MOOD_LED_UX5_COLOR_3";
        public static final String MOOD_LED_UX5_COLOR_4 = "MOOD_LED_UX5_COLOR_4";
        public static final String MOOD_LED_UX5_COLOR_5 = "MOOD_LED_UX5_COLOR_5";
        public static final String NORMAL_CHARGE_CURRRENT_SPEC_MAX = "NORMAL_CHARGE_CURRRENT_SPEC_MAX";
        public static final String NORMAL_CHARGE_CURRRENT_SPEC_MIN = "NORMAL_CHARGE_CURRRENT_SPEC_MIN";
        public static final String NORMAL_CHARGE_VOLTAGE = "NORMAL_CHARGE_VOLTAGE";
        public static final String NO_RGBSENSOR_SUPPORT_REV = "NO_RGBSENSOR_SUPPORT_REV";
        public static final String OIS_INITIAL_SPEC_MAX = "OIS_INITIAL_SPEC_MAX";
        public static final String OIS_INITIAL_SPEC_MIN = "OIS_INITIAL_SPEC_MIN";
        public static final String OIS_SELFTEST_SPEC_THRESHOLD = "OIS_SELFTEST_SPEC_THRESHOLD";
        public static final String PD_CHARGE_SPEC_MAX = "PD_CHARGE_SPEC_MAX";
        public static final String PD_CHARGE_SPEC_MIN = "PD_CHARGE_SPEC_MIN";
        public static final String PD_CURRRENT_SPEC_MAX = "PD_CURRRENT_SPEC_MAX";
        public static final String PD_CURRRENT_SPEC_MIN = "PD_CURRRENT_SPEC_MIN";
        public static final String PD_VOLTAGE = "PD_VOLTAGE";
        public static final String PD_VOLTAGE_IN_USBTYPEC = "PD_VOLTAGE_IN_USBTYPEC";
        public static final String POGO_PIN2_TEST_SPEC_MAX = "POGO_PIN2_TEST_SPEC_MAX";
        public static final String POGO_PIN2_TEST_SPEC_MIN = "POGO_PIN2_TEST_SPEC_MIN";
        public static final String POGO_PIN_TEST_SPEC_MAX = "POGO_PIN_TEST_SPEC_MAX";
        public static final String POGO_PIN_TEST_SPEC_MIN = "POGO_PIN_TEST_SPEC_MIN";
        public static final String PROXIMITY_HOVER_SELFTEST_SPEC_X_MAX = "PROXIMITY_HOVER_SELFTEST_SPEC_X_MAX";
        public static final String PROXIMITY_HOVER_SELFTEST_SPEC_X_MIN = "PROXIMITY_HOVER_SELFTEST_SPEC_X_MIN";
        public static final String PROXIMITY_HOVER_SELFTEST_SPEC_Y_MAX = "PROXIMITY_HOVER_SELFTEST_SPEC_Y_MAX";
        public static final String PROXIMITY_HOVER_SELFTEST_SPEC_Y_MIN = "PROXIMITY_HOVER_SELFTEST_SPEC_Y_MIN";
        public static final String QUICK_CHARGE_3_CURRRENT_SPEC_MAX = "QUICK_CHARGE_3_CURRRENT_SPEC_MAX";
        public static final String QUICK_CHARGE_3_CURRRENT_SPEC_MIN = "QUICK_CHARGE_3_CURRRENT_SPEC_MIN";
        public static final String QUICK_CHARGE_3_VOLTAGE_SPEC_MAX = "QUICK_CHARGE_3_VOLTAGE_SPEC_MAX";
        public static final String QUICK_CHARGE_3_VOLTAGE_SPEC_MIN = "QUICK_CHARGE_3_VOLTAGE_SPEC_MIN";
        public static final String QUICK_CHARGE_NORMAL_CURRRENT_SPEC_MAX = "QUICK_CHARGE_NORMAL_CURRRENT_SPEC_MAX";
        public static final String QUICK_CHARGE_NORMAL_CURRRENT_SPEC_MIN = "QUICK_CHARGE_NORMAL_CURRRENT_SPEC_MIN";
        public static final String QUICK_CHARGE_NORMAL_VOLTAGE_SPEC_MAX = "QUICK_CHARGE_NORMAL_VOLTAGE_SPEC_MAX";
        public static final String QUICK_CHARGE_NORMAL_VOLTAGE_SPEC_MIN = "QUICK_CHARGE_NORMAL_VOLTAGE_SPEC_MIN";
        public static final String RANGE_LIGHT_BRIGHT = "RANGE_LIGHT_BRIGHT";
        public static final String RGBSENSOR_SUPPORT_WHITE = "RGBSENSOR_SUPPORT_WHITE";
        public static final String SBU_SPEC_MAX = "SBU_SPEC_MAX";
        public static final String SBU_SPEC_MIN = "SBU_SPEC_MIN";
        public static final String SEMI_LOOPBACK_SPEC_MAX = "SEMI_LOOPBACK_SPEC_MAX";
        public static final String SEMI_LOOPBACK_SPEC_MIN = "SEMI_LOOPBACK_SPEC_MIN";
        public static final String SEMI_TOUCH_KEY_SPEC_MAX = "SEMI_TOUCH_KEY_SPEC_MAX";
        public static final String SEMI_TOUCH_KEY_SPEC_MIN = "SEMI_TOUCH_KEY_SPEC_MIN";
        public static final String SENSING_OPEN_CHECK_SPEC = "SENSING_OPEN_CHECK_SPEC";
        public static final String SIMPLE_TEST_G_MAX = "SIMPLE_TEST_G_MAX";
        public static final String SIMPLE_TEST_G_MIN = "SIMPLE_TEST_G_MIN";
        public static final String SPEN_BLE_BATT0_SPEC_MAX = "SPEN_BLE_BATT0_SPEC_MAX";
        public static final String SPEN_BLE_BATT0_SPEC_MIN = "SPEN_BLE_BATT0_SPEC_MIN";
        public static final String SPEN_BLE_BATT12_SPEC_MAX = "SPEN_BLE_BATT12_SPEC_MAX";
        public static final String SPEN_BLE_BATT12_SPEC_MIN = "SPEN_BLE_BATT12_SPEC_MIN";
        public static final String SPEN_BLE_BATT1_SPEC_MAX = "SPEN_BLE_BATT1_SPEC_MAX";
        public static final String SPEN_BLE_BATT1_SPEC_MIN = "SPEN_BLE_BATT1_SPEC_MIN";
        public static final String SPEN_BLE_BATT23_SPEC_MAX = "SPEN_BLE_BATT23_SPEC_MAX";
        public static final String SPEN_BLE_BATT23_SPEC_MIN = "SPEN_BLE_BATT23_SPEC_MIN";
        public static final String SPEN_BLE_BATT43_SPEC_MAX = "SPEN_BLE_BATT43_SPEC_MAX";
        public static final String SPEN_BLE_BATT43_SPEC_MIN = "SPEN_BLE_BATT43_SPEC_MIN";
        public static final String SPEN_BLE_RSSI_SPEC_MAX = "SPEN_BLE_RSSI_SPEC_MAX";
        public static final String SPEN_BLE_RSSI_SPEC_MIN = "SPEN_BLE_RSSI_SPEC_MIN";
        public static final String SPEN_BLE_TEST_MINRSSI = "SPEN_BLE_TEST_MINRSSI";
        public static final String SPEN_BLE_TEST_MINRSSI_FINAL = "SPEN_BLE_TEST_MINRSSI_FINAL";
        public static final String SPEN_BLE_TEST_SCANDELAY = "SPEN_BLE_TEST_SCANDELAY";
        public static final String SPEN_DISTANCE_ABS_SPEC = "SPEN_DISTANCE_ABS_SPEC";
        public static final String SPEN_NODE_COUNT_HEIGHT = "SPEN_NODE_COUNT_HEIGHT";
        public static final String SPEN_NODE_COUNT_WIDTH = "SPEN_NODE_COUNT_WIDTH";
        public static final String SPEN_UNNECESSARY_NODE_COUNT = "SPEN_UNNECESSARY_NODE_COUNT";
        public static final String SPK_CAL_CIRRUS_REPT_LEFT_SPEC = "SPK_CAL_CIRRUS_REPT_LEFT_SPEC";
        public static final String SPK_CAL_CIRRUS_REPT_RIGHT_SPEC = "SPK_CAL_CIRRUS_REPT_RIGHT_SPEC";
        public static final String SPK_CAL_CIRRUS_REPT_SPEC = "SPK_CAL_CIRRUS_REPT_SPEC";
        public static final String SPK_CAL_CIRRUS_TEMPERATURE_SPEC = "SPK_CAL_CIRRUS_TEMPERATURE_SPEC";
        public static final String SPK_CAL_MAXIM_IMPEDANCE_SPEC = "SPK_CAL_MAXIM_IMPEDANCE_SPEC";
        public static final String SPK_CAL_MAXIM_TEMPERATURE_SPEC = "SPK_CAL_MAXIM_TEMPERATURE_SPEC";
        public static final String SPK_CAL_NXP_IMPEDANCE_SPEC = "SPK_CAL_NXP_IMPEDANCE_SPEC";
        public static final String SPK_CAL_NXP_TEMPERATURE_SPEC = "SPK_CAL_NXP_TEMPERATURE_SPEC";
        public static final String SPK_CAL_RICHTEK_IMPEDANCE_SPEC = "SPK_CAL_RICHTEK_IMPEDANCE_SPEC";
        public static final String SPK_CAL_RICHTEK_TEMPERATURE_SPEC = "SPK_CAL_RICHTEK_TEMPERATURE_SPEC";
        public static final String TAG = "HardwareSpec";
        public static final String TOUCH_BACK_KEY_SPEC_MAX = "TOUCH_BACK_KEY_SPEC_MAX";
        public static final String TOUCH_BACK_KEY_SPEC_MIN = "TOUCH_BACK_KEY_SPEC_MIN";
        public static final String TOUCH_HIDDEN_KEY1_SPEC_MAX = "TOUCH_HIDDEN_KEY1_SPEC_MAX";
        public static final String TOUCH_HIDDEN_KEY1_SPEC_MIN = "TOUCH_HIDDEN_KEY1_SPEC_MIN";
        public static final String TOUCH_HIDDEN_KEY2_SPEC_MAX = "TOUCH_HIDDEN_KEY2_SPEC_MAX";
        public static final String TOUCH_HIDDEN_KEY2_SPEC_MIN = "TOUCH_HIDDEN_KEY2_SPEC_MIN";
        public static final String TOUCH_HIDDEN_KEY3_SPEC_MAX = "TOUCH_HIDDEN_KEY3_SPEC_MAX";
        public static final String TOUCH_HIDDEN_KEY3_SPEC_MIN = "TOUCH_HIDDEN_KEY3_SPEC_MIN";
        public static final String TOUCH_HIDDEN_KEY4_SPEC_MAX = "TOUCH_HIDDEN_KEY4_SPEC_MAX";
        public static final String TOUCH_HIDDEN_KEY4_SPEC_MIN = "TOUCH_HIDDEN_KEY4_SPEC_MIN";
        public static final String TOUCH_KEY_ABOV_SPEC_MAX = "TOUCH_KEY_ABOV_SPEC_MAX";
        public static final String TOUCH_KEY_ABOV_SPEC_MIN = "TOUCH_KEY_ABOV_SPEC_MIN";
        public static final String TOUCH_KEY_CYPRESS_SPEC_MAX = "TOUCH_KEY_CYPRESS_SPEC_MAX";
        public static final String TOUCH_KEY_CYPRESS_SPEC_MIN = "TOUCH_KEY_CYPRESS_SPEC_MIN";
        public static final String TOUCH_KEY_DEFAULT_SCALING = "TOUCH_KEY_DEFAULT_SCALING";
        public static final String TOUCH_KEY_GRAPH_REDUCTION = "TOUCH_KEY_GRAPH_REDUCTION";
        public static final String TOUCH_KEY_OUTER_SPEC_MAX = "TOUCH_KEY_OUTER_SPEC_MAX";
        public static final String TOUCH_KEY_OUTER_SPEC_MIN = "TOUCH_KEY_OUTER_SPEC_MIN";
        public static final String TOUCH_KEY_SPEC_MAX = "TOUCH_KEY_SPEC_MAX";
        public static final String TOUCH_KEY_SPEC_MIN = "TOUCH_KEY_SPEC_MIN";
        public static final String TOUCH_MENU_KEY_SPEC_MAX = "TOUCH_MENU_KEY_SPEC_MAX";
        public static final String TOUCH_MENU_KEY_SPEC_MIN = "TOUCH_MENU_KEY_SPEC_MIN";
        public static final String TOUCH_REF_KEY1_SPEC_MAX = "TOUCH_REF_KEY1_SPEC_MAX";
        public static final String TOUCH_REF_KEY1_SPEC_MIN = "TOUCH_REF_KEY1_SPEC_MIN";
        public static final String TOUCH_REF_KEY2_SPEC_MAX = "TOUCH_REF_KEY2_SPEC_MAX";
        public static final String TOUCH_REF_KEY2_SPEC_MIN = "TOUCH_REF_KEY2_SPEC_MIN";
        public static final String TOUCH_SWITCH_KEY_SPEC_MAX = "TOUCH_SWITCH_KEY_SPEC_MAX";
        public static final String TOUCH_SWITCH_KEY_SPEC_MIN = "TOUCH_SWITCH_KEY_SPEC_MIN";
        public static final String TSP_CONNECTION_MIN = "TSP_CONNECTION_MIN";
        public static final String TSP_DEADZONE_HEIGHT = "TSP_DEADZONE_HEIGHT";
        public static final String TSP_DEADZONE_WIDTH = "TSP_DEADZONE_WIDTH";
        public static final String TSP_HOVER_BLOCK_SIZE_HEIGHT_UM = "TSP_HOVER_BLOCK_SIZE_HEIGHT_UM";
        public static final String TSP_HOVER_BLOCK_SIZE_WIDTH_UM = "TSP_HOVER_BLOCK_SIZE_WIDTH_UM";
        public static final String TSP_HOVER_CIRCLE_INNER_MAGNIFIED = "TSP_HOVER_CIRCLE_INNER_MAGNIFIED";
        public static final String TSP_HOVER_CIRCLE_OUTER_MAGNIFIED = "TSP_HOVER_CIRCLE_OUTER_MAGNIFIED";
        public static final String TSP_NODE_COUNT_HEIGHT = "TSP_NODE_COUNT_HEIGHT";
        public static final String TSP_NODE_COUNT_WIDTH = "TSP_NODE_COUNT_WIDTH";
        public static final String TSP_PROX_ADC_RELEASE_SPEC = "TSP_PROX_ADC_RELEASE_SPEC";
        public static final String TSP_PROX_ADC_WORKING_SPEC = "TSP_PROX_ADC_WORKING_SPEC";
        public static final String TSP_SELFTEST_ABS_CAP_DATA_RX_MAX = "TSP_SELFTEST_ABS_CAP_DATA_RX_MAX";
        public static final String TSP_SELFTEST_ABS_CAP_DATA_RX_MIN = "TSP_SELFTEST_ABS_CAP_DATA_RX_MIN";
        public static final String TSP_SELFTEST_ABS_CAP_DATA_TX_MAX = "TSP_SELFTEST_ABS_CAP_DATA_TX_MAX";
        public static final String TSP_SELFTEST_ABS_CAP_DATA_TX_MIN = "TSP_SELFTEST_ABS_CAP_DATA_TX_MIN";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_CENTER_GREAT_SEC = "TSP_SELFTEST_CM_OFFSET_GAP_CENTER_GREAT_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_ONLY_CORNER_XY_SEC = "TSP_SELFTEST_CM_OFFSET_GAP_ONLY_CORNER_XY_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_ONLY_CORNER_XY_SEC_LOW_FREQ = "TSP_SELFTEST_CM_OFFSET_GAP_ONLY_CORNER_XY_SEC_LOW_FREQ";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_SIDE_X_GREAT_SEC = "TSP_SELFTEST_CM_OFFSET_GAP_SIDE_X_GREAT_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_SPLIT_CORNER_XY_SEC = "TSP_SELFTEST_CM_OFFSET_GAP_SPLIT_CORNER_XY_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_SPLIT_CORNER_XY_SEC_LOW_FREQ = "TSP_SELFTEST_CM_OFFSET_GAP_SPLIT_CORNER_XY_SEC_LOW_FREQ";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_Y1_GREAT_SEC = "TSP_SELFTEST_CM_OFFSET_GAP_Y1_GREAT_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_Y2_GREAT_SEC = "TSP_SELFTEST_CM_OFFSET_GAP_Y2_GREAT_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_GAP_Y3_GREAT_SEC = "TSP_SELFTEST_CM_OFFSET_GAP_Y3_GREAT_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_ONLY_CORNER_MAX_SEC = "TSP_SELFTEST_CM_OFFSET_ONLY_CORNER_MAX_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_ONLY_CORNER_MIN_SEC = "TSP_SELFTEST_CM_OFFSET_ONLY_CORNER_MIN_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_SPLIT_CORNER_GAP_SEC = "TSP_SELFTEST_CM_OFFSET_SPLIT_CORNER_GAP_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_SPLIT_CORNER_MAX_SEC = "TSP_SELFTEST_CM_OFFSET_SPLIT_CORNER_MAX_SEC";
        public static final String TSP_SELFTEST_CM_OFFSET_SPLIT_CORNER_MIN_SEC = "TSP_SELFTEST_CM_OFFSET_SPLIT_CORNER_MIN_SEC";
        public static final String TSP_SELFTEST_CM_PRE_DELTA_MAX_SEC = "TSP_SELFTEST_CM_PRE_DELTA_MAX_SEC";
        public static final String TSP_SELFTEST_CM_PRE_DELTA_MIN_SEC = "TSP_SELFTEST_CM_PRE_DELTA_MIN_SEC";
        public static final String TSP_SELFTEST_CM_UB_YCROSS_SEC = "TSP_SELFTEST_CM_UB_YCROSS_SEC";
        public static final String TSP_SELFTEST_CM_XY_SEC = "TSP_SELFTEST_CM_XY_SEC";
        public static final String TSP_SELFTEST_FORCE_CAL_MAX = "TSP_SELFTEST_FORCE_CAL_MAX";
        public static final String TSP_SELFTEST_FORCE_CAL_MIN = "TSP_SELFTEST_FORCE_CAL_MIN";
        public static final String TSP_SELFTEST_GAP_SEC = "TSP_SELFTEST_GAP_SEC";
        public static final String TSP_SELFTEST_HFDND_HGAP_KEY_MAX_ZINITIX = "TSP_SELFTEST_HFDND_HGAP_KEY_MAX_ZINITIX";
        public static final String TSP_SELFTEST_HFDND_HGAP_VIEW_MAX_ZINITIX = "TSP_SELFTEST_HFDND_HGAP_VIEW_MAX_ZINITIX";
        public static final String TSP_SELFTEST_HFDND_MAX_ZINITIX = "TSP_SELFTEST_HFDND_MAX_ZINITIX";
        public static final String TSP_SELFTEST_HFDND_MIN_ZINITIX = "TSP_SELFTEST_HFDND_MIN_ZINITIX";
        public static final String TSP_SELFTEST_HFDND_VGAP_KEY_MAX_ZINITIX = "TSP_SELFTEST_HFDND_VGAP_KEY_MAX_ZINITIX";
        public static final String TSP_SELFTEST_HFDND_VGAP_VIEW_MAX_ZINITIX = "TSP_SELFTEST_HFDND_VGAP_VIEW_MAX_ZINITIX";
        public static final String TSP_SELFTEST_HGAP_KEY_MAX_ZINITIX = "TSP_SELFTEST_HGAP_KEY_MAX_ZINITIX";
        public static final String TSP_SELFTEST_HGAP_VIEW_MAX_ZINITIX = "TSP_SELFTEST_HGAP_VIEW_MAX_ZINITIX";
        public static final String TSP_SELFTEST_KEY_CM_DATA_MAX_STM = "TSP_SELFTEST_KEY_CM_DATA_MAX_STM";
        public static final String TSP_SELFTEST_KEY_CM_DATA_MIN_STM = "TSP_SELFTEST_KEY_CM_DATA_MIN_STM";
        public static final String TSP_SELFTEST_MAX2 = "TSP_SELFTEST_MAX2";
        public static final String TSP_SELFTEST_MAX_SEC = "TSP_SELFTEST_MAX_SEC";
        public static final String TSP_SELFTEST_MIN2 = "TSP_SELFTEST_MIN2";
        public static final String TSP_SELFTEST_MIN_SEC = "TSP_SELFTEST_MIN_SEC";
        public static final String TSP_SELFTEST_REFERENCE_DIFF_H_MAX = "TSP_SELFTEST_REFERENCE_DIFF_H_MAX";
        public static final String TSP_SELFTEST_REFERENCE_DIFF_V_MAX = "TSP_SELFTEST_REFERENCE_DIFF_V_MAX";
        public static final String TSP_SELFTEST_RX_SHORT_KEY_MAX_ZINITIX = "TSP_SELFTEST_RX_SHORT_KEY_MAX_ZINITIX";
        public static final String TSP_SELFTEST_RX_SHORT_VIEW_MAX_ZINITIX = "TSP_SELFTEST_RX_SHORT_VIEW_MAX_ZINITIX";
        public static final String TSP_SELFTEST_SELF_DND_MAX_ZINITIX = "TSP_SELFTEST_SELF_DND_MAX_ZINITIX";
        public static final String TSP_SELFTEST_SELF_DND_MIN_ZINITIX = "TSP_SELFTEST_SELF_DND_MIN_ZINITIX";
        public static final String TSP_SELFTEST_SELF_HGAP_VIEW_MAX_ZINITIX = "TSP_SELFTEST_SELF_HGAP_VIEW_MAX_ZINITIX";
        public static final String TSP_SELFTEST_SELF_OFFSET_X_GAP = "TSP_SELFTEST_SELF_OFFSET_X_GAP";
        public static final String TSP_SELFTEST_SELF_OFFSET_X_MAX = "TSP_SELFTEST_SELF_OFFSET_X_MAX";
        public static final String TSP_SELFTEST_SELF_OFFSET_X_MIN = "TSP_SELFTEST_SELF_OFFSET_X_MIN";
        public static final String TSP_SELFTEST_SELF_OFFSET_Y_GAP = "TSP_SELFTEST_SELF_OFFSET_Y_GAP";
        public static final String TSP_SELFTEST_SELF_OFFSET_Y_MAX = "TSP_SELFTEST_SELF_OFFSET_Y_MAX";
        public static final String TSP_SELFTEST_SELF_OFFSET_Y_MIN = "TSP_SELFTEST_SELF_OFFSET_Y_MIN";
        public static final String TSP_SELFTEST_SELF_PRE_DELTA_GAP = "TSP_SELFTEST_SELF_PRE_DELTA_GAP";
        public static final String TSP_SELFTEST_SELF_PRE_DELTA_MAX = "TSP_SELFTEST_SELF_PRE_DELTA_MAX";
        public static final String TSP_SELFTEST_SELF_PRE_DELTA_MIN = "TSP_SELFTEST_SELF_PRE_DELTA_MIN";
        public static final String TSP_SELFTEST_SLOPE_MAX = "TSP_SELFTEST_SLOPE_MAX";
        public static final String TSP_SELFTEST_SLOPE_MIN = "TSP_SELFTEST_SLOPE_MIN";
        public static final String TSP_SELFTEST_SPEC_MAX_MINUS_MIN = "TSP_SELFTEST_SPEC_MAX_MINUS_MIN";
        public static final String TSP_SELFTEST_TX_SHORT_KEY_MAX_ZINITIX = "TSP_SELFTEST_TX_SHORT_KEY_MAX_ZINITIX";
        public static final String TSP_SELFTEST_TX_SHORT_VIEW_MAX_ZINITIX = "TSP_SELFTEST_TX_SHORT_VIEW_MAX_ZINITIX";
        public static final String TSP_SELFTEST_VGAP_KEY_MAX_ZINITIX = "TSP_SELFTEST_VGAP_KEY_MAX_ZINITIX";
        public static final String TSP_SELFTEST_VGAP_VIEW_MAX_ZINITIX = "TSP_SELFTEST_VGAP_VIEW_MAX_ZINITIX";
        public static final String TSP_SPEN_NODE_COUNT_HEIGHT = "TSP_SPEN_NODE_COUNT_HEIGHT";
        public static final String TSP_SPEN_NODE_COUNT_WIDTH = "TSP_SPEN_NODE_COUNT_WIDTH";
        public static final String TSP_X_AXIS_CHANNEL = "TSP_X_AXIS_CHANNEL";
        public static final String TSP_Y_AXIS_CHANNEL = "TSP_Y_AXIS_CHANNEL";
        public static final String VCONN_SPEC_MAX = "VCONN_SPEC_MAX";
        public static final String VCONN_SPEC_MIN = "VCONN_SPEC_MIN";
        public static final String WACOM_HEIGHT_BASIS = "WACOM_HEIGHT_BASIS";
        public static final String WACOM_WIDTH_BASIS = "WACOM_WIDTH_BASIS";
        public static final String WATERPROOF_IS_OPEN_SPEC = "WATERPROOF_IS_OPEN_SPEC";
        public static final String WATERPROOF_L_LEAK_MAIN = "WATERPROOF_L_LEAK_MAIN";
        public static final String WATERPROOF_L_LEAK_SUB = "WATERPROOF_L_LEAK_SUB";
        public static final String WATERPROOF_S_LEAK_MAIN = "WATERPROOF_S_LEAK_MAIN";
        public static final String WATERPROOF_S_LEAK_SUB = "WATERPROOF_S_LEAK_SUB";
        public static final String WIRELESSS_AFC_VOLTAGE = "WIRELESSS_AFC_VOLTAGE";
        public static final String WIRELESS_AFC_CURRENT_SPEC_MAX = "WIRELESS_AFC_CURRENT_SPEC_MAX";
        public static final String WIRELESS_AFC_CURRENT_SPEC_MIN = "WIRELESS_AFC_CURRENT_SPEC_MIN";
        public static final String WIRELESS_CHARGING_TX_EFFICIENCY_MIN = "WIRELESS_CHARGING_TX_EFFICIENCY_MIN";
        public static final String WIRELESS_CHARGING_TX_IIN3_MAX = "WIRELESS_CHARGING_TX_IIN3_MAX";
        public static final String WIRELESS_CHARGING_TX_IIN3_MIN = "WIRELESS_CHARGING_TX_IIN3_MIN";
        public static final String WIRELESS_CHARGING_TX_IOUT_MAX = "WIRELESS_CHARGING_TX_IOUT_MAX";
        public static final String WIRELESS_CHARGING_TX_IOUT_MIN = "WIRELESS_CHARGING_TX_IOUT_MIN";
        public static final String WIRELESS_CHARGING_TX_VIN3_MAX = "WIRELESS_CHARGING_TX_VIN3_MAX";
        public static final String WIRELESS_CHARGING_TX_VIN3_MIN = "WIRELESS_CHARGING_TX_VIN3_MIN";
        public static final String WIRELESS_CHARGING_TX_VOUT_MAX = "WIRELESS_CHARGING_TX_VOUT_MAX";
        public static final String WIRELESS_CHARGING_TX_VOUT_MIN = "WIRELESS_CHARGING_TX_VOUT_MIN";
        public static final String WIRELESS_CURRENT_SPEC_MAX = "WIRELESS_CURRENT_SPEC_MAX";
        public static final String WIRELESS_CURRENT_SPEC_MIN = "WIRELESS_CURRENT_SPEC_MIN";
        public static final String WIRELESS_NORMAL_CHARGE_VOLTAGE = "WIRELESS_NORMAL_CHARGE_VOLTAGE";

        public static boolean getBoolean(String id) {
            return Values.getBoolean(id, "value");
        }

        public static boolean getBoolean(String id, int logLevel) {
            return Values.getBoolean(id, "value", logLevel);
        }

        public static byte getByte(String id) {
            return Values.getByte(id, "value");
        }

        public static String getString(String id) {
            return Values.getString(id, "value");
        }

        public static String getString_semi(String id) {
            return Values.getString(id, "value_semi");
        }

        public static int getInt_semi(String id) {
            return Values.getInt(id, "value_semi");
        }

        public static int getInt(String id) {
            return Values.getInt(id, "value");
        }

        public static long getLong(String id) {
            return Values.getLong(id, "value");
        }

        public static float getFloat(String id) {
            return Values.getFloat(id, "value");
        }

        public static double getDouble(String id) {
            return Values.getDouble(id, "value");
        }

        public static boolean getInvisible(String id) {
            return Values.getBoolean(id, "invisible");
        }
    }

    public static class SystemInfo {
        public static final String SYS_INFO_BAND_CHANNEL = "SYS_INFO_BAND_CHANNEL";
        public static final String SYS_INFO_BATTERY_LEVEL = "SYS_INFO_BATTERY_LEVEL";
        public static final String SYS_INFO_CAMERA_FIRM_VERSION = "SYS_INFO_CAMERA_FIRM_VERSION";
        public static final String SYS_INFO_CSC_VERSION = "SYS_INFO_CSC_VERSION";
        public static final String SYS_INFO_CURRENT_CP = "SYS_INFO_CURRENT_CP";
        public static final String SYS_INFO_HW_VERSION = "SYS_INFO_HW_VERSION";
        public static final String SYS_INFO_LCD_VERSION = "SYS_INFO_LCD_VERSION";
        public static final String SYS_INFO_LTE_VERSION = "SYS_INFO_LTE_VERSION";
        public static final String SYS_INFO_MEMORY_SIZE = "SYS_INFO_MEMORY_SIZE";
        public static final String SYS_INFO_MODEL_NAME = "SYS_INFO_MODEL_NAME";
        public static final String SYS_INFO_OIS_CAMERA_FIRM_VERSION = "SYS_INFO_OIS_CAMERA_FIRM_VERSION";
        public static final String SYS_INFO_PDA_VERSION = "SYS_INFO_PDA_VERSION";
        public static final String SYS_INFO_PHONE2_VERSION = "SYS_INFO_PHONE2_VERSION";
        public static final String SYS_INFO_PHONE_VERSION = "SYS_INFO_PHONE_VERSION";
        public static final String SYS_INFO_RF_CAL_DATA = "SYS_INFO_RF_CAL_DATA";
        public static final String SYS_INFO_SENSOR_HUB_FIRM_VERSION = "SYS_INFO_SENSOR_HUB_FIRM_VERSION";
        public static final String SYS_INFO_SIO_MODE = "SYS_INFO_SIO_MODE";
        public static final String SYS_INFO_SMD_PDA = "SYS_INFO_SMD_PDA";
        public static final String SYS_INFO_SPEN_FIRM_VERSION = "SYS_INFO_SPEN_FIRM_VERSION";
        public static final String SYS_INFO_TSK_FIRM_VERSION = "SYS_INFO_TSK_FIRM_VERSION";
        public static final String SYS_INFO_TSP_FIRM_VERSION = "SYS_INFO_TSP_FIRM_VERSION";
        public static final String SYS_INFO_UNIQUE_NUMBER = "SYS_INFO_UNIQUE_NUMBER";
        public static final String SYS_INFO_WIDGET_FONT_SIZE = "SYS_INFO_WIDGET_FONT_SIZE";
        public static final String TAG = "SystemInfo";

        public static int getWidgetFontSize() {
            return Values.getInt(SYS_INFO_WIDGET_FONT_SIZE, "size");
        }

        public static boolean getVisibility(String id) {
            return Values.getBoolean(id, "visibility");
        }
    }

    public static class TestCase {
        public static final String ACCELEROMETER_FULL_SCALE = "ACCELEROMETER_FULL_SCALE";
        public static final String ACCSENSOR_DATA_RANGE = "ACCSENSOR_DATA_RANGE";
        public static final String AT_BATTEST_RESET_WHEN_READ = "AT_BATTEST_RESET_WHEN_READ";
        public static final String AT_CALIDATE = "AT_CALIDATE";
        public static final String AT_GPSSTEST = "AT_GPSSTEST";
        public static final String AT_HMACMISM = "AT_HMACMISM";
        public static final String AT_ISDBTEST = "AT_ISDBTEST";
        public static final String CAMERA_TORCH_MODE_RESPONSE_MARGINE = "CAMERA_TORCH_MODE_RESPONSE_MARGINE";
        public static final String CCIC_LPM_MODE_ENABLE_VALUE = "CCIC_LPM_MODE_ENABLE_VALUE";
        public static final String COLOR_COMPENSATION_MDNIE_CUSTOM_INDEX_SHIFT = "COLOR_COMPENSATION_MDNIE_CUSTOM_INDEX_SHIFT";
        public static final String COLOR_COMPENSATION_PREVIEW_100G_IMAGE_SET = "COLOR_COMPENSATION_PREVIEW_100G_IMAGE_SET";
        public static final String COLOR_COMPENSATION_PREVIEW_IMAGE_SET = "COLOR_COMPENSATION_PREVIEW_IMAGE_SET";
        public static final String CROSSTALK_VALUE_DIRECTION = "CROSSTALK_VALUE_DIRECTION";
        public static final String DELAY_BEFORE_ECHO_TEST = "DELAY_BEFORE_ECHO_TEST";
        public static final String FORCE_TOUCH_CAL_APPLY_WEIGHT = "FORCE_TOUCH_CAL_APPLY_WEIGHT";
        public static final String FORCE_TOUCH_CAL_EXCLUDE_MIN_MAX_VALUE = "FORCE_TOUCH_CAL_EXCLUDE_MIN_MAX_VALUE";
        public static final String FORCE_TOUCH_CAL_EXCLUDE_PEAK_VALUE = "FORCE_TOUCH_CAL_EXCLUDE_PEAK_VALUE";
        public static final String FORCE_TOUCH_CAL_STRENGTH_READ_COUNT = "FORCE_TOUCH_CAL_STRENGTH_READ_COUNT";
        public static final String FORCE_TOUCH_CAL_TEST_COUNT = "FORCE_TOUCH_CAL_TEST_COUNT";
        public static final String FORCE_TOUCH_DELAY_FOR_MEASUREMENT = "FORCE_TOUCH_DELAY_FOR_MEASUREMENT";
        public static final String FORCE_TOUCH_MAKE_OPEN_SPEC = "FORCE_TOUCH_MAKE_OPEN_SPEC";
        public static final String FORCE_TOUCH_PRESS_STRENGTH_READ_COUNT = "FORCE_TOUCH_PRESS_STRENGTH_READ_COUNT";
        public static final String FORCE_TOUCH_SELF_TEST_CHANNEL = "FORCE_TOUCH_SELF_TEST_CHANNEL";
        public static final String FORCE_TOUCH_TEST_CENTER = "FORCE_TOUCH_TEST_CENTER";
        public static final String FORCE_TOUCH_TEST_LEFT = "FORCE_TOUCH_TEST_LEFT";
        public static final String FORCE_TOUCH_TEST_RIGHT = "FORCE_TOUCH_TEST_RIGHT";
        public static final String FORCE_TOUCH_USING_UNIT_MM = "FORCE_TOUCH_USING_UNIT_MM";
        public static final String GEOMAGNETIC_ALPS_CAL_DELTA_SPEC = "GEOMAGNETIC_ALPS_CAL_DELTA_SPEC";
        public static final String GEOMAGNETIC_IC_POINT = "GEOMAGNETIC_IC_POINT";
        public static final String GESTURE_SENSOR_IR_CURRENT_CHANGE = "GESTURE_SENSOR_IR_CURRENT_CHANGE";
        public static final String GESTURE_SENSOR_PEEK_TO_PEEK = "GESTURE_SENSOR_PEEK_TO_PEEK";
        public static final String GRIPSENSOR_GRIP_COUNT = "GRIPSENSOR_GRIP_COUNT";
        public static final String HALLIC_TEST_MILLIS_SEC = "HALLIC_TEST_MILLIS_SEC";
        public static final String HALL_IC_SEMI_TEST = "HALL_IC_SEMI_TEST";
        public static final String HALL_IC_SEMI_TEST_2ND = "HALL_IC_SEMI_TEST_2ND";
        public static final String HALL_IC_TEST = "HALL_IC_TEST";
        public static final String HALL_IC_TEST_2ND = "HALL_IC_TEST_2ND";
        public static final String HUMITEMP_TEST = "HUMITEMP_TEST";
        public static final String HV_CHARGE = "HV_CHARGE";
        public static final String INTERVAL_FOR_ACC_SENSOR_FLATNESS = "INTERVAL_FOR_ACC_SENSOR_FLATNESS";
        public static final String IN_DISPLAY_FINGERPRINT_AREA = "IN_DISPLAY_FINGERPRINT_AREA";
        public static final String IN_DISPLAY_FINGERPRINT_MSG = "IN_DISPLAY_FINGERPRINT_MSG";
        public static final String IS_ACCGRAPH_PROXIMITY_MOTOR_FEEDBACK = "IS_ACCGRAPH_PROXIMITY_MOTOR_FEEDBACK";
        public static final String IS_ASAHI_USING_SYSFS = "IS_ASAHI_USING_SYSFS";
        public static final String IS_ATMEL_TSP_IC_TOUCHKEY = "IS_ATMEL_TSP_IC_TOUCHKEY";
        public static final String IS_BACKLIGHT_ON_FOR_GREEN_LCD = "IS_BACKLIGHT_ON_FOR_GREEN_LCD";
        public static final String IS_CM_GAP_TX_RX = "IS_CM_GAP_TX_RX";
        public static final String IS_CP_LPM_CHECK = "IS_CP_LPM_CHECK";
        public static final String IS_CP_SHORT_CHECK = "IS_CP_SHORT_CHECK";
        public static final String IS_CR_CHECK = "IS_CR_CHECK";
        public static final String IS_DISPLAY_LIGHT_SENSOR_ADC_ONLY = "IS_DISPLAY_LIGHT_SENSOR_ADC_ONLY";
        public static final String IS_DISPLAY_LIGHT_SENSOR_SLOW = "IS_DISPLAY_LIGHT_SENSOR_SLOW";
        public static final String IS_DISPLAY_LUX_ADC = "IS_DISPLAY_LUX_ADC";
        public static final String IS_DUMMYKEY_SAME_KEYCODE = "IS_DUMMYKEY_SAME_KEYCODE";
        public static final String IS_HIDDEN_TOUCHKEY = "IS_HIDDEN_TOUCHKEY";
        public static final String IS_HVDD_CHECK = "IS_HVDD_CHECK";
        public static final String IS_INNER_OUTER_TOUCHKEY_KEYSTRING = "IS_INNER_OUTER_TOUCHKEY_KEYSTRING";
        public static final String IS_IRLED_TEST_SPLIT_COMMAND = "IS_IRLED_TEST_SPLIT_COMMAND";
        public static final String IS_KEY_TEST_THRESHOLD = "IS_KEY_TEST_THRESHOLD";
        public static final String IS_MIS_CALIBRATION_CHECK = "IS_MIS_CALIBRATION_CHECK";
        public static final String IS_NEW_TSP_SELFTEST_SYNAPTICS = "IS_NEW_TSP_SELFTEST_SYNAPTICS";
        public static final String IS_NEW_TSP_SELFTEST_ZINITIX = "IS_NEW_TSP_SELFTEST_ZINITIX";
        public static final String IS_NEW_TSP_SELFTEST_ZINITIX_GF1 = "IS_NEW_TSP_SELFTEST_ZINITIX_GF1";
        public static final String IS_PROXIMITY_TEST_MOTOR_FEEDBACK = "IS_PROXIMITY_TEST_MOTOR_FEEDBACK";
        public static final String IS_RUN_CALIBRATION = "IS_RUN_CALIBRATION";
        public static final String IS_SENSOR_TEST_ACC_REVERSE = "IS_SENSOR_TEST_ACC_REVERSE";
        public static final String IS_TSP_CM_RATIO = "IS_TSP_CM_RATIO";
        public static final String IS_TSP_HOVERING_TEST_SET_EDGE_DEADLOCK = "IS_TSP_HOVERING_TEST_SET_EDGE_DEADLOCK";
        public static final String IS_TSP_SECOND_LCD_TEST = "IS_TSP_SECOND_LCD_TEST";
        public static final String IS_TSP_SEC_CM_OFFSET_GAP_GREAT_STYLE = "IS_TSP_SEC_CM_OFFSET_GAP_GREAT_STYLE";
        public static final String IS_TSP_SEC_CM_SPEC_OVER = "IS_TSP_SEC_CM_SPEC_OVER";
        public static final String IS_TSP_SEC_SPLIT_CORNER = "IS_TSP_SEC_SPLIT_CORNER";
        public static final String IS_TSP_SEC_UB_CROSS_ROUTING_PORTRAIT = "IS_TSP_SEC_UB_CROSS_ROUTING_PORTRAIT";
        public static final String IS_TSP_SELFTEST_SEC = "IS_TSP_SELFTEST_SEC";
        public static final String IS_TSP_SELFTEST_ZINITIX_H_V_GAP_KEY = "IS_TSP_SELFTEST_ZINITIX_H_V_GAP_KEY";
        public static final String IS_TSP_STANDARD_CHANNEL = "IS_TSP_STANDARD_CHANNEL";
        public static final String IS_TX_RX_SELF_CR = "IS_TX_RX_SELF_CR";
        public static final String IS_USING_SENSOR_MANAGER_IN_POWER_NOISE = "IS_USING_SENSOR_MANAGER_IN_POWER_NOISE";
        public static final String IS_VIBETONZ_UNSUPPORTED = "IS_VIBETONZ_UNSUPPORTED";
        public static final String IS_VIBETONZ_UNSUPPORTED_HW = "IS_VIBETONZ_UNSUPPORTED_HW";
        public static final int KEYCODE_END = 6;
        public static final String KEY_TEST_0 = "KEY_TEST_0";
        public static final String KEY_TEST_1 = "KEY_TEST_1";
        public static final String KEY_TEST_2 = "KEY_TEST_2";
        public static final String KEY_TEST_3 = "KEY_TEST_3";
        public static final String KEY_TEST_4 = "KEY_TEST_4";
        public static final String KEY_TEST_5 = "KEY_TEST_5";
        public static final String KEY_TEST_6 = "KEY_TEST_6";
        public static final String KEY_TEST_7 = "KEY_TEST_7";
        public static final String KEY_TEST_8 = "KEY_TEST_8";
        public static final String KEY_TEST_9 = "KEY_TEST_9";
        public static final String KEY_TEST_APP_RECENT = "KEY_TEST_APP_RECENT";
        public static final String KEY_TEST_APP_SELECT = "KEY_TEST_APP_SELECT";
        public static final String KEY_TEST_APP_SWITCH = "KEY_TEST_APP_SWITCH";
        public static final String KEY_TEST_BACK = "KEY_TEST_BACK";
        public static final String KEY_TEST_BEAM = "KEY_TEST_BEAM";
        public static final String KEY_TEST_BUTTON_SPACE = "KEY_TEST_BUTTON_SPACE";
        public static final String KEY_TEST_CALL = "KEY_TEST_CALL";
        public static final String KEY_TEST_CAMERA = "KEY_TEST_CAMERA";
        public static final String KEY_TEST_CONTACTS = "KEY_TEST_CONTACTS";
        public static final String KEY_TEST_DEL = "KEY_TEST_DEL";
        public static final String KEY_TEST_DPAD_CENTER = "KEY_TEST_DPAD_CENTER";
        public static final String KEY_TEST_DPAD_DOWN = "KEY_TEST_DPAD_DOWN";
        public static final String KEY_TEST_DPAD_LEFT = "KEY_TEST_DPAD_LEFT";
        public static final String KEY_TEST_DPAD_RIGHT = "KEY_TEST_DPAD_RIGHT";
        public static final String KEY_TEST_DPAD_UP = "KEY_TEST_DPAD_UP";
        public static final String KEY_TEST_EMERGENCY = "KEY_TEST_EMERGENCY";
        public static final String KEY_TEST_END = "KEY_TEST_END";
        public static final String KEY_TEST_F1 = "KEY_TEST_F1";
        public static final String KEY_TEST_FOCUS = "KEY_TEST_FOCUS";
        public static final String KEY_TEST_HIDDEN_1 = "KEY_TEST_HIDDEN_1";
        public static final String KEY_TEST_HIDDEN_2 = "KEY_TEST_HIDDEN_2";
        public static final String KEY_TEST_HIDDEN_3 = "KEY_TEST_HIDDEN_3";
        public static final String KEY_TEST_HIDDEN_4 = "KEY_TEST_HIDDEN_4";
        public static final String KEY_TEST_HOME = "KEY_TEST_HOME";
        public static final String KEY_TEST_INTELLI = "KEY_TEST_INTELLI";
        public static final String KEY_TEST_MENU = "KEY_TEST_MENU";
        public static final String KEY_TEST_MESSAGE = "KEY_TEST_MESSAGE";
        public static final String KEY_TEST_NETWOR_SEL = "KEY_TEST_NETWOR_SEL";
        public static final String KEY_TEST_POUND = "KEY_TEST_POUND";
        public static final String KEY_TEST_POWER = "KEY_TEST_POWER";
        public static final String KEY_TEST_REFERENCE1 = "KEY_TEST_REFERENCE1";
        public static final String KEY_TEST_REFERENCE2 = "KEY_TEST_REFERENCE2";
        public static final String KEY_TEST_SEARCH = "KEY_TEST_SEARCH";
        public static final String KEY_TEST_STAR = "KEY_TEST_STAR";
        public static final String KEY_TEST_SUB_APP_RECENT = "KEY_TEST_SUB_APP_RECENT";
        public static final String KEY_TEST_SUB_BACK = "KEY_TEST_SUB_BACK";
        public static final String KEY_TEST_TEXT_SIZE = "KEY_TEST_TEXT_SIZE";
        public static final String KEY_TEST_TOUCH_KEY_ODER = "KEY_TEST_TOUCH_KEY_ODER";
        public static final String KEY_TEST_TOUCH_KEY_ODER_HIDDEN = "KEY_TEST_TOUCH_KEY_ODER_HIDDEN";
        public static final String KEY_TEST_USER = "KEY_TEST_USER";
        public static final String KEY_TEST_VIEW_TABLE = "KEY_TEST_VIEW_TABLE";
        public static final String KEY_TEST_VOLUME_DOWN = "KEY_TEST_VOLUME_DOWN";
        public static final String KEY_TEST_VOLUME_UP = "KEY_TEST_VOLUME_UP";
        public static final String LOAD_CHARGE = "LOAD_CHARGE";
        public static final String LPA_MODE_DISABLE_VALUE = "LPA_MODE_DISABLE_VALUE";
        public static final String LPA_MODE_ENABLE_VALUE = "LPA_MODE_ENABLE_VALUE";
        public static final String LPM_MODE_DISABLE_VALUE = "LPM_MODE_DISABLE_VALUE";
        public static final String MAGNETIC_POWERNOISE_GRAPH_Y_RANGE = "MAGNETIC_POWERNOISE_GRAPH_Y_RANGE";
        public static final String MAIN_NAD_COUNT = "MAIN_NAD_COUNT";
        public static final String MIS_CALIBRATION_CHECK_SPEC = "MIS_CALIBRATION_CHECK_SPEC";
        public static final String MOBILE_TV_TYPE = "MOBILE_TV_TYPE";
        public static final String MOTOR_SPEC = "MOTOR_SPEC";
        public static final String NAD_TEST_COUNT = "NAD_TEST_COUNT";
        public static final String NEED_POWER_POLICY_ENABLE = "NEED_POWER_POLICY_ENABLE";
        public static final String NFC_CARD_MODE_UI_NOT_USE = "NFC_CARD_MODE_UI_NOT_USE";
        public static final String NFC_ESE_AND_AP_COMMUNICATION = "NFC_ESE_AND_AP_COMMUNICATION";
        public static final String NFC_ESE_TEST = "NFC_ESE_TEST";
        public static final String NFC_RWP2P_ENABLE = "NFC_RWP2P_ENABLE";
        public static final String NFC_SWP_TEST = "NFC_SWP_TEST";
        public static final String NFC_SWP_TEST_2ND = "NFC_SWP_TEST_2ND";
        public static final String NFC_TYPE_B_TEST_ITEM = "NFC_TYPE_B_TEST_ITEM";
        public static final String OTG2 = "OTG2";
        public static final String OTG3 = "OTG3";
        public static final String PA0_TEMP_ADC = "PA0_TEMP_ADC";
        public static final String PA0_THERMISTER_CELCIUS_DIVIDER = "PA0_THERMISTER_CELCIUS_DIVIDER";
        public static final String PA1_TEMP_ADC = "PA1_TEMP_ADC";
        public static final String PATH_SPK_CAL = "PATH_SPK_CAL";
        public static final String PA_TEMP_ADC_VAL_TYPE = "PA_TEMP_ADC_VAL_TYPE";
        public static final String PD_CHARGE = "PD_CHARGE";
        public static final String QWERTY_EXIT_BUTTON_SIZE = "QWERTY_EXIT_BUTTON_SIZE";
        public static final String QWERTY_KEY_TEST_ROW_1 = "QWERTY_KEY_TEST_ROW_1";
        public static final String QWERTY_KEY_TEST_ROW_10 = "QWERTY_KEY_TEST_ROW_10";
        public static final String QWERTY_KEY_TEST_ROW_11 = "QWERTY_KEY_TEST_ROW_11";
        public static final String QWERTY_KEY_TEST_ROW_2 = "QWERTY_KEY_TEST_ROW_2";
        public static final String QWERTY_KEY_TEST_ROW_3 = "QWERTY_KEY_TEST_ROW_3";
        public static final String QWERTY_KEY_TEST_ROW_4 = "QWERTY_KEY_TEST_ROW_4";
        public static final String QWERTY_KEY_TEST_ROW_5 = "QWERTY_KEY_TEST_ROW_5";
        public static final String QWERTY_KEY_TEST_ROW_6 = "QWERTY_KEY_TEST_ROW_6";
        public static final String QWERTY_KEY_TEST_ROW_7 = "QWERTY_KEY_TEST_ROW_7";
        public static final String QWERTY_KEY_TEST_ROW_8 = "QWERTY_KEY_TEST_ROW_8";
        public static final String QWERTY_KEY_TEST_ROW_9 = "QWERTY_KEY_TEST_ROW_9";
        public static final String QWERTY_KEY_TEXT_SIZE = "QWERTY_KEY_TEXT_SIZE";
        public static final String QWERTY_KEY_TEXT_VIEW_HEIGHT = "QWERTY_KEY_TEXT_VIEW_HEIGHT";
        public static final String READ_TARGET_GEOMAGNETIC = "READ_TARGET_GEOMAGNETIC";
        public static final String SEMI_KEY_TEST_BACK = "SEMI_KEY_TEST_BACK";
        public static final String SEMI_KEY_TEST_EMERGENCY = "SEMI_KEY_TEST_EMERGENCY";
        public static final String SEMI_KEY_TEST_HOME = "SEMI_KEY_TEST_HOME";
        public static final String SEMI_KEY_TEST_INTELLI = "SEMI_KEY_TEST_INTELLI";
        public static final String SEMI_KEY_TEST_POWER = "SEMI_KEY_TEST_POWER";
        public static final String SEMI_KEY_TEST_RECENT = "SEMI_KEY_TEST_RECENT";
        public static final String SEMI_KEY_TEST_USER = "SEMI_KEY_TEST_USER";
        public static final String SEMI_KEY_TEST_VOLUME_DOWN = "SEMI_KEY_TEST_VOLUME_DOWN";
        public static final String SEMI_KEY_TEST_VOLUME_UP = "SEMI_KEY_TEST_VOLUME_UP";
        public static final String SENSOR_TEST_ACC_BIT = "SENSOR_TEST_ACC_BIT";
        public static final String SENSOR_TEST_MARGIN_TOP = "SENSOR_TEST_MARGIN_TOP";
        public static final String SIMPLE_SIDE_KEY_TEST_INTELLI = "SIMPLE_SIDE_KEY_TEST_INTELLI";
        public static final String SIMPLE_SIDE_KEY_TEST_POWER = "SIMPLE_SIDE_KEY_TEST_POWER";
        public static final String SIMPLE_SIDE_KEY_TEST_USER = "SIMPLE_SIDE_KEY_TEST_USER";
        public static final String SIMPLE_SIDE_KEY_TEST_VOLUME_DOWN = "SIMPLE_SIDE_KEY_TEST_VOLUME_DOWN";
        public static final String SIMPLE_SIDE_KEY_TEST_VOLUME_UP = "SIMPLE_SIDE_KEY_TEST_VOLUME_UP";
        public static final String SIMPLE_TEST_MEGACAM_SCALE_VALUE = "SIMPLE_TEST_MEGACAM_SCALE_VALUE";
        public static final String SIMPLE_TEST_SUBCAM_SCALE_VALUE = "SIMPLE_TEST_SUBCAM_SCALE_VALUE";
        public static final String SPEAKER_POSITION = "SPEAKER_POSITION";
        public static final String SUPPORT_CHARGE_COUNT = "SUPPORT_CHARGE_COUNT";
        public static final String SUPPORT_CMR_GAP_DISPLAY = "SUPPORT_CMR_GAP_DISPLAY";
        public static final String SUPPORT_COLOR_COMPENSATION = "SUPPORT_COLOR_COMPENSATION";
        public static final String SUPPORT_COMPLEX_LOOPBACK_PATH = "SUPPORT_COMPLEX_LOOPBACK_PATH";
        public static final String SUPPORT_GEOMAGNETIC_ALPS_CAL = "SUPPORT_GEOMAGNETIC_ALPS_CAL";
        public static final String SUPPORT_GYRO_SET_DPS = "SUPPORT_GYRO_SET_DPS";
        public static final String SUPPORT_SEMI_MAXCLOCK_VALUE = "SUPPORT_SEMI_MAXCLOCK_VALUE";
        public static final String SUPPORT_SYNAPTICS_TRX_OPEN_AVERAGE_TEST = "SUPPORT_SYNAPTICS_TRX_OPEN_AVERAGE_TEST";
        public static final String SUPPORT_SYNAPTICS_TRX_OPEN_TEST = "SUPPORT_SYNAPTICS_TRX_OPEN_TEST";
        public static final String SUPPORT_TSP_SELFTEST_INCELL = "SUPPORT_TSP_SELFTEST_INCELL";
        public static final String SUPPORT_TSP_SELFTEST_TX_RX_SHORT = "SUPPORT_TSP_SELFTEST_TX_RX_SHORT";
        public static final String SUPPORT_TSP_SRAM_TEST = "SUPPORT_TSP_SRAM_TEST";
        public static final String TAG = "TestCase";
        public static final String TSP_SELFTEST_OPEN_SHORT_ZINITIX = "TSP_SELFTEST_OPEN_SHORT_ZINITIX";
        public static final String TSP_SELFTEST_SEPARATE_COUNT = "TSP_SELFTEST_SEPARATE_COUNT";
        public static final String TSP_SELFTEST_SEPARATE_DIRECTION_POINT = "TSP_SELFTEST_SEPARATE_DIRECTION_POINT";
        public static final String USB3 = "USB3";
        public static final String USB31 = "USB31";
        public static final String USB32 = "USB32";
        public static final String USE_DIFFERENT_TSP_CONNECTION_CHECK = "USE_DIFFERENT_TSP_CONNECTION_CHECK";
        public static final String VCONN_CHARGE = "VCONN_CHARGE";
        public static final String WATER_DETECT = "WATER_DETECT";

        public static boolean isTouchkey(String id) {
            return Values.getString(id, "keytype").equalsIgnoreCase("touch");
        }

        public static Point getViewPoint(String id) {
            String position = Values.getString(id, "point");
            Point point = new Point();
            if (position != null) {
                String[] pointStr = position.split(",");
                point.x = Integer.parseInt(pointStr[0]);
                point.y = Integer.parseInt(pointStr[1]);
            }
            return point;
        }

        public static PointF getViewPointF(String id) {
            String position = Values.getString(id, "point");
            PointF point = new PointF();
            if (position != null) {
                String[] pointStr = position.split(",");
                point.x = Float.parseFloat(pointStr[0]);
                point.y = Float.parseFloat(pointStr[1]);
            }
            return point;
        }

        public static float getKeyTextSize(String id) {
            return Values.getFloat(id, "size");
        }

        public static String getUnit(String id) {
            return Values.getString(id, "unit");
        }

        public static boolean getEnabled(String id) {
            return Values.getBoolean(id, "enable");
        }

        public static String getString(String id) {
            return Values.getString(id, "value");
        }

        public static String getTestName(String id) {
            return Values.getString(id, "testname");
        }

        public static int getTestOrder(String id) {
            return Values.getInt(id, "order");
        }

        public static int getInt(String id) {
            return Values.getInt(id, "value");
        }

        public static double getDouble(String id) {
            return Values.getDouble(id, "value");
        }

        public static int getCount(String id) {
            return Values.getInt(id, "count");
        }

        public static int getIntValue(String id) {
            return Values.getInt(id, "intValue");
        }

        public static float getSize(String id) {
            return Values.getFloat(id, "size");
        }

        public static String getKeyType(String id) {
            return Values.getString(id, "keytype");
        }

        public static String getAttribute(String id, String attr, String deafult) {
            String ret = Values.getString(id, attr, true);
            if (Properties.PROPERTIES_DEFAULT_STRING.equals(ret) || "".equals(ret)) {
                return deafult;
            }
            return ret;
        }
    }

    public static class Values {
        public static final String OverTheHorizonPath = "/system/hidden/INTERNAL_SDCARD/Samsung/Music/Over_the_Horizon.mp3";

        /* access modifiers changed from: private */
        public static boolean getBoolean(String id, String resultField) {
            return getBoolean(id, resultField, false);
        }

        /* access modifiers changed from: private */
        public static boolean getBoolean(String id, String resultField, int logLevel) {
            return getBoolean(id, resultField, false, logLevel);
        }

        /* access modifiers changed from: private */
        public static boolean getBoolean(String id, String resultField, boolean defaultValue) {
            boolean value = defaultValue;
            try {
                value = Boolean.parseBoolean(XMLDataStorage.instance().getAttributeValueById(id, resultField));
                StringBuilder sb = new StringBuilder();
                sb.append("id=");
                sb.append(id);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Values.getBoolean", sb.toString());
                return value;
            } catch (Exception e) {
                XmlUtil.log_e(e);
                return value;
            }
        }

        private static boolean getBoolean(String id, String resultField, boolean defaultValue, int logLevel) {
            boolean value = defaultValue;
            try {
                value = Boolean.parseBoolean(XMLDataStorage.instance().getAttributeValueById(id, resultField));
                if (logLevel > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("id=");
                    sb.append(id);
                    sb.append(", value=");
                    sb.append(value);
                    XmlUtil.log_d(Support.CLASS_NAME, "Values.getBoolean", sb.toString());
                }
            } catch (Exception e) {
                XmlUtil.log_e(e);
            }
            return value;
        }

        /* access modifiers changed from: private */
        public static byte getByte(String id, String resultField) {
            byte value = 0;
            try {
                value = Byte.parseByte(XMLDataStorage.instance().getAttributeValueById(id, resultField));
                StringBuilder sb = new StringBuilder();
                sb.append("id=");
                sb.append(id);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Values.getByte", sb.toString());
                return value;
            } catch (Exception e) {
                XmlUtil.log_e(e);
                return value;
            }
        }

        /* access modifiers changed from: private */
        public static String getString(String id, String resultField, boolean isLog) {
            String value = Properties.PROPERTIES_DEFAULT_STRING;
            try {
                value = XMLDataStorage.instance().getAttributeValueById(id, resultField);
                if (isLog) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("id=");
                    sb.append(id);
                    sb.append(", ");
                    sb.append(resultField);
                    sb.append("=");
                    sb.append(value);
                    XmlUtil.log_d(Support.CLASS_NAME, "Values.getString", sb.toString());
                }
            } catch (Exception e) {
                XmlUtil.log_e(e);
            }
            if (value == null) {
                return Properties.PROPERTIES_DEFAULT_STRING;
            }
            return value;
        }

        /* access modifiers changed from: private */
        public static String getString(String id, String resultField) {
            return getString(id, resultField, true);
        }

        /* access modifiers changed from: private */
        public static String getString(String id, String resultField, int logLevel) {
            String value = Properties.PROPERTIES_DEFAULT_STRING;
            try {
                value = XMLDataStorage.instance().getAttributeValueById(id, resultField);
                if (logLevel > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("id=");
                    sb.append(id);
                    sb.append(", value=");
                    sb.append(value);
                    XmlUtil.log_d(Support.CLASS_NAME, "Values.getString", sb.toString());
                }
            } catch (Exception e) {
                XmlUtil.log_e(e);
            }
            if (value == null) {
                return Properties.PROPERTIES_DEFAULT_STRING;
            }
            return value;
        }

        /* access modifiers changed from: private */
        public static int getInt(String id, String resultField) {
            int value = 0;
            try {
                value = Integer.parseInt(XMLDataStorage.instance().getAttributeValueById(id, resultField));
                StringBuilder sb = new StringBuilder();
                sb.append("id=");
                sb.append(id);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Values.getInt", sb.toString());
                return value;
            } catch (Exception e) {
                XmlUtil.log_e(e);
                return value;
            }
        }

        /* access modifiers changed from: private */
        public static long getLong(String id, String resultField) {
            long value = 0;
            try {
                value = Long.parseLong(XMLDataStorage.instance().getAttributeValueById(id, resultField));
                StringBuilder sb = new StringBuilder();
                sb.append("id=");
                sb.append(id);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Values.getFloat", sb.toString());
                return value;
            } catch (Exception e) {
                XmlUtil.log_e(e);
                return value;
            }
        }

        /* access modifiers changed from: private */
        public static float getFloat(String id, String resultField) {
            float value = 0.0f;
            try {
                value = Float.parseFloat(XMLDataStorage.instance().getAttributeValueById(id, resultField));
                StringBuilder sb = new StringBuilder();
                sb.append("id=");
                sb.append(id);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Values.getFloat", sb.toString());
                return value;
            } catch (Exception e) {
                XmlUtil.log_e(e);
                return value;
            }
        }

        /* access modifiers changed from: private */
        public static double getDouble(String id, String resultField) {
            double value = 0.0d;
            try {
                value = Double.parseDouble(XMLDataStorage.instance().getAttributeValueById(id, resultField));
                StringBuilder sb = new StringBuilder();
                sb.append("id=");
                sb.append(id);
                sb.append(", value=");
                sb.append(value);
                XmlUtil.log_d(Support.CLASS_NAME, "Values.getDouble", sb.toString());
                return value;
            } catch (Exception e) {
                XmlUtil.log_e(e);
                return value;
            }
        }
    }

    public static class Version {
        public static final String FACTORY_TEST_APP = "FACTORY_TEST_APP";
        public static final String FACTORY_TEST_COMMAND = "FACTORY_TEST_COMMAND";
        public static final String TAG = "Version";
        public static final String XML_DOCUMENT = "XML_DOCUMENT";

        public static String getString(String id) {
            return Values.getString(id, "value");
        }
    }
}
