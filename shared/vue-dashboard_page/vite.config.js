const path = require('path')
const { defineConfig } = require('vite')

export default defineConfig({
  build: {
    lib: {
      entry: path.resolve(__dirname, 'lib/main.js'),
      name: 'vue-dashboard_page',
      fileName: `dashboard_page.stories.js`
    }
  }
});