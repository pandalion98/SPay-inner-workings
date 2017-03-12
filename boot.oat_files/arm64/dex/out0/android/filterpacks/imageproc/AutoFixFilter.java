package android.filterpacks.imageproc;

import android.R;
import android.app.IActivityManager;
import android.app.StatusBarManager;
import android.bluetooth.BluetoothClass.Device;
import android.filterfw.core.Filter;
import android.filterfw.core.FilterContext;
import android.filterfw.core.Frame;
import android.filterfw.core.FrameFormat;
import android.filterfw.core.GenerateFieldPort;
import android.filterfw.core.Program;
import android.filterfw.core.ShaderProgram;
import android.filterfw.format.ImageFormat;
import android.media.MediaFile;
import android.net.UrlQuerySanitizer.IllegalCharacterValueSanitizer;
import android.net.wifi.passpoint.WifiPasspointInfo;
import android.os.BatteryManager;
import android.provider.Downloads.Impl;

public class AutoFixFilter extends Filter {
    private static final int[] normal_cdf = new int[]{9, 33, 50, 64, 75, 84, 92, 99, 106, 112, 117, 122, 126, 130, 134, 138, 142, 145, 148, 150, 154, 157, 159, 162, 164, 166, 169, 170, 173, 175, 177, 179, 180, 182, 184, 186, 188, 189, 190, 192, 194, 195, 197, 198, 199, 200, 202, 203, 205, 206, 207, 208, 209, 210, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 229, 230, 231, 232, 233, 234, 235, 236, 236, 237, 238, 239, 239, 240, 240, 242, 242, 243, R.styleable.Theme_searchViewStyle, R.styleable.Theme_buttonBarPositiveButtonStyle, R.styleable.Theme_buttonBarPositiveButtonStyle, 246, R.styleable.Theme_buttonBarNegativeButtonStyle, R.styleable.Theme_buttonBarNegativeButtonStyle, R.styleable.Theme_actionBarPopupTheme, R.styleable.Theme_timePickerStyle, R.styleable.Theme_timePickerStyle, R.styleable.Theme_timePickerDialogTheme, R.styleable.Theme_timePickerDialogTheme, R.styleable.Theme_toolbarStyle, 252, 253, 253, 254, 255, 255, 256, 256, 257, 258, 258, 259, 259, 259, 260, R.styleable.Theme_colorEdgeEffect, R.styleable.Theme_dialogPreferredPadding, R.styleable.Theme_dialogPreferredPadding, R.styleable.Theme_colorBackgroundFloating, R.styleable.Theme_colorBackgroundFloating, 264, 264, 265, 265, 266, 267, 267, Device.COMPUTER_LAPTOP, Device.COMPUTER_LAPTOP, 269, 269, 269, 270, 270, 271, Device.COMPUTER_HANDHELD_PC_PDA, Device.COMPUTER_HANDHELD_PC_PDA, 273, 273, 274, 274, 275, 275, Device.COMPUTER_PALM_SIZE_PC_PDA, Device.COMPUTER_PALM_SIZE_PC_PDA, 277, 277, 277, 278, 278, 279, 279, 279, Device.COMPUTER_WEARABLE, Device.COMPUTER_WEARABLE, IActivityManager.NOTIFY_CLEARTEXT_NETWORK_TRANSACTION, IActivityManager.CREATE_STACK_ON_DISPLAY, IActivityManager.CREATE_STACK_ON_DISPLAY, IActivityManager.CREATE_STACK_ON_DISPLAY, IActivityManager.GET_FOCUSED_STACK_ID_TRANSACTION, IActivityManager.GET_FOCUSED_STACK_ID_TRANSACTION, 284, 284, IActivityManager.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION, IActivityManager.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION, IActivityManager.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION, IActivityManager.RESIZE_TASK_TRANSACTION, IActivityManager.RESIZE_TASK_TRANSACTION, IActivityManager.GET_LOCK_TASK_MODE_STATE_TRANSACTION, IActivityManager.GET_LOCK_TASK_MODE_STATE_TRANSACTION, IActivityManager.SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION, IActivityManager.SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION, IActivityManager.SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION, IActivityManager.DUMP_HEAP_FINISHED_TRANSACTION, IActivityManager.DUMP_HEAP_FINISHED_TRANSACTION, IActivityManager.DUMP_HEAP_FINISHED_TRANSACTION, IActivityManager.SET_VOICE_KEEP_AWAKE_TRANSACTION, IActivityManager.SET_VOICE_KEEP_AWAKE_TRANSACTION, IActivityManager.SET_VOICE_KEEP_AWAKE_TRANSACTION, IActivityManager.UPDATE_LOCK_TASK_PACKAGES_TRANSACTION, IActivityManager.NOTE_ALARM_START_TRANSACTION, IActivityManager.NOTE_ALARM_START_TRANSACTION, IActivityManager.NOTE_ALARM_START_TRANSACTION, IActivityManager.NOTE_ALARM_FINISH_TRANSACTION, IActivityManager.NOTE_ALARM_FINISH_TRANSACTION, IActivityManager.GET_PACKAGE_PROCESS_STATE_TRANSACTION, IActivityManager.GET_PACKAGE_PROCESS_STATE_TRANSACTION, IActivityManager.GET_PACKAGE_PROCESS_STATE_TRANSACTION, IActivityManager.SHOW_LOCK_TASK_ESCAPE_MESSAGE_TRANSACTION, IActivityManager.SHOW_LOCK_TASK_ESCAPE_MESSAGE_TRANSACTION, IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION, IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION, IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION, IActivityManager.KEYGUARD_GOING_AWAY_TRANSACTION, IActivityManager.KEYGUARD_GOING_AWAY_TRANSACTION, IActivityManager.KEYGUARD_GOING_AWAY_TRANSACTION, IActivityManager.REGISTER_UID_OBSERVER_TRANSACTION, IActivityManager.REGISTER_UID_OBSERVER_TRANSACTION, IActivityManager.REGISTER_UID_OBSERVER_TRANSACTION, IActivityManager.UNREGISTER_UID_OBSERVER_TRANSACTION, IActivityManager.UNREGISTER_UID_OBSERVER_TRANSACTION, IActivityManager.UNREGISTER_UID_OBSERVER_TRANSACTION, IActivityManager.UNREGISTER_UID_OBSERVER_TRANSACTION, 300, 300, 301, 301, 302, 302, 302, 303, 303, 304, 304, 304, 305, 305, 305, 306, 306, 306, 307, 307, 307, 308, 308, 308, 309, 309, 309, 309, 310, 310, 310, 310, MediaFile.FILE_TYPE_AK3G, 312, 312, 312, 313, 313, 313, 314, 314, 314, 315, 315, 315, 315, 316, 316, 316, 317, 317, 317, 318, 318, 318, 319, 319, 319, 319, 319, 320, 320, 320, MediaFile.FILE_TYPE_ODF_LGU, MediaFile.FILE_TYPE_ODF_LGU, MediaFile.FILE_TYPE_K3GA, MediaFile.FILE_TYPE_K3GA, MediaFile.FILE_TYPE_K3GA, 323, 323, 323, 323, 324, 324, 324, 325, 325, 325, 325, 326, 326, 326, 327, 327, 327, 327, 328, 328, 328, 329, 329, 329, 329, 329, 330, 330, 330, 330, 331, 331, 332, 332, 332, 333, 333, 333, 333, 334, 334, 334, 334, 335, 335, 335, 336, 336, 336, 336, 337, 337, 337, 337, 338, 338, 338, 339, 339, 339, 339, 339, 339, MediaFile.FILE_TYPE_K3G, MediaFile.FILE_TYPE_K3G, MediaFile.FILE_TYPE_K3G, MediaFile.FILE_TYPE_K3G, MediaFile.FILE_TYPE_SKM, MediaFile.FILE_TYPE_SKM, MediaFile.FILE_TYPE_SKV, MediaFile.FILE_TYPE_SKV, MediaFile.FILE_TYPE_SKV, MediaFile.FILE_TYPE_SKV, MediaFile.FILE_TYPE_AK3GV, MediaFile.FILE_TYPE_AK3GV, MediaFile.FILE_TYPE_AK3GV, 344, 344, 344, 344, 345, 345, 345, 345, 346, 346, 346, 346, 347, 347, 347, 347, 348, 348, 348, 348, 349, 349, 349, 349, 349, 349, 350, 350, 350, 350, 351, 351, 352, 352, 352, 352, 353, 353, 353, 353, 354, 354, 354, 354, 355, 355, 355, 355, 356, 356, 356, 356, 357, 357, 357, 357, 358, 358, 358, 358, 359, 359, 359, 359, 359, 359, 359, 360, 360, 360, 360, 361, 361, 362, 362, 362, 362, 363, 363, 363, 363, 364, 364, 364, 364, 365, 365, 365, 365, 366, 366, 366, 366, 366, 367, 367, 367, 367, 368, 368, 368, 368, 369, 369, 369, 369, 369, 369, 370, 370, 370, 370, 370, 371, 371, 372, 372, 372, 372, 373, 373, 373, 373, 374, 374, 374, 374, 374, 375, 375, 375, 375, 376, 376, 376, 376, 377, 377, 377, 377, 378, 378, 378, 378, 378, 379, 379, 379, 379, 379, 379, 380, 380, 380, 380, 381, 381, 381, 382, 382, 382, 382, 383, 383, 383, 383, 384, 384, 384, 384, 385, 385, 385, 385, 385, 386, 386, 386, 386, 387, 387, 387, 387, 388, 388, 388, 388, 388, 389, 389, 389, 389, 389, 389, 390, 390, 390, 390, 391, 391, 392, 392, 392, 392, 392, 393, 393, 393, 393, 394, 394, 394, 394, 395, 395, 395, 395, 396, 396, 396, 396, 396, 397, 397, 397, 397, 398, 398, 398, 398, 399, 399, 399, 399, 399, 399, 400, 400, 400, 400, 400, 401, 401, 402, 402, 402, 402, 403, 403, 403, 403, IllegalCharacterValueSanitizer.URL_LEGAL, IllegalCharacterValueSanitizer.URL_LEGAL, IllegalCharacterValueSanitizer.URL_LEGAL, IllegalCharacterValueSanitizer.URL_LEGAL, IllegalCharacterValueSanitizer.URL_AND_SPACE_LEGAL, IllegalCharacterValueSanitizer.URL_AND_SPACE_LEGAL, IllegalCharacterValueSanitizer.URL_AND_SPACE_LEGAL, IllegalCharacterValueSanitizer.URL_AND_SPACE_LEGAL, Impl.STATUS_NOT_ACCEPTABLE, Impl.STATUS_NOT_ACCEPTABLE, Impl.STATUS_NOT_ACCEPTABLE, Impl.STATUS_NOT_ACCEPTABLE, Impl.STATUS_NOT_ACCEPTABLE, 407, 407, 407, 407, 408, 408, 408, 408, 409, 409, 409, 409, 409, 409, 410, 410, 410, 410, Impl.STATUS_LENGTH_REQUIRED, Impl.STATUS_LENGTH_REQUIRED, Impl.STATUS_PRECONDITION_FAILED, Impl.STATUS_PRECONDITION_FAILED, Impl.STATUS_PRECONDITION_FAILED, Impl.STATUS_PRECONDITION_FAILED, 413, 413, 413, 413, 414, 414, 414, 414, 415, 415, 415, 415, 416, 416, 416, 416, 417, 417, 417, 417, 418, 418, 418, 418, 419, 419, 419, 419, 419, 419, 420, 420, 420, 420, 421, 421, 422, 422, 422, 422, 423, 423, 423, 423, 424, 424, 424, 425, 425, 425, 425, 426, 426, 426, 426, 427, 427, 427, 427, 428, 428, 428, 429, 429, 429, 429, 429, 429, 430, 430, 430, 430, 431, 431, 432, 432, 432, 433, 433, 433, 433, 434, 434, 434, 435, 435, 435, 435, 436, 436, 436, 436, 437, 437, 437, 438, 438, 438, 438, 439, 439, 439, 439, 439, 440, 440, 440, 441, 441, 442, 442, 442, 443, 443, 443, 443, 444, 444, 444, 445, 445, 445, 446, 446, 446, 446, 447, 447, 447, 448, 448, 448, 449, 449, 449, 449, 449, 450, 450, 450, 451, 451, 452, 452, 452, 453, 453, 453, 454, 454, 454, 455, 455, 455, 456, 456, 456, 457, 457, 457, 458, 458, 458, 459, 459, 459, 459, 460, 460, 460, 461, 461, 462, 462, 462, 463, 463, 463, 464, 464, 465, 465, 465, 466, 466, 466, 467, 467, 467, 468, 468, 469, 469, 469, 469, 470, 470, 470, 471, 472, 472, 472, 473, 473, 474, 474, 474, 475, 475, 476, 476, 476, 477, 477, 478, 478, 478, 479, 479, 479, 480, 480, 480, WifiPasspointInfo.PRESET_CRED_MATCH, 482, 482, 483, 483, 484, 484, 484, 485, 485, 486, 486, 487, 487, 488, 488, 488, Impl.STATUS_CANNOT_RESUME, Impl.STATUS_CANNOT_RESUME, Impl.STATUS_CANNOT_RESUME, Impl.STATUS_CANCELED, Impl.STATUS_CANCELED, Impl.STATUS_UNKNOWN_ERROR, Impl.STATUS_FILE_ERROR, Impl.STATUS_FILE_ERROR, Impl.STATUS_UNHANDLED_REDIRECT, Impl.STATUS_UNHANDLED_REDIRECT, Impl.STATUS_UNHANDLED_HTTP_CODE, Impl.STATUS_UNHANDLED_HTTP_CODE, Impl.STATUS_HTTP_DATA_ERROR, Impl.STATUS_HTTP_DATA_ERROR, Impl.STATUS_HTTP_EXCEPTION, Impl.STATUS_HTTP_EXCEPTION, Impl.STATUS_TOO_MANY_REDIRECTS, Impl.STATUS_TOO_MANY_REDIRECTS, Impl.STATUS_BLOCKED, Impl.STATUS_BLOCKED, 499, 499, 499, 500, 501, 502, 502, 503, 503, 504, 504, 505, 505, 506, 507, 507, 508, 508, 509, 509, 510, 510, 511, 512, 513, 513, 514, 515, 515, 516, 517, 517, 518, 519, 519, 519, Device.PHONE_CORDLESS, 521, 522, 523, Device.PHONE_SMART, Device.PHONE_SMART, 525, 526, 526, 527, Device.PHONE_MODEM_OR_GATEWAY, 529, 529, 530, 531, Device.PHONE_ISDN, 533, 534, 535, 535, 536, 537, 538, 539, 539, 540, 542, 543, 544, 545, 546, 547, 548, 549, 549, 550, 552, 553, 554, StatusBarManager.DISABLE_VISIBILITY, StatusBarManager.ENABLE_VISIBILITY, 558, 559, 559, 561, 562, 564, 565, 566, 568, 569, 570, 572, 574, 575, 577, 578, 579, 582, 583, 585, 587, 589, 590, 593, 595, 597, 599, Impl.CD_STATUS_INVALID_DDVERSION, 604, 607, 609, 612, 615, 618, Impl.CD_STATUS_INVALID_CONTENTS, 624, 628, Impl.CD_STATUS_INSUFFICIENT_PHONE_MEMORY, 635, 639, 644, 649, Impl.CD_STATUS_DRM2_DOMAIN_NOT_EXIST, 659, 666, 673, 680, 690, 700, 714};
    private final String mAutoFixShader = "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float scale;\nuniform float shift_scale;\nuniform float hist_offset;\nuniform float hist_scale;\nuniform float density_offset;\nuniform float density_scale;\nvarying vec2 v_texcoord;\nvoid main() {\n  const vec3 weights = vec3(0.33333, 0.33333, 0.33333);\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float energy = dot(color.rgb, weights);\n  float mask_value = energy - 0.5;\n  float alpha;\n  if (mask_value > 0.0) {\n    alpha = (pow(2.0 * mask_value, 1.5) - 1.0) * scale + 1.0;\n  } else { \n    alpha = (pow(2.0 * mask_value, 2.0) - 1.0) * scale + 1.0;\n  }\n  float index = energy * hist_scale + hist_offset;\n  vec4 temp = texture2D(tex_sampler_1, vec2(index, 0.5));\n  float value = temp.g + temp.r * shift_scale;\n  index = value * density_scale + density_offset;\n  temp = texture2D(tex_sampler_2, vec2(index, 0.5));\n  value = temp.g + temp.r * shift_scale;\n  float dst_energy = energy * alpha + value * (1.0 - alpha);\n  float max_energy = energy / max(color.r, max(color.g, color.b));\n  if (dst_energy > max_energy) {\n    dst_energy = max_energy;\n  }\n  if (energy == 0.0) {\n    gl_FragColor = color;\n  } else {\n    gl_FragColor = vec4(color.rgb * dst_energy / energy, color.a);\n  }\n}\n";
    private Frame mDensityFrame;
    private int mHeight = 0;
    private Frame mHistFrame;
    private Program mNativeProgram;
    @GenerateFieldPort(name = "scale")
    private float mScale;
    private Program mShaderProgram;
    private int mTarget = 0;
    @GenerateFieldPort(hasDefault = true, name = "tile_size")
    private int mTileSize = 640;
    private int mWidth = 0;

    public AutoFixFilter(String name) {
        super(name);
    }

    public void setupPorts() {
        addMaskedInputPort("image", ImageFormat.create(3));
        addOutputBasedOnInput("image", "image");
    }

    public FrameFormat getOutputFormat(String portName, FrameFormat inputFormat) {
        return inputFormat;
    }

    public void initProgram(FilterContext context, int target) {
        switch (target) {
            case 3:
                ShaderProgram shaderProgram = new ShaderProgram(context, "precision mediump float;\nuniform sampler2D tex_sampler_0;\nuniform sampler2D tex_sampler_1;\nuniform sampler2D tex_sampler_2;\nuniform float scale;\nuniform float shift_scale;\nuniform float hist_offset;\nuniform float hist_scale;\nuniform float density_offset;\nuniform float density_scale;\nvarying vec2 v_texcoord;\nvoid main() {\n  const vec3 weights = vec3(0.33333, 0.33333, 0.33333);\n  vec4 color = texture2D(tex_sampler_0, v_texcoord);\n  float energy = dot(color.rgb, weights);\n  float mask_value = energy - 0.5;\n  float alpha;\n  if (mask_value > 0.0) {\n    alpha = (pow(2.0 * mask_value, 1.5) - 1.0) * scale + 1.0;\n  } else { \n    alpha = (pow(2.0 * mask_value, 2.0) - 1.0) * scale + 1.0;\n  }\n  float index = energy * hist_scale + hist_offset;\n  vec4 temp = texture2D(tex_sampler_1, vec2(index, 0.5));\n  float value = temp.g + temp.r * shift_scale;\n  index = value * density_scale + density_offset;\n  temp = texture2D(tex_sampler_2, vec2(index, 0.5));\n  value = temp.g + temp.r * shift_scale;\n  float dst_energy = energy * alpha + value * (1.0 - alpha);\n  float max_energy = energy / max(color.r, max(color.g, color.b));\n  if (dst_energy > max_energy) {\n    dst_energy = max_energy;\n  }\n  if (energy == 0.0) {\n    gl_FragColor = color;\n  } else {\n    gl_FragColor = vec4(color.rgb * dst_energy / energy, color.a);\n  }\n}\n");
                shaderProgram.setMaximumTileSize(this.mTileSize);
                this.mShaderProgram = shaderProgram;
                this.mTarget = target;
                return;
            default:
                throw new RuntimeException("Filter Sharpen does not support frames of target " + target + "!");
        }
    }

    private void initParameters() {
        this.mShaderProgram.setHostValue("shift_scale", Float.valueOf(0.00390625f));
        this.mShaderProgram.setHostValue("hist_offset", Float.valueOf(6.527415E-4f));
        this.mShaderProgram.setHostValue("hist_scale", Float.valueOf(0.99869454f));
        this.mShaderProgram.setHostValue("density_offset", Float.valueOf(4.8828125E-4f));
        this.mShaderProgram.setHostValue("density_scale", Float.valueOf(0.99902344f));
        this.mShaderProgram.setHostValue(BatteryManager.EXTRA_SCALE, Float.valueOf(this.mScale));
    }

    protected void prepare(FilterContext context) {
        int[] densityTable = new int[1024];
        for (int i = 0; i < 1024; i++) {
            densityTable[i] = (int) ((((long) normal_cdf[i]) * 65535) / ((long) 766));
        }
        this.mDensityFrame = context.getFrameManager().newFrame(ImageFormat.create(1024, 1, 3, 3));
        this.mDensityFrame.setInts(densityTable);
    }

    public void tearDown(FilterContext context) {
        if (this.mDensityFrame != null) {
            this.mDensityFrame.release();
            this.mDensityFrame = null;
        }
        if (this.mHistFrame != null) {
            this.mHistFrame.release();
            this.mHistFrame = null;
        }
    }

    public void fieldPortValueUpdated(String name, FilterContext context) {
        if (this.mShaderProgram != null) {
            this.mShaderProgram.setHostValue(BatteryManager.EXTRA_SCALE, Float.valueOf(this.mScale));
        }
    }

    public void process(FilterContext context) {
        Frame input = pullInput("image");
        FrameFormat inputFormat = input.getFormat();
        if (this.mShaderProgram == null || inputFormat.getTarget() != this.mTarget) {
            initProgram(context, inputFormat.getTarget());
            initParameters();
        }
        if (!(inputFormat.getWidth() == this.mWidth && inputFormat.getHeight() == this.mHeight)) {
            this.mWidth = inputFormat.getWidth();
            this.mHeight = inputFormat.getHeight();
            createHistogramFrame(context, this.mWidth, this.mHeight, input.getInts());
        }
        Frame output = context.getFrameManager().newFrame(inputFormat);
        this.mShaderProgram.process(new Frame[]{input, this.mHistFrame, this.mDensityFrame}, output);
        pushOutput("image", output);
        output.release();
    }

    private void createHistogramFrame(FilterContext context, int width, int height, int[] data) {
        int i;
        int[] histArray = new int[766];
        int y_border_thickness = (int) (((float) height) * 0.05f);
        int x_border_thickness = (int) (((float) width) * 0.05f);
        int pixels = (width - (x_border_thickness * 2)) * (height - (y_border_thickness * 2));
        for (int y = y_border_thickness; y < height - y_border_thickness; y++) {
            for (int x = x_border_thickness; x < width - x_border_thickness; x++) {
                int index = (y * width) + x;
                int energy = ((data[index] & 255) + ((data[index] >> 8) & 255)) + ((data[index] >> 16) & 255);
                histArray[energy] = histArray[energy] + 1;
            }
        }
        for (i = 1; i < 766; i++) {
            histArray[i] = histArray[i] + histArray[i - 1];
        }
        for (i = 0; i < 766; i++) {
            histArray[i] = (int) ((65535 * ((long) histArray[i])) / ((long) pixels));
        }
        FrameFormat shaderHistFormat = ImageFormat.create(766, 1, 3, 3);
        if (this.mHistFrame != null) {
            this.mHistFrame.release();
        }
        this.mHistFrame = context.getFrameManager().newFrame(shaderHistFormat);
        this.mHistFrame.setInts(histArray);
    }
}
