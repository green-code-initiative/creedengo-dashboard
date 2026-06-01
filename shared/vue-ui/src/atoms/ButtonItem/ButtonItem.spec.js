import { describe, it, expect } from 'vitest'

import { mount } from '@vue/test-utils'
import ButtonItem from './ButtonItem.vue'

describe('ButtonItem', () => {
  it('renders properly', () => {
    const wrapper = mount(ButtonItem, { props: { label: 'click me' } })
    expect(wrapper.text()).toContain('click me')
  })
})
