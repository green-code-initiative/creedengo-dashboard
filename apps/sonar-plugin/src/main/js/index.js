/**
 * @see See https://docs.sonarsource.com/sonarqube/latest/extension-guide/developing-a-plugin/adding-pages-to-the-webapp/#create-a-javascript-file-per-page
 */
import "@creedengo/vue-dashboard/script"
import "@creedengo/vue-dashboard/stylesheet"

function start(options) {
    const rootNode = options.el;
    rootNode.innerHTML = `
        <H1>Creedengo Dashboard - TOTO 1</H1>
        <div id="app"></div>
        <H1>Creedengo Dashboard - TOTO 2</H1>
    `;
    return () => stop(rootNode)
}

function stop(rootNode) {
    // pretty hardcore - not sure to maintain that as-is
    rootNode.innerHTML = ''
}

window.registerExtension('creedengodashboard/view', start, true)