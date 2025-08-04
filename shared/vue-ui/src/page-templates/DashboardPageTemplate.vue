<script setup>
import TagCard from '../organisms/TagCard/TagCard.vue';
import ScoreBlock from '../organisms/ScoreBlock/ScoreBlock.vue';


</script>

<template>
  <div class="main-container">
    <div class="score-card-container">
      <div class="score-block">
        <div class="score-block-inner">
          <ScoreBlock 
            :value="'C'"
            :label-bold="'Your app is not fully optimized.'"
            :label="'Keep going! You can continue by fixing the recommended rule on the right side. This is the one that currently has the highest impact on your app.'"
            :label-long="'You have between 10 and 19 minor severities or you have 1 or many major severity.'"
            :project-key="'FOO'"
            :branch="'main'"
            :minor-severities="15"
            :major-severities="3"
            :critical-severities="0"
          />
        </div>
      </div>
      <div class="divider-block" />
      <div class="priority-score">
        <div class="priority-block-inner">
          <h2>PriorityScore</h2>
          <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Modi, dolorem. Magnam iure magni rem commodi consequatur officiis asperiores vel eum accusantium vitae officia, provident cupiditate cumque? Fuga, vero. Ullam, sequi.</p>
        </div>
      </div>
    </div>
    <div class="tag-card-container">
      <div class="tag-card">
        <TagCard
          :metric-tag="'CPU'"
          :nb-rules="19"
          :optimized-rules="12"
          :minor-issues="40"
          :major-issues="19"
          :critical-issues="0"
          :project-key="'foo'"
          :branch="'main'"
        />
      </div>
      <div class="tag-card">
        <TagCard
          :metric-tag="'Network'"
          :nb-rules="19"
          :optimized-rules="12"
          :minor-issues="40"
          :major-issues="19"
          :critical-issues="0"
          :project-key="'foo'"
          :branch="'main'"
        />
      </div>
      <div class="tag-card">
        <TagCard
          :metric-tag="'RAM'"
          :nb-rules="19"
          :optimized-rules="12"
          :minor-issues="40"
          :major-issues="19"
          :critical-issues="0"
          :project-key="'foo'"
          :branch="'main'"
        />
      </div>
      <div class="tag-card">
        <TagCard
          :metric-tag="'Disk'"
          :nb-rules="19"
          :optimized-rules="12"
          :minor-issues="40"
          :major-issues="19"
          :critical-issues="0"
          :project-key="'foo'"
          :branch="'main'"
        />
      </div>
    </div>
  </div>
</template>

<style scoped>

.main-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-width: 1250px;
  margin: auto;
}

/* SCORE CARDS */
.score-card-container {
  display: flex;
  flex-wrap: wrap;
  background: #FFFFFF;
  box-shadow: 0px 6px 10px rgba(0, 0, 0, 0.05);
  border-radius: 10px;
}

.score-block {
  background-color: none;
  flex: 0 0 38%;
  min-width: 250px;
  height: 320px;
}

.score-block-inner {
  margin: 0 4rem 0 2.625rem;
}

.divider-block {
  width: 0.125rem;
  margin: 2.375rem 0;
  background: #BBBBBB;
}

.priority-score {
  background-color: none;
  flex: 1;
  min-width: 250px;
  height: 320px;
}

.priority-block-inner {
  margin: 0 2.625rem 0 4rem;
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
  .priority-score,
  .tag-card {
    min-width: unset;
    flex: none;
    height: auto;
    width: 100%;
  }
  .score-block-inner,
  .priority-block-inner {
    margin: 0 1rem;
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
  .priority-score,
  .tag-card {
    min-width: unset;
    flex: none;
    height: auto;
    width: 100%;
  }
  .score-block-inner,
  .priority-block-inner {
    margin: 0 2.625rem 0 4rem;
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
  .score-block-inner {
    margin: 0 4rem 0 2.625rem;
  }
  .priority-block-inner {
    margin: 0 2.625rem 0 4rem;
  }
}

</style>
