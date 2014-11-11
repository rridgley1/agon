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

import com.agon.core.repository.*;
import com.agon.core.repository.cassandra.*;
import com.codahale.metrics.MetricRegistry;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.netflix.governator.guice.lazy.LazySingleton;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;


public class AgonModule extends AbstractModule {
    private final Logger logger = LoggerFactory.getLogger(AgonModule.class);
    private EventBus eventBus;

    @Override
    protected void configure() {
        bind(ActionRepository.class).to(CassandraActionRepository.class);
        bind(PlayerRepository.class).to(CassandraPlayerRepository.class);
        bind(BadgeRepository.class).to(CassandraBadgeRepository.class);
        bind(EventRepository.class).to(CassandraEventRepository.class);
        bind(EventTypeRepository.class).to(CassandraEventTypeRepository.class);

        this.eventBus =  new AsyncEventBus(java.util.concurrent.Executors.newCachedThreadPool());
        bind(EventBus.class).toInstance(eventBus);

        bindListener(Matchers.any(), new TypeListener() {
            public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                typeEncounter.register(new InjectionListener<I>() {
                    public void afterInjection(I i) {
                        eventBus.register(i);
                    }
                });
            }
        });
    }

    @Provides
    @Named("agon-session")
    @LazySingleton
    public Session provideSession(Cluster cluster, AgonConfiguration configuration) {
        return cluster.connect(configuration.getCassandraFactory().getKeyspace());
    }

    @Provides
    @LazySingleton
    public Cluster provideCluster(AgonConfiguration configuration, Environment environment) {
        return configuration.getCassandraFactory().build(environment);
    }

    @Provides
    @Named("metrics")
    public MetricRegistry provideRegistry(Environment environment) {
        return environment.metrics();
    }


}
