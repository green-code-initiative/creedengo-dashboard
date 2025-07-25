
const nullStore = { get() {}, set() {} };

const defaultStore = {
    get(key){
        return localStorage.getItem(key)
    },
    set(key, value){
        return localStorage.setItem(key, value)
    }
}

export default  {
    get storageReady() {
        return Boolean(this.get && (this.get !== nullStore.get))
    },
    get servicesReady() {
        return Boolean(this.services)
    },
    reset() {
        delete this.services
        Object.assign(this, nullStore)
    },
    initStorage(store = defaultStore) {
        this.get = key => store.get(key);
        this.set = (key, value) => store.set(key, value);
    },
    initServices(services) {
        this.services = services;
    }
}
