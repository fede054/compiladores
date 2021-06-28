package testtwo.dto.rq;

public abstract class VehicleDTO {

    private String patent;

    public abstract Long getRatio();

    public String getPatent() {
        return patent;
    }

    public void setPatent(final String patent) {
        this.patent = patent;
    }

}
