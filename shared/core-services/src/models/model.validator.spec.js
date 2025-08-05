import { describe, expect, test } from 'vitest'

import { validate } from './modelValidator'

describe('validate(model, implementation)', () => {

  describe('Methods compliance', () => {

    test('Should describe missing method with parameters number', async () => {
      class Foo {
        bar(one, two, three) { return one + two + three + 5 }
      }
      expect(() => validate(Foo, {})).toThrowError(
        TypeError('Implementation miss the "bar" method with 3 parameters')
      )
    })

    test('Should describe missing async method with no parameters', async () => {
      class Foo {
        async bar() { return 5 }
      }
      expect(() => validate(Foo, {})).toThrowError(
        TypeError(
          'Implementation miss the "bar" async method with no parameters'
        )
      )
    })

    test('Should describe Invalid method', async () => {
      class Foo {
        async bar() { return 5 }
      }
      const implementation = { bar: () => {} }
      expect(() => validate(Foo, implementation)).toThrowError(
        TypeError(
          'Implementation has a "bar" method with no parameters' +
          ' instead of a "bar" async method with no parameters'
        )
      )
    })

    test('Should validate if no issue', async () => {
      class Foo {
        bar() { return 5 }
      } 
      const implementation = { bar: () => {} }
      expect(
        () => validate(Foo, implementation)
      ).not.toThrowError()
    })

  })

  describe('Properties compliance', () => {

    test('Should validate if no issue', async () => {
      class Foo {} 
      Foo.prototype.bar = ''
      Foo.prototype.baz = 42
      expect(() => validate(Foo, { bar: 'some text', baz: 89 })).not.toThrowError()
    })

    test('Should fail on missing property', async () => {
      class Foo {} 
      Foo.prototype.bar = ''
      Foo.prototype.baz = 42
      expect(() => validate(Foo, { baz: 89 })).toThrowError(
        TypeError('Implementation miss the string "bar" property')
      )
    })

    test('Should fail on wrong type property', async () => {
      class Foo {} 
      Foo.prototype.bar = ''
      Foo.prototype.baz = 42
      expect(() => validate(Foo, { bar: [], baz: 89 })).toThrowError(
        TypeError(
          'Implementation has a Array "bar" property' +
          ' instead of a string "bar" property'
        )
      )
    })
  })

})
