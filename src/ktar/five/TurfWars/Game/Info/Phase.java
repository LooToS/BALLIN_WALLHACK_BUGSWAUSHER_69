package ktar.five.TurfWars.Game.Info;

public enum Phase {

    startCount(0, 10, PhaseType.START_COUNT, 0),
    n1(1, 40, PhaseType.BUILDING, 64),
    n2(2, 90, PhaseType.KILLING, 1),
    n3(3, 20, PhaseType.BUILDING, 32),
    n4(4, 90, PhaseType.KILLING, 2),
    n5(5, 20, PhaseType.BUILDING, 32),
    n6(6, 99999, PhaseType.KILLING, 3);

    private final int phaseNumber;
    private final int seconds;
    private final PhaseType type;
    private final int amount;

    private Phase(int phaseNumber, int seconds, PhaseType type, int amount) {
        this.phaseNumber = phaseNumber;
        this.seconds = seconds;
        this.type = type;
        this.amount = amount;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public int getSeconds() {
        return seconds;
    }

    public PhaseType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public enum PhaseType {

        BUILDING,
        KILLING,
        START_COUNT

    }

}


