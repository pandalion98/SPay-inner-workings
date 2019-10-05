/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 *  java.util.List
 */
package com.samsung.android.spayfw.core;

import android.content.Context;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.b.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class a {
    private static a iJ = null;
    private String iH = null;
    private List<c> iI = null;
    private Context mContext;

    private a(Context context, String string) {
        this.iH = string;
        this.mContext = context;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static a a(Context context, String string) {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            block7 : {
                Log.d("Account", "Account getInstance accId = " + string);
                if (string != null) break block7;
                Log.d("Account", "Account getInstance accId is null");
                return iJ;
            }
            if (iJ == null) {
                Log.d("Account", "Account getInstance mAccount is null");
                iJ = new a(context, string);
                return iJ;
            }
            if (iJ.getAccountId().equals((Object)string)) {
                Log.d("Account", "Account getInstance mAccId equals accId");
                return iJ;
            }
            Log.d("Account", "Account getInstance accId = " + string + "mAccId = " + iJ.getAccountId());
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private int x(String string) {
        block7 : {
            block6 : {
                if (string == null) break block6;
                if (string.contains((CharSequence)"ALL")) {
                    return 15;
                }
                if (string.contains((CharSequence)"MST")) {
                    return 2;
                }
                if (string.contains((CharSequence)"NFC")) {
                    return 1;
                }
                if (string.contains((CharSequence)"ECM")) break block7;
            }
            return 0;
        }
        return 4;
    }

    public List<c> W() {
        a a2 = this;
        synchronized (a2) {
            List<c> list = this.iI;
            return list;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public c X() {
        a a2 = this;
        synchronized (a2) {
            String string;
            block4 : {
                string = e.h(this.mContext).getConfig("CONFIG_DEFAULT_CARD");
                if (string != null) break block4;
                return null;
            }
            c c2 = this.r(string);
            return c2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public c a(EnrollCardInfo enrollCardInfo) {
        a a2 = this;
        synchronized (a2) {
            String string;
            c c2;
            String string2;
            block11 : {
                String string3;
                if (enrollCardInfo instanceof EnrollCardPanInfo) {
                    BinAttribute binAttribute = BinAttribute.getBinAttribute(((EnrollCardPanInfo)enrollCardInfo).getPAN());
                    Log.d("Account", "BinAttribute : EnrollCardPanInfo : " + ((EnrollCardPanInfo)enrollCardInfo).getPAN());
                    string3 = binAttribute != null ? binAttribute.getCardBrand() : null;
                }
                if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
                    Log.d("Account", "getCardBrand");
                    string = ((EnrollCardReferenceInfo)enrollCardInfo).getCardBrand();
                    string2 = ((EnrollCardReferenceInfo)enrollCardInfo).getCardType();
                } else if (enrollCardInfo instanceof EnrollCardLoyaltyInfo) {
                    Log.d("Account", "PaymentFramework.CARD_BRAND_LOYALTY");
                    string = "LO";
                    string2 = null;
                } else {
                    string = null;
                    string2 = null;
                }
                break block11;
                string = string3;
                string2 = null;
            }
            if (string != null) {
                int n2 = this.x(enrollCardInfo.getCardPresentationMode());
                if ("PL".equals((Object)string)) {
                    n2 &= -2;
                }
                c2 = new c(this.mContext, string, string2, enrollCardInfo.getCardEntryMode(), n2);
                if (this.iI == null) {
                    this.iI = new ArrayList();
                }
            } else {
                Log.e("Account", "No valid cardType is found");
                return null;
            }
            this.iI.add((Object)c2);
            return c2;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean a(c c2) {
        a a2 = this;
        synchronized (a2) {
            boolean bl = false;
            if (c2 == null) return bl;
            if (this.iI == null) {
                this.iI = new ArrayList();
            }
            this.iI.add((Object)c2);
            return true;
        }
    }

    public String getAccountId() {
        return this.iH;
    }

    public boolean p(String string) {
        String string2 = this.iH;
        boolean bl = false;
        if (string2 != null) {
            boolean bl2 = this.iH.equals((Object)string);
            bl = false;
            if (bl2) {
                bl = true;
            }
        }
        return bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public c q(String string) {
        a a2 = this;
        synchronized (a2) {
            c c2;
            boolean bl;
            if (this.iI == null) return null;
            if (this.iI.isEmpty()) return null;
            if (string == null) return null;
            Iterator iterator = this.iI.iterator();
            do {
                if (!iterator.hasNext()) return null;
            } while ((c2 = (c)iterator.next()).getEnrollmentId() == null || !(bl = string.equals((Object)c2.getEnrollmentId())));
            return c2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public c r(String string) {
        a a2 = this;
        synchronized (a2) {
            boolean bl;
            c c2;
            if (string == null) {
                return null;
            }
            if (this.iI == null) return null;
            if (this.iI.isEmpty()) return null;
            Iterator iterator = this.iI.iterator();
            do {
                if (!iterator.hasNext()) return null;
            } while (!(bl = string.equals((Object)(c2 = (c)iterator.next()).ac().getTokenId())));
            return c2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean s(String string) {
        a a2 = this;
        synchronized (a2) {
            if (this.iI == null) return false;
            if (this.iI.isEmpty()) return false;
            if (string == null) return false;
            int n2 = 0;
            while (n2 < this.iI.size()) {
                c c2 = (c)this.iI.get(n2);
                if (c2.getEnrollmentId() != null && c2.getEnrollmentId().equals((Object)string)) {
                    c2.delete();
                    Log.d("Account", "deleteCardByEnrollmentId: done for " + string);
                    this.iI.remove(n2);
                    return true;
                }
                ++n2;
            }
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean t(String string) {
        a a2 = this;
        synchronized (a2) {
            if (this.iI == null) return false;
            if (this.iI.isEmpty()) return false;
            if (string == null) return false;
            int n2 = 0;
            while (n2 < this.iI.size()) {
                c c2 = (c)this.iI.get(n2);
                if (c2.ac().getTokenId() != null && c2.ac().getTokenId().equals((Object)string)) {
                    c2.delete();
                    Log.d("Account", "deleteCardByTokenId: done for " + string);
                    this.iI.remove(n2);
                    return true;
                }
                ++n2;
            }
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String u(String string) {
        a a2 = this;
        synchronized (a2) {
            if (string == null) {
                return "NONE";
            }
            List<c> list = this.iI;
            String string2 = null;
            if (list != null) {
                boolean bl = this.iI.isEmpty();
                string2 = null;
                if (!bl) {
                    String string3 = null;
                    for (int i2 = 0; i2 < this.iI.size(); ++i2) {
                        String string4;
                        c c2 = (c)this.iI.get(i2);
                        if (c2.getCardBrand().equals((Object)string) && c2.ac() != null && c2.ac().getTokenId() != null && c2.ac().getTokenStatus() != null) {
                            if ("ENROLLED".equals((Object)c2.ac().getTokenStatus())) {
                                string4 = string3;
                            } else {
                                String string5 = c2.ac().getTokenId();
                                String string6 = m.F(c2.ac().getTokenStatus());
                                string4 = string5 + ":" + string6;
                                if (string3 != null) {
                                    string4 = string3 + "," + string4;
                                }
                            }
                        } else {
                            string4 = string3;
                        }
                        string3 = string4;
                    }
                    string2 = string3;
                }
            }
            if (string2 != null) return string2;
            return "NONE";
        }
    }
}

