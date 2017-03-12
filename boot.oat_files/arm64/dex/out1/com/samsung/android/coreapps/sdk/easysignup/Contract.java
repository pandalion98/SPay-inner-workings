package com.samsung.android.coreapps.sdk.easysignup;

import android.net.Uri;

@Deprecated
public class Contract {
    public static final Uri AUTH_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("auth").build();
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://com.samsung.android.coreapps.easysignup");
    public static final Uri BASE_CONTENT_URI_PUBLIC = Uri.parse("content://com.samsung.android.coreapps.easysignup.public");
    public static final String CONTENT_AUTHORITY = "com.samsung.android.coreapps.easysignup";
    public static final String CONTENT_AUTHORITY_PUBLIC = "com.samsung.android.coreapps.easysignup.public";
    public static final Uri GLD_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("gld").build();
    public static final Uri SERVICE_CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("service").build();
    public static final int SERVICE_ID_CIRCLE = 5;
    public static final int SERVICE_ID_CONTACT = 0;
    public static final int SERVICE_ID_ESU = 4;
    public static final int SERVICE_ID_FREE_MESSAGE = 1;
    public static final int SERVICE_ID_MVOIP = 6;
    public static final int SERVICE_ID_RSHARE = 2;
    public static final int SERVICE_ID_SHOP = 3;
    public static final int SERVICE_OFF = 0;
    public static final int SERVICE_ON = 1;
}
