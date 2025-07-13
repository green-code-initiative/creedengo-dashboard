import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import IconBug from './IconBug.vue'

describe('IconBug', () => {
  it('IconBug renders properly by default', () => {
    const wrapper = mount(IconBug) 
    const svg = wrapper.find('svg')
    expect(svg).toBeTruthy()
  })
})
