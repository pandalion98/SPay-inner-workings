package android.location;

import android.util.SparseArray;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class GpsStatus {
    public static final int GPS_EVENT_FIRST_FIX = 3;
    public static final int GPS_EVENT_SATELLITE_STATUS = 4;
    public static final int GPS_EVENT_STARTED = 1;
    public static final int GPS_EVENT_STOPPED = 2;
    private static final int NUM_SATELLITES = 255;
    private Iterable<GpsSatellite> mSatelliteList = new Iterable<GpsSatellite>() {
        public Iterator<GpsSatellite> iterator() {
            return new SatelliteIterator(GpsStatus.this.mSatellites);
        }
    };
    private final SparseArray<GpsSatellite> mSatellites = new SparseArray();
    private int mTimeToFirstFix;

    public interface Listener {
        void onGpsStatusChanged(int i);
    }

    public interface NmeaListener {
        void onNmeaReceived(long j, String str);
    }

    private final class SatelliteIterator implements Iterator<GpsSatellite> {
        private int mIndex = 0;
        private final SparseArray<GpsSatellite> mSatellites;
        private final int mSatellitesCount;

        SatelliteIterator(SparseArray<GpsSatellite> satellites) {
            this.mSatellites = satellites;
            this.mSatellitesCount = satellites.size();
        }

        public boolean hasNext() {
            while (this.mIndex < this.mSatellitesCount) {
                if (((GpsSatellite) this.mSatellites.valueAt(this.mIndex)).mValid) {
                    return true;
                }
                this.mIndex++;
            }
            return false;
        }

        public GpsSatellite next() {
            while (this.mIndex < this.mSatellitesCount) {
                GpsSatellite satellite = (GpsSatellite) this.mSatellites.valueAt(this.mIndex);
                this.mIndex++;
                if (satellite.mValid) {
                    return satellite;
                }
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    GpsStatus() {
    }

    synchronized void setStatus(int svCount, int[] prns, float[] snrs, float[] elevations, float[] azimuths, int ephemerisMask, int almanacMask, int usedInFixMask, int[] used) {
        clearSatellites();
        for (int i = 0; i < svCount; i++) {
            int prn = prns[i];
            int prnShift = 1 << (prn - 1);
            if (prn > 0 && prn <= 255) {
                GpsSatellite satellite = (GpsSatellite) this.mSatellites.get(prn);
                if (satellite == null) {
                    satellite = new GpsSatellite(prn);
                    this.mSatellites.put(prn, satellite);
                }
                satellite.mValid = true;
                satellite.mSnr = snrs[i];
                satellite.mElevation = elevations[i];
                satellite.mAzimuth = azimuths[i];
                if (prns[i] <= 32) {
                    boolean z;
                    satellite.mHasEphemeris = (ephemerisMask & prnShift) != 0;
                    satellite.mHasAlmanac = (almanacMask & prnShift) != 0;
                    if ((usedInFixMask & prnShift) != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    satellite.mUsedInFix = z;
                } else if (used[i] == 1) {
                    satellite.mUsedInFix = true;
                }
            }
        }
    }

    void setStatus(GpsStatus status) {
        this.mTimeToFirstFix = status.getTimeToFirstFix();
        clearSatellites();
        SparseArray<GpsSatellite> otherSatellites = status.mSatellites;
        int otherSatellitesCount = otherSatellites.size();
        int satelliteIndex = 0;
        for (int i = 0; i < otherSatellitesCount; i++) {
            GpsSatellite otherSatellite = (GpsSatellite) otherSatellites.valueAt(i);
            int otherSatellitePrn = otherSatellite.getPrn();
            int satellitesCount = this.mSatellites.size();
            while (satelliteIndex < satellitesCount && ((GpsSatellite) this.mSatellites.valueAt(satelliteIndex)).getPrn() < otherSatellitePrn) {
                satelliteIndex++;
            }
            GpsSatellite satellite;
            if (satelliteIndex < this.mSatellites.size()) {
                satellite = (GpsSatellite) this.mSatellites.valueAt(satelliteIndex);
                if (satellite.getPrn() == otherSatellitePrn) {
                    satellite.setStatus(otherSatellite);
                } else {
                    satellite = new GpsSatellite(otherSatellitePrn);
                    satellite.setStatus(otherSatellite);
                    this.mSatellites.put(otherSatellitePrn, satellite);
                }
            } else {
                satellite = new GpsSatellite(otherSatellitePrn);
                satellite.setStatus(otherSatellite);
                this.mSatellites.append(otherSatellitePrn, satellite);
            }
        }
    }

    void setTimeToFirstFix(int ttff) {
        this.mTimeToFirstFix = ttff;
    }

    public int getTimeToFirstFix() {
        return this.mTimeToFirstFix;
    }

    public Iterable<GpsSatellite> getSatellites() {
        return this.mSatelliteList;
    }

    public int getMaxSatellites() {
        return 255;
    }

    private void clearSatellites() {
        int satellitesCount = this.mSatellites.size();
        for (int i = 0; i < satellitesCount; i++) {
            GpsSatellite satellite = (GpsSatellite) this.mSatellites.valueAt(i);
            satellite.mValid = false;
            satellite.mUsedInFix = false;
        }
    }
}
