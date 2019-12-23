package no.charlie.client;

import feign.Feign;
import feign.Headers;
import feign.Logger;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import okhttp3.ConnectionPool;

import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

@Headers({
        "Accept: text/html",
        "Content-Type: application/json; charset=utf-8"
})
public interface SlackClient {

    @RequestLine("POST /services/{kanal}")
    String postTilSlackkanal(@Param("kanal") String kanal, SlackMelding slackMelding);

    static SlackClient build(String baseUrl, ObjectMapper om) {
        final okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(10, 3000, TimeUnit.MILLISECONDS))
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .build();

        final OkHttpClient okHttpClient = new OkHttpClient(client);


        return Feign.builder()
                .client(okHttpClient)
//                .decoder(new JacksonDecoder(om))
                .encoder(new JacksonEncoder(om))
                .logLevel(Logger.Level.FULL)
                .target(SlackClient.class, baseUrl);

    }
}
