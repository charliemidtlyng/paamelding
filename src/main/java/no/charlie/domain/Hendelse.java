package no.charlie.domain;

import java.time.LocalDateTime;

public class Hendelse {

    private int id;
    private Hendelsestype hendelsestype;
    private String sted;
    private LocalDateTime starttid;
    private LocalDateTime paameldingstid;
    private LocalDateTime sisteSlackOppdatering;
    private String info;
    private String lenke;
    private int varighet;
    private int maksAntallDeltakere;

    public int getId() {
        return id;
    }

    public Hendelse withId(int id) {
        this.id = id;
        return this;
    }

    public Hendelsestype getHendelsestype() {
        return hendelsestype;
    }

    public Hendelse withHendelsestype(Hendelsestype hendelsestype) {
        this.hendelsestype = hendelsestype;
        return this;
    }

    public String getSted() {
        return sted;
    }

    public Hendelse withSted(String sted) {
        this.sted = sted;
        return this;
    }

    public LocalDateTime getStarttid() {
        return starttid;
    }

    public Hendelse withStarttid(LocalDateTime starttid) {
        this.starttid = starttid;
        return this;
    }

    public LocalDateTime getPaameldingstid() {
        return paameldingstid;
    }

    public Hendelse withPaameldingstid(LocalDateTime paameldingstid) {
        this.paameldingstid = paameldingstid;
        return this;
    }

    public LocalDateTime getSisteSlackOppdatering() {
        return sisteSlackOppdatering;
    }

    public Hendelse withSisteSlackOppdatering(LocalDateTime sisteSlackOppdatering) {
        this.sisteSlackOppdatering = sisteSlackOppdatering;
        return this;
    }

    public String getInfo() {
        return info;
    }

    public Hendelse withInfo(String info) {
        this.info = info;
        return this;
    }

    public String getLenke() {
        return lenke;
    }

    public Hendelse withLenke(String lenke) {
        this.lenke = lenke;
        return this;
    }

    public int getVarighet() {
        return varighet;
    }

    public Hendelse withVarighet(int varighet) {
        this.varighet = varighet;
        return this;
    }

    public int getMaksAntallDeltakere() {
        return maksAntallDeltakere;
    }

    public Hendelse withMaksAntallDeltakere(int maksAntallDeltakere) {
        this.maksAntallDeltakere = maksAntallDeltakere;
        return this;
    }

}
