package no.charlie.domain;

public class DeltakerRequest {
    private String navn;
    private String slacknavn;

    public String getNavn() {
        return navn;
    }

    public DeltakerRequest withNavn(String navn) {
        this.navn = navn;
        return this;
    }

    public String getSlacknavn() {
        return slacknavn;
    }

    public DeltakerRequest withSlacknavn(String slacknavn) {
        this.slacknavn = slacknavn;
        return this;
    }

}
