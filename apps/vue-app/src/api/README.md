# Vue.js HTTP API Client

## Vuex

[Vuex](https://vuex.vuejs.org/) was the **official** Vue state management solution and provides a solution similar to **Redux**.

The `api/` folder looks to be the convention pushed by Vuex for Vue.js applications to place the files fetching data from the server

Reference: https://vuex.vuejs.org/guide/structure.html

## Pinia

[Pinia](https://pinia.vuejs.org/) comes as the **official replacement** of Vuex

It doesn't explicitely tell where HTTP API clients should be placed but its code examples looks to propose the same


Reference: https://pinia.vuejs.org/cookbook/migration-vuex.html

## Test Vue.js App HTTP Request

The Vue.js comes with Vite and Vitest

Vitest recommend [Mock Service Worker](https://mswjs.io/) to mock HTTP requests

Reference:
- https://vitest.dev/guide/mocking.html#requests

https://test-utils.vuejs.org/guide/advanced/http-requests