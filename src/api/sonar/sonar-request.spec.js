/**
 * @see https://github.com/SonarSource/sonarqube/blob/master/server/sonar-web/src/main/js/helpers/__tests__/l10n-test.ts
 * @see
 */

import { beforeEach, afterEach, describe, expect, test, vi } from 'vitest'

import SonarRequest from './sonar-request'
/*
vi.mock('./sonar-i18n', async () => {
  return { t: vi.fn() }
})
  */

const { Ok, BadRequest } = SonarRequest.HttpStatus;

vi.mock('./sonar-toast', async () => {
  return {
    throwGlobalError: vi.fn(),
    addGlobalSuccessMessage: vi.fn()
  }
})

const url = '/my-url'

function mockResponse(headers = {}, status = Ok, value) {
  const body = value && value instanceof Object ? JSON.stringify(value) : value
  const response = new Response(body, { headers, status })
  response.json = vi.fn().mockResolvedValue(value)
  response.text = vi.fn().mockResolvedValue(value)
  return response
}

describe('sonar-request', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    window.fetch = vi.fn().mockResolvedValue(mockResponse({}, Ok, {}))
  })
  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('getText', () => {
    test('should get text without parameters', async () => {
      const response = mockResponse({}, Ok, '')
      window.fetch = vi.fn().mockResolvedValue(response)
      await SonarRequest.getText(url)

      expect(window.fetch).toHaveBeenCalledWith(url, expect.objectContaining({ method: 'GET' }))
      expect(response.text).toHaveBeenCalled()
    })

    test('should get text with parameters', async () => {
      await SonarRequest.getText(url, { data: 'test' })
      expect(window.fetch).toHaveBeenCalledWith(
        url + '?data=test',
        expect.objectContaining({ method: 'GET' })
      )
    })
  })

  describe('parseError', () => {
    test('should parse error and return the message', async () => {
      const response = new Response(JSON.stringify({ errors: [{ msg: 'Error1' }] }), {
        status: BadRequest
      })
      expect(await SonarRequest.parseError(response)).toBe('Error1')
    })

    test('should parse error and return concatenated messages', async () => {
      const response = new Response(
        JSON.stringify({ errors: [{ msg: 'Error1' }, { msg: 'Error2' }] }),
        { status: BadRequest }
      )
      expect(await SonarRequest.parseError(response)).toBe('Error1. Error2')
    })

    test('should parse error and return default message', async () => {
      const response = new Response('{}', { status: BadRequest })
      expect(await SonarRequest.parseError(response)).toBe('default_error_message')
      const responseUndefined = new Response('', { status: BadRequest })
      expect(await SonarRequest.parseError(responseUndefined)).toBe('default_error_message')
    })
  })

  describe('postJSON', () => {
    test('should post without parameters and get json', async () => {
      const response = mockResponse()
      window.fetch = vi.fn().mockResolvedValue(response)
      await SonarRequest.postJSON(url)

      expect(window.fetch).toHaveBeenCalledWith(url, expect.objectContaining({ method: 'POST' }))
      expect(response.json).toHaveBeenCalled()
    })

    test('should post with a body and get json', async () => {
      await SonarRequest.postJSON(url, { data: 'test' })
      expect(window.fetch).toHaveBeenCalledWith(
        url,
        expect.objectContaining({ body: 'data=test', method: 'POST' })
      )
    })
  })

  describe('postJSONBody', () => {
    test('should post without parameters and get json', async () => {
      const response = mockResponse();
      window.fetch = vi.fn().mockResolvedValue(response);
      await SonarRequest.postJSONBody(url);
  
      expect(window.fetch).toHaveBeenCalledWith(url, expect.objectContaining({ method: 'POST' }));
      expect(response.json).toHaveBeenCalled();
    });
  
    test('should post with a body and get json', async () => {
      await SonarRequest.postJSONBody(url, { nested: { data: 'test', withArray: [1, 2] } });
      expect(window.fetch).toHaveBeenCalledWith(
        url,
        expect.objectContaining({
          headers: { Accept: 'application/json', 'Content-Type': 'application/json' },
          body: '{"nested":{"data":"test","withArray":[1,2]}}',
          method: 'POST',
        }),
      );
    });
  });
  
  describe('post', () => {
    test('should post without parameters and return nothing', async () => {
      const response = mockResponse();
      window.fetch = vi.fn().mockResolvedValue(response);
      await SonarRequest.post(url, { data: 'test' });
  
      expect(window.fetch).toHaveBeenCalledWith(
        url,
        expect.objectContaining({ body: 'data=test', method: 'POST' }),
      );
      expect(response.json).not.toHaveBeenCalled();
      expect(response.text).not.toHaveBeenCalled();
    });
  
    test('should handle array values', async () => {
      const response = mockResponse();
      window.fetch = vi.fn().mockResolvedValue(response);
      await SonarRequest.post(url, { dataArray: ['1', '2'] });
  
      expect(window.fetch).toHaveBeenCalledWith(
        url,
        expect.objectContaining({ body: 'dataArray=1&dataArray=2', method: 'POST' }),
      );
    });
  });

})
