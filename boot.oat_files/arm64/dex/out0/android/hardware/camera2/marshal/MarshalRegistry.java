package android.hardware.camera2.marshal;

import android.hardware.camera2.utils.TypeReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarshalRegistry {
    private static HashMap<MarshalToken<?>, Marshaler<?>> sMarshalerMap = new HashMap();
    private static List<MarshalQueryable<?>> sRegisteredMarshalQueryables = new ArrayList();

    private static class MarshalToken<T> {
        private final int hash;
        final int nativeType;
        final TypeReference<T> typeReference;

        public MarshalToken(TypeReference<T> typeReference, int nativeType) {
            this.typeReference = typeReference;
            this.nativeType = nativeType;
            this.hash = typeReference.hashCode() ^ nativeType;
        }

        public boolean equals(Object other) {
            if (!(other instanceof MarshalToken)) {
                return false;
            }
            MarshalToken<?> otherToken = (MarshalToken) other;
            if (this.typeReference.equals(otherToken.typeReference) && this.nativeType == otherToken.nativeType) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.hash;
        }
    }

    public static <T> void registerMarshalQueryable(MarshalQueryable<T> queryable) {
        sRegisteredMarshalQueryables.add(queryable);
    }

    public static <T> Marshaler<T> getMarshaler(TypeReference<T> typeToken, int nativeType) {
        MarshalToken<T> marshalToken = new MarshalToken(typeToken, nativeType);
        Marshaler<T> marshaler = (Marshaler) sMarshalerMap.get(marshalToken);
        if (sRegisteredMarshalQueryables.size() == 0) {
            throw new AssertionError("No available query marshalers registered");
        }
        if (marshaler == null) {
            for (MarshalQueryable<T> castedPotential : sRegisteredMarshalQueryables) {
                if (castedPotential.isTypeMappingSupported(typeToken, nativeType)) {
                    marshaler = castedPotential.createMarshaler(typeToken, nativeType);
                    break;
                }
            }
            if (marshaler == null) {
                throw new UnsupportedOperationException("Could not find marshaler that matches the requested combination of type reference " + typeToken + " and native type " + MarshalHelpers.toStringNativeType(nativeType));
            }
            sMarshalerMap.put(marshalToken, marshaler);
        }
        return marshaler;
    }

    private MarshalRegistry() {
        throw new AssertionError();
    }
}
