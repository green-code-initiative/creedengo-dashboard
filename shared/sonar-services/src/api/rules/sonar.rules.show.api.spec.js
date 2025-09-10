import { setupServer } from 'msw/node'
import { HttpResponse, http } from 'msw'
import { afterAll, afterEach, beforeAll, describe, expect, test } from 'vitest'

import mockRuleDetails from './sonar.rules.show.mock'
import { getRuleDetails } from './sonar.rules.show.api'

export const restHandlers = [
  http.get('/api/rules/show', () => HttpResponse.json(mockRuleDetails))
]

const server = setupServer(...restHandlers)
// Start server before all tests
beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
//  Close server after all tests
afterAll(() => server.close())
// Reset handlers after each test `important for test isolation`
afterEach(() => server.resetHandlers())

describe('getRuleDetails', () => {
  test('getRuleDetails retrieve 1 rule', async () => {
    const issues = await getRuleDetails({ project: 'foo', branch: 'master' })
    expect(issues).toStrictEqual(mockRuleDetails.rule)
  })
})
