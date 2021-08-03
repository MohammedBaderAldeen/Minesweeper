package settings;

public enum Action {
    FLAG(1), UN_FLAG(2), OPEN(3), FLOOD(4),SUPER_SHIELD(5);
    private final int id;

    Action(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
