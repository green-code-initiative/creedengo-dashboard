import AbcdeScore from './AbcdeScore.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Design System/Molecules/ABCDE Score',
  component: AbcdeScore,
  tags: ['autodocs'],
  argTypes: {
    value: { control: { type: 'select' }, options: ['A', 'B', 'C', 'D', 'E'] }
  }
}

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const A = {
  args: {
    value: 'A'
  }
}

export const B = {
  args: {
    value: 'B'
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
    value: 'E '
  }
}
