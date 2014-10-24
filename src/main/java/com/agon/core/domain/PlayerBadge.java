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

import java.util.Date;
import java.util.UUID;

public class PlayerBadge {
    private long playerId;
    private Date unlocked;
    private UUID badgeId;

    private PlayerBadge(Builder builder) {
        playerId = builder.playerId;
        unlocked = builder.unlocked;
        badgeId = builder.badgeId;
    }

    public static final class Builder {
        private long playerId;
        private Date unlocked;
        private UUID badgeId;

        public Builder() {
        }

        public Builder playerId(long playerId) {
            this.playerId = playerId;
            return this;
        }

        public Builder unlocked(Date unlocked) {
            this.unlocked = unlocked;
            return this;
        }

        public Builder badgeId(UUID badgeId) {
            this.badgeId = badgeId;
            return this;
        }

        public PlayerBadge build() {
            return new PlayerBadge(this);
        }
    }

    public long getPlayerId() {
        return playerId;
    }

    public Date getUnlocked() {
        return unlocked;
    }

    public UUID getBadgeId() {
        return badgeId;
    }
}
