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
import com.agon.core.repository.PlayerRepository;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.core.querybuilder.Update;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.Date;
import java.util.List;
import java.util.UUID;


public class CassandraPlayerRepository implements PlayerRepository {
    private final Session session;

    @Inject
    public CassandraPlayerRepository(@Named("agon-session") Session session) {
        this.session = session;
    }

    @Override
    public void incrementEvent(long playerId, String event, long count) {
        Update updateStatement = QueryBuilder.update("player_event_counts");
        updateStatement.where(QueryBuilder.eq("player_id", playerId)).and(QueryBuilder.eq("event", event));
        updateStatement.with(QueryBuilder.incr("counter_value", count));
        session.execute(updateStatement);
    }

    @Override
    public void unlockBadge(long playerId, UUID badgeId) {
        Insert insertStatement = QueryBuilder.insertInto(session.getLoggedKeyspace(), "player_badges")
                .value("player_id", playerId)
                .value("badge_id", badgeId)
                .value("unlocked", new Date());
        session.execute(insertStatement);
    }

    @Override
    public boolean hasEarned(long playerId, UUID badgeId) {
        Select.Where earned = QueryBuilder.select()
                .all()
                .from("player_badges")
                .where(QueryBuilder.eq("player_id", playerId))
                .and(QueryBuilder.eq("badge_id", badgeId));
        ResultSet set = session.execute(earned);
        return !set.isExhausted();
    }

    @Override
    public boolean evaluate(long playerId, Badge badge) {
        if(hasEarned(playerId, badge.getId())) return false;

        int goalsAchieved = 0;

        for (Goal goal : badge.getGoals()) {
            Select.Where eval = QueryBuilder.select().all()
                    .from("player_event_counts")
                    .where(QueryBuilder.eq("player_id", playerId))
                    .and(QueryBuilder.eq("event", goal.getEvent()));

            ResultSet resultSet = session.execute(eval);
            Row row = resultSet.one();
            if(row != null) {
                long count = row.getLong("counter_value");
                if(count >= goal.getValue() ) {
                    goalsAchieved++;
                }
            }
        }
        return goalsAchieved == badge.getGoals().size();
    }

    @Override
    public List<Badge> earnedBadges(long playerId) {
        return null;
    }
}
