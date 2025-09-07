import { setupServer } from 'msw/node'
import { HttpResponse, http } from 'msw'
import { afterAll, afterEach, beforeAll, describe, expect, test } from 'vitest'

import mockPullRequestList from './sonar.project-pull-requests.list.mock'
import { getProjectPullRequests } from './sonar.project-pull-requests.list.api'

export const restHandlers = [
  http.get('/api/project_pull_requests/list', () => HttpResponse.json(mockPullRequestList))
]

const server = setupServer(...restHandlers)
// Start server before all tests
beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
//  Close server after all tests
afterAll(() => server.close())
// Reset handlers after each test `important for test isolation`
afterEach(() => server.resetHandlers())

describe('getProjectPullRequests()', () => {
  test('getProjectPullRequests() retrieves 1 pull request', async () => {
    const pullRequests = await getProjectPullRequests('foo')
    expect(pullRequests).toStrictEqual(mockPullRequestList.pullRequests)
  })
})
