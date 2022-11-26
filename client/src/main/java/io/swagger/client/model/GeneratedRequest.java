package io.swagger.client.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;
/**
 * Generated request
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2022-09-28T21:40:59.899Z[GMT]")
public class GeneratedRequest {
    private LiftRide liftRide;
    @SerializedName("skierId")
    private Integer skierId = null;

    @SerializedName("resortId")
    private Integer resortId = null;

    @SerializedName("seasonID")
    private Integer seasonId = null;

    @SerializedName("dayID")
    private Integer dayId = null;

    public GeneratedRequest(LiftRide liftRide, int skierId, int resortId, int seasonId, int dayId) {
        this.liftRide = liftRide;
        this.skierId = skierId;
        this.resortId = resortId;
        this.seasonId = seasonId;
        this.dayId = dayId;
    }

    /**
     * Get liftRide
     * @return liftRide
     **/
    public LiftRide getLiftRide() {
        return liftRide;
    }

    /**
     * Get skierId
     * @return skierId
     **/
    public Integer getSkierId() {
        return skierId;
    }


    /**
     * Get resortId
     * @return resortId
     **/
    public Integer getResortId() {
        return resortId;
    }

    /**
     * Get seasonId
     * @return seasonId
     **/
    public Integer getSeasonId() {
        return seasonId;
    }


    /**
     * Get dayId
     * @return dayId
     **/
    public Integer getDayId() {
        return dayId;
    }

    public void setLiftRide(LiftRide liftRide) {
        this.liftRide = liftRide;
    }
    public void setSkierId(Integer skierId) {
        this.skierId = skierId;
    }
    public void setResortId(Integer resortId) {
        this.resortId = resortId;
    }
    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }
    public void setDayId(Integer dayId) {
        this.dayId = dayId;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        //LiftRide liftRide = (LiftRide) o;
        GeneratedRequest request = (GeneratedRequest) o;
        return Objects.equals(this.liftRide, request.liftRide) &&
                Objects.equals(this.skierId, request.skierId) &&
                Objects.equals(this.resortId, request.resortId) &&
                Objects.equals(this.seasonId, request.seasonId) &&
                Objects.equals(this.dayId, request.dayId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(liftRide, skierId, resortId, seasonId, dayId);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class GenerateRequest {\n");
        sb.append("    liftRide: ").append(toIndentedString(liftRide)).append("\n");
        sb.append("    skierId: ").append(toIndentedString(skierId)).append("\n");
        sb.append("    resortId: ").append(toIndentedString(resortId)).append("\n");
        sb.append("    seasonId: ").append(toIndentedString(seasonId)).append("\n");
        sb.append("    dayId: ").append(toIndentedString(dayId)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

}
