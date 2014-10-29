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

package com.agon.core.service;

import com.agon.core.domain.*;
import com.agon.core.repository.BadgeRepository;
import com.agon.core.repository.PlayerRepository;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.google.inject.Inject;

import java.util.*;

public class AchievementService {
    private final PlayerRepository playerRepository;
    private final BadgeRepository badgeRepository;
    @Inject
    public AchievementService(PlayerRepository playerRepository, BadgeRepository badgeRepository) {
        this.playerRepository = playerRepository;
        this.badgeRepository = badgeRepository;
    }


    public ActionResult evaluate(Collection<Set<Evaluation>> evaluations) {
        List<Result> results = new ArrayList<>();

        for (Set<Evaluation> evaluation : evaluations) {
            List<Badge> earned = new ArrayList<>();
            long playerId = 0;

            for (Evaluation e : evaluation) {
                if(playerId == 0) playerId = e.getPlayerId();
                playerRepository.incrementEvent(e.getPlayerId(), e.getEvent(), e.getCount());

                Collection<Badge> badges = badgeRepository.findByEvent(e.getEvent());

                for (Badge badge : badges) {
                    boolean unlocked = playerRepository.evaluate(e.getPlayerId(), badge);
                    if(unlocked) {
                        playerRepository.unlockBadge(e.getPlayerId(), badge.getId());
                        earned.add(badge);
                    }
                }
            }

            if(earned.size() > 0) {
                results.add(new Result.Builder().badges(earned).playerId(playerId).build());
            }
        }

        return new ActionResult.Builder().results(results).build();
    }
}