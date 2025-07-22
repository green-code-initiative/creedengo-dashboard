import { defineConfig } from 'vite'
import { resolve } from 'node:path'

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    target: 'esnext',
    rollupOptions: {
      input: resolve(__dirname, './src/index.html'),
    }
  }
})