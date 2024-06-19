package object;


/**
 * This class represents the status of a bike in the bike sharing system.
 */
public enum BikeStatus {
    NON_OPERATIONAL(0),
    OPERATIONAL(1),
    RENTED(2),
    LOST(3);

    private int statusCode;

    BikeStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public static BikeStatus fromStatusCode(int statusCode) {
        for (BikeStatus status : BikeStatus.values()) {
            if (status.statusCode == statusCode) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown bike status code: " + statusCode);
    }

    @Override
    public String toString() {
        return String.valueOf(statusCode);
    }
}
