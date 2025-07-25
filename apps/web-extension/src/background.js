//import configuration from '../configuration-sample.js'

//import '../compat/browser-polyfill'

//const browser = chrome;

chrome.runtime.onInstalled.addListener(async () => {
  /*
    await chrome.contextMenus.create({
      id: 'openSidePanel',
      title: 'Open side panel',
      contexts: ['all']
    });
    */
    await chrome.tabs.create({ url: 'http://localhost:5173' });
    /*
    chrome.contextMenus.onClicked.addListener((info, tab) => {
        if (info.menuItemId === 'openSidePanel') {
          // This will open the panel in all the pages on the current window.
          chrome.sidebarAction.open({ windowId: tab.windowId });
        }
    });
    */
});
  