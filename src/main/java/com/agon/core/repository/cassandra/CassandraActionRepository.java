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

import com.agon.core.domain.Action;
import com.agon.core.domain.Paged;
import com.agon.core.repository.ActionRepository;
import com.codahale.metrics.annotation.Timed;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class CassandraActionRepository implements ActionRepository {
    private final Session session;

    @Inject
    public CassandraActionRepository(@Named("agon-session") Session session) {
        this.session = session;
    }

    @Override
    public void add(Action item) {

    }

    @Override
    public void addAll(Collection<Action> items) {
        BatchStatement batchStatement = new BatchStatement();
        String[] names = {"player_id", "event", "event_type", "event_time", "value"};

        for (Action a : items) {
            Object[] values = {a.getPlayerId(), a.getEvent(), a.getEventType(), UUIDs.timeBased(), a.getValue()};
            Statement statement = QueryBuilder
                    .insertInto(session.getLoggedKeyspace(), "actions")
                    .values(names, values);
            batchStatement.add(statement);
        }
        session.execute(batchStatement);
    }

    @Override
    public void delete(Action item) {

    }

    @Override
    public void delete(Collection<Action> items) {

    }

    @Override
    public void update(Action item) {

    }

    @Override
    public Optional<Action> get(UUID id) {
        return null;
    }

    @Override
    public Iterator<Action> getAll(Optional<Integer> limit) {
        return null;
    }

    @Override
    public Paged<Action> getAllPaged(Long startToken, Integer limit) {
        return null;
    }
}
