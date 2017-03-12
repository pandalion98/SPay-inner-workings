package android.media;

import android.net.ProxyInfo;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayDeque;
import java.util.LinkedList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* compiled from: TtmlRenderer */
class TtmlParser {
    private static final int DEFAULT_FRAMERATE = 30;
    private static final int DEFAULT_SUBFRAMERATE = 1;
    private static final int DEFAULT_TICKRATE = 1;
    static final String TAG = "TtmlParser";
    private long mCurrentRunId;
    private final TtmlNodeListener mListener;
    private XmlPullParser mParser;
    private LinkedList<TtmlRegionTag> mRegionSet = new LinkedList();

    public TtmlParser(TtmlNodeListener listener) {
        this.mListener = listener;
    }

    public void parse(String ttmlText, long runId) throws XmlPullParserException, IOException {
        this.mParser = null;
        this.mCurrentRunId = runId;
        loadParser(ttmlText);
        parseTtml();
    }

    private void loadParser(String ttmlFragment) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(false);
        this.mParser = factory.newPullParser();
        this.mParser.setInput(new StringReader(ttmlFragment));
    }

    private void extractAttribute(XmlPullParser parser, int i, StringBuilder out) {
        out.append(" ");
        out.append(parser.getAttributeName(i));
        out.append("=\"");
        out.append(parser.getAttributeValue(i));
        out.append("\"");
    }

    private void parseTtml() throws XmlPullParserException, IOException {
        ArrayDeque<TtmlNode> nodeStack = new ArrayDeque();
        int depthInUnsupportedTag = 0;
        boolean active = true;
        while (!isEndOfDoc()) {
            int eventType = this.mParser.getEventType();
            TtmlNode parent = (TtmlNode) nodeStack.peekLast();
            if (active) {
                if (eventType == 2) {
                    if (isSupportedTag(this.mParser.getName())) {
                        TtmlNode node = parseNode(parent);
                        nodeStack.addLast(node);
                        if (parent != null) {
                            parent.mChildren.add(node);
                        }
                    } else {
                        Log.w(TAG, "Unsupported tag " + this.mParser.getName() + " is ignored.");
                        depthInUnsupportedTag++;
                        active = false;
                    }
                } else if (eventType == 4) {
                    String text = this.mParser.getText();
                    if (!(TextUtils.isEmpty(text) || parent == null)) {
                        parent.mChildren.add(new TtmlNode(TtmlUtils.PCDATA, ProxyInfo.LOCAL_EXCL_LIST, text, 0, Long.MAX_VALUE, parent, this.mCurrentRunId));
                    }
                } else if (eventType == 3) {
                    if (this.mParser.getName().equals(TtmlUtils.TAG_P)) {
                        this.mListener.onTtmlNodeParsed((TtmlNode) nodeStack.getLast());
                    } else if (this.mParser.getName().equals(TtmlUtils.TAG_TT)) {
                        this.mListener.onRootNodeParsed((TtmlNode) nodeStack.getLast());
                    }
                    nodeStack.removeLast();
                }
            } else if (eventType == 2) {
                depthInUnsupportedTag++;
            } else if (eventType == 3) {
                depthInUnsupportedTag--;
                if (depthInUnsupportedTag == 0) {
                    active = true;
                }
            }
            this.mParser.next();
        }
    }

    private TtmlNode parseNode(TtmlNode parent) throws XmlPullParserException, IOException {
        if (this.mParser.getEventType() != 2) {
            return null;
        }
        long start = 0;
        long end = Long.MAX_VALUE;
        long dur = 0;
        if (this.mParser.getName().equals(TtmlUtils.TAG_P)) {
            int i = 0;
            while (i < this.mParser.getAttributeCount() && (start == 0 || ((end == 0 && dur == 0) || i <= 1))) {
                String attr = this.mParser.getAttributeName(i);
                String value = this.mParser.getAttributeValue(i);
                attr = attr.replaceFirst("^.*:", ProxyInfo.LOCAL_EXCL_LIST);
                if (attr.equals("begin")) {
                    start = TtmlUtils.parseTimeExpression(value, 30, 1, 1);
                } else if (attr.equals("end")) {
                    end = TtmlUtils.parseTimeExpression(value, 30, 1, 1);
                } else if (attr.equals(TtmlUtils.ATTR_DURATION)) {
                    dur = TtmlUtils.parseTimeExpression(value, 30, 1, 1);
                }
                i++;
            }
        }
        if (parent != null) {
            start += parent.mStartTimeMs;
            if (end != Long.MAX_VALUE) {
                end += parent.mStartTimeMs;
            }
        }
        if (dur > 0) {
            if (end != Long.MAX_VALUE) {
                Log.e(TAG, "'dur' and 'end' attributes are defined at the same time.'end' value is ignored.");
            }
            end = start + dur;
        }
        if (parent != null && end == Long.MAX_VALUE && parent.mEndTimeMs != Long.MAX_VALUE && end > parent.mEndTimeMs) {
            end = parent.mEndTimeMs;
        }
        return new TtmlNode(this.mParser.getName(), null, null, start, end, parent, this.mCurrentRunId);
    }

    private boolean isEndOfDoc() throws XmlPullParserException {
        return this.mParser.getEventType() == 1;
    }

    private static boolean isSupportedTag(String tag) {
        if (tag.equals(TtmlUtils.TAG_TT) || tag.equals(TtmlUtils.TAG_HEAD) || tag.equals(TtmlUtils.TAG_BODY) || tag.equals(TtmlUtils.TAG_DIV) || tag.equals(TtmlUtils.TAG_P) || tag.equals(TtmlUtils.TAG_SPAN) || tag.equals(TtmlUtils.TAG_BR)) {
            return true;
        }
        return false;
    }
}
