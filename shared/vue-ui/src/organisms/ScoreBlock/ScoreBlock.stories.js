import ScoreBlock from "./ScoreBlock.vue";


// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
    title: 'Organisms/ScoreBlock',
    component: ScoreBlock,
    tags: ['autodocs'],
    argTypes: {
        score: {
            control: { type: 'select' },
            options: ['A', 'B', 'C', 'D', 'E', ''],
            describe: 'Score to display in the block',
            defaultValue: ''
        },
        labelBold: { control: { type: 'text' }, required: false },
        label: { control: { type: 'text' }, required: false },
        labelLong: { control: { type: 'text' }, required: false },
        nbSeverity: { control: { type: 'number', min: 0 }, required: true },
        projectKey: { control: { type: 'text' }, required: true },
        branch: { control: { type: 'text' }, required: true },
        minorSeverities: { control: { type: 'number', min: 0 }, required: true },
        majorSeverities: { control: { type: 'number', min: 0 }, required: true },
        criticalSeverities: { control: { type: 'number', min: 0 }, required: true },
    }
};

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const ScoreA = {
  args: { 
    value: "A",
    labelBold: "Your app is fully optimized, congratulations!",
    label: "Don't forget to check it again if you update your app.",
    labelLong: "100 % optimized, congrats!",
    projectKey: 'FOO',
    branch: 'main',
    minorSeverities: 0,
    majorSeverities: 0,
    criticalSeverities: 0,
  }
}
export const ScoreB = {
  args: { 
    value: "B",
    labelBold: "Your app is nearly optimized.",
    label: "Well done! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.",
    labelLong: "You have between 1 and 9 minor severities.",
    projectKey: 'FOO',
    branch: 'main',
    minorSeverities: 8,
    majorSeverities: 0,
    criticalSeverities: 0,
  }
}

export const ScoreC = {
  args: { 
    value: "C",
    labelBold: "Your app is not fully optimized.",
    label: "Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
    labelLong: "You have between 10 and 19 minor severities or you have 1 or many major severity.",
    projectKey: 'FOO',
    branch: 'main',
    minorSeverities: 15,
    majorSeverities: 3,
    criticalSeverities: 0,
  }
}

export const ScoreD = {
  args: { 
    value: "D",
    labelBold: "Many elements of your application can be optimized.",
    label: "Don't worry! You can start by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
    labelLong: "You have more than 20 minor severities or more than 10 major severities or 1 or many critical severities.",
    projectKey: 'FOO',
    branch: 'main',
    minorSeverities: 25,
    majorSeverities: 13,
    criticalSeverities: 0,
  }
}

export const ScoreE = {
  args: { 
    value: "E",
    labelBold: "Several elements of your application can be optimized.",
    label: "Don't worry! You can start by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.", 
    labelLong: "You have 1 or more than 1 blocker severities.",
    projectKey: 'FOO',
    branch: 'main',
    minorSeverities: 127,
    majorSeverities: 34,
    criticalSeverities: 7,
  }
}