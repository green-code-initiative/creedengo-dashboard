## Creedengo Widgets

Widgets are standalone components binding visual components to feature services. 
They can use configurations and some parameters to target code analyse results.

![widgets](../../../documentation/dashboard-widgets.png)

They are meant to be used to create dashboards usable throught

- standalone built web or mobile applications (using Apache Cordova) 
- or integrated as 
  - a [Sonarqube plugin](https://docs.sonarsource.com/sonarqube-server/2025.1/extension-guide/developing-a-plugin/plugin-basics/),
    - This project started as a migration of the Capgemini Greensight project to Ecocode (previous Creedengo name), and Creedengo is actually mostly provided as a collection of Sonarqube plugins, so this target is the most advanced one
  - IDE plugins like VSCode, Visual Studio, WebStorm, Android Studio, X-Code, Eclipse, IntelliJ IDEA,
    - Should help the developer to have more visual and textual information about the code quality issues in the code they are currently working on
  - linter html reporter plugins (as ones for ktlint, eslint, ...)
  - Confluence,
  - or for any other product.
