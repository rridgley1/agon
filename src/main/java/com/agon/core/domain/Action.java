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

package com.agon.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = Action.Builder.class)
public class Action {
    @JsonProperty
    private int playerId;
    @JsonProperty
    private String event;
    @JsonProperty
    private String eventType;
    @JsonProperty
    private String value;
    @JsonProperty
    private String location;

    private Action(Builder builder) {
        playerId = builder.playerId;
        event = builder.event;
        eventType = builder.eventType;
        value = builder.value;
        location = builder.location;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private int playerId;
        private String event;
        private String eventType;
        private String value;
        private String location;

        public Builder() {
        }

        public Builder playerId(int playerId) {
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

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Action build() {
            return new Action(this);
        }
    }

    public int getPlayerId() {
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

    public String getLocation() {
        return location;
    }
}
