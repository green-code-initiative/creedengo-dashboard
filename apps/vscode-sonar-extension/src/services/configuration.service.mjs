import vscode from '../compat/vscode.cjs'

import core from '@creedengo/core-services';

const { workspace, Uri } = vscode;
const { api } = core

/**
 * @typedef {Object} CreedengoAuthSettings
 * @property {string} server A Sonar Server Url
 * @property {string} token A Sonar Authentication Token
 * @property {string} [projectKey] A Sonar defined project identifier (look for sonar properties if available)
 */


/* --------------------------------------------------------------------------------------------
 * Code Extract Adapted from SonarLint for VisualStudio Code
 * Copyright (C) 2017-2025 SonarSource SA
 * sonarlint@sonarsource.com
 * Licensed under the LGPLv3 License.
 * ------------------------------------------------------------------------------------------ */
const SONARLINT_CATEGORY = 'sonarlint';
const CONNECTIONS_SECTION = 'connectedMode.connections';
const SONARQUBE = 'sonarqube';
const SONARCLOUD = 'sonarcloud';
const SONARQUBE_CONNECTIONS_CATEGORY = `${SONARLINT_CATEGORY}.${CONNECTIONS_SECTION}.${SONARQUBE}`;
const SONARCLOUD_CONNECTIONS_CATEGORY = `${SONARLINT_CATEGORY}.${CONNECTIONS_SECTION}.${SONARCLOUD}`;

/**
 * @param {string} region 
 * @returns {'EU'|'US'}
 */
function sanitizeSonarCloudRegionSetting(region) {
    return region === 'us' ? 'US' : 'EU'
}
/**
 * @returns {[{connectionId: string, serverUrl: string, token: string}]}
 */
function getSonarQubeConnections() {
    return workspace.getConfiguration(SONARLINT_CATEGORY).get(`${CONNECTIONS_SECTION}.${SONARQUBE}`);
}
/**
 * @returns {[{connectionId: string, region: 'EU'|'US', organizationKey: string, token: string}]}
 */
function getSonarCloudConnections() {
    const connections = workspace.getConfiguration(SONARLINT_CATEGORY).get(`${CONNECTIONS_SECTION}.${SONARCLOUD}`);
    return connections.map(c => ({ ...c, region: sanitizeSonarCloudRegionSetting(c.region) }));
}
/* --------------------------------------------------------------------------------------------
 * End of Extract
 * ------------------------------------------------------------------------------------------ */

/**
 * @returns {CreedengoAuthSettings}
 */
function getSonarlintConfiguration() {
    const [{ serverUrl, token }] = getSonarQubeConnections()
    return { server: serverUrl, token }
}

/**
 * Retrieves the projectKey from the sonar-project.properties file
 * 
 * @returns {Promise<{ projectKey?: string}>}
 */
async function readSonarProjectProperties() {
    let projectKey; 
    const [rootFolder] = workspace.workspaceFolders;
    if (!rootFolder) {
        return {}
    }
    const sonarFileUri = Uri.joinPath(rootFolder.uri, 'sonar-project.properties');
    let file;
    try {
        file = await workspace.openTextDocument(sonarFileUri);
    } catch {
        return {}
    }
    const text = file.getText();
    const lines = text.split('\n');
    lines.some(line => {
        const [ keyPath, value ] = line.split('=').map(str => str.trim());
        if (keyPath !== 'sonar.projectKey') {
            return false
        }
        projectKey = value
        return true
    })
    return { projectKey };
}

/**
 * @returns {CreedengoAuthSettings}
 */
function readCreedengoVsCodeConfiguration() {
    const CONFIGURATION_CATEGORY = 'creedengo'
    const configuration = workspace.getConfiguration(CONFIGURATION_CATEGORY);
    const result = {};
    result.server = configuration.get('sonarUrl', '');
    result.token = configuration.get('sonarToken', '');
    return result;
}

/**
 * @returns {Promise<CreedengoAuthSettings>}
 */
async function getSonarConfiguration() {
    const configuration = readCreedengoVsCodeConfiguration()
    const properties = await readSonarProjectProperties()
    Object.assign(configuration, properties)
    return configuration;
}

/**
 * Update Creedengo API settings and return updated ProjectKey
 * 
 * @returns {Promise<{ ready:boolean, project: string}>}
 */
export async function sync() {
    const { server, token, projectKey } = await getSonarConfiguration()
    api.services.initSettings({ server, token })
    return { ready: Boolean(server && token && projectKey), project: projectKey };
}