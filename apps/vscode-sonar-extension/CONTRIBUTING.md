# Contributing to "Creedengo For Sonar" VSCode Extension

## Writing VSCode Extensions

VSCode requires the extension to declare its entry points in its `package.json` file in the `"contributes"` property.

Look then at the `src/extension.mjs` file to see how, on activation, the code register handlers for each expected entry point (commands, views, preferences)

[VSCode Extension API Documentation](https://code.visualstudio.com/api)

### CommonS vs ECMAScript Modules

While VSCode migrated itself to ECMAScript modules with version 1.94 in September 2024 (with great performance improvments... [see official anouncement](https://jan.miksovsky.com/posts/2025/03-17-vs-code-extension)), It is still not possible to directly write VSCode extensions in ESM.

Luckily, [as proposed by Jan Miksovsky](https://jan.miksovsky.com/posts/2025/03-17-vs-code-extension), it is possible to use a CommonJS wrapper as entry file and code the real extension code in ESM.

The main remaining constraints to keep in mind are:

- IMPORTANT
  - **All JavaScript file must use the `.mjs` extension instead of `.js`**. The files with `.js` extension will be interpreted as CoomonJS module for compatibility with legacy VSCode modules.
  - **Import `vscode` from `compat/vescode.cjs`**
- NOTE ALSO
  - We cannot directly do `import { method } from 'a-commonjs-module'` but we can do `import aCjsModule from 'a-commonjs-module'; const { method } = aCjsModule` instead
  - `vite` can not be used as builder as it dropped CommonJS support, **we then use `esbuild`** which is officially supported by VSCode and was already used by vite for developer builds.

## Architecture Main points

- All HTTP API calls are sent from the main VSCode Service worker
- A view service handles:
  - the generation of the view HTML from a separated HTML file
  - the loading of Script, Stylesheets, or images from the view
  - the message communication between the webview and the webview owner
- A configuration service handles:
  - the settings retrieval (sonar server url & authentication token)
  - the project key retrieval from the `sonar-project.properties` file

## Run & Debug the extension

(without packaging and installation)

> Warning: As the extension code is in a monorepo, the following process requires you to first **open the `vscode-sonar-extension` folder from a new VSCode window**

If you open this `vscode-sonar-extension` folder from a dedicated VSCode window, you can then:

- Press `F5` to open a new window with your extension loaded.
- Open the `Sonarqube Setup` tab and see the `Sustainability Score` panel provided by this extension
- Set breakpoints in the code inside `src/extension.mjs` to debug the extension.
- Find output from the extension in the debug console.

### Webview Debugging

From the development Host VSCode instance you can press  `ctrl` + `shift` + `p` (or `cmd` + `shift` + `p` on MacOS), and type the `Open Webview Developer Tools` command. You'll then see the Chrome Web Inspector and you will be able to :

- control the webview page Network requests
- see its generated DOM
- set breakpoints

## Unit tests

### Running the tests

#### From command line

You can use `pnpm test` to run the unit tests.

#### From VSCode

- Install the [Extension Test Runner](https://marketplace.visualstudio.com/items?itemName=ms-vscode.extension-test-runner)
- Open the Testing view from the activity bar and click the Run Test" button, or use the hotkey `Ctrl/Cmd + ; A`
- See the output of the test result in the Test Results view.

### Writing the tests

VSCode provides an official `vscode-test` CLI tool to launch unit tests. This one relies on the [mocha](https://mochajs.org/next/) JavaScript Test Framework.

We still apply the same convention used with `vitest` expect we need to use the `.mjs` extension. This means that:

- By design, we prefer not using `TypeScript` but plain `ECMAScript`
- For each `foo.mjs` file, we create a `foo.spec.mjs` file in the same folder

[Official VScode Documentation](https://code.visualstudio.com/api/working-with-extensions/testing-extension)

## UX Guidelines

VSCode supports native and community provided themes. Dark Themes are quite popular among the developpers.

It means that it is important to [follow the VSCode UX guidelines](https://code.visualstudio.com/api/ux-guidelines/overview) to create extensions that seamlessly integrate with VS Code's native interface and patterns.

> Beware this impacts the way we must Design the Design System Components
