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
import com.agon.resources.ActionResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerDropwizard;

public class MainApplication extends Application<AgonConfiguration> {
    public static void main(String[] args) throws Exception {
        new MainApplication().run(args);
    }

    private final SwaggerDropwizard swaggerDropwizard = new SwaggerDropwizard();

    @Override
    public void initialize(Bootstrap<AgonConfiguration> bootstrap) {
        GuiceBundle<AgonConfiguration> guiceBundle = GuiceBundle.<AgonConfiguration>newBuilder()
                .addModule(new AgonModule())
                .setConfigClass(AgonConfiguration.class)
                .build();

        bootstrap.addBundle(guiceBundle);
        swaggerDropwizard.onInitialize(bootstrap);
    }

    @Override
    public void run(AgonConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(ActionResource.class);
        swaggerDropwizard.onRun(configuration, environment, "localhost");
    }
}
