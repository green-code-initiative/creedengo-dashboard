import PriorityRule from './PriorityRule.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Design System/Organisms/PriorityRule',
  component: PriorityRule,
  tags: ['autodocs'],
  argTypes: {
    percentage: { control: { type: 'number', min: 0 }, required: true },
    ruleKey: { control: { type: 'text' }, required: false },
    ruleName: { control: { type: 'text' }, required: false },
    ruleHtmlDesc: { control: { type: 'text' }, required: false },
    ruleImpact: { control: { type: 'select' }, options: ['Optimized', 'Info', 'Low', 'Medium', 'High', 'Blocker'] },
    ruleMetricTags: { control: { type: 'object' } },
  }
}

export const AvoidUsingTernaryOperator = {
    args: {
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
}