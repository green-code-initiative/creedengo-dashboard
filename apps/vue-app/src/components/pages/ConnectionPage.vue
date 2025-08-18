<script setup>
import { TokenConnectionForm } from '@creedengo/vue-ui'
import core from '@creedengo/core-services';

const [, authenticator] = core.api.services.authenticators;
const onSuccess = () => this.$router.push('/projects')
</script>

<template>
  <h2>Sonar Connection</h2>
  <TokenConnectionForm 
    :authenticator="authenticator"
    :callback="onSuccess"
  />
</template>
