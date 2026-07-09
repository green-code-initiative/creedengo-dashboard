<script setup>
import ButtonItem from '../../Atoms/ButtonItem/ButtonItem.vue'

defineProps({
  buttons: {
    type: Array,
    required: true,
    validator(value) {
      return value.lenght && value.every((item) => Object.hasOwn(item, 'label'))
    }
  }
})
</script>

<template>
  <fieldset class="btn-group">
    <template v-for="button in buttons" :key="button.label">
      <button-item :type="`${button.type ?? 'standard'}`">{{ button.label }}</button-item>
    </template>
  </fieldset>
</template>

<style scoped>
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
</style>
