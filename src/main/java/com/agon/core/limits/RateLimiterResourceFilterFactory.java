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

package com.agon.core.limits;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.*;

import java.util.Collections;
import java.util.List;

public class RateLimiterResourceFilterFactory implements ResourceFilterFactory {
    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        List<ResourceFilter> response = null;
        boolean hasLimiterAnnotation = am.isAnnotationPresent(Limiter.class);

        if (hasLimiterAnnotation) {
            Limiter annotation = am.getAnnotation(Limiter.class);
            long requests = annotation.requests();
            response = Collections.<ResourceFilter>singletonList(new RateLimitFilter(requests, am.getMethod().getName()));
        }
        return response;
    }

    private class RateLimitFilter implements ResourceFilter, ContainerRequestFilter {
        private final long requests;
        private final String methodName;

        public RateLimitFilter(long requests, String methodName) {
            this.requests = requests;
            this.methodName = methodName;
        }

        @Override
        public ContainerRequest filter(ContainerRequest request) {
            String endpoint = request.getMethod() + "-" + request.getRequestUri();
            return request;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return this;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }
    }
}
