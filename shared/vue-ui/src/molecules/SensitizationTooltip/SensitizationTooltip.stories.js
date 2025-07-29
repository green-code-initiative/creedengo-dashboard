import SensitizationTooltip from './SensitizationTooltip.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Design System/Molecules/SensitizationTooltip',
  component: SensitizationTooltip,
  tags: ['autodocs'],
  argTypes: {
    label: { control: { type: 'text' }, required: false },
    projectLink: { control: { type: 'text' }, required: true },
    minorSeverities: { control: { type: 'number', min: 0 }, required: true },
    majorSeverities: { control: { type: 'number', min: 0 }, required: true },
    criticalSeverities: { control: { type: 'number', min: 0 }, required: true },
  }
}

export const ScoreA = {
  args: { 
    label: "100 % optimized, congrats!",
    projectLink: '/project/issues?branch=main&id=FOO&resolved=false',
    minorSeverities: 0,
    majorSeverities: 0,
    criticalSeverities: 0,
  }
}

export const ScoreB = {
  args: { 
    labelLong: "You have between 1 and 9 minor severities.",
    projectLink: '/project/issues?branch=main&id=FOO&resolved=false',
    minorSeverities: 8,
    majorSeverities: 0,
    criticalSeverities: 0,
  }
}

export const ScoreC = {
  args: { 
    label: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
    projectLink: '/project/issues?branch=main&id=FOO&resolved=false',
    minorSeverities: 15,
    majorSeverities: 3,
    criticalSeverities: 0,
  }
}

export const ScoreD = {
  args: { 
    label: "You have more than 20 minor severities or more than 10 major severities or 1 or many critical severities.",
    projectLink: '/project/issues?branch=main&id=FOO&resolved=false',
    minorSeverities: 25,
    majorSeverities: 13,
    criticalSeverities: 0,
  }
}

export const ScoreE = {
  args: { 
    label: "You have 1 or more than 1 blocker severities.",
    projectLink: '/project/issues?branch=main&id=FOO&resolved=false',
    minorSeverities: 127,
    majorSeverities: 34,
    criticalSeverities: 7,
  }
}