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

public class ActionResult {
    private List<Result> results;

    private ActionResult(Builder builder) {
        results = builder.results;
    }

    public static final class Builder {
        private List<Result> results;

        public Builder() {
        }

        public Builder results(List<Result> results) {
            this.results = results;
            return this;
        }

        public ActionResult build() {
            return new ActionResult(this);
        }
    }

    public List<Result> getResults() {
        return results;
    }
}
