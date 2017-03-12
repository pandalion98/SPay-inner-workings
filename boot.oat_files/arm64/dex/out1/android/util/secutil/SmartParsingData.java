package android.util.secutil;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartParsingData {
    private ArrayList<String> dateInfo = new ArrayList();
    private ArrayList<String> dateMillisInfo = new ArrayList();
    private ArrayList<String> emailInfo = new ArrayList();
    private String originalData = "";
    private ArrayList<String> phoneNumInfo = new ArrayList();
    private String remainData = "";
    private ArrayList<String> timeInfo = new ArrayList();
    private ArrayList<String> timeMillisInfo = new ArrayList();
    private ArrayList<String> urlInfo = new ArrayList();

    public void setInfo(String data, int type) {
        switch (type) {
            case 1:
                this.dateInfo.add(data);
                return;
            case 2:
                this.timeInfo.add(data);
                return;
            case 3:
                this.phoneNumInfo.add(data);
                return;
            case 4:
                this.emailInfo.add(data);
                return;
            case 5:
                this.urlInfo.add(data);
                return;
            case 6:
                this.dateMillisInfo.add(data);
                return;
            case 7:
                this.timeMillisInfo.add(data);
                return;
            default:
                return;
        }
    }

    public ArrayList<String> getInfo(int type) {
        switch (type) {
            case 1:
                return this.dateInfo;
            case 2:
                return this.timeInfo;
            case 3:
                return this.phoneNumInfo;
            case 4:
                return this.emailInfo;
            case 5:
                return this.urlInfo;
            case 6:
                return this.dateMillisInfo;
            case 7:
                return this.timeMillisInfo;
            default:
                return null;
        }
    }

    public int getCount(int type) {
        switch (type) {
            case 1:
                return this.dateInfo.size();
            case 2:
                return this.timeInfo.size();
            case 3:
                return this.phoneNumInfo.size();
            case 4:
                return this.emailInfo.size();
            case 5:
                return this.urlInfo.size();
            case 6:
                return this.dateMillisInfo.size();
            case 7:
                return this.timeMillisInfo.size();
            default:
                return 0;
        }
    }

    public boolean deleteInfo(int index, int type) {
        switch (type) {
            case 1:
                if (index >= this.dateInfo.size()) {
                    return false;
                }
                this.dateInfo.remove(index);
                return true;
            case 2:
                if (index >= this.timeInfo.size()) {
                    return false;
                }
                this.timeInfo.remove(index);
                return true;
            case 3:
                if (index >= this.phoneNumInfo.size()) {
                    return false;
                }
                this.phoneNumInfo.remove(index);
                return true;
            case 4:
                if (index >= this.emailInfo.size()) {
                    return false;
                }
                this.emailInfo.remove(index);
                return true;
            case 5:
                if (index >= this.urlInfo.size()) {
                    return false;
                }
                this.urlInfo.remove(index);
                return true;
            default:
                return false;
        }
    }

    public void setOriginalData(String str) {
        this.originalData = str;
    }

    public void setRemainData(String str) {
        this.remainData = str;
    }

    public String getOriginalData() {
        return this.originalData;
    }

    public String getRemainData() {
        return this.remainData;
    }

    public int getStartPosition(String str) {
        Matcher m = Pattern.compile("(" + Pattern.quote(str) + ")").matcher(getOriginalData());
        int index = -1;
        while (m.find()) {
            index = m.start();
        }
        return index;
    }

    public int getEndPosition(String str) {
        Matcher m = Pattern.compile("(" + Pattern.quote(str) + ")").matcher(getOriginalData());
        int index = -1;
        while (m.find()) {
            index = m.end() - 1;
        }
        return index;
    }

    public void clear() {
        this.dateInfo.clear();
        this.timeInfo.clear();
        this.phoneNumInfo.clear();
        this.emailInfo.clear();
        this.urlInfo.clear();
        this.dateMillisInfo.clear();
        this.timeMillisInfo.clear();
    }
}
