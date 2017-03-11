package com.samsung.android.spayfw.payprovider.plcc.util;

import android.text.TextUtils;
import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.appinterface.MstPayConfig;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntry;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntryItem;
import com.samsung.android.spayfw.payprovider.plcc.exception.PlccException;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SequenceUtils {
    public static MstPayConfig buildSequenceFromString(String str) {
        int i = 0;
        List parseBrackets = parseBrackets(str);
        MstPayConfig mstPayConfig = new MstPayConfig();
        List arrayList = new ArrayList();
        if (parseBrackets.size() == 1) {
            List arrayList2 = new ArrayList();
            String str2 = (String) parseBrackets.get(0);
            MstPayConfigEntry mstPayConfigEntry = new MstPayConfigEntry();
            mstPayConfigEntry.setBaudRate(getbaudRate(str2));
            mstPayConfigEntry.setDelayBetweenRepeat(getDelay(str2));
            arrayList2.add(buildSegment(str2));
            mstPayConfigEntry.setMstPayConfigEntry(arrayList2);
            arrayList.add(mstPayConfigEntry);
        } else if (parseBrackets.size() > 1) {
            int i2 = 0;
            while (i <= parseBrackets.size() - 1) {
                int i3;
                if (i == parseBrackets.size() - 1) {
                    i3 = i + 1;
                } else {
                    i3 = i;
                    while (i3 < parseBrackets.size()) {
                        if (isLumpEnd((String) parseBrackets.get(i3))) {
                            i3++;
                            break;
                        }
                        i3++;
                    }
                }
                MstPayConfigEntry mstPayConfigEntry2 = new MstPayConfigEntry();
                mstPayConfigEntry2.setBaudRate(getbaudRate((String) parseBrackets.get(i3 - 1)));
                mstPayConfigEntry2.setDelayBetweenRepeat(getDelay((String) parseBrackets.get(i3 - 1)));
                List arrayList3 = new ArrayList();
                for (i2 = 
                /* Method generation error in method: com.samsung.android.spayfw.payprovider.plcc.util.SequenceUtils.buildSequenceFromString(java.lang.String):com.samsung.android.spayfw.appinterface.MstPayConfig
jadx.core.utils.exceptions.CodegenException: Error generate insn: PHI: (r2_3 'i2' int) = (r2_2 'i2' int), (r2_6 'i2' int) binds: {(r2_2 'i2' int)=B:7:0x004b, (r2_6 'i2' int)=B:22:0x00b0} in method: com.samsung.android.spayfw.payprovider.plcc.util.SequenceUtils.buildSequenceFromString(java.lang.String):com.samsung.android.spayfw.appinterface.MstPayConfig
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:225)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:184)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:217)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:61)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:93)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:118)
	at jadx.core.codegen.RegionGen.connectElseIf(RegionGen.java:146)
	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:124)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:57)
	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:87)
	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:53)
	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:177)
	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:324)
	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:263)
	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:226)
	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:116)
	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:81)
	at jadx.core.codegen.CodeGen.visit(CodeGen.java:19)
	at jadx.core.ProcessClass.process(ProcessClass.java:43)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.CodegenException: Unknown instruction: PHI in method: com.samsung.android.spayfw.payprovider.plcc.util.SequenceUtils.buildSequenceFromString(java.lang.String):com.samsung.android.spayfw.appinterface.MstPayConfig
	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:512)
	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:219)
	... 27 more
 */

                private static List<String> parseBrackets(String str) {
                    List arrayList = new ArrayList();
                    if (str != null) {
                        Matcher matcher = Pattern.compile("\\((.*?)\\)").matcher(str);
                        while (matcher.find()) {
                            arrayList.add(matcher.group());
                        }
                    }
                    return arrayList;
                }

                private static MstPayConfigEntryItem buildSegment(String str) {
                    int i;
                    int i2 = 1;
                    if (str.indexOf("R") != -1) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    String toLowerCase = str.toLowerCase();
                    if (toLowerCase.indexOf("t") != -1) {
                        int extractNumber = extractNumber(toLowerCase.substring(toLowerCase.indexOf("t") + 1));
                        if (extractNumber < 1 || extractNumber > 2) {
                            throw new PlccException("Invalid Sequence Formattrack must be 1 or 2");
                        } else if (toLowerCase.indexOf("lz") != -1) {
                            int extractNumber2 = extractNumber(toLowerCase.substring(toLowerCase.indexOf("lz") + 2));
                            if (extractNumber2 < 0 || extractNumber2 > LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                                throw new PlccException("Invalid Sequence Format Leading Zeros should be between [0,1000]");
                            } else if (toLowerCase.indexOf("tz") != -1) {
                                int extractNumber3 = extractNumber(toLowerCase.substring(toLowerCase.indexOf("tz") + 2));
                                if (extractNumber3 < 0 || extractNumber3 > LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                                    throw new PlccException("Invalid Sequence Format Trailing Zeros should be between [0,1000]");
                                }
                                MstPayConfigEntryItem mstPayConfigEntryItem = new MstPayConfigEntryItem();
                                mstPayConfigEntryItem.setTrackIndex(extractNumber);
                                mstPayConfigEntryItem.setLeadingZeros(extractNumber2);
                                mstPayConfigEntryItem.setTrailingZeros(extractNumber3);
                                if (i == 0) {
                                    i2 = 0;
                                }
                                mstPayConfigEntryItem.setDirection(i2);
                                return mstPayConfigEntryItem;
                            } else {
                                throw new PlccException("Invalid Sequence Format No Trailing Zeros");
                            }
                        } else {
                            throw new PlccException("Invalid Sequence Format No Leading Zeros");
                        }
                    }
                    throw new PlccException("Invalid Sequence FormatNo Track Index");
                }

                private static boolean isLumpEnd(String str) {
                    if (str == null) {
                        return false;
                    }
                    String toLowerCase = str.toLowerCase();
                    int indexOf = toLowerCase.indexOf("d");
                    if (indexOf == -1 || extractNumber(toLowerCase.substring(indexOf + 1)) == 0) {
                        return false;
                    }
                    return true;
                }

                private static int getbaudRate(String str) {
                    int indexOf = str.indexOf("r");
                    if (indexOf != -1) {
                        indexOf = extractNumber(str.substring(indexOf + 1));
                        if (indexOf >= 20 && indexOf <= 2000) {
                            return indexOf;
                        }
                        throw new PlccException("Invalid Sequence Format: Baud rate should be between [20,2000]");
                    }
                    throw new PlccException("Invalid Sequence Format No baud rate found for lump");
                }

                private static int getDelay(String str) {
                    String toLowerCase = str.toLowerCase();
                    int extractNumber = extractNumber(toLowerCase.substring(toLowerCase.indexOf("d") + 1));
                    if (extractNumber >= 0 && extractNumber <= 5000) {
                        return extractNumber;
                    }
                    throw new PlccException("Invalid Sequence Format Delay should be between [0,5000]");
                }

                private static int extractNumber(String str) {
                    String str2 = BuildConfig.FLAVOR;
                    if (str == null) {
                        return 0;
                    }
                    String str3 = str2;
                    int i = 0;
                    while (i <= str.length() - 1 && TextUtils.isDigitsOnly(String.valueOf(str.charAt(i)))) {
                        str3 = str3 + String.valueOf(str.charAt(i));
                        i++;
                    }
                    if (str3.equals(BuildConfig.FLAVOR)) {
                        return 0;
                    }
                    return Integer.valueOf(str3).intValue();
                }
            }
