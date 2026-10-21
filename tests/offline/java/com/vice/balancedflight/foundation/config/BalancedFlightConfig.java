package com.vice.balancedflight.foundation.config;

public class BalancedFlightConfig {
    public static final Value<Boolean> enableSlowFallingOnFlightLoss = new Value<>(true);
    public static final Value<Boolean> ElytraAscended = new Value<>(true);
    public static final Value<Boolean> CreativeAscended = new Value<>(true);
    public static final Value<Boolean> ElytraAnchor = new Value<>(false);
    public static final Value<Boolean> CreativeAnchor = new Value<>(true);
    public static final Value<Double> anchorDistanceMultiplier = new Value<>(1.0);
    public static class Value<T> {
        private T value;
        public Value(T value) { this.value = value; }
        public T get() { return value; }
        public void set(T value) { this.value = value; }
    }
}
