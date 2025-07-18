/**
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/helpers/__tests__/l10n-test.ts
 * @see
 */

import {
  beforeEach,
  describe,
  expect,
  test
} from 'vitest'

import { t, tp, clearCache, addToCache } from './sonar-i18n'

const MSG = 'my_message';

beforeEach(() => {
  clearCache()
})

describe('translate', () => {
  test('should translate simple message', () => {
    addToCache({ my_key: MSG })
    expect(t('my_key')).toBe(MSG)
  })

  test('should translate message with composite key', () => {
    addToCache({ 'my.composite.message': MSG })
    expect(t('my', 'composite', 'message')).toBe(MSG)
    expect(t('my.composite', 'message')).toBe(MSG)
    expect(t('my', 'composite.message')).toBe(MSG)
    expect(t('my.composite.message')).toBe(MSG)
  })

  test('should not translate message but return its key', () => {
    expect(t('random')).toBe('random')
    expect(t('random', 'key')).toBe('random.key')
    expect(t('composite.random', 'key')).toBe('composite.random.key')
  })
})

describe('translateWithParameters', () => {
  test('should translate message with one parameter in the beginning', () => {
    addToCache({ x_apples: '{0} apples' })
    expect(tp('x_apples', 5)).toBe('5 apples')
  })

  test('should translate message with one parameter in the middle', () => {
    addToCache({ x_apples: 'I have {0} apples' })
    expect(tp('x_apples', 5)).toBe('I have 5 apples')
  })

  test('should translate message with one parameter in the end', () => {
    addToCache({ x_apples: 'Apples: {0}' })
    expect(tp('x_apples', 5)).toBe('Apples: 5')
  })

  test('should translate message with several parameters', () => {
    addToCache({
      x_apples: '{0}: I have {2} apples in my {1} baskets - {3}'
    })
    expect(tp('x_apples', 1, 2, 3, 4)).toBe(
      '1: I have 3 apples in my 2 baskets - 4'
    )
  })

  test('should not be affected by replacement pattern XSS vulnerability of String.replace', () => {
    addToCache({ x_apples: 'I have {0} apples' })
    expect(tp('x_apples', '$`')).toBe('I have $` apples')
  })

  test('should not translate message but return its key', () => {
    expect(tp('random', 5)).toBe('random.5')
    expect(tp('random', 1, 2, 3)).toBe('random.1.2.3')
    expect(tp('composite.random', 1, 2)).toBe('composite.random.1.2')
  })
})
