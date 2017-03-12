package android.sec.clipboard.util;

import android.content.ContentResolver;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.VoicemailContract.Voicemails;
import android.sec.clipboard.data.ClipboardConstants;
import android.util.secutil.Log;
import com.android.internal.R;
import com.samsung.android.cocktailbar.AbsCocktailLoadablePanel;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ClipboardDataBitmapUrl {
    private static final String TAG = "ClipboardDataBitmapUrl";

    public static Bitmap getResizeBitmap(byte[] bytes, int reqWidth, int reqHeight) {
        int sampleSize = 2;
        Options bitmapOption = new Options();
        bitmapOption.inJustDecodeBounds = true;
        bitmapOption.inPurgeable = true;
        try {
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bitmapOption);
            while (bitmapOption.outWidth / sampleSize >= reqWidth && bitmapOption.outHeight / sampleSize >= reqHeight) {
                sampleSize++;
            }
            bitmapOption.inSampleSize = sampleSize - 1;
            try {
                bitmapOption.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bitmapOption);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static Bitmap downloadSimpleBitmap(String urlname, int reqWidth, int reqHeight) {
        URL url;
        URLConnection connection;
        Bitmap Result;
        MalformedURLException e;
        IOException e2;
        OutOfMemoryError e3;
        URL url2 = null;
        int sampleSize = 2;
        Options bitmapOption = new Options();
        bitmapOption.inJustDecodeBounds = true;
        bitmapOption.inPurgeable = true;
        try {
            Log.d(TAG, "url : " + null);
            url = new URL(urlname);
            try {
                connection = url.openConnection();
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(3000);
                Result = BitmapFactory.decodeStream(connection.getInputStream(), null, bitmapOption);
            } catch (MalformedURLException e4) {
                e = e4;
                url2 = url;
                e.printStackTrace();
                Result = null;
                url = url2;
                if (bitmapOption != null) {
                }
                while (bitmapOption.outWidth / sampleSize >= reqWidth) {
                    sampleSize++;
                }
                bitmapOption.inSampleSize = sampleSize - 1;
                try {
                    bitmapOption.inJustDecodeBounds = false;
                    if (url == null) {
                        url2 = url;
                    } else {
                        url2 = new URL(urlname);
                    }
                    try {
                        connection = url2.openConnection();
                        connection.setConnectTimeout(2000);
                        connection.setReadTimeout(3000);
                        return BitmapFactory.decodeStream(connection.getInputStream(), null, bitmapOption);
                    } catch (MalformedURLException e5) {
                        e = e5;
                        e.printStackTrace();
                        return null;
                    } catch (IOException e6) {
                        e2 = e6;
                        e2.printStackTrace();
                        return null;
                    } catch (OutOfMemoryError e7) {
                        e3 = e7;
                        e3.printStackTrace();
                        return null;
                    }
                } catch (MalformedURLException e8) {
                    e = e8;
                    url2 = url;
                    e.printStackTrace();
                    return null;
                } catch (IOException e9) {
                    e2 = e9;
                    url2 = url;
                    e2.printStackTrace();
                    return null;
                } catch (OutOfMemoryError e10) {
                    e3 = e10;
                    url2 = url;
                    e3.printStackTrace();
                    return null;
                }
            } catch (IOException e11) {
                e2 = e11;
                url2 = url;
                e2.printStackTrace();
                return null;
            } catch (OutOfMemoryError e12) {
                e3 = e12;
                url2 = url;
                e3.printStackTrace();
                Result = null;
                url = url2;
                if (bitmapOption != null) {
                }
                while (bitmapOption.outWidth / sampleSize >= reqWidth) {
                    sampleSize++;
                }
                bitmapOption.inSampleSize = sampleSize - 1;
                bitmapOption.inJustDecodeBounds = false;
                if (url == null) {
                    url2 = new URL(urlname);
                } else {
                    url2 = url;
                }
                connection = url2.openConnection();
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(3000);
                return BitmapFactory.decodeStream(connection.getInputStream(), null, bitmapOption);
            }
        } catch (MalformedURLException e13) {
            e = e13;
            e.printStackTrace();
            Result = null;
            url = url2;
            if (bitmapOption != null) {
            }
            while (bitmapOption.outWidth / sampleSize >= reqWidth) {
                sampleSize++;
            }
            bitmapOption.inSampleSize = sampleSize - 1;
            bitmapOption.inJustDecodeBounds = false;
            if (url == null) {
                url2 = url;
            } else {
                url2 = new URL(urlname);
            }
            connection = url2.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(3000);
            return BitmapFactory.decodeStream(connection.getInputStream(), null, bitmapOption);
        } catch (IOException e14) {
            e2 = e14;
            e2.printStackTrace();
            return null;
        } catch (OutOfMemoryError e15) {
            e3 = e15;
            e3.printStackTrace();
            Result = null;
            url = url2;
            if (bitmapOption != null) {
            }
            while (bitmapOption.outWidth / sampleSize >= reqWidth) {
                sampleSize++;
            }
            bitmapOption.inSampleSize = sampleSize - 1;
            bitmapOption.inJustDecodeBounds = false;
            if (url == null) {
                url2 = new URL(urlname);
            } else {
                url2 = url;
            }
            connection = url2.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(3000);
            return BitmapFactory.decodeStream(connection.getInputStream(), null, bitmapOption);
        }
        if ((bitmapOption != null || bitmapOption.outWidth > -1) && bitmapOption.outHeight > -1) {
            while (bitmapOption.outWidth / sampleSize >= reqWidth && bitmapOption.outHeight / sampleSize >= reqHeight) {
                sampleSize++;
            }
            bitmapOption.inSampleSize = sampleSize - 1;
            bitmapOption.inJustDecodeBounds = false;
            if (url == null) {
                url2 = new URL(urlname);
            } else {
                url2 = url;
            }
            connection = url2.openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(3000);
            return BitmapFactory.decodeStream(connection.getInputStream(), null, bitmapOption);
        }
        android.util.Log.i(TAG, "Return null because received bitmap size is invalid. bitmapOption.outWidth :" + bitmapOption.outWidth + ", bitmapOption.outHeight :" + bitmapOption.outHeight);
        url2 = url;
        return Result;
    }

    public static Bitmap getFilePathBitmap(String fileName, int reqWidth, int reqHeight) {
        int sampleSize = 2;
        Options bitmapOption = new Options();
        bitmapOption.inJustDecodeBounds = true;
        bitmapOption.inPurgeable = true;
        try {
            Bitmap bm = BitmapFactory.decodeFile(fileName, bitmapOption);
            while (bitmapOption.outWidth / sampleSize >= reqWidth && bitmapOption.outHeight / sampleSize >= reqHeight) {
                sampleSize++;
            }
            bitmapOption.inSampleSize = sampleSize - 1;
            try {
                bitmapOption.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(fileName, bitmapOption);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        } catch (Exception e2) {
            if (ClipboardConstants.DEBUG) {
                android.util.Log.e(TAG, "exception arised during bm = BitmapFactory.decodeFile(((ClipboardDataBitmap) cbData).GetBitmapPath());");
            }
            return null;
        }
    }

    public static Bitmap getUriPathBitmap(ContentResolver contentResolver, Uri uri, int reqWidth, int reqHeight) {
        int sampleSize = 2;
        Options bitmapOption = new Options();
        bitmapOption.inJustDecodeBounds = true;
        bitmapOption.inPurgeable = true;
        if (contentResolver == null) {
            return null;
        }
        try {
            Bitmap bm = BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, bitmapOption);
            while (bitmapOption.outWidth / sampleSize >= reqWidth && bitmapOption.outHeight / sampleSize >= reqHeight) {
                sampleSize++;
            }
            bitmapOption.inSampleSize = sampleSize - 1;
            try {
                bitmapOption.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, bitmapOption);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int degree = findImageDegree(contentResolver, uri);
            if (degree != 0) {
                bm = rotateBitmap(bm, degree);
            }
            return bm;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static int findImageDegree(ContentResolver contentResolver, Uri uri) {
        int orientation = -1;
        String _data = null;
        if (AbsCocktailLoadablePanel.LOADABLE_CONTENT_CLASS.equals(uri.getScheme())) {
            Cursor c = null;
            try {
                c = contentResolver.query(uri, null, null, null, null);
                if (c != null && c.moveToNext()) {
                    int columnIdx = c.getColumnIndex(Voicemails._DATA);
                    if (columnIdx != -1) {
                        _data = c.getString(columnIdx);
                    }
                    columnIdx = c.getColumnIndex("orientation");
                    if (columnIdx != -1) {
                        try {
                            orientation = Integer.parseInt(c.getString(columnIdx));
                        } catch (NumberFormatException nfe) {
                            nfe.printStackTrace();
                        }
                    }
                }
                if (c != null) {
                    c.close();
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
                if (c != null) {
                    c.close();
                }
            } catch (Throwable th) {
                if (c != null) {
                    c.close();
                }
            }
        } else if ("file".equals(uri.getScheme())) {
            _data = uri.getPath();
        }
        if (orientation != -1) {
            return orientation;
        }
        if (_data == null) {
            return 0;
        }
        try {
            return getExifOrientation(_data);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static int getExifOrientation(String filepath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (exif == null) {
            return 0;
        }
        int orientation = exif.getAttributeInt("Orientation", -1);
        if (orientation == -1) {
            return 0;
        }
        switch (orientation) {
            case 3:
                return 180;
            case 6:
                return 90;
            case 8:
                return R.styleable.Theme_textAppearanceEasyCorrectSuggestion;
            default:
                return 0;
        }
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || bitmap == null) {
            return bitmap;
        }
        Matrix m = new Matrix();
        m.setRotate((float) degrees, ((float) bitmap.getWidth()) / 2.0f, ((float) bitmap.getHeight()) / 2.0f);
        try {
            Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            if (bitmap == converted) {
                return bitmap;
            }
            bitmap.recycle();
            return converted;
        } catch (OutOfMemoryError ex) {
            ex.printStackTrace();
            return bitmap;
        }
    }
}
