<script setup>
import { onMounted, reactive } from 'vue';
import { ScoreBlock } from '@creedengo/vue-ui'
import SonarAPI from '@creedengo/sonar-services'
import core from '@creedengo/core-services';

const { api, fetchScoreBlockData } = core;

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

const state = reactive({});

onMounted(async () => {
  const data = await fetchScoreBlockData(props)
  Object.assign(state, { ...data, ...props })
});
</script>

<template>
  <div class="score-block">
    <ScoreBlock
      :value="state.score"
      :error="state.error"
      :label="state.label"
      :description="state.description"
      :tips="state.tips"
      :baseUrl="state.baseUrl"
      :projectKey="state.projectKey"
      :branch="state.branch"
      :minorSeverities="state.minorSeverities"
      :majorSeverities="state.majorSeverities"
      :criticalSeverities="state.criticalSeverities"
    />
  </div>
</template>

<style scoped>

.score-block {
  margin: 0 2rem 0 2.625rem;
  background-color: none;
  flex: 0 0 38%;
  height: 320px;
}
.wrapper {
  display: flex;
  place-items: flex-start;
  flex-wrap: wrap;
}
</style>
