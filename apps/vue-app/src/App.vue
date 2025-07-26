<script setup>
import ScoreWidget from './components/Widgets/Score/ScoreWidget.vue';

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
  <header>
    <h1>Creedengo Dashboard</h1>
  </header>
  <main>
    <ScoreWidget
      :project="props.project"
      :branch="props.branch"
    />
  </main>
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
