package object;


/**
 * This class represents the place of a bike in the bike sharing system.
 * The place can be a city or a county.
 */
public enum Place {
    TAIPEI_CITY("TAIPEI_CITY"),
    NEW_TAIPEI_CITY("NEW_TAIPEI_CITY"),
    TAOYUAN_CITY("TAOYUAN_CITY"),
    HSINCHU_COUNTY("HSINCHU_COUNTY"),
    HSINCHU_CITY("HSINCHU_CITY"),
    HSINCHU_SCIENCE_PARK("HSINCHU_SCIENCE_PARK"),
    MIAOLI_COUNTY("MIAOLI_COUNTY"),
    TAICHUNG_CITY("TAICHUNG_CITY"),
    CHIAYI_CITY("CHIAYI_CITY"),
    TAINAN_CITY("TAINAN_CITY"),
    KAOHSIUNG_CITY("KAOHSIUNG_CITY"),
    PINGTUNG_COUNTY("PINGTUNG_COUNTY");

    private final String chineseName;

    Place(String chineseName) {
        this.chineseName = chineseName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public static Place fromChineseName(String chineseName) {
        for (Place place : Place.values()) {
            if (place.chineseName.equals(chineseName)) {
                return place;
            }
        }
        throw new IllegalArgumentException("Unknown place: " + chineseName);
    }

    @Override
    public String toString() {
        return chineseName;
    }
}
