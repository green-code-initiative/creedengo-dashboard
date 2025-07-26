import process from 'node:process'
import { defineConfig, devices } from '@playwright/test'

/**
 * See https://playwright.dev/docs/test-configuration.
 */
export default ({ basePath = '', port }) => {
  
  // Base URL to use in actions like `await page.goto('/')`.
  const baseURL = `http://localhost:${port}${basePath}`;

  const config = {
    testDir: 'e2e',
    testMatch: "**/*.spec.js",
    outputDir: 'playwright-report/', // Test artifacts such as screenshots, videos, traces, etc.
    fullyParallel: true,
    retries: process.env.CI ? 2 : 0, // Retry on CI only
    forbidOnly: !!process.env.CI, // Fail on CI if you left test.only in the code.
    workers: process.env.CI ? 1 : undefined, // Opt out of parallel tests on CI.
    // Reporters to use. See https://playwright.dev/docs/test-reporters
    reporter: [["list"], ["html", { open: "never", outputFolder: "playwright-report" }]],
    use: {
      baseURL,
      // screenshot: "on",
      // video: "on-first-retry",
      trace: 'on-first-retry', // trace when retrying failed test.
      headless: !!process.env.CI, // Only on CI systems run the tests headless.
      actionTimeout: 0, // Maximum time each action such as `click()` can take. 0 = no limit.
    },
    expect: { timeout: 5_000 }, // Maximum time expect() should wait
    timeout: 30_000, // Maximum time one test can run for.
    projects: [
      { name: 'chromium', use: { ...devices['Desktop Chrome'] } },
      { name: 'firefox', use: { ...devices['Desktop Firefox'] } },
      { name: 'webkit', use: { ...devices['Desktop Safari'] } }
      // { name: 'Mobile Chrome', use: { ...devices['Pixel 5'] } },
      // { name: 'Mobile Safari', use: { ...devices['iPhone 12'] } },
    ],
    webServer: {
      command: process.env.CI ? `vite preview --port ${port}` : 'vite dev',
      // command: process.env.CI ? `vite dev --port ${port}` : 'vite dev',
      url: baseURL,
      reuseExistingServer: !process.env.CI
    }
  };

  return defineConfig(config);
}
