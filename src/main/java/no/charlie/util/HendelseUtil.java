package no.charlie.util;

import no.charlie.domain.Hendelse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class HendelseUtil {

    public static boolean erHendelsestidspunktKlarTilSlackmelding(Hendelse hendelse) {
        return hendelse.getHendelsestype().erTrening()
                ? LocalDateTime.now().plusDays(1).isAfter(hendelse.getPaameldingstid())
                : LocalDateTime.now().plusDays(6).isAfter(hendelse.getStarttid());
    }

    public static List<Integer> hendelsesIder(List<Hendelse> hendelser) {
        return hendelser.stream()
                .map(Hendelse::getId)
                .collect(Collectors.toList());
    }

}
