/*
 * Creedengo Dashboard plugin - Provides dashboard to monitor and help reduce the environmental footprint of your programs
 * Copyright © 2025 Green Code Initiative (https://green-code-initiative.org)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.greencodeinitiative.creedengo.confluence.plugin.sonar;

import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarIssuesApi;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarMeasuresApi;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarProjectBranchesApi;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarProjectInfoApi;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarProjectPullRequestsApi;
import org.greencodeinitiative.creedengo.confluence.plugin.sonar.api.SonarRulesApi;

/**
 * Facade providing access to all SonarQube API services.
 *
 * <p>Usage from the Confluence macro:</p>
 * <pre>{@code
 * SonarService sonar = new SonarService(sonarqubeUrl, sonarqubeToken);
 * List<JsonObject> issues = sonar.issues().findIssues(projectKey, branch);
 * long ncloc = sonar.measures().getNumberOfLineOfCode(projectKey, branch);
 * }</pre>
 */
public class SonarService {

    private final SonarClient client;
    private final SonarIssuesApi issuesApi;
    private final SonarMeasuresApi measuresApi;
    private final SonarRulesApi rulesApi;
    private final SonarProjectBranchesApi branchesApi;
    private final SonarProjectInfoApi projectInfoApi;
    private final SonarProjectPullRequestsApi pullRequestsApi;

    /**
     * Create a SonarService with the given SonarQube URL and authentication token.
     *
     * @param sonarqubeUrl   the SonarQube server base URL
     * @param sonarqubeToken the authentication token (Bearer)
     */
    public SonarService(String sonarqubeUrl, String sonarqubeToken) {
        this.client = new SonarClient(sonarqubeUrl, sonarqubeToken);
        this.issuesApi = new SonarIssuesApi(client);
        this.measuresApi = new SonarMeasuresApi(client);
        this.rulesApi = new SonarRulesApi(client);
        this.branchesApi = new SonarProjectBranchesApi(client);
        this.projectInfoApi = new SonarProjectInfoApi(client);
        this.pullRequestsApi = new SonarProjectPullRequestsApi(client);
    }

    /** @return the Issues Search API */
    public SonarIssuesApi issues() {
        return issuesApi;
    }

    /** @return the Measures Component API */
    public SonarMeasuresApi measures() {
        return measuresApi;
    }

    /** @return the Rules Search/Show API */
    public SonarRulesApi rules() {
        return rulesApi;
    }

    /** @return the Project Branches API */
    public SonarProjectBranchesApi branches() {
        return branchesApi;
    }

    /** @return the Project Info API */
    public SonarProjectInfoApi projectInfo() {
        return projectInfoApi;
    }

    /** @return the Project Pull Requests API */
    public SonarProjectPullRequestsApi pullRequests() {
        return pullRequestsApi;
    }

    /** @return the underlying HTTP client (for advanced usage) */
    public SonarClient getClient() {
        return client;
    }
}
