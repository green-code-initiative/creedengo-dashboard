import TooltipBox from './TooltipBox.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Molecules/TooltipBox',
  component: TooltipBox,
  tags: ['autodocs'],
  argTypes: {
    anchor: { control: { type: 'boolean' }, required: false, default: false },
    default: { control: { type: 'text' }, required: true },
  },
  template: `
      <p>
        The
        <abbr>
          HTML
          <tooltip-box v-bind="args">
          </tooltip-box>
        </abbr>
        abbr element.
      </p>
  `,
}

export const WithAnchor = {
  args: { 
    anchor: true,
    default: "Hyper Text Markup Language"
  }
}

export const UseParentOnlyAsAnchor = {
  args: { 
    default: "Hyper Text Markup Language"
  },
}
