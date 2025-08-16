<script>
import IconCPU from '../../design-tokens/icons/IconCpu.vue'
import IconDisk from '../../design-tokens/icons/IconDisk.vue'
import IconMaintenance from '../../design-tokens/icons/IconMaintenance.vue'
import IconNetwork from '../../design-tokens/icons/IconNetwork.vue'
import IconRam from '../../design-tokens/icons/IconRam.vue'

export const icons = {
  cpu: IconCPU,
  disk: IconDisk,
  maintenance: IconMaintenance,
  network: IconNetwork,
  ram: IconRam
}
</script>
<script setup>
defineProps({
  label: {
    type: String,
    required: true,
    validator(value) {
      return icons[value.toLowerCase()]
    }
  }
})
</script>

<template>
  <div class="tag">
    <component
      :is="icons[label.toLowerCase()]"
      :size="40"
    />
    <p>{{ label }}</p>
  </div>
</template>

<style scoped>
.tag {
  display: flex;
  align-items: center;
  padding-top: 15px;
}
.tag p {
  padding: 0 15px 0 5px;
  margin: 0;
}
</style>
