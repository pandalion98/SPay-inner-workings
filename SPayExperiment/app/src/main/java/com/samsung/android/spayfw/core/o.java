/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.core;

import com.samsung.android.spayfw.b.c;

public class o {
    private static final a jZ = new a();

    public static boolean aN() {
        return o.b(o.getState(), 32760);
    }

    public static boolean aO() {
        return o.b(o.getState(), 544);
    }

    private static boolean b(int n2, int n3) {
        return (n2 & n3) != 0;
    }

    public static int getState() {
        return jZ.getState();
    }

    public static boolean o(int n2) {
        return (n2 & 32763) != 0;
    }

    public static boolean p(int n2) {
        return o.b(o.getState(), n2);
    }

    public static boolean q(int n2) {
        return jZ.q(n2);
    }

    public static boolean r(int n2) {
        return jZ.r(n2);
    }

    private static class a {
        private static int ka = 1;

        private a() {
        }

        private static void a(int n2, int n3, boolean bl) {
            if (bl) {
                c.d("StateMachine", "state has changed from " + a.s(n2) + " to " + a.s(n3));
                return;
            }
            c.d("StateMachine", "state cannot be changed from " + a.s(n2) + " to " + a.s(n3));
        }

        private static String s(int n2) {
            switch (n2) {
                default: {
                    return "UNKNOWN_STATE";
                }
                case 1: {
                    return "NPAY_READY";
                }
                case 2: {
                    return "NPAY_SELECTED";
                }
                case 16: {
                    return "PAY_IDLE";
                }
                case 32: {
                    return "PAY_NFC";
                }
                case 64: {
                    return "PAY_INIT_MST";
                }
                case 8: {
                    return "PAY_SEC_MST";
                }
                case 512: {
                    return "PAY_NFC_STOPPING";
                }
                case 1024: {
                    return "PAY_INIT_MST_STOPPING";
                }
                case 2048: {
                    return "PAY_SEC_MST_STOPPING";
                }
                case 3072: {
                    return "PAY_MST_STOPPING";
                }
                case 256: {
                    return "PAY_IDLE_STOPPING";
                }
                case 24320: {
                    return "PAY_STOPPING";
                }
                case 128: {
                    return "PAY_ECOMMERCE";
                }
                case 4096: {
                    return "PAY_ECOMMERCE_STOPPING";
                }
                case 8192: {
                    return "PAY_EXTRACT_CARDDETAIL";
                }
                case 16384: 
            }
            return "PAY_EXTRACT_CARDDETAIL_STOPPING";
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public boolean a(int n2, boolean bl) {
            boolean bl2 = false;
            a a2 = this;
            synchronized (a2) {
                if (!o.o(n2)) {
                    c.d("StateMachine", "the new state " + n2 + " is not valid");
                } else {
                    int n3 = ka;
                    if (!o.b(n3, n2)) {
                        switch (n3) {
                            default: {
                                c.d("StateMachine", "wrong state " + n2);
                                return false;
                            }
                            case 1: {
                                bl2 = o.b(n2, 2);
                                break;
                            }
                            case 2: {
                                bl2 = o.b(n2, 24337);
                                break;
                            }
                            case 16: {
                                bl2 = o.b(n2, 32737);
                                if (o.b(n2, 24320)) {
                                    n2 = 256;
                                    break;
                                }
                                if (!o.b(n2, 64)) break;
                                n2 = 64;
                                break;
                            }
                            case 32: {
                                bl2 = o.b(n2, 24329);
                                if (o.b(n2, 24320)) {
                                    n2 = 512;
                                    break;
                                }
                                if (!o.b(n2, 8)) break;
                                n2 = 8;
                                break;
                            }
                            case 64: {
                                bl2 = o.b(n2, 24353);
                                if (!o.b(n2, 24320)) break;
                                n2 = 1024;
                                break;
                            }
                            case 8: {
                                bl2 = o.b(n2, 24321);
                                if (!o.b(n2, 24320)) break;
                                n2 = 2048;
                                break;
                            }
                            case 128: {
                                bl2 = o.b(n2, 4097);
                                break;
                            }
                            case 8192: {
                                bl2 = o.b(n2, 16387);
                                break;
                            }
                            case 256: 
                            case 512: 
                            case 1024: 
                            case 2048: 
                            case 3072: 
                            case 4096: 
                            case 16384: 
                            case 24320: {
                                bl2 = o.b(n2, 1);
                            }
                        }
                        if (bl) {
                            if (bl2) {
                                ka = n2;
                            }
                            a.a(n3, n2, bl2);
                        } else {
                            c.d("StateMachine", "check state from " + a.s(n3) + " to " + a.s(n2));
                        }
                    } else {
                        if (bl) {
                            a.a(n3, n2, true);
                            return true;
                        } else {
                            c.d("StateMachine", "check state from " + a.s(n3) + " to " + a.s(n2));
                        }
                        return true;
                    }
                }
                return bl2;
            }
        }

        public int getState() {
            return ka;
        }

        public boolean q(int n2) {
            return this.a(n2, true);
        }

        public boolean r(int n2) {
            return this.a(n2, false);
        }
    }

}

