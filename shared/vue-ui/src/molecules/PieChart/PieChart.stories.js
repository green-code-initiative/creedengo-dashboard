import PieChart from './PieChart.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Molecules/PieChart',
  component: PieChart,
  tags: ['autodocs'],
  argTypes: {
    metricTag: { control: { type: 'select' }, options: ['CPU', 'RAM', 'Disk', 'Network'] },
    nbRules: { control: { type: 'number', min: 0 }, required: true },
    optimizedRules: { control: { type: 'number', min: 0 }, required: true },
    minorIssues: { control: { type: 'number', min: 0 }, required: true },
    majorIssues: { control: { type: 'number', min: 0 }, required: true },
    criticalIssues: { control: { type: 'number', min: 0 }, required: true },
  }
}

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const CpuPieChart = {
  args: { 
    metricTag: "CPU",
    nbRules: 19,
    optimizedRules: 12, 
    minorIssues: 40,
    majorIssues: 19,
    criticalIssues: 0,
  }
}

export const RamPieChart = {
  args: { 
    metricTag: "RAM",
    nbRules: 17,
    optimizedRules: 11, 
    minorIssues: 40,
    majorIssues: 17,
    criticalIssues: 0,
  }
}

export const NetworkPieChart = {
  args: { 
    metricTag: "Network",
    nbRules: 1,
    optimizedRules: 1,
    minorIssues: 0,
    majorIssues: 0,
    criticalIssues: 0,
  }
}

export const DiskPieChart = {
  args: { 
    metricTag: "Disk",
    nbRules: 1,
    optimizedRules: 1, 
    minorIssues: 0,
    majorIssues: 0,
    criticalIssues: 0,
  }
}