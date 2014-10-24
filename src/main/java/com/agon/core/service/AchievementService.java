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

import com.agon.core.domain.ActionResult;
import com.agon.core.domain.Evaluation;
import com.agon.core.domain.Result;
import com.agon.core.repository.PlayerRepository;
import com.google.inject.Inject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AchievementService {
    private final PlayerRepository playerRepository;

    @Inject
    public AchievementService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public ActionResult evaluate(Collection<Set<Evaluation>> evaluations) {
        List<Result> results = new ArrayList<>();

        for (Set<Evaluation> evaluation : evaluations) {
            for (Evaluation e : evaluation) {
                playerRepository.incrementEvent(e.getPlayerId(), e.getEvent(), e.getCount());
                // evaluate all achievements that use the action event for the player in the evaluation
            }
        }
        return new ActionResult.Builder().results(results).build();
    }
}
