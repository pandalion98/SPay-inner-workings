package com.android.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.os.Vibrator.MagnitudeTypes;
import android.provider.Settings.Secure;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.android.internal.R;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.HashMap;

public class DirectionLockView extends RelativeLayout {
    public static int DEFAULT_ARROW_SIZE = 182;
    private static double DIRECTION_TOLERANCE_ANGLE = 90.0d;
    public static boolean FOUR_CONFIGURATION_MODE;
    private int ARROW_SIZE;
    private int REFERENCE_SQUARE_DIMENSION;
    private boolean SETTINGS_APP;
    private String TAG;
    private final String TTS_DEFAULT_RATE_STRING;
    OnInitListener _TTSListener;
    private int currentErrorResId;
    private boolean isParentArrowSize;
    private AccessibilityManager mAccessibilityManager;
    private boolean mAllow;
    private boolean mAllowDouble;
    MediaPlayer mBeepEast;
    MediaPlayer mBeepNorth;
    MediaPlayer mBeepSouth;
    MediaPlayer mBeepWest;
    private Bitmap mBlankBitmap;
    private double mBottomY;
    private Bitmap mCurBitmap;
    private String mCurrentPassword;
    private String mCurrentPasswordNumbers;
    private Bitmap mDirectionBitmapDown;
    private Bitmap mDirectionBitmapLeft;
    private Bitmap mDirectionBitmapRight;
    private Bitmap mDirectionBitmapUp;
    private boolean mDirectionDetected;
    private ImageView mDirectionImageView;
    private DirectionLockTouchListener mDirectionLockTouchListener;
    private String mDownAnnounce;
    private final char mDownChar;
    private final int mDownNumber;
    private Bitmap mErrorBitmap;
    private boolean mForceRestart;
    private HashMap<String, String> mHashMap;
    private boolean mInsideReferenceSquare;
    private String mLeftAnnounce;
    private final char mLeftChar;
    private final int mLeftNumber;
    private double mLeftX;
    private int mParentArrowSize;
    private boolean mPlayBeep;
    private boolean mPlayVibration;
    private boolean mPlayVoice;
    private String mRightAnnounce;
    private final char mRightChar;
    private final int mRightNumber;
    private double mRightX;
    private double mScreenHeight;
    private double mScreenWidth;
    private boolean mShowArrow;
    private double mSquare_size;
    private TextToSpeech mTextToSpeech;
    private double mTopY;
    private double mTradeSpace;
    private String mUpAnnounce;
    private final char mUpChar;
    private final int mUpNumber;
    private int mVibratePattern;
    private final int mVibration_pattern_down;
    private final int mVibration_pattern_left;
    private final int mVibration_pattern_right;
    private final int mVibration_pattern_up;
    private Vibrator mVibrator;
    Context myContext;

    static /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$android$internal$widget$DirectionLockView$Direction = new int[Direction.values().length];

        static {
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.UP_RIGHT.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.DOWN_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.UP_LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.DOWN_LEFT.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.UP.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.RIGHT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.DOWN.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.LEFT.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                $SwitchMap$com$android$internal$widget$DirectionLockView$Direction[Direction.INVALID.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
        }
    }

    private enum Direction {
        UP,
        UP_RIGHT,
        RIGHT,
        DOWN_RIGHT,
        DOWN,
        DOWN_LEFT,
        LEFT,
        UP_LEFT,
        INVALID
    }

    public DirectionLockView(Context context) {
        this(context, null);
    }

    public DirectionLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DirectionLockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.TAG = "DirectionLockView";
        this.mBlankBitmap = null;
        this.mDirectionBitmapUp = null;
        this.mDirectionBitmapRight = null;
        this.mDirectionBitmapLeft = null;
        this.mDirectionBitmapDown = null;
        this.mCurBitmap = null;
        this.mErrorBitmap = null;
        this.SETTINGS_APP = false;
        this.isParentArrowSize = false;
        this.mAllowDouble = false;
        this.mParentArrowSize = 0;
        this.currentErrorResId = 0;
        this.TTS_DEFAULT_RATE_STRING = "tts_default_rate";
        this.mUpChar = 'U';
        this.mDownChar = 'D';
        this.mLeftChar = DateFormat.STANDALONE_MONTH;
        this.mRightChar = 'R';
        this.mUpNumber = 49;
        this.mLeftNumber = 57;
        this.mRightNumber = 51;
        this.mDownNumber = 55;
        this.REFERENCE_SQUARE_DIMENSION = 100;
        this.mVibration_pattern_up = HapticFeedbackConstants.VIBE_COMMON_C;
        this.mVibration_pattern_down = HapticFeedbackConstants.VIBE_COMMON_C;
        this.mVibration_pattern_right = HapticFeedbackConstants.VIBE_COMMON_G;
        this.mVibration_pattern_left = HapticFeedbackConstants.VIBE_COMMON_G;
        this._TTSListener = new OnInitListener() {
            public void onInit(int status) {
            }
        };
        this.myContext = context;
        initDirectionLockView();
    }

    public void clearScreen() {
        try {
            if (this.mBlankBitmap == null) {
                this.mBlankBitmap = Bitmap.createBitmap((int) this.mScreenWidth, (int) this.mScreenHeight, Config.ARGB_8888);
            }
            this.mDirectionImageView.setImageBitmap(this.mBlankBitmap);
            invalidate();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }

    public void setShowArrow(boolean value) {
        this.mShowArrow = value;
    }

    public void setPlayVibration(boolean value) {
        this.mPlayVibration = value;
    }

    public void setPlayVoice(boolean value) {
        this.mPlayVoice = value;
    }

    public void setPlayBeep(boolean value) {
        this.mPlayBeep = value;
    }

    private double dipToPixels(double dipValue) {
        return (double) TypedValue.applyDimension(1, (float) dipValue, getResources().getDisplayMetrics());
    }

    public void resetPassword() {
        this.mCurrentPassword = "";
        this.mCurrentPasswordNumbers = "";
    }

    public String getCurrentPassword() {
        return this.mCurrentPassword;
    }

    public String getCurrentPasswordNumbers() {
        return this.mCurrentPasswordNumbers;
    }

    public void setCurrentPassword(String givenPassword) {
        this.mCurrentPassword = givenPassword;
        this.mCurrentPasswordNumbers = "";
        for (int counter = 0; counter < givenPassword.length(); counter++) {
            switch (givenPassword.charAt(counter)) {
                case 'D':
                    this.mCurrentPasswordNumbers += '7';
                    break;
                case 'L':
                    this.mCurrentPasswordNumbers += '9';
                    break;
                case 'R':
                    this.mCurrentPasswordNumbers += '3';
                    break;
                case 'U':
                    this.mCurrentPasswordNumbers += '1';
                    break;
                default:
                    break;
            }
        }
    }

    public void setOnDirectionLockTouchListener(DirectionLockTouchListener listener) {
        this.mDirectionLockTouchListener = listener;
    }

    public boolean onTouchEvent(MotionEvent event) {
        Log.i(this.TAG, "in onTouchEvent()");
        if (!isEnabled()) {
            return false;
        }
        if ((event.getAction() & 255) == 3 || (event.getAction() & 255) == 1) {
            Log.i(this.TAG, "in onTouchEvent() ACTION_UP or ACTION_CANCEL called");
            if (this.mDirectionLockTouchListener != null) {
                this.mDirectionLockTouchListener.onDirectionDetected(DirectionLockTouchListener.FINGER_LIFT, this.mDirectionDetected);
            }
            this.mDirectionDetected = false;
            return true;
        }
        if (this.mDirectionLockTouchListener != null) {
            this.mDirectionLockTouchListener.onDirectionStarted();
        }
        if (event.getX() >= 0.0f && event.getY() >= 0.0f) {
            double curX = (double) event.getX();
            double curY = (double) event.getY();
            switch (event.getAction() & 255) {
                case 0:
                    this.mAllowDouble = true;
                    this.mForceRestart = false;
                    createReferenceSquare(curX, curY, true);
                    Log.i(this.TAG, "in onTouchEvent() ACTION_DOWN called");
                    this.mInsideReferenceSquare = true;
                    this.mDirectionDetected = false;
                    break;
                case 2:
                    if (!this.mForceRestart) {
                        if (this.mInsideReferenceSquare) {
                            if (this.mAllow) {
                                boolean insideTradeArea = false;
                                if (!FOUR_CONFIGURATION_MODE) {
                                    boolean isTradeNorthEast = curX >= this.mRightX - this.mTradeSpace && curX < this.mRightX && curY <= this.mTopY && curY >= this.mTopY - this.mTradeSpace;
                                    boolean isTradeEastNorth = curY > this.mTopY && curY <= this.mTopY + this.mTradeSpace && curX >= this.mRightX && curX <= this.mRightX + this.mTradeSpace;
                                    boolean isTradeEastSouth = curY < this.mBottomY && curY >= this.mBottomY - this.mTradeSpace && curX >= this.mRightX && curX <= this.mRightX + this.mTradeSpace;
                                    boolean isTradeSouthEast = curX >= this.mRightX - this.mTradeSpace && curX < this.mRightX && curY >= this.mBottomY && curY <= this.mBottomY + this.mTradeSpace;
                                    boolean isTradeSouthWest = curX >= this.mLeftX && curX <= this.mLeftX + this.mTradeSpace && curY >= this.mBottomY && curY <= this.mBottomY + this.mTradeSpace;
                                    boolean isTradeWestSouth = curX <= this.mLeftX && curX >= this.mLeftX - this.mTradeSpace && curY < this.mBottomY && curY >= this.mBottomY - this.mTradeSpace;
                                    boolean isTradeWestNorth = curX <= this.mLeftX && curX >= this.mLeftX - this.mTradeSpace && curY >= this.mTopY && curY <= this.mTopY + this.mTradeSpace;
                                    boolean isTradeNorthWest = curX >= this.mLeftX && curX <= this.mLeftX + this.mTradeSpace && curY <= this.mTopY && curY >= this.mTopY - this.mTradeSpace;
                                    if (isTradeNorthEast || isTradeEastNorth || isTradeEastSouth || isTradeSouthEast || isTradeSouthWest || isTradeWestSouth || isTradeWestNorth || isTradeNorthWest) {
                                        insideTradeArea = true;
                                    }
                                }
                                Direction curDirection = Direction.INVALID;
                                if (!insideTradeArea) {
                                    curDirection = getDirection(curX, curY, this.mLeftX, this.mRightX, this.mTopY, this.mBottomY);
                                }
                                switch (AnonymousClass2.$SwitchMap$com$android$internal$widget$DirectionLockView$Direction[curDirection.ordinal()]) {
                                    case 1:
                                    case 2:
                                    case 3:
                                    case 4:
                                        Log.i(this.TAG, "in onTouchEvent() Diagonal Detected");
                                        this.mForceRestart = true;
                                        clearScreen();
                                        if (this.mPlayVoice || this.mAccessibilityManager.isTouchExplorationEnabled()) {
                                            this.mTextToSpeech.speak(this.myContext.getString(R.string.direction_view_diagonal_msg), 0, this.mHashMap);
                                        }
                                        resetPassword();
                                        if (this.mDirectionLockTouchListener != null) {
                                            this.mDirectionLockTouchListener.onDirectionDetected(DirectionLockTouchListener.DIAGONAL, this.mDirectionDetected);
                                            break;
                                        }
                                        break;
                                    case 5:
                                    case 6:
                                    case 7:
                                    case 8:
                                        handleDirectionEvent(curDirection, this.mAllowDouble);
                                        createReferenceSquare(curX, curY, false);
                                        this.mAllowDouble = false;
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        this.mInsideReferenceSquare = true;
                        break;
                    }
                    break;
            }
        }
        return true;
    }

    private void initDirectionLockView() {
        Log.i(this.TAG, "in initDirectinLockView()");
        this.mHashMap = new HashMap();
        this.mHashMap.put("com.samsung.SMT.KEY_PARAM", "DISABLE_NOTICE_POPUP");
        refreshScreenDimensions();
        getDirectionImageView();
        this.mCurrentPassword = "";
        this.mCurrentPasswordNumbers = "";
        this.mInsideReferenceSquare = true;
        this.mForceRestart = false;
        this.mAllow = true;
        this.mShowArrow = true;
        this.mPlayVoice = true;
        this.mPlayVibration = true;
        this.mPlayBeep = true;
        this.mBeepNorth = MediaPlayer.create(this.myContext, R.raw.universal_lock_beep_up);
        this.mBeepSouth = MediaPlayer.create(this.myContext, R.raw.universal_lock_beep_down);
        this.mBeepEast = MediaPlayer.create(this.myContext, R.raw.universal_lock_beep_right);
        this.mBeepWest = MediaPlayer.create(this.myContext, R.raw.universal_lock_beep_left);
        this.mTextToSpeech = new TextToSpeech(this.myContext, this._TTSListener);
        int mDefaultRate = Secure.getInt(this.myContext.getContentResolver(), "tts_default_rate", 100);
        Log.i(this.TAG, "mTextToSpeech Rate - " + mDefaultRate);
        this.mTextToSpeech.setSpeechRate(((float) mDefaultRate) / 100.0f);
        this.mUpAnnounce = this.myContext.getString(R.string.direction_view_up);
        this.mDownAnnounce = this.myContext.getString(R.string.direction_view_down);
        this.mLeftAnnounce = this.myContext.getString(R.string.direction_view_left);
        this.mRightAnnounce = this.myContext.getString(R.string.direction_view_right);
        this.mSquare_size = dipToPixels((double) this.REFERENCE_SQUARE_DIMENSION);
        this.mLeftX = 0.0d;
        this.mRightX = 0.0d;
        this.mBottomY = 0.0d;
        this.mTopY = 0.0d;
        this.mTradeSpace = calculateTradeSpace(this.mSquare_size, DIRECTION_TOLERANCE_ANGLE);
        Log.i(this.TAG, "tradeSpace =" + this.mTradeSpace);
        this.mVibrator = (Vibrator) this.myContext.getSystemService("vibrator");
        this.mAccessibilityManager = (AccessibilityManager) this.myContext.getSystemService("accessibility");
        setImportantForAccessibility(1);
        setContentDescription(this.myContext.getString(R.string.direction_view_description_text));
    }

    public void setSettingsMode() {
        this.SETTINGS_APP = true;
    }

    public ImageView getDirectionImageView() {
        this.isParentArrowSize = false;
        return getDirectionImageView(0, 0, false, 0);
    }

    public ImageView getDirectionImageView(int arrowSize) {
        this.isParentArrowSize = true;
        return getDirectionImageView(0, 0, false, arrowSize);
    }

    public ImageView getDirectionImageView(int topMargin, int bottomMargin) {
        this.isParentArrowSize = false;
        return getDirectionImageView(topMargin, bottomMargin, true, 0);
    }

    public ImageView getDirectionImageView(int topMargin, int bottomMargin, int arrowSize) {
        this.isParentArrowSize = true;
        return getDirectionImageView(topMargin, bottomMargin, true, arrowSize);
    }

    private ImageView getDirectionImageView(int topMargin, int bottomMargin, boolean useMargins, int arrowSize) {
        Log.i(this.TAG, "in getDirectionImageView()");
        if (this.isParentArrowSize) {
            this.mParentArrowSize = arrowSize;
        }
        if (this.mDirectionImageView != null) {
            removeView(this.mDirectionImageView);
            this.mDirectionImageView = null;
        }
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        if (useMargins) {
            layoutParams.addRule(14);
            layoutParams.topMargin = topMargin;
            layoutParams.bottomMargin = bottomMargin;
        } else {
            layoutParams.addRule(13);
        }
        this.mDirectionImageView = new ImageView(this.myContext);
        addView(this.mDirectionImageView, (ViewGroup.LayoutParams) layoutParams);
        invalidate();
        return this.mDirectionImageView;
    }

    private double calculateTradeSpace(double sqaure_size, double toleranceAngle) {
        Log.i(this.TAG, "in calculateTradeSpace()");
        FOUR_CONFIGURATION_MODE = false;
        if (toleranceAngle < 90.0d) {
            return (sqaure_size / 2.0d) - (Math.tan(Math.toRadians(toleranceAngle / 2.0d)) * (sqaure_size / 2.0d));
        }
        FOUR_CONFIGURATION_MODE = true;
        return 0.0d;
    }

    private void refreshScreenDimensions() {
        Log.i(this.TAG, "in refreshScreenDimensions()");
        Rect rect = new Rect();
        getWindowVisibleDisplayFrame(rect);
        this.mScreenHeight = (double) Math.abs(rect.top - rect.bottom);
        this.mScreenWidth = (double) Math.abs(rect.right - rect.left);
        Log.i(this.TAG, "in refreshScreenDimensions() mScreenWidth = " + this.mScreenWidth + " mScreenHeight = " + this.mScreenHeight);
    }

    public void setAllow(boolean value) {
        Log.i(this.TAG, "in setAllow()");
        this.mAllow = value;
    }

    private void createReferenceSquare(double eventX, double eventY, boolean doScreenCorrection) {
        Log.i(this.TAG, "in createReferenceSquare()");
        this.mLeftX = eventX - (this.mSquare_size / 2.0d);
        this.mTopY = eventY - (this.mSquare_size / 2.0d);
        this.mRightX = (this.mSquare_size / 2.0d) + eventX;
        this.mBottomY = (this.mSquare_size / 2.0d) + eventY;
        if (doScreenCorrection) {
            if (this.mLeftX < 0.0d) {
                this.mRightX -= this.mLeftX;
                this.mLeftX = 0.0d;
            } else if (this.mRightX > this.mScreenWidth) {
                this.mLeftX -= this.mRightX - this.mScreenWidth;
                this.mRightX = this.mScreenWidth;
            }
            if (this.mTopY < 0.0d) {
                this.mBottomY -= this.mTopY;
                this.mTopY = 0.0d;
            } else if (this.mBottomY > this.mScreenHeight) {
                this.mTopY -= this.mBottomY - this.mScreenHeight;
                this.mBottomY = this.mScreenHeight;
            }
        }
        Log.i(this.TAG, "eventX = " + eventX + " eventY = " + eventY);
        Log.i(this.TAG, " mLeftX = " + this.mLeftX + "  mRightX = " + this.mRightX + " mBottomY = " + this.mBottomY + " mTopY = " + this.mTopY);
    }

    Direction getDirection(double curX, double curY, double leftX, double rightX, double topY, double bottomY) {
        Log.i(this.TAG, "getDirection() curX = " + curX + " curY = " + curY + " leftX = " + leftX + " rightX =" + rightX + " topY = " + topY + " bottomY = " + bottomY);
        Direction curDirection = Direction.INVALID;
        if (!FOUR_CONFIGURATION_MODE && curX >= rightX && curY <= topY) {
            curDirection = Direction.UP_RIGHT;
        } else if (!FOUR_CONFIGURATION_MODE && curX <= leftX && curY <= topY) {
            curDirection = Direction.UP_LEFT;
        } else if (!FOUR_CONFIGURATION_MODE && curX >= rightX && curY >= bottomY) {
            curDirection = Direction.DOWN_RIGHT;
        } else if (!FOUR_CONFIGURATION_MODE && curX <= leftX && curY >= bottomY) {
            curDirection = Direction.DOWN_LEFT;
        } else if ((FOUR_CONFIGURATION_MODE || (curX <= rightX && curX >= leftX)) && curY < topY) {
            curDirection = Direction.UP;
        } else if ((FOUR_CONFIGURATION_MODE || (curX <= rightX && curX >= leftX)) && curY > bottomY) {
            curDirection = Direction.DOWN;
        } else if ((FOUR_CONFIGURATION_MODE || (curY <= bottomY && curY >= topY)) && curX > rightX) {
            curDirection = Direction.RIGHT;
        } else if ((FOUR_CONFIGURATION_MODE || (curY <= bottomY && curY >= topY)) && curX < leftX) {
            curDirection = Direction.LEFT;
        }
        if (curDirection != Direction.INVALID) {
            this.mDirectionDetected = true;
        }
        return curDirection;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        Log.i(this.TAG, "in onConfigurationChanged()");
        refreshScreenDimensions();
        super.onConfigurationChanged(newConfig);
    }

    void handleDirectionEvent(Direction direction, boolean allowDouble) {
        boolean lengthCondition;
        Log.i(this.TAG, "in handleDirectionEvent()");
        this.mInsideReferenceSquare = false;
        if (this.mCurrentPassword.length() == 0) {
            lengthCondition = true;
        } else {
            lengthCondition = false;
        }
        boolean lastDirectionCondition = false;
        char lastDirectionChar = lengthCondition ? '\u0000' : this.mCurrentPassword.charAt(this.mCurrentPassword.length() - 1);
        Log.i(this.TAG, direction + " detected");
        this.mCurBitmap = null;
        this.mVibratePattern = -1;
        String mCurrentDirection = "";
        switch (AnonymousClass2.$SwitchMap$com$android$internal$widget$DirectionLockView$Direction[direction.ordinal()]) {
            case 5:
                lastDirectionCondition = 'U' != lastDirectionChar;
                if (lengthCondition || allowDouble || lastDirectionCondition) {
                    this.mCurrentPassword += "U";
                    this.mCurrentPasswordNumbers += SmartFaceManager.PAGE_BOTTOM;
                    if (this.mPlayVoice) {
                        this.mTextToSpeech.speak(this.mUpAnnounce, 0, this.mHashMap);
                    }
                    if (this.mPlayBeep) {
                        this.mBeepNorth.start();
                    }
                    if (this.mPlayVibration) {
                        this.mVibratePattern = HapticFeedbackConstants.VIBE_COMMON_C;
                    }
                    if (this.mShowArrow) {
                        this.mCurBitmap = this.mDirectionBitmapUp;
                    }
                    mCurrentDirection = DirectionLockTouchListener.UP;
                    break;
                }
            case 6:
                lastDirectionCondition = 'R' != lastDirectionChar;
                if (lengthCondition || allowDouble || lastDirectionCondition) {
                    this.mCurrentPassword += "R";
                    this.mCurrentPasswordNumbers += "3";
                    if (this.mPlayVoice) {
                        this.mTextToSpeech.speak(this.mRightAnnounce, 0, this.mHashMap);
                    }
                    if (this.mPlayBeep) {
                        this.mBeepEast.start();
                    }
                    if (this.mPlayVibration) {
                        this.mVibratePattern = HapticFeedbackConstants.VIBE_COMMON_G;
                    }
                    if (this.mShowArrow) {
                        this.mCurBitmap = this.mDirectionBitmapRight;
                    }
                    mCurrentDirection = DirectionLockTouchListener.RIGHT;
                    break;
                }
            case 7:
                lastDirectionCondition = 'D' != lastDirectionChar;
                if (lengthCondition || allowDouble || lastDirectionCondition) {
                    this.mCurrentPassword += "D";
                    this.mCurrentPasswordNumbers += "7";
                    if (this.mPlayVoice) {
                        this.mTextToSpeech.speak(this.mDownAnnounce, 0, this.mHashMap);
                    }
                    if (this.mPlayBeep) {
                        this.mBeepSouth.start();
                    }
                    if (this.mPlayVibration) {
                        this.mVibratePattern = HapticFeedbackConstants.VIBE_COMMON_C;
                    }
                    if (this.mShowArrow) {
                        this.mCurBitmap = this.mDirectionBitmapDown;
                    }
                    mCurrentDirection = DirectionLockTouchListener.DOWN;
                    break;
                }
            case 8:
                lastDirectionCondition = DateFormat.STANDALONE_MONTH != lastDirectionChar;
                if (lengthCondition || allowDouble || lastDirectionCondition) {
                    this.mCurrentPassword += "L";
                    this.mCurrentPasswordNumbers += "9";
                    if (this.mPlayVoice) {
                        this.mTextToSpeech.speak(this.mLeftAnnounce, 0, this.mHashMap);
                    }
                    if (this.mPlayBeep) {
                        this.mBeepWest.start();
                    }
                    if (this.mPlayVibration) {
                        this.mVibratePattern = HapticFeedbackConstants.VIBE_COMMON_G;
                    }
                    if (this.mShowArrow) {
                        this.mCurBitmap = this.mDirectionBitmapLeft;
                    }
                    mCurrentDirection = DirectionLockTouchListener.LEFT;
                    break;
                }
            default:
                Log.i(this.TAG, "Invalid Direction !");
                break;
        }
        if (lengthCondition || allowDouble || lastDirectionCondition) {
            if (this.mShowArrow) {
                this.mDirectionImageView.setImageBitmap(this.mCurBitmap);
            }
            if (this.mPlayVibration && this.mVibratePattern > 0) {
                this.mVibrator.vibrate(this.mVibratePattern, -1, null, MagnitudeTypes.NotificationMagnitude);
            }
            if (this.mDirectionLockTouchListener != null) {
                this.mDirectionLockTouchListener.onDirectionDetected(mCurrentDirection, this.mDirectionDetected);
                return;
            }
            return;
        }
        Log.i(this.TAG, direction + " ignored");
    }

    public void showErrorImage() {
        Log.i(this.TAG, "in showErrorImage()");
        this.mForceRestart = true;
        int resIDError = 0;
        if (this.mCurBitmap != null && this.mShowArrow) {
            if (this.mCurBitmap.equals(this.mDirectionBitmapLeft)) {
                resIDError = R.drawable.direction_unlock_screen_ic_arrow_left_error;
            } else if (this.mCurBitmap.equals(this.mDirectionBitmapRight)) {
                resIDError = R.drawable.direction_unlock_screen_ic_arrow_right_error;
            } else if (this.mCurBitmap.equals(this.mDirectionBitmapUp)) {
                resIDError = R.drawable.direction_unlock_screen_ic_arrow_up_error;
            } else if (this.mCurBitmap.equals(this.mDirectionBitmapDown)) {
                resIDError = R.drawable.direction_unlock_screen_ic_arrow_down_error;
            }
            if (this.currentErrorResId != resIDError || this.mErrorBitmap == null) {
                if (this.mErrorBitmap != null) {
                    this.mErrorBitmap.recycle();
                    this.mErrorBitmap = null;
                }
                this.mErrorBitmap = getBitmapFromResource(this.myContext.getResources(), resIDError, this.ARROW_SIZE, this.ARROW_SIZE);
                this.currentErrorResId = resIDError;
            }
            this.mDirectionImageView.setImageBitmap(this.mErrorBitmap);
        }
    }

    private void loadDirectionBitmaps(int arrowSize) {
        int resIDLeft;
        int resIDRight;
        int resIDUp;
        int resIDDown;
        Log.i(this.TAG, "in loadDirectionBitmaps()");
        if (!this.isParentArrowSize) {
            arrowSize = (int) dipToPixels((double) arrowSize);
        }
        this.ARROW_SIZE = arrowSize;
        if (this.SETTINGS_APP) {
            resIDLeft = R.drawable.direction_unlock_screen_ic_arrow_left;
            resIDRight = R.drawable.direction_unlock_screen_ic_arrow_right;
            resIDUp = R.drawable.direction_unlock_screen_ic_arrow_up;
            resIDDown = R.drawable.direction_unlock_screen_ic_arrow_down;
        } else {
            resIDLeft = R.drawable.direction_unlock_lockscreen_ic_arrow_left;
            resIDRight = R.drawable.direction_unlock_lockscreen_ic_arrow_right;
            resIDUp = R.drawable.direction_unlock_lockscreen_ic_arrow_up;
            resIDDown = R.drawable.direction_unlock_lockscreen_ic_arrow_down;
        }
        this.mDirectionBitmapLeft = getBitmapFromResource(this.myContext.getResources(), resIDLeft, this.ARROW_SIZE, this.ARROW_SIZE);
        this.mDirectionBitmapRight = getBitmapFromResource(this.myContext.getResources(), resIDRight, this.ARROW_SIZE, this.ARROW_SIZE);
        this.mDirectionBitmapUp = getBitmapFromResource(this.myContext.getResources(), resIDUp, this.ARROW_SIZE, this.ARROW_SIZE);
        this.mDirectionBitmapDown = getBitmapFromResource(this.myContext.getResources(), resIDDown, this.ARROW_SIZE, this.ARROW_SIZE);
    }

    private void freeBitmapMemory() {
        Log.i(this.TAG, "in freeBitmapMemory()");
        if (this.mBlankBitmap != null) {
            this.mBlankBitmap.recycle();
            this.mBlankBitmap = null;
        }
        releaseDirectionBitmaps();
        if (this.mCurBitmap != null) {
            this.mCurBitmap.recycle();
            this.mCurBitmap = null;
        }
        if (this.mErrorBitmap != null) {
            this.mErrorBitmap.recycle();
            this.mErrorBitmap = null;
        }
    }

    private Bitmap getBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Log.i(this.TAG, "getBitmapFromResource start  reqWidth:" + reqWidth + " reqHeight:" + reqHeight + "  resid:" + resId);
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap tempBitmap = BitmapFactory.decodeResource(res, resId, options);
        Bitmap returnBitmap = null;
        if (tempBitmap != null) {
            try {
                returnBitmap = Bitmap.createScaledBitmap(tempBitmap, reqWidth, reqHeight, true);
                if (!tempBitmap.equals(returnBitmap)) {
                    Log.i(this.TAG, "getBitmapFromResource recycle");
                    tempBitmap.recycle();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                if (!tempBitmap.equals(returnBitmap)) {
                    Log.i(this.TAG, "getBitmapFromResource recycle");
                    tempBitmap.recycle();
                }
            } catch (Throwable th) {
                if (!tempBitmap.equals(returnBitmap)) {
                    Log.i(this.TAG, "getBitmapFromResource recycle");
                    tempBitmap.recycle();
                }
            }
        }
        Log.i(this.TAG, "getBitmapFromResource End");
        return returnBitmap;
    }

    private int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void releaseDirectionBitmaps() {
        if (this.mDirectionBitmapUp != null) {
            this.mDirectionBitmapUp.recycle();
            this.mDirectionBitmapUp = null;
        }
        if (this.mDirectionBitmapRight != null) {
            this.mDirectionBitmapRight.recycle();
            this.mDirectionBitmapRight = null;
        }
        if (this.mDirectionBitmapLeft != null) {
            this.mDirectionBitmapLeft.recycle();
            this.mDirectionBitmapLeft = null;
        }
        if (this.mDirectionBitmapDown != null) {
            this.mDirectionBitmapDown.recycle();
            this.mDirectionBitmapDown = null;
        }
    }

    public void reloadBitmap() {
        Log.i(this.TAG, "in reloadBitmap()");
        releaseDirectionBitmaps();
        if (this.mShowArrow) {
            loadDirectionBitmaps(this.isParentArrowSize ? this.mParentArrowSize : DEFAULT_ARROW_SIZE);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(this.TAG, "in onAttached()");
        if (this.mShowArrow) {
            loadDirectionBitmaps(this.isParentArrowSize ? this.mParentArrowSize : DEFAULT_ARROW_SIZE);
        }
        Secure.putInt(this.myContext.getContentResolver(), "direction_lock_set", 1);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(this.TAG, "in onDetached()");
        freeBitmapMemory();
        Secure.putInt(this.myContext.getContentResolver(), "direction_lock_set", 0);
        if (this.mTextToSpeech != null) {
            this.mTextToSpeech.shutdown();
            this.mTextToSpeech = null;
        }
        if (this.mBeepNorth != null) {
            if (this.mBeepNorth.isPlaying()) {
                this.mBeepNorth.stop();
            }
            this.mBeepNorth.release();
            this.mBeepNorth = null;
        }
        if (this.mBeepSouth != null) {
            if (this.mBeepSouth.isPlaying()) {
                this.mBeepSouth.stop();
            }
            this.mBeepSouth.release();
            this.mBeepSouth = null;
        }
        if (this.mBeepEast != null) {
            if (this.mBeepEast.isPlaying()) {
                this.mBeepEast.stop();
            }
            this.mBeepEast.release();
            this.mBeepEast = null;
        }
        if (this.mBeepWest != null) {
            if (this.mBeepWest.isPlaying()) {
                this.mBeepWest.stop();
            }
            this.mBeepWest.release();
            this.mBeepWest = null;
        }
    }
}
