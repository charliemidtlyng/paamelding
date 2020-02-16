package no.charlie.domain;

import java.time.LocalDateTime;

public class HendelseRequest {

    private Hendelsestype hendelsestype;
    private String sted;
    private LocalDateTime starttid;
    private LocalDateTime paameldingstid;
    private String info;
    private String lenke;
    private int varighet;
    private int maksAntallDeltakere;

    public Hendelsestype getHendelsestype() {
        return hendelsestype;
    }

    public HendelseRequest withHendelsestype(Hendelsestype hendelsestype) {
        this.hendelsestype = hendelsestype;
        return this;
    }

    public String getSted() {
        return sted;
    }

    public HendelseRequest withSted(String sted) {
        this.sted = sted;
        return this;
    }

    public LocalDateTime getStarttid() {
        return starttid;
    }

    public HendelseRequest withStarttid(LocalDateTime starttid) {
        this.starttid = starttid;
        return this;
    }

    public LocalDateTime getPaameldingstid() {
        return paameldingstid;
    }

    public HendelseRequest withPaameldingstid(LocalDateTime paameldingstid) {
        this.paameldingstid = paameldingstid;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public HendelseRequest withInfo(String info) {
        this.info = info;
        return this;
    }

    public String getLenke() {
        return lenke;
    }

    public HendelseRequest withLenke(String lenke) {
        this.lenke = lenke;
        return this;
    }

    public int getVarighet() {
        return varighet;
    }

    public HendelseRequest withVarighet(int varighet) {
        this.varighet = varighet;
        return this;
    }

    public int getMaksAntallDeltakere() {
        return maksAntallDeltakere;
    }

    public HendelseRequest withMaksAntallDeltakere(int maksAntallDeltakere) {
        this.maksAntallDeltakere = maksAntallDeltakere;
        return this;
    }

}
