package android.text;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.service.notification.ZenModeConfig;
import android.telephony.SubscriptionManager;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import com.android.internal.R;
import com.samsung.android.multiwindow.MultiWindowFacade;
import java.io.IOException;
import java.io.StringReader;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/* compiled from: Html */
class HtmlToSpannedConverter implements ContentHandler {
    private static final float[] HEADER_SIZES = new float[]{1.5f, 1.4f, 1.3f, 1.2f, 1.1f, 1.0f};
    private ImageGetter mImageGetter;
    private XMLReader mReader;
    private String mSource;
    private SpannableStringBuilder mSpannableStringBuilder = new SpannableStringBuilder();
    private TagHandler mTagHandler;

    /* compiled from: Html */
    private static class Big {
        private Big() {
        }
    }

    /* compiled from: Html */
    private static class Blockquote {
        private Blockquote() {
        }
    }

    /* compiled from: Html */
    private static class Bold {
        private Bold() {
        }
    }

    /* compiled from: Html */
    private static class Font {
        public String mColor;
        public String mFace;

        public Font(String color, String face) {
            this.mColor = color;
            this.mFace = face;
        }
    }

    /* compiled from: Html */
    private static class Header {
        private int mLevel;

        public Header(int level) {
            this.mLevel = level;
        }
    }

    /* compiled from: Html */
    private static class Href {
        public String mHref;

        public Href(String href) {
            this.mHref = href;
        }
    }

    /* compiled from: Html */
    private static class Italic {
        private Italic() {
        }
    }

    /* compiled from: Html */
    private static class Monospace {
        private Monospace() {
        }
    }

    /* compiled from: Html */
    private static class Small {
        private Small() {
        }
    }

    /* compiled from: Html */
    private static class Sub {
        private Sub() {
        }
    }

    /* compiled from: Html */
    private static class Super {
        private Super() {
        }
    }

    /* compiled from: Html */
    private static class Underline {
        private Underline() {
        }
    }

    public HtmlToSpannedConverter(String source, ImageGetter imageGetter, TagHandler tagHandler, Parser parser) {
        this.mSource = source;
        this.mImageGetter = imageGetter;
        this.mTagHandler = tagHandler;
        this.mReader = parser;
    }

    public Spanned convert() {
        this.mReader.setContentHandler(this);
        try {
            this.mReader.parse(new InputSource(new StringReader(this.mSource)));
            Object[] obj = this.mSpannableStringBuilder.getSpans(0, this.mSpannableStringBuilder.length(), ParagraphStyle.class);
            for (int i = 0; i < obj.length; i++) {
                int start = this.mSpannableStringBuilder.getSpanStart(obj[i]);
                int end = this.mSpannableStringBuilder.getSpanEnd(obj[i]);
                if (end - 2 >= 0 && this.mSpannableStringBuilder.charAt(end - 1) == '\n' && this.mSpannableStringBuilder.charAt(end - 2) == '\n') {
                    end--;
                }
                if (end == start) {
                    this.mSpannableStringBuilder.removeSpan(obj[i]);
                } else {
                    this.mSpannableStringBuilder.setSpan(obj[i], start, end, 51);
                }
            }
            return this.mSpannableStringBuilder;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e2) {
            throw new RuntimeException(e2);
        }
    }

    private void handleStartTag(String tag, Attributes attributes) {
        if (!tag.equalsIgnoreCase("br")) {
            if (tag.equalsIgnoreCase("p")) {
                handleP(this.mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("div")) {
                handleP(this.mSpannableStringBuilder);
            } else if (tag.equalsIgnoreCase("strong")) {
                start(this.mSpannableStringBuilder, new Bold());
            } else if (tag.equalsIgnoreCase("b")) {
                start(this.mSpannableStringBuilder, new Bold());
            } else if (tag.equalsIgnoreCase("em")) {
                start(this.mSpannableStringBuilder, new Italic());
            } else if (tag.equalsIgnoreCase("cite")) {
                start(this.mSpannableStringBuilder, new Italic());
            } else if (tag.equalsIgnoreCase("dfn")) {
                start(this.mSpannableStringBuilder, new Italic());
            } else if (tag.equalsIgnoreCase("i")) {
                start(this.mSpannableStringBuilder, new Italic());
            } else if (tag.equalsIgnoreCase("big")) {
                start(this.mSpannableStringBuilder, new Big());
            } else if (tag.equalsIgnoreCase("small")) {
                start(this.mSpannableStringBuilder, new Small());
            } else if (tag.equalsIgnoreCase("font")) {
                startFont(this.mSpannableStringBuilder, attributes);
            } else if (tag.equalsIgnoreCase("blockquote")) {
                handleP(this.mSpannableStringBuilder);
                start(this.mSpannableStringBuilder, new Blockquote());
            } else if (tag.equalsIgnoreCase("tt")) {
                start(this.mSpannableStringBuilder, new Monospace());
            } else if (tag.equalsIgnoreCase("a")) {
                startA(this.mSpannableStringBuilder, attributes);
            } else if (tag.equalsIgnoreCase("u")) {
                start(this.mSpannableStringBuilder, new Underline());
            } else if (tag.equalsIgnoreCase("sup")) {
                start(this.mSpannableStringBuilder, new Super());
            } else if (tag.equalsIgnoreCase("sub")) {
                start(this.mSpannableStringBuilder, new Sub());
            } else if (tag.length() == 2 && Character.toLowerCase(tag.charAt(0)) == DateFormat.HOUR && tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
                handleP(this.mSpannableStringBuilder);
                start(this.mSpannableStringBuilder, new Header(tag.charAt(1) - 49));
            } else if (tag.equalsIgnoreCase("img")) {
                startImg(this.mSpannableStringBuilder, attributes, this.mImageGetter);
            } else if (this.mTagHandler != null) {
                this.mTagHandler.handleTag(true, tag, this.mSpannableStringBuilder, this.mReader);
            }
        }
    }

    private void handleEndTag(String tag) {
        if (tag.equalsIgnoreCase("br")) {
            handleBr(this.mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("p")) {
            handleP(this.mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("div")) {
            handleP(this.mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("strong")) {
            end(this.mSpannableStringBuilder, Bold.class, new StyleSpan(1));
        } else if (tag.equalsIgnoreCase("b")) {
            end(this.mSpannableStringBuilder, Bold.class, new StyleSpan(1));
        } else if (tag.equalsIgnoreCase("em")) {
            end(this.mSpannableStringBuilder, Italic.class, new StyleSpan(2));
        } else if (tag.equalsIgnoreCase("cite")) {
            end(this.mSpannableStringBuilder, Italic.class, new StyleSpan(2));
        } else if (tag.equalsIgnoreCase("dfn")) {
            end(this.mSpannableStringBuilder, Italic.class, new StyleSpan(2));
        } else if (tag.equalsIgnoreCase("i")) {
            end(this.mSpannableStringBuilder, Italic.class, new StyleSpan(2));
        } else if (tag.equalsIgnoreCase("big")) {
            end(this.mSpannableStringBuilder, Big.class, new RelativeSizeSpan(1.25f));
        } else if (tag.equalsIgnoreCase("small")) {
            end(this.mSpannableStringBuilder, Small.class, new RelativeSizeSpan((float) MultiWindowFacade.SPLIT_MAX_WEIGHT));
        } else if (tag.equalsIgnoreCase("font")) {
            endFont(this.mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("blockquote")) {
            handleP(this.mSpannableStringBuilder);
            end(this.mSpannableStringBuilder, Blockquote.class, new QuoteSpan());
        } else if (tag.equalsIgnoreCase("tt")) {
            end(this.mSpannableStringBuilder, Monospace.class, new TypefaceSpan("monospace"));
        } else if (tag.equalsIgnoreCase("a")) {
            endA(this.mSpannableStringBuilder);
        } else if (tag.equalsIgnoreCase("u")) {
            end(this.mSpannableStringBuilder, Underline.class, new UnderlineSpan());
        } else if (tag.equalsIgnoreCase("sup")) {
            end(this.mSpannableStringBuilder, Super.class, new SuperscriptSpan());
        } else if (tag.equalsIgnoreCase("sub")) {
            end(this.mSpannableStringBuilder, Sub.class, new SubscriptSpan());
        } else if (tag.length() == 2 && Character.toLowerCase(tag.charAt(0)) == DateFormat.HOUR && tag.charAt(1) >= '1' && tag.charAt(1) <= '6') {
            handleP(this.mSpannableStringBuilder);
            endHeader(this.mSpannableStringBuilder);
        } else if (this.mTagHandler != null) {
            this.mTagHandler.handleTag(false, tag, this.mSpannableStringBuilder, this.mReader);
        }
    }

    private static void handleP(SpannableStringBuilder text) {
        int len = text.length();
        if (len < 1 || text.charAt(len - 1) != '\n') {
            if (len != 0) {
                text.append((CharSequence) "\n\n");
            }
        } else if (len < 2 || text.charAt(len - 2) != '\n') {
            text.append((CharSequence) "\n");
        }
    }

    private static void handleBr(SpannableStringBuilder text) {
        text.append((CharSequence) "\n");
    }

    private static Object getLast(Spanned text, Class kind) {
        Object[] objs = text.getSpans(0, text.length(), kind);
        if (objs.length == 0) {
            return null;
        }
        return objs[objs.length - 1];
    }

    private static void start(SpannableStringBuilder text, Object mark) {
        int len = text.length();
        text.setSpan(mark, len, len, 17);
    }

    private static void end(SpannableStringBuilder text, Class kind, Object repl) {
        int len = text.length();
        Object obj = getLast(text, kind);
        int where = text.getSpanStart(obj);
        text.removeSpan(obj);
        if (where != len) {
            text.setSpan(repl, where, len, 33);
        }
    }

    private static void startImg(SpannableStringBuilder text, Attributes attributes, ImageGetter img) {
        String src = attributes.getValue("", "src");
        Drawable d = null;
        if (img != null) {
            d = img.getDrawable(src);
        }
        if (d == null) {
            d = Resources.getSystem().getDrawable(R.drawable.unknown_image);
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }
        int len = text.length();
        text.append((CharSequence) "￼");
        text.setSpan(new ImageSpan(d, src), len, text.length(), 33);
    }

    private static void startFont(SpannableStringBuilder text, Attributes attributes) {
        String color = attributes.getValue("", SubscriptionManager.COLOR);
        String face = attributes.getValue("", "face");
        int len = text.length();
        text.setSpan(new Font(color, face), len, len, 17);
    }

    private static void endFont(SpannableStringBuilder text) {
        int len = text.length();
        Font obj = getLast(text, Font.class);
        int where = text.getSpanStart(obj);
        text.removeSpan((Object) obj);
        if (where != len) {
            Font f = obj;
            if (!TextUtils.isEmpty(f.mColor)) {
                if (f.mColor.startsWith("@")) {
                    Resources res = Resources.getSystem();
                    int colorRes = res.getIdentifier(f.mColor.substring(1), SubscriptionManager.COLOR, ZenModeConfig.SYSTEM_AUTHORITY);
                    if (colorRes != 0) {
                        text.setSpan(new TextAppearanceSpan(null, 0, 0, res.getColorStateList(colorRes, null), null), where, len, 33);
                    }
                } else {
                    int c = Color.getHtmlColor(f.mColor);
                    if (c != -1) {
                        text.setSpan(new ForegroundColorSpan(-16777216 | c), where, len, 33);
                    }
                }
            }
            if (f.mFace != null) {
                text.setSpan(new TypefaceSpan(f.mFace), where, len, 33);
            }
        }
    }

    private static void startA(SpannableStringBuilder text, Attributes attributes) {
        String href = attributes.getValue("", "href");
        int len = text.length();
        text.setSpan(new Href(href), len, len, 17);
    }

    private static void endA(SpannableStringBuilder text) {
        int len = text.length();
        Href obj = getLast(text, Href.class);
        int where = text.getSpanStart(obj);
        text.removeSpan((Object) obj);
        if (where != len) {
            Href h = obj;
            if (h.mHref != null) {
                text.setSpan(new URLSpan(h.mHref), where, len, 33);
            }
        }
    }

    private static void endHeader(SpannableStringBuilder text) {
        int len = text.length();
        Header obj = getLast(text, Header.class);
        int where = text.getSpanStart(obj);
        text.removeSpan((Object) obj);
        while (len > where && text.charAt(len - 1) == '\n') {
            len--;
        }
        if (where != len) {
            text.setSpan(new RelativeSizeSpan(HEADER_SIZES[obj.mLevel]), where, len, 33);
            text.setSpan(new StyleSpan(1), where, len, 33);
        }
    }

    public void setDocumentLocator(Locator locator) {
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }

    public void endPrefixMapping(String prefix) throws SAXException {
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        handleStartTag(localName, attributes);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        handleEndTag(localName);
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        CharSequence sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = ch[i + start];
            if (c == ' ' || c == '\n') {
                char pred;
                int len = sb.length();
                if (len == 0) {
                    len = this.mSpannableStringBuilder.length();
                    if (len == 0) {
                        pred = '\n';
                    } else {
                        pred = this.mSpannableStringBuilder.charAt(len - 1);
                    }
                } else {
                    pred = sb.charAt(len - 1);
                }
                if (!(pred == ' ' || pred == '\n')) {
                    sb.append(' ');
                }
            } else {
                sb.append(c);
            }
        }
        this.mSpannableStringBuilder.append(sb);
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
    }

    public void processingInstruction(String target, String data) throws SAXException {
    }

    public void skippedEntity(String name) throws SAXException {
    }
}
