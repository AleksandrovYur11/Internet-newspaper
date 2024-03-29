import { createRouter, createWebHistory } from "vue-router"

import AuthorizationView from "@/views/AuthorizationView.vue"
import RegistrationView from "@/views/RegistrationView.vue"
import NewsView from "@/views/NewsView.vue"

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    linkActiveClass: "active",
    routes: [
        {
            path: "/",
            redirect: "/news/fresh-news", 
            component: NewsView,
        },
        {
            path: "/auth/sign-in",
            name: "sign-in",
            component: AuthorizationView,
        },
        {
            path: "/auth/sign-up",
            name: "sign-up",
            component: RegistrationView,
        },
        {
            path: "/news/fresh-news",
            name: "news",
            component: NewsView,
        },
    ],
})

export default router
