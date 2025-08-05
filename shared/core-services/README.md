# Creedengo Dashboard core services

Core services are the interfaces used to prepare data for the UI components

## Initialization

These services need to be initialized with a vendor implementation which is meant to provide project and project issues to the core services.

```js
// import core services
import core from '@creedengo/core-services';
// import a vendor service implementation (ex: Sonar)
import VendorApi from '@creedengo/vendor-services';
// bind the vendor API to the core services
core.api.init(VendorApi)
```

## Public API

Core services exposes the following APIs

### Settings

The settings service allows to store and retrieve Creedengo dashboard settings

```js
// initiate a settings storage
// the default storage uses the HTML5 localStorage API
settings.initStorage(store = defaultStore)

await settings.set(name, value) // store a settings value 
const value = await settings.get(name) // retrieves a settings value

settings.resetSettings() // remove any initoated store
settings.storageReady // true if a store is initiated
```

### Projects

```js
const projects = getProjects()
const { key } = projects[0]
```

### Score

Calculates an ecoscore for a given project

```js
const score = calculateProjectScore({ project, branch })
```

## Vendor expected implementation

To provide the expected core services, the initialization must have been done with
a Vendor implementation exposing the following methods.

```js
// Projects information
const projects = getProjects()
const { key } = projects[0]

const config = { project, branch }
// Project global information
const numberOfLines = await api.services.getNumberOfLineOfCode(config);
// Project Issues Information
const severityFacets = await api.services.getIssuesFacet('severities', { project, branch });
```

## Models

Vendor implementations ar meant to use provided models
