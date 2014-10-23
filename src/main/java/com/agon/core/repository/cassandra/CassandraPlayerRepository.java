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

package com.agon.core.repository.cassandra;

import com.agon.core.repository.PlayerRepository;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Update;
import com.google.inject.Inject;
import com.google.inject.name.Named;


public class CassandraPlayerRepository implements PlayerRepository {
    private final Cluster cluster;
    private final String keyspace;
    private final Session session;

    @Inject
    public CassandraPlayerRepository(Cluster cluster, @Named("keyspace") String keyspace) {
        this.cluster = cluster;
        this.keyspace = keyspace;
        session = this.cluster.connect(this.keyspace);
    }

    @Override
    public void increment(long playerId, String event, long count) {
        Update updateStatement = QueryBuilder.update("player_event_counts");
        updateStatement.where(QueryBuilder.eq("player_id", playerId)).and(QueryBuilder.eq("event", event));
        updateStatement.with(QueryBuilder.incr("counter_value", count));

        ResultSet resultSet = session.execute(updateStatement);
    }
}
