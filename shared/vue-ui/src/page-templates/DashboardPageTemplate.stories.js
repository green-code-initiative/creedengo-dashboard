import DashboardPageTemplate from "./DashboardPageTemplate.vue";

// export default {
//   title: "Page Templates/DashboardPageTemplate",
//   component: DashboardPageTemplate,
//   tags: ['autodocs']
// };

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
 title: 'Page Templates/DashboardPageTemplate',
 component: DashboardPageTemplate,
 tags: ['autodocs'],
 argTypes: {
   score: {
     control: {
       type: 'text'
     },
     required: true
   },
   scoreBlock: {
     control: {
       type: 'object'
     },
     required: true
   },
   tags: {
     control: {
       type: 'array'
     },
     required: true
   }
  }
};

export const ScoreAndTags = {
  args: {
    score: "C",
    scoreBlock: {
      labelBold: "Your app is not fully optimized.",
      label: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
      labelLong: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
      projectKey: 'FOO',
      branch: 'main',
      minorSeverities: 15,
      majorSeverities: 3,
      criticalSeverities: 0
    },
    tags: [
      {
        metricTag: "CPU",
        nbRules: 19,
        optimizedRules: 12, 
        minorIssues: 40,
        majorIssues: 19,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      },
      {
        metricTag: "RAM",
        nbRules: 17,
        optimizedRules: 11, 
        minorIssues: 40,
        majorIssues: 17,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      },
      {
        metricTag: "Network",
        nbRules: 1,
        optimizedRules: 1,
        minorIssues: 0,
        majorIssues: 0,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      },
      {
        metricTag: "Disk",
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
    score: "C",
    scoreBlock: {
      labelBold: "Your app is not fully optimized.",
      label: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
      labelLong: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
      projectKey: 'FOO',
      branch: 'main',
      minorSeverities: 15,
      majorSeverities: 3,
      criticalSeverities: 0
    },
    tags: [
      {
        metricTag: "CPU",
        nbRules: 19,
        optimizedRules: 12, 
        minorIssues: 40,
        majorIssues: 19,
        criticalIssues: 0,
        projectKey: 'foo',
        branch: 'main'
      },
      {
        metricTag: "RAM",
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

export const ScoreWithoutTags = {
  args: {
    score: "C",
    scoreBlock: {
      labelBold: "Your app is not fully optimized.",
      label: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
      labelLong: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
      projectKey: 'FOO',
      branch: 'main',
      minorSeverities: 15,
      majorSeverities: 3,
      criticalSeverities: 0
    },
    tags: []
  }
};