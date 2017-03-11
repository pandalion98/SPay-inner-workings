package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.samsung.android.spayfw.appinterface.IServerResponseCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ServerRequest;
import com.samsung.android.spayfw.appinterface.ServerResponseData;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.CommonClient;
import com.samsung.android.spayfw.remoteservice.GenericServerRequest;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.ServerResponse;
import com.samsung.android.spayfw.remoteservice.cashcard.CashCardClient;
import com.samsung.android.spayfw.remoteservice.commerce.PaymentServiceClient;
import com.samsung.android.spayfw.remoteservice.p018a.AnalyticsRequesterClient;
import com.samsung.android.spayfw.remoteservice.p020c.PromotionsClient;
import com.samsung.android.spayfw.remoteservice.p021d.RewardsClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterClient;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.io.File;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.spayfw.core.a.r */
public class ServerRequestProcessor extends Processor {
    private static ServerRequestProcessor me;

    /* renamed from: com.samsung.android.spayfw.core.a.r.a */
    private class ServerRequestProcessor extends C0413a<ServerResponse, GenericServerRequest> {
        IServerResponseCallback mf;
        final /* synthetic */ ServerRequestProcessor mg;

        public ServerRequestProcessor(ServerRequestProcessor serverRequestProcessor, IServerResponseCallback iServerResponseCallback) {
            this.mg = serverRequestProcessor;
            this.mf = iServerResponseCallback;
        }

        public void m503a(int i, ServerResponse serverResponse) {
            ServerResponseData serverResponseData;
            File file;
            Throwable e;
            Throwable th;
            File file2 = null;
            Log.m287i("ServerRequestProcessor", "ServerRequestProcessor: onRequestComplete: " + i);
            try {
                ServerResponseData serverResponseData2;
                if (this.mf != null) {
                    switch (i) {
                        case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                            this.mf.onFail(PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID);
                            this.mg.m505a(null, null);
                            return;
                        case ECCurve.COORD_AFFINE /*0*/:
                            this.mf.onFail(PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE);
                            this.mg.m505a(null, null);
                            return;
                        default:
                            String str;
                            String filePath;
                            if (serverResponse != null) {
                                str = (String) serverResponse.getResult();
                                filePath = serverResponse.getFilePath();
                            } else {
                                str = null;
                                filePath = null;
                            }
                            Log.m285d("ServerRequestProcessor", "ServerRequestProcessor: json response =  " + str);
                            serverResponseData = new ServerResponseData();
                            if (filePath == null) {
                                try {
                                    serverResponseData.setContent(str);
                                    file = null;
                                } catch (Exception e2) {
                                    e = e2;
                                    try {
                                        Log.m284c("ServerRequestProcessor", e.getMessage(), e);
                                        this.mg.m505a(serverResponseData, file2);
                                    } catch (Throwable th2) {
                                        e = th2;
                                        this.mg.m505a(serverResponseData, file2);
                                        throw e;
                                    }
                                }
                            }
                            File file3 = new File(filePath);
                            try {
                                ParcelFileDescriptor open = ParcelFileDescriptor.open(file3, 805306368);
                                serverResponseData.setFd(open);
                                Log.m285d("ServerRequestProcessor", "ServerRequestProcessor: file =  " + file3);
                                Log.m285d("ServerRequestProcessor", "ServerRequestProcessor: fd =  " + open);
                                file = file3;
                            } catch (Exception e3) {
                                e = e3;
                                file2 = file3;
                                Log.m284c("ServerRequestProcessor", e.getMessage(), e);
                                this.mg.m505a(serverResponseData, file2);
                            } catch (Throwable th3) {
                                e = th3;
                                file2 = file3;
                                this.mg.m505a(serverResponseData, file2);
                                throw e;
                            }
                            try {
                                Log.m285d("ServerRequestProcessor", "ServerRequestProcessor: content =  " + serverResponseData.getContent());
                                Log.m285d("ServerRequestProcessor", "ServerRequestProcessor: fd =  " + serverResponseData.getFd());
                                this.mf.onSuccess(i, serverResponseData);
                                serverResponseData2 = serverResponseData;
                                break;
                            } catch (Throwable e4) {
                                th = e4;
                                file2 = file;
                                e = th;
                                Log.m284c("ServerRequestProcessor", e.getMessage(), e);
                                this.mg.m505a(serverResponseData, file2);
                            } catch (Throwable e42) {
                                th = e42;
                                file2 = file;
                                e = th;
                                this.mg.m505a(serverResponseData, file2);
                                throw e;
                            }
                    }
                }
                file = null;
                this.mg.m505a(serverResponseData2, file);
            } catch (Exception e5) {
                e = e5;
                serverResponseData = null;
                Log.m284c("ServerRequestProcessor", e.getMessage(), e);
                this.mg.m505a(serverResponseData, file2);
            } catch (Throwable th4) {
                e = th4;
                serverResponseData = null;
                this.mg.m505a(serverResponseData, file2);
                throw e;
            }
        }
    }

    public static final synchronized ServerRequestProcessor m507u(Context context) {
        ServerRequestProcessor serverRequestProcessor;
        synchronized (ServerRequestProcessor.class) {
            if (me == null) {
                me = new ServerRequestProcessor(context);
            }
            serverRequestProcessor = me;
        }
        return serverRequestProcessor;
    }

    private ServerRequestProcessor(Context context) {
        super(context);
    }

    public void m509a(ServerRequest serverRequest, IServerResponseCallback iServerResponseCallback) {
        Log.m285d("ServerRequestProcessor", "process() called!");
        if (!(serverRequest == null || iServerResponseCallback == null)) {
            try {
                if (!(serverRequest.getRelativeUrl() == null || serverRequest.getServiceType() == -1 || serverRequest.getRequestMethod() == -1)) {
                    CommonClient v = m508v(serverRequest.getServiceType());
                    if (v == null) {
                        Log.m286e("ServerRequestProcessor", "Error not able to get client");
                        iServerResponseCallback.onFail(-5);
                        return;
                    }
                    v.m1124a(serverRequest.getRequestMethod(), serverRequest.getRelativeUrl(), serverRequest.getHeaders(), serverRequest.getBody()).m836a(new ServerRequestProcessor(this, iServerResponseCallback));
                    Log.m287i("ServerRequestProcessor", "ServerRequestProcessor: generic request made: " + serverRequest.getRequestMethod() + " " + serverRequest.getRelativeUrl());
                    return;
                }
            } catch (Throwable e) {
                Log.m284c("ServerRequestProcessor", e.getMessage(), e);
                iServerResponseCallback.onFail(-5);
                return;
            }
        }
        if (iServerResponseCallback != null) {
            iServerResponseCallback.onFail(-5);
        }
        Log.m286e("ServerRequestProcessor", "process: inputs are invalid! request = " + serverRequest + "; cb = " + iServerResponseCallback);
        if (serverRequest != null) {
            Log.m286e("ServerRequestProcessor", "process: request.toString = " + serverRequest.toString());
        }
    }

    private void m505a(ServerResponseData serverResponseData, File file) {
        Log.m285d("ServerRequestProcessor", "cleanServerResponse");
        if (serverResponseData != null && serverResponseData.getFd() != null) {
            try {
                serverResponseData.getFd().close();
                Log.m285d("ServerRequestProcessor", "closing serverResponse fd");
            } catch (Throwable e) {
                Log.m284c("ServerRequestProcessor", e.getMessage(), e);
            }
            if (file != null) {
                file.delete();
                Log.m285d("ServerRequestProcessor", "Delete file");
            }
        }
    }

    private CommonClient m508v(int i) {
        switch (i) {
            case ECCurve.COORD_AFFINE /*0*/:
                return TokenRequesterClient.m1126Q(this.mContext);
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                return PromotionsClient.m1170K(this.mContext);
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                return AnalyticsRequesterClient.m1159H(this.mContext);
            case F2m.PPB /*3*/:
                return CashCardClient.m1172I(this.mContext);
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                return PaymentServiceClient.m1183J(this.mContext);
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                return RewardsClient.m1185L(this.mContext);
            default:
                Log.m286e("ServerRequestProcessor", "Client not found " + i);
                return null;
        }
    }
}
