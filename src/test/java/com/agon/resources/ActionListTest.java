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

package com.agon.resources;

import com.agon.core.domain.Action;
import com.agon.core.domain.ActionList;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.fest.assertions.Assertions.assertThat;

public class ActionListTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializesToJSON() throws Exception {
        List<Action> actions = new ArrayList<>();
        actions.add(new Action.Builder()
                .playerId(1)
                .event("COIN_OUT")
                .eventType("progressive")
                .value("1.25")
                .location("1")
                .build());
        actions.add(new Action.Builder()
                .playerId(2)
                .event("MAX_BET")
                .eventType("progressive")
                .value("1.25")
                .location("2")
                .build());
        actions.add(new Action.Builder()
                .playerId(3)
                .event("MAX_BET")
                .eventType("progressive")
                .value("1.25")
                .location("3")
                .build());
        final ActionList list = new ActionList.Builder().actions(actions).build();
        assertThat(MAPPER.writeValueAsString(list))
                .isEqualTo(fixture("fixtures/actions.json"));
    }
}
