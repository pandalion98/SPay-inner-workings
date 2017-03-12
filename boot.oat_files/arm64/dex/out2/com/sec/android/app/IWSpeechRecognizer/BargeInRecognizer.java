package com.sec.android.app.IWSpeechRecognizer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;

public class BargeInRecognizer {
    public static final int STATE_READY = 0;
    public static final int STATE_RUNNING = 1;
    private static final String TAG = BargeInRecognizer.class.getSimpleName();
    private final String SVOICE_LANGUAGE_FILE;
    private AudioTask audio;
    private Thread audio_thread;
    private Handler handler;
    private int intBargeInResult;
    public boolean isEnableBargeIn;
    private boolean isEnableChineseBargeIn;
    private boolean isEnableExtraRussian;
    private boolean isEnableExtraSpanish;
    private Context mContext;
    private IWSpeechRecognizerListener mListener;
    private int mState;
    private Handler mStopHandler;
    private boolean samsungOOVResult;
    private int uselanguage;

    public BargeInRecognizer() {
        this.audio = null;
        this.audio_thread = null;
        this.mListener = null;
        this.mState = 0;
        this.isEnableBargeIn = false;
        this.isEnableChineseBargeIn = false;
        this.isEnableExtraSpanish = false;
        this.isEnableExtraRussian = false;
        this.samsungOOVResult = false;
        this.intBargeInResult = -1;
        this.uselanguage = 1;
        this.handler = null;
        this.mStopHandler = null;
        this.mContext = null;
        this.SVOICE_LANGUAGE_FILE = "/data/data/com.vlingo.midas/files/language.bin";
        this.mContext = null;
        init();
    }

    public BargeInRecognizer(Context ctx) {
        this.audio = null;
        this.audio_thread = null;
        this.mListener = null;
        this.mState = 0;
        this.isEnableBargeIn = false;
        this.isEnableChineseBargeIn = false;
        this.isEnableExtraSpanish = false;
        this.isEnableExtraRussian = false;
        this.samsungOOVResult = false;
        this.intBargeInResult = -1;
        this.uselanguage = 1;
        this.handler = null;
        this.mStopHandler = null;
        this.mContext = null;
        this.SVOICE_LANGUAGE_FILE = "/data/data/com.vlingo.midas/files/language.bin";
        this.mContext = ctx;
        Log.i(TAG, "BargeInRecognizer get Context " + this.mContext);
        init();
    }

    private void init() {
        Log.i(TAG, "make new BargeInRecognizer VER 15.11.9");
        this.isEnableBargeIn = isUseModel();
        this.isEnableChineseBargeIn = isUseChineseModel();
        this.isEnableExtraSpanish = isBargeInFile("/system/voicebargeindata/include/bargein_language_extra_es");
        this.isEnableExtraRussian = isBargeInFile("/system/voicebargeindata/include/bargein_language_extra_ru");
        setLanguage();
        this.mState = 0;
        Log.i(TAG, "isEnableBargeIn : " + this.isEnableBargeIn);
        Log.i(TAG, "uselanguage : " + this.uselanguage);
        Log.i(TAG, "isEnableChineseBargeIn : " + this.isEnableChineseBargeIn);
        Log.i(TAG, "isEnableExtraSpanish : " + this.isEnableExtraSpanish);
        Log.i(TAG, "isEnableExtraRussian : " + this.isEnableExtraRussian);
        Log.i(TAG, "mState : " + this.mState);
    }

    public void InitBargeInRecognizer(IWSpeechRecognizerListener listener) {
        this.mListener = listener;
        this.mState = 0;
    }

    public void setContext(Context context) {
        Log.i(TAG, "setContext");
        this.mContext = context;
    }

    public int getState() {
        Log.i(TAG, "getState mState : " + this.mState);
        return this.mState;
    }

    private void SendHandlerMessage(int type) {
        if (this.handler != null) {
            Message msg = this.handler.obtainMessage();
            Bundle b = new Bundle();
            b.putInt("commandType", type);
            msg.setData(b);
            if (type == 2) {
                Log.d(TAG, "sendMessageDelayed : 1500");
                this.handler.sendMessageDelayed(msg, 1500);
                return;
            }
            Log.d(TAG, "sendMessageDelayed : 700");
            this.handler.sendMessageDelayed(msg, 700);
        }
    }

    private void start(int commandType) {
        Log.i(TAG, "start");
        if (isBargeInEnabled(commandType)) {
            this.mState = 1;
            Log.d(TAG, "mState change to : " + this.mState);
            if (this.mStopHandler == null) {
                this.mStopHandler = new Handler(Looper.getMainLooper()) {
                    public void handleMessage(Message msg) {
                        Log.e(BargeInRecognizer.TAG, "audio is halt without stopBargeIn()");
                        BargeInRecognizer.this.stopBargeIn();
                    }
                };
                Log.d(TAG, "StopHandler create");
            }
            if (this.handler == null) {
                this.handler = new Handler(Looper.getMainLooper()) {
                    public void handleMessage(Message msg) {
                        BargeInRecognizer.this.delayedStartBargeIn(msg.getData().getInt("commandType"), BargeInRecognizer.this.mStopHandler);
                    }
                };
                Log.d(TAG, "handler create");
            }
            SendHandlerMessage(commandType);
        }
    }

    public void startBargeIn(int commandType) {
        Log.i(TAG, "startBargeIn");
        Log.i(TAG, "commandType : " + commandType);
        this.intBargeInResult = -1;
        setLanguage();
        start(commandType);
    }

    public void startBargeIn(int commandType, int setLang) {
        Log.i(TAG, "startBargeIn Type2");
        Log.i(TAG, "commandType : " + commandType);
        Log.i(TAG, "setLanguage : " + setLang);
        this.intBargeInResult = -1;
        this.uselanguage = setLang;
        start(commandType);
    }

    private void delayedStartBargeIn(int commandType, Handler stopHandler) {
        Log.i(TAG, "delayedStartBargeIn");
        synchronized (this) {
            if (this.audio != null) {
                Log.w(TAG, "BargeIn is running. So Do nothing");
                this.audio.BargeinAct[0] = (short) -1;
            } else {
                this.audio = new AudioTask(this.mListener, Config.DEFAULT_PATH, commandType, this.uselanguage, this.samsungOOVResult);
                if (this.audio == null || this.audio.rec == null) {
                    Log.e(TAG, "fail to running Bargein");
                    if (this.audio != null) {
                        this.audio.stop();
                    }
                    if (this.audio_thread != null) {
                        Log.e(TAG, "why running empty audio_thread");
                    }
                    this.audio = null;
                } else {
                    this.audio.setHandler(stopHandler);
                    this.audio_thread = new Thread(this.audio);
                    this.audio_thread.start();
                    this.mState = 1;
                    Log.d(TAG, "mState change to : " + this.mState);
                }
            }
        }
    }

    public void stopBargeIn() {
        Log.i(TAG, "stopBargeIn");
        synchronized (this) {
            if (this.isEnableBargeIn) {
                if (this.handler != null) {
                    this.handler.removeMessages(0);
                    this.handler = null;
                    Log.d(TAG, "handler = null");
                }
                if (this.mStopHandler != null) {
                    this.mStopHandler.removeMessages(0);
                    this.mStopHandler = null;
                    Log.d(TAG, "Stop Handler = null");
                }
                if (this.audio != null) {
                    this.intBargeInResult = this.audio.BargeinAct[0];
                    this.audio.stop();
                    if (this.audio_thread != null) {
                        try {
                            Log.d(TAG, "wait for audio to stop: begin");
                            this.audio_thread.join(700);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        Log.d(TAG, "audio_thread was not working");
                    }
                    Log.d(TAG, "wait for audio to stop: end");
                    this.audio = null;
                    Log.d(TAG, "audio = null");
                }
                this.audio_thread = null;
                Log.d(TAG, "audio_thread = null");
                this.mState = 0;
                Log.d(TAG, "mState change to : " + this.mState);
            }
        }
    }

    private boolean isUseModel() {
        if (isSamsungModel()) {
            this.samsungOOVResult = true;
            return true;
        } else if (isSensoryModel()) {
            this.samsungOOVResult = false;
            Log.i(TAG, "Could not find libsasr-jni.so use only libSensoryBargeInEngine.so");
            return true;
        } else {
            Log.e(TAG, "Error : Could not find libsasr-jni.so && libSensoryBargeInEngine.so");
            return false;
        }
    }

    private static boolean isSamsungModel() {
        if (isBargeInFile(Config.SAMSUNG_SO_FILE_PATH) && isBargeInFile(Config.GetSamsungModels(1)) && isBargeInFile(Config.GetSamsungModels(0))) {
            return true;
        }
        return false;
    }

    private static boolean isSensoryModel() {
        if (isBargeInFile(Config.SENSORY_SO_FILE_PATH) || isBargeInFile(Config.SENSORY_SO_FILE_PATH_64)) {
            return true;
        }
        return false;
    }

    public boolean isUseChineseModel() {
        if (isBargeInFile(Config.GetSamsungModels(2))) {
            return true;
        }
        return false;
    }

    public String getBargeInCmdLanguage() {
        switch (this.uselanguage) {
            case 0:
                return "ko-KR";
            case 1:
                return "en-US";
            case 2:
                return "zh-CN";
            case 3:
                return "es-ES";
            case 4:
                return "fr-FR";
            case 5:
                return "de-DE";
            case 6:
                return "it-IT";
            case 7:
                return "ja-JP";
            case 8:
                return "ru-RU";
            case 9:
                return "pt-BR";
            case 10:
                return "en-GB";
            case 11:
                return "v-es-LA";
            case 12:
                return "zh-TW";
            case 13:
                return "zh-HK";
            default:
                return "en-US";
        }
    }

    public String[] getBargeInCmdStringArray(int CommandType) {
        return getBargeInCmdStringArray(CommandType, this.uselanguage);
    }

    public String[] getBargeInCmdStringArray(int CommandType, int Language) {
        Log.i(TAG, "getBargeInCmdStringArray : CommandType ( " + CommandType + " ) Language ( " + Language + " )");
        if (Language >= 15) {
            Language = 1;
        }
        if (!isBargeInEnabled(CommandType, Language)) {
            Language = 1;
            Log.i(TAG, "getBargeInCmdStringArray : possible language is ( " + 1 + " )");
        }
        switch (CommandType) {
            case 2:
                return CommandLanguage.CALL[Language];
            case 3:
                return CommandLanguage.ALARM[Language];
            case 4:
                return CommandLanguage.MUSIC[Language];
            case 7:
                return CommandLanguage.CAMERA[Language];
            case 9:
                return CommandLanguage.CANCEL[Language];
            default:
                return null;
        }
    }

    public int getIntBargeInCmdLanguage() {
        Log.i(TAG, "getIntBargeInCmdLanguage : " + this.uselanguage);
        return this.uselanguage;
    }

    private void setLanguage() {
        String defaultLanguage = Locale.getDefault().toString();
        String stringLanguage = Locale.getDefault().getLanguage();
        String stringCountry = Locale.getDefault().getCountry();
        String sVoiceLanguage = null;
        Log.i(TAG, "stringLanguage : " + stringLanguage);
        Log.i(TAG, "stringCountry : " + stringCountry);
        Log.i(TAG, "sVoiceLanguage : " + sVoiceLanguage);
        if (sVoiceLanguage != null) {
            if (sVoiceLanguage.equals("ko-KR")) {
                this.uselanguage = 0;
            } else if (sVoiceLanguage.equals("en-US")) {
                this.uselanguage = 1;
            } else if (sVoiceLanguage.equals("zh-CN") && this.isEnableChineseBargeIn) {
                this.uselanguage = 2;
            } else if (sVoiceLanguage.equals("es-ES")) {
                this.uselanguage = 3;
            } else if (sVoiceLanguage.equals("v-es-LA")) {
                this.uselanguage = 11;
            } else if (sVoiceLanguage.equals("fr-FR")) {
                this.uselanguage = 4;
            } else if (sVoiceLanguage.equals("de-DE")) {
                this.uselanguage = 5;
            } else if (sVoiceLanguage.equals("it-IT")) {
                this.uselanguage = 6;
            } else if (sVoiceLanguage.equals("ja-JP")) {
                this.uselanguage = 7;
            } else if (sVoiceLanguage.equals("ru-RU")) {
                this.uselanguage = 8;
            } else if (sVoiceLanguage.equals("pt-BR")) {
                this.uselanguage = 9;
            } else if (sVoiceLanguage.equals("en-GB")) {
                this.uselanguage = 10;
            }
        } else if (stringLanguage == null) {
        } else {
            if (stringLanguage.equals(Locale.KOREA.getLanguage())) {
                this.uselanguage = 0;
            } else if (stringLanguage.equals(Locale.US.getLanguage())) {
                if (stringCountry.equals("GB")) {
                    this.uselanguage = 10;
                } else {
                    this.uselanguage = 1;
                }
            } else if (stringLanguage.equals(Locale.CHINA.getLanguage()) && this.isEnableChineseBargeIn) {
                if (stringCountry.equals("CN")) {
                    this.uselanguage = 2;
                } else if (stringCountry.equals("TW")) {
                    this.uselanguage = 12;
                } else if (stringCountry.equals("HK")) {
                    this.uselanguage = 13;
                } else if (stringCountry.equals("SG")) {
                    this.uselanguage = 14;
                } else {
                    this.uselanguage = 1;
                }
            } else if (stringCountry.equals("ES")) {
                this.uselanguage = 3;
                if (this.isEnableExtraSpanish || stringLanguage.equals("es")) {
                    Log.i(TAG, "Extra Sapnish is enabled : " + defaultLanguage);
                } else {
                    this.uselanguage = 1;
                }
            } else if (stringLanguage.equals("es")) {
                this.uselanguage = 11;
            } else if (stringLanguage.equals(Locale.FRANCE.getLanguage())) {
                this.uselanguage = 4;
            } else if (stringLanguage.equals(Locale.GERMAN.getLanguage())) {
                this.uselanguage = 5;
            } else if (stringLanguage.equals(Locale.ITALY.getLanguage())) {
                this.uselanguage = 6;
            } else if (stringLanguage.equals(Locale.JAPAN.getLanguage())) {
                this.uselanguage = 7;
            } else if (stringLanguage.equals("ru")) {
                this.uselanguage = 8;
            } else if (stringLanguage.equals("pt")) {
                if (stringCountry.equals("BR")) {
                    this.uselanguage = 9;
                } else {
                    this.uselanguage = 1;
                }
            } else if (!this.isEnableExtraRussian) {
                this.uselanguage = 1;
            } else if (defaultLanguage.contains("az_AZ") || defaultLanguage.contains("kk_KZ") || defaultLanguage.contains("uz_UZ") || defaultLanguage.equals("ky_KZ") || defaultLanguage.equals("tg_TJ") || defaultLanguage.equals("tk_TM") || defaultLanguage.equals("be_BY")) {
                this.uselanguage = 8;
                Log.i(TAG, "Extra Russian is enabled : " + defaultLanguage);
            } else {
                this.uselanguage = 1;
            }
        }
    }

    public int getIntBargeInResult() {
        if (this.audio != null) {
            return this.audio.BargeinAct[0];
        }
        return this.intBargeInResult;
    }

    private String readString(String filePath) {
        IOException e;
        File mFile = new File(filePath);
        FileInputStream mFileInputStream = null;
        if (!mFile.exists()) {
            return null;
        }
        try {
            FileInputStream mFileInputStream2 = new FileInputStream(mFile);
            try {
                byte[] data = new byte[mFileInputStream2.available()];
                mFileInputStream2.read(data);
                mFileInputStream2.close();
                mFileInputStream = mFileInputStream2;
                return new String(data);
            } catch (IOException e2) {
                e = e2;
                mFileInputStream = mFileInputStream2;
                if (mFileInputStream != null) {
                    try {
                        mFileInputStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                e.printStackTrace();
                return null;
            }
        } catch (IOException e3) {
            e = e3;
            if (mFileInputStream != null) {
                mFileInputStream.close();
            }
            e.printStackTrace();
            return null;
        }
    }

    public boolean isBargeInEnabled() {
        return this.isEnableBargeIn;
    }

    public boolean isBargeInEnabled(int commandType) {
        int language = this.uselanguage;
        if (isBargeInEnabled(commandType, language)) {
            return true;
        }
        if (language == 1) {
            return false;
        }
        this.uselanguage = 1;
        return isBargeInEnabled(commandType, this.uselanguage);
    }

    public boolean isBargeInEnabled(int commandType, int language) {
        if (isSensoryModel()) {
            String sensoryModelPath = Config.GetSensoryAM(language, commandType);
            String sensoryGrammarPath = Config.GetSensoryGRAMMAR(language, commandType);
            sensoryModelPath = sensoryModelPath + Config.SENSORY_MAIN_SUFFIX;
            sensoryGrammarPath = sensoryGrammarPath + Config.SENSORY_MAIN_SUFFIX;
            if (isBargeInFile(sensoryModelPath) && isBargeInFile(sensoryGrammarPath)) {
                Log.i(TAG, "isBargeInEnabled: SensoryBargeIn is available in commandType (" + commandType + ") uselanguage(" + language + ")");
                return true;
            }
        }
        if (isSamsungModel()) {
            String samsungModelPath = Config.GetSamsungModels(language);
            String samsungNameList = Config.GetSamsungPath(language) + Config.GetSamsungNameList(commandType);
            if (isBargeInFile(samsungModelPath) && isBargeInFile(samsungNameList)) {
                Log.i(TAG, "isBargeInEnabled: SamsungBargeIn is available in commandType (" + commandType + ") uselanguage(" + language + ")");
                return true;
            }
        }
        Log.w(TAG, "isBargeInEnabled: BargeIn is not available in commandType (" + commandType + ") uselanguage(" + language + ")");
        return false;
    }

    private static boolean isBargeInFile(String mFilePath) {
        if (new File(mFilePath).exists()) {
            return true;
        }
        return false;
    }
}
