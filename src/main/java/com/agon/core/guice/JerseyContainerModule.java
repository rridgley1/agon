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

package com.agon.core.guice;

import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.*;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.spi.MessageBodyWorkers;
import com.sun.jersey.spi.container.ExceptionMapperContext;
import com.sun.jersey.spi.container.WebApplication;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Providers;

public class JerseyContainerModule extends ServletModule {
    private final GuiceContainer container;

    public JerseyContainerModule(final GuiceContainer container) {
        this.container = container;
    }

    @Override
    protected void configureServlets() {
        bind(GuiceContainer.class).toInstance(container);
    }

    @Provides
    public WebApplication webApp(GuiceContainer guiceContainer) {
        return container.getWebApplication();
    }

    @Provides
    public Providers providers(WebApplication webApplication) {
        return webApplication.getProviders();
    }

    @Provides
    public FeaturesAndProperties featuresAndProperties(WebApplication webApplication) {
        return webApplication.getFeaturesAndProperties();
    }

    @Provides
    public MessageBodyWorkers messageBodyWorkers(WebApplication webApplication) {
        return webApplication.getMessageBodyWorkers();
    }

    @Provides
    public ExceptionMapperContext exceptionMapperContext(WebApplication webApplication) {
        return webApplication.getExceptionMapperContext();
    }

    @Provides
    public ResourceContext resourceContext(WebApplication webApplication) {
        return webApplication.getResourceContext();
    }

    @RequestScoped
    @Provides
    public HttpContext httpContext(WebApplication webApplication) {
        return webApplication.getThreadLocalHttpContext();
    }

    @Provides
    @RequestScoped
    public UriInfo uriInfo(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getUriInfo();
    }

    @Provides
    @RequestScoped
    public ExtendedUriInfo extendedUriInfo(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getUriInfo();
    }

    @RequestScoped
    @Provides
    public HttpRequestContext requestContext(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getRequest();
    }

    @RequestScoped
    @Provides
    public HttpHeaders httpHeaders(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getRequest();
    }

    @RequestScoped
    @Provides
    public Request request(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getRequest();
    }

    @RequestScoped
    @Provides
    public SecurityContext securityContext(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getRequest();
    }

    @RequestScoped
    @Provides
    public HttpResponseContext responseContext(WebApplication wa) {
        return wa.getThreadLocalHttpContext().getResponse();
    }
}
