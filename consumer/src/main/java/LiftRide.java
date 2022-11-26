public class LiftRide {
    public int resortId;
    public int seasonId;
    public int dayId;
    public int skierId;
    private int liftId;

    public LiftRide(int resortId, int seasonId, int dayId, int skierId, int liftId) {
        this.resortId = resortId;
        this.seasonId = seasonId;
        this.dayId = dayId;
        this.skierId = skierId;
        this.liftId = liftId;
    }

    public int getResortId() {
        return resortId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public int getDayId() {
        return dayId;
    }

    public int getSkierId() {
        return skierId;
    }

    public int getLiftId() {
        return liftId;
    }
}