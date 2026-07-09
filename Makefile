## ----------------------------------------------------------------------
## This Makefile helps you to go ahead on this project
## See below the main commands
## 
## Warning
## Makefile "target" naming convention have differencies with pnpm commands
## 
## ------------------------------------------------------------------------

# To better understand Makefiles:
# https://blog.devops.dev/devops-make-makefile-build-tool-101-part-1-7e6743df6c45

# Makefile Conventions
# https://www.gnu.org/software/make/manual/html_node/Makefile-Conventions.html

SHELL = /bin/sh # Recommended by convention

APT = apt-get
BREW = brew
COREPACK = corepack
CURL = curl
MAKE = make
NVM = nvm
PNPM = pnpm
YUM = yum

SONARQUBE_HOME = 

htmldir = ./apps/website/dist

.SUFFIXES:

# User handy targets

.PHONY: help

foo:
	sed -ne '/@sed/(## \s+)+//p' $(MAKEFILE_LIST)

help:
	@sed -ne '/@sed/!s/## //p' $(MAKEFILE_LIST)

############################################################################
# Standard Makefile Targets                                                #
# https://www.gnu.org/software/make/manual/html_node/Standard-Targets.html #
############################################################################

.PHONY: all
.PHONY: install
.PHONY: install-html
.PHONY: uninstall
.PHONY: clean
.PHONY: distclean
.PHONY: maintainer-clean
.PHONY: check
.PHONY: dist

## Compile the entire program. 
all:
	$(MAKE) install

## Compile the program and copy the plugins for actual use.
install:
	$(MAKE) init
	$(PNPM) build

## Install documentation as a Website
install-html:
	$(MAKE) init
	$(PNPM) build

## Delete all the installed files
uninstall:
	@rm -rf **/dist **/node **/target

## Delete all files that are created by building the program.
clean:
	@rm -rf **/node_modules

distclean:
	$(MAKE) clean

maintainer-clean:
	$(MAKE) distclean
	@rm -rf **/coverage **/playwright-report

## Perform self-tests (if any). 
## The user must build the program before running the tests
check: node_modules
	CI=true $(PNPM) test:e2e

## Create a distribution tar file for this program.
dist:
	$(MAKE) init
	$(PNPM) build

########################
# App specific targets #
########################

install-sonar-plugin:
	$(PNPM) build

###################################
# Maintainer and internal targets #
###################################

.PHONY: prune
.PHONY: init
.PHONY: init-dev
.PHONY: update
.PHONY: node-version
.PHONY: ci
.PHONY: dev

## Removes unreferenced packages from the pnpm store.
prune: node_modules
	@$(MAKE) init-pnpm
	@$(PNPM) store prune

## Install Node & PNPM if necessary, and install dependencies
init:
	@$(MAKE) init-pnpm
	@$(MAKE) node_modules

## Install PNPM via Node.js Corepack
init-pnpm:
ifeq ($($(SHELL) command -v pnpm),)
	@$(MAKE) init-node
	@$(COREPACK) enable pnpm
else
	@echo "pnpm $(shell pnpm -v) found"
endif

## Install Node.js via NVM with version defined in .nvmrc
init-node:
ifeq ($($(SHELL) command -v node),)
	@$(MAKE) init-nvm
	@$(NVM) install
else
	@echo "node.js $(shell node -v) found"
endif

## Install NVM via curl | bash and init with nvm.sh
init-nvm:
ifeq ($($(SHELL) command -v nvm),)
	@$(CURL) -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.3/install.sh | bash
	@$(SHELL) \. "$HOME/.nvm/nvm.sh"
else
    @echo "nvm $(shell nvm -v) found"
endif

init-dev:
	@$(MAKE) init
	@$(PNPM) exec playwright install

init-java:
ifneq ($($(SHELL) command -v javac),)
    @echo "nvm $(shell javac -v) found"
else ifneq ($($(SHELL) command -v apt-get),)
	@sudo $(APT) install openjdk-8-jrk-headless
else ifneq ($($(SHELL) command -v brew),)
	@$(BREW) brew install openjdk@11 
else ifneq ($($(SHELL) command -v yum),)
	@su -c "$(YUM) install java-1.8.0-openjdk"
endif

init-maven:
	@$(MAKE) init-java
	@$(PNPM) exec playwright install

init-brew:
	@$(SHELL) -c "$($(CURL) -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
	@echo 'eval "$(/opt/homebrew/bin/brew shellenv)"' >> /Users/vishal/.zprofile eval "$(/opt/homebrew/bin/brew shellenv)"


## Update project dependencies
update:
	$(PNPM) upgrade

## Activates the expected node.js version ( Download it if necessary)
node-version:
	$(NVM) use

## Emulates CI Validation Workflow
ci: node_modules
	$(MAKE) node.js
	CI=true $(PNPM) lint
	CI=true $(PNPM) coverage
	CI=true $(PNPM) build
	CI=true $(PNPM) exec playwright install --with-deps --only-shell
	CI=true $(PNPM) test:e2e

## Builds projects in development mode with live watching
dev: node_modules
	$(PNPM) dev

node_modules: package.json
	$(PNPM) install
