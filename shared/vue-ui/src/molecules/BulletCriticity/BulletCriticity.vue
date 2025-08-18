
<script setup>
import { computed } from 'vue';
import IconArrowRight from '../../design-tokens/icons/IconArrowRight.vue';
import ImpactTag from '../../atoms/ImpactTag/ImpactTag.vue';

const props = defineProps({
  impact: { type: String, required: false, default: 'Optimized',
    validator(value) {
      return ['Low', 'Medium', 'High', 'Optimized'].includes(value)
    }
  },
  label: { type: String, required: false, default: () => 'impact issues' },
  count: { type: Number, required: false, default: 0 },
  link: { type: String, required: false, default: '' },
});

const hasIssues = computed(() => props.count);
const showLink = computed(() => hasIssues.value && props.link);

function onClickOpenIssues() {
  globalThis.window.open(props.link);
}
</script>

<template>
  <li
    :class="{ clickable: showLink }"
    @click="showLink ? onClickOpenIssues() : null"
    @keyUp="showLink ? onClickOpenIssues() : null"
  >
    <ImpactTag
      :impact="`${impact}`" 
      :custom-label="`${label}`"
    />
    <strong>{{ count }}</strong>
    <IconArrowRight v-if="showLink" />
  </li>
</template>

<style scoped>

li {
  display: flex;
  list-style-type: none;
  font-size: 12px;
  margin-bottom: 17px;
}

li strong {
  margin-top: auto;
  margin-left: auto;
  margin-right: 10px;
}

li svg{
  margin-top: auto;
  margin-bottom: 2px;
}
.clickable {
  cursor: pointer;
}
</style>
