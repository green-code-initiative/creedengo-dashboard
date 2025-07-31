import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import AbcdeScore from './AbcdeScore.vue'

describe('AbcdeScore', () => {
  it('renders properly', () => {
    const wrapper = mount(AbcdeScore, { props: { value: 'C' } })
    expect(wrapper.text()).toContain('C')
  })

  it('sets score to undefined when no value is provided', () => {
    const wrapper = mount(AbcdeScore)
    expect(wrapper.vm.score).toBeUndefined()
  })
})
