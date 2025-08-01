import RuleCriticities from './RuleCriticities.vue'

const projectLink = 'project/issues?branch=$main&id=my-project&resolved=false&tags='

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Molecules/RuleCriticities',
  component: RuleCriticities,
  tags: ['autodocs'],
  argTypes: {
    metricTag: { control: { type: 'select' }, options: ['CPU', 'RAM', 'Disk', 'Network'] },
    optimizedRules: { control: { type: 'number', min: 0 }, required: true },
    minorIssues: { control: { type: 'number', min: 0 }, required: true },
    majorIssues: { control: { type: 'number', min: 0 }, required: true },
    criticalIssues: { control: { type: 'number', min: 0 }, required: true },
    projectLink: { control: { type: 'text' }, required: true },
  }
}

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const OptimizedImpact = {
  args: { 
    metricTag: "CPU",
    optimizedRules: 12, 
    minorIssues: 40,
    majorIssues: 19,
    criticalIssues: 0,
    projectLink: projectLink + 'cpu'
  }
}