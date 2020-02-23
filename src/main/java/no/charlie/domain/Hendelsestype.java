package no.charlie.domain;

public enum Hendelsestype {
    Fotballkamp,
    Fotballtrening,
    Innebandykamp,
    Innebandytrening,
    Volleyballkamp,
    Volleyballtrening;

    public boolean erTrening() {
        return this == Fotballtrening || this == Innebandytrening || this == Volleyballtrening;
    }
}
