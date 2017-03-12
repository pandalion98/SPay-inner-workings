package android.app.wallpaperbackup;

import android.content.Context;
import android.net.ProxyInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class WallpaperXmlParser extends DefaultHandler {
    private ArrayList<WallpaperUserPOJO> itemsList = new ArrayList();
    private Boolean mCurrentElement = Boolean.valueOf(false);
    private String mCurrentValue = ProxyInfo.LOCAL_EXCL_LIST;
    private WallpaperUserPOJO mItem = null;

    public WallpaperXmlParser(Context context, String fileName) throws ParserConfigurationException, SAXException, IOException {
        XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        xmlReader.setContentHandler(this);
        FileInputStream fis = new FileInputStream(new File(fileName));
        try {
            xmlReader.parse(new InputSource(new InputStreamReader(fis)));
        } finally {
            fis.close();
        }
    }

    public ArrayList<WallpaperUserPOJO> getItemsList() {
        return this.itemsList;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.mCurrentElement = Boolean.valueOf(true);
        this.mCurrentValue = ProxyInfo.LOCAL_EXCL_LIST;
        if (localName.equalsIgnoreCase(GenerateXMLForWallpaperUser.OBJECT_LIST_TAG)) {
            this.mItem = new WallpaperUserPOJO();
            this.itemsList.add(this.mItem);
        }
    }

    public WallpaperUserPOJO getObject() {
        return this.mItem;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.mCurrentElement = Boolean.valueOf(false);
        if (localName.equalsIgnoreCase("width")) {
            this.mItem.setWidth(Integer.parseInt(this.mCurrentValue));
        } else if (localName.equalsIgnoreCase("height")) {
            this.mItem.setHeight(Integer.parseInt(this.mCurrentValue));
        } else if (localName.equalsIgnoreCase("name")) {
            this.mItem.setName(this.mCurrentValue);
        } else if (localName.equalsIgnoreCase("component")) {
            this.mItem.setComponent(this.mCurrentValue);
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if (this.mCurrentElement.booleanValue()) {
            this.mCurrentValue += new String(ch, start, length);
        }
    }
}
