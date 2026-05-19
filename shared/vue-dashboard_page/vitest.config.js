import { defineVitestConfig } from '@creedengo/vitest-config'

import viteConfig from './vite.config'

export default defineVitestConfig({
  importMeta: import.meta,
  extraExclude: ['.storybook', '**/*.stories.js', 'e2e/**'],
  viteConfig,
});
