
<script setup>
import { computed } from 'vue';

import BulletCriticity from '../BulletCriticities/BulletCriticity.vue';

const props = defineProps({
  metricTag: { type: String, required: true },
  optimizedRules: { type: Number, required: true },
  minorIssues: { type: Number, required: true },
  majorIssues: { type: Number, required: true },
  criticalIssues: { type: Number, required: true },
  projectLink: { type: String, required: true },
});

const criticities = computed(() => [
  {
    criticity: 'minor',
    text: 'Low',
    issues: props.minorIssues,
  },
  {
    criticity: 'major',
    text: 'Medium',
    issues: props.majorIssues,
  },
  {
    criticity: 'critical',
    text: 'High',
    issues: props.criticalIssues,
  },
]);
</script>

<template>
  <ul class="rules-criticities">
    <BulletCriticity
      impact="Optimized"
      type="rule"
      :metric-tag="props.metricTag"
      :count="`${optimizedRules}`"
      :project-link="props.projectLink"
    />
    <BulletCriticity
      v-for="criticity in criticities"
      :key="criticity.criticity"
      type="issue"
      :metric-tag="props.metricTag"
      :impact="criticity.criticity"
      :count="criticity.issues"
      :project-key="props.projectKey"
    />
  </ul>
</template>

<style scoped>
.rules-criticities {
  width: 100%;
  height: 100%;
  border-left: 1px solid #f2f2f2;
  padding-left: 25px;
}
.clickable {
  cursor: pointer;
}
.bullet-not-covered::before {
  background-color: #bbbbbb;
}
.bullet-optimized::before {
  background-color: #85bb2f;
}
</style>

<style>
.criticity {
  display: flex;
  font-size: 12px;
  margin-bottom: 17px;
}
.criticity strong {
  margin-left: auto;
  margin-right: 10px;
}
.bullet::before {
  content: '';
  display: inline-block;
  vertical-align: middle;
  margin-top: -1px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  background-color: black;
  margin-right: 3px;
}
.no-issues {
  margin-right: 17px !important;
}
</style>
