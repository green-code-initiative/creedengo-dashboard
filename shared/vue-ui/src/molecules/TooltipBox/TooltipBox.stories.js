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
}

const Template = (args) => ({
  components: { TooltipBox },
  setup() {
    return { args }
  },
  template: `
    <p style='text-decoration: underline dashed 1px'>Mouse hover this text to display the tooltip content.</p>
    <tooltip-box v-bind="args">{{ args.default }}</tooltip-box>
  `,
});

export const WithAnchor = {
  args: { 
    anchor: true,
    default: "Hyper Text Markup Language"
  }
}
 
export const UseParentOnlyAsAnchor = Template.bind({});
UseParentOnlyAsAnchor.args = {
  default: "Hyper Text Markup Language",
  anchor: false,
}
