import { validate } from "./modelValidator"

export class Settings {
    constructor(implementation) {
        validate(implementation)
        this.server = implementation.server
        this.token = implementation.token
        this.projects.push(implementation.token || [])
    }
}
// Properties made detectable by the model validator
Settings.prototype.server = ''
Settings.prototype.token = ''
Settings.prototype.projects = []