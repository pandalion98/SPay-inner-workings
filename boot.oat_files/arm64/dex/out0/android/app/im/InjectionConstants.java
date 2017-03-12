package android.app.im;

import android.content.Context;

public interface InjectionConstants {
    public static final boolean DEBUG_ELASTIC = true;
    public static final String DELIMITER = "#";

    public enum DispatchParentCall {
        ONCREATE,
        ONSTART,
        ONPAUSE,
        ONRESUME,
        ONSTOP,
        ONDESTROY,
        ONRESTART,
        ONSAVEINSTANCESTATE,
        ONRESTOREINSTANCESTATE,
        ONCONFIGURATIONCHANGED,
        ONSEARCHREQUESTED
    }

    public enum FeatureType {
        OPTIONS_MENU("options"),
        CONTEXT_MENU("context"),
        DYNAMIC_VIEW("dynamic_view"),
        PREFERENCE_HEADER("preference_header"),
        PREFERENCE("preference"),
        CONTENT_PROVIDER("content_provider"),
        SCALE_VIEW("scale_view");
        
        private final String text;

        private FeatureType(String text) {
            this.text = text;
        }

        public String toString() {
            return this.text;
        }
    }

    public enum TargetType {
        ACTIVITY(Context.ACTIVITY_SERVICE),
        FRAGMENT("fragment");
        
        private final String text;

        private TargetType(String text) {
            this.text = text;
        }

        public String toString() {
            return this.text;
        }
    }
}
