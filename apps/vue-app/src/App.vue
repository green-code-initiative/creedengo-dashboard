<script setup>
import ScoreWidget from './components/Widgets/Score/ScoreWidget.vue';
</script>

<template>
  <header>
    <h1>You did it!</h1>
  </header>
  <main>
    <p>
      You have successfully installed the Creedengo Dashboard plugin. This plugin is designed to help you
      monitor and improve the sustainability of your codebase.
    </p>
    <ScoreWidget
      project-key="my-project-key"
      branch="main"
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
