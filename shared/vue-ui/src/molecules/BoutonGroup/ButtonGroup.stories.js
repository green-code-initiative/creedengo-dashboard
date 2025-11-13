import ButtonGroup from './ButtonGroup.vue'

export default {
  title: 'Mollecules/Button Group',
  component: ButtonGroup,
  tags: ['autodocs'],
  argTypes: {
    buttons: {
      control: { type: 'object' }
    }
  }
}

export const StandardButtonGroup = {
  args: {
    buttons: [
      { label: 'submit', type: 'primary' },
      { label: 'reset' },
      { label: 'cancel', type: 'standard' }
    ]
  }
}
