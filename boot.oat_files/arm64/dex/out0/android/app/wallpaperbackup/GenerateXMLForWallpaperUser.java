package android.app.wallpaperbackup;

import android.net.ProxyInfo;
import android.util.Log;
import android.util.Xml;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlSerializer;

public class GenerateXMLForWallpaperUser {
    public static final String COMPONENT = "component";
    public static final String HEIGHT = "height";
    public static final String NAME = "name";
    public static final String OBJECT_LIST_TAG = "User";
    private static final String TAG = "GenerateXMLForResult";
    private static final String TOP_TAG = "Wallpapers";
    public static final String WALLPAPER_XML_NAME = "wallpaper.xml";
    public static final String WIDTH = "width";
    ArrayList<WallpaperUserPOJO> userList;

    public GenerateXMLForWallpaperUser(ArrayList<WallpaperUserPOJO> userList, String path) throws IOException {
        this.userList = userList;
        createResultFile(path);
    }

    public void generateResultXML(File appsfile) throws IOException {
        XmlSerializer serializer = Xml.newSerializer();
        FileWriter writer = new FileWriter(appsfile);
        serializer.setOutput(writer);
        serializer.startDocument("UTF-8", Boolean.valueOf(true));
        serializer.startTag(ProxyInfo.LOCAL_EXCL_LIST, TOP_TAG);
        Iterator i$ = this.userList.iterator();
        while (i$.hasNext()) {
            WallpaperUserPOJO object = (WallpaperUserPOJO) i$.next();
            serializer.startTag(ProxyInfo.LOCAL_EXCL_LIST, OBJECT_LIST_TAG);
            serializer.attribute(ProxyInfo.LOCAL_EXCL_LIST, "ID", String.valueOf(0));
            serializer.startTag(ProxyInfo.LOCAL_EXCL_LIST, "width");
            serializer.text(Integer.toString(object.getWidth()));
            serializer.endTag(ProxyInfo.LOCAL_EXCL_LIST, "width");
            serializer.startTag(ProxyInfo.LOCAL_EXCL_LIST, "height");
            serializer.text(Integer.toString(object.getHeight()));
            serializer.endTag(ProxyInfo.LOCAL_EXCL_LIST, "height");
            serializer.startTag(ProxyInfo.LOCAL_EXCL_LIST, "name");
            serializer.text(object.getName());
            serializer.endTag(ProxyInfo.LOCAL_EXCL_LIST, "name");
            serializer.startTag(ProxyInfo.LOCAL_EXCL_LIST, "component");
            serializer.text(object.getComponent());
            serializer.endTag(ProxyInfo.LOCAL_EXCL_LIST, "component");
            serializer.endTag(ProxyInfo.LOCAL_EXCL_LIST, OBJECT_LIST_TAG);
        }
        serializer.endTag(ProxyInfo.LOCAL_EXCL_LIST, TOP_TAG);
        serializer.endDocument();
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void createResultFile(String strPath) throws IOException {
        Log.i(TAG, "strPath: " + strPath);
        File xmlDirLast = new File(strPath);
        if (!xmlDirLast.exists()) {
            Log.i(TAG, xmlDirLast.mkdir() + "folder created last");
        }
        File appsfile = new File(xmlDirLast.getPath() + "/" + WALLPAPER_XML_NAME);
        if (appsfile.exists()) {
            appsfile.delete();
        }
        appsfile.createNewFile();
        generateResultXML(appsfile);
    }
}
