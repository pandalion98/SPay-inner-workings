/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ActivityNotFoundException
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Bundle
 *  java.lang.Exception
 *  java.lang.String
 */
package com.samsung.android.spayfw.core;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class PaymentFrameworkStoreManage
extends Activity {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.applications.InstalledAppDetailsTop");
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse((String)("package:" + "com.samsung.android.spay")));
        intent.setFlags(268435456);
        try {
            this.getApplicationContext().startActivity(intent);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        catch (ActivityNotFoundException activityNotFoundException) {}
        this.finish();
    }
}

