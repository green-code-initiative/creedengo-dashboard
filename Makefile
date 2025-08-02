all:
	install

.PHONY: clean
clean:
	rm -rf **/dist **/coverage **/playwright-report **/node **/target

install:
	./scripts/install.sh

use:
	$(pnpm env use -g $(cat .nvmrc))

ci:
	$(pnpm env use -g $(cat .nvmrc))

	CI=true pnpm lint
	CI=true pnpm coverage
	CI=true pnpm build
	CI=true pnpm exec playwright install --with-deps --only-shell
	CI=true pnpm test:e2e

build: use
	pnpm build

dev: use
	pnpm dev
