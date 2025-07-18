import { dirname, resolve } from 'node:path'
import { fileURLToPath } from 'node:url'

import { defineConfig } from 'vite'

const __dirname = dirname(fileURLToPath(import.meta.url))

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    target: 'esnext',
    lib: {
      entry: resolve(__dirname + '/src/index.js'),
      name: 'sonar-service',
      fileName: 'sonar-service'
    }
  },
})
