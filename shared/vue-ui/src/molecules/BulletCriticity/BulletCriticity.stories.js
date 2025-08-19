import BulletCriticity from './BulletCriticity.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Molecules/BulletCriticity',
  component: BulletCriticity,
  decorators: [() => ({ template: '<ul><story/></ul>' })],
  tags: ['autodocs'],
  argTypes: {
    impact: { control: { type: 'select' }, options: ['Optimized', 'Low', 'Medium', 'High'] },
    count: { control: { type: 'number', min: 0 }, required: true },
    label: { control: { type: 'text' }, required: false },
    link: { control: { type: 'text' }, required: true },
  }
}

// TODO: Write interaction tests so the click generates the right navigation behavior
const link = 'project/issues?branch=$main&id=my-project&resolved=false&tags='

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const OptimizedDiskRules = {
  args: { impact: 'Optimized', label: 'Disk rules', count: 5, link: `${link}disk` }
}

export const LowImpact = {
  args: { impact: 'Low', count: 2563, link: `${link}cpu` }
}

export const MediumImpact = {
  args: { impact: 'Medium', count: 365, link: `${link}ram` }
}

export const HighImpactNoIssues = {
  args: { impact: 'High', count: 0, link: `${link}network` }
}
