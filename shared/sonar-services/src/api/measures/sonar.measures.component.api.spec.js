import { setupServer } from 'msw/node'
import { HttpResponse, http } from 'msw'
import { afterAll, afterEach, beforeAll, describe, expect, test } from 'vitest'

import mockComponentMeasuresList from './sonar.measures.component.mock'
import { getNumberOfLineOfCode } from './sonar.measures.component.api'

export const restHandlers = [
  http.get('/api/measures/component', () => HttpResponse.json(mockComponentMeasuresList))
]

const server = setupServer(...restHandlers)
// Start server before all tests
beforeAll(() => server.listen({ onUnhandledRequest: 'error' }))
//  Close server after all tests
afterAll(() => server.close())
// Reset handlers after each test `important for test isolation`
afterEach(() => server.resetHandlers())

describe('getNumberOfLineOfCode', () => {
  test('getNumberOfLineOfCode retrieves the number of lines of code', async () => {
    const numberOfLines = await getNumberOfLineOfCode({ project: 'foo', branch: 'master' })   
    expect(numberOfLines).toStrictEqual(114)
  })
})
