const forbiddenProperties = ['init', 'servicesReady', 'resetServices'];

function missImplementation() {
    throw new Error('API adapter is missing or incomplete')
}

const nullService = {
    getIssuesFacet: missImplementation,
    getNumberOfLineOfCode: missImplementation
}

function validate(adapter) {
    const hasRequired = Object.keys(nullService).every(
        value => (typeof adapter[value] === 'function')
    )
    const hasForbidden = forbiddenProperties.some(
        value => adapter[value] !== undefined
    )
    return hasRequired && !hasForbidden
}

export default {
    init(adapter) {
        validate(adapter);
        this.services = adapter
    },
    get servicesReady() {
        return Boolean(this.services && this.services !== nullService)
    },
    resetServices() {
        this.services = nullService
    },
}