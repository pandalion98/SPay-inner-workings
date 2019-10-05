/*
 * Decompiled with CFR 0.0.
 *
 * Could not load the following classes:
 *  android.spay.TACommandRequest
 *  android.spay.TACommandResponse
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.Map
 */
package com.samsung.android.spaytzsvc.api;

import android.spay.TACommandRequest;
import android.spay.TACommandResponse;

import com.samsung.android.spaytzsvc.api.Blob;
import com.samsung.android.spaytzsvc.api.TAStruct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javolution.io.Struct;

public class TACommands {
    public static final int CMD_SPAY_INIT = 2147483633;
    public static final int CMD_SPAY_LOAD_PIN_RANDOM = 2147483634;
    public static final int CMD_SPAY_MOVE_SERVICE_KEY = 2147483635;
    public static final int CMD_SPAY_SETUP_PIN_RANDOM = 2147483636;
    private static final String TAG = "TACommands";
    public static final byte[] TL_MAGIC_NUM = new byte[]{0, 0, 0, 0};

    // TODO: From build number, not really sure what this is for
    public static final int TL_VERSION = 273801000;

    Map<Integer, CommandInfo> mCmdInfo = new HashMap();

    public TACommands() {
        this.addCommandInfo(2147483634, new CommandInfo(LoadPinRandom.getMaxRequestSize(), LoadPinRandom.getMaxResponseSize()));
        this.addCommandInfo(2147483635, new CommandInfo(MoveServiceKey.getMaxRequestSize(), MoveServiceKey.getMaxResponseSize()));
        this.addCommandInfo(2147483636, new CommandInfo(SetupPinRandom.getMaxRequestSize(), SetupPinRandom.getMaxResponseSize()));
    }

    public void addCommandInfo(Integer n2, CommandInfo commandInfo) {
        this.mCmdInfo.put(n2, commandInfo);
    }

    public void addCommands(Map<Integer, CommandInfo> map) {
        this.mCmdInfo.putAll(map);
    }

    public Map<Integer, CommandInfo> getAllCommands() {
        return this.mCmdInfo;
    }

    public CommandsInfo getCommandsInfo() {
        return new CommandsInfo(this.getMaxRequestSize((CommandInfo[]) this.mCmdInfo.values().toArray((Object[]) new CommandInfo[this.mCmdInfo.size()])), this.getMaxResponseSize((CommandInfo[]) this.mCmdInfo.values().toArray((Object[]) new CommandInfo[this.mCmdInfo.size()])));
    }

    public int getMaxRequestSize(CommandInfo[] arrcommandInfo) {
        if (arrcommandInfo == null || arrcommandInfo.length == 0) {
            return -1;
        }
        Integer n2 = arrcommandInfo[0].mMaxRequestSize;
        for (int i2 = 1; i2 < arrcommandInfo.length; ++i2) {
            if (arrcommandInfo[i2].mMaxRequestSize <= n2) continue;
            n2 = arrcommandInfo[i2].mMaxRequestSize;
        }
        return n2;
    }

    public int getMaxResponseSize(CommandInfo[] arrcommandInfo) {
        if (arrcommandInfo == null || arrcommandInfo.length == 0) {
            return -1;
        }
        Integer n2 = arrcommandInfo[0].mMaxResponseSize;
        for (int i2 = 1; i2 < arrcommandInfo.length; ++i2) {
            if (arrcommandInfo[i2].mMaxResponseSize <= n2) continue;
            n2 = arrcommandInfo[i2].mMaxResponseSize;
        }
        return n2;
    }

    public static class CommandInfo {
        int mMaxRequestSize;
        int mMaxResponseSize;

        public CommandInfo(int n2, int n3) {
            this.mMaxRequestSize = n2;
            this.mMaxResponseSize = n3;
        }
    }

    public static class CommandsInfo {
        int mMaxRequestSize;
        int mMaxResponseSize;

        public CommandsInfo(int n2, int n3) {
            this.mMaxRequestSize = n2;
            this.mMaxResponseSize = n3;
        }
    }

    public static class Init {
        private static final int TIMA_MSR_MAX_SIZE = 1024;
        public static final int TZ_COMMON_CLOSE_COMMUNICATION_ERROR = 65538;
        public static final int TZ_COMMON_COMMUNICATION_ERROR = 65537;
        public static final int TZ_COMMON_INIT_ERROR = 65546;
        public static final int TZ_COMMON_INIT_ERROR_TAMPER_FUSE_FAIL = 65548;
        public static final int TZ_COMMON_INIT_FAILED = 65540;
        public static final int TZ_COMMON_INIT_MSR_MISMATCH = 65549;
        public static final int TZ_COMMON_INIT_MSR_MODIFIED = 65550;
        public static final int TZ_COMMON_INIT_UNINITIALIZED_SECURE_MEM = 65547;
        public static final int TZ_COMMON_INTERNAL_ERROR = 65541;
        public static final int TZ_COMMON_NULL_POINTER_EXCEPTION = 65542;
        public static final int TZ_COMMON_OK = 0;
        public static final int TZ_COMMON_RESPONSE_REQUEST_MISMATCH = 65539;
        public static final int TZ_COMMON_UNDEFINED_ERROR = 65543;

        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
                extends TACommandRequest {
            public Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 2147483633, arrby2);
            }

            public static class Data
                    extends TAStruct {
                Blob measurement = this.inner(new Blob(1024));

                public Data() {
                }

                public Data(byte[] arrby) {
                    this.measurement.setData(arrby);
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
                public Struct.Unsigned32 result = new Struct.Unsigned32();
            }

        }

    }

    public static class LoadPinRandom {
        public static final int TZ_SPAY_PIN_RANDOM_SO_SIZE = 1024;

        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
                extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 2147483634, arrby2);
            }

            public static class Data
                    extends TAStruct {
                Blob pin_random_so = this.inner(new Blob(1024));

                public Data() {
                }

                public Data(byte[] arrby) {
                    this.pin_random_so.setData(arrby);
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
                Struct.Unsigned32 result = new Struct.Unsigned32();
            }

        }

    }

    public static class MoveServiceKey {
        public static final int TZ_SPAY_MAX_ERROR_STR_LEN = 256;
        public static final int TZ_SPAY_MAX_SK_LEN = 8192;
        public static final int TZ_SPAY_MSK_DECAP_ERROR = 2;
        public static final String TZ_SPAY_MSK_DECAP_ERROR_STR = "Could not decapsulate SKMM buffer";
        public static final int TZ_SPAY_MSK_NO_ERROR = 0;
        public static final String TZ_SPAY_MSK_NO_ERROR_STR = "Success";
        public static final int TZ_SPAY_MSK_PARAM_ERROR = 1;
        public static final String TZ_SPAY_MSK_PARAM_ERROR_STR = "Invalid params from NW";
        public static final int TZ_SPAY_MSK_UNKNOWN_ERROR = 4;
        public static final String TZ_SPAY_MSK_UNKNOWN_ERROR_STR = "Unknown error";
        public static final int TZ_SPAY_MSK_WRAP_ERROR = 3;
        public static final String TZ_SPAY_MSK_WRAP_ERROR_STR = "Could not custom wrap buffer";

        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
                extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 2147483635, arrby2);
            }

            public static class Data
                    extends TAStruct {
                Blob skmm_encap_msg = this.inner(new Blob(8192));

                public Data() {
                }

                public Data(byte[] arrby) {
                    this.skmm_encap_msg.setData(arrby);
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
                Struct.Unsigned8[] error_msg = (Struct.Unsigned8[]) this.array((Struct.Member[]) new Struct.Unsigned8[256]);
                Struct.Unsigned32 return_code = new Struct.Unsigned32();
                Blob wrapped_msg = this.inner(new Blob(8192));
            }

        }

    }

    public static class SetupPinRandom {
        public static final int TZ_SPAY_PIN_RANDOM_SO_SIZE = 1024;

        public static int getMaxRequestSize() {
            return new Request.Data().size();
        }

        public static int getMaxResponseSize() {
            return new Response.Data().size();
        }

        public static class Request
                extends TACommandRequest {
            Request(byte[] arrby) {
                byte[] arrby2 = new Data(arrby).serialize();
                this.init(0, TACommands.TL_MAGIC_NUM, 2147483636, arrby2);
            }

            public static class Data
                    extends TAStruct {
                Blob pin_random_so = this.inner(new Blob(1024));

                public Data() {
                }

                public Data(byte[] arrby) {
                    this.pin_random_so.setData(arrby);
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
                Blob pin_random_so = this.inner(new Blob(1024));
                Struct.Unsigned32 result = new Struct.Unsigned32();
            }

        }

    }
}

