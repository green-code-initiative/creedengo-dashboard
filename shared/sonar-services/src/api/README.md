# Sonarsource Web and JavaScript APIs

This HTTP client provides an interface to the Sonarqube and Sonarcloud Web APIs

Sonarqube has 2 versions of its Web API (v1 and v2) while Sonarcloud has only the last version (v2)

Unfortunately Sonar:

- Does not officially document its Web API v1 anymore
- Does not publicly provide an Open API version of its Web API

## Creedengo Sonar Web API Support

The supported Sonar Web API are exposed in these Open API files:

- [Sonarqube/Sonarcloud Web API v2](./sonar.openapi.yml)

## JavaScript Helper APIs exposed by SonarQube

> There are several helper APIs exposed by SonarQube, like functions to make authenticated API requests.
>
> You can find the full list of exposed helpers [here](https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/app/components/extensions/exposeLibraries.ts).
>
> API calls (`window.SonarRequest`) ...

Official Source: [sonar-custom-plugin-example](https://github.com/SonarSource/sonar-custom-plugin-example?tab=readme-ov-file#how-to-use-these-files)

In integrated Pages, Sonar exposes its vendor JavaScript API which is almost not documented

It allows to be in an authenticated context so the plugin does not need authentication tokens to use the Web API

> **Warning**
> Those API are recommended for integration in the Sonar interface from a Sonar plugin BUT an alternative API call integration will have to be implemented for other targets
---------------------------------------------------------------------------------
> **Custom API retro-documentation based on Sonarqube source code**
>
> This API is exposed from this Sonar Source file: [sonar-web app/components/extensions
/exposeLibraries.ts](https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/app/components/extensions/exposeLibraries.ts)

### The `window.SonarRequest` API Call methods

The calls use the `fetch` native Web API

Most of the implementation is in this helper: [sonar-web helpers/request.ts](https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/helpers/request.ts)

#### `request(url: string)`

Create a Request object with this API:

```ts
class Request {
    setMethod(method: string) {}
    setData(data?: RequestData, isJSON = false) {}
    getSubmitData(customHeaders: any = {}) {}
    async submit() {}
}
```

The submit method calls the native `fetch()`API

#### `async get(url: string, data?: RequestData, bypassRedirect = false)`

Shortcut to do a GET request and return a Response

- Create a Request object
- Set the method to 'GET'
- Set the data
- Submit the request
- Check the status
Shortcut to do a GET request and return a Response

#### `async getJSON(url: string, data?: RequestData, options: RequestOptions = {})`

Shortcut to do a GET request and return response json

##### Example

[sonar-custom-plugin-example common/api.js](https://github.com/SonarSource/sonar-custom-plugin-example/blob/10.x/src/main/js/common/api.js)

```js
const responseMetrics = await getJSON(
    "/api/project_analyses/search", 
    {
        project: project.key,
        p: 1,
        ps: 500
    }
)
```

Request options are:

- `bypassRedirect`: `boolean`
- `customHeaders`: used only in SonarCloud
- `isExternal`: `boolean` used only in SonarCloud
- `useQueryParams`: `boolean` used only in SonarCloud

Source: [sonar-web sonar-aligned/helpers
/request.ts](https://github.com/SonarSource/sonarqube/blob/37e0ed33d0d419ec8f366490f64a427e24827886/server/sonar-web/src/main/js/sonar-aligned/helpers/request.ts#L38)

#### `async getText(url: string, data?: RequestData, bypassRedirect = false)`

Shortcut to do a GET request and return response text

The response body is returned as a string

#### `omitNil(obj: RequestData)`

Return a copy object without the `null` properties

##### Example

```js
const query = new URLSearchParams(omitNil(data)).toString();
```

In this example, it prevents from adding query parameters without values

#### `async parseError(response: Response)`

Parse error response of failed request and return the message as a string

#### `async post(url: string, data?: RequestData, bypassRedirect = false)`

Shortcut to do a POST request

#### `async postJSON(url: string, data?: RequestData, bypassRedirect = false)`

Shortcut to do a POST request and return response json

#### `async postJSONBody(url: string, data?: RequestData, bypassRedirect = false)`

Shortcut to do a POST request with a json body and return response json

#### `async throwGlobalError(param: Response | any, options?: throwGlobalError)`

Shows an Error message to the user and returns a rejected promise

##### Examples

```js
getJSON('/api/ce/analysis_status', params)
  .catch(throwGlobalError);
```

```js
postJSON('/api/alm_integrations/import_azure_project', data)
  .catch(throwGlobalError)
```

If the `options.redirectUnauthorizedNoReasons` is set to `true`, it will call `handleRequiredAuthentication()`

#### `addGlobalSuccessMessage(message: ReactNode, overrides?: ToastOptions)`

The `message` parameter can be either a string or a JSX ReactNode

##### Examples

[sonar-web queries
/settings.ts](https://github.com/SonarSource/sonarqube/blob/37e0ed33d0d419ec8f366490f64a427e24827886/server/sonar-web/src/main/js/queries/settings.ts#L92)

```js
addGlobalSuccessMessage(
  t('settings.authentication.form.settings.save_success')
);
```

[sonar-web create/project/components](https://github.com/SonarSource/sonarqube/blob/37e0ed33d0d419ec8f366490f64a427e24827886/server/sonar-web/src/main/js/apps/create/project/components/NewCodeDefinitionSelection.tsx#L30)

```jsx
addGlobalSuccessMessage(
  <FormattedMessage
    defaultMessage={t('onboarding.create_project.success.admin')}
    id="onboarding.create_project.success.admin"
    values={{
        project_link: <Link to={getProjectUrl(data.project.key)}>{data.project.name}</Link>,
    }}
  />,
);
```

The `overrides` parameter accepts [React-toastify](https://fkhadra.github.io/react-toastify/introduction/) options

Source:

- [sonar-web/design-system/src/components/toast-message
/toast-utils.tsx](https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/design-system/src/components/toast-message/toast-utils.tsx#L38)

### Localization

Localizable UI strings are defined in `src/main/resources/org/sonar/l10n/example/`. 

They are loaded at startup time and can used by the global `t()` and `tp()` functions.

See `src/main/js/admin_page/components/InstanceStatisticsApp.js` and `src/main/js/portfolio_page/components/VersionsMeasuresHistoryApp.js` for some examples.

#### `window.t(...keys: string[])`

Returns the **translated** message for the given key  of keys. In case of multiple parameters, keys are concatenated to create a single final translation key:

Example

```js
window.t('onboarding.create_project.success', 'admin')
// equivalent to
window.t('onboarding.create_project.success.admin')
```

#### `window.tp(messageKey: string, ...parameters: Array<string | number>)`

Retrive the translation for the given key and return the associated string with parameters injected in the ordered placeholders

[main/resources/org/sonar/l10n/example.properties](https://github.com/SonarSource/sonar-custom-plugin-example/blob/10.x/src/main/resources/org/sonar/l10n/example.properties#L14C1-L15C1)

```ini
example.admin_page.we_have_x_y=We have {0} {1}
example.admin_page.issues=Issues
```

[admin_page/components
/InstanceStatisticsApp.js](https://github.com/SonarSource/sonar-custom-plugin-example/blob/4efde8954e0820331729f481908a681b3ffbeb4c/src/main/js/admin_page/components/InstanceStatisticsApp.js#L78)

```js
window.tp(
    "example.admin_page.we_have_x_y",
    this.state.numberOfIssues,
    window.t("example.admin_page.issues")
)
```

This will return something like

> We have 42 Issues

## Resources

- https://docs.sonarsource.com/sonarqube/latest/extension-guide/web-api/
- https://next.sonarqube.com/sonarqube/web_api