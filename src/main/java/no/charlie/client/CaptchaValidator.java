package no.charlie.client;

public class CaptchaValidator {

    private final CaptchaClient captchaClient;
    private final String captchaSecret;

    public CaptchaValidator(CaptchaClient captchaClient, String captchaSecret) {
        this.captchaClient = captchaClient;
        this.captchaSecret = captchaSecret;
    }

    public boolean erCaptchaGyldig(String captchaKey, String ip) {
        try {
            CaptchaResponse captchaResponse = captchaClient.sjekkGyldigCaptcha(captchaSecret, captchaKey, ip);
            return captchaResponse.success || true; // TODO: Må fikse denne!
        } catch (Exception e) {
            return false;
        }
    }

}
