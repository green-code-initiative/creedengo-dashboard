install:
	./scripts/install.sh

test:
	turbo lint
	turbo coverage
	turbo e2e

build:
	turbo build

dev:
	turbo dev