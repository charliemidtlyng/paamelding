package no.charlie.client;

import feign.Feign;
import feign.Headers;
import feign.Logger;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import okhttp3.ConnectionPool;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Headers({
        "Accept: application/json; charset=utf-8",
        "Content-Type: application/json; charset=utf-8"
})
public interface CaptchaClient {

    @RequestLine("GET ?secret={secret}&response={response}&remoteip={remoteip}")
    CaptchaResponse sjekkGyldigCaptcha(
            @Param("secret") String secret,
            @Param("response") String response,
            @Param("remoteip") String remoteip
    );

    static CaptchaClient build(String baseUrl, ObjectMapper om) {
        final okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(10, 3000, TimeUnit.MILLISECONDS))
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .build();

        final OkHttpClient okHttpClient = new OkHttpClient(client);


        return Feign.builder()
                .client(okHttpClient)
                .decoder(new JacksonDecoder(om))
                .encoder(new JacksonEncoder(om))
                .logLevel(Logger.Level.FULL)
                .target(CaptchaClient.class, baseUrl);

    }
}
