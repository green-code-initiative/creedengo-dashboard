/**
 * Detect if a function is Async or not
 * 
 * @warning fails if it is a regular function returning a Promise
 * @param {Function} method 
 * @returns Boolean
 */
function isAsync(method) {
    const functionTag = Object.getPrototypeOf(method)[Symbol.toStringTag];
    return (functionTag === 'AsyncFunction')
}

/**
 * Create a string description of the expected method or property
 * 
 * @example "create" async method with 3 parameters
 * @example Array "items" property"
 * 
 * @param {String} name 
 * @param {Object} descriptor
 * @param {String} descriptor.value 
 * @returns String
 */
function getTypeDescription(name, { value }) {
    let type = value === null ? 'null' : typeof value;
    if (type === 'function') {
        const async = isAsync(value) ? 'async ' : ''
        const params = `with ${value.length || 'no'} parameters`
        return `"${name}" ${async}method ${params}`
    }
    if (type === 'object') {
        type = value.constructor.name ?? 'Object'
    }
    return `${type} "${name}" property`
}

/**
 * Check implementation methods & properties conformity with the model
 * 
 * @param {Function} model 
 * @param {Object} implementation 
 * @throws TypeError
 */
export function validate(model, implementation) {
    const expectations = Object.getOwnPropertyDescriptors(model.prototype)
    const implementations = Object.getOwnPropertyDescriptors(implementation)
    delete expectations.constructor;
    for (let [name, expectation] of Object.entries(expectations)) {
        const expectedType = getTypeDescription(name, expectation)
        const received = implementations?.[name]
        if (!received) {
            throw new TypeError(`Implementation miss the ${expectedType}`)
        }
        const receivedType = getTypeDescription(name, received)
        if (expectedType !== receivedType) {
            throw new TypeError(
                `Implementation has a ${receivedType} instead of a ${expectedType}`
            )
        }
    }
}
