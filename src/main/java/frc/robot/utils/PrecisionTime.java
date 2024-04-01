package frc.robot.utils;

public enum PrecisionTime {
    NANOSECONDS(PrecisionTime.NANO_SCALE),
    /**
     * Time unit representing one thousandth of a millisecond.
     */
    MICROSECONDS(PrecisionTime.MICRO_SCALE),
    /**
     * Time unit representing one thousandth of a second.
     */
    MILLISECONDS(PrecisionTime.MILLI_SCALE),
    /**
     * Time unit representing one second.
     */
    SECONDS(PrecisionTime.SECOND_SCALE);
    private static final long NANO_SCALE   = 1L;
    private static final long MICRO_SCALE  = 1000L * NANO_SCALE;
    private static final long MILLI_SCALE  = 1000L * MICRO_SCALE;
    private static final long SECOND_SCALE = 1000L * MILLI_SCALE;
    private final long scale;

    PrecisionTime(long scale) {
        this.scale = scale;
    }
    public double convert(PrecisionTime dest, double value){
        return (value * scale) / dest.scale;
    }


}
