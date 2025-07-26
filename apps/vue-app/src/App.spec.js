import { vi, beforeAll, describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'

import core from '@creedengo/core-services'

import App from './App.vue'

vi.mock('@creedengo/core-services')

beforeAll(() => {
  // Mock the API calls
  vi.mocked(core.calculateProjectScore).mockResolvedValue('D');
});


describe('App', () => {
    
    it('App renders properly by default', () => {
        const wrapper = mount(App, { props: { project: 'my-project', branch: 'master' }}) 
        expect(wrapper.exists()).toBeTruthy()
        expect(wrapper.find('h1').text()).toEqual('Creedengo Dashboard')
    })

})
  