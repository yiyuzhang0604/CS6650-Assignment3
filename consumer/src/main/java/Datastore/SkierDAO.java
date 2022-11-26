package Datastore;

public class SkierDAO extends DAO {

    private Skier skier;

    public SkierDAO(Skier skier) {
        this.skier = skier;
    }

    public void initializeSkier() {
        String insertStatement = "INSERT IGNORE skiers(skier_id, day_id, season, lift_id)\n" +
                "values (%d, %d, %d, 0);";
        insertStatement = String.format(insertStatement, skier.getSkier_id(), skier.getDay_id(), skier.getSeason());
        execute(insertStatement);
    }

    public void update() {
        String updateStatement = "UPDATE skiers set lift_id = lift_id + %d where skier_id = %d and day_id = %d and season = %d;";
        updateStatement = String.format(updateStatement, skier.getLift_id(), skier.getSkier_id(), skier.getDay_id(), skier.getSeason());
        execute(updateStatement);
    }
}
