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
      <strong>{{ value }}</strong>
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
}
.rate-list li {
  display: inline-block;
  font-size: 14px;
  width: 24px;
  height: 24px;
  margin: 0;
  color: rgba(255, 255, 255, 0.5);
  font-weight: bold;
  padding: 9px;
  vertical-align: middle;
  text-align: center;
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
  /* padding-bottom: 10px !important; */
  font-size: 28px !important;
  background: var(--bg-color) !important;
  color: white !important;
  width: 35px !important;
  height: 35px !important;
  border: 4px solid white;
  display: flex !important;
  justify-content: center;
  align-items: center;
}

.rate.a {
  --bg-color: #178c3d;
  --top: -10px;
  --left: 36px;
}
.rate.b {
  --bg-color: #85bb2f;
  --top: -10px;
  --left: 76px;
}
.rate.c {
  --bg-color: #fecb02;
  --top: -10px;
  --left: 116px;
}
.rate.d {
  --bg-color: #ff8e12;
  --top: -10px;
  --left: 160px;
}
.rate.e {
  --bg-color: #e30021;
  --top: -10px;
  --left: 198px;
}
</style>
