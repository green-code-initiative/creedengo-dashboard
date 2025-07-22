<script setup>
import { computed } from 'vue';

const props = defineProps({
  metricTag: { type: String, required: true },
  nbRules: { type: Number, required: true },
  optimizedRules: { type: Number, required: true },
  minorIssues: { type: Number, required: true },
  majorIssues: { type: Number, required: true },
  criticalIssues: { type: Number, required: true },
});

function getPercents(issues, nbIssues, restPercentage) {
    return issues ? ((issues / nbIssues) * restPercentage).toFixed(2) : '0'
}

const nbIssues = computed(() => props.minorIssues + props.majorIssues + props.criticalIssues);
const affectedRules = computed(() => props.nbRules - props.optimizedRules);
const cercleStyle = computed(() => {
    if (!props.nbRules) {
        return '' // TODO return a correct circle in NO RULES state
    }
    if (!nbIssues.value) {
        return '' // TODO return a full green circle
    }
    const percentage = (props.optimizedRules / props.nbRules) * 100;
    const restPercentage = 100 - percentage;
    const criticalIssuesPercentage = getPercents(props.criticalIssues, nbIssues, restPercentage)
    const majorIssuesPercentage = getPercents(props.majorIssues, nbIssues, restPercentage)
    const minorIssuesPercentage = getPercents(props.minorIssues, nbIssues, restPercentage)

    let firstSlice = (percentage / 100) * 360;
    const secondSlice = firstSlice + (parseFloat(minorIssuesPercentage) / 100) * 360;
    const thirdSlice = parseFloat(secondSlice) + (parseFloat(majorIssuesPercentage) / 100) * 360;
    const lastSlice = parseFloat(thirdSlice) + (parseFloat(criticalIssuesPercentage) / 100) * 360;

    if (
        percentage === 0 &&
        minorIssuesPercentage === 0 && 
        majorIssuesPercentage === 0 &&
        criticalIssuesPercentage === 0
    ) {
        firstSlice = 360;
    }

    return `${
        'white conic-gradient(' +
        'from 0deg at 50% 50%, ' +
        '#85BB2F 0deg, ' +
        '#85BB2F ' + firstSlice + 'deg, ' +
        '#FECB02 ' + firstSlice + 'deg, ' +
        '#FECB02 ' + secondSlice + 'deg, ' +
        '#FF8E12 ' + secondSlice + 'deg, ' +
        '#FF8E12 ' + thirdSlice + 'deg, ' +
        '#E30021 ' + thirdSlice + 'deg, ' +
        '#E30021 ' + lastSlice + 'deg, ' +
        '#85BB2F 360deg' +
        ')'}`;
});

</script>
<template>
  <div
    class="circle-shadow-inset circle-rules-error"
    :style="{
      background: cercleStyle,
      '--bg-color': 'white',
    }"
  >
    <p class="circle-inside">
      <strong>{{ nbIssues }}</strong> issues
    </p>
  </div>
  <div>
    <span class="circle-shadow-inset__legend">
      Affected {{ props.metricTag }} rules:
      <strong>{{ affectedRules }} / {{ props.nbRules }}</strong>
    </span>
  </div>
</template>
<style scoped>

.circle-shadow-inset {
  min-width: 130px;
  height: 130px;
  border-radius: 50%;
  background: var(--bg-color)
    conic-gradient(
      from 0deg at 50% 50%,
      var(--text-color) 0deg,
      var(--text-color) var(--angle),
      rgba(0, 0, 0, 0.2),
      var(--angle),
      rgba(0, 0, 0, 0.2),
      359.97deg,
      var(--text-color) 360deg
    );
  box-shadow: inset 0px 4px 4px rgba(0, 0, 0, 0.25);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  margin-left: 0;
}
.circle-shadow-inset::before {
  content: '';
  position: absolute;
  height: 80%;
  width: 80%;
  left: 10%;
  top: 10%;
  border-radius: 50%;
  background: var(--bg-color);
  box-shadow: 0px 4px 4px rgba(0, 0, 0, 0.25);
}
.circle-rules-error {
  max-width: 120px;
  min-width: 120px;
  height: 120px;
  margin-bottom: 10px;
  background: white
    conic-gradient(
      from 0deg at 50% 50%,
      #85bb2f 0deg,
      #85bb2f 300deg,
      #fecb02 300deg,
      #fecb02 335deg,
      #ff8e12 335deg,
      #ff8e12 350deg,
      #e30021 350deg,
      #e30021 360deg,
      #85bb2f 360deg
    );
}
.circle-rules-error p {
  font-size: 12px;
  text-align: center;
}
.circle-rules-error strong {
  display: block;
  font-size: 30px;
  font-weight: 500;
}
.circle-shadow-inset__legend {
  font-weight: 500;
  font-size: 12px;
  line-height: 17px;
  align-items: center;
  text-align: center;
  letter-spacing: -0.015em;
  display: block;
  margin: 0 auto;
}
.circle-inside {
  z-index: 1;
  margin-top: 15px;
  color: var(--text-color);
}
</style>