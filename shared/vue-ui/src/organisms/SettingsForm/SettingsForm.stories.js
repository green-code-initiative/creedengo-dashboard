import SettingsForm from './SettingsForm.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Organisms/SettingsForm',
  component: SettingsForm,
  tags: ['autodocs'],
  argTypes: {
    storage: { control: { type: 'object' } }
  }
}

const memoryStorage = {
  values: {},
  get(key) { return this.values[key] },
  set(key, value) { this.values[key] = value } 
}

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const MemoryStorage = {
  args: { storage: memoryStorage },

  play: async ({ canvas, userEvent }) => {
    const projectInput = canvas.getByLabelText('Project Key', {
      selector: 'input',
    });
    await userEvent.type(projectInput, 'green-code-initiative_creedengo-dashboard', {
      delay: 100,
    });
    const serverInput = canvas.getByLabelText('Server URL', {
      selector: 'input',
    });
    await userEvent.type(serverInput, 'https://my-sonar-server.com', {
      delay: 100,
    });
    const tokenInput = canvas.getByLabelText('Authentication Token', {
      selector: 'input',
    });
    await userEvent.type(tokenInput, '16646+55522gerhre-554htrh-13{enter}', {
      delay: 100,
    });
  },
}

