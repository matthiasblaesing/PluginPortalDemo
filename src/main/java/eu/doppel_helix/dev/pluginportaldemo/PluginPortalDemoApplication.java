package eu.doppel_helix.dev.pluginportaldemo;

import eu.doppel_helix.dev.pluginportaldemo.resources.AuthenticationResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class PluginPortalDemoApplication extends Application<PluginPortalDemoConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PluginPortalDemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "PluginPortalDemo";
    }

    @Override
    public void initialize(final Bootstrap<PluginPortalDemoConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final PluginPortalDemoConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(configuration);
            }
        });
        environment.jersey().register(AuthenticationResource.class);
    }

}
