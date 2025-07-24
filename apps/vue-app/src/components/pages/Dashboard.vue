<script setup>
import ScoreWidget from '../Widgets/Score/ScoreWidget.vue';
</script>

<template>
  <header>
    <h1>Creedengo Dashboard</h1>
  </header>
  <main>
    <ScoreWidget
      project-key="my-project-key"
      branch="main"
    />
  </main>
</template>
