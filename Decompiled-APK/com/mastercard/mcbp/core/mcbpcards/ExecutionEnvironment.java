package com.mastercard.mcbp.core.mcbpcards;

public interface ExecutionEnvironment {
    boolean isJailBroken();

    boolean wasRecentlyAttacked();
}
