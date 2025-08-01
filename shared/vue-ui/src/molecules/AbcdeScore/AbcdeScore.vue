<script setup>
defineProps({
  value: {
    type: String,
    required: true,
    validator(value) {
      return ['A', 'B', 'C', 'D', 'E'].includes(value)
    }
  }
})
</script>

<template>
  <ul class="rate-list">
    <li :class="['rate', value.toLowerCase()]">
      <strong class="rate-note">{{ value }}</strong>
    </li>
    <li>A</li>
    <li>B</li>
    <li>C</li>
    <li>D</li>
    <li>E</li>
  </ul>
</template>

<style scoped>
.rate-list {
  vertical-align: middle;
  position: relative;
  margin: 1em 0;
  padding-left: 0;
}
.rate-list li {
  box-sizing: content-box;
  display: inline-flex;
  font-size: 16px;
  width: 24px;
  height: 20px;
  margin: 0;
  color: rgba(255, 255, 255, 0.600);
  font-weight: bold;
  padding: 9px;
  align-items: center;
  justify-content: center;
  background: rgba(39, 39, 39, 0.267);
}

.rate-list li:nth-child(2) {
  background: #178c3d;
  border-radius: 30px 0px 0px 30px;
}
.rate-list li:nth-child(3) {
  background: #85bb2f;
}
.rate-list li:nth-child(4) {
  background: #fecb02;
}
.rate-list li:nth-child(5) {
  background: #ff8e12;
}
.rate-list li:nth-child(6) {
  background: #e30021;
  border-radius: 0px 30px 30px 0px;
}

.rate {
  position: absolute;
  top: var(--top);
  left: var(--left);
  border-radius: 50px;
  padding: 7px !important;
  font-size: 28px !important;
  background: var(--bg-color) !important;
  color: white !important;
  width: 36px !important;
  height: 36px !important;
  border: 4px solid white;
  display: flex !important;
  justify-content: center;
  align-items: center;
}

.rate-note {
  font-weight: bold;
}

.rate.a {
  --bg-color: #178c3d;
  --top: -10px;
  --left: -4px;
}
.rate.b {
  --bg-color: #85bb2f;
  --top: -10px;
  --left: 36px;
}
.rate.c {
  --bg-color: #fecb02;
  --top: -10px;
  --left: 76px;
}
.rate.d {
  --bg-color: #ff8e12;
  --top: -10px;
  --left: 118px;
}
.rate.e {
  --bg-color: #e30021;
  --top: -10px;
  --left: 158px;
}
</style>
