import { setupServer } from 'msw/node'
import { HttpResponse, http } from 'msw'
import { afterAll, afterEach, beforeAll, describe, expect, test } from 'vitest'

import mockRuleList from './sonar.rules.search.mock'
import { findRules } from './sonar.rules.search.api'

export const restHandlers = [
  http.get('/api/rules/search', () => HttpResponse.json(mockRuleList))
]

const server = setupServer(...restHandlers)
// Start server before all tests
beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
//  Close server after all tests
afterAll(() => server.close())
// Reset handlers after each test `important for test isolation`
afterEach(() => server.resetHandlers())

describe('findRules', () => {
  test('findRules retrieve 1 rule', async () => {
    const issues = await findRules({ project: 'foo', branch: 'master' })
    expect(issues).toStrictEqual(mockRuleList.rules)
  })
})
