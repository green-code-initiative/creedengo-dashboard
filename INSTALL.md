# INSTALLATION GUIDELINES

## Recommended Development Tools

This project is configured to work with the following tools

- [VSCode](https://code.visualstudio.com/) + The [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) Vue.js official extension (disable Vetur).
- [nvm](https://github.com/nvm-sh/nvm) and the vsc-nvm VSCode extension to ensure using the right node.js version
- [pnpm](https://pnpm.io/) as an alternative to npm which is more energy efficient by reducing consumed space and CPU
- [turborepo](https://turborepo.com/) as a monorepo manager (on top of pnpm worspaces) to handle the several creedengo-dashboard packages (`sonar-services`, `vue-ui`, `vue-app`, `storybook-app`, `vscode-extension`, ...)

## Installation process

Once you cloned the repository and went in its main foler, 3 solutions can be used to install the development environnment

### Makefile

If working on your system, you can use the `Makefile` to install the main tools (it also provides `test`, `build`, and `dev` commands)

```sh
make install
```

### script/install.sh

Run [./script/install.sh](./scripts/install.sh) or read ir to retrieve the commands used  

### Manually install each tool

### nvm

Follow the [nvm installation process](https://github.com/nvm-sh/nvm?tab=readme-ov-file#install--update-script)

> NOTE: you nay not use it and handle the Node.js version yourself but you would risk unexpected behaviors

### The expected node.js version

The expected version is defined in the `.nvmrc` file.

```sh
nvm use
```

Nvm will probably ask to install a specific node version using the `nvm install` command

### pnpm

The expected version is defined in the `.nvmrc` file

```sh
Corepack use pnpm
```

### Turborepo

Global install

```sh
pnpm add turbo --global  
```

### Project dependencies (and dev dependencies)

```sh
pnpm install
```
