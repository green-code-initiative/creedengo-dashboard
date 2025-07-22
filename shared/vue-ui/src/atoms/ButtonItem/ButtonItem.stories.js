import ButtonItem from './ButtonItem.vue'

export default {
  title: 'Atoms/Button',
  component: ButtonItem,
  tags: ['autodocs'],
  argTypes: {
    default: { control: 'text', default: 'click me' },
    type: {
      control: { type: 'inline-radio' },
      options: ['standard', 'primary'],
      default: 'standard'
    }
  }
}

export const StandardButton = {
  args: { default: 'click me', type: 'standard' }
}
export const PrimaryButton = {
  args: { default: 'click me', type: 'primary' }
}
