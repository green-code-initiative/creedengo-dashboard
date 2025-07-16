import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    target: 'esnext'
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
