import { describe, it, expect } from 'vitest'

import { defineVitestConfig } from './index.js'

describe('defineVitestConfig', () => {

    describe('require import.meta to define root', () => {
        it('Should throw if import.meta is not provided', () => {
            expect(defineVitestConfig).toThrow()
            expect(() => defineVitestConfig({ importMeta: {} })).toThrow()
        })

        it('Should not throw if import.meta is provided', () => {
            const importMeta = import.meta
            expect(() => defineVitestConfig({ importMeta })).not.toThrow()
        })
    })
    describe('environment', () => {
        it('Should set "jsdom" as default environment', () => {
            const { test } = defineVitestConfig({ importMeta: import.meta })
            expect(test.environment).toBe('jsdom')
        })

        it('Should apply provided environment', () => {
            const { test } = defineVitestConfig({ 
              importMeta: import.meta,
              environment: 'foo'
            })
            expect(test.environment).toBe('foo')
        })

    })
    describe('include', () => {
        it('Should set "include" to ["**/*.spec.js"]', () => {
            const { test } = defineVitestConfig({ importMeta: import.meta })
            expect(test.include).toEqual(["**/*.spec.js"])
        })

    })
    describe('reporter', () => {
        it('Should set "reporter" to ["text", "lcov"]', () => {
            const { test } = defineVitestConfig({ importMeta: import.meta })
            expect(test.coverage.reporter).toEqual(['text', 'lcov'])
        })
    })
})
