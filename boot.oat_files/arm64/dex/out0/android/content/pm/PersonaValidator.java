package android.content.pm;

import android.content.pm.IPersonaValidator.Stub;
import android.util.Log;

public abstract class PersonaValidator {
    public static final String METHOD_RESETPERSONA = "resetPersona";
    private static final String TAG = "Abstract-PersonaValidator";
    private LocalBinder mActualValidator;
    private final Object mLock = new Object();

    private static final class LocalBinder extends Stub {
        private PersonaValidator mValidator;

        public LocalBinder(PersonaValidator validator) {
            this.mValidator = validator;
        }

        public int validateCallerForAPI(long callerId, String methodName, int personaId) {
            if (this.mValidator != null) {
                Log.d(PersonaValidator.TAG, " LocalBinder:validateCallerForAPI() callerid :" + callerId + " method :" + methodName);
                return this.mValidator.validateCallerForAPI(callerId, methodName, personaId);
            }
            Log.d(PersonaValidator.TAG, "LocalBinder:(no validator assigned) validateCallerForAPI() callerid :" + callerId + " method :" + methodName);
            return -1;
        }
    }

    public IPersonaValidator getValidator() {
        IPersonaValidator iPersonaValidator;
        synchronized (this.mLock) {
            if (this.mActualValidator == null) {
                this.mActualValidator = new LocalBinder(this);
            }
            iPersonaValidator = this.mActualValidator;
        }
        return iPersonaValidator;
    }

    public int validateCallerForAPI(long callerId, String methodName, int personaId) {
        Log.d(TAG, "Dummy: validateCallerForAPI() callerid :" + callerId + " method :" + methodName);
        return -1;
    }
}
