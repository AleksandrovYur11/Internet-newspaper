import { defineStore } from "pinia"
import { useAuthStore } from "./AuthStore"

import router from "@/router/index.js"

export const useNewsStore = defineStore("news", {
    state: () => ({
        news: null,
        modal: false,
        edit_post_id: null,
        positive: "" 
    }),
    actions: {
        setPositiveTheme(theme_name){ // для вставки названия темы в фильтр при нажатии на тему
            this.positive = theme_name 
        },
       
        async getnews() {
            this.news = []
            
            try {
                const response = await fetch(
                    "http://localhost:8085/news/fresh-news",
                    {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                )
                if (!response.ok) {
                    throw new Error("Failed to fetch data")
                }
                const responseData = await response.json()
                this.news = responseData

                this.$patch({
                    news: this.news,
                })
            } catch (error) {
                console.error("Fetch error:", error)
            }
        },

        async getAuthStoreMethods() {
            const AuthStore = useAuthStore()
            return await AuthStore.updateAccessToken() // успешное/неуспешное обновление токена или вылет из приложения
        },
        
        isLiked(id_news, user_id) { // проверка на существование лайков пользователя конкретного посту
            const existNews = this.news.find((news) => news.id === id_news)
            const existLike = existNews.likes.some(
                (like) => like.user.id === Number(user_id)
            )
            return existLike
        },

        async addLike(id_news, user_id) {
            const jwtToken = sessionStorage.getItem("jwtToken")
            
            if (!jwtToken) {
                router.push("/auth/sign-in")
                alert("Войдите или зарегестрируйтесь")
                return
            }

            const existLike = this.isLiked(id_news, user_id)

            try {
                let response
                if (existLike) {
                    response = await fetch(
                        `http://localhost:8085/likes?newsId=${id_news}`,
                        {
                            method: "DELETE",
                            headers: new Headers({
                                Authorization: "Bearer " + jwtToken,
                                "Content-Type": "application/json",
                            }),
                        }
                    )
                } else {
                    response = await fetch(
                        `http://localhost:8085/likes/save?newsId=${id_news}`,
                        {
                            method: "POST",
                            headers: new Headers({
                                Authorization: "Bearer " + jwtToken,
                                "Content-Type": "application/json",
                            }),
                        }
                    )
                }

                if (!response.ok) {
                    if (response.status === 401) { // проверка на истечение access токена и его обновление
                        const result = await this.getAuthStoreMethods() 
                        if (result) {
                            return this.addLike(id_news, user_id) // повторяем вызываемый метод после обновления access токена
                        } else {
                            console.log("Failed to update access token")
                        }
                    } 
                    throw new Error("Access token update required")
                }

                const updatedNewsIndex = this.news.findIndex(
                    (news) => news.id === id_news
                )

                if (existLike) {
                    this.news[updatedNewsIndex].likes = this.news[ // удаление
                        updatedNewsIndex
                    ].likes
                        .filter((like) => like.user.id !== Number(user_id))
                } else {
                    const newLike = { user: { id: Number(user_id) } } // добавление
                    this.news[updatedNewsIndex].likes.push(newLike)
                }

            } catch (error) {
                console.error("Add like error ", error)
            }
        },
        
        async addNews(newsTitle, newsText, picture, themes) {
            const picture_url_obg = {
                url: picture,
            }

            const newsData = {
                newsTitle: newsTitle,
                newsText: newsText,
                picture: picture_url_obg,
                themes: themes
                    .split(",")
                    .map((theme) => ({ name: theme.trim() })),
            }

            const jwtToken = sessionStorage.getItem("jwtToken")
            
            try {
                const response = await fetch(
                    `http://localhost:8085/news/save`,
                    {
                        method: "POST",
                        headers: new Headers({
                            Authorization: "Bearer " + jwtToken,
                            "Content-Type": "application/json",
                        }),
                        body: JSON.stringify(newsData),
                    }
                )
                if (!response.ok) {
                    if (response.status === 401) {
                        const result = await this.getAuthStoreMethods() 
                        if (result) {
                            return this.addNews(
                                newsTitle,
                                newsText,
                                picture,
                                themes
                            )
                        } else {
                            console.log("Failed to update access token")
                        }
                    } 
                    throw new Error("Access token update required")
                }
                
                this.getnews()
                this.closeModal()

            } catch (error) {
                console.error("Add news error:", error)
            }
        },
        
        async filterThemes(positive, negative) {
            const themes = {
                favoritesThemes: positive
                    .split(",")
                    .map((theme) => ({ name: theme.trim() })),
                forbiddenThemes: negative
                    .split(",")
                    .map((theme) => ({ name: theme.trim() })),
            }

            try {
                const response = await fetch(
                    `http://localhost:8085/news/user-themes`,
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify(themes),
                    }
                )
                if (!response.ok) {
                    throw new Error("Failed request")
                }

                const responseData = await response.json()

                if (responseData.length === 0) {
                    alert('Ничего не найдено')
                } else {
                   this.$patch({
                    news: responseData,
                }) 
                }
            } catch (error) {
                console.error("Filter error:", error)
            }
        },

        showModal() {
            this.modal = true
        },

        closeModal() {
            this.modal = false
        },
    },
})
