<script setup>
import ImpactTag from '@components/atoms/ImpactTag/ImpactTag.vue';
import RuleIconTag from '@components/molecules/RuleIconTag/RuleIconTag.vue';
defineProps({
  percentage: { type: Number, required: true },
  ruleKey: { type: String, required: true },
  ruleName: { type: String, required: true },
  ruleHtmlDesc: { type: String, required: true },
  ruleImpact: {
    type: String,
    required: true,
    validator(value) {
      return ['Optimized', 'Info', 'Low', 'Medium', 'High', 'Blocker'].includes(value)
    }
  },
  ruleMetricTags: { type: Array, required: false, default: () => [] }
})
</script>
<template>
  <div class="title">
    {{ percentage === 100 ? 'Go Further' : 'Priority rule to correct' }}
  </div>
  <div class="priority-rule">
    {{ ruleName }}
  </div>
  <div 
    class="card-description" 
    v-html="ruleHtmlDesc"
  />
  <div
    v-if="percentage < 100"
    class="tags"
  >
    <ImpactTag :impact="ruleImpact" />
    <div class="tag" :key="label" v-for="label in ruleMetricTags">
      <RuleIconTag :label="label" />
    </div>
  </div>
  <div class="btn-group">
    <button
      class="btn-app"
      type="button"
      @click="
        windowRef.open(
          percentage < 100
            ? `${getBaseUrl()}/project/issues?branch=${branchLike}&resolved=false&rules=${ruleKey}&id=${projectKey}`
            : 'https://institutnr.org',
        )
      "
    >
      See {{ percentage < 100 ? 'issues' : 'more' }}
    </button>
    <button
      v-if="percentage < 100"
      class="btn-app-blue"
      type="button"
      @click="
        windowRef.open(
          `${getBaseUrl()}/coding_rules?open=${ruleKey}&q=${ruleKey}`,
        )
      "
    >
      More infos
    </button>
  </div>
</template>
<style scoped>

.title {
  font-weight: 700;
  margin-bottom: 30px;
  font-size: 20px;
}

.priority-rule {
  line-height: 24px;
  color: #444;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 10px;
}

.tags {
  display: flex;
  font-size: 10px;
  font-weight: bold;
}
.btn-group {
  margin-top: 24px;
  font-weight: 500;
}
.btn-group .btn-app-blue {
  margin-left: 24px;
}
.btn-group .btn-app-blue:first-child {
  margin-left: 0;
}
.btn-app-blue {
  background-color: #4aa9d5;
  border: 2px solid #4aa9d5;
  color: #444;
  padding: 8px 18px;
  border-radius: 100px;
  cursor: pointer;
}
</style>