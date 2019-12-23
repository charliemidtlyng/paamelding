package no.charlie.api;

import no.charlie.client.CaptchaValidator;
import no.charlie.domain.Hendelse;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.HttpHeaders;

public class Validator {

    private final CaptchaValidator captchaValidator;

    public Validator(CaptchaValidator captchaValidator) {
        this.captchaValidator = captchaValidator;
    }

    public void sjekkUgyldigeVerdierForAvmelding(int deltakerId, Optional<Hendelse> hendelse) {
        if (!hendelse.isPresent()) {
            throw new BadRequestException("Hendelse eksisterer ikke");
        }
        if (deltakerId <= 0) {
            throw new BadRequestException("Ugyldig id");
        }
    }

    public void sjekkUgyldigeVerdierForHendelse(Hendelse hendelse) {
        boolean manglerVerdi = Stream.of(
                hendelse.getHendelsestype(),
                hendelse.getSted(),
                hendelse.getStarttid(),
                hendelse.getMaksAntallDeltakere(),
                hendelse.getPaameldingstid()
        )
                .anyMatch(Objects::isNull);
        if (manglerVerdi) {
            throw new BadRequestException("Mangler et påkrevd felt");
        }
    }

    public void sjekkUgyldigeVerdierForPaamelding(boolean hendelseFinnes, HttpHeaders httpHeaders) {
        boolean erCaptchaGyldig = captchaValidator.erCaptchaGyldig(
                httpHeaders.getHeaderString("captcha-request"),
                finnIpFraRequestHeader(httpHeaders));
        if (!erCaptchaGyldig) {
            throw new BadRequestException("Ugyldig captcha");
        }
        if (!hendelseFinnes) {
            throw new BadRequestException(String.format("Hendelse finnes ikke"));
        }
    }

    private String finnIpFraRequestHeader(HttpHeaders httpHeaders) {
        List<String> requestHeadere = httpHeaders.getRequestHeader("X-Forwarded-For");
        return requestHeadere != null ? requestHeadere.stream().findFirst().orElse("") : "";
    }

}
