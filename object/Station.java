package object;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * An implementation of a station in the system.
 * The station has a name, id, area, position, and a list of pillars.
 */

public class Station {
    private String stationName;
    private String stationId;
    private Place area;
    private List<Float> Pos;
    private List<Pillar> pillarList;
    private int bikeType0Count;
    private int bikeType1Count;
    private int emptySlotCount;

    public Station(String stationName, String stationId, Place area, List<Float> Pos, List<Pillar> pillarList) {
        this.stationName = stationName;
        this.stationId = stationId;
        this.area = area;
        this.Pos = Pos;
        this.pillarList = pillarList;
    }

    // Getters and Setters
    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Place getArea() {
        return area;
    }

    public void setArea(Place area) {
        this.area = area;
    }

    public List<Float> getPos() {
        return Pos;
    }

    public void setPos(List<Float> Pos) {
        this.Pos = Pos;
    }

    public double getLatitude() {
        return this.Pos.get(0).doubleValue();
    }


    public double getLongitude() {
        return this.Pos.get(1).doubleValue();
    }

    public Integer getTwoBikeNumber() {
        int operationalBikes = 0;

        for (Pillar pillar : this.pillarList) {
            Bike bike = pillar.getBike();
            if (bike == null) {
            } 
            else if (bike.getStatus() == BikeStatus.OPERATIONAL && bike.getBikeType() == 0) {
                operationalBikes++;
            }
        }

        return operationalBikes;
    }

    public Integer getTwoEBikeNumber() {
        int operationalBikes = 0;

        for (Pillar pillar : this.pillarList) {
            Bike bike = pillar.getBike();
            if (bike == null) {
            } 
            else if (bike.getStatus() == BikeStatus.OPERATIONAL && bike.getBikeType() == 1) {
                operationalBikes++;
            }
        }

        return operationalBikes;
    }

    public Integer getEmptyNumber() {
        int slots = 0;

        for (Pillar pillar : this.pillarList) {
            Bike bike = pillar.getBike();
            if (bike == null) {
                slots += 1;
            } 
            else {
            }
        }

        return slots;
    }

    public Pillar getRandomPillarWithBike() {
        List<Pillar> pillarsWithBike = new ArrayList<>();

        for (Pillar pillar : this.pillarList) {
            Bike bike = pillar.getBike();
            if (bike != null){
                pillarsWithBike.add(pillar);
            }
        }

        if (pillarsWithBike.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex = random.nextInt(pillarsWithBike.size());
        return pillarsWithBike.get(randomIndex);
    }

    public Pillar getRandomEmptyPillar() {
        List<Pillar> emptyPillars = new ArrayList<>();
    
        for (Pillar pillar : this.pillarList) {
            Bike bike = pillar.getBike();
            if (bike == null) {
                emptyPillars.add(pillar);
            }
        }
    
        if (emptyPillars.isEmpty()) {
            return null;
        }
    
        Random random = new Random();
        int randomIndex = random.nextInt(emptyPillars.size());
        return emptyPillars.get(randomIndex);
    }

    public List<Pillar> getPillarList() {
        return pillarList;
    }

    public void setPillarList(List<Pillar> pillarList) {
        this.pillarList = pillarList;
    }

    public void setBikeType0Count(int bikeType0Count) {
        this.bikeType0Count = bikeType0Count;
    }

    public int getBikeType0Count() {
        return bikeType0Count;
    }

    public void setBikeType1Count(int bikeType1Count) {
        this.bikeType1Count = bikeType1Count;
    }

    public int getBikeType1Count() {
        return bikeType1Count;
    }

    public void setEmptySlotCount(int emptySlotCount) {
        this.emptySlotCount = emptySlotCount;
    }

    public int getEmptySlotCount() {
        return emptySlotCount;
    }

    @Override
    public String toString() {
        return "Station{" +
                "stationName='" + stationName + '\'' +
                ", stationId='" + stationId + '\'' +
                '}';
    }
}
