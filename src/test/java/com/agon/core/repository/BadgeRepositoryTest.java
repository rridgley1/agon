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

package com.agon.core.repository;

import com.datastax.driver.core.ResultSet;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.CassandraCQLUnit;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class BadgeRepositoryTest {
    @Rule
    public CassandraCQLUnit cassandraCQLUnit = new CassandraCQLUnit(new ClassPathCQLDataSet("cql/badge.cql","agon"));

    @Before
    public void before() throws IOException, InterruptedException, ConfigurationException, org.apache.cassandra.exceptions.ConfigurationException, TTransportException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra("cassandra-test.yaml");
    }

    @Test
    public void should_have_started_and_execute_cql_script() throws Exception {
        ResultSet result = cassandraCQLUnit.session.execute("select * from badges WHERE name='testBadge'");
        assertThat(result.iterator().next().getString("url"), is("/images/badge.jpg"));
    }
}
