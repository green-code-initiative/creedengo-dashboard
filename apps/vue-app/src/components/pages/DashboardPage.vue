<script setup>
import { onMounted, reactive } from 'vue';

import SonarAPI from '@creedengo/sonar-services'
import core from '@creedengo/core-services';
import { DashboardPageTemplate } from '@creedengo/vue-ui';

const { api, calculateProjectScore, getScoreTexts } = core;

api.init(SonarAPI)
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
    const value = await calculateProjectScore({ ...props });
    const { label, description, tips } = getScoreTexts(value);
    state.score = { value, label, description, tips };
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
        <i class="fa fa-spinner fa-spin" /> Loading score...
      </span>
      <span v-else-if="state.error">
        <i class="fa fa-exclamation-triangle" /> Score not available - {{ state.error }}
      </span>
      <span v-else>
        <DashboardPageTemplate :score="state.score" />
      </span>
    </div>
  </main>
</template>
