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
import com.agon.core.domain.Evaluation;
import com.agon.core.repository.ActionRepository;
import com.google.inject.Inject;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class ActionService {

    private final ActionRepository actionRepository;

    @Inject
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public void add(ActionList actions) {
        actionRepository.add(actions.getActions());
    }

    public Collection<Set<Evaluation>> buildEvaluations(ActionList actions) {
        Hashtable<Long, Set<Evaluation>> evaluations = new Hashtable<>();

        for (Action action : actions.getActions()) {
            Set<Evaluation> evals = evaluations.get(action.getPlayerId());
            if (evals == null) {
                evals = new HashSet<>();
                evals.add(new Evaluation.Builder()
                        .event(action.getEvent())
                        .playerId(action.getPlayerId())
                        .build());
                evaluations.put(action.getPlayerId(), evals);
            }

            for (Evaluation eval : evals) {
                if (eval.getEvent().equals(action.getEvent())) {
                    eval.incrementCount();
                }
            }
        }
        return evaluations.values();
    }
}
