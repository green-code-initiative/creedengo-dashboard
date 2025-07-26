
const coverageConfig = {
  istanbul: {
    include: ['**/*.stories.js'],
  },
};

/** @type { import('@storybook/vue3-vite').StorybookConfig } */
const config = {
  stories: ['../src/**/*.mdx', '../src/**/*.stories.js'],
  addons: [
    '@storybook/addon-links',
    '@storybook/addon-docs',
    '@storybook/addon-a11y',
    {
      name: '@storybook/addon-coverage',
      options: coverageConfig,
    },
  ],
  framework: {
    name: '@storybook/vue3-vite',
    options: {}
  },
  staticDirs: ['../public', '../dist', '../docs'],
}
export default config
