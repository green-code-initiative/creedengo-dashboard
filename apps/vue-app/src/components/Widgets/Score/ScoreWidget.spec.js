import { vi, describe, it, expect } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'

import SonarAPI from '@creedengo/sonar-services'
import core from '@creedengo/core-services';
import ScoreWidget from './ScoreWidget.vue'

vi.mock('@creedengo/core-services', () => ({ default: {
  api: { init: vi.fn() },
  calculateProjectScore: vi.fn()
}}))

describe('ScoreWidget', () => {
  it('Init the SonarAPI', async () => {
    mount(ScoreWidget, { props: { project: 'my-project-key', branch: 'main' } })
    await flushPromises()
    expect(core.api.init).toHaveBeenCalledWith(SonarAPI)
  })
  it('renders properly', async () => {
    core.calculateProjectScore.mockResolvedValue('D');
    const wrapper = mount(ScoreWidget, { props: { project: 'my-project-key', branch: 'main' } })
    await flushPromises()
    expect(core.calculateProjectScore).toHaveBeenCalledWith({ project: 'my-project-key', branch: 'main' })
    expect(wrapper.text()).toContain('DABCDE') // Assuming the score is D based on the mock data
  })
})
