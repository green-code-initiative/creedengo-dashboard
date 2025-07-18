import path from 'node:path'
import { fileURLToPath } from 'node:url'

import js from '@eslint/js'
import { includeIgnoreFile } from '@eslint/compat'

import markdown from 'eslint-plugin-markdown'
import prettier from 'eslint-config-prettier'
import creedengo from '@creedengo/eslint-plugin'

// Safely retrieve the .gitignore path
// see https://eslint.org/docs/latest/use/configure/ignore#including-gitignore-files
const __filename = fileURLToPath(import.meta.url)
const __dirname = path.dirname(__filename)
const gitignorePath = path.resolve(__dirname, '.gitignore')

export default [
  includeIgnoreFile(gitignorePath),
  prettier,
  {
    files: ['**/*.js'],
    languageOptions: {
      ecmaVersion: 'latest',
      sourceType: 'module',
    }
  },
  js.configs.recommended,
  ...markdown.configs.recommended,
  creedengo.configs['flat/recommended'],
]
