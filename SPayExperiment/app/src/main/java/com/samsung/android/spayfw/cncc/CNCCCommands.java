/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.cncc;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TACommands;
import com.samsung.android.spaytzsvc.api.TAStruct;
import java.util.ArrayList;
import java.util.List;
import javolution.io.Struct;

public class CNCCCommands
extends TACommands {
    public static final int CMD_CNCC_CMD_UNKNOWN = Integer.MAX_VALUE;
    public static final int CMD_CNCC_GET_NONCE = 3;
    public static final int CMD_CNCC_LOAD_CERTS = 1;
    public static final int CMD_CNCC_PROCESS_DATA = 2;
    public static final int CMD_TA_INIT = 2147483633;
    public static final int MAX_CERTS_IN_CHAIN = 3;
    private static final int MAX_CERT_CHAIN_DATA_LEN = 12288;
    private static final int MAX_CERT_DATA_LEN = 4096;
    private static final int MAX_DATA_LEN = 8192;
    private static final int MAX_ERROR_STR_LEN = 256;
    private static final int MAX_TA_NAME_LEN = 33;
    private static final String TAG = "CNCCCommands";

    public CNCCCommands() {
        this.addCommandInfo(2147483633, new TACommands.CommandInfo(TACommands.Init.getMaxRequestSize(), TACommands.Init.getMaxResponseSize()));
        this.addCommandInfo(1, new TACommands.CommandInfo(LoadCerts.getMaxRequestSize(), LoadCerts.getMaxResponseSize()));
        this.addCommandInfo(2, new TACommands.CommandInfo(ProcessData.getMaxRequestSize(), ProcessData.getMaxResponseSize()));
        this.addCommandInfo(3, new TACommands.CommandInfo(GetNonce.getMaxRequestSize(), GetNonce.getMaxResponseSize()));
    }

    public static byte[] getMagicNumber() {
        return TL_MAGIC_NUM;
    }

    public static int getVersion() {
        return 0;
    }

    public static class CertData
    extends Blob {
        public CertData() {
            super(4096);
        }
    }

    public static class CertDataChain
    extends TAStruct {
        CertData[] certs;
        Struct.Unsigned32 numCerts = new Struct.Unsigned32();

        public CertDataChain(int n2) {
            this.certs = new CertData[n2];
            for (int i2 = 0; i2 < n2; ++i2) {
                this.certs[i2] = this.inner(new CertData());
            }
        }

        public int getCount() {
            return (int)this.numCerts.get();
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        public List<byte[]> getData() {
            int n2 = this.getCount();
            if (n2 == 0) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            int n3 = 0;
            while (n3 < n2) {
                arrayList.add((Object)this.certs[n3].getData());
                ++n3;
            }
            return arrayList;
        }

        /*
         * Enabled aggressive block sorting
         */
        public void setData(List<byte[]> list) {
            if (list != null && list.size() > this.certs.length) {
                c.e(CNCCCommands.TAG, "Error: Can set Cert Chain more than the size of the destination Cert Chain");
                return;
            } else {
                if (list != null && list.size() > 0) {
                    for (int i2 = 0; i2 < list.size(); ++i2) {
                        this.certs[i2].setData((byte[])list.get(i2));
                    }
                }
                if (list == null) return;
                {
                    this.numCerts.set(list.size());
                    return;
                }
            }
        }
    }

    public static class GetNonce {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(int n2) {
                byte[] arrby = new Data(n2).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 3, arrby);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned32 _nonceSize = new Struct.Unsigned32();

                public Data() {
                }

                public Data(int n2) {
                    this._nonceSize.set(n2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                public Blob out_data = this.inner(new Blob(8192));
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class LoadCerts {
        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(byte[] arrby, byte[] arrby2) {
                byte[] arrby3 = new Data(arrby, arrby2).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 1, arrby3);
            }

            public static class Data
            extends TAStruct {
                Blob deviceEncryptRSA2048PrivCert = this.inner(new Blob(4096));
                Blob deviceSignRSA2048PrivCert = this.inner(new Blob(4096));

                public Data() {
                }

                public Data(byte[] arrby, byte[] arrby2) {
                    this.deviceSignRSA2048PrivCert.setData(arrby);
                    this.deviceEncryptRSA2048PrivCert.setData(arrby2);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Blob deviceEncryptRSA2048PrivCert = this.inner(new Blob(4096));
                Blob deviceRootRSA2048PubCert = this.inner(new Blob(4096));
                Blob deviceSignRSA2048PubCert = this.inner(new Blob(4096));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

    public static class ProcessData {
        public static final int MAX_RSA_CERTS_IN_CHAIN = 3;

        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
        extends TACommandRequest {
            Request(List<byte[]> list, byte[] arrby, int n2, int n3, byte[] arrby2, byte[] arrby3) {
                byte[] arrby4 = new Data(list, arrby, n2, n3, arrby2, arrby3).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 2, arrby4);
            }

            public static class Data
            extends TAStruct {
                Blob data = this.inner(new Blob(8192));
                Struct.Unsigned8 dataType = new Struct.Unsigned8();
                Blob dstTAName = this.inner(new Blob(33));
                Struct.Unsigned8 option = new Struct.Unsigned8();
                CertDataChain serverCertChain = this.inner(new CertDataChain(3));
                Blob srcTAName = this.inner(new Blob(33));

                public Data() {
                }

                public Data(List<byte[]> list, byte[] arrby, int n2, int n3, byte[] arrby2, byte[] arrby3) {
                    this.data.setData(arrby);
                    this.dataType.set((short)n2);
                    this.option.set((short)n3);
                    this.serverCertChain.setData(list);
                    this.srcTAName.setData(arrby2);
                    this.dstTAName.setData(arrby3);
                }
            }

        }

        public static class Response
        extends TACommandResponse {
            public Data mRetVal = new Data();

            public Response(TACommandResponse tACommandResponse) {
                super(tACommandResponse.mResponseCode, tACommandResponse.mErrorMsg, tACommandResponse.mResponse);
                this.mRetVal.deserialize(this.mResponse);
            }

            public static class Data
            extends TAStruct {
                Blob data = this.inner(new Blob(8192));
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[])this.array((Struct.Member[])new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
            }

        }

    }

}

