package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument.Page;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.CancellationSignal.OnCancelListener;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintAttributes.MediaSize;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentAdapter.LayoutResultCallback;
import android.print.PrintDocumentAdapter.WriteResultCallback;
import android.print.PrintDocumentInfo;
import android.print.PrintDocumentInfo.Builder;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class PrintHelperKitkat {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode;
    final Context mContext;
    Options mDecodeOptions;
    private final Object mLock;
    int mOrientation;
    int mScaleMode;

    /* renamed from: android.support.v4.print.PrintHelperKitkat.1 */
    class C00241 extends PrintDocumentAdapter {
        private PrintAttributes mAttributes;
        final /* synthetic */ Bitmap val$bitmap;
        final /* synthetic */ int val$fittingMode;
        final /* synthetic */ String val$jobName;

        C00241(String str, Bitmap bitmap, int i) {
            this.val$jobName = str;
            this.val$bitmap = bitmap;
            this.val$fittingMode = i;
        }

        public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
            boolean z = true;
            this.mAttributes = printAttributes2;
            PrintDocumentInfo build = new Builder(this.val$jobName).setContentType(PrintHelperKitkat.SCALE_MODE_FIT).setPageCount(PrintHelperKitkat.SCALE_MODE_FIT).build();
            if (printAttributes2.equals(printAttributes)) {
                z = false;
            }
            layoutResultCallback.onLayoutFinished(build, z);
        }

        public void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
            PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
            try {
                Page startPage = printedPdfDocument.startPage(PrintHelperKitkat.SCALE_MODE_FIT);
                startPage.getCanvas().drawBitmap(this.val$bitmap, PrintHelperKitkat.this.getMatrix(this.val$bitmap.getWidth(), this.val$bitmap.getHeight(), new RectF(startPage.getInfo().getContentRect()), this.val$fittingMode), null);
                printedPdfDocument.finishPage(startPage);
                printedPdfDocument.writeTo(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                PageRange[] pageRangeArr2 = new PageRange[PrintHelperKitkat.SCALE_MODE_FIT];
                pageRangeArr2[0] = PageRange.ALL_PAGES;
                writeResultCallback.onWriteFinished(pageRangeArr2);
            } catch (Throwable e) {
                Log.e(PrintHelperKitkat.LOG_TAG, "Error writing printed content", e);
                writeResultCallback.onWriteFailed(null);
            } catch (Throwable th) {
                if (printedPdfDocument != null) {
                    printedPdfDocument.close();
                }
                if (parcelFileDescriptor != null) {
                    try {
                        parcelFileDescriptor.close();
                    } catch (IOException e2) {
                    }
                }
            }
            if (printedPdfDocument != null) {
                printedPdfDocument.close();
            }
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e3) {
                }
            }
        }
    }

    /* renamed from: android.support.v4.print.PrintHelperKitkat.2 */
    class C00272 extends PrintDocumentAdapter {
        AsyncTask<Uri, Boolean, Bitmap> loadBitmap;
        private PrintAttributes mAttributes;
        Bitmap mBitmap;
        final /* synthetic */ int val$fittingMode;
        final /* synthetic */ Uri val$imageFile;
        final /* synthetic */ String val$jobName;

        /* renamed from: android.support.v4.print.PrintHelperKitkat.2.1 */
        class C00261 extends AsyncTask<Uri, Boolean, Bitmap> {
            final /* synthetic */ CancellationSignal val$cancellationSignal;
            final /* synthetic */ LayoutResultCallback val$layoutResultCallback;
            final /* synthetic */ PrintAttributes val$newPrintAttributes;
            final /* synthetic */ PrintAttributes val$oldPrintAttributes;

            /* renamed from: android.support.v4.print.PrintHelperKitkat.2.1.1 */
            class C00251 implements OnCancelListener {
                C00251() {
                }

                public void onCancel() {
                    C00272.this.cancelLoad();
                    C00261.this.cancel(false);
                }
            }

            C00261(CancellationSignal cancellationSignal, PrintAttributes printAttributes, PrintAttributes printAttributes2, LayoutResultCallback layoutResultCallback) {
                this.val$cancellationSignal = cancellationSignal;
                this.val$newPrintAttributes = printAttributes;
                this.val$oldPrintAttributes = printAttributes2;
                this.val$layoutResultCallback = layoutResultCallback;
            }

            protected void onPreExecute() {
                this.val$cancellationSignal.setOnCancelListener(new C00251());
            }

            protected Bitmap doInBackground(Uri... uriArr) {
                try {
                    return PrintHelperKitkat.this.loadConstrainedBitmap(C00272.this.val$imageFile, PrintHelperKitkat.MAX_PRINT_SIZE);
                } catch (FileNotFoundException e) {
                    return null;
                }
            }

            protected void onPostExecute(Bitmap bitmap) {
                boolean z = true;
                super.onPostExecute(bitmap);
                C00272.this.mBitmap = bitmap;
                if (bitmap != null) {
                    PrintDocumentInfo build = new Builder(C00272.this.val$jobName).setContentType(PrintHelperKitkat.SCALE_MODE_FIT).setPageCount(PrintHelperKitkat.SCALE_MODE_FIT).build();
                    if (this.val$newPrintAttributes.equals(this.val$oldPrintAttributes)) {
                        z = false;
                    }
                    this.val$layoutResultCallback.onLayoutFinished(build, z);
                    return;
                }
                this.val$layoutResultCallback.onLayoutFailed(null);
            }

            protected void onCancelled(Bitmap bitmap) {
                this.val$layoutResultCallback.onLayoutCancelled();
            }
        }

        C00272(String str, Uri uri, int i) {
            this.val$jobName = str;
            this.val$imageFile = uri;
            this.val$fittingMode = i;
            this.mBitmap = null;
        }

        public void onLayout(PrintAttributes printAttributes, PrintAttributes printAttributes2, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle bundle) {
            boolean z = true;
            if (cancellationSignal.isCanceled()) {
                layoutResultCallback.onLayoutCancelled();
                this.mAttributes = printAttributes2;
            } else if (this.mBitmap != null) {
                PrintDocumentInfo build = new Builder(this.val$jobName).setContentType(PrintHelperKitkat.SCALE_MODE_FIT).setPageCount(PrintHelperKitkat.SCALE_MODE_FIT).build();
                if (printAttributes2.equals(printAttributes)) {
                    z = false;
                }
                layoutResultCallback.onLayoutFinished(build, z);
            } else {
                this.loadBitmap = new C00261(cancellationSignal, printAttributes2, printAttributes, layoutResultCallback);
                this.loadBitmap.execute(new Uri[0]);
                this.mAttributes = printAttributes2;
            }
        }

        private void cancelLoad() {
            synchronized (PrintHelperKitkat.this.mLock) {
                if (PrintHelperKitkat.this.mDecodeOptions != null) {
                    PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                    PrintHelperKitkat.this.mDecodeOptions = null;
                }
            }
        }

        public void onFinish() {
            super.onFinish();
            cancelLoad();
            this.loadBitmap.cancel(true);
        }

        public void onWrite(PageRange[] pageRangeArr, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback writeResultCallback) {
            PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
            try {
                Page startPage = printedPdfDocument.startPage(PrintHelperKitkat.SCALE_MODE_FIT);
                startPage.getCanvas().drawBitmap(this.mBitmap, PrintHelperKitkat.this.getMatrix(this.mBitmap.getWidth(), this.mBitmap.getHeight(), new RectF(startPage.getInfo().getContentRect()), this.val$fittingMode), null);
                printedPdfDocument.finishPage(startPage);
                printedPdfDocument.writeTo(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
                PageRange[] pageRangeArr2 = new PageRange[PrintHelperKitkat.SCALE_MODE_FIT];
                pageRangeArr2[0] = PageRange.ALL_PAGES;
                writeResultCallback.onWriteFinished(pageRangeArr2);
            } catch (Throwable e) {
                Log.e(PrintHelperKitkat.LOG_TAG, "Error writing printed content", e);
                writeResultCallback.onWriteFailed(null);
            } catch (Throwable th) {
                if (printedPdfDocument != null) {
                    printedPdfDocument.close();
                }
                if (parcelFileDescriptor != null) {
                    try {
                        parcelFileDescriptor.close();
                    } catch (IOException e2) {
                    }
                }
            }
            if (printedPdfDocument != null) {
                printedPdfDocument.close();
            }
            if (parcelFileDescriptor != null) {
                try {
                    parcelFileDescriptor.close();
                } catch (IOException e3) {
                }
            }
        }
    }

    PrintHelperKitkat(Context context) {
        this.mDecodeOptions = null;
        this.mLock = new Object();
        this.mScaleMode = SCALE_MODE_FILL;
        this.mColorMode = SCALE_MODE_FILL;
        this.mOrientation = SCALE_MODE_FIT;
        this.mContext = context;
    }

    public void setScaleMode(int i) {
        this.mScaleMode = i;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void setColorMode(int i) {
        this.mColorMode = i;
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    public void printBitmap(String str, Bitmap bitmap) {
        if (bitmap != null) {
            int i = this.mScaleMode;
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            MediaSize mediaSize = MediaSize.UNKNOWN_PORTRAIT;
            if (bitmap.getWidth() > bitmap.getHeight()) {
                mediaSize = MediaSize.UNKNOWN_LANDSCAPE;
            }
            printManager.print(str, new C00241(str, bitmap, i), new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
    }

    private Matrix getMatrix(int i, int i2, RectF rectF, int i3) {
        Matrix matrix = new Matrix();
        float width = rectF.width() / ((float) i);
        if (i3 == SCALE_MODE_FILL) {
            width = Math.max(width, rectF.height() / ((float) i2));
        } else {
            width = Math.min(width, rectF.height() / ((float) i2));
        }
        matrix.postScale(width, width);
        matrix.postTranslate((rectF.width() - (((float) i) * width)) / 2.0f, (rectF.height() - (width * ((float) i2))) / 2.0f);
        return matrix;
    }

    public void printBitmap(String str, Uri uri) {
        PrintDocumentAdapter c00272 = new C00272(str, uri, this.mScaleMode);
        PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(this.mColorMode);
        if (this.mOrientation == SCALE_MODE_FIT) {
            builder.setMediaSize(MediaSize.UNKNOWN_LANDSCAPE);
        } else if (this.mOrientation == SCALE_MODE_FILL) {
            builder.setMediaSize(MediaSize.UNKNOWN_PORTRAIT);
        }
        printManager.print(str, c00272, builder.build());
    }

    private Bitmap loadConstrainedBitmap(Uri uri, int i) {
        int i2 = SCALE_MODE_FIT;
        Bitmap bitmap = null;
        if (i <= 0 || uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        loadBitmap(uri, options);
        int i3 = options.outWidth;
        int i4 = options.outHeight;
        if (i3 > 0 && i4 > 0) {
            int max = Math.max(i3, i4);
            while (max > i) {
                max >>>= SCALE_MODE_FIT;
                i2 <<= SCALE_MODE_FIT;
            }
            if (i2 > 0 && Math.min(i3, i4) / i2 > 0) {
                Options options2;
                synchronized (this.mLock) {
                    this.mDecodeOptions = new Options();
                    this.mDecodeOptions.inMutable = true;
                    this.mDecodeOptions.inSampleSize = i2;
                    options2 = this.mDecodeOptions;
                }
                try {
                    bitmap = loadBitmap(uri, options2);
                    synchronized (this.mLock) {
                        this.mDecodeOptions = null;
                    }
                } catch (Throwable th) {
                    synchronized (this.mLock) {
                    }
                    this.mDecodeOptions = null;
                }
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmap(Uri uri, Options options) {
        InputStream inputStream = null;
        if (uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        try {
            inputStream = this.mContext.getContentResolver().openInputStream(uri);
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable e) {
                    Log.w(LOG_TAG, "close fail ", e);
                }
            }
            return decodeStream;
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Throwable e2) {
                    Log.w(LOG_TAG, "close fail ", e2);
                }
            }
        }
    }
}
