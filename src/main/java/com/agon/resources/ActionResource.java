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

import com.agon.core.domain.*;
import com.agon.core.events.ActionListEvent;
import com.agon.core.limits.Limiter;
import com.agon.core.service.BadgeService;
import com.agon.core.service.ActionService;
import com.agon.core.versioning.ApiVersion;
import com.codahale.metrics.annotation.Timed;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/actions")
@Api(value = "/actions", description = "Operations for actions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ActionResource {

    private final ActionService actionService;
    private final BadgeService badgeService;
    private final EventBus bus;

    @Inject
    public ActionResource(ActionService actionService, BadgeService badgeService, EventBus bus) {
        this.actionService = actionService;
        this.badgeService = badgeService;
        this.bus = bus;
    }

    @POST
    @Timed
    @ApiOperation(value = "Post actions to the gamification engine for evaluation",
                  response = ActionResult.class)
    @ApiVersion(minVersion = 1)
    //@Limiter(requests = 1000)
    public Response post(ActionList actions) {
        bus.post(new ActionListEvent(actions));
//        //batchAdd all the actions to the db
//        actionService.batchAdd(actions);
        return Response.ok().entity(
                badgeService.evaluate(
                        actionService.buildEvaluations(actions)))
                .build();
    }
}
