/**
 * @see See https://docs.sonarsource.com/sonarqube/latest/extension-guide/developing-a-plugin/adding-pages-to-the-webapp/#create-a-javascript-file-per-page
 */
import "@creedengo/vue-dashboard/script"
import "@creedengo/vue-dashboard/stylesheet"

function start(options) {
    const rootNode = options.el;
    rootNode.innerHTML = `
        <div id="app"</div>
    `;
    return () => stop(rootNode)
}

function stop(rootNode) {
    // pretty hardcore - not sure to maintain that as-is
    rootNode.innerHTML = ''
}

window.registerExtension('creedengo/view', start, true)