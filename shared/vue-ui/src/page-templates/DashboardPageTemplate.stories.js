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
   rule: {
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

const score = {
  value: "C",
      label: "Your app is not fully optimized.",
      description: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
      tips: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
      projectKey: 'FOO',
      branch: 'main',
      minorSeverities: 15,
      majorSeverities: 3,
      criticalSeverities: 0
}

const priorityRule = {
  percentage: 25,
  ruleKey: "typescript:S1774",
  ruleName: "Avoid Using Ternary Operator",
  ruleHtmlDesc: `
      Ternary expressions, while concise, can often lead to code that is difficult 
      to read and understand, especially when they are nested or complex. 
      Prioritizing readability fosters maintainability and reduces the likelihood of bugs.
      Therefore, they should be removed in favor of more explicit control structures,
      such as if/else statements, to improve the clarity and readability of the code.
  `,
  ruleImpact: "High",
  ruleMetricTags: ["Maintenance"]
}

const cpuTag = {
  name: "CPU",
  nbRules: 19,
  optimizedRules: 12, 
  minorIssues: 40,
  majorIssues: 19,
  criticalIssues: 0,
  projectKey: 'foo',
  branch: 'main'
}

const ramTag = {
  name: "RAM",
  nbRules: 17,
  optimizedRules: 11, 
  minorIssues: 40,
  majorIssues: 17,
  criticalIssues: 0,
  projectKey: 'foo',
  branch: 'main'
}

const networkTag = {
  name: "Network",
  nbRules: 1,
  optimizedRules: 1,
  minorIssues: 0,
  majorIssues: 0,
  criticalIssues: 0,
  projectKey: 'foo',
  branch: 'main'
}

const diskTag = {
  name: "Disk",
  nbRules: 1,
  optimizedRules: 1, 
  minorIssues: 0,
  majorIssues: 0,
  criticalIssues: 0,
  projectKey: 'foo',
  branch: 'main'
}

export const CompleteDashboard = {
  args: {
    score,
    priorityRule,
    metricTags: [
      cpuTag,
      ramTag,
      networkTag,
      diskTag
    ]
  }
};

export const WithoutNetworkAndDiskTags = {
  args: {
    score,
    priorityRule,
    metricTags: [
      cpuTag,
      ramTag
    ]
  }
};

export const WithoutScore = {
  args: {
    priorityRule,
    metricTags: [
      cpuTag,
      ramTag,
      networkTag,
      diskTag
    ]
  }
};

export const WithoutPriorityRule = {
  args: {
    score,
    metricTags: [
      cpuTag,
      ramTag,
      networkTag,
      diskTag
    ]
  }
};

export const WithoutMetricTags = {
  args: {
    score,
    priorityRule,
    metricTags: []
  }
};
