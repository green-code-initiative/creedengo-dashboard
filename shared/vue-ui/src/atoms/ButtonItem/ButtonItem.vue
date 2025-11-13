<script setup>
defineEmits(['click'])

defineProps({
  type: {
    type: String,
    default: 'standard',
    validator(value) {
      return ['standard', 'primary'].includes(value)
    }
  }
})
</script>

<template>
  <button 
    :class="`${type === 'primary' ? 'btn-app-blue' : 'btn-app'}`"
    @click="onClick"
  >
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
