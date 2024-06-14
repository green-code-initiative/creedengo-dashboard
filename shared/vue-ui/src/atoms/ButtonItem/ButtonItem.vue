<script setup>
defineEmits(['click'])

defineProps({
  type: {
    type: String,
    dafault: 'standard',
    validator(value) {
      return ['standard', 'primary'].includes(value)
    }
  }
})
</script>

<template>
  <button @click="onClick" :class="`${type === 'primary' ? 'btn-app-blue' : 'btn-app'}`">
    <slot />
  </button>
</template>

<style scoped>
.btn-app {
  color: #444;
  background-color: white;
  border: 2px solid #444;
  padding: 8px 18px;
  border-radius: 100px;
  cursor: pointer;
}

.btn-app-blue {
  color: white;
  background-color: #4aa9d5;
  border: 2px solid #4aa9d5;
  padding: 8px 18px;
  border-radius: 100px;
  cursor: pointer;
}
</style>
