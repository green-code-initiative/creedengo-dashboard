import js from '@eslint/js'
import markdown from 'eslint-plugin-markdown'
import prettier from 'eslint-config-prettier'
import creedengo from '@creedengo/eslint-plugin'

import { initGitIgnore, legacyToFlat } from './utils.js'

legacyToFlat(creedengo, '2.1.0', '@creedengo/eslint-plugin', '@creedengo')

export * as globals from 'globals'

/**
 * 
 * @param {Object} params 
 * @param {string} params.importMeta Project provided `import.meta`
 * @param {Object} params.globals Authorized global APIs using provided globals collection 
 * @param {Array} params.extraJsFiles Extra JS file path patterns ex: ** / *.vue
 * @param {Array} params.extraConfig Extra ESlint configuration (plugins, custom config, ...)
 * @returns 
 */
export function defineConfig({ importMeta, globals = {}, extraJsFiles = [], extraConfig = [] }) {
  const ignoreConfig = [];
  if (importMeta) {
    ignoreConfig.push(initGitIgnore(importMeta))
  }
  const baseJsConfig = {
      files: ['**/*.js', extraJsFiles],
      languageOptions: { ecmaVersion: 'latest', sourceType: 'module', globals }
  }
  const commonPluginConfigs = [
    prettier,
    js.configs.recommended,
    ...markdown.configs.recommended,
    creedengo.configs['flat/recommended'],
  ]
  return [
    ...ignoreConfig, baseJsConfig, ...commonPluginConfigs, ...extraConfig
  ];
}