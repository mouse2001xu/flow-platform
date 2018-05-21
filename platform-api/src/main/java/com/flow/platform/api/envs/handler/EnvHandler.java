/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flow.platform.api.envs.handler;

import com.flow.platform.api.domain.Flow;
import com.flow.platform.api.envs.EnvKey;
import com.flow.platform.core.exception.IllegalParameterException;
import com.google.common.base.Strings;
import java.util.Collections;
import java.util.Set;

/**
 * @author yang
 */
public abstract class EnvHandler {

    /**
     * The process supported envs
     */
    public abstract EnvKey env();

    /**
     * To define the env is required
     */
    public abstract boolean isRequired();

    /**
     * The envs are depend
     */
    public Set<EnvKey> dependents() {
        return Collections.emptySet();
    }

    /**
     * Handle env variable on adding
     *
     * @throws com.flow.platform.core.exception.FlowException
     * @param flow target node
     */
    public void handle(final Flow flow) {
        String value = flow.getEnv(env());

        // check value is presented in node if it is required
        if (isRequired() && Strings.isNullOrEmpty(value)) {
            throw new IllegalParameterException("Required env '" + env() + "' is missing");
        }

        // do not process if not required and empty value
        if (!isRequired() && Strings.isNullOrEmpty(value)) {
            return;
        }

        checkEnvDependency(flow);

        onHandle(flow, value);
    }

    /**
     * Handle env variable on delete
     */
    public void unHandle(final Flow flow) {
        checkEnvDependency(flow);

        String value = flow.getEnv(env());
        onUnHandle(flow, value);
    }

    private void checkEnvDependency(Flow flow) {
        // delete dependent envs
        for (EnvKey key : dependents()) {
            if (Strings.isNullOrEmpty(flow.getEnv(key))) {
                throw new IllegalParameterException("Dependent value '" + key+ "' is missing");
            }
        }
    }

    abstract void onHandle(Flow flow, String value);

    abstract void onUnHandle(Flow flow, String value);
}
