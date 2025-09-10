globalThis.importMeta = (function iifePolyfillImportMeta() {
    const url = require('node:url')
    const { URL } = url;
    const filenameUrl = new URL(`file:${__filename}`)
    return {
        get env() {
            return process.env
        },
        url: filenameUrl.href 
    }
}())