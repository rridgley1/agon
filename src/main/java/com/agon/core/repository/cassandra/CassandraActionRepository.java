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
import com.agon.core.repository.ActionRepository;
import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.netflix.governator.guice.lazy.LazySingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.UUID;

@LazySingleton
public class CassandraActionRepository implements ActionRepository {
    private final Logger log = LoggerFactory.getLogger(CassandraActionRepository.class);
    private final Session session;
    private static final String insert = "insert into agon.actions (player_id, event, event_type, event_time, value) values (?, ?, ?, ?, ?);";
    private final PreparedStatement insertStatement;

    @Inject
    public CassandraActionRepository(@Named("agon-session") Session session) {
        this.session = session;
        insertStatement = session.prepare(insert);
    }

    @Override
    public void add(Action a) {
        BoundStatement statement = new BoundStatement(insertStatement);
        statement.bind(a.getPlayerId(), a.getEvent(), a.getEventType(), UUIDs.timeBased(), a.getValue());
        session.execute(statement);
    }

    @Override
    public void addAll(Collection<Action> items) {
        try {
            BatchStatement batchStatement = new BatchStatement();

            for (Action a : items) {
                BoundStatement statement = new BoundStatement(insertStatement);
                statement.bind(a.getPlayerId(), a.getEvent(), a.getEventType(), UUIDs.timeBased(), a.getValue());
                batchStatement.add(statement);
            }
            session.execute(batchStatement);
        } catch (Exception e) {
            log.error("Could not execute batch", e);
        }
    }

    @Override
    public void delete(Action item) {
        throw new NotImplementedException();
    }

    @Override
    public void delete(Collection<Action> items) {
        throw new NotImplementedException();
    }

    @Override
    public void update(Action item) {

    }

    @Override
    public Optional<Action> get(UUID id) {
        return null;
    }
}
