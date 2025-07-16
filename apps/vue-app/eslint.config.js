import globals from 'globals'
import path from 'node:path'
import { fileURLToPath } from 'node:url'

import js from '@eslint/js'
import { includeIgnoreFile } from '@eslint/compat'

import markdown from 'eslint-plugin-markdown'
import vue from 'eslint-plugin-vue'
import prettier from 'eslint-config-prettier'
import creedengo from '@creedengo/eslint-plugin'

import playwright from 'eslint-plugin-playwright'
import storybook from 'eslint-plugin-storybook'

// Safely retrieve the .gitignore path
// see https://eslint.org/docs/latest/use/configure/ignore#including-gitignore-files
const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)
const gitignorePath = path.resolve(__dirname, '.gitignore')

// patch creedengo plugin for flat config support
// see https://eslint.org/docs/latest/extend/plugin-migration-flat-config
if (!creedengo.configs['flat/recommended']) {
  creedengo.meta = {
    name: '@creedengo/eslint-plugin',
    version: '2.1.0'
  }
  creedengo.configs['flat/recommended'] = {
    plugins: {
      '@creedengo': creedengo
    },
    rules: creedengo.configs.recommended.rules
  }
}

export default [
  includeIgnoreFile(gitignorePath),
  prettier,
  {
    files: ['**/*.vue', '**/*.js'],
    languageOptions: {
      ecmaVersion: 'latest',
      sourceType: 'module',
      globals: {
        ...globals.node // probably to be removed - allow node for node.process in main.js sonar-i18n.js
      }
    }
  },
  js.configs.recommended,
  ...markdown.configs.recommended,
  ...vue.configs['flat/recommended'],
  creedengo.configs['flat/recommended'],
  {
    ...storybook.configs['plugin:storybook/recommended'],
    files: ['**/*.stories.js']
  },
  {
    ...playwright.configs['flat/recommended'],
    files: ['e2e/**/*.spec.js']
  }
]
