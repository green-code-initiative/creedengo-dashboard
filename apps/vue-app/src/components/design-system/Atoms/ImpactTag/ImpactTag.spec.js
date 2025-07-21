import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import ImpactTag from './ImpactTag.vue'

describe('ImpactTag', () => {
  it('Optimized renders properly', () => {
    const wrapper = mount(ImpactTag, { props: { impact: 'Optimized' } })
    expect(wrapper.text()).toContain('Optimized')
  })
  it('Info renders properly', () => {
    const wrapper = mount(ImpactTag, { props: { impact: 'Info' } })
    expect(wrapper.text()).toContain('Info')
  })
})
