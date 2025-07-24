# Creedengo Design System

The **Creedengo Design System** provides a library of components to be used for Dashboards and Settings page creation.

Design System component have no interaction with any server. Their displayed data are provided by the application using this library.

## 🌍 Sustainability 

The implementation of a Design System participates to provide more sustainable digital services. The [W3C Web Sustainability Guidelines](https://w3c.github.io/sustainableweb-wsg/) mentions it in on of its rules 

> [2.13 Use a design system to prioritize interface consistency](https://w3c.github.io/sustainableweb-wsg/#use-a-design-system-to-prioritize-interface-consistency)
>
> A design system is employed based on web standards and recognizable patterns to mutualize interface components and provide a consistent experience for visitors.

Its additional informations explain how Design Systems benefits to the Environment, Social Equity, Accessibility, Performance, Economic, and Conversions

## ♿ Accessibility

The Design System expects to follow Accessibility Guidelines the best possible way to comply to W3C ATAG and to help the final Dashboards to comply to 

- [W3C **WCAG** (Web Content Accessibility Guidelines)](https://www.w3.org/WAI/standards-guidelines/wcag/)
- [W3C WAI-**ARIA** (Accessible Rich Internet Applications)](https://www.w3.org/WAI/standards-guidelines/aria/)
- and the french [**RGAA** (Référentiel général d'amélioration de l'accessibilité)](https://accessibilite.numerique.gouv.fr)

Accessibility is taken into consideration during the conception and also benefits from automatic tests provided by Storybook (see [Technologies](#technologies))

## 🚀 Performances

By the reuse of "standardized" components, Design Systems prevent from code duplications and participate to the reduction of Web pages weight.

Also, the Creedengo Design system uses different best practices pushing performances forward, like the Atomic Design principles ([see Atomic Design](#atomic-design)), CSS spliting, Vectorial images, [native semantic HTML](https://developer.mozilla.org/en-US/docs/Web/HTML/Reference/Elements) elements priorization.

## 🎨 Adaptability

The components of this Design System are meant to be usable in many contexts including

- Web views for the Java built Sonarqube Servers
- Web views for the Java built Atlassian Confluence Servers
- Web Views of VSCode extensions
- Browser Extensions for Chrome, Edge, Safari and Firefox
- Mobile Applications
- ...

Some of these context (like Confluence or VSCode) expect the support of their themes ecosystems, which means that Creedengo components need to provide a correct rendering with most of this themes. It then works with a light base stylesheet and very limited embeded styles within the components.

## ⚛ Atomic Design

The Creedengo design system follows an Atomic Design structure. [Atomic Design](https://bradfrost.com/blog/post/atomic-web-design/) is a methodology for creating design systems that emphasizes the use of small, reusable components to build larger, more complex interfaces.

![Atomic Design](https://atomicdesign.bradfrost.com/images/content/instagram-atomic.png)

## ∞ DesignOps

Following the [Atomic Design principles](#-atomic-design), and using [Storybook](#tests-and-documentation) and [Figma](#design--specifications), designers and developers can mutually shared feedbacks and enhancements.

The selected test tools allow us to prevent the Design System from regressions.

## 💻 Technologies

> Figma - Storybook - Vue.js - Vite.js

### Design & Specifications

The Designer contributors provided the initial specification through **[Figma](https://www.figma.com/fr-fr/)** which is one of the mainstream solutions.

It allows communication between the designer and the developers and, while it was not initially integrated, allows to build composable UI with Design tokens and components.

> The initial Figma specification will be rewriten to follow the [Atomic Design structure](#-atomic-design). We should benefit of toolings allowing us to reimport it from [our storybook implementation](#tests-and-documentation). 

### Development

This version of the Creedengo Design System currently uses the **[Vue.js](https://vuejs.org)** frontend framework to develop its UI components and **[Vite.js](https://vite.dev)** to build the compressed version that will be used by the dashboards.

#### Why Vue.js

Vue.js is meant to be lighter than the Angular and React developement stacks. It then made sense to quickly provide a solution when the Capgemini Greensight project started to provide a management dashboard that would help development teaams to reduce the carbon footprint of their digital services.

> Nowadays, we are potentially considering to migrate to even lighter solutions like [Svelte Kit](https://svelte.dev) or even [native Web Components](https://developer.mozilla.org/en-US/docs/Web/API/Web_components).

#### Why Vite.js

Vite was first used because it is the solution pushed by the Vue.js ecosystem.

It was then also maintained because it internally uses:

- **[esbuild](https://esbuild.github.io)**, for live hot rebuilds in development phase. It is developed in GO and provides very high performances with very low footprint
- **[rollup.js](https://rollupjs.org)** for production builds, which provide more options to split builds in lazy loadable sub-modules.

> [Rolldown](https://rolldown.rs): Vite proposes to beta test the usage of Rolldown instead of Rollup, which is a must faster port of Rollup rewritten in Rust. We consider jumping to it after few tests
>
> **[esbuild](https://esbuild.github.io)** everywhere... More tests need to be done but esbuild will probably stay faster than Rolldown. We may try a full esbuild configuration to also target the production builds. It would need to prove that the final builds won't negatively impact the production builds.

### Tests and Documentation

**[Storybook](https://storybook.js.org)** is the choseen solution to automize UI component tests including:

- Accessibility tests
- Interaction tests
- Visual tests

> Note: As the Design System components have no interaction with any service, end-to-end integration tests are the responsability of the Web application using it.

Storybook also allow us to build a static website documenting the Creedengo Design System implementation to anyone.
