# CREEDENGO

_creedengo_ is a collective project aiming to reduce environmental footprint of software at the code level. The goal of the project is to provide a list of static code analyzers to highlight code structures that may have a negative ecological impact: energy and resources over-consumption, "fatware", shortening terminals' lifespan, etc.

## Configuration

Open the application **options** to setup the Creedengo configuration. It will be stored in the web extension storage.

### Configuration fields

#### Sonar Authentication Token `string` (mandatory)

This token is mandatory to authenticate to the sonar server with a role allowing to access to your projects data.

#### Project key `string`

The Project Key is necessary to get a sustainability score and priority rules to fix related to a project

#### Server URL `string`

The server URL is used to retrieve the project issues from the server HTTP API. It defaults to the Sonarcloud URL if no dedicated SonarQube URL is provided.

