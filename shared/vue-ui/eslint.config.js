import { defineConfig, globals } from '@creedengo/eslint-config'

import vue from 'eslint-plugin-vue'
import storybook from 'eslint-plugin-storybook'

export default defineConfig({
  importMeta: import.meta,
  globals: globals.node,
  extraFiles: ['**/*.vue'],
  extraConfig: [
    ...vue.configs['flat/recommended'],
    {
      ...storybook.configs['plugin:storybook/recommended'],
      files: ['**/*.stories.js']
    },
  ]
});
