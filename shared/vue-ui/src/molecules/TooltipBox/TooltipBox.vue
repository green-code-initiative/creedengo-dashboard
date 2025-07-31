<script setup>
/**
 * Accessible Tooltip
 * @see https://web.dev/articles/building/a-tooltip-component
 */
defineProps({
  anchor: { type: Boolean, required: false, default: false },
});

</script>
<template>
  <div 
    v-if="anchor" 
    class="anchor"
  >
    i
  </div>
  <div 
    ref="refTooltip"
    inert
    role="tooltip"
  >
    <slot />
  </div>
</template>

<style scoped>

.anchor {
  border-radius: 100%;
  text-align: center;
  background-color: #4aa9d5;
  width: 13px;
  height: 13px;
  color: white;
  font-weight: 500;
  font-size: smaller;
  float: right;
  margin-left: 5px;
}

div[role=tooltip] {

  --_p-inline: 1.5ch;
  --_p-block: .75ch;
  --_triangle-size: 7px;
  --_shadow-alpha: 50%;

  /* make the tooltips non-interactive so they don’t 
     steal pointer events from their parent element */
  pointer-events: none;
  user-select: none;

  /* hide the tooltip with opacity */
  opacity: 0;

  transition: opacity .2s ease, transform .2s ease;

  /* Take tooltip out of document flow with position absolute: */
  position: absolute;
  z-index: 1;

  inline-size: max-content;

  max-inline-size: 25ch;

  text-align: start;

  font-size: 1rem;

  font-weight: normal;
  line-height: normal;
  line-height: initial;

  padding: var(--_p-block) var(--_p-inline);
  margin: 0;
  border-radius: 5px;

  /* Use System Colors https://developer.mozilla.org/en-US/docs/Web/CSS/system-color */
  color: CanvasText;
  background-color: Canvas;

  border: 1px solid

}

/* create a stacking context for elements with tooltips */
:has(> div[role=tooltip]) {
  position: relative;
}

/* when those parent elements have focus, hover, etc */
:has(> div[role=tooltip]):is(:hover, :focus-visible, :active) > div[role=tooltip] {
  opacity: 1;

  transition-delay: 200ms;
}

/* prepend some prose for screen readers only */
div[role="tooltip"]::before {
  content: "; Has tooltip: ";
  clip: rect(1px, 1px, 1px, 1px);
  clip-path: inset(50%);
  height: 1px;
  width: 1px;
  margin: -1px;
  overflow: hidden;
  padding: 0;
  position: absolute;
}

/* top tooltip styles */
div[role="tooltip"]:is(
  [tip-position="top"],
  [tip-position="block-start"],
  :not([tip-position]),
  [tip-position="bottom"],
  [tip-position="block-end"]
) {
  text-align: center;
}

.hidden {
  display: none;
}
.tooltip-content {
  width: 200px;
}
</style>
