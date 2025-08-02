
<script setup>
import { computed } from 'vue';
import IconArrowRight from '../../design-tokens/icons/IconArrowRight.vue';
import ImpactTag from '../../atoms/ImpactTag/ImpactTag.vue';

const props = defineProps({
  count: { type: Number, required: true },
  impact: { type: String, required: false, default: 'Optimized',
    validator(value) {
      return ['Low', 'Medium', 'High', 'Optimized'].includes(value)
    }
  },
  metricTag: { type: String, required: false, default: '',
    validator(value) {
      return ['CPU', 'RAM', 'Disk', 'Network'].includes(value)
    }
  },
  projectLink: { type: String, required: true },
});

const isRule = computed(() => props.impact === 'Optimized');
const hasIssues = computed(() => !isRule.value && props.count > 0);
const customLabel = computed(() => isRule.value ? `${props.metricTag} rules` : 'impact issues');

function onClickOpenIssues(impact) {
  globalThis.window.open(`${props.projectLink}&severities=${impact.toUpperCase()}`);
}
</script>


<template>
  <li
    :class="[
      'criticity',
      'bullet',
      `bullet-${impact}-impact`,
      { clickable: isIssues },
    ]"
    @click="hasIssues ? onClickOpenIssues(impact) : null"
    @keyUp="hasIssues ? onClickOpenIssues(impact) : null"
  >
    &nbsp;&nbsp;<ImpactTag
                  :impact="`${impact}`" 
                  :custom-label="`${customLabel}`"
                />
    <strong :class="{ 'no-issues': !hasIssues }">{{ count }}</strong>
    <IconArrowRight v-if="hasIssues" />
  </li>
</template>

<style scoped>
.bullet-minor-impact::before {
  background-color: #fecb02;
}
.bullet-major-impact::before {
  background-color: #ff8e12;
}
.bullet-critical-impact::before {
  background-color: #e30021;
}
</style>
