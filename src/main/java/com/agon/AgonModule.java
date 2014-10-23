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

import com.agon.core.repository.ActionRepository;
import com.agon.core.repository.PlayerRepository;
import com.agon.core.repository.cassandra.CassandraActionRepository;
import com.agon.core.repository.cassandra.CassandraPlayerRepository;
import com.datastax.driver.core.Cluster;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.netflix.governator.guice.lazy.LazySingleton;
import io.dropwizard.setup.Environment;


public class AgonModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ActionRepository.class).to(CassandraActionRepository.class);
        bind(PlayerRepository.class).to(CassandraPlayerRepository.class);
    }

    @Provides
    @Named("keyspace")
    public String provideKeyspace(AgonConfiguration configuration) {
        return configuration.getCassandraFactory().getKeyspace();
    }

    @Provides
    @LazySingleton
    public Cluster provideCluster(AgonConfiguration configuration, Environment environment) {
        return  configuration.getCassandraFactory().build(environment);
    }
}
