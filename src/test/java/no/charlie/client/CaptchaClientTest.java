package no.charlie.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CaptchaClientTest {

    @Test
    void testCaptchaClient() {
        CaptchaClient captchaClient = CaptchaClient.build("https://www.google.com/recaptcha/api/siteverify", new ObjectMapper());
        String secret = "secret";
        CaptchaResponse captchaResponse = captchaClient.sjekkGyldigCaptcha(secret,
                "6LdhmgATAAAAADMLD50qGZ-DSaa3bVlrsQp6BTgA", "216.58.207.228");
        assertNotNull(captchaResponse);
    }
}