import { validate } from "./modelValidator"

export class Issues {
    #implementation
    constructor(implementation) {
        validate(implementation)
        this.#implementation = implementation
    }
    async getIssuesFacet(facetName, settings) {
        return await this.#implementation.getIssuesFacet(facetName, settings)
    }
}