import vscode from './compat/vscode.cjs'

import { CreedengoScoreViewProvider } from './views/score/score.view.mjs';

const { commands, window, workspace } = vscode

/**
 * @param {vscode.ExtensionContext} context
 */
export function activate(context) {
  const  { subscriptions, extensionUri } = context
  console.log('Activating "Creedengo for Sonar" extension');

  const id = 'creedengo-for-sonar';
  const provider = new CreedengoScoreViewProvider(extensionUri);

  // Commands
  subscriptions.push(
	  commands.registerCommand(`${id}.clearScore`, () => provider.clearScore())
  );
  subscriptions.push(
	  commands.registerCommand(`${id}.updateScore`, () => provider.updateScore())
  );
  subscriptions.push(
	  commands.registerCommand(`${id}.refresh`, () => provider.refresh())
  );

  // Views
  subscriptions.push(
  	window.registerWebviewViewProvider(`${id}.scoreView`, provider)
  );

  // Auto Refresh on configuration change
  const refresh = () => provider.refresh()
  const configurationActionMap = {
    'creedengo.serverUrl': refresh,
    'creedengo.token': refresh,
  }
  workspace.onDidChangeConfiguration(async event => {
    for (let [key, action] of Object.entries(configurationActionMap)) {
      if (event.affectsConfiguration(key)) {
        action()
      }  
    }
  })
}

// This method is called when your extension is deactivated
export function deactivate() {
  console.log('Extension "creedengo" is now deactivated.');
}
