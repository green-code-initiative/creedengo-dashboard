import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'

// MOCK
if (process.env.NODE_ENV === 'development') {
    const { worker } = await import('./mocks/browser')
    await worker.start()
}

createApp(App).mount('#app')
