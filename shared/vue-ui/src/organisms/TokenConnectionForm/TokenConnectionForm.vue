<script setup>
import { onMounted, reactive, ref } from 'vue';

const { authenticator } = defineProps({
  authenticator: {
    // TODO - may use an "Authenticator Class"
    // It would then need to be shared by core and ui
    // we don't want direct dependencies between them
    type: Object,
    required: true,
    validator(value) {
      return (
        typeof value.connect === 'function' &&
        typeof value.disconnect === 'function' &&
        typeof value.connecting === 'boolean' &&
        typeof value.connected === 'boolean'
      )
    }
  }
})

const server = ref('')
const token = ref('')

const data = reactive({
  connected: false,
  pending: false,
  error: ''
});

onMounted(async () => {
  data.connected = authenticator.connected
  data.pending = authenticator.pending
});

async function disconnect() {
  data.pending = true
  await authenticator.disconnect()
  data.pending = false
  data.connected = authenticator.connected
}

async function connect() {
  data.pending = true
  try {
    await authenticator.connect(server.value, token.value)
    data.connected = authenticator.connected
  } catch (error) {
    data.error = error.message
  }
  data.pending = false
}

</script>

<template>
  <!-- should use a notification component -->
  <div v-if="data.error">
    <p>Error: {{ data.error }}</p>
  </div>
  <div v-if="data.pending">
    <p>Request Sent... Waiting Server Response...</p>
  </div>
  <form
    v-if="data.connected"
    name="disconnectionForm"
    @submit.prevent="disconnect"
  >
    <input
      type="submit"
      value="Disconnect"
      :disabled="data.pending"
    >
  </form>
  <form
    v-else
    name="connectionForm"
    @submit.prevent="connect"
  >
    <fieldset :disabled="data.pending">
      <div>
        <label for="server">Server URL</label><sup>*</sup>
      </div>
      <div>
        <input 
          id="server" 
          v-model="server"
          type="url"
          required
        >
      </div>
      <div>
        <label for="token">Authentication Token</label><sup>*</sup>
      </div>
      <div>
        <input
          id="token"
          v-model="token"
          type="password"
          minLength="20"
          required
        >
      </div>
      <div>
        <input type="reset">
        &nbsp;
        <input type="submit">
      </div>
    </fieldset>
  </form>
  <details>
    <summary>Instructions</summary>
    <p><u>Server URL:</u> The URL of the Sonarqube Server or Sonarcloud hosting the project analyses.</p>
    <ul>
      <li>
        Beware Sonarcloud does not support community plugins like the Creedengo ones yet.
      </li>
    </ul>
    <p><u>Authentication Token:</u> A token generated from your Sonarqube profile settings.</p>
    <p><u>General Considerations:</u></p>
    <ul>
      <li>
        Be sure to have installed Creedengo Sonar plugins related to your programming languages.
      </li>
      <li>
        Without Creedengo plugins, only issues provided by sonar with the `sustainability` tag will
        be supported. You'll probably get a "better" score, but it will be much less relevent, and
        your final carbon footprint will be harder to reduce without the Creedengo provided rules.
      </li>
    </ul>
  </details>
</template>
