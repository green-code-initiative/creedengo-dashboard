<script setup>
import TagCard from '../organisms/TagCard/TagCard.vue';
import ScoreBlock from '../organisms/ScoreBlock/ScoreBlock.vue';

const props = defineProps({
  score: {
    type: Object,
    required: true,
    validator: (obj) =>
      obj &&
      typeof obj.value === 'string' &&
      typeof obj.label === 'string' &&
      typeof obj.description === 'string' &&
      typeof obj.tips === 'string' &&
      typeof obj.projectKey === 'string' &&
      typeof obj.branch === 'string' &&
      typeof obj.minorSeverities === 'number' &&
      typeof obj.majorSeverities === 'number' &&
      typeof obj.criticalSeverities === 'number'
  },
  metricTags: {
    type: Array,
    required: true,
    validator: (arr) => arr.every(metricTag => 
      metricTag && 
      typeof metricTag.name === 'string' &&
      typeof metricTag.nbRules === 'number' &&
      typeof metricTag.optimizedRules === 'number' &&
      typeof metricTag.minorIssues === 'number' &&
      typeof metricTag.majorIssues === 'number' &&
      typeof metricTag.criticalIssues === 'number' &&
      typeof metricTag.projectKey === 'string' &&
      typeof metricTag.branch === 'string'
    )
  }
});

</script>

<template>
  <div class="main-container">
    <div class="score-card-container">
      <div class="score-block">
        <ScoreBlock 
          :value="score.value"
          :label-bold="score.label"
          :label="score.description"
          :label-long="score.tips"
          :project-key="score.projectKey"
          :branch="score.branch"
          :minor-severities="score.minorSeverities"
          :major-severities="score.majorSeverities"
          :critical-severities="score.criticalSeverities"
        />
      </div>
      <div class="divider-block"></div>
      <div class="priority-block">
        <h2>Priority Rule</h2>
        <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Modi, dolorem. Magnam iure magni rem commodi consequatur officiis asperiores vel eum accusantium vitae officia, provident cupiditate cumque? Fuga, vero. Ullam, sequi.</p>
      </div>
    </div>
    <div class="tag-card-container">
      <div 
        v-for="(metricTag, index) in props.metricTags"
        :key="index" 
        v-bind="metricTag"
        class="tag-card"
      >
        <TagCard
          :metric-tag="metricTag.name"
          :nb-rules="metricTag.nbRules"
          :optimized-rules="metricTag.optimizedRules"
          :minor-issues="metricTag.minorIssues"
          :major-issues="metricTag.majorIssues"
          :critical-issues="metricTag.criticalIssues"
          :project-key="metricTag.projectKey"
          :branch="metricTag.branch"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>

.main-container {
  display: flex;
  justify-content: center;
  flex-direction: column;
  gap: 1rem;
  max-width: 1250px;
  margin: auto;
}

.divider-block {
  width: 0.125rem;
  margin: 2.375rem 0;
  background: #BBBBBB;
}

/* SCORE AND PRIORITY RULE CARDS */
.score-card-container {
  display: flex;
  flex-wrap: wrap;
  background: #FFFFFF;
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.05);
  border-radius: 10px;
}

.score-block {
  margin: 0 2rem 0 2.625rem;
  background-color: none;
  flex: 0 0 38%;
  height: 320px;
}

.priority-block {
  margin: 0 2.625rem 0 3rem;
  background-color: none;
  flex: 1;
  height: 320px;
}

/* TAG CARDS */
.tag-card-container {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

.tag-card {
  background-color: none;
  flex: 1 1 48%;
  min-width: 250px;
  height: 271px;
}

@media (max-width: 500px)  {
  .main-container {
    max-width: 100%;
    padding: 0.5rem;
  }
  .score-card-container {
    flex-direction: column;
    gap: 0.5rem;
  }
  .tag-card-container {
    flex-direction: column;
    gap: 0.5rem;
  }
  .score-block,
  .priority-block,
  .tag-card {
    min-width: unset;
    flex: none;
    height: auto;
  }
  .score-block,
  .priority-block {
    margin: 0 1.125rem;
  }
  .divider-block {
    width: calc(100% - 5rem);
    height: 0.125rem;
    margin: 0.5rem 2.5rem;
  }
}

@media (min-width: 500px) and (max-width: 1024px)  {
  .main-container {
    max-width: 100%;
    padding: 0.5rem;
  }
  .score-card-container {
    flex-direction: column;
    gap: 0.5rem;
  }
  .tag-card-container {
    flex-direction: column;
    gap: 0.5rem;
  }
  .score-block,
  .priority-block,
  .tag-card {
    min-width: unset;
    flex: none;
    height: auto;
  }
  .score-block,
  .priority-block {
    margin: 0 3rem;
  }
  .divider-block {
    width: calc(100% - 10rem);
    height: 0.125rem;
    margin: 1rem 5rem;
  }
}

@media (min-width: 1025px) {
  .main-container {
    margin: 0 0.5rem;
  }
  .score-card-container {
    flex-direction: row;
  }
  .tag-card-container {
    justify-content: flex-start;
  }
}

</style>
