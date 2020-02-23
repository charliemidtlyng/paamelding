package no.charlie.domain;

import java.time.LocalDateTime;

public class Endring {
    private int id;
    private int deltakerId;
    private int hendelseid;
    private String endringstekst;
    private LocalDateTime endringstidspunkt;

    public int getId() {
        return id;
    }

    public Endring withId(int id) {
        this.id = id;
        return this;
    }

    public int getDeltakerId() {
        return deltakerId;
    }

    public Endring withDeltakerId(int deltakerId) {
        this.deltakerId = deltakerId;
        return this;
    }

    public int getHendelseid() {
        return hendelseid;
    }

    public Endring withHendelseid(int hendelseid) {
        this.hendelseid = hendelseid;
        return this;
    }

    public String getEndringstekst() {
        return endringstekst;
    }

    public Endring withEndringstekst(String endringstekst) {
        this.endringstekst = endringstekst;
        return this;
    }

    public LocalDateTime getEndringstidspunkt() {
        return endringstidspunkt;
    }

    public Endring withEndringstidspunkt(LocalDateTime endringstidspunkt) {
        this.endringstidspunkt = endringstidspunkt;
        return this;
    }
}
