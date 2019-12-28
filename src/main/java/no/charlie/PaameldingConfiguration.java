package no.charlie;

import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.flyway.FlywayFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PaameldingConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    private String fotballSlack;
    private String innebandySlack;
    private String volleyballSlack;
    private String captchaSecret;
    private String magicHeader;

    private SwaggerBundleConfiguration swaggerBundleConfiguration = new SwaggerBundleConfiguration();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }


    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return new JerseyClientConfiguration();
    }
    public FlywayFactory getFlywayFactory() {
        FlywayFactory flywayFactory = new FlywayFactory();
        return flywayFactory;
    }

    public String getFotballSlack() {
        return fotballSlack;
    }

    public String getInnebandySlack() {
        return innebandySlack;
    }

    public String getVolleyballSlack() {
        return volleyballSlack;
    }

    public String getCaptchaSecret() {
        return captchaSecret;
    }

    public String getMagicHeader() {
        return magicHeader;
    }

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        swaggerBundleConfiguration.setResourcePackage("no.charlie");
        return swaggerBundleConfiguration;
    }
}
