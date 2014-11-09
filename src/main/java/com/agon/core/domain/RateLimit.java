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

@JsonDeserialize(builder = RateLimit.Builder.class)
public class RateLimit {
    @JsonProperty
    private String limitKey;
    @JsonProperty
    private int total;
    @JsonProperty
    private int remaining;
    @JsonProperty
    private Date reset;

    private RateLimit(Builder builder) {
        limitKey = builder.limitKey;
        total = builder.total;
        remaining = builder.remaining;
        reset = builder.reset;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static final class Builder {
        private String limitKey;
        private int total;
        private int remaining;
        private Date reset;

        public Builder() {
        }

        public Builder limitKey(String limitKey) {
            this.limitKey = limitKey;
            return this;
        }

        public Builder total(int total) {
            this.total = total;
            return this;
        }

        public Builder remaining(int remaining) {
            this.remaining = remaining;
            return this;
        }

        public Builder reset(Date reset) {
            this.reset = reset;
            return this;
        }

        public RateLimit build() {
            return new RateLimit(this);
        }
    }

    public String getLimitKey() {
        return limitKey;
    }

    public int getTotal() {
        return total;
    }

    public int getRemaining() {
        return remaining;
    }

    public Date getReset() {
        return reset;
    }
}
