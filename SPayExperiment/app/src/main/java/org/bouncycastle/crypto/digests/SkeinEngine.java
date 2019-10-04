/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Enumeration
 *  java.util.Hashtable
 *  java.util.Vector
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.digests;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.ThreefishEngine;
import org.bouncycastle.crypto.params.SkeinParameters;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Memoable;

public class SkeinEngine
implements Memoable {
    private static final Hashtable INITIAL_STATES = new Hashtable();
    private static final int PARAM_TYPE_CONFIG = 4;
    private static final int PARAM_TYPE_KEY = 0;
    private static final int PARAM_TYPE_MESSAGE = 48;
    private static final int PARAM_TYPE_OUTPUT = 63;
    public static final int SKEIN_1024 = 1024;
    public static final int SKEIN_256 = 256;
    public static final int SKEIN_512 = 512;
    long[] chain;
    private long[] initialState;
    private byte[] key;
    private final int outputSizeBytes;
    private Parameter[] postMessageParameters;
    private Parameter[] preMessageParameters;
    private final byte[] singleByte = new byte[1];
    final ThreefishEngine threefish;
    private final UBI ubi;

    static {
        SkeinEngine.initialState(256, 128, new long[]{-2228972824489528736L, -8629553674646093540L, 1155188648486244218L, -3677226592081559102L});
        SkeinEngine.initialState(256, 160, new long[]{1450197650740764312L, 3081844928540042640L, -3136097061834271170L, 3301952811952417661L});
        SkeinEngine.initialState(256, 224, new long[]{-4176654842910610933L, -8688192972455077604L, -7364642305011795836L, 4056579644589979102L});
        SkeinEngine.initialState(256, 256, new long[]{-243853671043386295L, 3443677322885453875L, -5531612722399640561L, 7662005193972177513L});
        SkeinEngine.initialState(512, 128, new long[]{-6288014694233956526L, 2204638249859346602L, 3502419045458743507L, -4829063503441264548L, 983504137758028059L, 1880512238245786339L, -6715892782214108542L, 7602827311880509485L});
        SkeinEngine.initialState(512, 160, new long[]{2934123928682216849L, -4399710721982728305L, 1684584802963255058L, 5744138295201861711L, 2444857010922934358L, -2807833639722848072L, -5121587834665610502L, 118355523173251694L});
        SkeinEngine.initialState(512, 224, new long[]{-3688341020067007964L, -3772225436291745297L, -8300862168937575580L, 4146387520469897396L, 1106145742801415120L, 7455425944880474941L, -7351063101234211863L, -7048981346965512457L});
        SkeinEngine.initialState(512, 384, new long[]{-6631894876634615969L, -5692838220127733084L, -7099962856338682626L, -2911352911530754598L, 2000907093792408677L, 9140007292425499655L, 6093301768906360022L, 2769176472213098488L});
        SkeinEngine.initialState(512, 512, new long[]{5261240102383538638L, 978932832955457283L, -8083517948103779378L, -7339365279355032399L, 6752626034097301424L, -1531723821829733388L, -7417126464950782685L, -5901786942805128141L});
    }

    public SkeinEngine(int n2, int n3) {
        if (n3 % 8 != 0) {
            throw new IllegalArgumentException("Output size must be a multiple of 8 bits. :" + n3);
        }
        this.outputSizeBytes = n3 / 8;
        this.threefish = new ThreefishEngine(n2);
        this.ubi = new UBI(this.threefish.getBlockSize());
    }

    public SkeinEngine(SkeinEngine skeinEngine) {
        this(8 * skeinEngine.getBlockSize(), 8 * skeinEngine.getOutputSize());
        this.copyIn(skeinEngine);
    }

    private void checkInitialised() {
        if (this.ubi == null) {
            throw new IllegalArgumentException("Skein engine is not initialised.");
        }
    }

    private static Parameter[] clone(Parameter[] arrparameter, Parameter[] arrparameter2) {
        if (arrparameter == null) {
            return null;
        }
        if (arrparameter2 == null || arrparameter2.length != arrparameter.length) {
            arrparameter2 = new Parameter[arrparameter.length];
        }
        System.arraycopy((Object)arrparameter, (int)0, (Object)arrparameter2, (int)0, (int)arrparameter2.length);
        return arrparameter2;
    }

    private void copyIn(SkeinEngine skeinEngine) {
        this.ubi.reset(skeinEngine.ubi);
        this.chain = Arrays.clone((long[])skeinEngine.chain, (long[])this.chain);
        this.initialState = Arrays.clone((long[])skeinEngine.initialState, (long[])this.initialState);
        this.key = Arrays.clone((byte[])skeinEngine.key, (byte[])this.key);
        this.preMessageParameters = SkeinEngine.clone(skeinEngine.preMessageParameters, this.preMessageParameters);
        this.postMessageParameters = SkeinEngine.clone(skeinEngine.postMessageParameters, this.postMessageParameters);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void createInitialState() {
        long[] arrl = (long[])INITIAL_STATES.get((Object)SkeinEngine.variantIdentifier(this.getBlockSize(), this.getOutputSize()));
        if (this.key == null && arrl != null) {
            this.chain = Arrays.clone((long[])arrl);
        } else {
            this.chain = new long[this.getBlockSize() / 8];
            if (this.key != null) {
                this.ubiComplete(0, this.key);
            }
            this.ubiComplete(4, new Configuration(8 * this.outputSizeBytes).getBytes());
        }
        if (this.preMessageParameters != null) {
            for (int i2 = 0; i2 < this.preMessageParameters.length; ++i2) {
                Parameter parameter = this.preMessageParameters[i2];
                this.ubiComplete(parameter.getType(), parameter.getValue());
            }
        }
        this.initialState = Arrays.clone((long[])this.chain);
    }

    private void initParams(Hashtable hashtable) {
        Enumeration enumeration = hashtable.keys();
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        while (enumeration.hasMoreElements()) {
            Integer n2 = (Integer)enumeration.nextElement();
            byte[] arrby = (byte[])hashtable.get((Object)n2);
            if (n2 == 0) {
                this.key = arrby;
                continue;
            }
            if (n2 < 48) {
                vector.addElement((Object)new Parameter(n2, arrby));
                continue;
            }
            vector2.addElement((Object)new Parameter(n2, arrby));
        }
        this.preMessageParameters = new Parameter[vector.size()];
        vector.copyInto((Object[])this.preMessageParameters);
        SkeinEngine.sort(this.preMessageParameters);
        this.postMessageParameters = new Parameter[vector2.size()];
        vector2.copyInto((Object[])this.postMessageParameters);
        SkeinEngine.sort(this.postMessageParameters);
    }

    private static void initialState(int n2, int n3, long[] arrl) {
        INITIAL_STATES.put((Object)SkeinEngine.variantIdentifier(n2 / 8, n3 / 8), (Object)arrl);
    }

    /*
     * Enabled aggressive block sorting
     */
    private void output(long l2, byte[] arrby, int n2, int n3) {
        byte[] arrby2 = new byte[8];
        ThreefishEngine.wordToBytes(l2, arrby2, 0);
        long[] arrl = new long[this.chain.length];
        this.ubiInit(63);
        this.ubi.update(arrby2, 0, arrby2.length, arrl);
        this.ubi.doFinal(arrl);
        int n4 = (-1 + (n3 + 8)) / 8;
        int n5 = 0;
        while (n5 < n4) {
            int n6 = Math.min((int)8, (int)(n3 - n5 * 8));
            if (n6 == 8) {
                ThreefishEngine.wordToBytes(arrl[n5], arrby, n2 + n5 * 8);
            } else {
                ThreefishEngine.wordToBytes(arrl[n5], arrby2, 0);
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)(n2 + n5 * 8), (int)n6);
            }
            ++n5;
        }
        return;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static void sort(Parameter[] arrparameter) {
        if (arrparameter != null) {
            for (int i2 = 1; i2 < arrparameter.length; ++i2) {
                Parameter parameter = arrparameter[i2];
                for (int i3 = i2; i3 > 0 && parameter.getType() < arrparameter[i3 - 1].getType(); --i3) {
                    arrparameter[i3] = arrparameter[i3 - 1];
                }
                arrparameter[i3] = parameter;
            }
        }
    }

    private void ubiComplete(int n2, byte[] arrby) {
        this.ubiInit(n2);
        this.ubi.update(arrby, 0, arrby.length, this.chain);
        this.ubiFinal();
    }

    private void ubiFinal() {
        this.ubi.doFinal(this.chain);
    }

    private void ubiInit(int n2) {
        this.ubi.reset(n2);
    }

    private static Integer variantIdentifier(int n2, int n3) {
        return new Integer(n2 | n3 << 16);
    }

    @Override
    public Memoable copy() {
        return new SkeinEngine(this);
    }

    public int doFinal(byte[] arrby, int n2) {
        this.checkInitialised();
        if (arrby.length < n2 + this.outputSizeBytes) {
            throw new DataLengthException("Output buffer is too short to hold output of " + this.outputSizeBytes + " bytes");
        }
        this.ubiFinal();
        if (this.postMessageParameters != null) {
            for (int i2 = 0; i2 < this.postMessageParameters.length; ++i2) {
                Parameter parameter = this.postMessageParameters[i2];
                this.ubiComplete(parameter.getType(), parameter.getValue());
            }
        }
        int n3 = this.getBlockSize();
        int n4 = (-1 + (n3 + this.outputSizeBytes)) / n3;
        for (int i3 = 0; i3 < n4; ++i3) {
            int n5 = Math.min((int)n3, (int)(this.outputSizeBytes - i3 * n3));
            this.output(i3, arrby, n2 + i3 * n3, n5);
        }
        this.reset();
        return this.outputSizeBytes;
    }

    public int getBlockSize() {
        return this.threefish.getBlockSize();
    }

    public int getOutputSize() {
        return this.outputSizeBytes;
    }

    public void init(SkeinParameters skeinParameters) {
        this.chain = null;
        this.key = null;
        this.preMessageParameters = null;
        this.postMessageParameters = null;
        if (skeinParameters != null) {
            if (skeinParameters.getKey().length < 16) {
                throw new IllegalArgumentException("Skein key must be at least 128 bits.");
            }
            this.initParams(skeinParameters.getParameters());
        }
        this.createInitialState();
        this.ubiInit(48);
    }

    public void reset() {
        System.arraycopy((Object)this.initialState, (int)0, (Object)this.chain, (int)0, (int)this.chain.length);
        this.ubiInit(48);
    }

    @Override
    public void reset(Memoable memoable) {
        SkeinEngine skeinEngine = (SkeinEngine)memoable;
        if (this.getBlockSize() != skeinEngine.getBlockSize() || this.outputSizeBytes != skeinEngine.outputSizeBytes) {
            throw new IllegalArgumentException("Incompatible parameters in provided SkeinEngine.");
        }
        this.copyIn(skeinEngine);
    }

    public void update(byte by) {
        this.singleByte[0] = by;
        this.update(this.singleByte, 0, 1);
    }

    public void update(byte[] arrby, int n2, int n3) {
        this.checkInitialised();
        this.ubi.update(arrby, n2, n3, this.chain);
    }

    private static class Configuration {
        private byte[] bytes = new byte[32];

        public Configuration(long l2) {
            this.bytes[0] = 83;
            this.bytes[1] = 72;
            this.bytes[2] = 65;
            this.bytes[3] = 51;
            this.bytes[4] = 1;
            this.bytes[5] = 0;
            ThreefishEngine.wordToBytes(l2, this.bytes, 8);
        }

        public byte[] getBytes() {
            return this.bytes;
        }
    }

    public static class Parameter {
        private int type;
        private byte[] value;

        public Parameter(int n2, byte[] arrby) {
            this.type = n2;
            this.value = arrby;
        }

        public int getType() {
            return this.type;
        }

        public byte[] getValue() {
            return this.value;
        }
    }

    private class UBI {
        private byte[] currentBlock;
        private int currentOffset;
        private long[] message;
        private final UbiTweak tweak = new UbiTweak();

        public UBI(int n2) {
            this.currentBlock = new byte[n2];
            this.message = new long[this.currentBlock.length / 8];
        }

        private void processBlock(long[] arrl) {
            int n2 = 0;
            SkeinEngine.this.threefish.init(true, SkeinEngine.this.chain, this.tweak.getWords());
            for (int i2 = 0; i2 < this.message.length; ++i2) {
                this.message[i2] = ThreefishEngine.bytesToWord(this.currentBlock, i2 * 8);
            }
            SkeinEngine.this.threefish.processBlock(this.message, arrl);
            while (n2 < arrl.length) {
                arrl[n2] = arrl[n2] ^ this.message[n2];
                ++n2;
            }
        }

        public void doFinal(long[] arrl) {
            for (int i2 = this.currentOffset; i2 < this.currentBlock.length; ++i2) {
                this.currentBlock[i2] = 0;
            }
            this.tweak.setFinal(true);
            this.processBlock(arrl);
        }

        public void reset(int n2) {
            this.tweak.reset();
            this.tweak.setType(n2);
            this.currentOffset = 0;
        }

        public void reset(UBI uBI) {
            this.currentBlock = Arrays.clone((byte[])uBI.currentBlock, (byte[])this.currentBlock);
            this.currentOffset = uBI.currentOffset;
            this.message = Arrays.clone((long[])uBI.message, (long[])this.message);
            this.tweak.reset(uBI.tweak);
        }

        public void update(byte[] arrby, int n2, int n3, long[] arrl) {
            int n4;
            for (int i2 = 0; n3 > i2; i2 += n4) {
                if (this.currentOffset == this.currentBlock.length) {
                    this.processBlock(arrl);
                    this.tweak.setFirst(false);
                    this.currentOffset = 0;
                }
                n4 = Math.min((int)(n3 - i2), (int)(this.currentBlock.length - this.currentOffset));
                System.arraycopy((Object)arrby, (int)(n2 + i2), (Object)this.currentBlock, (int)this.currentOffset, (int)n4);
                this.currentOffset = n4 + this.currentOffset;
                this.tweak.advancePosition(n4);
            }
        }
    }

    private static class UbiTweak {
        private static final long LOW_RANGE = 9223372034707292160L;
        private static final long T1_FINAL = Long.MIN_VALUE;
        private static final long T1_FIRST = 0x4000000000000000L;
        private boolean extendedPosition;
        private long[] tweak = new long[2];

        public UbiTweak() {
            this.reset();
        }

        /*
         * Enabled aggressive block sorting
         */
        public void advancePosition(int n2) {
            if (this.extendedPosition) {
                long[] arrl = new long[]{0xFFFFFFFFL & this.tweak[0], 0xFFFFFFFFL & this.tweak[0] >>> 32, 0xFFFFFFFFL & this.tweak[1]};
                long l2 = n2;
                for (int i2 = 0; i2 < arrl.length; ++i2) {
                    long l3;
                    arrl[i2] = l3 = l2 + arrl[i2];
                    l2 = l3 >>> 32;
                }
                this.tweak[0] = (0xFFFFFFFFL & arrl[1]) << 32 | 0xFFFFFFFFL & arrl[0];
                this.tweak[1] = -4294967296L & this.tweak[1] | 0xFFFFFFFFL & arrl[2];
                return;
            } else {
                long l4;
                this.tweak[0] = l4 = this.tweak[0] + (long)n2;
                if (l4 <= 9223372034707292160L) return;
                {
                    this.extendedPosition = true;
                    return;
                }
            }
        }

        public int getType() {
            return (int)(63L & this.tweak[1] >>> 56);
        }

        public long[] getWords() {
            return this.tweak;
        }

        public boolean isFinal() {
            return (Long.MIN_VALUE & this.tweak[1]) != 0L;
        }

        public boolean isFirst() {
            return (0x4000000000000000L & this.tweak[1]) != 0L;
        }

        public void reset() {
            this.tweak[0] = 0L;
            this.tweak[1] = 0L;
            this.extendedPosition = false;
            this.setFirst(true);
        }

        public void reset(UbiTweak ubiTweak) {
            this.tweak = Arrays.clone((long[])ubiTweak.tweak, (long[])this.tweak);
            this.extendedPosition = ubiTweak.extendedPosition;
        }

        public void setFinal(boolean bl) {
            if (bl) {
                long[] arrl = this.tweak;
                arrl[1] = Long.MIN_VALUE | arrl[1];
                return;
            }
            long[] arrl = this.tweak;
            arrl[1] = Long.MAX_VALUE & arrl[1];
        }

        public void setFirst(boolean bl) {
            if (bl) {
                long[] arrl = this.tweak;
                arrl[1] = 0x4000000000000000L | arrl[1];
                return;
            }
            long[] arrl = this.tweak;
            arrl[1] = -4611686018427387905L & arrl[1];
        }

        public void setType(int n2) {
            this.tweak[1] = -274877906944L & this.tweak[1] | (63L & (long)n2) << 56;
        }

        public String toString() {
            return this.getType() + " first: " + this.isFirst() + ", final: " + this.isFinal();
        }
    }

}

