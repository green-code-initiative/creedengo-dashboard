import { test, expect } from '@playwright/test'

// See here how to get started:
// https://playwright.dev/docs/intro
test('visits the app root url', async ({ page }) => {
  await page.goto('/')
  await expect(page.locator('h1')).toHaveText('Creedengo Dashboard')

  // TODO - FIX the way to run MockServiceWorker from the CI
  await expect(page.locator('strong.rate-note')).toHaveText('D')
})
