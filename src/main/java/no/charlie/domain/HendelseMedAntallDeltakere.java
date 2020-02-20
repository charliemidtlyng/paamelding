package no.charlie.domain;

public class HendelseMedAntallDeltakere {

    private Hendelse hendelseInfo;
    private int antallPaameldteDeltakere;

    public Hendelse getHendelseInfo() {
        return hendelseInfo;
    }

    public HendelseMedAntallDeltakere withHendelseInfo(Hendelse hendelseInfo) {
        this.hendelseInfo = hendelseInfo;
        return this;
    }

    public int getAntallPaameldteDeltakere() {
        return antallPaameldteDeltakere;
    }

    public HendelseMedAntallDeltakere withAntallPaameldteDeltakere(int antallPaameldteDeltakere) {
        this.antallPaameldteDeltakere = antallPaameldteDeltakere;
        return this;
    }
}
