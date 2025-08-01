import RuleIconTag, { icons } from './RuleIconTag.vue'

// More on how to set up stories at: https://storybook.js.org/docs/writing-stories
export default {
  title: 'Molecules/RuleIconTag',
  component: RuleIconTag,
  tags: ['autodocs'],
  argTypes: {
    label: { control: { type: 'select' }, options: Object.keys(icons) }
  }
}

// More on writing stories with args: https://storybook.js.org/docs/writing-stories/args
export const CpuIconTag = {
  args: { label: 'cpu' }
}
export const DiskIconTag = {
  args: { label: 'disk' }
}
export const MaintenanceIconTag = {
  args: { label: 'maintenance' }
}
export const NetworkIconTag = {
  args: { label: 'network' }
}
export const RamIconTag = {
  args: { label: 'ram' }
}
