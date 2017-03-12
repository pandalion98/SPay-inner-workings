package android.graphics;

public class Atlas {
    public static final int FLAG_ADD_PADDING = 2;
    public static final int FLAG_DEFAULTS = 2;
    private final Policy mPolicy;

    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$graphics$Atlas$Type = new int[Type.values().length];

        static {
            try {
                $SwitchMap$android$graphics$Atlas$Type[Type.SliceMinArea.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$graphics$Atlas$Type[Type.SliceMaxArea.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$graphics$Atlas$Type[Type.SliceShortAxis.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$graphics$Atlas$Type[Type.SliceLongAxis.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public static class Entry {
        public int x;
        public int y;
    }

    private static abstract class Policy {
        abstract Entry pack(int i, int i2, Entry entry);

        private Policy() {
        }
    }

    private static class SlicePolicy extends Policy {
        private final int mPadding;
        private final Cell mRoot = new Cell();
        private final SplitDecision mSplitDecision;

        private static class Cell {
            int height;
            Cell next;
            int width;
            int x;
            int y;

            private Cell() {
            }

            public String toString() {
                return String.format("cell[x=%d y=%d width=%d height=%d", new Object[]{Integer.valueOf(this.x), Integer.valueOf(this.y), Integer.valueOf(this.width), Integer.valueOf(this.height)});
            }
        }

        private interface SplitDecision {
            boolean splitHorizontal(int i, int i2, int i3, int i4);
        }

        private static class LongerFreeAxisSplitDecision implements SplitDecision {
            private LongerFreeAxisSplitDecision() {
            }

            public boolean splitHorizontal(int freeWidth, int freeHeight, int rectWidth, int rectHeight) {
                return freeWidth > freeHeight;
            }
        }

        private static class MaxAreaSplitDecision implements SplitDecision {
            private MaxAreaSplitDecision() {
            }

            public boolean splitHorizontal(int freeWidth, int freeHeight, int rectWidth, int rectHeight) {
                return rectWidth * freeHeight <= freeWidth * rectHeight;
            }
        }

        private static class MinAreaSplitDecision implements SplitDecision {
            private MinAreaSplitDecision() {
            }

            public boolean splitHorizontal(int freeWidth, int freeHeight, int rectWidth, int rectHeight) {
                return rectWidth * freeHeight > freeWidth * rectHeight;
            }
        }

        private static class ShorterFreeAxisSplitDecision implements SplitDecision {
            private ShorterFreeAxisSplitDecision() {
            }

            public boolean splitHorizontal(int freeWidth, int freeHeight, int rectWidth, int rectHeight) {
                return freeWidth <= freeHeight;
            }
        }

        SlicePolicy(int width, int height, int flags, SplitDecision splitDecision) {
            super();
            this.mPadding = (flags & 2) != 0 ? 1 : 0;
            Cell first = new Cell();
            int i = this.mPadding;
            first.y = i;
            first.x = i;
            first.width = width - (this.mPadding * 2);
            first.height = height - (this.mPadding * 2);
            this.mRoot.next = first;
            this.mSplitDecision = splitDecision;
        }

        Entry pack(int width, int height, Entry entry) {
            Cell prev = this.mRoot;
            for (Cell cell = this.mRoot.next; cell != null; cell = cell.next) {
                if (insert(cell, prev, width, height, entry)) {
                    return entry;
                }
                prev = cell;
            }
            return null;
        }

        private boolean insert(Cell cell, Cell prev, int width, int height, Entry entry) {
            if (cell.width < width || cell.height < height) {
                return false;
            }
            int deltaWidth = cell.width - width;
            int deltaHeight = cell.height - height;
            Cell first = new Cell();
            Cell second = new Cell();
            first.x = (cell.x + width) + this.mPadding;
            first.y = cell.y;
            first.width = deltaWidth - this.mPadding;
            second.x = cell.x;
            second.y = (cell.y + height) + this.mPadding;
            second.height = deltaHeight - this.mPadding;
            if (this.mSplitDecision.splitHorizontal(deltaWidth, deltaHeight, width, height)) {
                first.height = height;
                second.width = cell.width;
            } else {
                first.height = cell.height;
                second.width = width;
                Cell temp = first;
                first = second;
                second = temp;
            }
            if (first.width > 0 && first.height > 0) {
                prev.next = first;
                prev = first;
            }
            if (second.width <= 0 || second.height <= 0) {
                prev.next = cell.next;
            } else {
                prev.next = second;
                second.next = cell.next;
            }
            cell.next = null;
            entry.x = cell.x;
            entry.y = cell.y;
            return true;
        }
    }

    public enum Type {
        SliceMinArea,
        SliceMaxArea,
        SliceShortAxis,
        SliceLongAxis
    }

    public Atlas(Type type, int width, int height) {
        this(type, width, height, 2);
    }

    public Atlas(Type type, int width, int height, int flags) {
        this.mPolicy = findPolicy(type, width, height, flags);
    }

    public Entry pack(int width, int height) {
        return pack(width, height, null);
    }

    public Entry pack(int width, int height, Entry entry) {
        if (entry == null) {
            entry = new Entry();
        }
        return this.mPolicy.pack(width, height, entry);
    }

    private static Policy findPolicy(Type type, int width, int height, int flags) {
        switch (AnonymousClass1.$SwitchMap$android$graphics$Atlas$Type[type.ordinal()]) {
            case 1:
                return new SlicePolicy(width, height, flags, new MinAreaSplitDecision());
            case 2:
                return new SlicePolicy(width, height, flags, new MaxAreaSplitDecision());
            case 3:
                return new SlicePolicy(width, height, flags, new ShorterFreeAxisSplitDecision());
            case 4:
                return new SlicePolicy(width, height, flags, new LongerFreeAxisSplitDecision());
            default:
                return null;
        }
    }
}
