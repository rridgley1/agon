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

import java.util.UUID;

public class EventType {
    private UUID id;
    private String name;

    private EventType(Builder builder) {
        id = builder.id;
        name = builder.name;
    }

    public static final class Builder {
        private UUID id;
        private String name;

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

        public EventType build() {
            return new EventType(this);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
