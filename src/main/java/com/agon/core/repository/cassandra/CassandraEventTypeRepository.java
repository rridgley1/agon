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

import com.agon.core.domain.EventType;
import com.agon.core.domain.Paged;
import com.agon.core.repository.EventTypeRepository;
import com.datastax.driver.core.Session;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class CassandraEventTypeRepository implements EventTypeRepository {
    private final Session session;

    @Inject
    public CassandraEventTypeRepository(@Named("agon-session") Session session) {
        this.session = session;
    }

    @Override
    public void add(EventType item) {

    }

    @Override
    public void addAll(Collection<EventType> items) {

    }

    @Override
    public void delete(EventType item) {

    }

    @Override
    public void delete(Collection<EventType> items) {

    }

    @Override
    public void update(EventType item) {

    }

    @Override
    public Optional<EventType> get(UUID id) {
        return null;
    }

    @Override
    public Iterator<EventType> getAll(Optional<Integer> limit) {
        return null;
    }

    @Override
    public Paged<EventType> getAllPaged(Long startToken, Integer limit) {
        return null;
    }
}
