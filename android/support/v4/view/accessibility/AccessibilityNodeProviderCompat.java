package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
    private static final AccessibilityNodeProviderImpl IMPL;
    private final Object mProvider;

    interface AccessibilityNodeProviderImpl {
        Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat);
    }

    static class AccessibilityNodeProviderStubImpl implements AccessibilityNodeProviderImpl {
        AccessibilityNodeProviderStubImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return null;
        }
    }

    static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderStubImpl {

        /* renamed from: android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderJellyBeanImpl.1 */
        class C00461 implements AccessibilityNodeInfoBridge {
            final /* synthetic */ AccessibilityNodeProviderCompat val$compat;

            C00461(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
                this.val$compat = accessibilityNodeProviderCompat;
            }

            public boolean performAction(int i, int i2, Bundle bundle) {
                return this.val$compat.performAction(i, i2, bundle);
            }

            public List<Object> findAccessibilityNodeInfosByText(String str, int i) {
                List findAccessibilityNodeInfosByText = this.val$compat.findAccessibilityNodeInfosByText(str, i);
                List<Object> arrayList = new ArrayList();
                int size = findAccessibilityNodeInfosByText.size();
                for (int i2 = 0; i2 < size; i2++) {
                    arrayList.add(((AccessibilityNodeInfoCompat) findAccessibilityNodeInfosByText.get(i2)).getInfo());
                }
                return arrayList;
            }

            public Object createAccessibilityNodeInfo(int i) {
                AccessibilityNodeInfoCompat createAccessibilityNodeInfo = this.val$compat.createAccessibilityNodeInfo(i);
                if (createAccessibilityNodeInfo == null) {
                    return null;
                }
                return createAccessibilityNodeInfo.getInfo();
            }
        }

        AccessibilityNodeProviderJellyBeanImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new C00461(accessibilityNodeProviderCompat));
        }
    }

    static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderStubImpl {

        /* renamed from: android.support.v4.view.accessibility.AccessibilityNodeProviderCompat.AccessibilityNodeProviderKitKatImpl.1 */
        class C00471 implements AccessibilityNodeInfoBridge {
            final /* synthetic */ AccessibilityNodeProviderCompat val$compat;

            C00471(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
                this.val$compat = accessibilityNodeProviderCompat;
            }

            public boolean performAction(int i, int i2, Bundle bundle) {
                return this.val$compat.performAction(i, i2, bundle);
            }

            public List<Object> findAccessibilityNodeInfosByText(String str, int i) {
                List findAccessibilityNodeInfosByText = this.val$compat.findAccessibilityNodeInfosByText(str, i);
                List<Object> arrayList = new ArrayList();
                int size = findAccessibilityNodeInfosByText.size();
                for (int i2 = 0; i2 < size; i2++) {
                    arrayList.add(((AccessibilityNodeInfoCompat) findAccessibilityNodeInfosByText.get(i2)).getInfo());
                }
                return arrayList;
            }

            public Object createAccessibilityNodeInfo(int i) {
                AccessibilityNodeInfoCompat createAccessibilityNodeInfo = this.val$compat.createAccessibilityNodeInfo(i);
                if (createAccessibilityNodeInfo == null) {
                    return null;
                }
                return createAccessibilityNodeInfo.getInfo();
            }

            public Object findFocus(int i) {
                AccessibilityNodeInfoCompat findFocus = this.val$compat.findFocus(i);
                if (findFocus == null) {
                    return null;
                }
                return findFocus.getInfo();
            }
        }

        AccessibilityNodeProviderKitKatImpl() {
        }

        public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new C00471(accessibilityNodeProviderCompat));
        }
    }

    static {
        if (VERSION.SDK_INT >= 19) {
            IMPL = new AccessibilityNodeProviderKitKatImpl();
        } else if (VERSION.SDK_INT >= 16) {
            IMPL = new AccessibilityNodeProviderJellyBeanImpl();
        } else {
            IMPL = new AccessibilityNodeProviderStubImpl();
        }
    }

    public AccessibilityNodeProviderCompat() {
        this.mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
    }

    public AccessibilityNodeProviderCompat(Object obj) {
        this.mProvider = obj;
    }

    public Object getProvider() {
        return this.mProvider;
    }

    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int i) {
        return null;
    }

    public boolean performAction(int i, int i2, Bundle bundle) {
        return false;
    }

    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String str, int i) {
        return null;
    }

    public AccessibilityNodeInfoCompat findFocus(int i) {
        return null;
    }
}
