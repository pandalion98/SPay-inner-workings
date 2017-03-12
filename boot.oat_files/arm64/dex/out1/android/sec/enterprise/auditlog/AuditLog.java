package android.sec.enterprise.auditlog;

import android.sec.enterprise.EnterpriseDeviceManager.EDMProxyServiceHelper;
import android.sec.enterprise.IEDMProxy;

public class AuditLog {
    public static final int ALERT = 1;
    public static final int AUDIT_LOG_GROUP_APPLICATION = 5;
    public static final int AUDIT_LOG_GROUP_EVENTS = 4;
    public static final int AUDIT_LOG_GROUP_NETWORK = 3;
    public static final int AUDIT_LOG_GROUP_SECURITY = 1;
    public static final int AUDIT_LOG_GROUP_SYSTEM = 2;
    public static final int CRITICAL = 2;
    public static final int ERROR = 3;
    public static final int NOTICE = 5;
    private static final String TAG = "AuditLog";
    public static final int WARNING = 4;

    public static void log(int severityGrade, int moduleGroup, boolean outcome, int uid, String swComponent, String logMessage) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.AuditLogger(severityGrade, moduleGroup, outcome, uid, swComponent, logMessage);
            }
        } catch (Exception e) {
        }
    }

    public static void logMMS(int severityGrade, int moduleGroup, boolean outcome, int pid, String swComponent, String logMessage) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.AuditLogger(severityGrade, moduleGroup, outcome, pid, swComponent, logMessage);
            }
        } catch (Exception e) {
        }
    }

    public static void logAsUser(int severityGrade, int moduleGroup, boolean outcome, int uid, String swComponent, String logMessage, int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.AuditLoggerAsUser(severityGrade, moduleGroup, outcome, uid, swComponent, logMessage, userId);
            }
        } catch (Exception e) {
        }
    }

    public static void logPrivileged(int severityGrade, int moduleGroup, boolean outcome, int pid, String swComponent, String logMessage) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.AuditLoggerPrivileged(severityGrade, moduleGroup, outcome, pid, swComponent, logMessage);
            }
        } catch (Exception e) {
        }
    }

    public static void logPrivilegedAsUser(int severityGrade, int moduleGroup, boolean outcome, int pid, String swComponent, String logMessage, int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                lService.AuditLoggerPrivilegedAsUser(severityGrade, moduleGroup, outcome, pid, swComponent, logMessage, userId);
            }
        } catch (Exception e) {
        }
    }

    public static boolean isAuditLogEnabledAsUser(int userId) {
        try {
            IEDMProxy lService = EDMProxyServiceHelper.getService();
            if (lService != null) {
                return lService.isAuditLogEnabledAsUser(userId);
            }
        } catch (Exception e) {
        }
        return false;
    }
}
