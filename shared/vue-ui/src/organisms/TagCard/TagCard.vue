
<script setup>
import { computed } from 'vue';
import PieChart from '../../molecules/PieChart/PieChart.vue';
import RulesCriticities from '../..//molecules/RuleCriticities/RuleCriticities.vue';

const props = defineProps({
  metricTag: { type: String, required: true },
  nbRules: { type: Number, required: true },
  optimizedRules: { type: Number, required: true },
  minorIssues: { type: Number, required: true },
  majorIssues: { type: Number, required: true },
  criticalIssues: { type: Number, required: true },

  projectKey: { type: String, required: true },
  branch: { type: String, required: true },
// tagIssues: { type: TagIssues, required: true },
// branchLike: { type: String, required: true }
});

const noIssues = computed(
  () =>
    props.minorIssues === 0 &&
    props.majorIssues === 0 &&
    props.criticalIssues === 0,
);

const projectLink = computed(() => {
    const id = props.projectKey
    const tags = props.metricTag.toLowerCase()
    const filter = `branch=${props.branch}&id=${id}&resolved=false&tags=${tags}`
    return `${window.baseUrl}/project/issues?${filter}`;
})

</script>
<template>
  <div class="tag-card">
    <div class="head">
      <div>
        <div class="title">
          <strong>{{ props.metricTag }}</strong>
        </div>
        <div
          class="ellispse"
          data-for="issue_ellispse_tooltip"
          data-tip="Affected rules correspond to the number of rules associated with errors identified by SonarQube, among the Greensight rules that impact this component"
          data-iscapture="true"
        >
          i
          <q-tooltip
            class="tooltip tooltip-bg-dark"
            anchor="top middle"
            self="bottom middle"
          >
            <div class="tooltip-small">
              Affected rules correspond to the number of rules associated with
              errors identified by SonarQube, among the Greensight rules that
              impact this component
            </div>
          </q-tooltip>
        </div>
      </div>
      <a
        v-if="!noIssues"
        :href="`${ projectLink }`"
        target="blank"
      >
        See all issues
      </a>
    </div>
    <div class="body">
      <div class="body-left">
        <PieChart
          :metric-tag="props.metricTag"
          :nb-rules="props.nbRules"
          :optimized-rules="props.optimizedRules"
          :minor-issues="props.minorIssues"
          :major-issues="props.majorIssues"
          :critical-issues="props.criticalIssues"
        />
      </div>

      <div class="body-right">
        <RulesCriticities
          :metric-tag="props.metricTag"
          :optimized-rules="props.optimizedRules"
          :minor-issues="props.minorIssues"
          :major-issues="props.majorIssues"
          :critical-issues="props.criticalIssues"
          :project-link="projectLink"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>
.tag-card {
  padding: 30px 30px !important;
  flex: 1 0 40% !important;
  margin: 15px;
  height: fit-content;
  background: #ffffff;
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.05);
  padding-left: 30px;
  padding: 15px 10px 15px 30px;
  margin: 15px;
  border-radius: 10px;
}
.head {
  margin-bottom: 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.head a {
  border-bottom: none;
  color: #4aa9d5;
  font-weight: 500;
  font-size: 11px;
}
.title {
  float: left;
  margin-left: 5px;
  font-size: 18px;
  height: 10px;
}
.ellispse {
  border-radius: 100%;
  text-align: center;
  background-color: #4aa9d5;
  width: 13px;
  height: 13px;
  color: white;
  font-weight: 500;
  font-size: smaller;
  float: right;
  margin-left: 5px;
}
.body {
  display: flex;
  flex-wrap: wrap;
}
.body-left {
  flex: 1 0 40% !important;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.body-right {
  flex: 1 0 50% !important;
}
</style>
