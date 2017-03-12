package com.sec.android.app.IWSpeechRecognizer;

import android.media.AudioRecord;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.sensoryinc.fluentsoftsdk.SensoryBargeInEngine;
import com.sensoryinc.fluentsoftsdk.SensoryBargeInEngineWrapper;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

class AudioTask implements Runnable {
    static final int DEFAULT_BLOCK_SIZE = 160;
    private int AUDIO_RECORD_FOR_BARGE_IN = 11;
    private int AUDIO_RECORD_FOR_BARGE_IN_SENSORY = 19;
    private int AUDIO_RECORD_FOR_VOICE_RECOGNITION = 6;
    private int AUDIO_START = 0;
    public short[] BargeinAct = new short[]{(short) -1};
    private final int RECOGNITION_WAIT_TIME = 100;
    public String TAG = AudioTask.class.getSimpleName();
    public double THscore = -1.5d;
    private MMUIRecognizer aMMUIRecognizer = null;
    private SensoryBargeInEngine aSensoryBargeInEngine = null;
    private String acousticModelPathname = (Config.GetSensoryAM(0, 2) + Config.SENSORY_MAIN_SUFFIX);
    private String acousticModelPathname_sub = (Config.GetSensoryAM(0, 2) + Config.SENSORY_SUB_SUFFIX);
    public int block_size = 0;
    public byte[] buf;
    public float[] cmResult = new float[]{0.0f};
    public long consoleInitReturn = -1;
    public long consoleInitReturn_sub = -1;
    public String defaultloadNameList = Config.GetSamsungNameList(0);
    public boolean done = false;
    private int dualThresholdFlag = 0;
    public File f = null;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String[] result = msg.getData().getStringArray("recognition_result");
            if (AudioTask.this.m_listener != null) {
                AudioTask.this.m_listener.onResults(result);
            }
        }
    };
    public boolean isCameraBargeIn = false;
    public boolean isCancelBargeIn = false;
    public boolean isEnableSamsungOOVResult = false;
    private boolean isMakePCM = false;
    public boolean isSASRInitProblem = false;
    public boolean isSensoryBargeInEnable = false;
    public boolean isSensoryCameraBargeIn = false;
    public boolean isSensoryResult = false;
    public boolean isSubModelBargeInEnable = false;
    public String loadNameList = Config.GetSamsungNameList(0);
    public String loadPath = null;
    public int mCommandType = 0;
    public int mEmbeddedEngineLanguage = 1;
    public FileOutputStream mFileOutputStream = null;
    public int mLanguage = 1;
    public Handler mStopHandler = null;
    private IWSpeechRecognizerListener m_listener = null;
    public String modelPath = (Config.GetSamsungPath(1) + "param");
    public int numRecogResult = 0;
    public LinkedBlockingQueue<short[]> q = null;
    private int readNshorts = -1;
    public AudioRecord rec = null;
    private int recogAfterReadCount = 0;
    public int resultSASRInit = 0;
    public int resultSASRLoadModel = 0;
    private String searchGrammarPathname = (Config.GetSensoryGRAMMAR(0, 2) + Config.SENSORY_MAIN_SUFFIX);
    private String searchGrammarPathname_sub = (Config.GetSensoryGRAMMAR(0, 2) + Config.SENSORY_SUB_SUFFIX);
    public float sensoryCMscore = 0.0f;
    public float sensoryCantoneseRejectCMTH = 300.0f;
    public float sensoryChineseCaptureCMTH = 800.0f;
    public float sensoryChineseStopCMTH = 130.0f;
    public float sensoryJapaneseShootCMTH = 400.0f;
    public float sensoryKoreanCancelCMTH = 150.0f;
    public float sensoryKoreanRejectCMTH = 100.0f;
    public float sensoryKoreanShootCMTH = 400.0f;
    public float sensoryKoreanStopCMTH = 150.0f;
    public float sensoryRussianCheeseCMTH = 300.0f;
    public float sensoryUKEnglishStopCMTH = 400.0f;
    public float sensoryUSEnglishCaptureCMTH = 450.0f;
    public float sensoryUSEnglishCheeseCMTH = 400.0f;
    public float sensoryUSEnglishRecordVideoCMTH = 250.0f;
    public float sensoryUSEnglishShootCMTH = 150.0f;
    public float sensoryUSEnglishSnoozeCMTH = 100.0f;
    public float sensoryUSEnglishStopCMTH = 400.0f;
    public short[] speech = null;
    public String[] strResult = new String[3];
    private int totalReadCount = 0;
    public String[] utfResult = new String[1];
    public String wordListPath = Config.GetSamsungPath(1);

    AudioTask(IWSpeechRecognizerListener listener, String path, int command, int language, boolean samsungOOVResult) {
        init(new LinkedBlockingQueue(), DEFAULT_BLOCK_SIZE, listener, path, command, language, samsungOOVResult);
    }

    void init(LinkedBlockingQueue<short[]> q, int block_size, IWSpeechRecognizerListener listener, String path, int command, int Language, boolean samsungOOVResult) {
        Log.i(this.TAG, "init()");
        Log.i(this.TAG, "command : " + command);
        Log.i(this.TAG, "Language : " + Language);
        this.done = false;
        this.q = q;
        this.block_size = block_size;
        this.mCommandType = command;
        this.rec = null;
        if (command == 7 && Language == 0) {
            this.dualThresholdFlag = -1;
        }
        setSensoryFilePath(Language, command);
        this.isEnableSamsungOOVResult = samsungOOVResult;
        Log.i(this.TAG, "isSensoryBargeInEnable : " + this.isSensoryBargeInEnable);
        Log.i(this.TAG, "isEnableSamsungOOVResult : " + this.isEnableSamsungOOVResult);
        if (this.isCameraBargeIn || this.isCancelBargeIn) {
            this.AUDIO_START = 0;
            this.rec = getAudioRecord(this.AUDIO_RECORD_FOR_VOICE_RECOGNITION);
            if (this.rec != null) {
                Log.d(this.TAG, "new AudioRecord : " + this.AUDIO_RECORD_FOR_VOICE_RECOGNITION);
            }
        } else if (this.isSensoryBargeInEnable) {
            this.AUDIO_START = 50;
            this.rec = getAudioRecord(this.AUDIO_RECORD_FOR_BARGE_IN_SENSORY);
            if (this.rec != null) {
                Log.d(this.TAG, "new AudioRecord : " + this.AUDIO_RECORD_FOR_BARGE_IN_SENSORY);
            }
        }
        if (this.rec == null) {
            this.rec = getAudioRecord(this.AUDIO_RECORD_FOR_BARGE_IN);
            Log.d(this.TAG, "new AudioRecord : " + this.AUDIO_RECORD_FOR_BARGE_IN);
        }
        this.m_listener = listener;
        this.loadPath = path;
        this.mLanguage = Language;
        setEmbeddedEngineLanguage();
        this.BargeinAct[0] = (short) -1;
        this.totalReadCount = 0;
        this.recogAfterReadCount = 0;
        if (this.isMakePCM) {
            this.f = new File("/data/log", "testPCM.pcm");
            try {
                this.mFileOutputStream = new FileOutputStream(this.f, true);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }
        this.speech = new short[DEFAULT_BLOCK_SIZE];
        this.buf = new byte[(this.block_size * 2)];
        if (this.isSensoryBargeInEnable) {
            this.aSensoryBargeInEngine = SensoryBargeInEngineWrapper.getInstance();
            this.consoleInitReturn = this.aSensoryBargeInEngine.phrasespotInit(this.acousticModelPathname, this.searchGrammarPathname);
            if (this.isSubModelBargeInEnable) {
                this.consoleInitReturn_sub = this.aSensoryBargeInEngine.phrasespotInit(this.acousticModelPathname_sub, this.searchGrammarPathname_sub);
            }
        }
        if ((this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) || !this.isSensoryBargeInEnable) {
            this.aMMUIRecognizer = IWSpeechRecognizerWrapper.getInstance();
            if (this.aMMUIRecognizer != null) {
                this.aMMUIRecognizer.SetSRLanguage(this.mEmbeddedEngineLanguage);
            }
            setSamsungFilePath(this.mEmbeddedEngineLanguage, command);
            Log.d(this.TAG, "Load Model");
            if (this.aMMUIRecognizer != null) {
                this.resultSASRLoadModel = this.aMMUIRecognizer.SASRLoadModel(this.modelPath);
                if (this.resultSASRLoadModel == 0) {
                    this.isSASRInitProblem = true;
                }
            }
            Log.d(this.TAG, "Load Model result : " + this.resultSASRLoadModel);
            if (isBargeInFile(this.wordListPath + this.loadNameList)) {
                Log.d(this.TAG, "Wordlist is " + this.loadNameList);
            } else {
                Log.d(this.TAG, "Wordlist is not exist. So set default wordlist");
                this.loadNameList = this.defaultloadNameList;
            }
            Log.d(this.TAG, "Load Wordlist");
            if (this.aMMUIRecognizer != null) {
                if (!this.isSASRInitProblem) {
                    this.resultSASRInit = this.aMMUIRecognizer.SASRInit(this.wordListPath + this.loadNameList);
                }
                if (this.resultSASRInit == 0) {
                    this.isSASRInitProblem = true;
                }
            }
            Log.d(this.TAG, "Load Wordlist result : " + this.resultSASRInit);
            if (!(this.aMMUIRecognizer == null || this.isSASRInitProblem)) {
                this.aMMUIRecognizer.SASRReset();
            }
        }
        Log.d(this.TAG, "resultSASRLoadModel : " + this.resultSASRLoadModel);
        Log.d(this.TAG, "resultSASRInit : " + this.resultSASRInit);
        Log.d(this.TAG, "isSASRInitProblem : " + this.isSASRInitProblem);
    }

    public int getBlockSize() {
        return this.block_size;
    }

    public void setBlockSize(int block_size) {
        this.block_size = block_size;
    }

    public LinkedBlockingQueue<short[]> getQueue() {
        return this.q;
    }

    public void stop() {
        Log.i(this.TAG, "AudioTask : stop start");
        this.mStopHandler = null;
        this.done = true;
        this.readNshorts = -1;
        Log.i(this.TAG, "AudioTask : stop end");
    }

    public void stopBargeInAudioRecord() {
        Log.i(this.TAG, "stopBargeInAudioRecord start");
        if (this.rec != null) {
            Log.d(this.TAG, "Call rec.stop start");
            this.rec.stop();
            Log.d(this.TAG, "Call rec.stop end");
            Log.d(this.TAG, "Call rec.release start");
            this.rec.release();
            Log.d(this.TAG, "Call rec.release end");
            this.rec = null;
            Log.d(this.TAG, "rec = null");
        }
        Log.i(this.TAG, "stopBargeInAudioRecord end");
    }

    public void run() {
        Log.i(this.TAG, "run start");
        if (this.rec != null) {
            Log.d(this.TAG, "Call rec.startRecording start");
            this.rec.startRecording();
            Log.d(this.TAG, "Call startRecording end");
            while (!this.done) {
                readByteBlock();
                if (!this.done) {
                    if (this.readNshorts <= 0) {
                        break;
                    }
                }
                break;
            }
        }
        Log.e(this.TAG, "Bargein fail to start");
        stopBargeInAudioRecord();
        if (this.aMMUIRecognizer != null) {
            Log.i(this.TAG, "SASRClose start");
            if (!this.isSASRInitProblem) {
                Log.d(this.TAG, "SASRCloseReturn : " + this.aMMUIRecognizer.SASRClose());
            }
            Log.i(this.TAG, "SASRClose end");
        }
        if (this.aSensoryBargeInEngine != null) {
            Log.i(this.TAG, "phrasespotClose start");
            if (this.consoleInitReturn != -1) {
                this.aSensoryBargeInEngine.phrasespotClose(this.consoleInitReturn);
            }
            if (this.isSubModelBargeInEnable && this.consoleInitReturn_sub != -1) {
                this.aSensoryBargeInEngine.phrasespotClose(this.consoleInitReturn_sub);
            }
            Log.i(this.TAG, "phrasespotClose end");
        }
        this.aMMUIRecognizer = null;
        this.aSensoryBargeInEngine = null;
        this.m_listener = null;
        Log.d(this.TAG, "aMMUIRecognizer = null");
        Log.d(this.TAG, "aSensoryBargeInEngine = null");
        Log.d(this.TAG, "m_listener = null");
        Log.i(this.TAG, "run end");
        if (!this.done && this.mStopHandler != null) {
            this.mStopHandler.sendEmptyMessage(0);
        }
    }

    int readByteBlock() {
        if (this.isSASRInitProblem) {
            Log.e(this.TAG, "readByteBlock return -1 : isSASRInitProblem");
            this.readNshorts = -1;
            return -1;
        } else if (this.done) {
            Log.e(this.TAG, "readByteBlock return -1 : Section1");
            this.readNshorts = -1;
            return -1;
        } else {
            if (!(this.rec == null || this.done)) {
                this.readNshorts = this.rec.read(this.buf, 0, this.buf.length);
            }
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : Section2");
                this.readNshorts = -1;
                return -1;
            }
            if (this.readNshorts < 320) {
                Log.e(this.TAG, "AudioRecord Read problem : nshorts = " + this.readNshorts + " command = " + this.mCommandType + " language : " + this.mLanguage);
            }
            if (this.totalReadCount % 20 == 0) {
                Log.d(this.TAG, "nshorts = " + (this.readNshorts * 10) + " command = " + this.mCommandType + " language : " + this.mLanguage + " dualThr : " + this.dualThresholdFlag);
            }
            this.totalReadCount++;
            if (this.recogAfterReadCount != 0) {
                this.recogAfterReadCount = (this.recogAfterReadCount + 1) % 100;
            }
            for (int i = 0; i < 320; i += 2) {
                this.speech[i / 2] = twoBytesToShort(this.buf[i], this.buf[i + 1]);
            }
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : Section3");
                this.readNshorts = -1;
                return -1;
            }
            if (this.readNshorts > 0) {
                if (this.isMakePCM) {
                    try {
                        this.mFileOutputStream.write(this.buf);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (this.done) {
                    Log.e(this.TAG, "readByteBlock return -1 : Section4");
                    this.readNshorts = -1;
                    return -1;
                }
                if (this.isSensoryBargeInEnable) {
                    if (this.done) {
                        Log.e(this.TAG, "readByteBlock return -1 : Section5");
                        this.readNshorts = -1;
                        return -1;
                    } else if (this.aSensoryBargeInEngine != null && this.totalReadCount > this.AUDIO_START) {
                        boolean successRecog = getSensoryRecognitionResult(this.consoleInitReturn, this.speech);
                        if (this.isSubModelBargeInEnable && getSensoryRecognitionResult(this.consoleInitReturn_sub, this.speech)) {
                            Log.i(this.TAG, "It is Recognized by sub Model");
                        }
                    }
                }
                if (this.totalReadCount > 50 && ((this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) || !this.isSensoryBargeInEnable)) {
                    if (this.done) {
                        Log.e(this.TAG, "readByteBlock return -1 : Section6");
                        this.readNshorts = -1;
                        return -1;
                    } else if (this.aMMUIRecognizer != null) {
                        this.readNshorts = getMMUIRecognitionResult(this.speech, this.readNshorts);
                        if (this.readNshorts == -1) {
                            return -1;
                        }
                    }
                }
            }
            Log.i(this.TAG, "readNshorts is " + this.readNshorts + " So do nothing");
            return this.readNshorts;
        }
    }

    private boolean getSensoryRecognitionResult(long consoleInitReturn, short[] speech) {
        float[] sensoryResultValue = new float[3];
        if (this.dualThresholdFlag == -1) {
            sensoryResultValue[1] = -1.0f;
        } else {
            sensoryResultValue[1] = (float) this.dualThresholdFlag;
        }
        String consoleResult = this.aSensoryBargeInEngine.phrasespotPipe(consoleInitReturn, speech, 160, 16000, sensoryResultValue);
        this.dualThresholdFlag = (int) sensoryResultValue[1];
        if (consoleResult != null) {
            this.BargeinAct[0] = (short) getSensoryBargeInAct(this.mCommandType, consoleResult);
            this.strResult[0] = consoleResult;
            float sensoryCMscore = sensoryResultValue[0];
            Log.i(this.TAG, "consoleResult : " + consoleResult);
            Log.d(this.TAG, "strResult[0] : " + this.strResult[0]);
            Log.d(this.TAG, "BargeinAct[0] : " + this.BargeinAct[0]);
            Log.i(this.TAG, "sensoryCMscore : " + sensoryCMscore);
            Log.i(this.TAG, "dualThresholdFlag = " + this.dualThresholdFlag);
            if (!resultSensoryOOV(consoleInitReturn, this.BargeinAct[0], sensoryCMscore)) {
                if (!this.isSensoryCameraBargeIn) {
                    SendHandlerMessage(this.strResult);
                    return true;
                } else if (this.recogAfterReadCount == 0) {
                    this.recogAfterReadCount = 1;
                    SendHandlerMessage(this.strResult);
                    if (this.isEnableSamsungOOVResult) {
                        this.isSensoryResult = true;
                        Log.i(this.TAG, "Set isSensoryResult = true. So isSensoryResult : " + this.isSensoryResult);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean resultSensoryOOV(long consoleInitReturn, int action, float sensoryCMscore) {
        if (this.mCommandType != 2) {
            if (this.mCommandType != 7) {
                if (this.mCommandType != 3) {
                    if (this.mCommandType == 9) {
                        switch (this.mLanguage) {
                            case 0:
                                if (action == 1 && sensoryCMscore < this.sensoryKoreanCancelCMTH) {
                                    Log.i(this.TAG, "Korean cancel score is low. So rejected");
                                    return true;
                                }
                            default:
                                break;
                        }
                    }
                }
                switch (this.mLanguage) {
                    case 0:
                        if (action == 1 && sensoryCMscore < this.sensoryKoreanStopCMTH) {
                            Log.i(this.TAG, "Korean stop score is low. So rejected");
                            return true;
                        }
                    case 1:
                        if (action == 1 && sensoryCMscore < this.sensoryUSEnglishStopCMTH) {
                            Log.i(this.TAG, "US English stop score is low. So rejected");
                            return true;
                        } else if (action == 2 && sensoryCMscore < this.sensoryUSEnglishSnoozeCMTH) {
                            Log.i(this.TAG, "US English snooze score is low. So rejected");
                            return true;
                        }
                        break;
                    case 2:
                        if (action == 1 && sensoryCMscore < this.sensoryChineseStopCMTH) {
                            Log.i(this.TAG, "Chinese stop score is low. So rejected");
                            return true;
                        }
                    case 10:
                        if (action == 1 && sensoryCMscore < this.sensoryUKEnglishStopCMTH) {
                            Log.i(this.TAG, "UK English stop score is low. So rejected");
                            return true;
                        }
                    default:
                        break;
                }
            } else if (this.dualThresholdFlag != 1) {
                switch (this.mLanguage) {
                    case 1:
                    case 10:
                        if (this.isSubModelBargeInEnable && consoleInitReturn == this.consoleInitReturn_sub) {
                            if (action == 2 && sensoryCMscore < this.sensoryUSEnglishCheeseCMTH) {
                                Log.i(this.TAG, "Sub English cheese score is low. So rejected");
                                return true;
                            } else if (action == 3 && sensoryCMscore < this.sensoryUSEnglishCaptureCMTH) {
                                Log.i(this.TAG, "Sub English capture score is low. So rejected");
                                return true;
                            } else if (action == 4 && sensoryCMscore < this.sensoryUSEnglishShootCMTH) {
                                Log.i(this.TAG, "Sub English shoot score is low. So rejected");
                                return true;
                            } else if (action == 5 && sensoryCMscore < this.sensoryUSEnglishRecordVideoCMTH) {
                                Log.i(this.TAG, "Sub English record video score is low. So rejected");
                                return true;
                            }
                        }
                        break;
                    case 2:
                        if (action == 3 && sensoryCMscore < this.sensoryChineseCaptureCMTH) {
                            Log.i(this.TAG, "Chinese capture score is low. So rejected");
                            return true;
                        }
                    case 7:
                        if (action == 4 && sensoryCMscore < this.sensoryJapaneseShootCMTH) {
                            Log.i(this.TAG, "Japanese shoot score is low. So rejected");
                            return true;
                        }
                    case 8:
                        if (action == 2 && sensoryCMscore < this.sensoryRussianCheeseCMTH) {
                            Log.e(this.TAG, "Russian cheese score is low. So rejected");
                            return true;
                        }
                    default:
                        break;
                }
            } else {
                switch (this.mLanguage) {
                    case 0:
                        if (action == 3 && sensoryCMscore < 300.0f) {
                            Log.i(this.TAG, "Korean capture score is low. So rejected");
                            return true;
                        } else if (action == 2 && sensoryCMscore < 1200.0f) {
                            Log.i(this.TAG, "Korean cheese score is low. So rejected");
                            return true;
                        } else if (action == 4 && sensoryCMscore < 400.0f) {
                            Log.i(this.TAG, "Korean shoot score is low. So rejected");
                            return true;
                        } else if (action == 5 && sensoryCMscore < 800.0f) {
                            Log.i(this.TAG, "Korean record video score is low. So rejected");
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        switch (this.mLanguage) {
            case 0:
                if (action == 2 && sensoryCMscore < this.sensoryKoreanRejectCMTH) {
                    Log.i(this.TAG, "Korean reject score is low. So rejected");
                    return true;
                }
            case 13:
                if (action == 2 && sensoryCMscore < this.sensoryCantoneseRejectCMTH) {
                    Log.i(this.TAG, "HK Chinese reject score is low. So rejected");
                    return true;
                }
        }
        return false;
    }

    private int getMMUIRecognitionResult(short[] speech, int readNshorts) {
        int result = 0;
        if (this.aMMUIRecognizer != null) {
            result = this.aMMUIRecognizer.RECThread(speech);
        }
        if (result == -2) {
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section1");
                return -1;
            } else if (this.aMMUIRecognizer != null) {
                Log.d(this.TAG, "Barge-in : Too long input so Reset");
                this.aMMUIRecognizer.ResetFx();
                this.aMMUIRecognizer.SASRReset();
            }
        }
        if (this.done) {
            Log.e(this.TAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section2");
            return -1;
        }
        if (result == 2 && this.aMMUIRecognizer != null) {
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section3");
                return -1;
            }
            this.aMMUIRecognizer.ResetFx();
            this.numRecogResult = this.aMMUIRecognizer.SASRDoRecognition(this.cmResult, this.strResult, "/system/voicebargeindata/sasr/input.txt", this.BargeinAct, this.utfResult);
            this.strResult[0] = this.strResult[0].replace('_', ' ');
            if (this.mEmbeddedEngineLanguage == 0 || this.mEmbeddedEngineLanguage == 2) {
                this.utfResult[0] = this.utfResult[0].replace('_', ' ');
                this.strResult[0] = this.utfResult[0];
            }
            Log.i(this.TAG, "numResult[0] : " + this.cmResult[0]);
            Log.i(this.TAG, "strResult[0] : " + this.strResult[0]);
            Log.i(this.TAG, "BargeinAct[0] : " + this.BargeinAct[0]);
            if (this.mCommandType == 3 && this.BargeinAct[0] == (short) 2) {
                this.THscore = -1.8d;
            } else if (this.mCommandType == 7) {
                this.THscore = -1.0d;
            } else {
                this.THscore = -1.5d;
            }
            Log.i(this.TAG, "THscore : " + this.THscore);
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : getMMUIRecognitionResult - Section4");
                return -1;
            }
            if (this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) {
                if (this.isSensoryResult) {
                    Log.i(this.TAG, "isSensoryCameraBargeIn is true and isSensoryResult is true");
                    Log.d(this.TAG, "EmbeddedEngine Recognizer : " + this.BargeinAct[0]);
                    this.isSensoryResult = false;
                    Log.i(this.TAG, "Set isSensoryResult = false. So isSensoryResult : " + this.isSensoryResult);
                } else {
                    Log.i(this.TAG, "isSensoryCameraBargeIn is true and keyword is not detected by sensory and keyword or non-keyword is detected by embeddedEngine.");
                    this.strResult[0] = "TH-Reject";
                    this.BargeinAct[0] = (short) -1;
                    SendHandlerMessage(this.strResult);
                }
            } else if (((double) this.cmResult[0]) > this.THscore) {
                SendHandlerMessage(this.strResult);
            } else {
                this.strResult[0] = "TH-Reject";
                this.BargeinAct[0] = (short) -1;
                SendHandlerMessage(this.strResult);
            }
            if (this.done) {
                Log.e(this.TAG, "readByteBlock return -1 : Section13");
                return -1;
            }
            this.aMMUIRecognizer.SASRReset();
        }
        return readNshorts;
    }

    public static short twoBytesToShort(byte b1, byte b2) {
        return (short) ((b1 & 255) | (b2 << 8));
    }

    public void setHandler(Handler stopHandler) {
        this.mStopHandler = stopHandler;
    }

    public void SendHandlerMessage(String[] result) {
        Message msg = this.handler.obtainMessage();
        Bundle b = new Bundle();
        b.putStringArray("recognition_result", result);
        msg.setData(b);
        try {
            this.handler.sendMessage(msg);
        } catch (IllegalStateException e) {
            Log.e(this.TAG, "IllegalStateException " + e.getMessage());
            stop();
        }
    }

    public void setSamsungFilePath(int language, int domain) {
        this.wordListPath = Config.GetSamsungPath(language);
        this.modelPath = this.wordListPath + "param";
        this.loadNameList = Config.GetSamsungNameList(domain);
    }

    public void setSensoryFilePath(int language, int domain) {
        String sensoryModelPath = Config.GetSensoryAM(language, domain);
        String sensoryGrammarPath = Config.GetSensoryGRAMMAR(language, domain);
        String sensoryModelPath_sub = sensoryModelPath;
        String sensoryGrammarPath_sub = sensoryGrammarPath;
        sensoryModelPath = sensoryModelPath + Config.SENSORY_MAIN_SUFFIX;
        sensoryGrammarPath = sensoryGrammarPath + Config.SENSORY_MAIN_SUFFIX;
        sensoryModelPath_sub = sensoryModelPath_sub + Config.SENSORY_SUB_SUFFIX;
        sensoryGrammarPath_sub = sensoryGrammarPath_sub + Config.SENSORY_SUB_SUFFIX;
        if (isBargeInFile(Config.SENSORY_SO_FILE_PATH) || isBargeInFile(Config.SENSORY_SO_FILE_PATH_64)) {
            if (isBargeInFile(sensoryModelPath) && isBargeInFile(sensoryGrammarPath)) {
                this.isSensoryBargeInEnable = true;
                this.acousticModelPathname = sensoryModelPath;
                this.searchGrammarPathname = sensoryGrammarPath;
            }
            if (isBargeInFile(sensoryModelPath_sub) && isBargeInFile(sensoryGrammarPath_sub)) {
                this.isSubModelBargeInEnable = true;
                this.acousticModelPathname_sub = sensoryModelPath_sub;
                this.searchGrammarPathname_sub = sensoryGrammarPath_sub;
                Log.i(this.TAG, "SUB model is loaded ");
            }
        }
        if (this.mCommandType == 7) {
            this.isCameraBargeIn = true;
            if (this.isSensoryBargeInEnable) {
                this.isSensoryCameraBargeIn = true;
            }
        } else if (this.mCommandType == 9) {
            this.isCancelBargeIn = true;
        }
    }

    public boolean isSensoryBargeinEnabled() {
        return this.isSensoryBargeInEnable;
    }

    public int getSensoryBargeInAct(int domain, String result) {
        switch (domain) {
            case 0:
                if (result.startsWith("stop")) {
                    return 1;
                }
                break;
            case 1:
                if (result.startsWith("next")) {
                    return 1;
                }
                if (result.startsWith("previous")) {
                    return 2;
                }
                break;
            case 2:
                if (result.startsWith("answer")) {
                    return 1;
                }
                if (result.startsWith("reject")) {
                    return 2;
                }
                break;
            case 3:
                if (result.startsWith("stop")) {
                    return 1;
                }
                if (result.startsWith("snooze")) {
                    return 2;
                }
                break;
            case 4:
            case 5:
            case 6:
                if (result.startsWith("next")) {
                    return 1;
                }
                if (result.startsWith("previous")) {
                    return 2;
                }
                if (result.startsWith("pause")) {
                    return 3;
                }
                if (result.startsWith("play")) {
                    return 4;
                }
                if (result.startsWith("volume up") || result.startsWith("volume_up") || result.startsWith("volumeup")) {
                    return 5;
                }
                if (result.startsWith("volume down") || result.startsWith("volume_down") || result.startsWith("volumedown")) {
                    return 6;
                }
                break;
            case 7:
                if (result.startsWith("smile")) {
                    return 1;
                }
                if (result.startsWith("cheese")) {
                    return 2;
                }
                if (result.startsWith("capture")) {
                    return 3;
                }
                if (result.startsWith("shoot")) {
                    return 4;
                }
                if (result.startsWith("record video") || result.startsWith("record_video") || result.startsWith("recordvideo")) {
                    return 5;
                }
                if (result.startsWith("auto settings") || result.startsWith("auto_settings") || result.startsWith("autosettings")) {
                    return 6;
                }
                if (result.startsWith("beauty face") || result.startsWith("beauty_face") || result.startsWith("beautyface")) {
                    return 7;
                }
                if (result.startsWith("timer")) {
                    return 8;
                }
                if (result.startsWith("zoom in") || result.startsWith("zoom_in") || result.startsWith("zoomin")) {
                    return 9;
                }
                if (result.startsWith("zoom out") || result.startsWith("zoom_out") || result.startsWith("zoomout")) {
                    return 10;
                }
                if (result.startsWith("flash on") || result.startsWith("flash_on") || result.startsWith("flashon")) {
                    return 11;
                }
                if (result.startsWith("flash off") || result.startsWith("flash_off") || result.startsWith("flashoff")) {
                    return 12;
                }
                if (result.startsWith("upload pics") || result.startsWith("upload_pics") || result.startsWith("uploadpics")) {
                    return 13;
                }
                if (result.startsWith("gallery")) {
                    return 14;
                }
                break;
            case 8:
                if (result.startsWith("buddy photo share") || result.startsWith("buddy_photo_share") || result.startsWith("buddyphotoshare")) {
                    return 1;
                }
                if (result.startsWith("next")) {
                    return 2;
                }
                if (result.startsWith("previous")) {
                    return 3;
                }
                if (result.startsWith("play")) {
                    return 4;
                }
                if (result.startsWith("slideshow")) {
                    return 5;
                }
                if (result.startsWith("stop")) {
                    return 6;
                }
                if (result.startsWith("camera")) {
                    return 7;
                }
                break;
            case 9:
                if (result.startsWith("cancel")) {
                    return 1;
                }
                break;
            case 10:
                if (result.startsWith("yes")) {
                    return 1;
                }
                if (result.startsWith("no")) {
                    return 2;
                }
                break;
        }
        return -1;
    }

    private AudioRecord getAudioRecord(int source) {
        Throwable th;
        Log.i(this.TAG, "getAudioRecord modified by jy");
        AudioRecord retAudioRecord;
        try {
            retAudioRecord = new AudioRecord(source, 16000, 16, 2, 8192);
            try {
                if (retAudioRecord.getState() != 1) {
                    Log.d(this.TAG, "getAudioRecord for " + source + "=false, got !initialized");
                    if (retAudioRecord != null) {
                        retAudioRecord.release();
                    }
                    return null;
                }
                Log.d(this.TAG, "got AudioRecord using source=" + source + ", also " + 16000 + " " + 16 + " " + 2 + " " + 8192);
                Log.i(this.TAG, "getAudioRecord for " + source + "=true");
                return retAudioRecord;
            } catch (IllegalArgumentException e) {
                try {
                    Log.e(this.TAG, "getAudioRecord for " + source + "=false, IllegalArgumentException");
                    Log.e(this.TAG, "got IllegalArgumentException using source=" + source + ", also " + 16000 + " " + 16 + " " + 2 + " " + 8192);
                    if (retAudioRecord != null) {
                        retAudioRecord.release();
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        } catch (IllegalArgumentException e2) {
            retAudioRecord = null;
            Log.e(this.TAG, "getAudioRecord for " + source + "=false, IllegalArgumentException");
            Log.e(this.TAG, "got IllegalArgumentException using source=" + source + ", also " + 16000 + " " + 16 + " " + 2 + " " + 8192);
            if (retAudioRecord != null) {
                retAudioRecord.release();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            retAudioRecord = null;
            throw th;
        }
    }

    public void setEmbeddedEngineLanguage() {
        this.mEmbeddedEngineLanguage = this.mLanguage;
        if (this.isSensoryCameraBargeIn && this.isEnableSamsungOOVResult) {
            this.mEmbeddedEngineLanguage = 0;
        } else if (this.mEmbeddedEngineLanguage == 10) {
            this.mEmbeddedEngineLanguage = 1;
        } else if (this.mEmbeddedEngineLanguage == 11) {
            this.mEmbeddedEngineLanguage = 3;
        } else if (this.mEmbeddedEngineLanguage == 9) {
            this.mEmbeddedEngineLanguage = 1;
        } else if (this.mEmbeddedEngineLanguage == 13) {
            this.mEmbeddedEngineLanguage = 2;
        } else if (this.mEmbeddedEngineLanguage == 12) {
            this.mEmbeddedEngineLanguage = 2;
        } else if (this.mEmbeddedEngineLanguage == 14) {
            this.mEmbeddedEngineLanguage = 2;
        }
        Log.i(this.TAG, "mEmbeddedEngineLanguage : " + this.mEmbeddedEngineLanguage);
    }

    private boolean isBargeInFile(String mFilePath) {
        if (new File(mFilePath).exists()) {
            return true;
        }
        return false;
    }
}
