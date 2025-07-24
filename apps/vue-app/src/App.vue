<script setup>
import { ref, computed } from 'vue'
import Configuration from './components/pages/Settings.vue';
import Dashboard from './components/pages/Dashboard.vue';
import NotFound from './components/pages/NotFound.vue';

const routes = {
  '/': Dashboard,
  '/settings': Configuration
}

const currentPath = ref(window.location.hash)

window.addEventListener('hashchange', () => {
  currentPath.value = window.location.hash
})

const currentView = computed(() => {
  return routes[currentPath.value.slice(1) || '/'] || NotFound
})

</script>

<template>
  <component :is="currentView" />
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
