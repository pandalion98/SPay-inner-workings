package android.graphics;

import android.util.Xml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class FontListParser {

    public static class Alias {
        public String name;
        public String toName;
        public int weight;
    }

    public static class Config {
        public List<Alias> aliases = new ArrayList();
        public List<Family> families = new ArrayList();

        Config() {
        }
    }

    public static class Family {
        public List<Font> fonts;
        public String lang;
        public String name;
        public String variant;

        public Family(String name, List<Font> fonts, String lang, String variant) {
            this.name = name;
            this.fonts = fonts;
            this.lang = lang;
            this.variant = variant;
        }
    }

    public static class Font {
        public String fontName;
        public boolean isItalic;
        public int weight;

        Font(String fontName, int weight, boolean isItalic) {
            this.fontName = fontName;
            this.weight = weight;
            this.isItalic = isItalic;
        }
    }

    public static Config parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, null);
            parser.nextTag();
            Config readFamilies = readFamilies(parser);
            return readFamilies;
        } finally {
            in.close();
        }
    }

    private static Config readFamilies(XmlPullParser parser) throws XmlPullParserException, IOException {
        Config config = new Config();
        parser.require(2, null, "familyset");
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                if (parser.getName().equals("family")) {
                    config.families.add(readFamily(parser));
                } else if (parser.getName().equals("alias")) {
                    config.aliases.add(readAlias(parser));
                } else {
                    skip(parser);
                }
            }
        }
        return config;
    }

    private static Family readFamily(XmlPullParser parser) throws XmlPullParserException, IOException {
        String name = parser.getAttributeValue(null, "name");
        String lang = parser.getAttributeValue(null, "lang");
        String variant = parser.getAttributeValue(null, "variant");
        List<Font> fonts = new ArrayList();
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                if (parser.getName().equals("font")) {
                    String weightStr = parser.getAttributeValue(null, "weight");
                    fonts.add(new Font("/system/fonts/" + parser.nextText(), weightStr == null ? 400 : Integer.parseInt(weightStr), "italic".equals(parser.getAttributeValue(null, TtmlUtils.TAG_STYLE))));
                } else {
                    skip(parser);
                }
            }
        }
        return new Family(name, fonts, lang, variant);
    }

    private static Alias readAlias(XmlPullParser parser) throws XmlPullParserException, IOException {
        Alias alias = new Alias();
        alias.name = parser.getAttributeValue(null, "name");
        alias.toName = parser.getAttributeValue(null, "to");
        String weightStr = parser.getAttributeValue(null, "weight");
        if (weightStr == null) {
            alias.weight = 400;
        } else {
            alias.weight = Integer.parseInt(weightStr);
        }
        skip(parser);
        return alias;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        int depth = 1;
        while (depth > 0) {
            switch (parser.next()) {
                case 2:
                    depth++;
                    break;
                case 3:
                    depth--;
                    break;
                default:
                    break;
            }
        }
    }
}
