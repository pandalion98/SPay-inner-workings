package android.support.v4.view.accessibility;

import android.os.Bundle;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import java.util.List;

class AccessibilityNodeProviderCompatKitKat {

    interface AccessibilityNodeInfoBridge {
        Object createAccessibilityNodeInfo(int i);

        List<Object> findAccessibilityNodeInfosByText(String str, int i);

        Object findFocus(int i);

        boolean performAction(int i, int i2, Bundle bundle);
    }

    /* renamed from: android.support.v4.view.accessibility.AccessibilityNodeProviderCompatKitKat.1 */
    static class C00491 extends AccessibilityNodeProvider {
        final /* synthetic */ AccessibilityNodeInfoBridge val$bridge;

        C00491(AccessibilityNodeInfoBridge accessibilityNodeInfoBridge) {
            this.val$bridge = accessibilityNodeInfoBridge;
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(int i) {
            return (AccessibilityNodeInfo) this.val$bridge.createAccessibilityNodeInfo(i);
        }

        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String str, int i) {
            return this.val$bridge.findAccessibilityNodeInfosByText(str, i);
        }

        public boolean performAction(int i, int i2, Bundle bundle) {
            return this.val$bridge.performAction(i, i2, bundle);
        }

        public AccessibilityNodeInfo findFocus(int i) {
            return (AccessibilityNodeInfo) this.val$bridge.findFocus(i);
        }
    }

    AccessibilityNodeProviderCompatKitKat() {
    }

    public static Object newAccessibilityNodeProviderBridge(AccessibilityNodeInfoBridge accessibilityNodeInfoBridge) {
        return new C00491(accessibilityNodeInfoBridge);
    }
}
