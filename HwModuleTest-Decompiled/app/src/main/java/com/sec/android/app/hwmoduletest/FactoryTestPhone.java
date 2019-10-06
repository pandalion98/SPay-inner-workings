package com.sec.android.app.hwmoduletest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class FactoryTestPhone {
    private static final int FACTORY_TEST_HISTORY_VIEW_FIRST_COMMAND = 18;
    private static final int FACTORY_TEST_HISTORY_VIEW_SECOND_COMMAND = 3;
    private static final int FACTORY_TEST_STATUS_FIRST_COMMAND = 18;
    private static final int FACTORY_TEST_STATUS_SECOND_COMMAND = 4;
    private static final int MSG_DATA_CP_ACCELEROMETER = 206;
    private static final int MSG_REQUEST_GRIP_CONFIRM = 205;
    private static final int MSG_REQUEST_HISTORY_NV_VALUES = 101;
    private static final int MSG_REQUEST_ITEM_NV_UPDATE = 102;
    private static final int MSG_REQUEST_TEST_NV_VALUES = 100;
    public static final byte OEM_MISC_GET_GRIP_SENSOR_INFO = 112;
    public static final byte OEM_MISC_SET_GRIP_SENSOR_INFO = 113;
    /* access modifiers changed from: private */
    public static Queue<Messages> mQueue = null;
    /* access modifiers changed from: private */
    public String CLASS_NAME = "FactoryTestPhone";
    private boolean IS_CAL_TEST_PASS = false;
    private boolean IS_FINAL_TEST_PASS = false;
    private boolean IS_LTECAL_TEST_PASS = false;
    private boolean IS_LTEFINAL_TEST_PASS = false;
    private boolean IS_PBA_TEST_PASS = false;
    private boolean IS_SMD_TEST_PASS = false;
    private Message Response;
    /* access modifiers changed from: private */
    public boolean isConnected = false;
    Context mContext;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 206) {
                switch (i) {
                    case 100:
                        LtUtil.log_i(FactoryTestPhone.this.CLASS_NAME, "handleMessage", "Received Test NV Values from RIL");
                        return;
                    case 101:
                        LtUtil.log_i(FactoryTestPhone.this.CLASS_NAME, "handleMessage", "Received History NV Values from RIL");
                        return;
                    case 102:
                        LtUtil.log_i(FactoryTestPhone.this.CLASS_NAME, "handleMessage", "Item NV update success");
                        return;
                    default:
                        return;
                }
            } else {
                LtUtil.log_i(FactoryTestPhone.this.CLASS_NAME, "handleMessage", "MSG_DATA_CP_ACCELEROMETER");
            }
        }
    };
    private final HashMap<String, Byte> mNVKeyToItemIDHashmap = new HashMap<>();
    private ServiceConnection mSecPhoneServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            LtUtil.log_i(FactoryTestPhone.this.CLASS_NAME, "ServiceConnection", "onServiceConnected()");
            FactoryTestPhone.this.mServiceMessenger = new Messenger(service);
            FactoryTestPhone.this.isConnected = true;
            while (FactoryTestPhone.mQueue.peek() != null) {
                Messages mMessage = new Messages((Messages) FactoryTestPhone.mQueue.poll());
                StringBuilder sb = new StringBuilder();
                sb.append("mData: ");
                sb.append(new String(mMessage.Data));
                sb.append("mResponse");
                sb.append(mMessage.Response);
                LtUtil.log_i(FactoryTestPhone.this.CLASS_NAME, "mSecPhoneServiceConnection.onServiceConnected", sb.toString());
                FactoryTestPhone.this.sendMessageToRil(mMessage.Data, mMessage.Response);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            LtUtil.log_i(FactoryTestPhone.this.CLASS_NAME, "onServiceDisconnected", "onServiceDisconnedted()");
            FactoryTestPhone.this.isConnected = false;
            FactoryTestPhone.this.mServiceMessenger = null;
            FactoryTestPhone.this.rebindToSecPhoneService();
        }
    };
    /* access modifiers changed from: private */
    public Messenger mServiceMessenger = null;
    private Messenger mSvcModeMessenger;

    class Messages {
        byte[] Data;
        Message Response;

        Messages(byte[] mData, Message mMessage) {
            this.Data = mData;
            this.Response = mMessage;
        }

        Messages(Messages mMessage) {
            this.Data = mMessage.Data;
            this.Response = mMessage.Response;
        }
    }

    public FactoryTestPhone(Context context) {
        this.mContext = context;
        this.mSvcModeMessenger = new Messenger(this.mHandler);
        mQueue = new LinkedList();
    }

    public FactoryTestPhone(Context context, Handler ReceivedHandler) {
        this.mContext = context;
        this.mSvcModeMessenger = new Messenger(ReceivedHandler);
        mQueue = new LinkedList();
    }

    public void bindSecPhoneService() {
        LtUtil.log_i(this.CLASS_NAME, "bindSecPhoneService", "bindSecPhoneService()");
        Intent intent = new Intent();
        intent.setClassName("com.sec.phone", "com.sec.phone.SecPhoneService");
        this.mContext.bindService(intent, this.mSecPhoneServiceConnection, 1);
    }

    public void unbindSecPhoneService() {
        LtUtil.log_i(this.CLASS_NAME, "unbindSecPhoneService", "unbindSecPhoneService()");
        try {
            this.mContext.unbindService(this.mSecPhoneServiceConnection);
            this.isConnected = false;
        } catch (Exception e) {
        }
    }

    public boolean getResultForPGMItems(String NVKey) {
        int itemNv = Integer.parseInt(NVKey, 16);
        if (itemNv == 1) {
            return this.IS_SMD_TEST_PASS;
        }
        if (itemNv == 4) {
            return this.IS_PBA_TEST_PASS;
        }
        if (itemNv == 7) {
            return this.IS_CAL_TEST_PASS;
        }
        switch (itemNv) {
            case 10:
                return this.IS_FINAL_TEST_PASS;
            case 11:
                return this.IS_LTECAL_TEST_PASS;
            case 12:
                return this.IS_LTEFINAL_TEST_PASS;
            default:
                return false;
        }
    }

    public void setResultForPGMItems(String NVKey, boolean result) {
        int itemNv = Integer.parseInt(NVKey, 16);
        if (itemNv == 1) {
            this.IS_SMD_TEST_PASS = result;
        } else if (itemNv == 4) {
            this.IS_PBA_TEST_PASS = result;
        } else if (itemNv != 7) {
            switch (itemNv) {
                case 10:
                    this.IS_FINAL_TEST_PASS = result;
                    return;
                case 11:
                    this.IS_LTECAL_TEST_PASS = result;
                    return;
                case 12:
                    this.IS_LTEFINAL_TEST_PASS = result;
                    return;
                default:
                    return;
            }
        } else {
            this.IS_CAL_TEST_PASS = result;
        }
    }

    /* access modifiers changed from: private */
    public void rebindToSecPhoneService() {
        if (this.mServiceMessenger == null) {
            new Thread() {
                public void run() {
                    while (FactoryTestPhone.this.mServiceMessenger == null) {
                        FactoryTestPhone.this.bindSecPhoneService();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            LtUtil.log_i(FactoryTestPhone.this.CLASS_NAME, "rebindToSecPhoneService", "rebindToSecPhoneService : sleep interrupted.");
                        }
                    }
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public void sendMessageToRil(byte[] data, Message response) {
        LtUtil.log_i(this.CLASS_NAME, "sendMessageToRil", "sendMessage()");
        Bundle req = response.getData();
        StringBuilder sb = new StringBuilder();
        sb.append("sendMessage() - current Queue size before : ");
        sb.append(mQueue.size());
        LtUtil.log_i(this.CLASS_NAME, "sendMessageToRil", sb.toString());
        if (data != null) {
            req.putByteArray("request", data);
            response.setData(req);
            response.replyTo = this.mSvcModeMessenger;
            try {
                if (this.mServiceMessenger != null) {
                    LtUtil.log_i(this.CLASS_NAME, "sendMessageToRil", "sendMessage() to RIL");
                    this.mServiceMessenger.send(response);
                }
            } catch (RemoteException e) {
            }
        } else {
            LtUtil.log_e(this.CLASS_NAME, "sendMessageToRil", "sendMessage : data is null");
        }
        mQueue.peek();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("sendMessage() - current Queue size after : ");
        sb2.append(mQueue.size());
        LtUtil.log_i(this.CLASS_NAME, "sendMessageToRil", sb2.toString());
    }

    private void invokeOemRilRequestRaw(byte[] data, Message response) {
        LtUtil.log_i(this.CLASS_NAME, "invokeOemRilRequestRaw", "invokeOemRilRequestRaw");
        if (!mQueue.offer(new Messages(data, response))) {
            LtUtil.log_e(this.CLASS_NAME, "invokeOemRilRequestRaw", "invokeOemRilRequestRaw : failed offer a item");
            return;
        }
        if (this.isConnected) {
            LtUtil.log_v(this.CLASS_NAME, "invokeOemRilRequestRaw", "isConnected == true");
            while (mQueue.peek() != null) {
                Messages mMessages = (Messages) mQueue.poll();
                sendMessageToRil(mMessages.Data, mMessages.Response);
            }
        } else {
            LtUtil.log_v(this.CLASS_NAME, "invokeOemRilRequestRaw", "isConnected == false");
            bindSecPhoneService();
        }
    }

    public void requestTestNvViewToRil() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(18);
            dos.writeByte(3);
            dos.writeShort(7);
            dos.writeByte(1);
            dos.writeByte(0);
            dos.writeByte(2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        invokeOemRilRequestRaw(bos.toByteArray(), this.mHandler.obtainMessage(100));
    }

    public void requestHistoryNvViewToRil() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(18);
            dos.writeByte(3);
            dos.writeShort(7);
            dos.writeByte(1);
            dos.writeByte(0);
            dos.writeByte(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        invokeOemRilRequestRaw(bos.toByteArray(), this.mHandler.obtainMessage(101));
    }

    public void updateNVItem(byte itemID, byte result) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(18);
            dos.writeByte(4);
            dos.writeShort(9);
            dos.writeByte(3);
            dos.writeByte(0);
            dos.writeByte(1);
            dos.writeByte(itemID);
            dos.writeByte(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        invokeOemRilRequestRaw(bos.toByteArray(), this.mHandler.obtainMessage(102));
    }

    public void requestGripSensorOn(boolean isOn) {
        byte[] data;
        LtUtil.log_i(this.CLASS_NAME, "requestGripSensorOn", "requestGripSensorOn()");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        StringBuilder sb = new StringBuilder();
        sb.append("RILSUPPORTBR_____ requestGripSensorOn() [");
        sb.append(Feature.getString(Feature.MODEL_NAME));
        sb.append("]_____RILSUPPORTBR");
        LtUtil.log_d(this.CLASS_NAME, "requestGripSensorOn", sb.toString());
        if (isOn) {
            data = new byte[]{2, 0, HwModuleTest.ID_SLEEP, 1};
        } else {
            data = new byte[]{2, 0, HwModuleTest.ID_SLEEP, 0};
        }
        try {
            dos.writeByte(18);
            dos.writeByte(9);
            dos.writeShort(8);
            dos.write(data);
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        this.Response = this.mHandler.obtainMessage(205);
        invokeOemRilRequestRaw(bos.toByteArray(), this.Response);
    }

    public void sendToRilCpAccelermeter(byte[] control) {
        LtUtil.log_d(this.CLASS_NAME, "sendToRilCpAccelermeter", "RILSUPPORTBR_____ sendToRilCpAccelermeter() _____RILSUPPORTBR");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(18);
            dos.writeByte(9);
            dos.writeShort(8);
            dos.write(control);
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        invokeOemRilRequestRaw(bos.toByteArray(), this.mHandler.obtainMessage(206));
    }

    public void sendGripControlToRil(byte type, byte CtrGrip, int HandlerMSG) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            dos.writeByte(17);
            dos.writeByte(type);
            dos.writeShort(5);
            dos.writeByte(CtrGrip);
            dos.flush();
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            try {
                dos.close();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        this.Response = this.mHandler.obtainMessage(HandlerMSG);
        invokeOemRilRequestRaw(bos.toByteArray(), this.Response);
    }

    public void DestroySecPhoneService() {
        unbindSecPhoneService();
        this.mSecPhoneServiceConnection = null;
    }

    public void ClearQueue() {
        mQueue.clear();
    }
}
