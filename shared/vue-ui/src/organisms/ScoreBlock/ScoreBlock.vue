<script setup>
import { computed } from 'vue';
import AbcdeScore from '../../molecules/AbcdeScore/AbcdeScore.vue';
import TooltipBox from '../../molecules/TooltipBox/TooltipBox.vue';
import RuleCriticities from '../../molecules/RuleCriticities/RuleCriticities.vue';

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

const projectLink = computed(() => {
    const id = props.projectKey
    const filter = `branch=${props.branch}&id=${id}&resolved=false`
    return `${globalThis.baseUrl}/project/issues?${filter}`;
})
</script>

<template>
  <h2>Your eco-design score is</h2>
  <abcde-score :value="value" />
  <div class="rate-description">
    <strong>{{ labelBold }}</strong>
    <br>
    <br>
    {{ label }}
    <br>
    <div class="tooltip-calc">
      how my score is calculated ?
      <tooltip-box>
        <p>
          <strong>{{ labelLong }} </strong><br>
          {{ text }}
        </p>
        <br>
        <p>
          <strong>Identified issues in your app:</strong>
        </p>
        <rule-criticities
          :minor-severities="minorSeverities"
          :major-severities="majorSeverities"
          :critical-severities="criticalSeverities"
          :project-link="projectLink"
        />
      </tooltip-box>
    </div>
  </div>
</template>

<style scoped>
h2 {
  margin-top: 30px;
}
.rate-description {
  padding-top: 20px;
}
.tooltip-calc {
  margin-top: 20px;
  margin-bottom: 1.25rem;
  text-decoration: underline;
  display: inline-block;
}
</style>
