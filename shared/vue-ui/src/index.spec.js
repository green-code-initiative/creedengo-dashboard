import { describe, it, expect } from 'vitest'

import * as ui from './index.js'

describe('vue-ui export', () => {
  
  it('export expected icons', () => {
    expect(ui).toHaveProperty('IconBug')
    expect(ui).toHaveProperty('IconCpu')
    expect(ui).toHaveProperty('IconDisk')
    expect(ui).toHaveProperty('IconMaintenance')
    expect(ui).toHaveProperty('IconNetwork')
    expect(ui).toHaveProperty('IconRam')
  })
  it('export expected other atoms', () => {
    expect(ui).toHaveProperty('ImpactTag')
  })
  it('export expected molecules', () => {
    expect(ui).toHaveProperty('AbcdeScore')
    expect(ui).toHaveProperty('RuleIconTag')
  })
})
