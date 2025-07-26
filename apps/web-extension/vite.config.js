import { defineConfig } from 'vite'

// https://vitejs.dev/config/
export default defineConfig({
  build: {
    target: 'es2015', // Compliance with RGESN 2.4, WSG 2.2
  }
})