package fi.valonia.pyorallatoihin.data;

public enum Sport {

    BICYCLE("BICYCLE"),  ELECTRIC_CYCLE("ELECTRIC_CYCLE"), WALKING("WALKING"), KICK_SCOOTER("KICK_SCOOTER"), ROLLER_BLADES(
            "ROLLER_BLADES"), ROWING("ROWING"), WITH_HORSE("WITH_HORSE"), WITH_DOG_SLED(
            "WITH_DOG_SLED"), OTHER("OTHER");

    String stringRepresentation;

    private Sport(String stringRepresentation) {
        this.stringRepresentation = stringRepresentation;
    }

    public String getStringRepresentation() {
        return stringRepresentation;
    }

    public static String sportToString(Sport sport) {
        return sport.getStringRepresentation();
    };

    public static Sport stringToSport(String string) {
        if (string == null) {
            return Sport.OTHER;
        }
        for (Sport sport : Sport.values()) {
            if (string.equals(sport.getStringRepresentation())) {
                return sport;
            }
        }
        return Sport.OTHER;
    }
}
