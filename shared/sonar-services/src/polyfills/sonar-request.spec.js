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

function mockResponse(headers = {}, status = Ok, value) {
  const body = value && value instanceof Object ? JSON.stringify(value) : value
  const response = new Response(body, { headers, status })
  response.json = vi.fn().mockResolvedValue(value)
  response.text = vi.fn().mockResolvedValue(value)
  return response
}

const fetchMock = vi.fn()

vi.stubGlobal('fetch', fetchMock)

describe('sonar-request', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    fetchMock.mockResolvedValue(mockResponse({}, Ok, {}))
  })
  afterEach(() => {
    vi.restoreAllMocks()
  })

  describe('getText', () => {
    test('should get text without parameters', async () => {
      const response = mockResponse({}, Ok, '')
      fetchMock.mockResolvedValue(response)
      await SonarRequest.getText('/my-url')

      expect(fetchMock).toHaveBeenCalledWith('/my-url', expect.objectContaining({ method: 'GET' }))
      expect(response.text).toHaveBeenCalled()
    })

    test('should get text with parameters', async () => {
      await SonarRequest.getText('/my-url', { data: 'test' })
      expect(fetchMock).toHaveBeenCalledWith(
        '/my-url?data=test',
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
      fetchMock.mockResolvedValue(response)
      await SonarRequest.postJSON('/my-url')

      expect(fetchMock).toHaveBeenCalledWith('/my-url', expect.objectContaining({ method: 'POST' }))
      expect(response.json).toHaveBeenCalled()
    })

    test('should post with a body and get json', async () => {
      await SonarRequest.postJSON('/my-url', { data: 'test' })
      expect(fetchMock).toHaveBeenCalledWith(
        '/my-url',
        expect.objectContaining({ body: 'data=test', method: 'POST' })
      )
    })
  })

  describe('postJSONBody', () => {
    test('should post without parameters and get json', async () => {
      const response = mockResponse();
      fetchMock.mockResolvedValue(response);
      await SonarRequest.postJSONBody('/my-url');
  
      expect(fetchMock).toHaveBeenCalledWith('/my-url', expect.objectContaining({ method: 'POST' }));
      expect(response.json).toHaveBeenCalled();
    });
  
    test('should post with a body and get json', async () => {
      await SonarRequest.postJSONBody('/my-url', { nested: { data: 'test', withArray: [1, 2] } });
      expect(fetchMock).toHaveBeenCalledWith(
        '/my-url',
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
      fetchMock.mockResolvedValue(response);
      await SonarRequest.post('/my-url', { data: 'test' });
  
      expect(fetchMock).toHaveBeenCalledWith(
        '/my-url',
        expect.objectContaining({ body: 'data=test', method: 'POST' }),
      );
      expect(response.json).not.toHaveBeenCalled();
      expect(response.text).not.toHaveBeenCalled();
    });
  
    test('should handle array values', async () => {
      const response = mockResponse();
      fetchMock.mockResolvedValue(response);
      await SonarRequest.post('/my-url', { dataArray: ['1', '2'] });
  
      expect(fetchMock).toHaveBeenCalledWith(
        '/my-url',
        expect.objectContaining({ body: 'dataArray=1&dataArray=2', method: 'POST' }),
      );
    });
  });

})
