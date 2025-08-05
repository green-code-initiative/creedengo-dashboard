import ConnectionForm from './TokenConnectionForm.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Organisms/Token Connection Form',
  component: ConnectionForm,
  tags: ['autodocs'],
  argTypes: {
    authenticator: { control: { type: 'object' } }
  }
}

const validCredentials = {
  ['https://valid-sonar-url.com']: { 
    ['valid+55522gerhre-554htrh-13{enter}']: true 
  }
}

const authenticator = {
  connectionDetails: {},
  pending: false,
  connected: false,
  // disconnexion emulation
  async disconnect() {
    this.connected = false
  },
  // connexion emulation
  async connect(url, token) {
    this.pending = true
    const successStatus = Boolean(validCredentials?.[url]?.[token])
    this.connected = successStatus
    this.connectionDetails = successStatus ? { url, token } : {}
    if (!successStatus) {
      throw new Error('Connection Failed!')
    } 
  }
}

// for more readeable interaction scenarrio
const hitKey = { Enter: '{enter}' }

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const WaitingAuthentication = {
  args: { authenticator },
}

export const PendingConnection = {
  args: { authenticator: { ...authenticator, pending: true }},
}

export const ConnexionError = {
  args: { authenticator },

  play: async ({ canvas, userEvent }) => {
    const serverInput = canvas.getByLabelText('Server URL', {
      selector: 'input',
    });
    await userEvent.type(serverInput, 'https://wrong-sonar-url.com', {
      delay: 100,
    });
    const tokenInput = canvas.getByLabelText('Authentication Token', {
      selector: 'input',
    });
    await userEvent.type(tokenInput, `wrong+55522gerhre-554htrh-13${hitKey.Enter}`, {
      delay: 100,
    });
  },
}

export const ConnexionSuccess = {
  args: { authenticator },

  play: async ({ canvas, userEvent }) => {
    const serverInput = canvas.getByLabelText('Server URL', {
      selector: 'input',
    });
    await userEvent.type(serverInput, 'https://valid-sonar-url.com', {
      delay: 100,
    });
    const tokenInput = canvas.getByLabelText('Authentication Token', {
      selector: 'input',
    });
    await userEvent.type(tokenInput, `valid+55522gerhre-554htrh-13${hitKey.Enter}`, {
      delay: 100,
    });
  },
}
