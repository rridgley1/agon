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

public class Evaluation {
    private long playerId;
    private String event;
    private long count;

    private Evaluation(Builder builder) {
        playerId = builder.playerId;
        event = builder.event;
        count = builder.count;
    }

    public void incrementCount() {
        count++;
    }

    public static final class Builder {
        private long playerId;
        private String event;
        private long count;

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

        public Builder count(long count) {
            this.count = count;
            return this;
        }

        public Evaluation build() {
            return new Evaluation(this);
        }
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getEvent() {
        return event;
    }

    public long getCount() {
        return count;
    }
}
