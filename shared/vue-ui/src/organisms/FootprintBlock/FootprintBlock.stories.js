import FootprintBlock from "./FootprintBlock.vue";


// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
    title: 'Organisms/FootprintBlock',
    component: FootprintBlock,
    tags: ['autodocs'],
    argTypes: {
        score: { control: { type: 'number', min: 0 }, required: true },
    }
};

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const Score0 = {
  args: { 
    score: 0,
  }
}
export const Score15 = {
  args: { 
    score: 0.15,
  }
}