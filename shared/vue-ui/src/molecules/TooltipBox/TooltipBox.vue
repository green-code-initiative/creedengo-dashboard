<script setup>
/**
 * Accessible Tooltip
 * @see https://web.dev/articles/building/a-tooltip-component
 */
defineProps({
  anchor: { type: Boolean, required: false, default: false },
  position: { type: String, required: false, default: 'bottom',
    validator(value) {
      return ['top', 'top-left', 'top-right', 'left', 'right', 'bottom' ,'bottom-left', 'bottom-right'].includes(value)
    }
  },
});

</script>

<template>
  <div  v-if="anchor" class="anchor">i</div>
  <div class="main-anchor"></div>

  <div 
    ref="refTooltip"
    inert
    role="tooltip"
    :class="['pos', position.toLowerCase()]"
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
  line-height: 13px;
  color: white;
  font-weight: 500;
  font-size: small;
  float: right;
  margin-left: 5px;
  margin-top: 3px;
  cursor: help;
}

div[role=tooltip] {

  --_p-inline: 2.5ch;
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

  max-inline-size: 50ch;

  text-align: start;

  font-size: 1rem;

  font-weight: normal;
  line-height: initial;

  padding: var(--_p-block) var(--_p-inline);
  margin: 0px 5px 30px 5px;

  border: 1px solid;
  border-radius: 5px;

  /* Use System Colors https://developer.mozilla.org/en-US/docs/Web/CSS/system-color */
  color: CanvasText;
  background-color: Canvas;
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

/* Positionning tooltip around the main-anchor*/
.main-anchor {
  anchor-name: --main-anchor;
}

.pos {
  position-anchor: --main-anchor;
}

.top {
  position-area: y-start;
}

.top-left {
  position-area: y-start x-start;
}

.top-right {
  position-area: y-start x-end;
}

.bottom {
  position-area: y-end;
}

.bottom-left {
  position-area: y-end x-start;
}

.bottom-right {
  position-area: y-end x-end;
}

.left {
  position-area: x-start;
}

.right {
  position-area: x-end;
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
