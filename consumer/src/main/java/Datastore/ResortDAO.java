package Datastore;

public class ResortDAO extends DAO {
    private Resort resort;

    public ResortDAO(Resort resort) {
        this.resort = resort;

    }

    public void insertResort() {
        String insertStatement = "INSERT IGNORE resorts(resort_id, season, day_id, skier_id, lift_id)\n" +
                "values (%d, %d, %d, %d, %d);";
        insertStatement = String.format(insertStatement, resort.getResort_id(), resort.getSeason(), resort.getDay_id(), resort.getSkier_id(), resort.getLift_id());
        execute(insertStatement);

    }
}
