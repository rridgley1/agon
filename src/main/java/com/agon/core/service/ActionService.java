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

import com.agon.core.domain.Action;
import com.agon.core.domain.ActionList;
import com.agon.core.domain.ActionResult;
import com.agon.core.domain.Evaluation;
import com.agon.core.repository.ActionRepository;
import com.agon.core.repository.PlayerRepository;
import com.google.inject.Inject;

import java.util.*;

public class ActionService {

    private final ActionRepository actionRepository;
    private final PlayerRepository playerRepository;

    @Inject
    public ActionService(ActionRepository actionRepository, PlayerRepository playerRepository) {
        this.actionRepository = actionRepository;
        this.playerRepository = playerRepository;
    }

    public ActionResult addAndEvaluateActions(ActionList actions) {
        actionRepository.add(actions.getActions());
        Collection<Set<Evaluation>> evaluations = buildEvaluations(actions);
        for (Set<Evaluation> evaluation : evaluations) {
            for (Evaluation e : evaluation) {
                playerRepository.increment(e.getPlayerId(), e.getEvent(), e.getCount());
            }
        }
        return null;
    }

    private Collection<Set<Evaluation>> buildEvaluations(ActionList actions) {
        Hashtable<Long, Set<Evaluation>> evaluations = new Hashtable<>();

        for (Action action : actions.getActions()) {
            Set<Evaluation> evals = evaluations.get(action.getPlayerId());
            if(evals == null) {
                evals = new HashSet<>();
                evals.add(new Evaluation.Builder()
                        .event(action.getEvent())
                        .playerId(action.getPlayerId())
                        .build());
                evaluations.put(action.getPlayerId(), evals);
            }

            for (Evaluation eval : evals) {
                if(eval.getEvent().equals(action.getEvent())) {
                    eval.incrementCount();
                }
            }
        }
        return evaluations.values();
    }
}
