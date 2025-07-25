import IconBug from './atoms/Icons/IconBug.vue'
import ImpactTag from './atoms/ImpactTag/ImpactTag.vue'
import RuleIconTag, { icons } from './molecules/RuleIconTag/RuleIconTag.vue'
import AbcdeScore from './molecules/AbcdeScore/AbcdeScore.vue'
import ScoreBlock from './organisms/ScoreBlock/ScoreBlock.vue'

const { IconCpu, IconDisk, IconMaintenance, IconNetwork, IconRam } = icons;

// Atoms
export { IconBug, IconCpu, IconDisk, IconMaintenance, IconNetwork, IconRam }
export { ImpactTag }
// Molecules
export { AbcdeScore, ScoreBlock, RuleIconTag }
