import { defineStore } from "pinia"
import router from "@/router/index.js"

import { useCommentsStore } from "@/stores/CommentsStore"

export const useAuthStore = defineStore("auth", {
    state: () => ({
        ///////////
        user: null,
        role: null,
        news: null,
        user_role: "",
        user_name: "",
    }),
    actions: {
        async updateAccessToken() {
            const refreshToken = {
                refreshToken: sessionStorage.getItem("jwtRefreshToken"),
            }
            try {
                const response = await fetch(
                    "http://localhost:8085/auth/refresh-token",
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(refreshToken),
                    }
                )
                const responseData = await response.json()

                if (
                    responseData.refreshToken ===
                    `Failed for [${refreshToken.refreshToken}]: Refresh token was expired. Please make a new signin request`
                ) {
                    return await this.deleteToken() // когда refresh-токен истек, вылетаем из приложения и удаляем сохраненные данные
                }

                sessionStorage.setItem("jwtToken", responseData.accessToken)
            } catch (error) {
                console.error("Update token error:", error)
            }
        },

        async deleteToken() {
            const refreshToken = {
                refreshToken: sessionStorage.getItem("jwtRefreshToken"),
            }
            try {
                const response = await fetch(
                    "http://localhost:8085/auth/sign-out",
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(refreshToken),
                    }
                )

                if (response.ok) {
                    router.push("/auth/sign-in")

                    sessionStorage.removeItem("jwtToken")
                    sessionStorage.removeItem("jwtRefreshToken")
                    sessionStorage.removeItem("user_role")
                    sessionStorage.removeItem("user_id")
                    sessionStorage.removeItem("user_name")

                    const CommentStore = useCommentsStore() // чтобы комментарии для нового входа выводились с нуля
                    CommentStore.commentsCountInfo = []
                    
                    return true
                } else {
                    throw new Error("Удаление refresh-токен не прошло")
                }

            } catch (error) {
                console.error("Sign-out error:", error)
            }
        },

        async authorization(textEmail, textPassword) {
            const loginData = {
                email: textEmail,
                password: textPassword,
            }
            try {
                const response = await fetch(
                    "http://localhost:8085/auth/sign-in",
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(loginData),
                    }
                )

                if (!response.ok) {
                    alert("Неверный вход!")
                    throw new Error("Authentication failed")
                }
                const responseData = await response.json()

                sessionStorage.setItem("user_id", responseData.id)
                sessionStorage.setItem("user_role", responseData.roles[0])
                sessionStorage.setItem(
                    "user_name",
                    responseData.surname.charAt(0).toUpperCase() +
                        responseData.surname.slice(1) +
                        " " +
                        responseData.name[0].toUpperCase() +
                        "."
                )

                this.user = sessionStorage.getItem("user_id")
                this.role = sessionStorage.getItem("user_role")
                this.user_name = sessionStorage.getItem("user_name")

                const jwtToken = responseData.accessToken
                const jwtRefreshToken = responseData.refreshToken
                sessionStorage.setItem("jwtRefreshToken", jwtRefreshToken)
                sessionStorage.setItem("jwtToken", jwtToken)

                router.push("/news/fresh-news")
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },

        async registration(regData) {
            const userData = {
                name: regData.textName,
                surname: regData.textSurname,
                email: regData.textEmail,
                password: regData.textPassword,
            }
            try {
                const response = await fetch(
                    "http://localhost:8085/auth/sign-up",
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(userData),
                    }
                )

                if (!response.ok) {
                    if (response.status === 409) {
                        alert("Пользователь с данным email уже существует!")
                    }
                    throw new Error("Registration failed")
                }

                router.push("/auth/sign-in")
                alert("Вы успешно зарегестрированы!")
            } catch (error) {
                console.error("Registration error:", error)
            }
        },
    },
})
