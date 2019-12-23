package no.charlie.client;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CaptchaResponse {
    @JsonProperty("error-codes")
    public List<String> errorCodes;
    public boolean success;

    public CaptchaResponse() {
    }

}
