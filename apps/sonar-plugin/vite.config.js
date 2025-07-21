import { defineConfig } from 'vite'
import { fileURLToPath } from 'url'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  build: {
    lib: {
      entry: fileURLToPath(new URL('./src/main/js/index.js', import.meta.url)),
      name: 'creedengo-sonar-dashboard',
      fileName: (format) => `my-lib.${format}.js`
    },
    rollupOptions: {
      external: [],
      output: {
        entryFileNames: `assets/view.js`,
        assetFileNames: `assets/view.css`
      }
    }
  }
})