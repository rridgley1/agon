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

import java.util.List;

public class Result {
    private long playerId;
    private List<Badge> badges;

    private Result(Builder builder) {
        playerId = builder.playerId;
        badges = builder.badges;
    }

    public static final class Builder {
        private long playerId;
        private List<Badge> badges;

        public Builder() {
        }

        public Builder playerId(long playerId) {
            this.playerId = playerId;
            return this;
        }

        public Builder badges(List<Badge> badges) {
            this.badges = badges;
            return this;
        }

        public Result build() {
            return new Result(this);
        }
    }

    public long getPlayerId() {
        return playerId;
    }

    public List<Badge> getBadges() {
        return badges;
    }
}
