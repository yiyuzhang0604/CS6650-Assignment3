public class LiftResponse {
    private Integer resortId;
    private Integer seasonId;
    private Integer dayId;
    private Integer skierId;

    public LiftResponse(Integer resortId, Integer seasonId, Integer dayId, Integer skierId) {
        this.resortId = resortId;
        this.seasonId = seasonId;
        this.dayId = dayId;
        this.skierId = skierId;
    }

    public Integer getResortId() {
        return resortId;
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public Integer getDayId() {
        return dayId;
    }

    public Integer getSkierId() {
        return skierId;
    }

    @Override
    public String toString() {
        return "LiftResponse{" +
                "resortId=" + resortId +
                ", seasonId=" + seasonId +
                ", dayId=" + dayId +
                ", skierId=" + skierId +
                '}';
    }
}
