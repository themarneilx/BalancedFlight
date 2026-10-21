package com.vice.balancedflight;

public class BalancedFlight {
    public static final TestLogger LOGGER = new TestLogger();
    public static class TestLogger {
        public int warnings;
        public void warn(String message, Throwable exception) { warnings++; }
    }
}
