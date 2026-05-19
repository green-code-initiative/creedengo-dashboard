/**
 * 
 * @param {Response} response 
 */
export function responseToError(response) {

    throw new HttpError(

    )
}
const HttpErrorMessage = {
    400: 'Bad Request',
    401: 'Unauthorized',
    402: 'Payment Required',
    403: 'Forbidden',
    404: 'Not Found',
    405: 'Method Not Allowed',
    406: 'Not Acceptable',
    408: 'Request Timeout',
    409: 'Conflict',
    410: 'Gone',
    412: 'Precondition Failed',
    413: 'Content Too Large',
    414: 'URI Too Long',

    500: 'Internal Server Error',
    501: 'Not Implemented',
    503: 'Service Unavailable',
} 

/**
 * @param {Response} cause 
 */
class HttpError extends Error {
    constructor(cause) {
        const statusText = HttpErrorMessage[cause.status];
        const message = `Error ${cause.status}: ${statusText}`;
        super(message);
        this.name = `Http${statusText.replace(' ', '')}`;
        this.cause = cause;
    }
    async parseServerMessage() {
        const { cause } = this;
        if (cause.body === null) {
            return ''
        }
        const json = await cause.json();
        const data = 'data' in json ? data.json : json;
        const { message, errors } = data
        return message ?? errors?.map((error) => error.msg).join('. ') ?? '';
    }
}