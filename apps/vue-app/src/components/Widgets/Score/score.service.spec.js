import { vi, afterAll, beforeAll, describe, expect, test } from 'vitest'

import { getIssuesFacet } from '@/api/sonar/issues/sonar.issues.search.api'
import { getNumberOfLineOfCode } from '@/api/sonar/measures/sonar.measures.component.api';
import { calculateProjectScore } from './score.service'

vi.mock('@/api/sonar/issues/sonar.issues.search.api')
vi.mock('@/api/sonar/measures/sonar.measures.component.api');

beforeAll(() => {
  // Mock the API calls
  vi.mocked(getIssuesFacet).mockResolvedValue({ });
  vi.mocked(getNumberOfLineOfCode).mockResolvedValue(100);
});

afterAll(() => {
  // Mock the API calls
});

describe('calculateProjectScore', () => {

  describe('Score E', () => {
    test('Should return E is there is more than one Blocker', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ blocker: 9 });
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('E')
    })
    test('Should return E is there is at least one Blocker', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ blocker: 1 });
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('E')
    })
    test('Should not return E is there is no Blocker', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ 
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
        vi.mocked(getIssuesFacet).mockResolvedValue({ critical: 1 });
        const score = await calculateProjectScore('foo', 'master')   
        expect(score).toStrictEqual('D')
    })
    test('Should return D is there is no Blocker and Critical but at least 10 Major', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ major: 10 });
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('D')
    })
    test('Should return D if there is no Blocker, Critical, and Major but at least 8% of minor issues related to lines of code', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ minor: 16 });
      vi.mocked(getNumberOfLineOfCode).mockResolvedValue(200);
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('D')
    });

  })

  describe('Score C', () => {

    test('Should return C is there is no Blocker and Critical, and less than 8% Minor but at least one Major', async () => {
        vi.mocked(getIssuesFacet).mockResolvedValue({ major: 1 });
        const score = await calculateProjectScore('foo', 'master')   
        expect(score).toStrictEqual('C')
    })

    test('Should return C is there is no Blocker, Critical, and Major, and less than 8% Minor but at least 10 Minor or Info', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ minor: 7, info: 6 });
      vi.mocked(getNumberOfLineOfCode).mockResolvedValue(200);
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('C')
    })
    test('Should not return C if there is no Blocker, Critical, and Major, less than 8% of minor and less than 10 Minor or info', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ minor: 7, info: 2 });
      vi.mocked(getNumberOfLineOfCode).mockResolvedValue(200);
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).not.toStrictEqual('C')
    });


  })

  describe('Score B', () => {
    test('Should return B if there is no Blocker, Critical, Major, and less than 8% Minor but between one and 10 Minor or Info', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ minor: 1, info: 0 });
      let score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('B')
      vi.mocked(getIssuesFacet).mockResolvedValue({ minor: 0, info: 1 });
      score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('B')
    })

    test('Should not return B if there is no Blocker, Critical, Major, and less than 8% Minor and no Minor or Info', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ minor: 0, info: 0 });
      vi.mocked(getNumberOfLineOfCode).mockResolvedValue(200);
      const score = await calculateProjectScore('foo', 'master')   
      expect(score).not.toStrictEqual('B')
    });
  })

  describe('Score A', () => {
    test('Should return A if there is no Issue', async () => {
      vi.mocked(getIssuesFacet).mockResolvedValue({ });
      let score = await calculateProjectScore('foo', 'master')   
      expect(score).toStrictEqual('A')
    })
  })

})
