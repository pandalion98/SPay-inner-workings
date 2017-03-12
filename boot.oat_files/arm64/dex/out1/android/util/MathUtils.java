package android.util;

import java.util.Random;

public final class MathUtils {
    private static final float DEG_TO_RAD = 0.017453292f;
    private static final float RAD_TO_DEG = 57.295784f;
    private static final Random sRandom = new Random();

    private MathUtils() {
    }

    public static float abs(float v) {
        return v > 0.0f ? v : -v;
    }

    public static int constrain(int amount, int low, int high) {
        if (amount < low) {
            return low;
        }
        return amount > high ? high : amount;
    }

    public static long constrain(long amount, long low, long high) {
        if (amount < low) {
            return low;
        }
        return amount > high ? high : amount;
    }

    public static float constrain(float amount, float low, float high) {
        if (amount < low) {
            return low;
        }
        return amount > high ? high : amount;
    }

    public static float log(float a) {
        return (float) Math.log((double) a);
    }

    public static float exp(float a) {
        return (float) Math.exp((double) a);
    }

    public static float pow(float a, float b) {
        return (float) Math.pow((double) a, (double) b);
    }

    public static float max(float a, float b) {
        return a > b ? a : b;
    }

    public static float max(int a, int b) {
        return a > b ? (float) a : (float) b;
    }

    public static float max(float a, float b, float c) {
        return a > b ? a > c ? a : c : b > c ? b : c;
    }

    public static float max(int a, int b, int c) {
        if (a > b) {
            if (a <= c) {
                a = c;
            }
            return (float) a;
        }
        if (b <= c) {
            b = c;
        }
        return (float) b;
    }

    public static float min(float a, float b) {
        return a < b ? a : b;
    }

    public static float min(int a, int b) {
        return a < b ? (float) a : (float) b;
    }

    public static float min(float a, float b, float c) {
        return a < b ? a < c ? a : c : b < c ? b : c;
    }

    public static float min(int a, int b, int c) {
        if (a < b) {
            if (a >= c) {
                a = c;
            }
            return (float) a;
        }
        if (b >= c) {
            b = c;
        }
        return (float) b;
    }

    public static float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.hypot((double) (x2 - x1), (double) (y2 - y1));
    }

    public static float dist(float x1, float y1, float z1, float x2, float y2, float z2) {
        float x = x2 - x1;
        float y = y2 - y1;
        float z = z2 - z1;
        return (float) Math.sqrt((double) (((x * x) + (y * y)) + (z * z)));
    }

    public static float mag(float a, float b) {
        return (float) Math.hypot((double) a, (double) b);
    }

    public static float mag(float a, float b, float c) {
        return (float) Math.sqrt((double) (((a * a) + (b * b)) + (c * c)));
    }

    public static float sq(float v) {
        return v * v;
    }

    public static float dot(float v1x, float v1y, float v2x, float v2y) {
        return (v1x * v2x) + (v1y * v2y);
    }

    public static float cross(float v1x, float v1y, float v2x, float v2y) {
        return (v1x * v2y) - (v1y * v2x);
    }

    public static float radians(float degrees) {
        return DEG_TO_RAD * degrees;
    }

    public static float degrees(float radians) {
        return RAD_TO_DEG * radians;
    }

    public static float acos(float value) {
        return (float) Math.acos((double) value);
    }

    public static float asin(float value) {
        return (float) Math.asin((double) value);
    }

    public static float atan(float value) {
        return (float) Math.atan((double) value);
    }

    public static float atan2(float a, float b) {
        return (float) Math.atan2((double) a, (double) b);
    }

    public static float tan(float angle) {
        return (float) Math.tan((double) angle);
    }

    public static float lerp(float start, float stop, float amount) {
        return ((stop - start) * amount) + start;
    }

    public static float norm(float start, float stop, float value) {
        return (value - start) / (stop - start);
    }

    public static float map(float minStart, float minStop, float maxStart, float maxStop, float value) {
        return ((maxStart - maxStop) * ((value - minStart) / (minStop - minStart))) + maxStart;
    }

    public static int random(int howbig) {
        return (int) (sRandom.nextFloat() * ((float) howbig));
    }

    public static int random(int howsmall, int howbig) {
        return howsmall >= howbig ? howsmall : (int) ((sRandom.nextFloat() * ((float) (howbig - howsmall))) + ((float) howsmall));
    }

    public static float random(float howbig) {
        return sRandom.nextFloat() * howbig;
    }

    public static float random(float howsmall, float howbig) {
        return howsmall >= howbig ? howsmall : howsmall + (sRandom.nextFloat() * (howbig - howsmall));
    }

    public static void randomSeed(long seed) {
        sRandom.setSeed(seed);
    }
}
