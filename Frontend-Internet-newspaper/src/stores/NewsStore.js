import { defineStore } from "pinia"
import { useAuthStore } from "./AuthStore"
import { useCommentsStore } from "@/stores/CommentsStore.js"

export const useNewsStore = defineStore("news", {
    state: () => ({
        news: null,
        likes: [],

        modal: false,
        role: "",
        edit_post_id: null,
        filtr_news: null,

        //with comments
        newCommets: [],
    }),
    actions: {
        async getAuthStoreMethods() {
            const AuthStore = useAuthStore()
            return await AuthStore.updateAccessToken()
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

                // const CommentsStore = useCommentsStore()
                // CommentsStore.showComments()
                
                this.$patch({
                    news: this.news,
                })
            } catch (error) {
                console.error("Fetch error:", error)
            }
        },
        async addLike(id_news, user_id) {
            const jwtToken = sessionStorage.getItem("jwtToken")
            if (!jwtToken) {
                console.error("Отсутствует JWT-токен!")
                return
            }
            try {
               
                const likes = []

                this.news.forEach((news) => {
                    if (news.id === id_news && news.likes && news.likes.length !== 0) {
                        news.likes.forEach((like) => {
                        if (like.user.id === Number(user_id)) {
                            likes.push(news[id_news])
                        }
                      })
                    }
                  })

                if (likes.length!==0) {
                    const response = await fetch(
                        `http://localhost:8085/likes?newsId=${id_news}`,
                        {
                            method: "DELETE",
                            headers: new Headers({
                                Authorization: "Bearer " + jwtToken,
                                "Content-Type": "application/json",
                            }),
                        }
                    )
                    if (!response.ok) {
                        if (response.status === 401) {
                            const result = await this.getAuthStoreMethods()
                            if (result) {
                                return this.addLike(id_news, user_id)
                            } else {
                                console.log("false в update access")
                            }
                        } else {
                            console.log("какой то другой статус")
                        }
                        //alert("Удаление лайка не прошло!!")
                        throw new Error("Authentication failed")
                    }
                    console.log("удалено")
                } else {
                    const response = await fetch(
                        `http://localhost:8085/likes/save?newsId=${id_news}`,
                        {
                            method: "POST",
                            headers: new Headers({
                                Authorization: "Bearer " + jwtToken,
                                "Content-Type": "application/json",
                            }),
                        }
                    )
                  
                    if (!response.ok) {
                        if (response.status === 401) {
                            const result = await this.getAuthStoreMethods()
                            if (result) {
                                return this.addLike(id_news, user_id)
                            } else {
                                console.log("false в update access")
                            }
                        } else {
                            console.log("какой то другой статус")
                        }
                        //alert("Добавление лайка не прошло!!")
                        throw new Error("Authentication failed")
                    }
                    console.log("добавлено")
                }
                //!!!!!!!!!!!
                this.getnews()

            } catch (error) {
                console.error("Authentication error:", error)
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
            console.log(newsData)
            const jwtToken = sessionStorage.getItem("jwtToken")
            if (!jwtToken) {
                console.error("Отсутствует JWT-токен!")
                return
            }
            try {
                console.log(jwtToken)
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
                            return this.addNews(newsTitle, newsText, picture, themes)
                        } else {
                            console.log("false в update access")
                        }
                    } else {
                        console.log("какой то другой статус")
                    }
                    //alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                const responseData = await response.json()
                console.log(responseData)
                this.getnews()
                this.closeModal()
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        showModal() {
            this.modal = true
        },
        closeModal() {
            this.modal = false
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
                            // Другие необходимые заголовки
                        },
                        body: JSON.stringify(themes),
                    }
                )
                if (!response.ok) {
                    alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                const responseData = await response.json()
                // this.filtr_news = responseData
                console.log(responseData)

                this.$patch({
                    news: responseData,
                })

            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
    },
})
