package android.text.style;

import android.graphics.Paint;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import java.util.Locale;

public class LocaleSpan extends MetricAffectingSpan implements ParcelableSpan {
    private final Locale mLocale;

    public LocaleSpan(Locale locale) {
        this.mLocale = locale;
    }

    public LocaleSpan(Parcel src) {
        this.mLocale = new Locale(src.readString(), src.readString(), src.readString());
    }

    public int getSpanTypeId() {
        return getSpanTypeIdInternal();
    }

    public int getSpanTypeIdInternal() {
        return 23;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        writeToParcelInternal(dest, flags);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
        dest.writeString(this.mLocale.getLanguage());
        dest.writeString(this.mLocale.getCountry());
        dest.writeString(this.mLocale.getVariant());
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public void updateDrawState(TextPaint ds) {
        apply(ds, this.mLocale);
    }

    public void updateMeasureState(TextPaint paint) {
        apply(paint, this.mLocale);
    }

    private static void apply(Paint paint, Locale locale) {
        paint.setTextLocale(locale);
    }
}
