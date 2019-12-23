package no.charlie.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlackClientTest {


    @Disabled
    @Test
    void testSlackIntegrasjon() throws JsonProcessingException {
        SlackClient slackClient = SlackClient.build("https://hooks.slack.com", new ObjectMapper());
        SlackMelding so = new SlackMelding("Test overskrift", "Fotballtrening", "<https://fotball.bekk.no/4|meld på>");
        String stringStringMap = slackClient.postTilSlackkanal("T028UJTLQ/BCQ56FMPX/8M2mzs3yD8YOeT1FcFrrMLUf", so);
        assertNotNull(stringStringMap);
    }

}