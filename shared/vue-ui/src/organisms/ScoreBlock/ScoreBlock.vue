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
  error: { type: Object, required: false, default: null },
  label: { type: String, required: true },
  description: { type: String, required: true },
  tips: { type: String, required: true },
  baseUrl: { type: String, required: false, default: '' },
  projectKey: { type: String, required: false, default: '' },
  branch: { type: String, required: false, default: '' },
  minorSeverities: { type: Number, required: false, default: NaN },
  majorSeverities: { type: Number, required: false, default: NaN },
  criticalSeverities: { type: Number, required: false, default: NaN },
});

const projectLink = computed(() => {
    const id = props.projectKey
    const filter = `branch=${props.branch}&id=${id}&resolved=false`
    return `${props.baseUrl}/project/issues?${filter}`;
})
</script>

<template>
  <div>
    <h2>Your eco-design score is</h2>
    <abcde-score :value="value" />
    <div class="rate-description">
      <strong>{{ error ? error.type : label }}</strong>
      <br>
      <br>
      {{ error ? error.message : description }}
      <br>
      <div
        v-if="!Number.isNaN(minorSeverities)" 
        class="tooltip-calc"
      >
        how my score is calculated ?
        <tooltip-box>
          <p>
            <strong>Calculation method:</strong><br>
            {{ tips }}
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
