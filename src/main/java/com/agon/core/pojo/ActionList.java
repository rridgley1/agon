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

package com.agon.core.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.List;

@JsonDeserialize(builder = ActionList.Builder.class)
public class ActionList {
    @JsonProperty("actions")
    private List<Action> actions;

    private ActionList(Builder builder) {
        actions = builder.actions;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private List<Action> actions;

        public Builder() {
        }

        public Builder actions(List<Action> actions) {
            this.actions = actions;
            return this;
        }

        public ActionList build() {
            return new ActionList(this);
        }
    }

    public List<Action> getActions() {
        return actions;
    }
}
