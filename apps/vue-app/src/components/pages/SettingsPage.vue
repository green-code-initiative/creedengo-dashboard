<script setup>
import { SettingsForm } from '@creedengo/vue-ui'
import core from '@creedengo/core-services';

const { settings } = core;

if (!settings.storageReady) {
  settings.initStorage() // defaults to the Browser localStorage
}
</script>

<template>
  <header>
    <h1>Creedengo Settings</h1>
  </header>
  <div>&nbsp;</div>
  <main>
    <settings-form :storage="settings" />
  </main>
</template>
