package com.samsung.android.contextaware.manager;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.HashSet;

public class ContextAwarePropertyBundle implements Parcelable {
    protected static final Creator<ContextAwarePropertyBundle> CREATOR = new Creator<ContextAwarePropertyBundle>() {
        public ContextAwarePropertyBundle createFromParcel(Parcel source) {
            return new ContextAwarePropertyBundle(source);
        }

        public ContextAwarePropertyBundle[] newArray(int size) {
            return new ContextAwarePropertyBundle[size];
        }
    };
    private static final boolean[] booleanVal = new boolean[1];
    private static char[] charArrayVal;
    private static ArrayList<Double> doubleArrayListVal;
    private static double[] doubleArrayVal;
    private static HashSet<Double> doubleHashSetVal;
    private static double doubleVal;
    private static ArrayList<Float> floatArrayListVal;
    private static float[] floatArrayVal;
    private static HashSet<Float> floatHashSetVal;
    private static float floatVal;
    private static ArrayList<Integer> integerArrayListVal;
    private static int[] integerArrayVal;
    private static HashSet<Integer> integerHashSetVal;
    private static int integerVal;
    private static ArrayList<Long> longArrayListVal;
    private static long[] longArrayVal;
    private static HashSet<Long> longHashSetVal;
    private static long longVal;
    private static ArrayList<String> stringArrayListVal;
    private static String[] stringArrayVal;
    private static HashSet<String> stringHashSetVal;
    private static String stringVal;
    private int mType;

    private enum PropertyType {
        BOOLEAN_TYPE {
            <T> void setValue(T value) {
                if (value instanceof Boolean) {
                    ContextAwarePropertyBundle.booleanVal[0] = ((Boolean) value).booleanValue();
                }
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.booleanVal;
            }
        },
        INTEGER_TYPE {
            <T> void setValue(T value) {
                if (value instanceof Integer) {
                    ContextAwarePropertyBundle.integerVal = ((Integer) value).intValue();
                } else if (value instanceof Long) {
                    ContextAwarePropertyBundle.integerVal = ((Long) value).intValue();
                } else if (value instanceof Float) {
                    ContextAwarePropertyBundle.integerVal = ((Float) value).intValue();
                } else if (value instanceof Double) {
                    ContextAwarePropertyBundle.integerVal = ((Double) value).intValue();
                }
            }

            <E> E getValue() {
                return Integer.valueOf(ContextAwarePropertyBundle.integerVal);
            }
        },
        LONG_TYPE {
            <T> void setValue(T value) {
                if (value instanceof Integer) {
                    ContextAwarePropertyBundle.longVal = ((Integer) value).longValue();
                } else if (value instanceof Long) {
                    ContextAwarePropertyBundle.longVal = ((Long) value).longValue();
                } else if (value instanceof Float) {
                    ContextAwarePropertyBundle.longVal = ((Float) value).longValue();
                } else if (value instanceof Double) {
                    ContextAwarePropertyBundle.longVal = ((Double) value).longValue();
                }
            }

            <E> E getValue() {
                return Long.valueOf(ContextAwarePropertyBundle.longVal);
            }
        },
        FLOAT_TYPE {
            <T> void setValue(T value) {
                if (value instanceof Integer) {
                    ContextAwarePropertyBundle.floatVal = ((Integer) value).floatValue();
                } else if (value instanceof Long) {
                    ContextAwarePropertyBundle.floatVal = ((Long) value).floatValue();
                } else if (value instanceof Float) {
                    ContextAwarePropertyBundle.floatVal = ((Float) value).floatValue();
                } else if (value instanceof Double) {
                    ContextAwarePropertyBundle.floatVal = (float) ((Double) value).longValue();
                }
            }

            <E> E getValue() {
                return Float.valueOf(ContextAwarePropertyBundle.floatVal);
            }
        },
        DOUBLE_TYPE {
            <T> void setValue(T value) {
                if (value instanceof Integer) {
                    ContextAwarePropertyBundle.doubleVal = ((Integer) value).doubleValue();
                } else if (value instanceof Long) {
                    ContextAwarePropertyBundle.doubleVal = ((Long) value).doubleValue();
                } else if (value instanceof Float) {
                    ContextAwarePropertyBundle.doubleVal = ((Float) value).doubleValue();
                } else if (value instanceof Double) {
                    ContextAwarePropertyBundle.doubleVal = ((Double) value).doubleValue();
                }
            }

            <E> E getValue() {
                return Double.valueOf(ContextAwarePropertyBundle.doubleVal);
            }
        },
        STRING_TYPE {
            <T> void setValue(T value) {
                if (value instanceof String) {
                    ContextAwarePropertyBundle.stringVal = (String) value;
                }
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.stringVal;
            }
        },
        CHAR_ARRAY_TYPE {
            <T> void setValue(T value) {
                if (value instanceof char[]) {
                    ContextAwarePropertyBundle.charArrayVal = (char[]) value;
                }
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.charArrayVal;
            }
        },
        INTEGER_ARRAY_TYPE {
            <T> void setValue(T value) {
                if (value instanceof int[]) {
                    ContextAwarePropertyBundle.integerArrayVal = (int[]) value;
                }
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.integerArrayVal;
            }
        },
        LONG_ARRAY_TYPE {
            <T> void setValue(T value) {
                if (value instanceof long[]) {
                    ContextAwarePropertyBundle.longArrayVal = (long[]) value;
                }
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.longArrayVal;
            }
        },
        FLOAT_ARRAY_TYPE {
            <T> void setValue(T value) {
                if (value instanceof float[]) {
                    ContextAwarePropertyBundle.floatArrayVal = (float[]) value;
                }
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.floatArrayVal;
            }
        },
        DOUBLE_ARRAY_TYPE {
            <T> void setValue(T value) {
                if (value instanceof double[]) {
                    ContextAwarePropertyBundle.doubleArrayVal = (double[]) value;
                }
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.doubleArrayVal;
            }
        },
        STRING_ARRAY_TYPE {
            <T> void setValue(T value) {
                if (value instanceof String[]) {
                    ContextAwarePropertyBundle.stringArrayVal = (String[]) value;
                }
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.stringArrayVal;
            }
        },
        INTEGER_ARRAY_LIST_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.integerArrayListVal = (ArrayList) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.integerArrayListVal;
            }
        },
        LONG_ARRAY_LIST_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.longArrayListVal = (ArrayList) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.longArrayListVal;
            }
        },
        FLOAT_ARRAY_LIST_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.floatArrayListVal = (ArrayList) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.floatArrayListVal;
            }
        },
        DOUBLE_ARRAY_LIST_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.doubleArrayListVal = (ArrayList) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.doubleArrayListVal;
            }
        },
        STRING_ARRAY_LIST_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.stringArrayListVal = (ArrayList) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.stringArrayListVal;
            }
        },
        INTEGER_HASH_SET_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.integerHashSetVal = (HashSet) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.integerHashSetVal;
            }
        },
        LONG_HASH_SET_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.longHashSetVal = (HashSet) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.longHashSetVal;
            }
        },
        FLOAT_HASH_SET_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.floatHashSetVal = (HashSet) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.floatHashSetVal;
            }
        },
        DOUBLE_HASH_SET_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.doubleHashSetVal = (HashSet) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.doubleHashSetVal;
            }
        },
        STRING_HASH_SET_TYPE {
            <T> void setValue(T value) {
                ContextAwarePropertyBundle.stringHashSetVal = (HashSet) value;
            }

            <E> E getValue() {
                return ContextAwarePropertyBundle.stringHashSetVal;
            }
        };

        abstract <E> E getValue();

        abstract <T> void setValue(T t);

        protected int getCode() {
            return ordinal();
        }
    }

    public ContextAwarePropertyBundle() {
        setType(-1);
        PropertyType.BOOLEAN_TYPE.setValue(Boolean.FALSE);
        PropertyType.INTEGER_TYPE.setValue(Integer.valueOf(0));
        PropertyType.LONG_TYPE.setValue(Long.valueOf(0));
        PropertyType.FLOAT_TYPE.setValue(Float.valueOf(0.0f));
        PropertyType.DOUBLE_TYPE.setValue(Double.valueOf(0.0d));
        PropertyType.STRING_TYPE.setValue("");
    }

    protected ContextAwarePropertyBundle(Parcel input) {
        readFromParcel(input);
    }

    public <E> E getValue() {
        for (PropertyType i : PropertyType.values()) {
            if (getType() == i.getCode()) {
                return i.getValue();
            }
        }
        return null;
    }

    public <T> void setValue(int type, T value) {
        for (PropertyType i : PropertyType.values()) {
            if (type == i.getCode()) {
                setType(type);
                i.setValue(value);
                return;
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBooleanArray((boolean[]) PropertyType.BOOLEAN_TYPE.getValue());
        dest.writeInt(((Integer) PropertyType.INTEGER_TYPE.getValue()).intValue());
        dest.writeLong(((Long) PropertyType.LONG_TYPE.getValue()).longValue());
        dest.writeFloat(((Float) PropertyType.FLOAT_TYPE.getValue()).floatValue());
        dest.writeDouble(((Double) PropertyType.DOUBLE_TYPE.getValue()).doubleValue());
        dest.writeString((String) PropertyType.STRING_TYPE.getValue());
        dest.writeCharArray((char[]) PropertyType.CHAR_ARRAY_TYPE.getValue());
        dest.writeIntArray((int[]) PropertyType.INTEGER_ARRAY_TYPE.getValue());
        dest.writeLongArray((long[]) PropertyType.LONG_ARRAY_TYPE.getValue());
        dest.writeFloatArray((float[]) PropertyType.FLOAT_ARRAY_TYPE.getValue());
        dest.writeDoubleArray((double[]) PropertyType.DOUBLE_ARRAY_TYPE.getValue());
        dest.writeStringArray((String[]) PropertyType.STRING_ARRAY_TYPE.getValue());
        dest.writeSerializable((ArrayList) PropertyType.INTEGER_ARRAY_LIST_TYPE.getValue());
        dest.writeSerializable((ArrayList) PropertyType.LONG_ARRAY_LIST_TYPE.getValue());
        dest.writeSerializable((ArrayList) PropertyType.FLOAT_ARRAY_LIST_TYPE.getValue());
        dest.writeSerializable((ArrayList) PropertyType.DOUBLE_ARRAY_LIST_TYPE.getValue());
        dest.writeSerializable((ArrayList) PropertyType.STRING_ARRAY_LIST_TYPE.getValue());
        dest.writeSerializable((HashSet) PropertyType.INTEGER_HASH_SET_TYPE.getValue());
        dest.writeSerializable((HashSet) PropertyType.LONG_HASH_SET_TYPE.getValue());
        dest.writeSerializable((HashSet) PropertyType.FLOAT_HASH_SET_TYPE.getValue());
        dest.writeSerializable((HashSet) PropertyType.DOUBLE_HASH_SET_TYPE.getValue());
        dest.writeSerializable((HashSet) PropertyType.STRING_HASH_SET_TYPE.getValue());
        dest.writeInt(getType());
    }

    private void readFromParcel(Parcel src) {
        src.readBooleanArray((boolean[]) PropertyType.BOOLEAN_TYPE.getValue());
        PropertyType.INTEGER_TYPE.setValue(Integer.valueOf(src.readInt()));
        PropertyType.LONG_TYPE.setValue(Long.valueOf(src.readLong()));
        PropertyType.FLOAT_TYPE.setValue(Float.valueOf(src.readFloat()));
        PropertyType.DOUBLE_TYPE.setValue(Double.valueOf(src.readDouble()));
        PropertyType.STRING_TYPE.setValue(src.readString());
        PropertyType.CHAR_ARRAY_TYPE.setValue(src.createCharArray());
        PropertyType.INTEGER_ARRAY_TYPE.setValue(src.createIntArray());
        PropertyType.LONG_ARRAY_TYPE.setValue(src.createLongArray());
        PropertyType.FLOAT_ARRAY_TYPE.setValue(src.createFloatArray());
        PropertyType.DOUBLE_ARRAY_TYPE.setValue(src.createDoubleArray());
        PropertyType.STRING_ARRAY_TYPE.setValue(src.createStringArray());
        PropertyType.INTEGER_ARRAY_LIST_TYPE.setValue((ArrayList) src.readSerializable());
        PropertyType.LONG_ARRAY_LIST_TYPE.setValue((ArrayList) src.readSerializable());
        PropertyType.FLOAT_ARRAY_LIST_TYPE.setValue((ArrayList) src.readSerializable());
        PropertyType.DOUBLE_ARRAY_LIST_TYPE.setValue((ArrayList) src.readSerializable());
        PropertyType.STRING_ARRAY_LIST_TYPE.setValue((ArrayList) src.readSerializable());
        PropertyType.INTEGER_HASH_SET_TYPE.setValue((HashSet) src.readSerializable());
        PropertyType.LONG_HASH_SET_TYPE.setValue((HashSet) src.readSerializable());
        PropertyType.FLOAT_HASH_SET_TYPE.setValue((HashSet) src.readSerializable());
        PropertyType.DOUBLE_HASH_SET_TYPE.setValue((HashSet) src.readSerializable());
        PropertyType.STRING_HASH_SET_TYPE.setValue((HashSet) src.readSerializable());
        setType(src.readInt());
    }

    public int getBooleanTypeCode() {
        return PropertyType.BOOLEAN_TYPE.getCode();
    }

    public int getIntegerTypeCode() {
        return PropertyType.INTEGER_TYPE.getCode();
    }

    public int getLongTypeCode() {
        return PropertyType.LONG_TYPE.getCode();
    }

    public int getFloatTypeCode() {
        return PropertyType.FLOAT_TYPE.getCode();
    }

    public int getDoubleTypeCode() {
        return PropertyType.DOUBLE_TYPE.getCode();
    }

    public int getStringTypeCode() {
        return PropertyType.STRING_TYPE.getCode();
    }

    public int getCharArrayTypeCode() {
        return PropertyType.CHAR_ARRAY_TYPE.getCode();
    }

    public int getIntegerArrayTypeCode() {
        return PropertyType.INTEGER_ARRAY_TYPE.getCode();
    }

    public int getLongArrayTypeCode() {
        return PropertyType.LONG_ARRAY_TYPE.getCode();
    }

    public int getFloatArrayTypeCode() {
        return PropertyType.FLOAT_ARRAY_TYPE.getCode();
    }

    public int getDoubleArrayTypeCode() {
        return PropertyType.DOUBLE_ARRAY_TYPE.getCode();
    }

    public int getStringArrayTypeCode() {
        return PropertyType.STRING_ARRAY_TYPE.getCode();
    }

    public int getIntegerArrayListTypeCode() {
        return PropertyType.INTEGER_ARRAY_LIST_TYPE.getCode();
    }

    public int getLongArrayListTypeCode() {
        return PropertyType.LONG_ARRAY_LIST_TYPE.getCode();
    }

    public int getFloatArrayListTypeCode() {
        return PropertyType.FLOAT_ARRAY_LIST_TYPE.getCode();
    }

    public int getDoubleArrayListTypeCode() {
        return PropertyType.DOUBLE_ARRAY_LIST_TYPE.getCode();
    }

    public int getStringArrayListTypeCode() {
        return PropertyType.STRING_ARRAY_LIST_TYPE.getCode();
    }

    public int getIntegerHashSetTypeCode() {
        return PropertyType.INTEGER_HASH_SET_TYPE.getCode();
    }

    public int getLongHashSetTypeCode() {
        return PropertyType.LONG_HASH_SET_TYPE.getCode();
    }

    public int getFloatHashSetTypeCode() {
        return PropertyType.FLOAT_HASH_SET_TYPE.getCode();
    }

    public int getDoubleHashSetTypeCode() {
        return PropertyType.DOUBLE_HASH_SET_TYPE.getCode();
    }

    public int getStringHashSetTypeCode() {
        return PropertyType.STRING_HASH_SET_TYPE.getCode();
    }

    private void setType(int type) {
        this.mType = type;
    }

    public int getType() {
        return this.mType;
    }
}
