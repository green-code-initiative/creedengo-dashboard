
<script setup>
import { computed } from 'vue';

import BulletCriticity from '../BulletCriticity/BulletCriticity.vue';

const props = defineProps({
  metricTag: { type: String, required: false, default: '' },
  optimizedRules: { type: Number, required: false, default: 0 },
  minorIssues: { type: Number, required: false, default: 0 },
  majorIssues: { type: Number, required: false, default: 0 },
  criticalIssues: { type: Number, required: false, default: 0 },
  link: { type: String, required: true },
});

const levels = computed(() => [
  { name: 'Low', count: props.minorIssues },
  { name: 'Medium', count: props.majorIssues },
  { name: 'High', count: props.criticalIssues },
]);

const link = computed(() => `${props.link}&tags=${props.metricTag.toLowerCase()}`)
</script>

<template>
  <ul class="rules-criticities">
    <BulletCriticity
      v-if="!isNaN(optimizedRules)"
      impact="Optimized"
      :label="`${props.metricTag} rules`"
      :count="`${optimizedRules}`"
    />
    <BulletCriticity
      v-for="{ name, count } in levels"
      :key="name"
      :impact="name"
      :count="count"
      :link="`${link}&severities=${name.toUpperCase()}`"
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
</style>
