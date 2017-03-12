package android.text;

import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.AlignmentSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.ParagraphStyle;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import com.android.internal.util.ArrayUtils;
import com.samsung.android.smartface.SmartFaceManager;
import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

public class Html {

    private static class HtmlParser {
        private static final HTMLSchema schema = new HTMLSchema();

        private HtmlParser() {
        }
    }

    public interface ImageGetter {
        Drawable getDrawable(String str);
    }

    public interface TagHandler {
        void handleTag(boolean z, String str, Editable editable, XMLReader xMLReader);
    }

    private Html() {
    }

    public static Spanned fromHtml(String source) {
        return fromHtml(source, null, null);
    }

    public static Spanned fromHtml(String source, ImageGetter imageGetter, TagHandler tagHandler) {
        Parser parser = new Parser();
        try {
            parser.setProperty("http://www.ccil.org/~cowan/tagsoup/properties/schema", HtmlParser.schema);
            return new HtmlToSpannedConverter(source, imageGetter, tagHandler, parser).convert();
        } catch (SAXNotRecognizedException e) {
            throw new RuntimeException(e);
        } catch (SAXNotSupportedException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static String toHtml(Spanned text) {
        StringBuilder out = new StringBuilder();
        withinHtml(out, text);
        return out.toString();
    }

    public static String escapeHtml(CharSequence text) {
        StringBuilder out = new StringBuilder();
        withinStyle(out, text, 0, text.length());
        return out.toString();
    }

    private static void withinHtml(StringBuilder out, Spanned text) {
        int len = text.length();
        int i = 0;
        while (i < text.length()) {
            int next = text.nextSpanTransition(i, len, ParagraphStyle.class);
            ParagraphStyle[] style = (ParagraphStyle[]) text.getSpans(i, next, ParagraphStyle.class);
            String elements = " ";
            boolean needDiv = false;
            for (int j = 0; j < style.length; j++) {
                if (style[j] instanceof AlignmentSpan) {
                    Alignment align = ((AlignmentSpan) style[j]).getAlignment();
                    needDiv = true;
                    if (align == Alignment.ALIGN_CENTER) {
                        elements = "align=\"center\" " + elements;
                    } else if (align == Alignment.ALIGN_OPPOSITE) {
                        elements = "align=\"right\" " + elements;
                    } else {
                        elements = "align=\"left\" " + elements;
                    }
                }
            }
            if (needDiv) {
                out.append("<div ").append(elements).append(">");
            }
            withinDiv(out, text, i, next);
            if (needDiv) {
                out.append("</div>");
            }
            i = next;
        }
    }

    private static void withinDiv(StringBuilder out, Spanned text, int start, int end) {
        int i = start;
        while (i < end) {
            int next = text.nextSpanTransition(i, end, QuoteSpan.class);
            QuoteSpan[] quotes = (QuoteSpan[]) text.getSpans(i, next, QuoteSpan.class);
            for (QuoteSpan quote : quotes) {
                out.append("<blockquote>");
            }
            withinBlockquote(out, text, i, next);
            for (QuoteSpan quoteSpan : quotes) {
                out.append("</blockquote>\n");
            }
            i = next;
        }
    }

    private static String getOpenParaTagWithDirection(Spanned text, int start, int end) {
        int len = end - start;
        byte[] levels = ArrayUtils.newUnpaddedByteArray(len);
        char[] buffer = TextUtils.obtain(len);
        TextUtils.getChars(text, start, end, buffer, 0);
        switch (AndroidBidi.bidi(2, buffer, levels, len, false)) {
            case -1:
                return "<p dir=\"rtl\">";
            default:
                return "<p dir=\"ltr\">";
        }
    }

    private static void withinBlockquote(StringBuilder out, Spanned text, int start, int end) {
        out.append(getOpenParaTagWithDirection(text, start, end));
        int i = start;
        while (i < end) {
            int next = TextUtils.indexOf((CharSequence) text, '\n', i, end);
            if (next < 0) {
                next = end;
            }
            int nl = 0;
            while (next < end && text.charAt(next) == '\n') {
                nl++;
                next++;
            }
            if (withinParagraph(out, text, i, next - nl, nl, next == end)) {
                out.append("</p>\n");
                out.append(getOpenParaTagWithDirection(text, next, end));
            }
            i = next;
        }
        out.append("</p>\n");
    }

    private static boolean withinParagraph(StringBuilder out, Spanned text, int start, int end, int nl, boolean last) {
        int i = start;
        while (i < end) {
            int j;
            int s;
            int next = text.nextSpanTransition(i, end, CharacterStyle.class);
            CharacterStyle[] style = (CharacterStyle[]) text.getSpans(i, next, CharacterStyle.class);
            for (j = 0; j < style.length; j++) {
                if (style[j] instanceof StyleSpan) {
                    s = ((StyleSpan) style[j]).getStyle();
                    if ((s & 1) != 0) {
                        out.append("<b>");
                    }
                    if ((s & 2) != 0) {
                        out.append("<i>");
                    }
                }
                if (style[j] instanceof TypefaceSpan) {
                    if ("monospace".equals(((TypefaceSpan) style[j]).getFamily())) {
                        out.append("<tt>");
                    }
                }
                if (style[j] instanceof SuperscriptSpan) {
                    out.append("<sup>");
                }
                if (style[j] instanceof SubscriptSpan) {
                    out.append("<sub>");
                }
                if (style[j] instanceof UnderlineSpan) {
                    out.append("<u>");
                }
                if (style[j] instanceof StrikethroughSpan) {
                    out.append("<strike>");
                }
                if (style[j] instanceof URLSpan) {
                    out.append("<a href=\"");
                    out.append(((URLSpan) style[j]).getURL());
                    out.append("\">");
                }
                if (style[j] instanceof ImageSpan) {
                    out.append("<img src=\"");
                    out.append(((ImageSpan) style[j]).getSource());
                    out.append("\">");
                    i = next;
                }
                if (style[j] instanceof AbsoluteSizeSpan) {
                    out.append("<font size =\"");
                    out.append(((AbsoluteSizeSpan) style[j]).getSize() / 6);
                    out.append("\">");
                }
                if (style[j] instanceof ForegroundColorSpan) {
                    out.append("<font color =\"#");
                    String color = Integer.toHexString(((ForegroundColorSpan) style[j]).getForegroundColor() + 16777216);
                    while (color.length() < 6) {
                        color = SmartFaceManager.PAGE_MIDDLE + color;
                    }
                    out.append(color);
                    out.append("\">");
                }
            }
            withinStyle(out, text, i, next);
            j = style.length - 1;
            while (j >= 0) {
                if (style[j] instanceof ForegroundColorSpan) {
                    out.append("</font>");
                }
                if (style[j] instanceof AbsoluteSizeSpan) {
                    out.append("</font>");
                }
                if (style[j] instanceof URLSpan) {
                    out.append("</a>");
                }
                if (style[j] instanceof StrikethroughSpan) {
                    out.append("</strike>");
                }
                if (style[j] instanceof UnderlineSpan) {
                    out.append("</u>");
                }
                if (style[j] instanceof SubscriptSpan) {
                    out.append("</sub>");
                }
                if (style[j] instanceof SuperscriptSpan) {
                    out.append("</sup>");
                }
                if ((style[j] instanceof TypefaceSpan) && ((TypefaceSpan) style[j]).getFamily().equals("monospace")) {
                    out.append("</tt>");
                }
                if (style[j] instanceof StyleSpan) {
                    s = ((StyleSpan) style[j]).getStyle();
                    if ((s & 1) != 0) {
                        out.append("</b>");
                    }
                    if ((s & 2) != 0) {
                        out.append("</i>");
                    }
                }
                j--;
            }
            i = next;
        }
        if (nl == 1) {
            out.append("<br>\n");
            return false;
        }
        for (i = 2; i < nl; i++) {
            out.append("<br>");
        }
        return !last;
    }

    private static void withinStyle(StringBuilder out, CharSequence text, int start, int end) {
        int i = start;
        while (i < end) {
            char c = text.charAt(i);
            if (c == '<') {
                out.append("&lt;");
            } else if (c == '>') {
                out.append("&gt;");
            } else if (c == '&') {
                out.append("&amp;");
            } else if (c < '?' || c > '?') {
                if (c > '~' || c < ' ') {
                    out.append("&#").append(c).append(";");
                } else if (c == ' ') {
                    while (i + 1 < end && text.charAt(i + 1) == ' ') {
                        out.append("&nbsp;");
                        i++;
                    }
                    out.append(' ');
                } else {
                    out.append(c);
                }
            } else if (c < '?' && i + 1 < end) {
                char d = text.charAt(i + 1);
                if (d >= '?' && d <= '?') {
                    i++;
                    out.append("&#").append((65536 | ((c - 55296) << 10)) | (d - 56320)).append(";");
                }
            }
            i++;
        }
    }
}
