import { defineConfig, globals } from '@creedengo/eslint-config'

export default defineConfig({ 
    importMeta: import.meta,
    globals: {
        ...globals.worker,
        FormData: false,
        Response: false,
        console: false,
    }
});
