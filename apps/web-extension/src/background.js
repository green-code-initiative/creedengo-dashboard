//import configuration from '../configuration-sample.js'

browser.runtime.onInstalled.addListener(() => {
    browser.contextMenus.create({
      id: 'openSidePanel',
      title: 'Open side panel',
      contexts: ['all']
    });
    browser.tabs.create({ url: 'index.html' });
});
  
browser.contextMenus.onClicked.addListener((info, tab) => {
    if (info.menuItemId === 'openSidePanel') {
      // This will open the panel in all the pages on the current window.
      browser.sidebarAction.open({ windowId: tab.windowId });
    }
});