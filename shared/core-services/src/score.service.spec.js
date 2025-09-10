import { vi, afterAll, beforeAll, describe, expect, test } from 'vitest'

import api from './adapter'
import { calculateProjectScore } from './score.service'

vi.mock('./adapter', async () => ({ default: { services: {
  getIssuesFacet: vi.fn(),
  getNumberOfLineOfCode: vi.fn()
}}}))

beforeAll(() => {
  // Mock the API calls
  api.services.getIssuesFacet.mockResolvedValue({ });
  api.services.getNumberOfLineOfCode.mockResolvedValue(100);
});

afterAll(() => {
  // Clean the API calls
});

describe('calculateProjectScore', () => {

  describe('Score E', () => {
    test('Should return E is there is more than one Blocker', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ blocker: 9 });
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('E')
    })
    test('Should return E is there is at least one Blocker', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ blocker: 1 });
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('E')
    })
    test('Should not return E is there is no Blocker', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ 
        info: 500,
        minor: 2000,
        major: 100,
        critical: 100
      });
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).not.toStrictEqual('E')
    })
  })

  describe('Score D', () => {
    test('Should return D is there is no Blocker but at least one Critical', async () => {
        api.services.getIssuesFacet.mockResolvedValue({ critical: 1 });
        const score = await calculateProjectScore('foo', 'master')   
        expect(score).toStrictEqual('D')
    })
    test('Should return D is there is no Blocker and Critical but at least 10 Major', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ major: 10 });
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('D')
    })
    test('Should return D if there is no Blocker, Critical, and Major but at least 8% of minor issues related to lines of code', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ minor: 16 });
      api.services.getNumberOfLineOfCode.mockResolvedValue(200);
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('D')
    });

  })

  describe('Score C', () => {

    test('Should return C is there is no Blocker and Critical, and less than 8% Minor but at least one Major', async () => {
        api.services.getIssuesFacet.mockResolvedValue({ major: 1 });
        const score = await calculateProjectScore('foo', 'master')   
        expect(score).toStrictEqual('C')
    })

    test('Should return C is there is no Blocker, Critical, and Major, and less than 8% Minor but at least 10 Minor or Info', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ minor: 7, info: 6 });
      api.services.getNumberOfLineOfCode.mockResolvedValue(200);
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('C')
    })
    test('Should not return C if there is no Blocker, Critical, and Major, less than 8% of minor and less than 10 Minor or info', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ minor: 7, info: 2 });
      api.services.getNumberOfLineOfCode.mockResolvedValue(200);
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).not.toStrictEqual('C')
    });


  })

  describe('Score B', () => {
    test('Should return B if there is no Blocker, Critical, Major, and less than 8% Minor but between one and 10 Minor or Info', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ minor: 1, info: 0 });
      let score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('B')
      api.services.getIssuesFacet.mockResolvedValue({ minor: 0, info: 1 });
      score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('B')
    })

    test('Should not return B if there is no Blocker, Critical, Major, and less than 8% Minor and no Minor or Info', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ minor: 0, info: 0 });
      api.services.getNumberOfLineOfCode.mockResolvedValue(200);
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).not.toStrictEqual('B')
    });
  })

  describe('Score A', () => {
    test('Should return A if there is no Issue', async () => {
      api.services.getIssuesFacet.mockResolvedValue({ });
      let score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('A')
    })
  })

})
