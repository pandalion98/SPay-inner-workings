/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.PrintStream
 *  java.io.PrintWriter
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.Iterator
 */
package com.samsung.android.spayfw.utils;

import com.samsung.android.spayfw.b.c;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

public class IOExceptionHandler {
    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public static <S, T extends a<S>> void a(T var0) {
        var1_1 = new ArrayList();
        var4_2 = var0.aJ();
        var0.f(var4_2);
        try {
            var0.e(var4_2);
            ** GOTO lbl12
        }
        catch (IOException var16_3) {
            var1_1.add((Object)var16_3);
lbl12: // 2 sources:
            try {
                var0.d(var4_2);
                ** GOTO lbl19
            }
            catch (IOException var18_5) {
                var1_1.add((Object)var18_5);
lbl19: // 3 sources:
                var0.c(var4_2);
                catch (IOException var10_6) {
                    var1_1.add((Object)var10_6);
                    var0.e(var4_2);
                    {
                        block23 : {
                            catch (IOException var12_7) {
                                var1_1.add((Object)var12_7);
                            }
                            var0.d(var4_2);
                            ** try [egrp 10[TRYBLOCK] [11 : 109->142)] { 
lbl32: // 1 sources:
                            break block23;
                            catch (IOException var14_8) {
                                var1_1.add((Object)var14_8);
                            }
                        }
                        var0.c(var4_2);
                    }
                    catch (Throwable var5_9) {
                        var0.e(var4_2);
                        {
                            block24 : {
                                catch (IOException var6_10) {
                                    var1_1.add((Object)var6_10);
                                }
                                var0.d(var4_2);
                                ** try [egrp 13[TRYBLOCK] [16 : 156->190)] { 
lbl47: // 1 sources:
                                break block24;
                                catch (IOException var8_11) {
                                    var1_1.add((Object)var8_11);
                                }
                            }
                            var0.c(var4_2);
                            throw var5_9;
                        }
lbl53: // 6 sources:
                        catch (IOException var2_4) {
                            var1_1.add((Object)var2_4);
                        }
                    }
                }
            }
        }
        if (var1_1.size() == 0) return;
        throw new ExceptionChain((ArrayList<Exception>)var1_1);
    }

    public static <S, T extends a<S>> void a(T t2, boolean bl) {
        try {
            IOExceptionHandler.a(t2);
            return;
        }
        catch (ExceptionChain exceptionChain) {
            if (bl) {
                c.c("IOProcessor", exceptionChain.getMessage(), (Throwable)((Object)exceptionChain));
                return;
            }
            throw exceptionChain;
        }
    }

    private static class ExceptionChain
    extends RuntimeException {
        private ArrayList<Exception> exceptions;

        /*
         * Enabled aggressive block sorting
         */
        public ExceptionChain(ArrayList<Exception> arrayList) {
            StringBuilder stringBuilder = new StringBuilder().append("Exception Chain contains ");
            String string = arrayList.size() > 1 ? arrayList.size() + " exceptions" : arrayList.size() + " exception";
            super(stringBuilder.append(string).toString(), (Throwable)((Object)arrayList.get(0)));
            this.exceptions = arrayList;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void printStackTrace(PrintStream printStream) {
            PrintStream printStream2 = printStream;
            synchronized (printStream2) {
                super.printStackTrace(printStream);
                Iterator iterator = this.exceptions.iterator();
                int n2 = 0;
                while (iterator.hasNext()) {
                    Exception exception = (Exception)((Object)iterator.next());
                    StringBuilder stringBuilder = new StringBuilder().append("Exception ");
                    printStream.println(stringBuilder.append(++n2).append(" :").toString());
                    exception.printStackTrace(printStream);
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void printStackTrace(PrintWriter printWriter) {
            PrintWriter printWriter2 = printWriter;
            synchronized (printWriter2) {
                super.printStackTrace(printWriter);
                Iterator iterator = this.exceptions.iterator();
                int n2 = 0;
                while (iterator.hasNext()) {
                    Exception exception = (Exception)((Object)iterator.next());
                    StringBuilder stringBuilder = new StringBuilder().append("Exception ");
                    printWriter.println(stringBuilder.append(++n2).append(" :").toString());
                    exception.printStackTrace(printWriter);
                }
                return;
            }
        }
    }

    public static abstract class a<T> {
        public abstract T aJ();

        public abstract void c(T var1);

        public void d(T t2) {
        }

        public void e(T t2) {
        }

        public abstract void f(T var1);
    }

}

