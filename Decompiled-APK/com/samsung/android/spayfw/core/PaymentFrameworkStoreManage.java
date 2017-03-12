package com.samsung.android.spayfw.core;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class PaymentFrameworkStoreManage extends Activity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.applications.InstalledAppDetailsTop");
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + "com.samsung.android.spay"));
        intent.setFlags(268435456);
        try {
            getApplicationContext().startActivity(intent);
        } catch (ActivityNotFoundException e) {
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        finish();
    }
}
