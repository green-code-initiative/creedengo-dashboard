import DashboardPageTemplate from "./DashboardPageTemplate.vue";

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
 title: 'Page Templates/DashboardPageTemplate',
 component: DashboardPageTemplate,
 tags: ['autodocs'],
 argTypes: {
   score: {
     control: {
       type: 'object'
     },
     required: true
   },
   metricTags: {
     control: {
       type: 'array'
     },
     required: true
   }
  }
};

export const ScoreAndMetricTags = {
  args: {
    score: {
      value: "C",
      label: "Your app is not fully optimized.",
      description: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
      tips: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
      projectKey: 'FOO',
      branch: 'main',
      minorSeverities: 15,
      majorSeverities: 3,
      criticalSeverities: 0
    },
    metricTags: [
      {
        name: "CPU",
        nbRules: 19,
        optimizedRules: 12, 
        minorIssues: 40,
        majorIssues: 19,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      },
      {
        name: "RAM",
        nbRules: 17,
        optimizedRules: 11, 
        minorIssues: 40,
        majorIssues: 17,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      },
      {
        name: "Network",
        nbRules: 1,
        optimizedRules: 1,
        minorIssues: 0,
        majorIssues: 0,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      },
      {
        name: "Disk",
        nbRules: 1,
        optimizedRules: 1, 
        minorIssues: 0,
        majorIssues: 0,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      }
    ]
  }
};

export const ScoreWithCPUAndRAM = {
  args: {
    score: {
      value: "C",
      label: "Your app is not fully optimized.",
      description: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
      tips: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
      projectKey: 'FOO',
      branch: 'main',
      minorSeverities: 15,
      majorSeverities: 3,
      criticalSeverities: 0
    },
    metricTags: [
      {
        name: "CPU",
        nbRules: 19,
        optimizedRules: 12, 
        minorIssues: 40,
        majorIssues: 19,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      },
      {
        name: "RAM",
        nbRules: 17,
        optimizedRules: 11, 
        minorIssues: 40,
        majorIssues: 17,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      }
    ]
  }
};

export const ScoreWithoutMetricTags = {
  args: {
    score: {
      value: "C",
      label: "Your app is not fully optimized.",
      description: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
      tips: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
      projectKey: 'FOO',
      branch: 'main',
      minorSeverities: 15,
      majorSeverities: 3,
      criticalSeverities: 0
    },
    metricTags: []
  }
};