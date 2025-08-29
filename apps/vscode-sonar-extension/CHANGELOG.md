# Change Log

All notable changes to the "creedengo-for-sonar" extension will be documented in this file.

Check [Keep a Changelog](http://keepachangelog.com/) for recommendations on how to structure this file.

## [0.1.0](https://github.com/green-code-initiative/creedengo-dashboard/releases/tag/v0.1.0-vscode) - 2025-08-27

### Added

- Sonar connection configuration (Sonar Server URL & Authtication Token)
- A view service for easier management of the vscode extension views 
- Initial connection to Sonar
- Retrieval of the project id from the `sonar-project.properties` file
- Retrieval of git branches analized by Sonar
- Retrieval of Sonar issues information to calculate the score
- 2 vscode commands: `update score` and `clear score`
- Display of the score in the Sustainability panel
