import { workspace, Uri } from 'vscode';

async function readSonarProjectProperties() {
    const result = {};
    const [rootFolder] = workspace.workspaceFolders;
    if (!rootFolder) {
        return result
    }
    const sonarFileUri = Uri.joinPath(rootFolder.uri, 'sonar-project.properties');
    const file = await workspace.openTextDocument(sonarFileUri);
    const text = file.getText();
    const lines = text.split('\n');
    let left = 2;
    lines.every(line => {
        const [ keyPath, value ] = line.split('=').map(str => str.trim());
        if (['sonar.organization','sonar.projectKey'].includes(keyPath)) {
            const [, key] = keyPath.split('.');
            result[key] = value;
            left -= 1;
        }
        return Boolean(left);
    })
    return result;
}

function readVsCodeConfiguration() {
    const configuration = workspace.getConfiguration('creedengo');
    const result = {};
    result.server = configuration.get('sonarUrl', '');
    result.token = configuration.get('sonarToken', '');
    result.organization = configuration.get('sonarOrganisationKey', '');
    result.projectKey = configuration.get('sonarProjectKey', '');
    return result;
}

export async function getSonarConfiguration() {
    const configuration = readVsCodeConfiguration();
    const properties = await readSonarProjectProperties();
    Object.assign(configuration, properties);
    return configuration;
}

export function getNonce() {
  let text = "";
  const possible =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  for (let i = 0; i < 32; i++) {
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  }
  return text;
}