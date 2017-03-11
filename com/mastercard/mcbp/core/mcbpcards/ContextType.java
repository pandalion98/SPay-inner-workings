package com.mastercard.mcbp.core.mcbpcards;

public enum ContextType {
    MCHIP_FIRST_TAP,
    MCHIP_COMPLETED,
    MAGSTRIPE_FIRST_TAP,
    MAGSTRIPE_COMPLETED,
    CONTEXT_CONFLICT,
    UNSUPPORTED_TRANSIT
}
