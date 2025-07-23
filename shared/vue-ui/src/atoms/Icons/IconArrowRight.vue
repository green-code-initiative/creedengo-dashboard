<script setup>
defineProps({
  height: {
    type: Number,
    default: 12,
  },
  width: {
    type: Number,
    default: 7,
  }
})
</script>
<template>
  <svg 
    :height="`${height}px`"
    :width="`${width}px`"
    :viewBox="`0 0 ${width} ${height}`"
    fill="none"
    xmlns="http://www.w3.org/2000/svg"
  >
    <path
      fill-rule="evenodd"
      clip-rule="evenodd"
      d="M1.23662 11L1 10.6037L5.38583 6L1 1.3963L1.23662 1L6 6L1.23662 11Z"
      fill="#3F3D56"
      stroke="#BBBBBB"
    />
  </svg>
</template>
