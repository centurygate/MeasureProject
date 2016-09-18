package measurebussiness.model;

public class Measure extends MeasureKey {
    private Double distance;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
    
    @Override
    public String toString()
    {
        return "Measure{" +
                "distance=" + distance +
                "} " + super.toString();
    }
}