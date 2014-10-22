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

@JsonDeserialize(builder = Action.Builder.class)
public class Action {
    @JsonProperty
    private long playerId;
    @JsonProperty
    private String event;
    @JsonProperty
    private String eventType;
    @JsonProperty
    private String value;

    private Action(Builder builder) {
        playerId = builder.playerId;
        event = builder.event;
        eventType = builder.eventType;
        value = builder.value;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private long playerId;
        private String event;
        private String eventType;
        private String value;

        public Builder() {
        }

        public Builder playerId(long playerId) {
            this.playerId = playerId;
            return this;
        }

        public Builder event(String event) {
            this.event = event;
            return this;
        }

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Action build() {
            return new Action(this);
        }
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getEvent() {
        return event;
    }

    public String getEventType() {
        return eventType;
    }

    public String getValue() {
        return value;
    }
}
