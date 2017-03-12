package com.samsung.android.contextaware.aggregator.lpp.algorithm;

public class CoordinateTransform {
    public static double[] llh2xyz(double[] llh) {
        double[] xyz = new double[3];
        double phi = llh[0];
        double lambda = llh[1];
        double h = llh[2];
        double e = Math.sqrt(1.0d - ((6356752.3142d / 6378137.0d) * (6356752.3142d / 6378137.0d)));
        double sinphi = Math.sin(phi);
        double cosphi = Math.cos(phi);
        double coslam = Math.cos(lambda);
        double sinlam = Math.sin(lambda);
        double tmp = 1.0d - (e * e);
        double tmpden = Math.sqrt(1.0d + (tmp * (Math.tan(phi) * Math.tan(phi))));
        double x = ((6378137.0d * coslam) / tmpden) + ((h * coslam) * cosphi);
        double y = ((6378137.0d * sinlam) / tmpden) + ((h * sinlam) * cosphi);
        double z = (((6378137.0d * tmp) * sinphi) / Math.sqrt(1.0d - (((e * e) * sinphi) * sinphi))) + (h * sinphi);
        xyz[0] = x;
        xyz[1] = y;
        xyz[2] = z;
        return xyz;
    }

    public static double[] llh2enu(double[] llh, double[] llhorg) {
        double[] xyz = new double[3];
        double[] xyzorg = new double[3];
        double[] xyz2 = llh2xyz(llh);
        double[] xyzorg2 = llh2xyz(llhorg);
        for (int i = 0; i < 3; i++) {
            xyz[i] = xyz2[i];
            xyzorg[i] = xyzorg2[i];
        }
        return xyz2enu(xyz, xyzorg);
    }

    public static double[] xyz2enu(double[] xyz, double[] xyzorg) {
        double[] enu = new double[3];
        Matrix matrix = new Matrix(xyz, 3);
        matrix = new Matrix(xyzorg, 3);
        Matrix difxyz = new Matrix(3, 1);
        difxyz.setMatrix(0, 2, 0, 0, matrix.plus(matrix.uminus()));
        double[] orgllh = xyz2llh(xyzorg);
        double phi = orgllh[0];
        double lam = orgllh[1];
        double sinphi = Math.sin(phi);
        double cosphi = Math.cos(phi);
        double sinlam = Math.sin(lam);
        double coslam = Math.cos(lam);
        Matrix R = new Matrix(3, 3);
        R.set(0, 0, -sinlam);
        R.set(0, 1, coslam);
        R.set(0, 2, 0.0d);
        R.set(1, 0, (-sinphi) * coslam);
        R.set(1, 1, (-sinphi) * sinlam);
        R.set(1, 2, cosphi);
        R.set(2, 0, cosphi * coslam);
        R.set(2, 1, cosphi * sinlam);
        R.set(2, 2, sinphi);
        enu[0] = R.times(difxyz).get(0, 0);
        enu[1] = R.times(difxyz).get(1, 0);
        enu[2] = R.times(difxyz).get(2, 0);
        return enu;
    }

    public static double[] xyz2llh(double[] xyz) {
        double lon;
        double[] llh = new double[3];
        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];
        double x2 = x * x;
        double y2 = y * y;
        double z2 = z * z;
        double e = Math.sqrt(1.0d - ((6356752.3142d / 6378137.0d) * (6356752.3142d / 6378137.0d)));
        double b2 = 6356752.3142d * 6356752.3142d;
        double e2 = e * e;
        double ep = e * (6378137.0d / 6356752.3142d);
        double r = Math.sqrt(x2 + y2);
        double r2 = r * r;
        double F = (54.0d * b2) * z2;
        double G = (((1.0d - e2) * z2) + r2) - (e2 * ((6378137.0d * 6378137.0d) - (6356752.3142d * 6356752.3142d)));
        double c = (((e2 * e2) * F) * r2) / ((G * G) * G);
        double s = Math.pow((1.0d + c) + Math.sqrt((c * c) + (2.0d * c)), 0.3333333333333333d);
        double P = F / ((((3.0d * (((1.0d / s) + s) + 1.0d)) * (((1.0d / s) + s) + 1.0d)) * G) * G);
        double Q = Math.sqrt(1.0d + (((2.0d * e2) * e2) * P));
        double ro = ((-((P * e2) * r)) / (1.0d + Q)) + Math.sqrt(((((6378137.0d * 6378137.0d) / 2.0d) * (1.0d + (1.0d / Q))) - ((((1.0d - e2) * P) * z2) / ((1.0d + Q) * Q))) - ((P * r2) / 2.0d));
        double tmp = (r - (e2 * ro)) * (r - (e2 * ro));
        double U = Math.sqrt(tmp + z2);
        double V = Math.sqrt(((1.0d - e2) * z2) + tmp);
        double height = U * (1.0d - (b2 / (6378137.0d * V)));
        double lat = Math.atan((((ep * ep) * ((b2 * z) / (6378137.0d * V))) + z) / r);
        double temp = Math.atan(y / x);
        if (x >= 0.0d) {
            lon = temp;
        } else if (x >= 0.0d || y < 0.0d) {
            lon = temp - 3.141592653589793d;
        } else {
            lon = 3.141592653589793d + temp;
        }
        llh[0] = lat;
        llh[1] = lon;
        llh[2] = height;
        return llh;
    }

    public static double[] enu2xyz(double[] enu, double[] xyzorg) {
        double[] xyz = new double[3];
        Matrix matrix = new Matrix(enu, 3);
        double[] orgllh = xyz2llh(xyzorg);
        double phi = orgllh[0];
        double lam = orgllh[1];
        double sinphi = Math.sin(phi);
        double cosphi = Math.cos(phi);
        double sinlam = Math.sin(lam);
        double coslam = Math.cos(lam);
        Matrix R = new Matrix(3, 3);
        R.set(0, 0, -sinlam);
        R.set(0, 1, coslam);
        R.set(0, 2, 0.0d);
        R.set(1, 0, (-sinphi) * coslam);
        R.set(1, 1, (-sinphi) * sinlam);
        R.set(1, 2, cosphi);
        R.set(2, 0, cosphi * coslam);
        R.set(2, 1, cosphi * sinlam);
        R.set(2, 2, sinphi);
        xyz[0] = xyzorg[0] + R.inverse().times(matrix).get(0, 0);
        xyz[1] = xyzorg[1] + R.inverse().times(matrix).get(1, 0);
        xyz[2] = xyzorg[2] + R.inverse().times(matrix).get(2, 0);
        return xyz;
    }

    public static double[] enu2llh(double[] enu, double[] orgllh) {
        return xyz2llh(enu2xyz(enu, llh2xyz(orgllh)));
    }
}
