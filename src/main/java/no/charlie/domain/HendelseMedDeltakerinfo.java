package no.charlie.domain;

import java.util.ArrayList;
import java.util.List;

public class HendelseMedDeltakerinfo {

    private Hendelse hendelseInfo;
    private List<Deltaker> deltakere = new ArrayList<>();
    private int antallPaameldteDeltakere;

    public Hendelse getHendelseInfo() {
        return hendelseInfo;
    }

    public HendelseMedDeltakerinfo withHendelseInfo(Hendelse hendelseInfo) {
        this.hendelseInfo = hendelseInfo;
        return this;
    }

    public List<Deltaker> getDeltakere() {
        return deltakere;
    }

    public HendelseMedDeltakerinfo withDeltakere(List<Deltaker> deltakere) {
        this.deltakere = deltakere;
        return this;
    }

    public int getAntallPaameldteDeltakere() {
        return antallPaameldteDeltakere;
    }

    public HendelseMedDeltakerinfo withAntallPaameldteDeltakere(int antallPaameldteDeltakere) {
        this.antallPaameldteDeltakere = antallPaameldteDeltakere;
        return this;
    }
}
