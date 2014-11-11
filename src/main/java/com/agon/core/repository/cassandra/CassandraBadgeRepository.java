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

import com.agon.core.domain.Badge;
import com.agon.core.domain.Goal;
import com.agon.core.domain.Paged;
import com.agon.core.repository.BadgeRepository;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.*;

public class CassandraBadgeRepository implements BadgeRepository {
    private final Session session;

    @Inject
    public CassandraBadgeRepository(@Named("agon-session") Session session) {
        this.session = session;
    }

    @Override
    public Collection<Badge> findByEvent(String event) {
        Map<UUID, Badge> badges = new HashMap<>();

        Select.Where select = QueryBuilder.select().distinct().column("badge_id")
                .from("badge_events")
                .where(QueryBuilder.eq("event", event));

        ResultSet set = session.execute(select);

        for(Row row:set.all()) {
            UUID badgeId = row.getUUID("badge_id");

            if(badges.get(badgeId) == null) {
                badges.put(badgeId, get(badgeId).get());
            }
        }
        return badges.values();
    }

    @Override
    public List<Goal> findGoalsByBadgeId(UUID badgeId) {
        Select.Where goalQuery = QueryBuilder.select("event", "count")
                .from("badge_events")
                .where(QueryBuilder.eq("badge_id", badgeId));
        ResultSet rows = session.execute(goalQuery);
        List<Goal> goals = new ArrayList<>();

        for (Row row : rows) {
            goals.add(new Goal.Builder()
                    .event(row.getString("event"))
                    .value(row.getInt("count"))
                    .build());
        }

        return goals;
    }

    @Override
    public void add(Badge item) {

    }

    @Override
    public void addAll(Collection<Badge> items) {

    }

    @Override
    public void delete(Badge item) {

    }

    @Override
    public void delete(Collection<Badge> items) {

    }

    @Override
    public void update(Badge item) {

    }

    @Override
    public Optional<Badge> get(UUID id) {
        Select.Where badgeQuery = QueryBuilder
                .select()
                .all()
                .from("badges")
                .where(QueryBuilder.eq("id", id));
        ResultSet set = session.execute(badgeQuery);
        Row row = set.one();

        return Optional.of(new Badge.Builder().id(row.getUUID("id"))
                .retired(row.getBool("retired"))
                .description(row.getString("description"))
                .name(row.getString("name"))
                .goals(findGoalsByBadgeId(id))
                .url(row.getString("url"))
                .build());
    }
}
