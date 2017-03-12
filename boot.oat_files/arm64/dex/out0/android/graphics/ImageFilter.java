package android.graphics;

import android.hardware.SensorManager;
import android.os.SystemProperties;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageFilter {
    public static final String DEBUG_HWUI_IMAGE_FILTER_ENABLE_PROPERTY = "debug.hwui.imagefilter.enable";
    public static final String DEBUG_HWUI_IMAGE_FILTER_LOG_PROPERTY = "debug.hwui.imagefilter.log";
    private static final String LOG_TAG = "HWUI_IMAGE_FILTER";
    public static final int TYPE_BITMAP_COLOR = 52;
    public static final int TYPE_BITMAP_MASK = 52;
    public static final int TYPE_BLENDING = 53;
    public static final int TYPE_BLUR = 54;
    public static final int TYPE_COLORIZE = 16;
    public static final int TYPE_COLOR_CLAMP = 18;
    public static final int TYPE_COSINE_BLUR = 4;
    private static final int TYPE_CUSTOM_FILTER = 238;
    public static final int TYPE_DESATURATION = 17;
    public static final int TYPE_DIRECTIONAL_BLUR = 2;
    public static final int TYPE_DISTORTION = 49;
    public static final int TYPE_DROP_SHADOW = 55;
    public static final int TYPE_GAUSSIAN_BLUR = 1;
    public static final int TYPE_MOSAIC = 51;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_SGI_BLUR = 5;
    public static final int TYPE_VIGNETTE = 50;
    public static final int TYPE_ZOOM_BLUR = 3;
    private static final boolean sLogingEnabled = SystemProperties.getBoolean(DEBUG_HWUI_IMAGE_FILTER_LOG_PROPERTY, false);
    public final long mNativeImageFilter;

    public static class GenericImageFilter extends ImageFilterSet {
        protected static final float FALSE = 0.0f;
        protected static final int FILTER_BLEND_DST_FACTOR = 6;
        protected static final int FILTER_BLEND_SRC_FACTOR = 5;
        protected static final int FILTER_BLEND_USAGE = 4;
        protected static final int FILTER_DOWN_SAMPLE_RATE_H = 8;
        protected static final int FILTER_DOWN_SAMPLE_RATE_V = 9;
        protected static final int FILTER_HAS_SAMPLERS = 3;
        protected static final int FILTER_HAS_UNIFORMS = 2;
        protected static final int FILTER_IS_CHANGED = 1;
        protected static final int FILTER_IS_ENABLED = 0;
        protected static final int FILTER_TRANSFORM = 7;
        protected static final float GL_CONSTANT_ALPHA = 12.0f;
        protected static final float GL_CONSTANT_COLOR = 10.0f;
        protected static final float GL_DST_ALPHA = 8.0f;
        protected static final float GL_DST_COLOR = 4.0f;
        protected static final float GL_ONE = 1.0f;
        protected static final float GL_ONE_MINUS_CONSTANT_ALPHA = 13.0f;
        protected static final float GL_ONE_MINUS_CONSTANT_COLOR = 11.0f;
        protected static final float GL_ONE_MINUS_DST_ALPHA = 9.0f;
        protected static final float GL_ONE_MINUS_DST_COLOR = 5.0f;
        protected static final float GL_ONE_MINUS_SRC_ALPHA = 7.0f;
        protected static final float GL_ONE_MINUS_SRC_COLOR = 3.0f;
        protected static final float GL_SRC_ALPHA = 6.0f;
        protected static final float GL_SRC_ALPHA_SATURATE = 14.0f;
        protected static final float GL_SRC_COLOR = 2.0f;
        protected static final float GL_ZERO = 0.0f;
        protected static final float TRUE = 1.0f;
        public static final String mVertexShaderCodeCommon = "attribute vec2 texCoords;\nattribute vec4 position;\nvarying vec2 outTexCoords;\nuniform mat4 projection;\nvoid main() {\n   outTexCoords = texCoords;\n   gl_Position = projection * position;\n}\n";
        protected float[] mData1;
        protected float[] mData2;
        protected String[] mFrag;
        protected boolean mIsFilterData01Modified;
        protected boolean mIsFilterData01Used;
        protected boolean mIsFilterData02Modified;
        protected boolean mIsFilterData02Used;
        protected boolean mIsFilterParamsModified;
        protected boolean mIsFilterParamsUsed;
        protected float[] mParams;
        protected int mPassNum;
        protected String[] mVert;

        public GenericImageFilter(int passNum, String[] vert, String[] frag) {
            this.mPassNum = 0;
            this.mParams = new float[16];
            this.mData1 = new float[64];
            this.mData2 = new float[64];
            this.mIsFilterParamsUsed = false;
            this.mIsFilterData01Used = false;
            this.mIsFilterData02Used = false;
            this.mIsFilterParamsModified = false;
            this.mIsFilterData01Modified = false;
            this.mIsFilterData02Modified = false;
            setup(passNum, vert, frag);
        }

        public GenericImageFilter(String vert, String frag) {
            this.mPassNum = 0;
            this.mParams = new float[16];
            this.mData1 = new float[64];
            this.mData2 = new float[64];
            this.mIsFilterParamsUsed = false;
            this.mIsFilterData01Used = false;
            this.mIsFilterData02Used = false;
            this.mIsFilterParamsModified = false;
            this.mIsFilterData01Modified = false;
            this.mIsFilterData02Modified = false;
            this.mPassNum = 1;
            setup(1, new String[]{vert}, new String[]{frag});
        }

        protected void setup(String vert, String frag) {
            this.mPassNum = 1;
            this.mVert = new String[]{vert};
            this.mFrag = new String[]{vert};
            buildWorkerFilters();
            notifyWorkerFilters();
        }

        protected void setup(int passNum, String[] vert, String[] frag) {
            this.mPassNum = passNum;
            this.mVert = vert;
            this.mFrag = frag;
            buildWorkerFilters();
            notifyWorkerFilters();
        }

        protected void notifyWorkerFilters() {
            for (int pass = 0; pass < this.mPassNum; pass++) {
                CustomFilter filter = (CustomFilter) getFilterAt(pass);
                if (this.mIsFilterParamsUsed && this.mIsFilterParamsModified) {
                    filter.setUniformfv("filterParams", 1, this.mParams.length, this.mParams);
                }
                if (this.mIsFilterData01Used && this.mIsFilterData01Modified) {
                    filter.setUniformfv("filterData01", 1, this.mData1.length, this.mData1);
                }
                if (this.mIsFilterData02Used && this.mIsFilterData02Modified) {
                    filter.setUniformfv("filterData02", 1, this.mData2.length, this.mData2);
                }
            }
        }

        protected void buildWorkerFilters() {
            super.clearFilters();
            for (int pass = 0; pass < this.mPassNum; pass++) {
                addFilter(ImageFilter.createCustomFilter(getVertexShaderCode(pass), getFragmentShaderCode(pass)));
            }
        }

        protected String getVertexShaderCodeCommon() {
            return mVertexShaderCodeCommon;
        }

        protected String getVertexShaderCode(int pass) {
            if (pass < 0) {
                return null;
            }
            if (pass >= this.mVert.length) {
                return this.mVert[this.mVert.length - 1];
            }
            return this.mVert[pass];
        }

        protected String getFragmentShaderCode(int pass) {
            if (pass < 0) {
                return null;
            }
            if (pass >= this.mFrag.length) {
                return this.mFrag[this.mFrag.length - 1];
            }
            return this.mFrag[pass];
        }

        protected void useFilterParams() {
            this.mIsFilterParamsUsed = true;
        }

        protected void unUseFilterParams() {
            this.mIsFilterParamsUsed = false;
        }

        protected void setFilterParamsChanged() {
            this.mIsFilterParamsModified = true;
        }

        protected void resetFilterParamsChanged() {
            this.mIsFilterParamsModified = false;
        }

        protected void useFilterData01() {
            this.mIsFilterData01Used = true;
        }

        protected void unUseFilterData01() {
            this.mIsFilterData01Used = false;
        }

        protected void setFilterData01Changed() {
            this.mIsFilterData01Modified = true;
        }

        protected void resetFilterData01Changed() {
            this.mIsFilterData01Modified = false;
        }

        protected void useFilterData02() {
            this.mIsFilterData02Used = true;
        }

        protected void unUseFilterData02() {
            this.mIsFilterData02Used = false;
        }

        protected void setFilterData02Changed() {
            this.mIsFilterData02Modified = true;
        }

        protected void resetFilterData02Changed() {
            this.mIsFilterData02Modified = false;
        }

        protected void setParam(int index, float value) {
            this.mParams[index] = value;
            useFilterParams();
            setFilterParamsChanged();
        }

        public void setBitmap(Bitmap bitmap) {
            for (int pass = 0; pass < this.mPassNum; pass++) {
                ((CustomFilter) getFilterAt(pass)).setSamplerBitmap("maskSampler", 0, bitmap);
            }
        }

        public void setSamplerBitmap(String name, int id, Bitmap bitmap) {
            for (int pass = 0; pass < this.mPassNum; pass++) {
                ((CustomFilter) getFilterAt(pass)).setSamplerBitmap(name, id, bitmap);
            }
        }

        protected void setSamplingRate(int filter, float horizontal, float vertical) {
            if (filter >= 0 && filter <= this.mPassNum && filter <= getFilterCount()) {
                ImageFilter f = getFilterAt(filter);
                f.setValue(8, horizontal);
                f.setValue(9, vertical);
            }
        }
    }

    public static class BitmapColorMaskFilter extends GenericImageFilter {
        private static final int ALPHA = 4;
        private static final int BLUE = 3;
        private static final int ENABLE_GRADIENT = 0;
        private static final int GRADIENT_ALPHA = 8;
        private static final int GRADIENT_BLUE = 7;
        private static final int GRADIENT_ENDX = 11;
        private static final int GRADIENT_ENDY = 12;
        private static final int GRADIENT_GREEN = 6;
        private static final int GRADIENT_RED = 5;
        private static final int GRADIENT_STARTX = 9;
        private static final int GRADIENT_STARTY = 10;
        private static final int GREEN = 2;
        private static final int RED = 1;
        private static String mFragmentShaderCodeGradient = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\n\nvoid main(void) {\n    vec4 startColor = vec4(filterParams[1], filterParams[2], filterParams[3], filterParams[4]);\n    vec4 endColor = vec4(filterParams[5], filterParams[6], filterParams[7], filterParams[8]);\n    vec2 startPoint = vec2(filterParams[9], filterParams[10]);\n    vec2 endPoint = vec2(filterParams[11], filterParams[12]);\n    vec2 send = endPoint - startPoint;\n    vec2 scur = outTexCoords - startPoint;\n    float proj = dot(send, scur) / dot(send, send);\n    vec4 mask = mix(startColor, endColor, smoothstep(0.0, 1.0, proj));\n    vec4 texColor = texture2D(baseSampler, outTexCoords);\n    mask.rgb *= mask.a;\n    gl_FragColor = mask + texColor * (1.0 - mask.a);\n}\n\n";
        private static String mFragmentShaderCodeMask = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D maskSampler;\nuniform float filterParams[16];\n\nvoid main(void) {\n   vec4 texColor = texture2D(baseSampler, outTexCoords);\n\tvec4 maskColor = texture2D(maskSampler, outTexCoords);\n\tvec4 domColor = vec4(filterParams[1], filterParams[2], filterParams[3], filterParams[4]) * texColor;\n\tfloat alpha = domColor.a * maskColor.a;\n\tdomColor.rgb = domColor.rgb * alpha;\n\tdomColor.a = alpha;\n\tgl_FragColor = domColor + texColor * (1.0 - domColor.a);\n}\n\n";
        private boolean mGradientEnabled = true;

        public BitmapColorMaskFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCodeGradient);
            useFilterParams();
            if (ImageFilter.sLogingEnabled) {
                Log.d(ImageFilter.LOG_TAG, String.format("{0x%x}->BitmapColorMaskFilter()", new Object[]{Integer.valueOf(hashCode())}));
            }
        }

        public void setBitmap(Bitmap bitmap) {
            super.setBitmap(bitmap);
        }

        public void setColor(int color) {
            setColor(((float) Color.red(color)) / 255.0f, ((float) Color.green(color)) / 255.0f, ((float) Color.blue(color)) / 255.0f, ((float) Color.alpha(color)) / 255.0f);
        }

        public void setColor(float red, float green, float blue, float alpha) {
            setParam(1, Math.max(0.0f, Math.min(red, 1.0f)));
            setParam(2, Math.max(0.0f, Math.min(green, 1.0f)));
            setParam(3, Math.max(0.0f, Math.min(blue, 1.0f)));
            setParam(4, Math.max(0.0f, Math.min(alpha, 1.0f)));
            setFilterParamsChanged();
            resetGradient();
        }

        public void setGradient(float startX, float startY, int startColor, float endX, float endY, int endColor) {
            if (ImageFilter.sLogingEnabled) {
                Log.d(ImageFilter.LOG_TAG, String.format("{0x%x}->BitmapColorMaskFilter.setGradient(%f,%f,0x%x,  %f, %f, 0x%x)", new Object[]{Integer.valueOf(hashCode()), Float.valueOf(startX), Float.valueOf(startY), Integer.valueOf(startColor), Float.valueOf(endX), Float.valueOf(endY), Integer.valueOf(endColor)}));
            }
            float g = ((float) Color.green(startColor)) / 255.0f;
            float b = ((float) Color.blue(startColor)) / 255.0f;
            float a = ((float) Color.alpha(startColor)) / 255.0f;
            setParam(1, Math.max(0.0f, Math.min(((float) Color.red(startColor)) / 255.0f, 1.0f)));
            setParam(2, Math.max(0.0f, Math.min(g, 1.0f)));
            setParam(3, Math.max(0.0f, Math.min(b, 1.0f)));
            setParam(4, Math.max(0.0f, Math.min(a, 1.0f)));
            g = ((float) Color.green(endColor)) / 255.0f;
            b = ((float) Color.blue(endColor)) / 255.0f;
            a = ((float) Color.alpha(endColor)) / 255.0f;
            setParam(5, Math.max(0.0f, Math.min(((float) Color.red(endColor)) / 255.0f, 1.0f)));
            setParam(6, Math.max(0.0f, Math.min(g, 1.0f)));
            setParam(7, Math.max(0.0f, Math.min(b, 1.0f)));
            setParam(8, Math.max(0.0f, Math.min(a, 1.0f)));
            setParam(9, startX);
            setParam(10, 1.0f - startY);
            setParam(11, endX);
            setParam(12, 1.0f - endY);
            enableGradient();
        }

        public void resetGradient() {
            if (ImageFilter.sLogingEnabled) {
                Log.d(ImageFilter.LOG_TAG, String.format("{0x%x}->BitmapColorMaskFilter.resetGradient()", new Object[]{Integer.valueOf(hashCode())}));
            }
            setParam(0, 0.0f);
            if (true == this.mGradientEnabled) {
                this.mGradientEnabled = false;
                String[] strArr = new String[]{GenericImageFilter.mVertexShaderCodeCommon};
                String[] strArr2 = new String[1];
                strArr2[0] = this.mGradientEnabled ? mFragmentShaderCodeGradient : mFragmentShaderCodeMask;
                setup(1, strArr, strArr2);
            }
            notifyWorkerFilters();
        }

        public void enableGradient() {
            setParam(0, 1.0f);
            if (!this.mGradientEnabled) {
                this.mGradientEnabled = true;
                String[] strArr = new String[]{GenericImageFilter.mVertexShaderCodeCommon};
                String[] strArr2 = new String[1];
                strArr2[0] = this.mGradientEnabled ? mFragmentShaderCodeGradient : mFragmentShaderCodeMask;
                setup(1, strArr, strArr2);
            }
            notifyWorkerFilters();
        }
    }

    public static class BitmapMaskFilter extends ImageFilter {
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public BitmapMaskFilter() {
            super(52);
            try {
                throw new Exception("This method is deprecated!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setBitmap(Bitmap bitmap) {
            try {
                throw new Exception("This method is deprecated!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setColor(float red, float green, float blue, float alpha) {
            try {
                throw new Exception("This method is deprecated!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class BlendingFilter extends GenericImageFilter {
        public static final int BLENDING_MODE_MULTIPLY = 1;
        public static final int BLENDING_MODE_NORMAL = 0;
        public static final int BLENDING_MODE_SCREEN = 2;
        private static String mFragmentShaderCodeMultipy = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D maskSampler;\n\nvoid main(void) {\n       vec4 texColor = texture2D(baseSampler, outTexCoords);\n\t\tvec4 mask = texture2D(maskSampler, outTexCoords);\n       gl_FragColor = texColor * mask;\n}\n\n";
        private static String mFragmentShaderCodeNormal = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D maskSampler;\n\nvoid main(void) {\n\tvec4 texColor = texture2D(baseSampler, outTexCoords);\n\tvec4 mask = texture2D(maskSampler, outTexCoords);\n\tgl_FragColor = mask + texColor * (1.0 - mask.a);\n}\n\n";
        private static String mFragmentShaderCodeScreen = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D maskSampler;\n\nvoid main(void) {\n\t\tvec4 unitColor = vec4(1.0);\n       vec4 texColor = texture2D(baseSampler, outTexCoords);\n\t\tvec4 mask = texture2D(maskSampler, outTexCoords);\n       gl_FragColor = unitColor - ((unitColor-texColor) * (unitColor-mask));\n}\n\n";
        private int mBlendMode;

        public BlendingFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, getFragmentShader(0));
            this.mBlendMode = 0;
            this.mBlendMode = 0;
        }

        public BlendingFilter(int operation) {
            super(GenericImageFilter.mVertexShaderCodeCommon, getFragmentShader(operation));
            this.mBlendMode = 0;
            this.mBlendMode = operation;
        }

        public void setBitmap(Bitmap bitmap) {
            super.setBitmap(bitmap);
        }

        public void setOperation(int operation) {
            if (this.mBlendMode != operation) {
                this.mBlendMode = operation;
                setup(GenericImageFilter.mVertexShaderCodeCommon, getFragmentShader(operation));
            }
        }

        protected static String getFragmentShader(int mode) {
            if (mode == 0) {
                return mFragmentShaderCodeNormal;
            }
            return mode == 1 ? mFragmentShaderCodeMultipy : mFragmentShaderCodeScreen;
        }
    }

    public static class BlurFilter extends ImageFilter {
        public static final int TYPE_COSINE = 1;
        public static final int TYPE_GAUSSIAN = 0;
        public static final int TYPE_SGI = 2;

        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public BlurFilter() {
            super(54);
        }

        public void setRadius(float radius) {
            super.setValue(0, Math.max(0.0f, Math.min(radius, 250.0f)));
        }

        public void setOptimization(int type) {
            super.setValue(1, (float) type);
        }
    }

    public static class ColorClampFilter extends GenericImageFilter {
        private static final int MAX_ALPHA = 7;
        private static final int MAX_BLUE = 6;
        private static final int MAX_GREEN = 5;
        private static final int MAX_RED = 4;
        private static final int MIN_ALPHA = 3;
        private static final int MIN_BLUE = 2;
        private static final int MIN_GREEN = 1;
        private static final int MIN_RED = 0;
        private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nvoid main(void) {\n  vec4 color = texture2D(baseSampler, outTexCoords);\n  vec4 minVal = vec4(filterParams[0], filterParams[1], filterParams[2], filterParams[3]);\n  vec4 maxVal = vec4(filterParams[4], filterParams[5], filterParams[6], filterParams[7]);\n  gl_FragColor = clamp(color, minVal, maxVal);\n}\n\n";

        public ColorClampFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
            useFilterParams();
            setMinColor(0.0f, 0.0f, 0.0f, 0.0f);
            setMaxColor(1.0f, 1.0f, 1.0f, 1.0f);
        }

        public void setMinColor(int color) {
            setMinColor(((float) Color.red(color)) / 255.0f, ((float) Color.green(color)) / 255.0f, ((float) Color.blue(color)) / 255.0f, ((float) Color.alpha(color)) / 255.0f);
        }

        public void setMinColor(float red, float green, float blue, float alpha) {
            this.mParams[0] = Math.max(0.0f, Math.min(red, 1.0f));
            this.mParams[1] = Math.max(0.0f, Math.min(green, 1.0f));
            this.mParams[2] = Math.max(0.0f, Math.min(blue, 1.0f));
            this.mParams[3] = Math.max(0.0f, Math.min(alpha, 1.0f));
            setFilterParamsChanged();
            notifyWorkerFilters();
        }

        public void setMaxColor(int color) {
            setMaxColor(((float) Color.red(color)) / 255.0f, ((float) Color.green(color)) / 255.0f, ((float) Color.blue(color)) / 255.0f, ((float) Color.alpha(color)) / 255.0f);
        }

        public void setMaxColor(float red, float green, float blue, float alpha) {
            this.mParams[4] = Math.max(0.0f, Math.min(red, 1.0f));
            this.mParams[5] = Math.max(0.0f, Math.min(green, 1.0f));
            this.mParams[6] = Math.max(0.0f, Math.min(blue, 1.0f));
            this.mParams[7] = Math.max(0.0f, Math.min(alpha, 1.0f));
            setFilterParamsChanged();
            notifyWorkerFilters();
        }
    }

    public static class ColorizeFilter extends GenericImageFilter {
        private static final int ALPHA = 3;
        private static final int BLEND_RATIO = 4;
        private static final int BLUE = 2;
        private static final int GREEN = 1;
        private static final int RED = 0;
        private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\n\nvoid main(void) {\n\t vec4 texColor = texture2D(baseSampler, outTexCoords);\n\t vec4 colorizeColor = vec4(filterParams[0], filterParams[1], filterParams[2], filterParams[3]);\n\t gl_FragColor = mix(colorizeColor, texColor, filterParams[4]);\n}\n\n";

        public ColorizeFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
            useFilterParams();
            setBlendRatio(0.0f);
        }

        public void setColor(int color) {
            setColor(((float) Color.red(color)) / 255.0f, ((float) Color.green(color)) / 255.0f, ((float) Color.blue(color)) / 255.0f, ((float) Color.alpha(color)) / 255.0f);
        }

        public void setColor(float red, float green, float blue, float alpha) {
            this.mParams[0] = Math.max(0.0f, Math.min(red, 1.0f));
            this.mParams[1] = Math.max(0.0f, Math.min(green, 1.0f));
            this.mParams[2] = Math.max(0.0f, Math.min(blue, 1.0f));
            this.mParams[3] = Math.max(0.0f, Math.min(alpha, 1.0f));
            setFilterParamsChanged();
            notifyWorkerFilters();
        }

        public void setBlendRatio(float ratio) {
            this.mParams[4] = Math.max(0.0f, Math.min(ratio, 1.0f));
            setFilterParamsChanged();
            notifyWorkerFilters();
        }
    }

    public static class CosineBlurFilter extends GenericImageFilter {
        private static final float MAX_RADIUS = 250.0f;
        private static final int RADIUS = 0;
        private static final int STEP_COUNT = 2;
        private static String[] mFragmentShaderCode = new String[]{"#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nvarying vec2 rescoefs;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaW;\n\nvoid main(void) {\n    highp vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n\t float step = 1.0 / areaW ;\n\t float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n\t \t scaledStep = step * filterData02[i];\n\t\t texPos.s = outTexCoords.s + scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n\t\t texPos.s = outTexCoords.s - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n\t gl_FragColor = fragColorBlur;\n}\n\n", "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nvarying vec2 rescoefs;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaH;\n\nvoid main(void) {\n\t highp vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n\t float step = 1.0 / areaH;\n\t float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n\t \t scaledStep = step * filterData02[i];\n\t\t texPos.t = outTexCoords.t + scaledStep; \n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n\t\t texPos.t = outTexCoords.t - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n\t gl_FragColor = fragColorBlur;\n}\n\n"};
        private float mQuality = SensorManager.GRAVITY_PLUTO;
        private float mRadius = 0.0f;

        public CosineBlurFilter() {
            super(2, new String[]{GenericImageFilter.mVertexShaderCodeCommon}, mFragmentShaderCode);
            useFilterData01();
            useFilterData02();
        }

        public void setRadius(int radius) {
            try {
                throw new Exception("This method is deprecated!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setRadius(float radius) {
            if (this.mRadius != radius) {
                if (this.mRadius > MAX_RADIUS) {
                    this.mRadius = MAX_RADIUS;
                } else if (this.mRadius < 0.0f) {
                    this.mRadius = 0.0f;
                } else {
                    this.mRadius = radius;
                }
                computeCosineCoefs();
                setupDownSampling();
                notifyWorkerFilters();
            }
        }

        protected void setupDownSampling() {
            float downSampleRate = (float) Math.sqrt((double) this.mRadius);
            if (downSampleRate < 1.0f) {
                downSampleRate = 1.0f;
            }
            setSamplingRate(0, downSampleRate, 1.0f);
            setSamplingRate(1, downSampleRate, downSampleRate);
        }

        protected void computeCosineCoefs() {
            if (this.mRadius > SensorManager.MAGNETIC_FIELD_EARTH_MAX) {
                this.mRadius = SensorManager.MAGNETIC_FIELD_EARTH_MAX;
            }
            if (this.mRadius <= 0.0f) {
                this.mData1[0] = 1.0f;
                this.mData2[0] = 0.0f;
                return;
            }
            int r;
            float coeff1 = 0.5f / this.mRadius;
            float coeff2 = 3.1415927f / this.mRadius;
            float normalizeFactor = 0.0f;
            float[] data = new float[128];
            for (r = 0; ((float) r) <= this.mRadius; r++) {
                data[r] = ((float) (((double) coeff1) * Math.cos((double) (coeff2 * ((float) r))))) + coeff1;
                if (r > 0) {
                    normalizeFactor += data[r];
                }
            }
            normalizeFactor = (normalizeFactor * 2.0f) + data[0];
            for (r = 0; ((float) r) <= this.mRadius; r++) {
                data[r] = data[r] / normalizeFactor;
            }
            for (r = 0; ((float) r) <= this.mRadius; r++) {
                this.mData1[r] = data[r];
                this.mData2[r] = (float) r;
            }
            setParam(0, this.mRadius);
            setParam(2, this.mRadius + 1.0f);
            setFilterData01Changed();
            setFilterData02Changed();
        }
    }

    public static class CustomFilter extends ImageFilter {
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public CustomFilter(String vcode, String fcode) {
            super(238);
            setVertexShader(vcode);
            setFragmentShader(fcode);
        }

        public void setUpdateMargin(int left, int top, int right, int bottom) {
            super.setUpdateMargin(left, top, right, bottom);
        }

        public void setUniform1f(String name, float value0) {
            super.setUniformf(name, 1, 1, new float[]{value0});
        }

        public void setUniform2f(String name, float value0, float value1) {
            super.setUniformf(name, 2, 1, new float[]{value0, value1});
        }

        public void setUniform3f(String name, float value0, float value1, float value2) {
            super.setUniformf(name, 3, 1, new float[]{value0, value1, value2});
        }

        public void setUniform4f(String name, float value0, float value1, float value2, float value3) {
            super.setUniformf(name, 4, 1, new float[]{value0, value1, value2, value3});
        }

        public void setValue(int index, float value) {
            super.setValue(index, value);
        }

        public void setUniformfv(String name, int vec, int count, float[] value) {
            super.setUniformf(name, vec, count, value);
        }

        public void setUniform1i(String name, int value0) {
            super.setUniformi(name, 1, 1, new int[]{value0});
        }

        public void setUniform2i(String name, int value0, int value1) {
            super.setUniformi(name, 2, 1, new int[]{value0, value1});
        }

        public void setUniform3i(String name, int value0, int value1, int value2) {
            super.setUniformi(name, 3, 1, new int[]{value0, value1, value2});
        }

        public void setUniform4i(String name, int value0, int value1, int value2, int value3) {
            super.setUniformi(name, 4, 1, new int[]{value0, value1, value2, value3});
        }

        public void setUniformiv(String name, int vec, int count, int[] value) {
            super.setUniformi(name, vec, count, value);
        }

        public void setUniformMatrix(String name, int row, int col, float[] value) {
            super.setUniformMatrix(name, row, col, value);
        }

        public void setSamplerBitmap(String name, int id, Bitmap bitmap) {
            super.setSamplerBitmap(name, id, bitmap);
        }
    }

    public static class DesaturationFilter extends GenericImageFilter {
        private static final int DEASATURATION = 0;
        private static final float MAX_DEASATURATION = 1.0f;
        private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nvoid main(void) {\n\t vec4 texColor = texture2D(baseSampler, outTexCoords);\n\t float lum = dot(vec3(0.2126,0.7152,0.0722), texColor.rgb);\n\t vec4 grayColor = vec4(lum, lum, lum, texColor.a);\n\t gl_FragColor = mix(grayColor, texColor, filterParams[0]);\n}\n\n";
        private float mDesaturation = 0.0f;

        public DesaturationFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
            setSaturation(1.0f);
        }

        public void setSaturation(float saturation) {
            if (this.mDesaturation != saturation) {
                if (this.mDesaturation > 1.0f) {
                    this.mDesaturation = 1.0f;
                } else if (this.mDesaturation < 0.0f) {
                    this.mDesaturation = 0.0f;
                } else {
                    this.mDesaturation = saturation;
                }
                setParam(0, this.mDesaturation);
                notifyWorkerFilters();
            }
        }
    }

    public static class DirectionalBlurFilter extends GenericImageFilter {
        private static final float MAX_RADIUS = 250.0f;
        private static final int RADIUS = 0;
        private static final int STEP = 1;
        private static final int STEP_COUNT = 2;
        private static String mFragmentShaderCode = "precision highp float;varying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float nWidthCoef;\n\nvoid main(void) {\n\t vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n\t float step = filterParams[1];\n\t float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n\t \t scaledStep = step * filterData02[i] * nWidthCoef;\n\t\t texPos.t = outTexCoords.t + scaledStep; \n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n\t\t texPos.t = outTexCoords.t - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n\t gl_FragColor = fragColorBlur;\n}\n";
        private float mQuality = SensorManager.GRAVITY_PLUTO;
        private float mRadius = 0.0f;

        public DirectionalBlurFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
            useFilterData01();
            useFilterData02();
        }

        public void setDistance(float distance) {
        }

        public void setAngle(float degree) {
            super.setValue(1, degree);
        }

        public void setRadius(float radius) {
            if (this.mRadius != radius) {
                if (this.mRadius > MAX_RADIUS) {
                    this.mRadius = MAX_RADIUS;
                } else if (this.mRadius < 0.0f) {
                    this.mRadius = 0.0f;
                } else {
                    this.mRadius = radius;
                }
                computeGaussCoefs();
                notifyWorkerFilters();
            }
        }

        protected void computeGaussCoefs() {
            float power;
            int interFactor = (int) (this.mRadius * this.mQuality);
            if (interFactor > 64) {
                power = this.mRadius / ((float) interFactor);
                interFactor = 64;
            } else if (interFactor < 1) {
                power = 1.0f;
                interFactor = 1;
            } else {
                power = 1.67777f;
            }
            if (this.mRadius <= 0.0f) {
                this.mData1[0] = 1.0f;
                this.mData2[0] = 0.0f;
                return;
            }
            int i;
            float sigma = (0.3f * this.mRadius) + SensorManager.GRAVITY_PLUTO;
            float coeff1 = (float) (1.0d / (Math.sqrt(6.283185307179586d) * ((double) sigma)));
            float coeff2 = -1.0f / ((2.0f * sigma) * sigma);
            float normalizeFactor = 0.0f;
            float[] data = new float[128];
            float interpalationStep = this.mRadius / ((float) interFactor);
            float r = 0.0f;
            for (i = 1; i < interFactor; i++) {
                data[i] = (float) ((((double) coeff1) * Math.pow(2.718281828459045d, (double) ((r * r) * coeff2))) * ((double) power));
                normalizeFactor += data[i];
                r += interpalationStep;
            }
            data[0] = coeff1 * power;
            normalizeFactor = (normalizeFactor * 2.0f) + data[0];
            for (i = 0; i < interFactor; i++) {
                data[i] = data[i] / normalizeFactor;
            }
            r = 0.0f;
            for (i = 0; i < interFactor; i++) {
                this.mData1[i] = data[i];
                this.mData2[i] = r;
                r += interpalationStep;
            }
            setParam(0, this.mRadius);
            setParam(1, interpalationStep);
            setParam(2, (float) interFactor);
            setFilterData01Changed();
            setFilterData02Changed();
        }
    }

    public static class DistortionFilter extends GenericImageFilter {
        private static final int DISTORTION = 0;
        private static final float MAX_RADIUS = 1024.0f;
        private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nvoid main(void) {\n   vec2 xy = 2.0 * outTexCoords.xy - 1.0;\n   vec2 uv;\n   float d = length(xy);\n   if ( d < 1.0 ) {\n       float theta = atan(xy.y, xy.x);\n       float radius = length(xy);\n       radius = pow(radius, filterParams[0]+1.0);\n       xy.x = radius * cos(theta);\n       xy.y = radius * sin(theta);\n       uv = 0.5 * (xy + 1.0);\n   } else {\n       uv = outTexCoords.xy;\n   }\n   gl_FragColor = texture2D(baseSampler, uv);\n}\n\n";
        private float mRadius = 0.0f;

        public DistortionFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
        }

        public void setDistortion(float power) {
            if (this.mRadius != power) {
                if (this.mRadius > MAX_RADIUS) {
                    this.mRadius = MAX_RADIUS;
                } else if (this.mRadius < 0.0f) {
                    this.mRadius = 0.0f;
                } else {
                    this.mRadius = power;
                }
                setParam(0, this.mRadius);
                notifyWorkerFilters();
            }
        }
    }

    public static class DropShadowFilter extends GenericImageFilter {
        private static final int ALPHA = 6;
        private static final int BLUE = 5;
        private static final int CYCLES_COUNT = 7;
        private static final int DIRECTION_X = 1;
        private static final int DIRECTION_Y = 2;
        private static final int DISTANCE = 0;
        private static final int GREEN = 4;
        private static final int RED = 3;
        private static final int SHORTDISTANCE = 8;
        private static String[] mFragmentShaderCode = new String[]{"precision highp float;\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float areaW;\nuniform float areaH;\n\nvoid main(void) {\n    vec2 uv = vec2(outTexCoords);\n    float c = areaW / areaH;\n    vec2 vDirection = vec2(filterParams[1] / areaW, filterParams[2] / areaH) * filterParams[0];\n    if(areaW>areaH) vDirection.y*=c; else vDirection.x/=c; \n    float shadowalpha = filterParams[6];\n    vec4 savedcolor = texture2D(baseSampler, uv);\n    vec4 pixel = vec4(0.0);\n    float ratio = 0.0;\n    float filter = 0.0;\n    float count = 0.0;\n    float distantRatio = 0.0;\n    float ccl = filterParams[7];\n    float divider = 1.0/ccl;\n    for (float i = 0.0; i < ccl; i += 1.0) {\n        distantRatio = (i < 5.0) ? ((i+1.0) / 5.0) : 1.0;\n        uv -= vDirection * distantRatio;\n        uv = clamp(uv, 0.0, 1.0);\n        pixel = texture2D(baseSampler, uv);\n        if (pixel.a > 0.0)\n            break;\n        count += divider;\n    }\n    ratio = min(count, 1.0) * 1.570797;\n    filter = 1.0 - min(sin(ratio) + 0.2, 1.0);\n    gl_FragColor = vec4(savedcolor.rgb * savedcolor.a, filter * shadowalpha);\n}\n\n", "precision highp float;\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nuniform sampler2D originalSampler;\nuniform float filterParams[16];\nuniform float areaW;\nuniform float areaH;\n\nvoid main(void) {\n    float c = areaW / areaH;\n    vec2 vDirection = vec2(filterParams[1] / areaW, filterParams[2] / areaH) * filterParams[0];\n    if(areaW>areaH) vDirection.y*=c; else vDirection.x/=c; \n    vec2 uv = vec2(outTexCoords);\n    vec4 sceneColor = texture2D(originalSampler, outTexCoords);\n    vec4 pixel = vec4(0.0);\n    vec4 weightedShadowColor = (1.0 - sceneColor.a) * vec4(filterParams[3], filterParams[4], filterParams[5], 1.0);\n    vec4 dropShadowColor = vec4(0.0);\n    float count = 0.0;\n    float height = 0.0;\n    float ccl = filterParams[7];\n    for (float i = 0.0; i < ccl; i += 1.0) {\n        uv = clamp(uv, 0.0, 1.0);\n        pixel = texture2D(baseSampler, uv);\n        height = dot(pixel.rgb, vec3(1.0));\n        if (height > 0.0)\n            continue;\n        count += 1.0;\n        dropShadowColor += pixel.a*weightedShadowColor;\n        uv -= vDirection;\n    }\n    dropShadowColor *= count / (count*count + 0.0001);\n    gl_FragColor = sceneColor + dropShadowColor;\n}\n\n"};
        private float mAngle = -10.0f;
        private float mDistance = 0.003f;
        private float mQuality = 9.0f;

        public DropShadowFilter() {
            super(2, new String[]{GenericImageFilter.mVertexShaderCodeCommon}, mFragmentShaderCode);
            setDistance(20.0f);
            setAngle(-10.0f);
            setShadowColor(1.0f, 0.5f, 0.5f, 1.0f);
            setQuality(15.0f);
            ((CustomFilter) getFilterAt(0)).setValue(4, 0.0f);
            ((CustomFilter) getFilterAt(1)).setValue(4, 1.0f);
            ((CustomFilter) getFilterAt(1)).setValue(5, 1.0f);
            ((CustomFilter) getFilterAt(1)).setValue(6, 7.0f);
            preSetupShader();
        }

        public void setShadowColor(float red, float green, float blue, float alpha) {
            this.mParams[3] = Math.max(0.0f, Math.min(red, 1.0f));
            this.mParams[4] = Math.max(0.0f, Math.min(green, 1.0f));
            this.mParams[5] = Math.max(0.0f, Math.min(blue, 1.0f));
            this.mParams[6] = Math.max(0.0f, Math.min(alpha, 1.0f));
            setFilterParamsChanged();
            preSetupShader();
        }

        public void setDistance(float distance) {
            this.mDistance = distance;
            preSetupShader();
        }

        public void setAngle(float angle) {
            this.mAngle = angle;
            preSetupShader();
        }

        public void setQuality(float quality) {
            this.mQuality = 5.0f + (0.2f * Math.max(0.0f, Math.min(quality, SensorManager.LIGHT_CLOUDY)));
            preSetupShader();
        }

        protected void preSetupShader() {
            float radian = (this.mAngle / 180.0f) * 3.141592f;
            float xdir = ((float) Math.cos((double) radian)) * this.mDistance;
            float ydir = ((float) Math.sin((double) radian)) * this.mDistance;
            float cyclesCount = this.mQuality;
            float distance = 1.0f / (cyclesCount - 2.0f);
            setParam(0, distance);
            setParam(1, xdir);
            setParam(2, ydir);
            setParam(7, cyclesCount);
            setParam(8, distance * 0.2f);
            notifyWorkerFilters();
        }
    }

    public static class GaussianBlurFilter extends GenericImageFilter {
        private static final float MAX_RADIUS = 250.0f;
        private static final int RADIUS = 0;
        private static final int STEP = 1;
        private static final int STEP_COUNT = 2;
        private static String[] mFragmentShaderCode = new String[]{"#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nvarying vec2 rescoefs;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaW;\n\nvoid main(void) {\n\t vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n\t float step = 1.0 / areaW ;\n\t float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n\t \t scaledStep = step * filterData02[i];\n\t\t texPos.s = outTexCoords.s + scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n\t\t texPos.s = outTexCoords.s - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n\t gl_FragColor = fragColorBlur;\n}\n\n", "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform sampler2D baseSampler;\nvarying vec2 rescoefs;\nuniform float filterParams[16];\nuniform float filterData01[64];\nuniform float filterData02[64];\nuniform float areaH;\n\nvoid main(void) {\n\t vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n    vec2 texPos = vec2(outTexCoords);\n\t float step = 1.0 / areaH;\n\t float scaledStep = 0.0;\n    fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[0]);\n    for(int i = 1; i < int(filterParams[2]); i++){\n\t \t scaledStep = step * filterData02[i];\n\t\t texPos.t = outTexCoords.t + scaledStep; \n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n\t\t texPos.t = outTexCoords.t - scaledStep;\n        fragColorBlur += (texture2D(baseSampler, texPos) * filterData01[i]);\n    }\n\t gl_FragColor = fragColorBlur;\n}\n\n"};
        private float mQuality = SensorManager.GRAVITY_PLUTO;
        private float mRadius = 0.0f;

        public GaussianBlurFilter() {
            super(2, new String[]{GenericImageFilter.mVertexShaderCodeCommon}, mFragmentShaderCode);
            useFilterParams();
            useFilterData01();
            useFilterData02();
        }

        public void setRadius(int radius) {
            try {
                throw new Exception("This method is deprecated!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setRadius(float radius) {
            if (this.mRadius != radius) {
                if (this.mRadius > MAX_RADIUS) {
                    this.mRadius = MAX_RADIUS;
                } else if (this.mRadius < 0.0f) {
                    this.mRadius = 0.0f;
                } else {
                    this.mRadius = radius;
                }
                setupDownSampling();
                computeGaussCoefs();
                notifyWorkerFilters();
            }
        }

        protected void setupDownSampling() {
            float downSampleRate = (float) Math.sqrt((double) this.mRadius);
            if (downSampleRate < 1.0f) {
                downSampleRate = 1.0f;
            }
            setSamplingRate(0, downSampleRate, 1.0f);
            setSamplingRate(1, downSampleRate, downSampleRate);
        }

        protected void computeGaussCoefs() {
            float power;
            int interFactor = (int) (this.mRadius * SensorManager.GRAVITY_PLUTO);
            if (interFactor > 64) {
                power = this.mRadius / ((float) interFactor);
                interFactor = 64;
            } else if (interFactor < 1) {
                power = 1.0f;
                interFactor = 1;
            } else {
                power = 1.67777f;
            }
            if (this.mRadius <= 0.0f) {
                this.mData1[0] = 1.0f;
                this.mData2[0] = 0.0f;
                return;
            }
            int i;
            float sigma = (0.3f * this.mRadius) + SensorManager.GRAVITY_PLUTO;
            float coeff1 = (float) (1.0d / (Math.sqrt((double) (2.0f * 3.1415927f)) * ((double) sigma)));
            float coeff2 = -1.0f / ((2.0f * sigma) * sigma);
            float normalizeFactor = 0.0f;
            float[] data = new float[128];
            float interpalationStep = this.mRadius / ((float) interFactor);
            float r = 0.0f;
            for (i = 1; i < interFactor; i++) {
                data[i] = (float) ((((double) coeff1) * Math.pow((double) 2.7182817f, (double) ((r * r) * coeff2))) * ((double) power));
                normalizeFactor += data[i];
                r += interpalationStep;
            }
            data[0] = coeff1 * power;
            normalizeFactor = (normalizeFactor * 2.0f) + data[0];
            for (i = 0; i < interFactor; i++) {
                data[i] = data[i] / normalizeFactor;
            }
            r = 0.0f;
            for (i = 0; i < interFactor; i++) {
                this.mData1[i] = data[i];
                this.mData2[i] = r;
                r += interpalationStep;
            }
            setParam(0, this.mRadius);
            setParam(1, 1.0f);
            setParam(2, (float) interFactor);
            setFilterData01Changed();
            setFilterData02Changed();
        }
    }

    public static class MosaicFilter extends GenericImageFilter {
        private static final float MAX_RADIUS = 1024.0f;
        private static final int RADIUS = 0;
        private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nuniform mat4 projection;\nuniform mat4 transform;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float areaW;\nuniform float areaH;\n\nvoid main(void) {\n\t float radius_h = filterParams[0] / areaW;\n\t float radius_v = filterParams[0] / areaH;\n\t vec2 texCoord = vec2((floor(outTexCoords.s / radius_h) + 0.5) * radius_h, (floor(outTexCoords.t / radius_v) + 0.5) * radius_v);\n\t gl_FragColor = texture2D(baseSampler, texCoord);\n}\n\n";
        private float mRadius = 0.0f;

        public MosaicFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
            useFilterParams();
        }

        public void setRadius(float radius) {
            if (this.mRadius != radius) {
                if (this.mRadius > MAX_RADIUS) {
                    this.mRadius = MAX_RADIUS;
                } else if (this.mRadius < 0.0f) {
                    this.mRadius = 0.0f;
                } else {
                    this.mRadius = radius;
                }
                setParam(0, this.mRadius);
                notifyWorkerFilters();
            }
        }
    }

    public static class SgiBlurFilter extends GenericImageFilter {
        private static final float MAX_RADIUS = 60.0f;
        private static final int RADIUS = 1;
        private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nvarying vec2 vNeighborTexCoord[12];\nuniform sampler2D baseSampler;\n\nvoid main(void) {\n   highp vec4 fragColorBlur = vec4(0.0, 0.0, 0.0, 0.0);\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[0])  * 0.00903788620091937;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[1])  * 0.0217894371884468;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[2])  * 0.0447649434011506;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[3])  * 0.0783687553896893;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[4])  * 0.116912444814134;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[5])  * 0.148624846131112;\n   fragColorBlur += texture2D(baseSampler, outTexCoords        )  * 0.161003373749805;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[6])  * 0.148624846131112;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[7])  * 0.116912444814134;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[8])  * 0.0783687553896893;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[9])  * 0.0447649434011506;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[10]) * 0.0217894371884468;\n   fragColorBlur += texture2D(baseSampler, vNeighborTexCoord[11]) * 0.00903788620091937;\n   gl_FragColor = fragColorBlur;\n}\n\n";
        private static String[] mVertexShaderCode = new String[]{"attribute vec2 texCoords;\nattribute vec4 position;\nuniform float areaW;\nuniform float sampleRate;\nuniform float filterParams[16];\nvarying vec2 outTexCoords;\nvarying vec2 vNeighborTexCoord[12];\nvoid main() {\n   outTexCoords = texCoords;\n   float v = filterParams[1] / 6.0 / areaW;\n   vNeighborTexCoord[0]  = outTexCoords + vec2(-6.0 * v, 0.0);\n   vNeighborTexCoord[1]  = outTexCoords + vec2(-5.0 * v, 0.0);\n   vNeighborTexCoord[2]  = outTexCoords + vec2(-4.0 * v, 0.0);\n   vNeighborTexCoord[3]  = outTexCoords + vec2(-3.0 * v, 0.0);\n   vNeighborTexCoord[4]  = outTexCoords + vec2(-2.0 * v, 0.0);\n   vNeighborTexCoord[5]  = outTexCoords + vec2(-1.0 * v, 0.0);\n   vNeighborTexCoord[6]  = outTexCoords + vec2( 1.0 * v, 0.0);\n   vNeighborTexCoord[7]  = outTexCoords + vec2( 2.0 * v, 0.0);\n   vNeighborTexCoord[8]  = outTexCoords + vec2( 3.0 * v, 0.0);\n   vNeighborTexCoord[9]  = outTexCoords + vec2( 4.0 * v, 0.0);\n   vNeighborTexCoord[10] = outTexCoords + vec2( 5.0 * v, 0.0);\n   vNeighborTexCoord[11] = outTexCoords + vec2( 6.0 * v, 0.0);\n   gl_Position = position;\n}\n", "attribute vec2 texCoords;\nattribute vec4 position;\nuniform float areaH;\nuniform float sampleRate;\nuniform float filterParams[16];\nvarying vec2 outTexCoords;\nvarying vec2 vNeighborTexCoord[12];\nvoid main() {\n   outTexCoords = texCoords;\n   float v = filterParams[1] / 6.0 / areaH;\n   vNeighborTexCoord[0]  = outTexCoords + vec2(0.0, -6.0 * v );\n   vNeighborTexCoord[1]  = outTexCoords + vec2(0.0, -5.0 * v );\n   vNeighborTexCoord[2]  = outTexCoords + vec2(0.0, -4.0 * v );\n   vNeighborTexCoord[3]  = outTexCoords + vec2(0.0, -3.0 * v );\n   vNeighborTexCoord[4]  = outTexCoords + vec2(0.0, -2.0 * v );\n   vNeighborTexCoord[5]  = outTexCoords + vec2(0.0, -1.0 * v );\n   vNeighborTexCoord[6]  = outTexCoords + vec2(0.0,  1.0 * v );\n   vNeighborTexCoord[7]  = outTexCoords + vec2(0.0,  2.0 * v );\n   vNeighborTexCoord[8]  = outTexCoords + vec2(0.0,  3.0 * v );\n   vNeighborTexCoord[9]  = outTexCoords + vec2(0.0,  4.0 * v );\n   vNeighborTexCoord[10] = outTexCoords + vec2(0.0,  5.0 * v );\n   vNeighborTexCoord[11] = outTexCoords + vec2(0.0,  6.0 * v );\n   gl_Position = position;\n}\n"};
        private float mRadius = 0.0f;

        public SgiBlurFilter() {
            super(2, mVertexShaderCode, new String[]{mFragmentShaderCode});
            setRadius(1.0f);
        }

        public void setRadius(float radius) {
            if (this.mRadius != radius) {
                this.mRadius = Math.max(0.0f, Math.min(radius, 60.0f));
                setParam(1, this.mRadius);
                setupDownSampling();
                notifyWorkerFilters();
            }
        }

        protected void setupDownSampling() {
            float downSampleRate = this.mRadius / 6.0f;
            if (downSampleRate < 1.0f) {
                downSampleRate = 1.0f;
            }
            setSamplingRate(0, downSampleRate, 1.0f);
            setSamplingRate(1, downSampleRate, downSampleRate);
        }
    }

    public static class VignetteFilter extends GenericImageFilter {
        private static final float MAX_RADIUS = 1024.0f;
        private static final int RADIUS = 0;
        private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nvarying vec2 resolution;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float areaW;\nuniform float areaH;\n\nvoid main(void) {\n   vec2 u_resolution = vec2(areaW, areaH);\n   vec4 texColor = texture2D(baseSampler, outTexCoords);\n   vec2 relativePosition = gl_FragCoord.xy / u_resolution - 0.5;\n   float len = length(relativePosition);\n   float vignette = smoothstep(filterParams[0] + 0.1, filterParams[0] - 0.1, len);\n   texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.9);\n   gl_FragColor = texColor;\n}\n\n";
        private float mRadius = 0.0f;

        public VignetteFilter() {
            super(GenericImageFilter.mVertexShaderCodeCommon, mFragmentShaderCode);
            useFilterParams();
        }

        public void setRadius(float radius) {
            if (this.mRadius != radius) {
                if (this.mRadius > MAX_RADIUS) {
                    this.mRadius = MAX_RADIUS;
                } else if (this.mRadius < 0.0f) {
                    this.mRadius = 0.0f;
                } else {
                    this.mRadius = radius;
                }
                setParam(0, this.mRadius);
                notifyWorkerFilters();
            }
        }
    }

    public static class ZoomBlurFilter extends GenericImageFilter {
        private static final int QUALITY = 1;
        private static final int ZOOM = 0;
        private static String mFragmentShaderCode = "#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec2 outTexCoords;\nvarying vec2 Pos;\nuniform sampler2D baseSampler;\nuniform float filterParams[16];\nuniform float filterData01[64];\nvoid main(void) {\nvec2 xy = 2.0 * outTexCoords.xy - 1.0;\nvec2 uv = (vec2(Pos.x, Pos.y)+ vec2(1.0))/vec2(2.0);\nvec2 dir = 0.5 - uv;\nfloat dist = sqrt(dir.x*dir.x + dir.y*dir.y);\ndir = dir/dist;\nvec4 color = texture2D(baseSampler,uv);\nfloat scaledStep = 0.0;\nvec4 sum = color;\nfor(int i = 0; i < int(filterParams[1])/2; i++){\n\tsum += texture2D( baseSampler, uv + dir * -filterData01[i]);\n\tsum += texture2D( baseSampler, uv + dir * filterData01[i]);\n}\nsum *= 1.0/filterParams[1];\nfloat t = dist * filterParams[0];\nt = clamp( t ,0.0, 1.0);\ngl_FragColor = mix( color, sum, t );\n}\n";
        private static String mVertexShaderCode = "attribute vec2 texCoords;\nattribute vec4 position;\nvarying vec2 outTexCoords;\nvarying vec2 Pos;\nvoid main() {\n\toutTexCoords = texCoords;\n\tgl_Position = position;\n\tPos = position.xy;\n}\n";
        private int mQuality = 0;
        private float mZoom = 1.0f;

        public ZoomBlurFilter() {
            super(mVertexShaderCode, mFragmentShaderCode);
            useFilterParams();
            useFilterData01();
        }

        public void setPivot(float xRatio, float yRatio) {
        }

        public void setZoomRatio(float zoom) {
            this.mZoom = zoom;
            paramsChanged();
        }

        public void setQuality(int quality) {
            this.mQuality = quality;
            paramsChanged();
        }

        private void paramsChanged() {
            computeZoomBlur();
            notifyWorkerFilters();
        }

        private void computeZoomBlur() {
            if (this.mQuality < 10) {
                this.mQuality = 10;
            }
            if (this.mQuality > 64) {
                this.mQuality = 64;
            }
            for (int i = 0; i < this.mQuality / 2; i++) {
                this.mData1[i] = (1.0f / ((float) (this.mQuality * 2))) * ((float) i);
            }
            setFilterData01Changed();
            setParam(0, this.mZoom);
            setParam(1, (float) this.mQuality);
        }
    }

    private static native void finalizer(long j);

    private static native long native_copy(long j);

    private static native int native_getType(long j);

    private static native float native_getValue(long j, int i);

    private static native long native_init(int i);

    private static native void native_setFragmentShader(long j, String str, String str2);

    private static native void native_setSamplerBitmap(long j, String str, int i, long j2);

    private static native void native_setType(long j, int i);

    private static native void native_setUniformMatrix(long j, String str, int i, int i2, float[] fArr);

    private static native void native_setUniformf(long j, String str, int i, int i2, float[] fArr);

    private static native void native_setUniformi(long j, String str, int i, int i2, int[] iArr);

    private static native void native_setUpdateMargin(long j, int i, int i2, int i3, int i4);

    private static native void native_setValue(long j, int i, float f);

    private static native void native_setVertexShader(long j, String str, String str2);

    protected ImageFilter() {
        this(0);
    }

    protected ImageFilter(int filterType) {
        this.mNativeImageFilter = native_init(filterType);
    }

    protected ImageFilter(ImageFilter filter) {
        this.mNativeImageFilter = native_copy(filter.mNativeImageFilter);
    }

    public ImageFilter copy() {
        return clone();
    }

    public ImageFilter clone() {
        return new ImageFilter(this);
    }

    public int getType() {
        return native_getType(this.mNativeImageFilter);
    }

    public void setValue(int index, float value) {
        native_setValue(this.mNativeImageFilter, index, value);
    }

    protected void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            native_setSamplerBitmap(this.mNativeImageFilter, "filterSamplersStage1", 0, 0);
        }
    }

    protected void setUpdateMargin(int left, int top, int right, int bottom) {
        native_setUpdateMargin(this.mNativeImageFilter, left, top, right, bottom);
    }

    private String getMd5FromStr(String str) {
        try {
            return new BigInteger(1, MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"))).toString(32);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException();
        }
    }

    protected void setVertexShader(String code) {
        native_setVertexShader(this.mNativeImageFilter, code, getMd5FromStr(code));
    }

    protected void setFragmentShader(String code) {
        native_setFragmentShader(this.mNativeImageFilter, code, getMd5FromStr(code));
    }

    protected void setUniformf(String name, int vec, int count, float[] value) {
        if (vec > 0 && vec <= 4 && count > 0) {
            native_setUniformf(this.mNativeImageFilter, name, vec, count, value);
        }
    }

    protected void setUniformi(String name, int vec, int count, int[] value) {
        if (vec > 0 && vec <= 4 && count > 0) {
            native_setUniformi(this.mNativeImageFilter, name, vec, count, value);
        }
    }

    protected void setUniformMatrix(String name, int row, int col, float[] value) {
        if (row >= 2 && row <= 4 && col >= 2 && col <= 4) {
            native_setUniformMatrix(this.mNativeImageFilter, name, row, col, value);
        }
    }

    protected void setSamplerBitmap(String name, int id, Bitmap bitmap) {
        if (bitmap == null) {
            native_setSamplerBitmap(this.mNativeImageFilter, name, id, 0);
        }
    }

    protected float getValue(int index) {
        return native_getValue(this.mNativeImageFilter, index);
    }

    protected void finalize() throws Throwable {
        try {
            finalizer(this.mNativeImageFilter);
        } finally {
            super.finalize();
        }
    }

    public static ImageFilter createImageFilter(int type) {
        switch (type) {
            case 1:
                return new GaussianBlurFilter();
            case 2:
                return new DirectionalBlurFilter();
            case 3:
                return new ZoomBlurFilter();
            case 4:
                return new CosineBlurFilter();
            case 5:
                return new SgiBlurFilter();
            case 16:
                return new ColorizeFilter();
            case 17:
                return new DesaturationFilter();
            case 18:
                return new ColorClampFilter();
            case 49:
                return new DistortionFilter();
            case 50:
                return new VignetteFilter();
            case 51:
                return new MosaicFilter();
            case 52:
                return new BitmapColorMaskFilter();
            case 53:
                return new BlendingFilter();
            case 54:
                return new BlurFilter();
            case 55:
                return new DropShadowFilter();
            default:
                return null;
        }
    }

    public static CustomFilter createCustomFilter(String vertexShaderCode, String fragmentShaderCode) {
        if (vertexShaderCode == null || fragmentShaderCode == null) {
            return null;
        }
        return new CustomFilter(vertexShaderCode, fragmentShaderCode);
    }
}
