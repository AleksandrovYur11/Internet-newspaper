import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

router.replace('/auth/news')

import "bootstrap/dist/css/bootstrap.css"
import "bootstrap-vue-next/dist/bootstrap-vue-next.css"

import { BootstrapVueNext } from "bootstrap-vue-next"

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(BootstrapVueNext)

app.mount("#app")