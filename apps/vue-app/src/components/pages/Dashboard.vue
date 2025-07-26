<script setup>
import ScoreWidget from '../Widgets/Score/ScoreWidget.vue';

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
