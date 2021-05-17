package testtwo.dto;

public class Car extends Vehicle {

    private static final Long RATIO = 10L;

    public Car(final String patent) {
        super();
        super.setPatent(patent);
    }

    @Override
    public Long getRatio() {
        return RATIO;
    }

}
