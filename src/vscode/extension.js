import { window, commands } from 'vscode';

import { CreedengoScoreViewProvider } from './score.view.js';

/**
 * @param {vscode.ExtensionContext} context
 */
export function activate({ subscriptions, extensionUri }) {
  console.log('Activating "Creedengo for Sonar" extension');

  const id = 'Creedengo';
  const provider = new CreedengoScoreViewProvider(extensionUri);
  
  // Views
  subscriptions.push(
  	window.registerWebviewViewProvider(`${id}.Score`, provider)
  );

  // Commands
  subscriptions.push(
	  commands.registerCommand(`${id}.clearScore`, () => provider.clearScore())
  );
  subscriptions.push(
	  commands.registerCommand(`${id}.updateScore`, () => provider.updateScore())
  );

}


// This method is called when your extension is deactivated
export function deactivate() {
  console.log('Extension "creedengo" is now deactivated.');
}
