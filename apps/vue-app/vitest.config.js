import { defineVitestConfig } from '@creedengo/vitest-config'

import viteConfig from './vite.config'

export default defineVitestConfig({
  viteConfig,
  importMeta: import.meta,
  extraExclude: ['**/*.stories.js', 'e2e/**', '.storybook'],
});
