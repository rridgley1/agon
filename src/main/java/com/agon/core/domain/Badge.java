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

@JsonDeserialize(builder = Badge.Builder.class)
public class Badge {
    @JsonProperty
    private UUID id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String url;
    @JsonProperty
    private String description;
    @JsonProperty
    private boolean retired;
    @JsonProperty
    private List<Goal> goals;

    private Badge(Builder builder) {
        id = builder.id;
        name = builder.name;
        url = builder.url;
        description = builder.description;
        retired = builder.retired;
        goals = builder.goals;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private UUID id;
        private String name;
        private String url;
        private String description;
        private boolean retired;
        private List<Goal> goals;

        public Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder retired(boolean retired) {
            this.retired = retired;
            return this;
        }

        public Builder goals(List<Goal> goals) {
            this.goals = goals;
            return this;
        }

        public Badge build() {
            return new Badge(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRetired() {
        return retired;
    }

    public List<Goal> getGoals() {
        return goals;
    }
}
