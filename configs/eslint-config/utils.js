import { fileURLToPath } from 'node:url'
import fs from 'node:fs'
import { includeIgnoreFile } from '@eslint/compat'

/**
 * Apply the project .gitignore to the ESLint configuration
 * 
 * @see https://eslint.org/docs/latest/use/configure/ignore#including-gitignore-files
 * 
 * @param {Object} importMeta 
 * @param {string} importMeta.url 
 */
export function initGitIgnore(importMeta) {
  const gitignorePath = fileURLToPath(new URL(".gitignore", importMeta.url));
  if (fs.existsSync(gitignorePath)) {
    return includeIgnoreFile(gitignorePath, "Imported .gitignore patterns")
  }
  return {};
}

/**
 * Patch ESLint plugin to support flat config which is mandatory fo ESLint 9+
 * 
 * @see https://eslint.org/docs/latest/extend/plugin-migration-flat-config 
 * 
 * @param {Object} plugin 
 * @param {string} version 
 * @param {string} name 
 * @param {string} author 
 */
export function legacyToFlat(plugin, version, name, author) {
  if (!plugin.configs['flat/recommended']) {
    plugin.meta = { name, version }
    plugin.configs['flat/recommended'] = {
      plugins: { [author ?? name]: plugin },
      rules: plugin.configs.recommended.rules
    }
  }
}
