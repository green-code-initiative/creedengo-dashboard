<script>
import IconCPU from '../../atoms/Icons/IconCpu.vue'
import IconDisk from '../../atoms/Icons/IconDisk.vue'
import IconMaintenance from '../../atoms/Icons/IconMaintenance.vue'
import IconNetwork from '../../atoms/Icons/IconNetwork.vue'
import IconRam from '../../atoms/Icons/IconRam.vue'

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
