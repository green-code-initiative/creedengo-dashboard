<script setup>
import { computed, ref } from 'vue';
import IconArrowRight from '../../atoms/Icons/IconArrowRight.vue';

const refScoreTooltip = ref(null);

const props = defineProps({
  label: { type: String, required: true },
  projectLink: { type: String, required: true },
  minorSeverities: { type: Number, required: true },
  majorSeverities: { type: Number, required: true },
  criticalSeverities: { type: Number, required: true },
});

const identifiedIssues = computed(() => [
  {
    severity: 'minor',
    label: 'Low',
    data: props.minorSeverities,
  },
  {
    severity: 'major',
    label: 'Medium',
    data: props.majorSeverities,
  },
  {
    severity: 'critical',
    label: 'High',
    data: props.criticalSeverities,
  },
]);

function onClickOpenIssues(severity) {
  const context = window.baseUrl;
  window.open(
    `${context}/issues?id=${
      props.projectKey
    }&resolved=false&severities=${severity.toUpperCase()}&tags=greensight`,
  );
}

function show() { 
  console.log('Fonction show called from child component');
  refScoreTooltip.value.show(); 
}

function hide() {
  console.log('Fonction hide called from child component');
  refScoreTooltip.value.hide(); 
}

defineExpose({ show, hide });
</script>
<template>
  <q-menu
    ref="refScoreTooltip"
    class="tooltip tooltip-bg-dark"
    anchor="top middle"
    self="bottom middle"
  >
    <div
      class="tooltip-content"
      @mouseleave="hide"
      @focusout="hide"
    >
      <p>
        <strong>Calculation method: </strong><br>
        {{ label }}
      </p>
      <br>
      <p>
        <strong>Identified issues in your app:</strong>
      </p>
      <div
        v-for="issues in identifiedIssues"
        :key="issues.label"
        :class="[`links-${issues.severity}`, 'links']"
      >
        <div
          @click="onClickOpenIssues(issues.severity)"
          @keyup="onClickOpenIssues(issues.severity)"
        >
          {{ issues.data }} Low impact
          <img
            :src="IconArrowRight" 
            alt=""
            class="img-links"
          >
        </div>
      </div>
    </div>
  </q-menu>
</template>

<style scoped>
.tooltip-content {
  width: 200px;
}
.links-minor {
  color: #fecb02;
}
.links-major {
  color: #ff8e12;
}
.links-critical {
  color: #e30021;
}
.links {
  cursor: pointer;
  font-weight: 400;
}
.img-links {
  margin-top: 3px;
  margin-left: 5px;
  height: 10px;
}
</style>
