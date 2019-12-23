package no.charlie;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import no.charlie.api.DeltakerService;
import no.charlie.api.HendelseResource;
import no.charlie.api.HendelseService;
import no.charlie.api.SlackResource;
import no.charlie.api.Validator;
import no.charlie.client.CaptchaClient;
import no.charlie.client.CaptchaValidator;
import no.charlie.client.SlackClient;
import no.charlie.client.SlackService;
import no.charlie.db.DeltakerDAO;
import no.charlie.db.HendelseDAO;

import java.util.Arrays;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.jdbi.v3.core.Jdbi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaameldingApplication extends Application<PaameldingConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaameldingApplication.class);

    public static void main(final String[] args) throws Exception {
        new PaameldingApplication().run("server", "config.yml");
    }

    @Override
    public String getName() {
        return "paamelding";
    }

    @Override
    public void initialize(final Bootstrap<PaameldingConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

    }

    @Override
    public void run(final PaameldingConfiguration configuration,
                    final Environment environment) {
        DataSourceFactory dataSourceFactory = configuration.getDataSourceFactory();
        ManagedDataSource dataSource = dataSourceFactory.build(environment.metrics(), "db");

        SlackClient slackClient = SlackClient.build("https://hooks.slack.com", environment.getObjectMapper());
        CaptchaClient captchaClient = CaptchaClient.build("https://www.google.com/recaptcha/api/siteverify", environment.getObjectMapper());


        LOGGER.info("Migrerer database");
//        configuration.getFlywayFactory().build(dataSource).migrate();

        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, dataSourceFactory, "db");

        final HendelseDAO hendelseDao = jdbi.onDemand(HendelseDAO.class);
        final DeltakerDAO deltakerDao = jdbi.onDemand(DeltakerDAO.class);
        final SlackService slackService = new SlackService(slackClient, configuration.getFotballSlack(),
                configuration.getInnebandySlack(), configuration.getVolleyballSlack());
        final CaptchaValidator captchaValidator = new CaptchaValidator(captchaClient, configuration.getCaptchaSecret());
        final HendelseService hendelseService = new HendelseService(hendelseDao, deltakerDao);
        final DeltakerService deltakerService = new DeltakerService(deltakerDao, hendelseService, slackService);
        final HendelseResource hendelseResource = new HendelseResource(hendelseService, deltakerService, new Validator(captchaValidator));
        final SlackResource slackResource = new SlackResource(slackService, hendelseService, configuration.getMagicHeader());
        environment.getObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        environment.getObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        environment.jersey().register(hendelseResource);
        environment.jersey().register(slackResource);

    }
}
