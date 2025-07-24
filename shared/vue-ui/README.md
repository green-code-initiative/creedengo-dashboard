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

The Design System expects to follow Accessibility Guidelines the best possible way to comply to [W3C **ATAG** (Authoring Tool Accessibility Guidelines)](https://www.w3.org/WAI/standards-guidelines/atag/) and to help the final Dashboards to comply to 

- [W3C **WCAG** (Web Content Accessibility Guidelines)](https://www.w3.org/WAI/standards-guidelines/wcag/)
- [W3C WAI-**ARIA** (Accessible Rich Internet Applications)](https://www.w3.org/WAI/standards-guidelines/aria/)
- and their local variants such as
  - [France] [**RGAA** (Référentiel général d'amélioration de l'accessibilité)](https://accessibilite.numerique.gouv.fr)
  - [United States] the [**Section 508**](https://www.section508.gov/) (1998) of the [Rehabilitation Act](https://www.access-board.gov/about/law/ra.html) 

Accessibility is taken into consideration during the conception and also benefits from automatic tests provided by Storybook (see [Technologies](#-technologies))

> Note: beware Accessibility of digital services needs manual tests for full compliance validation. Any help is most wanted to participate to this effort (see [CONTRIBUTING](./CONTRIBUTING.md)). 

## 🚀 Performances

By the reuse of "standardized" components, Design Systems prevent from code duplications and participate to the reduction of Web pages weight.

Also, the Creedengo Design system uses different best practices pushing performances forward, like the Atomic Design principles ([see Atomic Design](#-atomic-design)), CSS spliting, Vectorial images, [native semantic HTML](https://developer.mozilla.org/en-US/docs/Web/HTML/Reference/Elements) elements priorization.

## 🎨 Adaptability

The components of this Design System are meant to be usable in many contexts including

- Web views for the Java built Sonarqube Servers
- Web views for the Java built Atlassian Confluence Servers
- Web Views of VSCode extensions
- Browser Extensions for Chrome, Edge, Safari and Firefox
- Mobile Applications
- ...

Some of these context (like Confluence or VSCode) expect the support of their themes ecosystems, which means that Creedengo components need to provide a correct rendering with most of this themes. It then works with a light base stylesheet and very limited embeded styles within the components.

> See
> - [Microsoft VSCode Native Themes](https://code.visualstudio.com/docs/configure/themes),
> - [Microsoft VSCode Community Themes](https://marketplace.visualstudio.com/search?term=theme&target=VSCode&category=All%20categories&sortBy=Relevance),
> - [Atlassian Confluence Native Themes](https://confluence.atlassian.com/doc/set-and-use-your-preferred-theme-1425051235.html)
> - [Atlassian Community Themes](https://marketplace.atlassian.com/search?query=Theme&product=confluence&categories=Content%20and%20communication&useCases=Themes%20%26%20Styles)

## ⚛ Atomic Design

The Creedengo design system follows an Atomic Design structure. [Atomic Design](https://bradfrost.com/blog/post/atomic-web-design/) is a methodology for creating design systems that emphasizes the use of small, reusable components to build larger, more complex interfaces.

![Atomic Design](https://atomicdesign.bradfrost.com/images/content/instagram-atomic.png)

## ∞ DesignOps

Inspired by DevOps, in 2016, AirBnB found out it could reuse and apply its concepts with Benefits for the Design iterations ([see here](https://medium.com/airbnb-design/airbnb-designops-2734cf4801b3)).

Note that DesignOps is often refered as a way to improve interactions in big projects with many people, but using this approach and related tools also helps a lot for smaller projects like Creedengo Dashboard which can't benefit from 100% available designers, devolopers, and testers with meetings on fixed working hours.

While often promoting team organization enhancements, the most important properties we're looking for in DesignOps are:

- Increased productivity
- Increased quality
- Ensured consistency
- Enhanced Innovation

Following the [Atomic Design principles](#-atomic-design), and using [Storybook](#tests-and-documentation) and [Figma](#design--specifications):

- Designers and developers can mutually share feedbacks and enhancements.
- Designers and developers work can be protected by automated non-regression tests
- Publishing the Design System documentation can allow our core team and end-users to see the ui advancements of expecting new features and share feedbacks even before a beta version of new dashboard versions are available.
- Contributors with little time or more humble experience can easilly push a new component, or a component eveolution, and almost immediately see its impact

> More about [DesignOps](https://www.capicua.com/blog/designops)

## 💻 Technologies

> Figma - Storybook - Vue.js - Vite.js

### Design & Specifications

The Designer contributors provided the initial specification through **[Figma](https://www.figma.com/fr-fr/)** which is one of the mainstream solutions.

It allows communication between the designer and the developers and, while it was not initially integrated, allows to build composable UI with Design tokens and components.

> The initial Figma specification will be rewriten to follow the [Atomic Design structure](#-atomic-design). We should benefit of toolings allowing us to reimport it from [our storybook implementation](#tests-and-documentation). 

> Please note our Design System is still in very early stage. Do not hesitate to contact us if you have Design experience we could benefit from (see [CONTRIBUTING](./CONTRIBUTING.md)).

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

<img width="2030" height="1348" alt="image" src="https://github.com/user-attachments/assets/d2206937-0863-46f2-ae95-1efe88ec27cb" />

