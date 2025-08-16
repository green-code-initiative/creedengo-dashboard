import ImpactTag from './ImpactTag.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Atoms/Impact Tag',
  component: ImpactTag,
  tags: ['autodocs'],
  argTypes: {
    impact: { control: { type: 'select' }, options: ['Optimized', 'Info', 'Low', 'Medium', 'High', 'Blocker'] },
    customLabel: { control: { type: 'text' }, required: false, default: 'impact' }
  }
}

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const OptimizedImpact = {
  args: { impact: 'Optimized', customLabel: 'CPU rules' }
}

export const InfoImpact = {
  args: { impact: 'Info' }
}

export const LowImpact = {
  args: { impact: 'Low' }
}

export const MediumImpact = {
  args: { impact: 'Medium' }
}

export const HighImpact = {
  args: { impact: 'High' }
}

export const BlockerImpact = {
  args: { impact: 'Blocker' }
}