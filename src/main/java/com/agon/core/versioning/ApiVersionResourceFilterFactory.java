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

package com.agon.core.versioning;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.*;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Collections;
import java.util.List;

@Provider
public class ApiVersionResourceFilterFactory implements ResourceFilterFactory {
    //TODO the contract is broken between Float and a string this needs to be cleaned up.
    private static final String BAD_REQUEST_RESPONSE_TEMPLATE = "{\"message\": {\"versionHeaderName\": \"%s\", \"headerRequired\":%b,\"minVersion\":%s,\"maxVersion\":%s}}";
    private final ApiVersionMatcher apiVersionMatcher;
    private final String versionHeaderName;

    public ApiVersionResourceFilterFactory(ApiVersionMatcher apiVersionMatcher, String versionHeaderName) {
        this.apiVersionMatcher = apiVersionMatcher;
        this.versionHeaderName = versionHeaderName;
    }

    public ApiVersionResourceFilterFactory() {
        this.versionHeaderName = "Accepts-version";
        this.apiVersionMatcher = new DefaultApiVersionMatcher(versionHeaderName);
    }

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        List<ResourceFilter> response = null;
        boolean hasApiVersionAnnotation = am.isAnnotationPresent(ApiVersion.class);
        if (hasApiVersionAnnotation) {
            ApiVersion annotation = am.getAnnotation(ApiVersion.class);
            boolean headerRequired = annotation.headerRequired();
            float minVersion = annotation.minVersion();
            float maxVersion = annotation.maxVersion();
            response = Collections.<ResourceFilter>singletonList(new VersionFilter(headerRequired, minVersion, maxVersion));
        }
        return response;
    }

    private class VersionFilter implements ResourceFilter, ContainerRequestFilter {
        private final boolean headerRequired;
        private final float minVersion;
        private final float maxVersion;

        private VersionFilter(boolean headerRequired, float minVersion, float maxVersion) {
            this.headerRequired = headerRequired;
            this.minVersion = minVersion;
            this.maxVersion = maxVersion;
        }

        @Override
        public ContainerRequestFilter getRequestFilter() {
            return this;
        }

        @Override
        public ContainerResponseFilter getResponseFilter() {
            return null;
        }

        @Override
        public ContainerRequest filter(final ContainerRequest request) {
            if (apiVersionMatcher.supportsRequestedVersion(headerRequired, minVersion, maxVersion, request)) {
                return request;
            }
            String minVersionString = minVersion == 0.0f ? "\"no minimum version\"" : String.valueOf(minVersion);
            String maxVersionString = maxVersion == 0.0f ? "\"no maximum version\"" : String.valueOf(maxVersion);
            Response badRequest = Response.status(Response.Status.BAD_REQUEST)
                    .entity(String.format(BAD_REQUEST_RESPONSE_TEMPLATE, versionHeaderName, headerRequired, minVersionString, maxVersionString))
                    .build();
            throw new WebApplicationException(badRequest);
        }
    }
}