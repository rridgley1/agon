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

import com.agon.core.exceptions.RateLimitExceededException;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import net.spy.memcached.MemcachedClient;


import static com.codahale.metrics.MetricRegistry.name;

public class RateLimiter {
    private final Meter meter;
    private final MemcachedClient memcachedClient;
    private final String name;
    private final int bucketSize;
    private final double leakRatePerMillis;

    public RateLimiter(MemcachedClient memcachedClient, String name,
                       int bucketSize, double leakRatePerMinute) {
        MetricRegistry metricRegistry = SharedMetricRegistries.getOrCreate("agon");
        this.meter = metricRegistry.meter(name(getClass(), name, "exceeded"));
        this.memcachedClient = memcachedClient;
        this.name = name;
        this.bucketSize = bucketSize;
        this.leakRatePerMillis = leakRatePerMinute / (60.0 * 1000.0);
    }

    public void validate(String key, int amount) throws RateLimitExceededException {
        LeakyBucket bucket = getBucket(key);

        if (bucket.add(amount)) {
            setBucket(key, bucket);
        } else {
            meter.mark();
            throw new RateLimitExceededException(key + " , " + amount);
        }
    }

    public void validate(String key) throws RateLimitExceededException {
        validate(key, 1);
    }

    private void setBucket(String key, LeakyBucket bucket) {
        memcachedClient.set(getBucketName(key),
                (int) Math.ceil((bucketSize / leakRatePerMillis) / 1000), bucket);
    }

    private LeakyBucket getBucket(String key) {
        LeakyBucket bucket = (LeakyBucket) memcachedClient.get(getBucketName(key));
        if (bucket == null) {
            return new LeakyBucket(bucketSize, leakRatePerMillis);
        } else {
            return bucket;
        }
    }

    private String getBucketName(String key) {
        return LeakyBucket.class.getSimpleName() + name + key;
    }
}
