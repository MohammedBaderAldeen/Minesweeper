package settings;

public enum Status {
    COVERED(-1), BLANK(0), ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
    FLAGGED(9), BOMBED(10), GRAY_BOMBED(11), DISPLAY_ZERO(12), DISPLAY_ONE(13), DISPLAY_TWO(14),
    DISPLAY_THREE(15), DISPLAY_FOUR(16), DISPLAY_FIVE(17), DISPLAY_SIX(18), DISPLAY_SEVEN(19),
    DISPLAY_EIGHT(20), DISPLAY_NINE(21), SMILEY(22), DEAD_SMILEY(23), SUN_GLASSES(24), SHIELD(25),SUPER_SHIELD(26) ;

    private final int id;

    Status(int id) {
        this.id = id;
    }

    public static boolean isNumber(Status status) {
        return (status != COVERED && status != BLANK && status != FLAGGED && status != BOMBED && status != GRAY_BOMBED);
    }

    public int getValue() {
        return this.id;
    }

    public static Status getValue(int id) {
        for (Status status : Status.values()) {
            if (status.getValue() == id) {
                return status;
            }
        }
        return Status.COVERED;
    }

    public static Status getDisplayNumber(int num) {
//        switch (num) {
//            case 0:
//                return DISPLAY_ZERO;
//            case 1:
//                return DISPLAY_ONE;
//        }
        return getValue(num + 12);
    }
}
