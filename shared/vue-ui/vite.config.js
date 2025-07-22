import { fileURLToPath, URL } from 'node:url'
import { resolve } from 'node:path'

import { defineConfig } from 'vite'
import tsconfigPaths from 'vite-tsconfig-paths'
import vue from '@vitejs/plugin-vue'

const rootFolder = fileURLToPath(new URL('./src', import.meta.url))

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), tsconfigPaths()],
  build: {
    target: 'esnext',
    lib: {
      entry: resolve(rootFolder, 'index.js'),
      name: 'vue-ui',
      fileName: 'vue-ui'
    }
  }
})
