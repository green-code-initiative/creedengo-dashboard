import { vi, beforeEach, describe, expect, test } from 'vitest'

import settings from './settings.service'
import { set } from 'lodash-es';

let store;

const storageApi = {
  set: (key, value) => store[key] = value,
  get: (key) => store[key],
};

beforeEach(()=> {
  store = {};
  settings.resetSettings()
})

describe('Settings service', () => {

  describe('initStorage', () => {
    test('apply the given store', async () => {
      settings.initStorage(storageApi)
      expect(settings.get('foo')).toBe(undefined)
      settings.set('foo', 'bar')
      expect(settings.get('foo')).toBe('bar')
    })

  })
  
  describe('storageReady', () => {
    test('return false if not initialized', async () => {
      expect(settings.storageReady).toBe(false)
    })
    test('return true if initialized', async () => {
      settings.initStorage(storageApi)
      expect(settings.storageReady).toBe(true)
    })
  })

  describe('resetSettings', () => {
    test('storageReady return false after initialized and reset', async () => {
      settings.initStorage(storageApi)
      settings.resetSettings()
      expect(settings.storageReady).toBe(false)
    })

  })

})
