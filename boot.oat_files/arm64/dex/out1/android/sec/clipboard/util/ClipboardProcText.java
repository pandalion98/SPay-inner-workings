package android.sec.clipboard.util;

import android.util.Log;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public class ClipboardProcText {
    private static final String IMG_BEGIN = "<img";
    private static final String IMG_SRC = "src=\"";
    private static final String TAG = "ClipboardProcText";
    private static boolean body_found = false;
    private static String href = "";
    private static boolean in_body = false;
    private static boolean pre = false;

    public static String getImgFileNameFormHtml(String sSource) {
        String sSourceOriginal = sSource;
        String sSourceLower = sSourceOriginal.toLowerCase();
        String sResult = "";
        int iIndex = sSourceLower.indexOf(IMG_BEGIN);
        if (iIndex != -1) {
            while (iIndex > -1) {
                sSourceLower = sSourceLower.substring(iIndex);
                sSourceOriginal = sSourceOriginal.substring(iIndex);
                int iSubIndex = sSourceLower.indexOf(IMG_SRC);
                if (iSubIndex <= 0) {
                    break;
                }
                iSubIndex += IMG_SRC.length();
                sSourceLower = sSourceLower.substring(iSubIndex);
                sSourceOriginal = sSourceOriginal.substring(iSubIndex);
                int i1 = sSourceLower.indexOf("\"");
                sResult = sSourceOriginal.substring(0, i1);
                sSourceLower = sSourceLower.substring(i1);
                sSourceOriginal = sSourceOriginal.substring(i1);
                if (sResult.length() > 0) {
                    break;
                }
                iIndex = sSourceLower.indexOf(IMG_BEGIN);
            }
        } else if (sSourceLower.indexOf("<iframe") >= 0) {
            Log.d(TAG, "This is using a iframe tag.");
        }
        return sResult;
    }

    public static String convertString(String source) throws Exception {
        StringBuffer result = new StringBuffer();
        StringBuffer result2 = new StringBuffer();
        StringReader input = new StringReader(source);
        try {
            StringBuffer s;
            int dChar = input.read();
            while (dChar != -1) {
                String text = "";
                if (dChar == 60) {
                    text = convertTag(getTag(input));
                } else if (dChar == 38) {
                    String specialchar = getSpecial(input);
                    if (specialchar.equals("lt;") || specialchar.equals("#60")) {
                        text = "<";
                    } else if (specialchar.equals("gt;") || specialchar.equals("#62")) {
                        text = ">";
                    } else if (specialchar.equals("amp;") || specialchar.equals("#38")) {
                        text = "&";
                    } else if (specialchar.equals("nbsp;")) {
                        text = " ";
                    } else if (specialchar.equals("quot;") || specialchar.equals("#34")) {
                        text = "\"";
                    } else if (specialchar.equals("copy;") || specialchar.equals("#169")) {
                        text = "[Copyright]";
                    } else if (specialchar.equals("reg;") || specialchar.equals("#174")) {
                        text = "[Registered]";
                    } else if (specialchar.equals("trade;") || specialchar.equals("#153")) {
                        text = "[Trademark]";
                    } else {
                        text = "&" + specialchar;
                    }
                } else if (pre || !Character.isWhitespace((char) dChar)) {
                    text = "" + ((char) dChar);
                } else {
                    if (in_body) {
                        s = result;
                    } else {
                        s = result2;
                    }
                    if (s.length() <= 0 || !Character.isWhitespace(s.charAt(s.length() - 1))) {
                        text = " ";
                    } else {
                        text = "";
                    }
                }
                if (in_body) {
                    s = result;
                } else {
                    s = result2;
                }
                s.append(text);
                dChar = input.read();
            }
            if (body_found) {
                s = result;
            } else {
                s = result2;
            }
            return s.toString().trim();
        } catch (Exception e) {
            input.close();
            throw e;
        }
    }

    private static String getTag(Reader r) throws IOException {
        StringBuffer result = new StringBuffer();
        int level = 1;
        result.append('<');
        while (level > 0) {
            int dChar = r.read();
            if (dChar == -1) {
                break;
            }
            result.append((char) dChar);
            if (dChar == 60) {
                level++;
            } else if (dChar == 62) {
                level--;
            }
        }
        return result.toString();
    }

    private static String getSpecial(Reader rReader) throws IOException {
        StringBuffer result = new StringBuffer();
        rReader.mark(1);
        int dCharacter = rReader.read();
        while (Character.isLetter((char) dCharacter)) {
            result.append((char) dCharacter);
            rReader.mark(1);
            dCharacter = rReader.read();
        }
        if (dCharacter == 59) {
            result.append(';');
        } else {
            rReader.reset();
        }
        return result.toString();
    }

    private static boolean isTag(String sSentence, String sTag) {
        sSentence = sSentence.toLowerCase();
        return sSentence.startsWith(new StringBuilder().append("<").append(sTag.toLowerCase()).append(">").toString()) || sSentence.startsWith("<" + sTag.toLowerCase() + " ");
    }

    private static String convertTag(String sText) throws IOException {
        String result = "";
        if (isTag(sText, "body")) {
            in_body = true;
            body_found = true;
            return result;
        } else if (isTag(sText, "/body")) {
            in_body = false;
            return "";
        } else if (isTag(sText, "center")) {
            return "";
        } else {
            if (isTag(sText, "/center")) {
                return "";
            }
            if (isTag(sText, "pre")) {
                result = "";
                pre = true;
                return result;
            } else if (isTag(sText, "/pre")) {
                result = "";
                pre = false;
                return result;
            } else if (isTag(sText, "p")) {
                return "\n";
            } else {
                if (isTag(sText, "br")) {
                    return "\n";
                }
                if (isTag(sText, "h1") || isTag(sText, "h2") || isTag(sText, "h3") || isTag(sText, "h4") || isTag(sText, "h5") || isTag(sText, "h6") || isTag(sText, "h7")) {
                    return "";
                }
                if (isTag(sText, "/h1") || isTag(sText, "/h2") || isTag(sText, "/h3") || isTag(sText, "/h4") || isTag(sText, "/h5") || isTag(sText, "/h6") || isTag(sText, "/h7")) {
                    return "";
                }
                if (isTag(sText, "/dl")) {
                    return "";
                }
                if (isTag(sText, "dd")) {
                    return "";
                }
                if (isTag(sText, "dt")) {
                    return "      ";
                }
                if (isTag(sText, "li")) {
                    return "\n   ";
                }
                if (isTag(sText, "/ul")) {
                    return "";
                }
                if (isTag(sText, "/ol")) {
                    return "";
                }
                if (isTag(sText, "hr")) {
                    return "_________________________________________";
                }
                if (isTag(sText, "table")) {
                    return "";
                }
                if (isTag(sText, "/table")) {
                    return "";
                }
                if (isTag(sText, "form")) {
                    return "";
                }
                if (isTag(sText, "/form")) {
                    return "";
                }
                if (isTag(sText, "b")) {
                    return "";
                }
                if (isTag(sText, "/b")) {
                    return "";
                }
                if (isTag(sText, "i")) {
                    return "";
                }
                if (isTag(sText, "/i")) {
                    return "";
                }
                int idx;
                if (isTag(sText, "img")) {
                    idx = sText.indexOf("alt=\"");
                    if (idx == -1) {
                        return result;
                    }
                    idx += 5;
                    return sText.substring(idx, sText.indexOf("\"", idx));
                } else if (isTag(sText, "a")) {
                    idx = sText.indexOf("href=\"");
                    if (idx != -1) {
                        idx += 6;
                        href = sText.substring(idx, sText.indexOf("\"", idx));
                        return result;
                    }
                    href = "";
                    return result;
                } else if (isTag(sText, "/a")) {
                    if (href.length() > 0) {
                        return "\n";
                    }
                    return result;
                } else if (isTag(sText, "/tr")) {
                    return "\n";
                } else {
                    return result;
                }
            }
        }
    }
}
