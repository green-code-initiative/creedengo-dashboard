install:
	./scripts/install.sh

ci:
	pnpm env use -g 22.17.0
	CI=true pnpm build
	CI=true pnpm lint
	CI=true pnpm coverage
	CI=true pnpm test:e2e

build:
	pnpm build

dev:
	pnpm dev
