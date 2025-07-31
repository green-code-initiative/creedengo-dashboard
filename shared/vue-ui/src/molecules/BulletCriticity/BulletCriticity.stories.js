import BulletCriticity from './BulletCriticity.vue'

const projectLink = 'project/issues?branch=$main&id=my-project&resolved=false&tags='

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Molecules/BulletCriticity',
  component: BulletCriticity,
  decorators: [() => ({ template: '<ul><story/></ul>' })],
  tags: ['autodocs'],
  argTypes: {
    count: { control: { type: 'number', min: 0 }, required: true },
    type: { control: { type: 'select' }, options: ['rule', 'issue'] },
    impact: { control: { type: 'select' }, options: ['Optimized', 'Low', 'Medium', 'High'] },
    metricTag: { control: { type: 'select' }, options: ['CPU', 'RAM', 'Disk', 'Network'] },
    projectLink: { control: { type: 'text' }, required: true },
  }
}

// TODO: Write interaction tests so the click generates the right navigation behavior

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const OptimizedDiskRules = {
  args: { impact: 'Optimized', type: 'rule', count: 5, metricTag: 'Disk', projectLink: projectLink + 'disk' }
}

export const LowImpact = {
  args: { impact: 'Low', type: 'issue', count: 2563, metricTag: 'CPU', projectLink: projectLink + 'cpu' }
}

export const MediumImpact = {
  args: { impact: 'Medium', type: 'issue', count: 365, metricTag: 'RAM', projectLink: projectLink + 'ram' }
}

export const HighImpactNoIssues = {
  args: { impact: 'High', type: 'issue', count: 0, metricTag: 'Network', projectLink: projectLink + 'network' }
}
