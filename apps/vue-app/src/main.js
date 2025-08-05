import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
//import router from './router'

import core from '@creedengo/core-services'
import SonarApi from '@creedengo/sonar-services'

core.api.init(SonarApi)

// MOCK
if (globalThis?.process.env.NODE_ENV === 'development') {
    const { worker } = await import('./api/mocks/browser')
    await worker.start()
}

createApp(App)
    //.use(router)
    .mount('#app')
