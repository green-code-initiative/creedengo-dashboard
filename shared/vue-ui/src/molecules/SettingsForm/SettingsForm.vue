<script setup>
import { onMounted, reactive, defineProps } from 'vue';

const { storage } = defineProps({
  storage: {
    type: Object,
    required: true,
    validator(value) {
      return (
        typeof value.get === 'function' &&
        typeof value.set === 'function' 
      )
    }
  }
})

const data = reactive({
  project: '',
  server: '',
  token: '',
});

onMounted(async () => {
  data.project = await storage.get('project')
  data.server = await storage.get('server')
  data.token = await storage.get('token')
});


async function onSubmit(event) {
  event.preventDefault()
  await storage.set('project', data.project)
  await storage.set('server', data.server)
  await storage.set('token', data.token)
  globalThis.alert("Settings Saved Successfully")
  return false;
}

</script>

<template>
  <form
    name="configurationForm"
    :onSubmit="onSubmit"
  >
    <div>
      <label for="project">Project Key</label><sup>*</sup>
    </div>
    <div>
      <input
        id="project"
        :value="data.project"
        type="text"
        minLength="5"
        pattern="[a-zA-Z0-9-_](5,*)"
        required
      >
    </div>
    <div>
      <label for="server">Server URL</label><sup>*</sup>
    </div>
    <div>
      <input 
        id="server" 
        :value="data.server"
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
        :value="data.token"
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
  </form>
  <details>
    <summary>Instructions</summary>
    <p><u>Project Key:</u> The Sonarqube identifier of your project.</p>
    <ul>
      <li>
        This identifier is usually availlable in the <code>sonar-project.properties</code> file.
        Its property name in the file is <code>sonar.projectKey</code>
      </li>
    </ul>
    <p><u>Server URL:</u> The URL of the Sonarqube Server hosting the project analyses.</p>
    <ul>
      <li>
        If no Server URL is provided, Sonarcloud will be used. 
        But beware it does not support community plugins like the Creedengo ones yet.
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
