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

import java.util.List;
import java.util.UUID;

@JsonDeserialize(builder = Paged.Builder.class)
public class Paged<T> {
    @JsonProperty
    private UUID startToken;
    @JsonProperty
    private UUID endToken;
    @JsonProperty
    private List<T> items;

    private Paged(Builder<T> builder) {
        startToken = builder.startToken;
        endToken = builder.endToken;
        items = builder.items;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder<T> {
        private UUID startToken;
        private UUID endToken;
        private List<T> items;

        public Builder() {
        }

        public Builder startToken(UUID startToken) {
            this.startToken = startToken;
            return this;
        }

        public Builder endToken(UUID endToken) {
            this.endToken = endToken;
            return this;
        }

        public Builder items(List<T> items) {
            this.items = items;
            return this;
        }

        public Paged build() {
            return new Paged(this);
        }
    }

    public UUID getStartToken() {
        return startToken;
    }

    public UUID getEndToken() {
        return endToken;
    }

    public List<T> getItems() {
        return items;
    }
}