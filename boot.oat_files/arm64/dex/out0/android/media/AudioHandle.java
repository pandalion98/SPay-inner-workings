package android.media;

class AudioHandle {
    private final int mId;

    AudioHandle(int id) {
        this.mId = id;
    }

    int id() {
        return this.mId;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof AudioHandle)) {
            return false;
        }
        if (this.mId == ((AudioHandle) o).id()) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.mId;
    }

    public String toString() {
        return Integer.toString(this.mId);
    }
}
