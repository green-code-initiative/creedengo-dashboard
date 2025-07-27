
const nullStore = { get() {}, set() {} };

const defaultStore = {
    get(key){
        return globalThis.localStorage.getItem(key)
    },
    set(key, value){
        return globalThis.localStorage.setItem(key, value)
    }
}

export default  {
    get storageReady() {
        return Boolean(this.get && (this.get !== nullStore.get))
    },
    resetSettings() {
        Object.assign(this, nullStore)
    },
    initStorage(store = defaultStore) {
        this.get = key => store.get(key);
        this.set = (key, value) => store.set(key, value);
    }
}
