package no.charlie.domain;

import java.time.LocalDateTime;

import static java.lang.Boolean.TRUE;

public class Deltaker {
    private int id;
    private int hendelseid;
    private String navn;
    private String slacknavn;
    private LocalDateTime registreringstidspunkt;
    private LocalDateTime avmeldingstidspunkt;
    private Boolean uttatt;

    public int getId() {
        return id;
    }

    public Deltaker withId(int id) {
        this.id = id;
        return this;
    }

    public int getHendelseid() {
        return hendelseid;
    }

    public Deltaker withHendelseid(int hendelseid) {
        this.hendelseid = hendelseid;
        return this;
    }

    public String getNavn() {
        return navn;
    }

    public Deltaker withNavn(String navn) {
        this.navn = navn;
        return this;
    }

    public String getSlacknavn() {
        return slacknavn;
    }

    public Deltaker withSlacknavn(String slacknavn) {
        this.slacknavn = slacknavn;
        return this;
    }

    public LocalDateTime getRegistreringstidspunkt() {
        return registreringstidspunkt;
    }

    public Deltaker withRegistreringstidspunkt(LocalDateTime registreringstidspunkt) {
        this.registreringstidspunkt = registreringstidspunkt;
        return this;
    }

    public LocalDateTime getAvmeldingstidspunkt() {
        return avmeldingstidspunkt;
    }

    public Deltaker withAvmeldingstidspunkt(LocalDateTime avmeldingstidspunkt) {
        this.avmeldingstidspunkt = avmeldingstidspunkt;
        return this;
    }

    public Boolean getUttatt() {
        return uttatt;
    }

    public Deltaker withUttatt(Boolean uttatt) {
        this.uttatt = uttatt;
        return this;
    }

    public boolean erIkkeUttatt() {
        return !TRUE.equals(uttatt);
    }

    public boolean erPaameldt() {
        return avmeldingstidspunkt == null;
    }
}
