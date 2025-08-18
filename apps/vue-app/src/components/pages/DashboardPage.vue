<script setup>
import { onMounted, reactive } from 'vue';

import { AbcdeScore } from '@creedengo/vue-ui'
import core from '@creedengo/core-services';

const { calculateProjectScore } = core;

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

const state = reactive({ score: '', error: null });

onMounted(async () => {
  try {
    state.score = await calculateProjectScore({ ...props });
  } catch (error) {
    state.score = 'N/A';
    globalThis.console.error('Error fetching score:', error);
    state.error = JSON.stringify(Object.values(error));
  }
});
</script>

<template>
  <header>
    <h1>Creedengo Dashboard</h1>
  </header>
  <main>
    <div class="wrapper">
      <span v-if="!state.score">
        <em>Loading score...</em>
      </span>
      <span v-else-if="state.error">
        <em>Score not available - {{ state.error }}</em>
      </span>
      <span v-else>
        <AbcdeScore :value="state.score" />
      </span>
    </div>
  </main>
</template>
