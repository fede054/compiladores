package entity;

public class Truck extends Vehicle {

    private static final Long RATIO = 15L;

    public Truck(final String patent) {
        super();
        super.setPatent(patent);
    }

    @Override
    public Long getRatio() {
        return RATIO;
    }

}
