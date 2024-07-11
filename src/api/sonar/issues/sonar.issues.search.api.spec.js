import { setupServer } from 'msw/node'
import { HttpResponse, http } from 'msw'
import { afterAll, afterEach, beforeAll, describe, expect, test } from 'vitest'

import mockIssueList from './sonar.issues.search.mock'
import { findIssues } from './sonar.issues.search.api'

export const restHandlers = [
  http.get('/api/issues/search', () => HttpResponse.json(mockIssueList))
]

const server = setupServer(...restHandlers)
// Start server before all tests
beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
//  Close server after all tests
afterAll(() => server.close())
// Reset handlers after each test `important for test isolation`
afterEach(() => server.resetHandlers())

describe('findIssues', () => {
  test('findIssues retrieve 1 issue', async () => {
    const issues = await findIssues('foo', 'js', 'master')   
    expect(issues).toStrictEqual(mockIssueList.issues)
  })
})