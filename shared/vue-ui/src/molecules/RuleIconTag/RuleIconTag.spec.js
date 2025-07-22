import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import RuleIconTag from './RuleIconTag.vue'

describe('RuleIconTag', () => {
  it('CPU renders properly', () => {
    const wrapper = mount(RuleIconTag, { props: { label: 'cpu' } })
    expect(wrapper.text()).toContain('cpu')
  })
  it('Network renders properly', () => {
    const wrapper = mount(RuleIconTag, { props: { label: 'Network' } })
    expect(wrapper.text()).toContain('Network')
  })
})
