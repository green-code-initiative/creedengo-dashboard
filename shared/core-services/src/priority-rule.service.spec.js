import { vi, describe, expect, test } from 'vitest'

import api from './adapter'
import { getPriorityRule } from './priority-rule.service'

vi.mock('./adapter', async () => ({ default: { services: {
  getIssuesFacet: vi.fn(),
  getRuleDetails: vi.fn().mockImplementation(key => ({
    'blocker:key': { key },
    'critical:key': { key },
    'major:key': { key },
    'minor:key': { key },
    'info:key': { key },
  })[key])
}}}))

const mockConfig = { project: 'foo' }

function mockGetIssuesFacet(data) {
  return (facet, config) => {
    expect(config).toHaveProperty('severity')
    expect(config).toHaveProperty('project', 'foo')
    expect(facet).toBe('rules')
    return data[config.severity] || {}
  }
}

const BLOCKER = { 'blocker:key': 4, 'blocker:key2': 3 }
const CRITICAL = { 'critical:key': 7, 'critical:key2': 4  }
const MAJOR = { 'major:key': 12, 'critical:key2': 4 }
const MINOR_OR_INFO = { 'minor:key': 31, 'info:key': 12, 'minor:key2': 4 }

describe('getPriorityRule()', () => {

  test('Should return Blocker issue related rule if one exist', async () => {
    const mock = mockGetIssuesFacet({ BLOCKER, CRITICAL, MAJOR })
    api.services.getIssuesFacet.mockImplementation(mock);
    const rule = await getPriorityRule(mockConfig)   
    expect(rule).toHaveProperty('key', 'blocker:key')
  })

  test('Should return Critical issue related rule if one exist and no blocker exist', async () => {
    const mock = mockGetIssuesFacet({ CRITICAL, MAJOR })
    api.services.getIssuesFacet.mockImplementation(mock);
    const rule = await getPriorityRule(mockConfig)   
    expect(rule).toHaveProperty('key', 'critical:key')
  })

  test('Should return major issue related rule if one exist and no blocker or critical exist', async () => {
    const mock = mockGetIssuesFacet({  MAJOR, 'MINOR,INFO': MINOR_OR_INFO })
    api.services.getIssuesFacet.mockImplementation(mock);
    const rule = await getPriorityRule(mockConfig)   
    expect(rule).toHaveProperty('key', 'major:key')
  })

  test('Should return minor issue related rule if one exist and no blocker, critical or major exist', async () => {
    const mock = mockGetIssuesFacet({ 'MINOR,INFO': MINOR_OR_INFO })
    api.services.getIssuesFacet.mockImplementation(mock);
    const rule = await getPriorityRule(mockConfig)   
    expect(rule).toHaveProperty('key', 'minor:key')
  })

  test('Should return null if no issue exist', async () => {
    const mock = mockGetIssuesFacet({ })
    api.services.getIssuesFacet.mockImplementation(mock);
    const rule = await getPriorityRule(mockConfig)   
    expect(rule).toBe(null)
  })

})
