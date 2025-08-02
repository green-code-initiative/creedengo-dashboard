import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import AbcdeScore from './AbcdeScore.vue'

describe('AbcdeScore', () => {
  it('renders properly when a score is provided', () => {
    const wrapper = mount(AbcdeScore, { props: { value: 'C' } })
    expect(wrapper.text()).toContain('C')
  })

  it('renders properly when the provided score is an empty string', () => {
    const wrapper = mount(AbcdeScore, { props: { value: '' } })
    expect(wrapper.vm.score).toBe('')
  })
  it('renders properly when no score is provided', () => {
    const wrapper = mount(AbcdeScore)
    expect(wrapper.vm.score).toBe('')
  })
})
