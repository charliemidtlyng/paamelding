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
        "Accept: application/json; charset=utf-8",
        "Content-Type: application/json; charset=utf-8"
})
public interface NifClient {

    @RequestLine("GET ?sfid=354&orgDistrictId=10307&teamIds=723658,696999&autoSearch=true&showJsonData=true&showSearchPane=false&showInfoPane=false&showPager=false&defaultColors=true&noUrl=true&showHeader=false&showGenerateUrl=false&showToggleSearch=false&showPrintButton=false&orderAsc=false&FromDate={fromDate}&ToDate={toDate}&pageSize=50")
    String hentFotballKamper(@Param("fromDate") String fraDato, @Param("toDate") String tilDato);

    @RequestLine("GET ?sfid=354&orgDistrictId=10307&teamId=861667&autoSearch=true&showJsonData=true&showSearchPane=false&showInfoPane=false&showPager=false&defaultColors=true&noUrl=true&showHeader=false&showGenerateUrl=false&showToggleSearch=false&showPrintButton=false&orderAsc=false&FromDate={fromDate}&ToDate={toDate}&pageSize=50")
    String hentVolleyballKamper(@Param("fromDate") String fraDato, @Param("toDate") String tilDato);

    static NifClient build(ObjectMapper om) {
        final String baseUrl = "https://wp.nif.no/PageMatchAvansert.aspx";
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
                .target(NifClient.class, baseUrl);

    }
}