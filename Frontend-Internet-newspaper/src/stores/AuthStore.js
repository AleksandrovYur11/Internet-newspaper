import { defineStore } from "pinia"
import router from "@/router/index.js"

import { useNewsStore } from "@/stores/NewsStore"
import { useCommentsStore } from "@/stores/CommentsStore"

export const useAuthStore = defineStore("auth", {
    state: () => ({
        JWT: null,
        selectedRole: null,
        user: null,
        role: null,
        reg_users: [],
        news: null,
        user_role: "",
        user_name: ''
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
                    return await this.deleteToken() // использование await для вызова асинхронного метода
                }
                sessionStorage.setItem("jwtToken", responseData.accessToken)
            } catch (error) {
                return false
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
                console.log(response)
                if (response.ok) {
                    // window.location.reload() - ???
                    router.push("/auth/sign-in")
                    sessionStorage.removeItem("jwtToken")
                    sessionStorage.removeItem("jwtRefreshToken")
                    sessionStorage.removeItem("user_role")
                    sessionStorage.removeItem("user_id")

                    const CommentStore = useCommentsStore()
                    CommentStore.commentsCountInfo = []
                    console.log(CommentStore.commentsCountInfo)

                    //alert("Ваша сессия истекла, требуется войти заново")
                    return true
                } else {
                    throw new Error("refresh не дошел")
                }
            } catch (error) {
                return false
            }
        },
        async login(textEmail, textPassword) {
            console.log('888')
            const loginData = {
                email: textEmail,
                password: textPassword,
            }
            console.log(loginData)
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

                console.log(response)
                if (!response.ok) {
                    alert("Неверный вход!")
                    throw new Error("Authentication failed")
                }

                const responseData = await response.json()

                sessionStorage.setItem("user_id", responseData.id)
                sessionStorage.setItem("user_role", responseData.roles[0])

                this.user = sessionStorage.getItem("user_id")
                this.role = sessionStorage.getItem("user_role")

                console.log(this.user)
                console.log(this.role)

                //console.log()

                const jwtToken = responseData.accessToken
                const jwtRefreshToken = responseData.refreshToken
                sessionStorage.setItem("jwtRefreshToken", jwtRefreshToken)

                sessionStorage.setItem("user_name", responseData.name)

                this.role = responseData.roles[0]
                console.log(responseData.roles[0])

                sessionStorage.setItem("jwtToken", jwtToken)

                router.push("/news/fresh-news")
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async register(regData) {
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
                console.log(response)
                if (!response.ok) {
                    if (response.status === 409) {
                        alert('Пользователь с данным email уже существует!')
                    }
                    throw new Error("Registration failed")
                }

                router.push("/auth/sign-in")
                alert('Вы успешно зарегестрированы!')
            } catch (error) {
                console.error("Registration error:", error)
            }
        },
    },
})
