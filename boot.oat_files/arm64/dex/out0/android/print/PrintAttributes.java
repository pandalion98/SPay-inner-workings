package android.print;

import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.hardware.Camera.Parameters;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import java.util.Map;

public final class PrintAttributes implements Parcelable {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    public static final Creator<PrintAttributes> CREATOR = new Creator<PrintAttributes>() {
        public PrintAttributes createFromParcel(Parcel parcel) {
            return new PrintAttributes(parcel);
        }

        public PrintAttributes[] newArray(int size) {
            return new PrintAttributes[size];
        }
    };
    public static final int DUPLEX_MODE_LONG_EDGE = 2;
    public static final int DUPLEX_MODE_NONE = 1;
    public static final int DUPLEX_MODE_SHORT_EDGE = 4;
    private static final int VALID_COLOR_MODES = 3;
    private static final int VALID_DUPLEX_MODES = 7;
    private int mColorMode;
    private int mDuplexMode;
    private MediaSize mMediaSize;
    private Margins mMinMargins;
    private Resolution mResolution;

    public static final class Builder {
        private final PrintAttributes mAttributes = new PrintAttributes();

        public Builder setMediaSize(MediaSize mediaSize) {
            this.mAttributes.setMediaSize(mediaSize);
            return this;
        }

        public Builder setResolution(Resolution resolution) {
            this.mAttributes.setResolution(resolution);
            return this;
        }

        public Builder setMinMargins(Margins margins) {
            this.mAttributes.setMinMargins(margins);
            return this;
        }

        public Builder setColorMode(int colorMode) {
            this.mAttributes.setColorMode(colorMode);
            return this;
        }

        public Builder setDuplexMode(int duplexMode) {
            this.mAttributes.setDuplexMode(duplexMode);
            return this;
        }

        public PrintAttributes build() {
            return this.mAttributes;
        }
    }

    public static final class Margins {
        public static final Margins NO_MARGINS = new Margins(0, 0, 0, 0);
        private final int mBottomMils;
        private final int mLeftMils;
        private final int mRightMils;
        private final int mTopMils;

        public Margins(int leftMils, int topMils, int rightMils, int bottomMils) {
            this.mTopMils = topMils;
            this.mLeftMils = leftMils;
            this.mRightMils = rightMils;
            this.mBottomMils = bottomMils;
        }

        public int getLeftMils() {
            return this.mLeftMils;
        }

        public int getTopMils() {
            return this.mTopMils;
        }

        public int getRightMils() {
            return this.mRightMils;
        }

        public int getBottomMils() {
            return this.mBottomMils;
        }

        void writeToParcel(Parcel parcel) {
            parcel.writeInt(this.mLeftMils);
            parcel.writeInt(this.mTopMils);
            parcel.writeInt(this.mRightMils);
            parcel.writeInt(this.mBottomMils);
        }

        static Margins createFromParcel(Parcel parcel) {
            return new Margins(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
        }

        public int hashCode() {
            return ((((((this.mBottomMils + 31) * 31) + this.mLeftMils) * 31) + this.mRightMils) * 31) + this.mTopMils;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Margins other = (Margins) obj;
            if (this.mBottomMils != other.mBottomMils) {
                return false;
            }
            if (this.mLeftMils != other.mLeftMils) {
                return false;
            }
            if (this.mRightMils != other.mRightMils) {
                return false;
            }
            if (this.mTopMils != other.mTopMils) {
                return false;
            }
            return true;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Margins{");
            builder.append("leftMils: ").append(this.mLeftMils);
            builder.append(", topMils: ").append(this.mTopMils);
            builder.append(", rightMils: ").append(this.mRightMils);
            builder.append(", bottomMils: ").append(this.mBottomMils);
            builder.append("}");
            return builder.toString();
        }
    }

    public static final class MediaSize {
        public static final MediaSize ISO_A0 = new MediaSize("ISO_A0", "android", 17040648, 33110, 46810);
        public static final MediaSize ISO_A1 = new MediaSize("ISO_A1", "android", 17040649, 23390, 33110);
        public static final MediaSize ISO_A10 = new MediaSize("ISO_A10", "android", 17040658, 1020, 1460);
        public static final MediaSize ISO_A2 = new MediaSize("ISO_A2", "android", 17040650, 16540, 23390);
        public static final MediaSize ISO_A3 = new MediaSize("ISO_A3", "android", 17040651, 11690, 16540);
        public static final MediaSize ISO_A4 = new MediaSize("ISO_A4", "android", 17040652, 8270, 11690);
        public static final MediaSize ISO_A5 = new MediaSize("ISO_A5", "android", 17040653, 5830, 8270);
        public static final MediaSize ISO_A6 = new MediaSize("ISO_A6", "android", 17040654, 4130, 5830);
        public static final MediaSize ISO_A7 = new MediaSize("ISO_A7", "android", 17040655, 2910, 4130);
        public static final MediaSize ISO_A8 = new MediaSize("ISO_A8", "android", 17040656, 2050, 2910);
        public static final MediaSize ISO_A9 = new MediaSize("ISO_A9", "android", 17040657, 1460, 2050);
        public static final MediaSize ISO_B0 = new MediaSize("ISO_B0", "android", 17040659, 39370, 55670);
        public static final MediaSize ISO_B1 = new MediaSize("ISO_B1", "android", 17040660, 27830, 39370);
        public static final MediaSize ISO_B10 = new MediaSize("ISO_B10", "android", 17040669, 1220, 1730);
        public static final MediaSize ISO_B2 = new MediaSize("ISO_B2", "android", 17040661, 19690, 27830);
        public static final MediaSize ISO_B3 = new MediaSize("ISO_B3", "android", 17040662, 13900, 19690);
        public static final MediaSize ISO_B4 = new MediaSize("ISO_B4", "android", 17040663, 9840, 13900);
        public static final MediaSize ISO_B5 = new MediaSize("ISO_B5", "android", 17040664, 6930, 9840);
        public static final MediaSize ISO_B6 = new MediaSize("ISO_B6", "android", 17040665, 4920, 6930);
        public static final MediaSize ISO_B7 = new MediaSize("ISO_B7", "android", 17040666, 3460, 4920);
        public static final MediaSize ISO_B8 = new MediaSize("ISO_B8", "android", 17040667, 2440, 3460);
        public static final MediaSize ISO_B9 = new MediaSize("ISO_B9", "android", 17040668, 1730, 2440);
        public static final MediaSize ISO_C0 = new MediaSize("ISO_C0", "android", 17040670, 36100, 51060);
        public static final MediaSize ISO_C1 = new MediaSize("ISO_C1", "android", 17040671, 25510, 36100);
        public static final MediaSize ISO_C10 = new MediaSize("ISO_C10", "android", 17040680, 1100, 1570);
        public static final MediaSize ISO_C2 = new MediaSize("ISO_C2", "android", 17040672, 18030, 25510);
        public static final MediaSize ISO_C3 = new MediaSize("ISO_C3", "android", 17040673, 12760, 18030);
        public static final MediaSize ISO_C4 = new MediaSize("ISO_C4", "android", 17040674, 9020, 12760);
        public static final MediaSize ISO_C5 = new MediaSize("ISO_C5", "android", 17040675, 6380, 9020);
        public static final MediaSize ISO_C6 = new MediaSize("ISO_C6", "android", 17040676, 4490, 6380);
        public static final MediaSize ISO_C7 = new MediaSize("ISO_C7", "android", 17040677, 3190, 4490);
        public static final MediaSize ISO_C8 = new MediaSize("ISO_C8", "android", 17040678, 2240, 3190);
        public static final MediaSize ISO_C9 = new MediaSize("ISO_C9", "android", 17040679, 1570, 2240);
        public static final MediaSize JIS_B0 = new MediaSize("JIS_B0", "android", 17040719, 40551, 57323);
        public static final MediaSize JIS_B1 = new MediaSize("JIS_B1", "android", 17040718, 28661, 40551);
        public static final MediaSize JIS_B10 = new MediaSize("JIS_B10", "android", 17040709, 1259, 1772);
        public static final MediaSize JIS_B2 = new MediaSize("JIS_B2", "android", 17040717, 20276, 28661);
        public static final MediaSize JIS_B3 = new MediaSize("JIS_B3", "android", 17040716, 14331, 20276);
        public static final MediaSize JIS_B4 = new MediaSize("JIS_B4", "android", 17040715, 10118, 14331);
        public static final MediaSize JIS_B5 = new MediaSize("JIS_B5", "android", 17040714, 7165, 10118);
        public static final MediaSize JIS_B6 = new MediaSize("JIS_B6", "android", 17040713, 5049, 7165);
        public static final MediaSize JIS_B7 = new MediaSize("JIS_B7", "android", 17040712, 3583, 5049);
        public static final MediaSize JIS_B8 = new MediaSize("JIS_B8", "android", 17040711, 2520, 3583);
        public static final MediaSize JIS_B9 = new MediaSize("JIS_B9", "android", 17040710, 1772, 2520);
        public static final MediaSize JIS_EXEC = new MediaSize("JIS_EXEC", "android", 17040720, 8504, 12992);
        public static final MediaSize JPN_CHOU2 = new MediaSize("JPN_CHOU2", "android", 17040723, 4374, 5748);
        public static final MediaSize JPN_CHOU3 = new MediaSize("JPN_CHOU3", "android", 17040722, 4724, 9252);
        public static final MediaSize JPN_CHOU4 = new MediaSize("JPN_CHOU4", "android", 17040721, 3543, 8071);
        public static final MediaSize JPN_HAGAKI = new MediaSize("JPN_HAGAKI", "android", 17040724, 3937, 5827);
        public static final MediaSize JPN_KAHU = new MediaSize("JPN_KAHU", "android", 17040726, 9449, 12681);
        public static final MediaSize JPN_KAKU2 = new MediaSize("JPN_KAKU2", "android", 17040727, 9449, 13071);
        public static final MediaSize JPN_OUFUKU = new MediaSize("JPN_OUFUKU", "android", 17040725, 5827, 7874);
        public static final MediaSize JPN_YOU4 = new MediaSize("JPN_YOU4", "android", 17040728, 4134, 9252);
        private static final String LOG_TAG = "MediaSize";
        public static final MediaSize NA_FOOLSCAP = new MediaSize("NA_FOOLSCAP", "android", 17040692, 8000, 13000);
        public static final MediaSize NA_GOVT_LETTER = new MediaSize("NA_GOVT_LETTER", "android", 17040682, 8000, 10500);
        public static final MediaSize NA_INDEX_3X5 = new MediaSize("NA_INDEX_3X5", "android", 17040687, 3000, 5000);
        public static final MediaSize NA_INDEX_4X6 = new MediaSize("NA_INDEX_4X6", "android", 17040688, 4000, 6000);
        public static final MediaSize NA_INDEX_5X8 = new MediaSize("NA_INDEX_5X8", "android", 17040689, 5000, 8000);
        public static final MediaSize NA_JUNIOR_LEGAL = new MediaSize("NA_JUNIOR_LEGAL", "android", 17040684, 8000, 5000);
        public static final MediaSize NA_LEDGER = new MediaSize("NA_LEDGER", "android", 17040685, 17000, 11000);
        public static final MediaSize NA_LEGAL = new MediaSize("NA_LEGAL", "android", 17040683, 8500, 14000);
        public static final MediaSize NA_LETTER = new MediaSize("NA_LETTER", "android", 17040681, 8500, 11000);
        public static final MediaSize NA_MONARCH = new MediaSize("NA_MONARCH", "android", 17040690, 7250, 10500);
        public static final MediaSize NA_QUARTO = new MediaSize("NA_QUARTO", "android", 17040691, 8000, 10000);
        public static final MediaSize NA_TABLOID = new MediaSize("NA_TABLOID", "android", 17040686, 11000, 17000);
        public static final MediaSize OM_DAI_PA_KAI = new MediaSize("OM_DAI_PA_KAI", "android", 17040707, 10827, 15551);
        public static final MediaSize OM_JUURO_KU_KAI = new MediaSize("OM_JUURO_KU_KAI", "android", 17040708, 7796, 10827);
        public static final MediaSize OM_PA_KAI = new MediaSize("OM_PA_KAI", "android", 17040706, 10512, 15315);
        public static final MediaSize PRC_1 = new MediaSize("PRC_1", "android", 17040695, 4015, 6496);
        public static final MediaSize PRC_10 = new MediaSize("PRC_10", "android", 17040704, 12756, 18032);
        public static final MediaSize PRC_16K = new MediaSize("PRC_16K", "android", 17040705, 5749, 8465);
        public static final MediaSize PRC_2 = new MediaSize("PRC_2", "android", 17040696, 4015, 6929);
        public static final MediaSize PRC_3 = new MediaSize("PRC_3", "android", 17040697, 4921, 6929);
        public static final MediaSize PRC_4 = new MediaSize("PRC_4", "android", 17040698, 4330, 8189);
        public static final MediaSize PRC_5 = new MediaSize("PRC_5", "android", 17040699, 4330, 8661);
        public static final MediaSize PRC_6 = new MediaSize("PRC_6", "android", 17040700, 4724, 12599);
        public static final MediaSize PRC_7 = new MediaSize("PRC_7", "android", 17040701, 6299, 9055);
        public static final MediaSize PRC_8 = new MediaSize("PRC_8", "android", 17040702, 4724, 12165);
        public static final MediaSize PRC_9 = new MediaSize("PRC_9", "android", 17040703, 9016, 12756);
        public static final MediaSize ROC_16K = new MediaSize("ROC_16K", "android", 17040694, 7677, 10629);
        public static final MediaSize ROC_8K = new MediaSize("ROC_8K", "android", 17040693, 10629, 15354);
        public static final MediaSize UNKNOWN_LANDSCAPE = new MediaSize("UNKNOWN_LANDSCAPE", "android", 17040730, Integer.MAX_VALUE, 1);
        public static final MediaSize UNKNOWN_PORTRAIT = new MediaSize("UNKNOWN_PORTRAIT", "android", 17040729, 1, Integer.MAX_VALUE);
        private static final Map<String, MediaSize> sIdToMediaSizeMap = new ArrayMap();
        private final int mHeightMils;
        private final String mId;
        public final String mLabel;
        public final int mLabelResId;
        public final String mPackageName;
        private final int mWidthMils;

        public MediaSize(String id, String packageName, int labelResId, int widthMils, int heightMils) {
            if (TextUtils.isEmpty(id)) {
                throw new IllegalArgumentException("id cannot be empty.");
            } else if (TextUtils.isEmpty(packageName)) {
                throw new IllegalArgumentException("packageName cannot be empty.");
            } else if (labelResId <= 0) {
                throw new IllegalArgumentException("labelResId must be greater than zero.");
            } else if (widthMils <= 0) {
                throw new IllegalArgumentException("widthMils cannot be less than or equal to zero.");
            } else if (heightMils <= 0) {
                throw new IllegalArgumentException("heightMils cannot be less than or euqual to zero.");
            } else {
                this.mPackageName = packageName;
                this.mId = id;
                this.mLabelResId = labelResId;
                this.mWidthMils = widthMils;
                this.mHeightMils = heightMils;
                this.mLabel = null;
                sIdToMediaSizeMap.put(this.mId, this);
            }
        }

        public MediaSize(String id, String label, int widthMils, int heightMils) {
            if (TextUtils.isEmpty(id)) {
                throw new IllegalArgumentException("id cannot be empty.");
            } else if (TextUtils.isEmpty(label)) {
                throw new IllegalArgumentException("label cannot be empty.");
            } else if (widthMils <= 0) {
                throw new IllegalArgumentException("widthMils cannot be less than or equal to zero.");
            } else if (heightMils <= 0) {
                throw new IllegalArgumentException("heightMils cannot be less than or euqual to zero.");
            } else {
                this.mId = id;
                this.mLabel = label;
                this.mWidthMils = widthMils;
                this.mHeightMils = heightMils;
                this.mLabelResId = 0;
                this.mPackageName = null;
            }
        }

        public MediaSize(String id, String label, String packageName, int widthMils, int heightMils, int labelResId) {
            this.mPackageName = packageName;
            this.mId = id;
            this.mLabelResId = labelResId;
            this.mWidthMils = widthMils;
            this.mHeightMils = heightMils;
            this.mLabel = label;
        }

        public String getId() {
            return this.mId;
        }

        public String getLabel(PackageManager packageManager) {
            if (!TextUtils.isEmpty(this.mPackageName) && this.mLabelResId > 0) {
                try {
                    return packageManager.getResourcesForApplication(this.mPackageName).getString(this.mLabelResId);
                } catch (NotFoundException e) {
                    Log.w(LOG_TAG, "Could not load resouce" + this.mLabelResId + " from package " + this.mPackageName);
                } catch (NameNotFoundException e2) {
                    Log.w(LOG_TAG, "Could not load resouce" + this.mLabelResId + " from package " + this.mPackageName);
                }
            }
            return this.mLabel;
        }

        public int getWidthMils() {
            return this.mWidthMils;
        }

        public int getHeightMils() {
            return this.mHeightMils;
        }

        public boolean isPortrait() {
            return this.mHeightMils >= this.mWidthMils;
        }

        public MediaSize asPortrait() {
            return isPortrait() ? this : new MediaSize(this.mId, this.mLabel, this.mPackageName, Math.min(this.mWidthMils, this.mHeightMils), Math.max(this.mWidthMils, this.mHeightMils), this.mLabelResId);
        }

        public MediaSize asLandscape() {
            return !isPortrait() ? this : new MediaSize(this.mId, this.mLabel, this.mPackageName, Math.max(this.mWidthMils, this.mHeightMils), Math.min(this.mWidthMils, this.mHeightMils), this.mLabelResId);
        }

        void writeToParcel(Parcel parcel) {
            parcel.writeString(this.mId);
            parcel.writeString(this.mLabel);
            parcel.writeString(this.mPackageName);
            parcel.writeInt(this.mWidthMils);
            parcel.writeInt(this.mHeightMils);
            parcel.writeInt(this.mLabelResId);
        }

        static MediaSize createFromParcel(Parcel parcel) {
            return new MediaSize(parcel.readString(), parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt(), parcel.readInt());
        }

        public int hashCode() {
            return ((this.mWidthMils + 31) * 31) + this.mHeightMils;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            MediaSize other = (MediaSize) obj;
            if (this.mWidthMils != other.mWidthMils) {
                return false;
            }
            if (this.mHeightMils != other.mHeightMils) {
                return false;
            }
            return true;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("MediaSize{");
            builder.append("id: ").append(this.mId);
            builder.append(", label: ").append(this.mLabel);
            builder.append(", packageName: ").append(this.mPackageName);
            builder.append(", heightMils: ").append(this.mHeightMils);
            builder.append(", widthMils: ").append(this.mWidthMils);
            builder.append(", labelResId: ").append(this.mLabelResId);
            builder.append("}");
            return builder.toString();
        }

        public static MediaSize getStandardMediaSizeById(String id) {
            return (MediaSize) sIdToMediaSizeMap.get(id);
        }
    }

    public static final class Resolution {
        private final int mHorizontalDpi;
        private final String mId;
        private final String mLabel;
        private final int mVerticalDpi;

        public Resolution(String id, String label, int horizontalDpi, int verticalDpi) {
            if (TextUtils.isEmpty(id)) {
                throw new IllegalArgumentException("id cannot be empty.");
            } else if (TextUtils.isEmpty(label)) {
                throw new IllegalArgumentException("label cannot be empty.");
            } else if (horizontalDpi <= 0) {
                throw new IllegalArgumentException("horizontalDpi cannot be less than or equal to zero.");
            } else if (verticalDpi <= 0) {
                throw new IllegalArgumentException("verticalDpi cannot be less than or equal to zero.");
            } else {
                this.mId = id;
                this.mLabel = label;
                this.mHorizontalDpi = horizontalDpi;
                this.mVerticalDpi = verticalDpi;
            }
        }

        public String getId() {
            return this.mId;
        }

        public String getLabel() {
            return this.mLabel;
        }

        public int getHorizontalDpi() {
            return this.mHorizontalDpi;
        }

        public int getVerticalDpi() {
            return this.mVerticalDpi;
        }

        void writeToParcel(Parcel parcel) {
            parcel.writeString(this.mId);
            parcel.writeString(this.mLabel);
            parcel.writeInt(this.mHorizontalDpi);
            parcel.writeInt(this.mVerticalDpi);
        }

        static Resolution createFromParcel(Parcel parcel) {
            return new Resolution(parcel.readString(), parcel.readString(), parcel.readInt(), parcel.readInt());
        }

        public int hashCode() {
            return ((this.mHorizontalDpi + 31) * 31) + this.mVerticalDpi;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            Resolution other = (Resolution) obj;
            if (this.mHorizontalDpi != other.mHorizontalDpi) {
                return false;
            }
            if (this.mVerticalDpi != other.mVerticalDpi) {
                return false;
            }
            return true;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("Resolution{");
            builder.append("id: ").append(this.mId);
            builder.append(", label: ").append(this.mLabel);
            builder.append(", horizontalDpi: ").append(this.mHorizontalDpi);
            builder.append(", verticalDpi: ").append(this.mVerticalDpi);
            builder.append("}");
            return builder.toString();
        }
    }

    PrintAttributes() {
        this.mDuplexMode = 1;
    }

    private PrintAttributes(Parcel parcel) {
        Resolution createFromParcel;
        Margins margins = null;
        this.mDuplexMode = 1;
        this.mMediaSize = parcel.readInt() == 1 ? MediaSize.createFromParcel(parcel) : null;
        if (parcel.readInt() == 1) {
            createFromParcel = Resolution.createFromParcel(parcel);
        } else {
            createFromParcel = null;
        }
        this.mResolution = createFromParcel;
        if (parcel.readInt() == 1) {
            margins = Margins.createFromParcel(parcel);
        }
        this.mMinMargins = margins;
        this.mColorMode = parcel.readInt();
        this.mDuplexMode = parcel.readInt();
    }

    public MediaSize getMediaSize() {
        return this.mMediaSize;
    }

    public void setMediaSize(MediaSize mediaSize) {
        this.mMediaSize = mediaSize;
    }

    public Resolution getResolution() {
        return this.mResolution;
    }

    public void setResolution(Resolution resolution) {
        this.mResolution = resolution;
    }

    public Margins getMinMargins() {
        return this.mMinMargins;
    }

    public void setMinMargins(Margins margins) {
        this.mMinMargins = margins;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public void setColorMode(int colorMode) {
        enforceValidColorMode(colorMode);
        this.mColorMode = colorMode;
    }

    public boolean isPortrait() {
        return this.mMediaSize.isPortrait();
    }

    public int getDuplexMode() {
        return this.mDuplexMode;
    }

    public void setDuplexMode(int duplexMode) {
        enforceValidDuplexMode(duplexMode);
        this.mDuplexMode = duplexMode;
    }

    public PrintAttributes asPortrait() {
        if (isPortrait()) {
            return this;
        }
        PrintAttributes attributes = new PrintAttributes();
        attributes.setMediaSize(getMediaSize().asPortrait());
        Resolution oldResolution = getResolution();
        attributes.setResolution(new Resolution(oldResolution.getId(), oldResolution.getLabel(), oldResolution.getVerticalDpi(), oldResolution.getHorizontalDpi()));
        attributes.setMinMargins(getMinMargins());
        attributes.setColorMode(getColorMode());
        attributes.setDuplexMode(getDuplexMode());
        return attributes;
    }

    public PrintAttributes asLandscape() {
        if (!isPortrait()) {
            return this;
        }
        PrintAttributes attributes = new PrintAttributes();
        attributes.setMediaSize(getMediaSize().asLandscape());
        Resolution oldResolution = getResolution();
        attributes.setResolution(new Resolution(oldResolution.getId(), oldResolution.getLabel(), oldResolution.getVerticalDpi(), oldResolution.getHorizontalDpi()));
        attributes.setMinMargins(getMinMargins());
        attributes.setColorMode(getColorMode());
        attributes.setDuplexMode(getDuplexMode());
        return attributes;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        if (this.mMediaSize != null) {
            parcel.writeInt(1);
            this.mMediaSize.writeToParcel(parcel);
        } else {
            parcel.writeInt(0);
        }
        if (this.mResolution != null) {
            parcel.writeInt(1);
            this.mResolution.writeToParcel(parcel);
        } else {
            parcel.writeInt(0);
        }
        if (this.mMinMargins != null) {
            parcel.writeInt(1);
            this.mMinMargins.writeToParcel(parcel);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeInt(this.mColorMode);
        parcel.writeInt(this.mDuplexMode);
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = (((((((this.mColorMode + 31) * 31) + this.mDuplexMode) * 31) + (this.mMinMargins == null ? 0 : this.mMinMargins.hashCode())) * 31) + (this.mMediaSize == null ? 0 : this.mMediaSize.hashCode())) * 31;
        if (this.mResolution != null) {
            i = this.mResolution.hashCode();
        }
        return hashCode + i;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PrintAttributes other = (PrintAttributes) obj;
        if (this.mColorMode != other.mColorMode) {
            return false;
        }
        if (this.mDuplexMode != other.mDuplexMode) {
            return false;
        }
        if (this.mMinMargins == null) {
            if (other.mMinMargins != null) {
                return false;
            }
        } else if (!this.mMinMargins.equals(other.mMinMargins)) {
            return false;
        }
        if (this.mMediaSize == null) {
            if (other.mMediaSize != null) {
                return false;
            }
        } else if (!this.mMediaSize.equals(other.mMediaSize)) {
            return false;
        }
        if (this.mResolution == null) {
            if (other.mResolution != null) {
                return false;
            }
            return true;
        } else if (this.mResolution.equals(other.mResolution)) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PrintAttributes{");
        builder.append("mediaSize: ").append(this.mMediaSize);
        if (this.mMediaSize != null) {
            builder.append(", orientation: ").append(this.mMediaSize.isPortrait() ? Parameters.SCENE_MODE_PORTRAIT : Parameters.SCENE_MODE_LANDSCAPE);
        } else {
            builder.append(", orientation: ").append("null");
        }
        builder.append(", resolution: ").append(this.mResolution);
        builder.append(", minMargins: ").append(this.mMinMargins);
        builder.append(", colorMode: ").append(colorModeToString(this.mColorMode));
        builder.append(", duplexMode: ").append(duplexModeToString(this.mDuplexMode));
        builder.append("}");
        return builder.toString();
    }

    public void clear() {
        this.mMediaSize = null;
        this.mResolution = null;
        this.mMinMargins = null;
        this.mColorMode = 0;
        this.mDuplexMode = 1;
    }

    public void copyFrom(PrintAttributes other) {
        this.mMediaSize = other.mMediaSize;
        this.mResolution = other.mResolution;
        this.mMinMargins = other.mMinMargins;
        this.mColorMode = other.mColorMode;
        this.mDuplexMode = other.mDuplexMode;
    }

    static String colorModeToString(int colorMode) {
        switch (colorMode) {
            case 1:
                return "COLOR_MODE_MONOCHROME";
            case 2:
                return "COLOR_MODE_COLOR";
            default:
                return "COLOR_MODE_UNKNOWN";
        }
    }

    static String duplexModeToString(int duplexMode) {
        switch (duplexMode) {
            case 1:
                return "DUPLEX_MODE_NONE";
            case 2:
                return "DUPLEX_MODE_LONG_EDGE";
            case 4:
                return "DUPLEX_MODE_SHORT_EDGE";
            default:
                return "DUPLEX_MODE_UNKNOWN";
        }
    }

    static void enforceValidColorMode(int colorMode) {
        if ((colorMode & 3) == 0 || Integer.bitCount(colorMode) != 1) {
            throw new IllegalArgumentException("invalid color mode: " + colorMode);
        }
    }

    static void enforceValidDuplexMode(int duplexMode) {
        if ((duplexMode & 7) == 0 || Integer.bitCount(duplexMode) != 1) {
            throw new IllegalArgumentException("invalid duplex mode: " + duplexMode);
        }
    }
}
