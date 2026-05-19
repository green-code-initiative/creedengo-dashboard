<script setup>
import { ref } from 'vue'

const message = ref('Dashboard opérationnel !!!')
</script>

<template>
  <div>
    <h1>{{ message }}</h1>
  </div>
</template>