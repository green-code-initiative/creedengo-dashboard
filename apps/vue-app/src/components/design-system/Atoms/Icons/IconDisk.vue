<script setup>
defineProps({
  size: {
    type: Number,
    default: 48
  }
})
</script>
<template>
  <svg
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 24 24"
    fill="#444"
    :width="`${size}px`"
    :height="`${size}px`"
  >
    <path
      d="M0 0h24v24H0z"
      fill="none"
    />
    <path
      d="M2 20h20v-4H2v4zm2-3h2v2H4v-2zM2 4v4h20V4H2zm4 3H4V5h2v2zm-4 7h20v-4H2v4zm2-3h2v2H4v-2z"
    />
  </svg>
</template>
