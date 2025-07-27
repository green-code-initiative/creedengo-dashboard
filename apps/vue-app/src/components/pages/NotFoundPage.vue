<script setup>
import { reactive, onMounted } from 'vue'

let path = reactive('');

onMounted(async () => {
  path = globalThis.location.hash
});
</script>

<template>
  <header>
    <h1>Wrong URL</h1>
  </header>
  <main>
    Page "{{ path }}" not Found
  </main>
</template>
