import { setupServer } from 'msw/node'
import { HttpResponse, http } from 'msw'
import { afterAll, afterEach, beforeAll, describe, expect, test } from 'vitest'

import mockBranchList from './sonar.project-branches.list.mock'
import { getProjectBranches } from './sonar.project-branches.list.api'

export const restHandlers = [
  http.get('/api/project_branches/list', () => HttpResponse.json(mockBranchList))
]

const server = setupServer(...restHandlers)
// Start server before all tests
beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
//  Close server after all tests
afterAll(() => server.close())
// Reset handlers after each test `important for test isolation`
afterEach(() => server.resetHandlers())

describe('getProjectBranches()', () => {
  test('getProjectBranches() retrieves 1 branch', async () => {
    const branches = await getProjectBranches('foo')
    expect(branches).toStrictEqual(mockBranchList.branches)
  })
})
