import polyfill from 'webextension-polyfill/dist/browser-polyfill.min.js'

polyfill();

if (!browserAction.sidebarAction) {
    browserAction.sidebarAction = browser.sidePanel;
}