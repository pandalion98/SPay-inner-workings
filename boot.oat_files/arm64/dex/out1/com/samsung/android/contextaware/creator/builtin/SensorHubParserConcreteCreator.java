package com.samsung.android.contextaware.creator.builtin;

import android.content.Context;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.OrientationParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.PowerResetNotiParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubDebugMsgParser;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.DATA_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProtocol.LIB_TYPE;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubParserProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.TypeParser;

public class SensorHubParserConcreteCreator {
    private static PowerResetNotiParser sPowerResetNotiObservable;
    private final Context mContext;

    private enum SensorHubNonLibParserList {
        ORIENTATION(ContextType.SENSORHUB_PARSER_NONLIB_ORIENTATION.getCode()) {
            protected final void createObj(Context context) {
                registerParser(DATA_TYPE.NONLIBRARY_DATATYPE_ORIENTATION.toString(), new OrientationParser());
            }
        },
        POWER_RESET(ContextType.SENSORHUB_PARSER_NOTI_POWER_RESET.getCode()) {
            protected final void createObj(Context context) {
                SensorHubParserConcreteCreator.sPowerResetNotiObservable = new PowerResetNotiParser(context);
                registerParser(LIB_TYPE.TYPE_NOTI_POWER.toString(), SensorHubParserConcreteCreator.sPowerResetNotiObservable);
            }
        },
        SENSORHUB_DEBUG_MSG(ContextType.SENSORHUB_PARSER_SENSORHUB_DEBUG_MSG.getCode()) {
            protected final void createObj(Context context) {
                registerParser(LIB_TYPE.TYPE_SENSORHUB_DEBUG_MSG.toString(), new SensorHubDebugMsgParser());
            }
        };
        
        private final String name;

        protected abstract void createObj(Context context);

        private SensorHubNonLibParserList(String name) {
            this.name = name;
        }

        protected final void registerParser(String key, ISensorHubParser parser) {
            TypeParser libParser = SensorHubParserProvider.getInstance().getLibParser();
            if (libParser != null) {
                libParser.registerParser(key, parser);
            }
        }
    }

    public SensorHubParserConcreteCreator(Context context) {
        this.mContext = context;
        create();
    }

    private void create() {
        SensorHubParserProvider.getInstance().initialize(this.mContext);
        for (SensorHubNonLibParserList i : SensorHubNonLibParserList.values()) {
            i.createObj(this.mContext);
        }
    }

    public final ISensorHubResetObservable getPowerObservable() {
        return sPowerResetNotiObservable;
    }
}
