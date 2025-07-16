import { fileURLToPath } from 'node:url'
import { mergeConfig, defineConfig, configDefaults } from 'vitest/config'
import viteConfig from './vite.config'

export default mergeConfig(
  viteConfig,
  defineConfig({
    test: {
      environment: 'jsdom',
      exclude: [...configDefaults.exclude, 'e2e/**', '**/*.stories.js'],
      root: fileURLToPath(new URL('./', import.meta.url)),
      coverage: {
        reporter: ['text', 'lcov'],
        exclude: ['**/*.stories.js','.*rc*','*.config.js'],
      }
    }
  })
)
