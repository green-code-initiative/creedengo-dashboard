<script>
import IconCPU from '../../Atoms/Icons/IconCpu.vue'
import IconDisk from '../../Atoms/Icons/IconDisk.vue'
import IconMaintenance from '../../Atoms/Icons/IconMaintenance.vue'
import IconNetwork from '../../Atoms/Icons/IconNetwork.vue'
import IconRam from '../../Atoms/Icons/IconRam.vue'

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
