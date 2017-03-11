package com.google.android.gms.location.places;

import com.google.android.gms.common.data.Freezable;
import java.util.List;

public interface AutocompletePrediction extends Freezable<AutocompletePrediction> {

    public interface Substring {
        int getLength();

        int getOffset();
    }

    String getDescription();

    List<? extends Substring> getMatchedSubstrings();

    String getPlaceId();

    List<Integer> getPlaceTypes();
}
