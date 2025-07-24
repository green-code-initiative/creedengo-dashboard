import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import VueDevTools from 'vite-plugin-vue-devtools'

/**
 * 
 * @see https://vitejs.dev/config/
 * @see https://ecoresponsable.numerique.gouv.fr/publications/referentiel-general-ecoconception/#specifications
 * @see https://w3c.github.io/sustainableweb-wsg/#visitor-constraints
 */
export default defineConfig({
  plugins: [vue(), VueDevTools()],
  build: {
    target: 'es2015', // Compliance with RGESN 2.4, WSG 2.2
    rollupOptions: {
      external: [],
      output: {
        entryFileNames: `view.js`,
        assetFileNames: `view.css`
      }
    }
  },
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
