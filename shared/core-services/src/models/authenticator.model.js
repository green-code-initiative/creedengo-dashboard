
export class Authenticator {
  #implementation
  constructor(authenticator) {
    this.#implementation = authenticator
  }
  get connected() {
    return this.#implementation.connected
  }
  get pending() {
    return this.#implementation.pending
  }
  async disconnect(...params) {
    this.#implementation.disconnect(...params)
  }
  async connect(...params) {
    this.#implementation.connect(...params)    
  }
}