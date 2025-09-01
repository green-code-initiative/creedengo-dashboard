# Creedengo-Dashboard

[![Build and Tests](https://github.com/green-code-initiative/creedengo-dashboard/actions/workflows/build.yml/badge.svg)](https://github.com/green-code-initiative/creedengo-dashboard/actions/workflows/build.yml)
[![E2E Tests](https://github.com/green-code-initiative/creedengo-dashboard/actions/workflows/playwright.yml/badge.svg)](https://github.com/green-code-initiative/creedengo-dashboard/actions/workflows/playwright.yml)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=green-code-initiative_creedengo-dashboard&metric=bugs)](https://sonarcloud.io/summary/new_code?id=green-code-initiative_creedengo-dashboard)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=green-code-initiative_creedengo-dashboard&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=green-code-initiative_creedengo-dashboard)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=green-code-initiative_creedengo-dashboard&metric=coverage)](https://sonarcloud.io/summary/new_code?id=green-code-initiative_creedengo-dashboard)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=green-code-initiative_creedengo-dashboard&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=green-code-initiative_creedengo-dashboard)

This project is meant to provide Sustainable Code Dashboards to

- show potential impact on sustainability of unffollowed recommendations
- help decisions regarding code enhancement priorisation
- and even potentially prevent new issues to be pushed to production

## Installation

> As of today, we still need to finish few steps so you can use the dashboard in production

- **VSCode Extension:** an early stage first [0.1.0](https://github.com/green-code-initiative/creedengo-dashboard/releases/tag/v0.1.0-vscode) release can be downloaded from Github
- **Sonar plugin:** no realease are yet available - we hope to publish one in September, 2025

> We dearly welcome contributors


## Open Architecture

This project starts as a Vue.js application

- made available as a Sonarqube extension page
- related to the sonar inspected project using the sonarqube web APIs

Still, the code is structured to remain product agnostic

- allowing dashboards to be integrated in other contexts like
  - IDE addons for developers (VSCode, WebStorm, Visual Studio, Android Studio, X-Code, ...)
  - CI Pull/Merge Requests (Bitbucket, Github, Gitlab, Azure Devops, Gerrit, ...)
  - Project Wikis for decision makers (Confluence, Github/Gitlab, Teams/Sharepoint, Azure Devops, ...)
  - Or even dedicated Apps/Webapps (iOS, Android, ...)
- allowing to retrieves issues from other sources than the sonarqube API like
  - Other Static code  analyses solutions (Github, Codacy, Qodana, ...)
  - Linter reports (ESLint, csslint, klint, speccy/spectral, PHPStan, pylint, sqlfluff, ...)

![multi lint issue sourced Web Application integrable into many solutions](./apps/vue-app/documentation/open-architecture.png)

## Open and testable Widget Catalog

The Dashboard are constructed from widgets (web components) exposed to storybooks.

It allows:

- to individually develop, enhance and test those widgets (accessibility, behaviors, visual regression)
- to ultimately help having stable end-to-end tests with gherkin step-definitions provided by these components themselves
- to be able to provide on-demand dashboards or dedicated widgets in any end application contexts (ex: independant Confluence macros)

![built as a collection of wigdets usable for many dashboard](./apps/vue-app/documentation/dashboard-widgets.png)

## Project Build and Start

Read the [CONTRIBUTING.md](./CONTRIBUTING.md) file to look how to start the project at its current state
