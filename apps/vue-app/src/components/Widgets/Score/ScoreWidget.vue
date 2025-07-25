<template>
  <div class="wrapper">
    <span v-if="!state.score">
      <i class="fa fa-spinner fa-spin" /> Loading score...
    </span>
    <span v-else-if="state.error">
      <i class="fa fa-exclamation-triangle" /> Score not available - {{ state.error }}
    </span>
    <span v-else>
      <AbcdeScore :value="state.score" />
    </span>
  </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue';

import { AbcdeScore } from '@creedengo/vue-ui'
import SonarAPI from '@creedengo/sonar-services'
import core from '@creedengo/core-services';

const { api, calculateProjectScore } = core;

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
    state.score = await calculateProjectScore({ ...props });
  } catch (error) {
    state.score = 'N/A';
    console.error('Error fetching score:', error);
    state.error = JSON.stringify(Object.values(error));
  }
});
</script>

<style scoped>
.wrapper {
  display: flex;
  place-items: flex-start;
  flex-wrap: wrap;
}
</style>