/**
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/helpers/__tests__/l10n-test.ts
 * @see
 */

import { vi, describe, beforeAll, afterEach, test, expect } from 'vitest'
import { toast } from 'vue3-toastify'

import { addGlobalErrorMessage, addGlobalSuccessMessage } from './sonar-toast'

vi.mock('vue3-toastify', async () => ({ toast: { error: vi.fn(), success: vi.fn() } }))

beforeAll(()=> {
  vi.resetModules()
})
describe('sonar toast', () => {
  afterEach(() => vi.clearAllMocks())

  test('addGlobalErrorMessage should call toast.error() with the right message', () => {
    addGlobalErrorMessage('error message')
    expect(toast.error).toHaveBeenCalledWith('error message')
  })

  test('addGlobalSuccessMessage should call toast.success() with the right message', () => {
    addGlobalSuccessMessage('error message')
    expect(toast.success).toHaveBeenCalledWith('error message')
  })

  test('addGlobalErrorMessage should accept the delay option', () => {
    addGlobalErrorMessage('error message', { delay: 3000 })
    expect(toast.error).toHaveBeenCalledWith('error message', { delay: 3000 })
  })

  test('addGlobalSuccessMessage should not pass the data option', () => {
    addGlobalSuccessMessage('it worked', { data: 'bar', autoClose: 3000, role: 'status' })
    expect(toast.success).toHaveBeenCalledWith('it worked', { autoClose: 3000, role: 'status' })
  })

})
