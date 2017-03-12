package android.media;

public class AudioDevicePort extends AudioPort {
    private final String mAddress;
    private final int mType;

    AudioDevicePort(AudioHandle handle, String deviceName, int[] samplingRates, int[] channelMasks, int[] channelIndexMasks, int[] formats, AudioGain[] gains, int type, String address) {
        super(handle, AudioManager.isInputDevice(type) ? 1 : 2, deviceName, samplingRates, channelMasks, channelIndexMasks, formats, gains);
        this.mType = type;
        this.mAddress = address;
    }

    public int type() {
        return this.mType;
    }

    public String address() {
        return this.mAddress;
    }

    public AudioDevicePortConfig buildConfig(int samplingRate, int channelMask, int format, AudioGainConfig gain) {
        return new AudioDevicePortConfig(this, samplingRate, channelMask, format, gain);
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof AudioDevicePort)) {
            return false;
        }
        AudioDevicePort other = (AudioDevicePort) o;
        if (this.mType != other.type()) {
            return false;
        }
        if ((this.mAddress != null || other.address() == null) && this.mAddress.equals(other.address())) {
            return super.equals(o);
        }
        return false;
    }

    public String toString() {
        return "{" + super.toString() + ", mType: " + (this.mRole == 1 ? AudioSystem.getInputDeviceName(this.mType) : AudioSystem.getOutputDeviceName(this.mType)) + ", mAddress: " + this.mAddress + "}";
    }
}
