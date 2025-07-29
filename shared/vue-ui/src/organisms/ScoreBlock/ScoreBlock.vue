<script setup>
import { ref, computed } from 'vue';
import AbcdeScore from '../../molecules/AbcdeScore/AbcdeScore.vue';
import SensitizationTooltip from '../../molecules/SensitizationTooltip/SensitizationTooltip.vue';
import { nextTick, onMounted } from 'vue';

const props = defineProps({
  value: {
    type: String,
    required: false,
    default: '',
    validator(value) {
      return ['A', 'B', 'C', 'D', 'E', ''].includes(value)
    }
  },
  labelBold: { type: String, required: true },
  label: { type: String, required: true },
  labelLong: { type: String, required: true },
  projectKey: { type: String, required: true },
  branch: { type: String, required: true },
  minorSeverities: { type: Number, required: true },
  majorSeverities: { type: Number, required: true },
  criticalSeverities: { type: Number, required: true },
});

const refSensitizationTooltip = ref(null);
const scoreTooltipClosable = ref(true);

onMounted(() => {
  console.log('-----PARENT MOUNTED-----');
  console.log('TOOLTIP REF:', refSensitizationTooltip.value);
  console.log('TYPEOF SHOW:', typeof refSensitizationTooltip.value?.show);
  console.log('TYPEOF HIDE:', typeof refSensitizationTooltip.value?.hide);
});

function showScoreTooltip() {
  nextTick(() => {
    if (refSensitizationTooltip.value?.show) {
      console.log('Calling show from parent component on mouseover');
      refSensitizationTooltip.value.show();
    } else {
      console.warn('Tooltip method show is not available in parent on mouseover');
    }
  });
  
  refSensitizationTooltip.value?.show?.();

  scoreTooltipClosable.value = false;
  setTimeout(() => {
    scoreTooltipClosable.value = true;
  }, 2000);
}
const projectLink = computed(() => {
    const id = props.projectKey
    const filter = `branch=${props.branch}&id=${id}&resolved=false`
    return `${window.baseUrl}/project/issues?${filter}`;
})
</script>

<template>
  <h2>Your eco-design score is</h2>
  <AbcdeScore :value="value" />
  <div class="rate-description">
    <strong>{{ labelBold }}</strong>
    <br>
    <br>
    {{ label }}
    <br>
    <div
      class="tooltip-calc"
      @mouseover="showScoreTooltip"
      @focusin="showScoreTooltip"
    >
      how my score is calculated ?
      <SensitizationTooltip
        ref="refSensitizationTooltip"
        :label="labelLong"
        :minor-severities="minorSeverities"
        :major-severities="majorSeverities"
        :critical-severities="criticalSeverities"
        :project-link="projectLink"
      />
    </div>
  </div>
</template>

<style scoped>
.rate-description {
  padding-top: 20px;
}
.tooltip-calc {
  margin-top: 20px;
  text-decoration: underline;
  display: inline-block;
}
</style>
