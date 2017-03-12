package com.samsung.android.smartclip;

import android.util.Log;
import com.samsung.android.smartface.SmartFaceManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/* compiled from: SmartClipDataCropperImpl */
class ReflectionUtils {
    public static final int MATCH_TYPE_CLASS_NAME_ONLY = 1;
    public static final int MATCH_TYPE_FULL_NAME = 0;
    private static final String TAG = "ReflectionUtils";

    ReflectionUtils() {
    }

    protected static int getArraySize(Object array, String objectTypeStr) {
        if (objectTypeStr.startsWith("[I")) {
            return ((int[]) array).length;
        }
        if (objectTypeStr.startsWith("[Z")) {
            return ((boolean[]) array).length;
        }
        if (objectTypeStr.startsWith("[J")) {
            return ((long[]) array).length;
        }
        if (objectTypeStr.startsWith("[B")) {
            return ((byte[]) array).length;
        }
        if (objectTypeStr.startsWith("[F")) {
            return ((float[]) array).length;
        }
        if (objectTypeStr.startsWith("[C")) {
            return ((char[]) array).length;
        }
        if (objectTypeStr.startsWith("[S")) {
            return ((short[]) array).length;
        }
        if (objectTypeStr.startsWith("[D")) {
            return ((double[]) array).length;
        }
        if (objectTypeStr.startsWith("[L")) {
            return ((Object[]) array).length;
        }
        return 0;
    }

    protected static Object getArrayValueObject(Object array, int index) {
        String dataTypeStr = array.getClass().getName();
        if (dataTypeStr.startsWith("[I")) {
            return new Integer(((int[]) array)[index]);
        }
        if (dataTypeStr.startsWith("[Z")) {
            return new Boolean(((boolean[]) array)[index]);
        }
        if (dataTypeStr.startsWith("[J")) {
            return new Long(((long[]) array)[index]);
        }
        if (dataTypeStr.startsWith("[B")) {
            return new Byte(((byte[]) array)[index]);
        }
        if (dataTypeStr.startsWith("[F")) {
            return new Float(((float[]) array)[index]);
        }
        if (dataTypeStr.startsWith("[C")) {
            return new Integer(((char[]) array)[index]);
        }
        if (dataTypeStr.startsWith("[S")) {
            return new Short(((short[]) array)[index]);
        }
        if (dataTypeStr.startsWith("[D")) {
            return new Double(((double[]) array)[index]);
        }
        if (dataTypeStr.startsWith("[L")) {
            return ((Object[]) array)[index];
        }
        return "Unknown(" + dataTypeStr + ")";
    }

    protected static boolean isPrimitiveDataType(String dataType) {
        if (dataType.equals("short") || dataType.equals("int") || dataType.equals("long") || dataType.equals("char") || dataType.equals("byte") || dataType.equals("float") || dataType.equals("double") || dataType.equals("boolean")) {
            return true;
        }
        return false;
    }

    protected static int findObjFromArrayList(ArrayList<Object> arrayList, Object objToFind) {
        int arraySize = arrayList.size();
        for (int i = 0; i < arraySize; i++) {
            if (objToFind == arrayList.get(i)) {
                return i;
            }
        }
        return -1;
    }

    protected static String getIndentString(int depth) {
        String indent = "";
        for (int i = 0; i < depth; i++) {
            indent = indent + "\t";
        }
        return indent;
    }

    protected static String extractClassNameFromFullClassPath(String classPath) {
        String[] strs = classPath.split("\\.");
        if (strs.length == 0) {
            return "";
        }
        return strs[strs.length - 1];
    }

    public static void dumpObjectFieldsWithClassTypeFilter(Object objToDump, String objName, int maxDepth, String classTypeFilter) {
        ArrayList<Object> objList = new ArrayList();
        Log.e(TAG, "-------- Field list dump start : " + objToDump.toString() + " / Object type filter : " + classTypeFilter + " ----------");
        dumpObjectFields(objToDump, objList, objName, null, "", 0, maxDepth, classTypeFilter, null);
        Log.e(TAG, "-------- Field list dump finished ----------");
    }

    public static void dumpObjectFieldsWithValueFilter(Object objToDump, String objName, int maxDepth, String valueFilter) {
        ArrayList<Object> objList = new ArrayList();
        Log.e(TAG, "-------- Field list dump start : " + objToDump.toString() + " / Value filter : " + valueFilter + " ----------");
        dumpObjectFields(objToDump, objList, objName, null, "", 0, maxDepth, null, valueFilter);
        Log.e(TAG, "-------- Field list dump finished ----------");
    }

    public static void dumpObjectFields(Object objToDump, String objName, int maxDepth) {
        ArrayList<Object> objList = new ArrayList();
        Log.e(TAG, "-------- Field list dump start : " + objToDump.toString() + " ----------");
        dumpObjectFields(objToDump, objList, objName, null, "", 0, maxDepth, null, null);
        Log.e(TAG, "-------- Field list dump finished ----------");
    }

    protected static void dumpObjectFields(Object objToDump, ArrayList<Object> dumpedObj, String fieldNamePrefix, Field fieldInfo, String fullPath, int depth, int maxDepth, String classTypeFilter, String valueFilter) {
        if (objToDump != null) {
            String objectValueStr;
            String indent;
            String fieldTypeStr;
            String fieldName;
            int arrayLength;
            int i;
            Object o;
            int i$;
            boolean accessable;
            Object fieldObject;
            boolean isFinalField;
            boolean isStaticField;
            Class<?> curObjClass = objToDump.getClass();
            String objectTypeStr = curObjClass.getName();
            boolean alreadyDumpedObj = findObjFromArrayList(dumpedObj, objToDump) != -1;
            if (!curObjClass.isPrimitive()) {
                if (!objectTypeStr.contains("java.lang.")) {
                    objectValueStr = "@" + Integer.toHexString(objToDump.hashCode());
                    if (curObjClass.isArray()) {
                        objectValueStr = objectValueStr + " [arraySize = " + getArraySize(objToDump, objectTypeStr) + "]";
                    }
                    indent = getIndentString(depth);
                    fieldTypeStr = (fieldInfo == null ? fieldInfo.getType().getName() : "").replace("[L", "");
                    if (fieldNamePrefix == null) {
                        fieldNamePrefix = "";
                    }
                    fieldName = fieldNamePrefix + (fieldInfo == null ? fieldInfo.getName() : "");
                    if (fullPath == null) {
                        fullPath = "";
                    }
                    if ((classTypeFilter == null || classTypeFilter.equals(objectTypeStr)) && (valueFilter == null || valueFilter.equals(objectValueStr))) {
                        if (!curObjClass.isPrimitive() || fieldTypeStr.equals(objectTypeStr)) {
                            Log.e(TAG, indent + fieldName + " = " + objectValueStr + " (" + fieldTypeStr + ") : " + fullPath);
                        } else {
                            Log.e(TAG, indent + fieldName + " = " + objectValueStr + " (" + fieldTypeStr + " / " + objectTypeStr + ") : " + fullPath);
                        }
                    }
                    if (!fullPath.equals("")) {
                        fullPath = fullPath + ".";
                    }
                    fullPath = fullPath + fieldName + "(" + extractClassNameFromFullClassPath(objectTypeStr) + ")";
                    if (!alreadyDumpedObj) {
                        if (depth + 1 < maxDepth) {
                            dumpedObj.add(objToDump);
                        }
                        if (curObjClass.isArray()) {
                            arrayLength = getArraySize(objToDump, objectTypeStr);
                            i = 0;
                            while (i < arrayLength && i < 100) {
                                o = getArrayValueObject(objToDump, i);
                                if (!(o == null || (o.getClass().isPrimitive() && o.toString().equals(SmartFaceManager.PAGE_MIDDLE)))) {
                                    dumpObjectFields(o, dumpedObj, "[" + i + "]", null, fullPath, depth + 1, maxDepth, classTypeFilter, valueFilter);
                                }
                                i++;
                            }
                            if (arrayLength > 100) {
                                Log.e(TAG, indent + "\t[Dumped until index " + 100 + "]");
                            }
                        } else if (!isPrimitiveDataType(objectTypeStr)) {
                            if (!objectTypeStr.contains("java.lang.")) {
                                while (curObjClass != null) {
                                    for (Field field : curObjClass.getDeclaredFields()) {
                                        try {
                                            accessable = field.isAccessible();
                                            field.setAccessible(true);
                                            fieldObject = field.get(objToDump);
                                            field.setAccessible(accessable);
                                        } catch (IllegalArgumentException e) {
                                            fieldObject = null;
                                            e.printStackTrace();
                                        } catch (IllegalAccessException e2) {
                                            fieldObject = null;
                                            e2.printStackTrace();
                                        }
                                        isFinalField = (field.getModifiers() & 16) == 0;
                                        isStaticField = (field.getModifiers() & 8) == 0;
                                        if (!field.isEnumConstant() && (!(isStaticField && isFinalField) && depth + 1 < maxDepth)) {
                                            dumpObjectFields(fieldObject, dumpedObj, null, field, fullPath, depth + 1, maxDepth, classTypeFilter, valueFilter);
                                        }
                                    }
                                    curObjClass = curObjClass.getSuperclass();
                                }
                            }
                        }
                    }
                }
            }
            objectValueStr = objToDump.toString();
            if (curObjClass.isArray()) {
                objectValueStr = objectValueStr + " [arraySize = " + getArraySize(objToDump, objectTypeStr) + "]";
            }
            indent = getIndentString(depth);
            if (fieldInfo == null) {
            }
            fieldTypeStr = (fieldInfo == null ? fieldInfo.getType().getName() : "").replace("[L", "");
            if (fieldNamePrefix == null) {
                fieldNamePrefix = "";
            }
            if (fieldInfo == null) {
            }
            fieldName = fieldNamePrefix + (fieldInfo == null ? fieldInfo.getName() : "");
            if (fullPath == null) {
                fullPath = "";
            }
            if (!curObjClass.isPrimitive()) {
            }
            Log.e(TAG, indent + fieldName + " = " + objectValueStr + " (" + fieldTypeStr + ") : " + fullPath);
            if (fullPath.equals("")) {
                fullPath = fullPath + ".";
            }
            fullPath = fullPath + fieldName + "(" + extractClassNameFromFullClassPath(objectTypeStr) + ")";
            if (!alreadyDumpedObj) {
                if (depth + 1 < maxDepth) {
                    dumpedObj.add(objToDump);
                }
                if (curObjClass.isArray()) {
                    arrayLength = getArraySize(objToDump, objectTypeStr);
                    i = 0;
                    while (i < arrayLength) {
                        o = getArrayValueObject(objToDump, i);
                        dumpObjectFields(o, dumpedObj, "[" + i + "]", null, fullPath, depth + 1, maxDepth, classTypeFilter, valueFilter);
                        i++;
                    }
                    if (arrayLength > 100) {
                        Log.e(TAG, indent + "\t[Dumped until index " + 100 + "]");
                    }
                } else if (!isPrimitiveDataType(objectTypeStr)) {
                    if (!objectTypeStr.contains("java.lang.")) {
                        while (curObjClass != null) {
                            for (i$ = 0; i$ < len$; i$++) {
                                accessable = field.isAccessible();
                                field.setAccessible(true);
                                fieldObject = field.get(objToDump);
                                field.setAccessible(accessable);
                                if ((field.getModifiers() & 16) == 0) {
                                }
                                if ((field.getModifiers() & 8) == 0) {
                                }
                                dumpObjectFields(fieldObject, dumpedObj, null, field, fullPath, depth + 1, maxDepth, classTypeFilter, valueFilter);
                            }
                            curObjClass = curObjClass.getSuperclass();
                        }
                    }
                }
            }
        }
    }

    public static void dumpObjectMethods(Object objToDump) {
        Log.d(TAG, "-------- Method list dump start : " + objToDump.toString() + " ----------");
        for (Class<?> curObjClass = objToDump.getClass(); curObjClass != null; curObjClass = curObjClass.getSuperclass()) {
            Log.d(TAG, " -- Methods of " + curObjClass.getName() + " class --");
            for (Method method : curObjClass.getDeclaredMethods()) {
                Log.d(TAG, method.toGenericString());
            }
        }
        Log.d(TAG, "-------- Method list dump finished ----------");
    }

    public static void dumpClassHierarchy(Object objToDump) {
        Class<?> objClass = objToDump.getClass();
        Log.d(TAG, "-------- Class hierarchy dump start : " + objToDump.toString() + " ----------");
        for (Class<?> c = objClass; c != null; c = c.getSuperclass()) {
            Log.d(TAG, "-- Class name : " + c.getName());
            Class<?>[] clz = c.getInterfaces();
            for (Class<?> cls : clz) {
                Log.d(TAG, "   + interfaces : " + cls.getName());
            }
        }
        Log.d(TAG, "-------- Class hierarchy dump finished ----------");
    }

    protected static void getFieldObjectByObjectType(Object srcObj, int matchType, String fieldObjectType, int maxSearchResultCount, ArrayList<Object> searchResult, int curDepth, int maxDepth, boolean skipWellKnownClass) {
        if (srcObj != null && fieldObjectType != null && curDepth != maxDepth) {
            for (Class<?> curObjClass = srcObj.getClass(); curObjClass != null; curObjClass = curObjClass.getSuperclass()) {
                if (skipWellKnownClass) {
                    String className = curObjClass.getName();
                    if (className != null && (className.startsWith("android.view.") || className.startsWith("java."))) {
                        return;
                    }
                }
                for (Field field : curObjClass.getDeclaredFields()) {
                    String fieldType = field.getType().getName();
                    try {
                        boolean accessable = field.isAccessible();
                        field.setAccessible(true);
                        Object memberObj = field.get(srcObj);
                        field.setAccessible(accessable);
                        if (memberObj != null) {
                            boolean matched;
                            switch (matchType) {
                                case 1:
                                    matched = fieldType.endsWith("." + fieldObjectType);
                                    break;
                                default:
                                    matched = fieldType.equals(fieldObjectType);
                                    break;
                            }
                            if (matched) {
                                boolean haveSameObject = false;
                                Iterator i$ = searchResult.iterator();
                                while (i$.hasNext()) {
                                    if (i$.next() == memberObj) {
                                        haveSameObject = true;
                                        if (!haveSameObject) {
                                            searchResult.add(memberObj);
                                        }
                                    }
                                }
                                if (haveSameObject) {
                                    searchResult.add(memberObj);
                                }
                            } else {
                                getFieldObjectByObjectType(memberObj, matchType, fieldObjectType, maxSearchResultCount, searchResult, curDepth + 1, maxDepth, skipWellKnownClass);
                            }
                            if (maxSearchResultCount > 0 && searchResult.size() >= maxSearchResultCount) {
                                return;
                            }
                        }
                        continue;
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e2) {
                    }
                }
            }
        }
    }

    public static Object[] getFieldObjectByObjectType(Object srcObj, int matchType, String fieldObjectType, int maxSearchResultCount, boolean skipWellKnownClass) {
        return getFieldObjectByObjectType(srcObj, matchType, fieldObjectType, maxSearchResultCount, 1, skipWellKnownClass);
    }

    public static Object[] getFieldObjectByObjectType(Object srcObj, int matchType, String fieldObjectType, int maxSearchResultCount, int maxDepth, boolean skipWellKnownClass) {
        ArrayList<Object> searchResult = new ArrayList();
        if (srcObj == null || fieldObjectType == null) {
            return searchResult.toArray();
        }
        getFieldObjectByObjectType(srcObj, matchType, fieldObjectType, maxSearchResultCount, searchResult, 0, maxDepth, skipWellKnownClass);
        return searchResult.toArray();
    }

    public static Object getFieldObjectByFieldName(Object srcObj, String fieldName) {
        if (srcObj == null || fieldName == null) {
            return null;
        }
        for (Class<?> curObjClass = srcObj.getClass(); curObjClass != null; curObjClass = curObjClass.getSuperclass()) {
            Field[] arr$ = curObjClass.getDeclaredFields();
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                Field field = arr$[i$];
                if (fieldName.equals(field.getName())) {
                    try {
                        boolean accessable = field.isAccessible();
                        field.setAccessible(true);
                        Object fieldObject = field.get(srcObj);
                        field.setAccessible(accessable);
                        return fieldObject;
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e2) {
                    }
                } else {
                    i$++;
                }
            }
        }
        return null;
    }
}
