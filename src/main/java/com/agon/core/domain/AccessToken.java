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

import java.util.Date;

@JsonDeserialize(builder = AccessToken.Builder.class)
public class AccessToken {
    @JsonProperty
    private Date created;
    @JsonProperty
    private String token;
    @JsonProperty
    private long playerId;
    @JsonProperty
    private String clientId;

    private AccessToken(Builder builder) {
        created = builder.created;
        token = builder.token;
        playerId = builder.playerId;
        clientId = builder.clientId;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private Date created;
        private String token;
        private long playerId;
        private String clientId;

        public Builder() {
        }

        public Builder created(Date created) {
            this.created = created;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder playerId(long playerId) {
            this.playerId = playerId;
            return this;
        }

        public Builder clientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public AccessToken build() {
            return new AccessToken(this);
        }
    }

    public Date getCreated() {
        return created;
    }

    public String getToken() {
        return token;
    }

    public long getPlayerId() {
        return playerId;
    }

    public String getClientId() {
        return clientId;
    }
}
