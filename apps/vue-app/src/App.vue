<script setup>
import { ref, computed } from 'vue'
import SettingsPage from './components/pages/SettingsPage.vue';
import DashboardPage from './components/pages/DashboardPage.vue';
import NotFoundPage from './components/pages/NotFoundPage.vue';

import { isSonarPlugin, isWebExtension, isWebsite } from './context';
import ConnectionPage from './components/pages/ConnectionPage.vue';

const routes = {
  '/': DashboardPage,
  '/settings': SettingsPage
}

const currentPath = ref(globalThis.location.hash)

globalThis.addEventListener('hashchange', () => {
  currentPath.value = globalThis.location.hash
})

const currentView = computed(() => {
  return routes[currentPath.value.slice(1) || '/'] || NotFoundPage
})


const props = defineProps({
  project: {
    type: String,
    required: true,
  },
  branch: {
    type: String,
    required: true,
  }
})
</script>

<template>
  <div v-if="isSonarPlugin">
    <h2>Creedengo Sonar Dashboard</h2>
    <DashboardPage
      :project="props.project"
      :branch="props.branch"
    />
  </div>
  <div v-if="isWebExtension">
    <header>
      <h1>Creedengo Extension Dashboard</h1>
    </header>
    <main>
      <component
        :is="currentView"
        :project="props.project"
        :branch="props.branch"
      />
    </main>
    <nav aria-labelledby="menu-web-extension">
      <strong id="menu-web-extension">Menu</strong>
      <a href="#/">Dashboard</a> |
      <a href="#/settings">Settings</a>
    </nav>
  </div>
  <div v-if="isWebsite">
    <header>
      <h1>Creedengo Website Dashboard</h1>
    </header>
    <main>
      <ConnectionPage></ConnectionPage>
    </main>
    <nav aria-labelledby="menu-website">
      <strong id="menu-website">Menu</strong>
      <a href="#/">Dashboard</a> |
      <a href="#/settings">Settings</a>
    </nav>
  </div>
</template>

<style scoped>
header {
  line-height: 1.5;
}

p {
  margin: 1em 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
