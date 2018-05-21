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

package com.flow.platform.api.git;

import com.flow.platform.api.domain.Flow;
import com.flow.platform.api.envs.GitEnvs;
import com.flow.platform.util.git.GitClient;
import com.flow.platform.util.git.GitException;
import com.flow.platform.util.git.GitLabClient;
import java.nio.file.Path;

/**
 * @author yang
 */
public class GitLabClientBuilder extends GitClientBuilder {

    private final String host;

    private final String token;

    private final String project;

    public GitLabClientBuilder(Flow flow, Path sourceFolder) {
        super(flow, sourceFolder);
        host = flow.getEnv(GitEnvs.FLOW_GIT_URL);
        token = flow.getEnv(GitEnvs.FLOW_GITLAB_TOKEN);
        project = flow.getEnv(GitEnvs.FLOW_GITLAB_PROJECT);
    }

    @Override
    public GitClient build() throws GitException {
        return new GitLabClient(host, token, project);
    }
}
