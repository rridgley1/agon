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

package com.agon.resources;

import com.agon.core.limits.Limiter;
import com.agon.core.service.BadgeService;
import com.agon.core.versioning.ApiVersion;
import com.codahale.metrics.annotation.Timed;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;


@Path("/badges")
@Api(value = "/badges", description = "Operations for badges")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BadgeResource {
    private final BadgeService badgeService;

    @Inject
    public BadgeResource(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    @GET
    @Path("/:id")
    @Timed
    @ApiOperation(value = "Get all badges or a badge by id", response = Response.class)
    @ApiVersion(minVersion = 1)
    @Limiter(requests = 1000)
    public Response get(@PathParam("id") UUID id) {
        return Response.ok().build();
    }
}
