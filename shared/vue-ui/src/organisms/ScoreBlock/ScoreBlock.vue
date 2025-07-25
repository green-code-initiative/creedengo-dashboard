<script setup>
import AbcdeScore from '../../molecules/AbcdeScore/AbcdeScore.vue';
import labels from '../../../../../apps/vue-app/src/assets/data/labels.js';
import { computed } from 'vue';

const props = defineProps({
  value: {
    type: String,
    required: false,
    default: '',
    validator(value) {
      return ['A', 'B', 'C', 'D', 'E', ''].includes(value)
    }
  }
});

const scoreLabels = computed(() => {
    const texts = labels.scores.find(s => s.score === props.value);
    if (!texts) {
      throw new Error(`Score ${props.value} not found in labels.`);
    }
    return texts;
});
</script>

<template>
  <h2>Your eco-design score is</h2>
  <AbcdeScore :value="props.value" />
  <strong>{{ scoreLabels.labelBold }}</strong>
  <p>{{ scoreLabels.label }}</p>
</template>


<style scoped>
</style>
