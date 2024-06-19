package ubikesystem;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import object.Place;

/**
 * This class calculates the fee for renting a bike.
 * The fee is calculated based on the duration of the rental and the region where the bike is rented.
 * This class provides methods to calculate the fee for YouBike2.0, YouBike2.0E, and cross-area rentals.
 */

public class FeeCalculator {
    // Fee structure for YouBike2.0
    static int[][] youBikeFees = {
        {0, 10, 10, 20, 40}, // Taipei City
        {5, 10, 10, 20, 40}, // New Taipei City
        {0, 0, 10, 20, 40}, // Taoyuan City
        {10, 10, 10, 20, 40}, // Hsinchu County
        {10, 10, 10, 20, 40}, // Hsinchu City
        {10, 10, 10, 20, 40}, // Hsinchu Science Park
        {0, 10, 10, 20, 40}, // Miaoli County
        {0, 10, 10, 20, 40}, // Taichung City
        {0, 10, 10, 20, 40}, // Chiayi City
        {10, 10, 10, 20, 40}, // Tainan City
        {5, 10, 10, 20, 40}, // Kaohsiung City
        {0, 10, 10, 20, 40} // Pingtung County
    };

    // Fee structure for YouBike2.0E
    static int[][] youBikeEFees = {
        {20, 20, 40}, // Taoyuan City
        {20, 20, 40}, // Hsinchu County
        {20, 20, 40}, // Hsinchu City
        {20, 20, 40}, // Hsinchu Science Park
        {10, 20, 40}, // Miaoli County
        {10, 20, 40}, // Taichung City
        {10, 20, 40}, // Chiayi City
        {20, 20, 40}, // Tainan City
        {10, 20, 40}, // Kaohsiung City
        {10, 20, 40} // Pingtung County
    };

    static int[][] crossAreaFees = {
        {0, 0, 0, 630, 630, 630, 630, 815, 935, 1010, 1065, 1090}, // Taipei City
        {0, 0, 0, 630, 630, 630, 630, 815, 935, 1010, 1065, 1090}, // New Taipei City
        {0, 0, 0, 600, 600, 600, 600, 750, 910, 980, 1035, 1060}, // Taoyuan City
        {630, 630, 600, 0, 0, 0, 0, 0, 810, 880, 940, 965}, // Hsinchu County
        {630, 630, 600, 0, 0, 0, 0, 0, 810, 880, 940, 965}, // Hsinchu City
        {630, 630, 600, 0, 0, 0, 0, 0, 810, 880, 940, 965}, // Hsinchu Science Park
        {630, 630, 600, 0, 0, 0, 0, 0, 810, 880, 940, 965}, // Miaoli County
        {815, 815, 750, 0, 0, 0, 0, 0, 660, 820, 875, 900}, // Taichung City
        {935, 935, 910, 810, 810, 810, 810, 660, 0, 615, 715, 740}, // Chiayi City
        {1010, 1010, 980, 880, 880, 880, 880, 820, 615, 0, 0, 0}, // Tainan City
        {1065, 1065, 1035, 940, 940, 940, 940, 875, 715, 0, 0, 0}, // Kaohsiung City
        {1090, 1090, 1060, 965, 965, 965, 965, 900, 740, 0, 0, 0} // Pingtung County    
    };

    private static int getMinutes(Date start, Date end) {
        long duration = end.getTime() - start.getTime();
        return (int) TimeUnit.MILLISECONDS.toMinutes(duration);
    }

    private static int getRegionIndex(Place place) {
        return place.ordinal();
    }

    public int calculateYouBikeFee(Date start, Date end, Place region) {
        int minutes = getMinutes(start, end);
        int[] fees = youBikeFees[getRegionIndex(region)];
        int fee = 0;
        if (minutes <= 30) {
            fee = fees[0];
        } else if (minutes <= 60) {
            fee = fees[1];
        } else if (minutes <= 240) {
            fee = fees[1] + ((minutes - 60) / 30) * fees[2];
        } else if (minutes <= 480) {
            fee = fees[1] + (180 / 30) * fees[2] + ((minutes - 240) / 30) * fees[3];
        } else {
            fee = fees[1] + (180 / 30) * fees[2] + (240 / 30) * fees[3] + ((minutes - 480) / 30) * fees[4];
        }
        return fee;
    }

    public int calculateYouBikeEFee(Date start, Date end, Place region) {
        int minutes = getMinutes(start, end);
        int[] fees = youBikeEFees[getRegionIndex(region) - 2];
        int fee = 0;
        if (minutes <= 30) {
            fee = fees[0];
        } else if (minutes <= 120) {
            fee = fees[0] + ((minutes - 30) / 30) * fees[1];
        } else {
            fee = fees[0] + ((120 - 30) / 30) * fees[1] + ((minutes - 120) / 30) * fees[2];
        }
        return fee;
    }

    public int calculateCrossAreaFee(Date start, Date end, Place startRegion, Place endRegion) {
        int startRegionIndex = getRegionIndex(startRegion);
        int endRegionIndex = getRegionIndex(endRegion);
        int fee = crossAreaFees[startRegionIndex][endRegionIndex];
        return fee;
    }
}
