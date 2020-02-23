package no.charlie.domain;

import static no.charlie.domain.Hendelsestype.Fotballkamp;
import static no.charlie.domain.Hendelsestype.Fotballtrening;
import static no.charlie.domain.Hendelsestype.Innebandykamp;
import static no.charlie.domain.Hendelsestype.Innebandytrening;
import static no.charlie.domain.Hendelsestype.Volleyballkamp;
import static no.charlie.domain.Hendelsestype.Volleyballtrening;

public class SlackPaths {
    private final String fotballPath;
    private final String volleyballPath;
    private final String innebandyPath;
    private final String fotballLenke = "http://fotball.bekk.no/";
    private final String volleyballLenke = "http://volleyball.bekk.no/";
    private final String innebandyLenke = "http://innebandy.bekk.no/";
    private final String charliePath = "T028UJTLQ/BUCTBP8VC/j2DWV4tO4jj6hspCEzWjsjWM";

    public SlackPaths(String fotballPath, String volleyballPath, String innebandyPath) {
        this.fotballPath = fotballPath;
        this.volleyballPath = volleyballPath;
        this.innebandyPath = innebandyPath;
    }

    public String finnRiktigKanal(Hendelsestype hendelsestype) {
        if (charliePath != null) {
            return charliePath;
        }
        if (Fotballtrening.equals(hendelsestype) || Fotballkamp.equals(hendelsestype)) {
            return fotballPath;
        }

        if (Volleyballtrening.equals(hendelsestype) || Volleyballkamp.equals(hendelsestype)) {
            return volleyballPath;
        }

        if (Innebandytrening.equals(hendelsestype) || Innebandykamp.equals(hendelsestype)) {
            return innebandyPath;
        }

        throw new RuntimeException("Ukjent hendelsestype");
    }

    public String finnRiktigLenke(Hendelsestype hendelsestype) {
        if (Fotballtrening.equals(hendelsestype) || Fotballkamp.equals(hendelsestype)) {
            return fotballLenke;
        }

        if (Volleyballtrening.equals(hendelsestype) || Volleyballkamp.equals(hendelsestype)) {
            return volleyballLenke;
        }

        if (Innebandytrening.equals(hendelsestype) || Innebandykamp.equals(hendelsestype)) {
            return innebandyLenke;
        }

        throw new RuntimeException("Ukjent hendelsestype");
    }

}
