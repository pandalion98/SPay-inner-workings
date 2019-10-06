package com.sec.xmldata.support;

import android.content.Context;
import android.os.FactoryTest;
import android.os.SystemProperties;
import android.util.Base64;
import android.widget.Toast;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.HwTestMenu;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.SensorTestMenu;
import egis.client.api.EgisFingerprint;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLDataStorage {
    public static final String CLASS_NAME = "XMLDataStorage";
    private static XMLDataStorage mInstance = null;
    private ConcurrentHashMap<String, String> mAttrCache = new ConcurrentHashMap<>();
    private Context mContext;
    private DocumentBuilder mDOMParser;
    private Document mDocument;
    private boolean mForceLDU = false;
    private boolean mInterposer = false;
    private boolean mWasCompletedParsing = false;
    private XPath mXPath;

    public static class ElementIdMismatchException extends RuntimeException {
        private static final long serialVersionUID = 5195850516633538384L;

        public ElementIdMismatchException() {
        }

        public ElementIdMismatchException(String detailMessage) {
            super(detailMessage);
        }
    }

    public static synchronized XMLDataStorage instance() {
        XMLDataStorage xMLDataStorage;
        synchronized (XMLDataStorage.class) {
            if (mInstance == null) {
                mInstance = new XMLDataStorage();
            }
            xMLDataStorage = mInstance;
        }
        return xMLDataStorage;
    }

    private XMLDataStorage() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            this.mDOMParser = factory.newDocumentBuilder();
            this.mXPath = XPathFactory.newInstance().newXPath();
        } catch (ParserConfigurationException e) {
            XmlUtil.log_e(e);
        }
    }

    public boolean parseXML(Context context) {
        String packageName = context.getPackageName();
        this.mContext = context;
        if (packageName.equals("com.sec.factory")) {
            XmlUtil.TAG = "FactoryTestApp";
        } else if (packageName.equals("com.sec.android.app.factorykeystring")) {
            XmlUtil.TAG = "Lcdtest";
        } else if (packageName.equals("com.sec.android.app.hwmoduletest")) {
            XmlUtil.TAG = "HwModuleTest";
        } else if (packageName.equals("com.sec.OneJigBinary")) {
            XmlUtil.TAG = "OneJigBinary";
        } else {
            XmlUtil.log_i(CLASS_NAME, "parsingXML", "UnKnown PackageName");
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("packageName=");
        sb.append(XmlUtil.TAG);
        XmlUtil.log_d(CLASS_NAME, "parseXML", sb.toString());
        this.mForceLDU = Properties.getProp("persist.sys.cpboot").equalsIgnoreCase("disable");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mIsLduModel=");
        sb2.append(this.mForceLDU);
        XmlUtil.log_d(CLASS_NAME, "parseXML", sb2.toString());
        this.mInterposer = Properties.getProp("ro.build.interposer").equalsIgnoreCase(EgisFingerprint.MAJOR_VERSION);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Interposer binary =");
        sb3.append(this.mInterposer);
        XmlUtil.log_d(CLASS_NAME, "parseXML", sb3.toString());
        synchronized (XMLDataStorage.class) {
            if (!instance().wasCompletedParsing()) {
                String str = "base";
                String modelXML = SystemProperties.get("ro.product.model", "base");
                String nameXML = SystemProperties.get("ro.product.name", "base");
                if (modelXML.equalsIgnoreCase("sm-g9308")) {
                    modelXML = "sm-g9300";
                    XmlUtil.log_i(CLASS_NAME, "parsingXML", "sm-g9308 using sm-g9300.xml");
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append(modelXML.trim().replace(" ", "").toLowerCase(Locale.ENGLISH));
                sb4.append(".dat");
                String modelXML2 = sb4.toString();
                StringBuilder sb5 = new StringBuilder();
                sb5.append(nameXML.trim().replace(" ", "").toLowerCase(Locale.ENGLISH));
                sb5.append(".dat");
                String nameXML2 = sb5.toString();
                StringBuilder sb6 = new StringBuilder();
                sb6.append("modelXML=");
                sb6.append(modelXML2);
                XmlUtil.log_d(CLASS_NAME, "parseXML", sb6.toString());
                StringBuilder sb7 = new StringBuilder();
                sb7.append("nameXML=");
                sb7.append(nameXML2);
                XmlUtil.log_d(CLASS_NAME, "parseXML", sb7.toString());
                if (!instance().parseAsset(context, modelXML2)) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("ro.product.model file not found : ");
                    sb8.append(modelXML2);
                    XmlUtil.log_e(CLASS_NAME, "parseXML", sb8.toString());
                    if (!instance().parseAsset(context, nameXML2)) {
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("ro.product.name file not found : ");
                        sb9.append(nameXML2);
                        XmlUtil.log_e(CLASS_NAME, "parseXML", sb9.toString());
                        instance().parseAsset(context, "base.dat");
                        XmlUtil.log_e(CLASS_NAME, "parseXML", "Default file loaded => base.dat");
                    }
                }
                XmlUtil.log_i(CLASS_NAME, "parsingXML", "data parsing completed.");
            } else {
                XmlUtil.log_i(CLASS_NAME, "parsingXML", "FtClient => data parsing was completed.");
            }
        }
        return true;
    }

    private boolean parseAsset(Context context, String xml) {
        try {
            XmlUtil.log_d(CLASS_NAME, "parseAsset", "parseAsset Is Started");
            byte[] dataBytes = Base64.decode(convertIStoString(context, xml), 0);
            StringBuilder sb = new StringBuilder();
            sb.append("Convert dat file: ");
            sb.append(xml);
            XmlUtil.log_i(CLASS_NAME, "parseAsset", sb.toString());
            Document document = this.mDOMParser.parse(convertBytesToIS(dataBytes));
            String baseXml = "base.dat";
            String str = "";
            if (getBaseDocument(document) != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Decode base file: ");
                sb2.append(baseXml);
                XmlUtil.log_d(CLASS_NAME, "parseAsset", sb2.toString());
                Document baseDocument = this.mDOMParser.parse(convertBytesToIS(Base64.decode(convertIStoString(context, baseXml), 0)));
                Document subDocument = document;
                if (getReferenceDocument(document) != null) {
                    String referXml = getReferenceDocument(document);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Decode reference file: ");
                    sb3.append(referXml);
                    XmlUtil.log_d(CLASS_NAME, "parseAsset", sb3.toString());
                    Document tempDocument = redefinitionXml(baseDocument, this.mDOMParser.parse(convertBytesToIS(Base64.decode(convertIStoString(context, referXml), 0))));
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("completed redefinitionXml1: ");
                    sb4.append(baseXml);
                    sb4.append("<- ");
                    sb4.append(referXml);
                    XmlUtil.log_d(CLASS_NAME, "parseAsset", sb4.toString());
                    this.mDocument = redefinitionXml(tempDocument, subDocument);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("completed redefinitionXml2: ");
                    sb5.append(baseXml);
                    sb5.append("<- ");
                    sb5.append(xml);
                    XmlUtil.log_d(CLASS_NAME, "parseAsset", sb5.toString());
                } else {
                    this.mDocument = redefinitionXml(baseDocument, subDocument);
                }
            } else {
                this.mDocument = document;
            }
            if (FactoryTest.isFactoryBinary()) {
                replaceLineSpec();
            }
            XmlUtil.log_d(CLASS_NAME, "parseAsset", "parseAsset Is Completed");
            this.mWasCompletedParsing = true;
        } catch (Exception e) {
            this.mWasCompletedParsing = false;
            XmlUtil.log_e(e);
        }
        return this.mWasCompletedParsing;
    }

    private Document redefinitionXml(Document base, Document sub) {
        redefinitionById(base, sub);
        if (sub.getElementsByTagName("FactoryTestMenu").item(0) != null) {
            XmlUtil.log_d(CLASS_NAME, "parseAsset", "FactoryTestMenu");
            swapNode(base, base.getElementsByTagName("FactoryTestMenu").item(0), sub.getElementsByTagName("FactoryTestMenu").item(0));
        }
        if (sub.getElementsByTagName("FactoryTestMenuSub").item(0) != null) {
            XmlUtil.log_d(CLASS_NAME, "parseAsset", "FactoryTestMenuSub");
            swapNode(base, base.getElementsByTagName("FactoryTestMenuSub").item(0), sub.getElementsByTagName("FactoryTestMenuSub").item(0));
        }
        if (sub.getElementsByTagName("SemiFunctionMenu").item(0) != null) {
            XmlUtil.log_d(CLASS_NAME, "parseAsset", "SemiFunctionMenu");
            swapNode(base, base.getElementsByTagName("SemiFunctionMenu").item(0), sub.getElementsByTagName("SemiFunctionMenu").item(0));
        }
        if (sub.getElementsByTagName("CKDTestMenu").item(0) != null) {
            XmlUtil.log_d(CLASS_NAME, "parseAsset", "CKDTestMenu");
            swapNode(base, base.getElementsByTagName("CKDTestMenu").item(0), sub.getElementsByTagName("CKDTestMenu").item(0));
        }
        if (sub.getElementsByTagName(HwTestMenu.TAG).item(0) != null) {
            XmlUtil.log_d(CLASS_NAME, "parseAsset", HwTestMenu.TAG);
            swapNode(base, base.getElementsByTagName(HwTestMenu.TAG).item(0), sub.getElementsByTagName(HwTestMenu.TAG).item(0));
        }
        if (sub.getElementsByTagName(SensorTestMenu.TAG).item(0) != null) {
            XmlUtil.log_d(CLASS_NAME, "parseAsset", SensorTestMenu.TAG);
            swapNode(base, base.getElementsByTagName(SensorTestMenu.TAG).item(0), sub.getElementsByTagName(SensorTestMenu.TAG).item(0));
        }
        return base;
    }

    private String convertIStoString(Context context, String xmlFile) {
        InputStream inputstream;
        StringBuilder sb = new StringBuilder();
        sb.append("/sdcard/");
        sb.append(xmlFile);
        String mModelFileInSdcard = sb.toString();
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.setLength(0);
        boolean inputstream2 = null;
        try {
            File file = new File(mModelFileInSdcard);
            inputstream2 = FactoryTest.isFactoryBinary();
            if (!inputstream2 || !file.exists()) {
                inputstream = context.getResources().getAssets().open(xmlFile, 3);
            } else {
                inputstream = new FileInputStream(file);
                Toast.makeText(context, "Read model file from sdcard", 1).show();
            }
            BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputstream));
            String str = "";
            while (true) {
                String readLine = buffreader.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                stringbuilder.append(line);
            }
            if (inputstream != null) {
                try {
                    inputstream.close();
                } catch (IOException e) {
                    XmlUtil.log_e(e);
                }
            }
        } catch (IOException e2) {
            XmlUtil.log_e(e2);
            if (inputstream2 != null) {
                inputstream2.close();
            }
        } finally {
            if (inputstream2 != null) {
                try {
                    inputstream2.close();
                } catch (IOException e3) {
                    XmlUtil.log_e(e3);
                }
            }
        }
        return stringbuilder.toString();
    }

    private InputStream convertBytesToIS(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    public boolean wasCompletedParsing() {
        return this.mWasCompletedParsing;
    }

    public Element getElementById(String id) {
        return this.mDocument.getElementById(id);
    }

    public Element getElementByName(String nodeName) {
        StringBuilder sb = new StringBuilder();
        sb.append("//");
        sb.append(nodeName);
        return (Element) xpath(sb.toString(), XPathConstants.NODE);
    }

    public Element getElementByAttribute(String attribute, String attributeValue) {
        StringBuilder sb = new StringBuilder();
        sb.append("//*[@");
        sb.append(attribute);
        sb.append("='");
        sb.append(attributeValue);
        sb.append("']");
        return (Element) xpath(sb.toString(), XPathConstants.NODE);
    }

    public Element[] getElementSetByName(String nodeName) {
        StringBuilder sb = new StringBuilder();
        sb.append("//");
        sb.append(nodeName);
        NodeList nodeList = (NodeList) xpath(sb.toString(), XPathConstants.NODESET);
        if (nodeList != null) {
            return makeElementArray(nodeList);
        }
        return null;
    }

    public Element[] getChildElementSet(String parentNodeName) {
        StringBuilder sb = new StringBuilder();
        sb.append("//");
        sb.append(parentNodeName);
        sb.append("/*");
        NodeList nodeList = (NodeList) xpath(sb.toString(), XPathConstants.NODESET);
        if (nodeList != null) {
            return makeElementArray(nodeList);
        }
        return null;
    }

    public String getAttributeValueById(String id, String resultAttr) {
        String str = "$";
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("$");
        sb.append(resultAttr);
        String cacheId = sb.toString();
        String attr = (String) this.mAttrCache.get(cacheId);
        if (attr != null) {
            return attr;
        }
        String value = this.mDocument.getElementById(id).getAttribute(resultAttr);
        this.mAttrCache.putIfAbsent(cacheId, value);
        return value;
    }

    public String getAttributeValueByTag(String tag, String resultAttr) {
        return ((Element) this.mDocument.getElementsByTagName(tag).item(0)).getAttribute(resultAttr);
    }

    public String getAttributeValueByAttribute(String finaAttr, String findAttrValue, String resultAttr) {
        Element element = getElementByAttribute(finaAttr, findAttrValue);
        if (element != null) {
            return element.getAttribute(resultAttr);
        }
        return null;
    }

    public String[] getAttributeNameSet(Element element) {
        String[] attributeSet = null;
        if (element.hasAttributes()) {
            NamedNodeMap map = element.getAttributes();
            attributeSet = new String[map.getLength()];
            for (int i = 0; i < map.getLength(); i++) {
                attributeSet[i] = map.item(i).getNodeName();
            }
        }
        return attributeSet;
    }

    public Document getDocument() {
        return this.mDocument;
    }

    private String getReferenceDocument(Document document) {
        Element element = (Element) xpath(document, "/Factory/ReferenceDocument", XPathConstants.NODE);
        String str = "";
        String str2 = "";
        String datDocument = element != null ? element.getAttribute("document") : null;
        if (datDocument == null) {
            return datDocument;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(datDocument.substring(0, datDocument.indexOf(".")));
        sb.append(".dat");
        return sb.toString();
    }

    private String getBaseDocument(Document document) {
        return "base.xml";
    }

    private Object xpath(String expression, QName returnType) {
        return xpath(this.mDocument, expression, returnType);
    }

    private Object xpath(Document document, String expression, QName returnType) {
        try {
            return this.mXPath.compile(expression).evaluate(document, returnType);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Element[] makeElementArray(NodeList nodeList) {
        Element[] elementSet = new Element[nodeList.getLength()];
        for (int i = 0; i < nodeList.getLength(); i++) {
            elementSet[i] = (Element) nodeList.item(i);
        }
        return elementSet;
    }

    private void redefinitionById(Document baseDocument, Document subDocument) {
        redefinitionById(baseDocument, subDocument.getDocumentElement());
    }

    private void redefinitionById(Document baseDocument, Element redefNode) {
        if (redefNode.hasAttributes()) {
            String id = redefNode.getAttribute("id");
            if (id != null && !id.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("id=");
                sb.append(id);
                XmlUtil.log_d(CLASS_NAME, "redefinitionById", sb.toString());
                String[] attributes = getAttributeNameSet(redefNode);
                if (attributes != null) {
                    for (String attr : attributes) {
                        if (!attr.equals("id")) {
                            Element element = baseDocument.getElementById(id);
                            if (element == null) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Element \"");
                                sb2.append(id);
                                sb2.append("\" does not exist in base document.");
                                XmlUtil.log_d(CLASS_NAME, "redefinitionById", sb2.toString());
                            } else if (id.equalsIgnoreCase(Feature.MODEL_COMMUNICATION_MODE) && (this.mForceLDU || this.mInterposer)) {
                                XmlUtil.log_d(CLASS_NAME, "redefinitionById", "force change MODEL_COMMUNICATION_MODE=none");
                                element.setAttribute(attr, "none");
                            } else if (!id.equalsIgnoreCase(Feature.CHIPSET_NAME_CP) || !this.mForceLDU) {
                                element.setAttribute(attr, redefNode.getAttribute(attr));
                            } else {
                                XmlUtil.log_d(CLASS_NAME, "redefinitionById", "force change CHIPSET_NAME_CP=#DEFAULT for LDU model");
                                element.setAttribute(attr, "#DEFAULT");
                            }
                        }
                    }
                }
            }
        }
        if (redefNode.hasChildNodes()) {
            NodeList childNodes = redefNode.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeType() == 1) {
                    redefinitionById(baseDocument, (Element) childNodes.item(i));
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:95:0x020a A[SYNTHETIC, Splitter:B:95:0x020a] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean replaceLineSpec() {
        /*
            r21 = this;
            r1 = r21
            java.lang.String r2 = "/efs/FactoryApp/line_spec"
            r3 = 0
            r4 = 0
            r5 = 0
            r6 = r5
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0031, all -> 0x002b }
            java.io.FileReader r7 = new java.io.FileReader     // Catch:{ Exception -> 0x0031, all -> 0x002b }
            r7.<init>(r2)     // Catch:{ Exception -> 0x0031, all -> 0x002b }
            r0.<init>(r7)     // Catch:{ Exception -> 0x0031, all -> 0x002b }
            r4 = r0
            java.lang.String r0 = r4.readLine()     // Catch:{ Exception -> 0x0031, all -> 0x002b }
            r3 = r0
            if (r3 == 0) goto L_0x0020
            java.lang.String r0 = r3.trim()     // Catch:{ Exception -> 0x0031, all -> 0x002b }
            r3 = r0
        L_0x0020:
            r4.close()     // Catch:{ IOException -> 0x0025 }
        L_0x0024:
            goto L_0x003b
        L_0x0025:
            r0 = move-exception
            r7 = r0
            com.sec.xmldata.support.XmlUtil.log_e(r0)
            goto L_0x0024
        L_0x002b:
            r0 = move-exception
            r17 = r2
            r2 = r0
            goto L_0x0208
        L_0x0031:
            r0 = move-exception
            com.sec.xmldata.support.XmlUtil.log_e(r0)     // Catch:{ all -> 0x0204 }
            if (r4 == 0) goto L_0x003b
            r4.close()     // Catch:{ IOException -> 0x0025 }
            goto L_0x0024
        L_0x003b:
            if (r3 == 0) goto L_0x01fd
            boolean r0 = r3.isEmpty()
            if (r0 != 0) goto L_0x01fd
            java.lang.String r0 = ":"
            java.lang.String[] r0 = r3.split(r0)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            if (r0 == 0) goto L_0x01d1
            int r7 = r0.length     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            if (r7 == 0) goto L_0x01d1
            int r7 = r0.length     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r8 = r5
        L_0x0050:
            if (r8 >= r7) goto L_0x01d1
            r9 = r0[r8]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r10 = "//"
            java.lang.String[] r10 = r9.split(r10)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r11 = r10[r5]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            if (r11 == 0) goto L_0x01bb
            r11 = 1
            r12 = r10[r11]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            if (r12 == 0) goto L_0x01bb
            r12 = 2
            r13 = r10[r12]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            if (r13 == 0) goto L_0x01bb
            java.lang.String r13 = "XMLDataStorage"
            java.lang.String r14 = "replaceLineSpec"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r15.<init>()     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r12 = "spec : "
            r15.append(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r12 = r10[r5]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r15.append(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r12 = " // "
            r15.append(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r12 = r10[r11]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r15.append(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r12 = " // "
            r15.append(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r12 = 2
            r5 = r10[r12]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r15.append(r5)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r5 = r15.toString()     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            com.sec.xmldata.support.XmlUtil.log_d(r13, r14, r5)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            org.w3c.dom.Document r5 = r1.mDocument     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r12 = r10[r11]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            org.w3c.dom.Element r5 = r5.getElementById(r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            if (r5 != 0) goto L_0x00de
            r12 = 0
            r13 = r10[r12]     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            org.w3c.dom.Element[] r12 = r1.getChildElementSet(r13)     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            r13 = r5
            r5 = 0
        L_0x00aa:
            int r14 = r12.length     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            if (r5 >= r14) goto L_0x00df
            r14 = r10[r11]     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            r15 = r12[r5]     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            org.w3c.dom.NamedNodeMap r15 = r15.getAttributes()     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            java.lang.String r11 = "name"
            org.w3c.dom.Node r11 = r15.getNamedItem(r11)     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            java.lang.String r11 = r11.getNodeValue()     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            boolean r11 = r14.equals(r11)     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            if (r11 == 0) goto L_0x00c8
            r11 = r12[r5]     // Catch:{ Exception -> 0x00d5, all -> 0x00cc }
            r13 = r11
        L_0x00c8:
            int r5 = r5 + 1
            r11 = 1
            goto L_0x00aa
        L_0x00cc:
            r0 = move-exception
            r17 = r2
            r18 = r3
            r19 = r4
            goto L_0x01fc
        L_0x00d5:
            r0 = move-exception
            r17 = r2
            r18 = r3
            r19 = r4
            goto L_0x01ec
        L_0x00de:
            r13 = r5
        L_0x00df:
            if (r13 == 0) goto L_0x0191
            java.lang.String r5 = "XMLDataStorage"
            java.lang.String r11 = "replaceLineSpec"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r12.<init>()     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r14 = "specItem[2] ="
            r12.append(r14)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r14 = 2
            r15 = r10[r14]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r12.append(r15)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r12 = r12.toString()     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            com.sec.xmldata.support.XmlUtil.log_d(r5, r11, r12)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r5 = 2
            r5 = r10[r5]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r11 = "__"
            java.lang.String[] r5 = r5.split(r11)     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            int r11 = r5.length     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            r12 = 0
        L_0x0107:
            if (r12 >= r11) goto L_0x0187
            r14 = r5[r12]     // Catch:{ Exception -> 0x01e5, all -> 0x01dd }
            java.lang.String r15 = "XMLDataStorage"
            r16 = r0
            java.lang.String r0 = "replaceLineSpec"
            r17 = r2
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0180, all -> 0x0179 }
            r2.<init>()     // Catch:{ Exception -> 0x0180, all -> 0x0179 }
            r18 = r3
            java.lang.String r3 = "attr = "
            r2.append(r3)     // Catch:{ Exception -> 0x0174, all -> 0x016f }
            r2.append(r14)     // Catch:{ Exception -> 0x0174, all -> 0x016f }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x0174, all -> 0x016f }
            com.sec.xmldata.support.XmlUtil.log_d(r15, r0, r2)     // Catch:{ Exception -> 0x0174, all -> 0x016f }
            java.lang.String r0 = "="
            java.lang.String[] r0 = r14.split(r0)     // Catch:{ Exception -> 0x0174, all -> 0x016f }
            java.lang.String r2 = "XMLDataStorage"
            java.lang.String r3 = "replaceLineSpec"
            java.lang.StringBuilder r15 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0174, all -> 0x016f }
            r15.<init>()     // Catch:{ Exception -> 0x0174, all -> 0x016f }
            r19 = r4
            java.lang.String r4 = "attrItem[] = "
            r15.append(r4)     // Catch:{ Exception -> 0x01b9 }
            r20 = r5
            r4 = 0
            r5 = r0[r4]     // Catch:{ Exception -> 0x01b9 }
            r15.append(r5)     // Catch:{ Exception -> 0x01b9 }
            java.lang.String r4 = " = "
            r15.append(r4)     // Catch:{ Exception -> 0x01b9 }
            r4 = 1
            r5 = r0[r4]     // Catch:{ Exception -> 0x01b9 }
            r15.append(r5)     // Catch:{ Exception -> 0x01b9 }
            java.lang.String r4 = r15.toString()     // Catch:{ Exception -> 0x01b9 }
            com.sec.xmldata.support.XmlUtil.log_d(r2, r3, r4)     // Catch:{ Exception -> 0x01b9 }
            r2 = 0
            r3 = r0[r2]     // Catch:{ Exception -> 0x01b9 }
            r4 = 1
            r5 = r0[r4]     // Catch:{ Exception -> 0x01b9 }
            r13.setAttribute(r3, r5)     // Catch:{ Exception -> 0x01b9 }
            int r12 = r12 + 1
            r0 = r16
            r2 = r17
            r3 = r18
            r4 = r19
            r5 = r20
            goto L_0x0107
        L_0x016f:
            r0 = move-exception
            r19 = r4
            goto L_0x01fc
        L_0x0174:
            r0 = move-exception
            r19 = r4
            goto L_0x01ec
        L_0x0179:
            r0 = move-exception
            r18 = r3
            r19 = r4
            goto L_0x01fc
        L_0x0180:
            r0 = move-exception
            r18 = r3
            r19 = r4
            goto L_0x01ec
        L_0x0187:
            r16 = r0
            r17 = r2
            r18 = r3
            r19 = r4
            r2 = 0
            goto L_0x01c4
        L_0x0191:
            r16 = r0
            r17 = r2
            r18 = r3
            r19 = r4
            r2 = 0
            java.lang.String r0 = "XMLDataStorage"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01b9 }
            r3.<init>()     // Catch:{ Exception -> 0x01b9 }
            java.lang.String r4 = "Element \""
            r3.append(r4)     // Catch:{ Exception -> 0x01b9 }
            r4 = 1
            r4 = r10[r4]     // Catch:{ Exception -> 0x01b9 }
            r3.append(r4)     // Catch:{ Exception -> 0x01b9 }
            java.lang.String r4 = "\" does not exist in base document."
            r3.append(r4)     // Catch:{ Exception -> 0x01b9 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x01b9 }
            com.sec.xmldata.support.XmlUtil.log_d(r0, r3)     // Catch:{ Exception -> 0x01b9 }
            goto L_0x01c4
        L_0x01b9:
            r0 = move-exception
            goto L_0x01ec
        L_0x01bb:
            r16 = r0
            r17 = r2
            r18 = r3
            r19 = r4
            r2 = r5
        L_0x01c4:
            int r8 = r8 + 1
            r5 = r2
            r0 = r16
            r2 = r17
            r3 = r18
            r4 = r19
            goto L_0x0050
        L_0x01d1:
            r16 = r0
            r17 = r2
            r18 = r3
            r19 = r4
            r0 = 1
            r6 = r0
            goto L_0x01f5
        L_0x01dd:
            r0 = move-exception
            r17 = r2
            r18 = r3
            r19 = r4
            goto L_0x01fc
        L_0x01e5:
            r0 = move-exception
            r17 = r2
            r18 = r3
            r19 = r4
        L_0x01ec:
            com.sec.xmldata.support.XmlUtil.log_e(r0)     // Catch:{ all -> 0x01fb }
            java.lang.String r2 = "LINE_SPEC"
            com.sec.xmldata.support.Support.Kernel.deleteFile(r2)     // Catch:{ all -> 0x01fb }
        L_0x01f5:
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.String> r0 = r1.mAttrCache
            r0.clear()
            goto L_0x0203
        L_0x01fb:
            r0 = move-exception
        L_0x01fc:
            throw r0
        L_0x01fd:
            r17 = r2
            r18 = r3
            r19 = r4
        L_0x0203:
            return r6
        L_0x0204:
            r0 = move-exception
            r17 = r2
            r2 = r0
        L_0x0208:
            if (r4 == 0) goto L_0x0213
            r4.close()     // Catch:{ IOException -> 0x020e }
            goto L_0x0213
        L_0x020e:
            r0 = move-exception
            r5 = r0
            com.sec.xmldata.support.XmlUtil.log_e(r0)
        L_0x0213:
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.xmldata.support.XMLDataStorage.replaceLineSpec():boolean");
    }

    private void swapNode(Document targetDocument, Node baseNode, Node subNode) {
        baseNode.getParentNode().replaceChild(cloneNode(targetDocument, (Element) subNode), baseNode);
    }

    private Node cloneNode(Document document, Element cloneTarget) {
        Element newNode = document.createElement(cloneTarget.getNodeName());
        if (cloneTarget.hasAttributes()) {
            String[] attributes = getAttributeNameSet(cloneTarget);
            if (attributes != null) {
                for (String attr : attributes) {
                    newNode.setAttribute(attr, cloneTarget.getAttribute(attr));
                }
            }
        }
        NodeList list = cloneTarget.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeType() == 1) {
                newNode.appendChild(cloneNode(document, (Element) list.item(i)));
            }
        }
        return newNode;
    }
}
