package android.app;

public class Hop {
    private static final int BARWIDTH_MAX = 255;
    private static final int BARWIDTH_MIN = 0;
    private static final int INTER_PACKET_DELAY_MAX = 65535;
    private static final int INTER_PACKET_DELAY_MIN = 1;
    private static final int INTER_SYM_DELAY_MAX = 65535;
    private static final int INTER_SYM_DELAY_MIN = 1;
    private static final int PACKET_COUNT_MAX = 255;
    private static final int PACKET_COUNT_MIN = 1;
    private static final int SYMBOL_COUNT_MAX = 255;
    private static final int SYMBOL_COUNT_MIN = 1;
    public int barWidth;
    public int interPacketDelay;
    public int interSymbolDelay;
    public int packetCnt;
    public int symbolCnt;

    public Hop(String line) {
        String[] ss = line.split(",");
        this.barWidth = Integer.parseInt(ss[0]);
        this.symbolCnt = Integer.parseInt(ss[1]);
        this.interSymbolDelay = Integer.parseInt(ss[2]);
        this.packetCnt = Integer.parseInt(ss[3]);
        this.interPacketDelay = Integer.parseInt(ss[4]);
        if (this.barWidth < 0) {
            this.barWidth = 0;
        } else if (this.barWidth > 255) {
            this.barWidth = 255;
        }
        if (this.symbolCnt < 1) {
            this.symbolCnt = 1;
        } else if (this.symbolCnt > 255) {
            this.symbolCnt = 255;
        }
        if (this.interSymbolDelay < 1) {
            this.interSymbolDelay = 1;
        } else if (this.interSymbolDelay > 65535) {
            this.interSymbolDelay = 65535;
        }
        if (this.packetCnt < 1) {
            this.packetCnt = 1;
        } else if (this.packetCnt > 255) {
            this.packetCnt = 255;
        }
        if (this.interPacketDelay < 1) {
            this.interPacketDelay = 1;
        } else if (this.interPacketDelay > 65535) {
            this.interPacketDelay = 65535;
        }
    }

    public Hop(int barWidth, int symbolCnt, int interSymbolDelay, int packetCnt, int interPacketDelay) {
        if (barWidth < 0) {
            barWidth = 0;
        } else if (barWidth > 255) {
            barWidth = 255;
        }
        if (symbolCnt < 1) {
            symbolCnt = 1;
        } else if (symbolCnt > 255) {
            symbolCnt = 255;
        }
        if (interSymbolDelay < 1) {
            interSymbolDelay = 1;
        } else if (interSymbolDelay > 65535) {
            interSymbolDelay = 65535;
        }
        if (packetCnt < 1) {
            packetCnt = 1;
        } else if (packetCnt > 255) {
            packetCnt = 255;
        }
        if (interPacketDelay < 1) {
            interPacketDelay = 1;
        } else if (interPacketDelay > 65535) {
            interPacketDelay = 65535;
        }
        this.barWidth = barWidth;
        this.symbolCnt = symbolCnt;
        this.interSymbolDelay = interSymbolDelay;
        this.packetCnt = packetCnt;
        this.interPacketDelay = interPacketDelay;
    }

    public String toString() {
        return this.barWidth + "," + this.symbolCnt + "," + this.interSymbolDelay + "," + this.packetCnt + "," + this.interPacketDelay;
    }
}
