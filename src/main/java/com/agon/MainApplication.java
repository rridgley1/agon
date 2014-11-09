/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.agon;

import com.agon.core.guice.GuiceBundle;
import com.agon.core.limits.RateLimitBundle;
import com.agon.core.limits.RateLimitExceededExceptionMapper;
import com.agon.core.limits.RateLimitResponseFilter;
import com.agon.core.versioning.ApiVersionBundle;
import com.agon.resources.*;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class MainApplication extends Application<AgonConfiguration> {
    public static void main(String[] args) throws Exception {
        new MainApplication().run(args);
    }

    private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();
    private final ApiVersionBundle<AgonConfiguration> apiVersionBundle = new ApiVersionBundle<>();
    private final RateLimitBundle<AgonConfiguration> rateLimitBundle = new RateLimitBundle<>();

    @Override
    public void initialize(Bootstrap<AgonConfiguration> bootstrap) {
        GuiceBundle<AgonConfiguration> guiceBundle = GuiceBundle.<AgonConfiguration>newBuilder()
                .addModule(new AgonModule())
                .setConfigClass(AgonConfiguration.class)
                .build();

        bootstrap.addBundle(guiceBundle);
        swaggerDropwizard.onInitialize(bootstrap);
        bootstrap.addBundle(apiVersionBundle);
        //bootstrap.addBundle(rateLimitBundle);
    }

    @Override
    public void run(AgonConfiguration configuration, Environment environment) throws Exception {
        // CORS support
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("Cross Origin Filter", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Authorization,X-Requested-With,Content-Type,Accept,Origin");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        environment.jersey().register(ActionResource.class);
        environment.jersey().register(BadgeResource.class);
        environment.jersey().register(EventResource.class);
        environment.jersey().register(EventTypeResource.class);
        environment.jersey().register(OAuthResource.class);

        environment.jersey().register(new RateLimitExceededExceptionMapper());
        swaggerDropwizard.onRun(configuration, environment, "localhost");
    }
}
