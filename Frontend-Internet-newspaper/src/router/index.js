import { createRouter, createWebHistory } from "vue-router"
import AuthorizationView from "@/views/AuthorizationView.vue"
import RegistrationView from "@/views/RegistrationView.vue"
import NewsView from "@/views/NewsView.vue"
import NomeView from "@/views/HomeView.vue"

// import { useAuthStore } from "@/stores/AuthStore.vue"

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    linkActiveClass: "active",
    routes: [
        {
            path: "/",
            redirect: "/news/fresh-news", // перенаправление старта
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
            path: "/auth/home",
            name: "home",
            component: NomeView,
            meta: { requiresAuth: true },
        },
        {
            path: "/news/fresh-news",
            name: "news",
            component: NewsView,
        },
    ],
})

// router.beforeEach((to, from, next) => {
//   if (to.matched.some(record => record.meta.requiresAuth)) {

//     const jwtToken = localStorage.getItem('jwtToken'); // Получение токена из localStorage

//     if (!jwtToken) {
//       // Если токена нет, перенаправляем на страницу входа
//       next({ path: '/auth/signin' });
//     } else {
//       // Если токен есть, разрешаем доступ к защищенному маршруту
//       next();
//     }
//   } else {
//     // Для открытых маршрутов
//     next();
//   }
// });

export default router
