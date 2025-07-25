import ScoreBlock from "./ScoreBlock.vue";


// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
    title: 'Design System/Organisms/ScoreBlock',
    component: ScoreBlock,
    tags: ['autodocs'],
    argTypes: {
        score: {
            control: { type: 'select' },
            options: ['A', 'B', 'C', 'D', 'E', ''],
            describe: 'Score to display in the block',
            defaultValue: ''
        }
    }
};

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const A = {
    args: {
        value: 'A'
    }
}
  
export const B = {
    args: {
        value: 'B',
    }
}

export const C = {
    args: {
        value: 'C'
    }
}

export const D = {
    args: {
        value: 'D'
    }
}

export const E = {
    args: {
        value: 'E'
    }
}