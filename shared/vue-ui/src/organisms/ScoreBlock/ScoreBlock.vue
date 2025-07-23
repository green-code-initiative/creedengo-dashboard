<script setup>
import { ref, computed } from 'vue';
import AbcdeScore from '@components/molecules/AbcdeScore/AbcdeScore.vue';
import SensitizationTooltip from '../../molecules/SensitizationTooltip/SensitizationTooltip.vue';

const props = defineProps({
  score: { type: String, required: true, 
    validator(value) {
      return ['A', 'B', 'C', 'D', 'E'].includes(value)
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

function showScoreTooltip() {
  refSensitizationTooltip.value.show();

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
  <div class="title">Your eco-design score is</div>
  <AbcdeScore :value="score" />
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
.title {
  font-weight: 700;
  margin-bottom: 30px;
  font-size: 20px;
}
.rate-description {
  padding-top: 20px;
}
.tooltip-calc {
  margin-top: 20px;
  font-size: 10px;
  text-decoration: underline;
  display: inline-block;
}
</style>
