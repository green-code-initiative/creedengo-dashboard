import { fileURLToPath } from 'node:url'
import { 
  mergeConfig,
  defineConfig,
  configDefaults,
  coverageConfigDefaults
} from 'vitest/config'

const reporter = ['text', 'lcov'];

/**
 * 
 * @param {Object} params
 * @param {Object} params.importMeta import.meta
 * @param {Object} params.viteConfig must be imported from vite.config.js.
 * @param {Object} [params.extraExclude] extra excluded file patterns
 * @param {string} [params.envirnoment] default: 'jsdom'
 * @returns 
 */
export function defineVitestConfig({
  importMeta,
  viteConfig ,
  extraExclude = [],
  environment = 'jsdom',
}) {
  const root = fileURLToPath(new URL('./', importMeta.url.replace('node_modules/.vite-temp/', '')))
  const include = ['**/*.spec.js'];
  const exclude = [
    ...configDefaults.exclude, 
    'node_modules',
    '.*rc*', '*.config.js', '**/mockServiceWorker.js', 
    ...extraExclude
  ]
  const coverage = { 
    reporter, 
    exclude: [...coverageConfigDefaults.exclude, ...exclude] 
  } 
  
  return mergeConfig(
    viteConfig,
    defineConfig({ test: { environment, include, exclude, root, coverage } })
  )
}
