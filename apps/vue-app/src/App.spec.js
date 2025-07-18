import { vi, beforeAll, describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'

import { getIssuesFacet } from '@/api/sonar/issues/sonar.issues.search.api'
import { getNumberOfLineOfCode } from '@/api/sonar/measures/sonar.measures.component.api';

import App from './App.vue'

vi.mock('@/api/sonar/issues/sonar.issues.search.api')
vi.mock('@/api/sonar/measures/sonar.measures.component.api');

beforeAll(() => {
  // Mock the API calls
  vi.mocked(getIssuesFacet).mockResolvedValue({ minor: 25 });
  vi.mocked(getNumberOfLineOfCode).mockResolvedValue(100);
});


describe('App', () => {
    
    it('App renders properly by default', () => {
        const wrapper = mount(App) 
        expect(wrapper.exists()).toBeTruthy()
        expect(wrapper.html()).toContain('You did it!')
    })

})
  