import { vi, describe, it, expect } from 'vitest'
import { mount, flushPromises } from '@vue/test-utils'

import { calculateProjectScore } from './score.service';
import ScoreWidget from './ScoreWidget.vue'

vi.mock('./score.service')

describe('ScoreWidget', () => {
  it('renders properly', async () => {
    vi.mocked(calculateProjectScore).mockResolvedValue('D');
    const wrapper = mount(ScoreWidget, { props: { projectKey: 'my-project-key', branch: 'main' } })
    await flushPromises()
    expect(wrapper.text()).toContain('DABCDE') // Assuming the score is D based on the mock data
  })
})
