<script setup>
defineProps({
  impact: {
    type: String,
    required: true,
    validator(value) {
      return ['Optimized', 'Info', 'Low', 'Medium', 'High', 'Blocker'].includes(value)
    }
  },
  customLabel: { type: String, required: false, default: 'impact' }
})
</script>

<template>
  <div class="tag">
    <div :class="['impact', impact.toLowerCase()]" />
    <p><strong>{{ impact }}</strong> {{ customLabel }}</p>
  </div>
</template>

<style scoped>
.tag {
  display: flex;
  align-items: center;
  padding-top: 15px;
}
.tag p {
  padding: 0 15px 0 5px;
  margin: 0;
}
.impact {
  height: 17px;
  width: 17px;
  border-radius: 50%;
  background-color: var(--bg-color);
}
.impact.optimized {
  --bg-color: #85bb2f;
}
.impact.low {
  --bg-color: #fecb02;
}
.impact.medium {
  --bg-color: #ff8e12;
}
.impact.high {
  --bg-color: #e30021;
}
.impact.blocker {
  --bg-color: #e30021;
}
.impact.info {
  --bg-color: #4aa9d5;
}
</style>
