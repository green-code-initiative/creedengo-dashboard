<script setup>
import { ref, computed } from 'vue'
import SettingsPage from './components/pages/SettingsPage.vue';
import DashboardPage from './components/pages/DashboardPage.vue';
import NotFoundPage from './components/pages/NotFoundPage.vue';

const routes = {
  '/': DashboardPage,
  '/settings': SettingsPage
}

const currentPath = ref(window.location.hash)

window.addEventListener('hashchange', () => {
  currentPath.value = window.location.hash
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
  <component
    :is="currentView"
    :project="props.project"
    :branch="props.branch"
  />
  <aside>
    <a href="#/">Dashboard</a> |
    <a href="#/settings">Settings</a>
  </aside>
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
