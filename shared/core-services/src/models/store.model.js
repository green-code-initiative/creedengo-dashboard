import { validate } from "./modelValidator"

export class Store {
    #implementation
    constructor(implementation) {
        validate(implementation)
        this.#implementation = implementation
    }
    async get(key) {
        return await this.#implementation.get(key)
    }
    async set(key, value) {
        return await this.#implementation.set(key, value)
    }
}