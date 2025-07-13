#!/bin/sh -x

command_exists () {
    command -v $1 >/dev/null 2>&1;
}

# INIT ZSHRC WHICH MAY BE MISSING ON MACOS
if [[ $0 == "zsh" ]]; then
  touch ~/.zshrc
fi

if ! command_exists nvm; then
    # INSTALL NVM
    NVM_VERSION="v0.39.7"
    NVM_URL="https://raw.githubusercontent.com/nvm-sh/nvm/${NVM_VERSION}/install.sh"
    if command_exists wget; then
    wget -qO- "${NVM_URL}" | bash
    elif command_exists curl; then
    curl -o- "${NVM_URL}" | bash
    fi
fi

# INSTALL PNPM
nvm use
corepack use pnpm@10

# INSTALL NODE DEPENDENCIES
pnpm install

# INSTALL PLAYWRIGHT
pnpm exec playwright install
