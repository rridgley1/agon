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

import java.io.Serializable;

public class LeakyBucket implements Serializable {
    private final int bucketSize;
    private final double leakRatePerMillis;
    private int spaceRemaining;
    private long lastUpdateTimeMillis;

    public LeakyBucket(int bucketSize, double leakRatePerMillis) {
        this.bucketSize = bucketSize;
        this.leakRatePerMillis = leakRatePerMillis;
        this.spaceRemaining = bucketSize;
        this.lastUpdateTimeMillis = System.currentTimeMillis();
    }

    public boolean add(int amount) {
        this.spaceRemaining = getUpdatedSpaceRemaining();

        if (this.spaceRemaining >= amount) {
            this.spaceRemaining -= amount;
            return true;
        } else {
            return false;
        }
    }

    private int getUpdatedSpaceRemaining() {
        long elapsedTime = System.currentTimeMillis() - this.lastUpdateTimeMillis;
        return Math.min(this.bucketSize,
                (int) Math.floor(this.spaceRemaining + (elapsedTime * this.leakRatePerMillis)));
    }
}
