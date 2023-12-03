import { defineStore } from "pinia"
import { useAuthStore } from "./AuthStore"

export const useNewsStore = defineStore("news", {
    state: () => ({
        news: null,
        likes: [],
        edit: false,
        modal: false,
        news_for_edit: [],
        role: ''
    }),
    actions: {
        async getnews() {
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

                console.log("Response data:", responseData)

            } catch (error) {
                console.error("Fetch error:", error)
            }
        },
        async sendcomment(id_news, new_comment) {
            if (!new_comment.trim()) {
                console.error("Комментарий пуст!")
                alert("Комментарий пуст!")
                return
            }
            const textComment = {
                textComment: new_comment,
            }
            const jwtToken = sessionStorage.getItem("jwtToken")
            if (!jwtToken) {
                console.error("Отсутствует JWT-токен!")
                return
            }
            try {
                console.log(jwtToken)
                const response = await fetch(
                    `http://localhost:8085/comment/${id_news}`,
                    {
                        method: "POST",
                        headers: new Headers({
                            Authorization: "Bearer " + jwtToken,
                            "Content-Type": "application/json",
                        }),
                        body: JSON.stringify(textComment),
                    }
                )
                if (!response.ok) {
                    alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                this.getnews()
                // const responseData = await response.json()
                // console.log(responseData)
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async deleteComment(id_comment) {
            const role = sessionStorage.getItem("user_role")
            const jwtToken = sessionStorage.getItem("jwtToken")
            console.log(jwtToken)

            if (!jwtToken) {
                console.error("Отсутствует JWT-токен!")
                return
            } else {
                console.log(jwtToken)
            }
            try {
                if (role == 'ROLE_ADMIN') {
                   const response = await fetch(
                    `http://localhost:8085/comment/admin/${id_comment}`,
                    {
                        method: "DELETE",
                        headers: new Headers({
                            Authorization: "Bearer " + jwtToken,
                            "Content-Type": "application/json",
                        }),
                    }
                )
                if (!response.ok) {
                    alert("Удаление не прошло")
                    throw new Error("Authentication failed")
                } 
                } else if (role == 'ROLE_USER') {
                    const response = await fetch(
                        `http://localhost:8085/comment/user/${id_comment}`,
                        {
                            method: "DELETE",
                            headers: new Headers({
                                Authorization: "Bearer " + jwtToken,
                                "Content-Type": "application/json",
                            }),
                        }
                    )
                    if (!response.ok) {
                        alert("Удаление не прошло")
                        throw new Error("Authentication failed")
                    } 
                }
                this.getnews()
                // const responseData = await response.json()
                // console.log(responseData)
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async addLike(id_news, user_id) {
            // const like = {
            //     post_id: id_news,
            //     likes_count: news.find(post => post.id === id_news).likes.length || 0,
            // }
            const jwtToken = sessionStorage.getItem("jwtToken")
            if (!jwtToken) {
                console.error("Отсутствует JWT-токен!")
                return
            }
            try {
                console.log(jwtToken)
                console.log(this.news)
                const hasNewsWithCommentByUser = this.news.some((item) => {
                    if (item.likes.length !== 0) {
                        console.log(
                            item.likes.some((like) => like.user.id == user_id)
                        )
                        console.log(item.likes[0].user.id)
                        console.log(user_id)
                        return (
                            item.id === id_news &&
                            item.likes.some((like) => like.user.id == user_id)
                        )
                    } else {
                        return false
                    }
                })

                if (hasNewsWithCommentByUser) {
                    const response = await fetch(
                        `http://localhost:8085/likes/${id_news}`,
                        {
                            method: "DELETE",
                            headers: new Headers({
                                Authorization: "Bearer " + jwtToken,
                                "Content-Type": "application/json",
                            }),
                        }
                    )
                    if (!response.ok) {
                        alert("Удаление лайка не прошло!!")
                        throw new Error("Authentication failed")
                    }
                    console.log("удалено")
                } else {
                    const response = await fetch(
                        `http://localhost:8085/likes/${id_news}`,
                        {
                            method: "POST",
                            headers: new Headers({
                                Authorization: "Bearer " + jwtToken,
                                "Content-Type": "application/json",
                            }),
                        }
                    )
                    if (!response.ok) {
                        alert("Добавление лайка не прошло!!")
                        throw new Error("Authentication failed")
                    }
                    console.log("добавлено")
                }
                this.getnews()
                // const responseData = await response.json()
                // console.log(responseData)
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async addNews(newsTitle, newsText, picture, themes) {
            const picture_url_obg = {
                url: picture
            }
            const newsData = {
                newsTitle: newsTitle,
                newsText: newsText,
                picture: picture_url_obg,
                themes: themes.split(',').map(theme => ({ name: theme.trim() }))
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
                    `http://localhost:8085/news/create`,
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
                    alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                const responseData = await response.json()
                console.log(responseData)
                this.getnews()
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async getInfoNews(news_id) {
            this.showEdit()
            const jwtToken = sessionStorage.getItem("jwtToken")
            if (!jwtToken) {
                console.error("Отсутствует JWT-токен!")
                return
            }
            try {
                console.log(jwtToken)
                const response = await fetch(
                    `http://localhost:8085/news/${news_id}`,
                    {
                        method: "GET",
                        headers: new Headers({
                            Authorization: "Bearer " + jwtToken,
                            "Content-Type": "application/json",
                        }),
                    }
                )
                if (!response.ok) {
                    alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                const responseData = await response.json()
                // this.news_for_edit.push(responseData)
                sessionStorage.setItem('news_for_edit', JSON.stringify(responseData))
                console.log(responseData)
                this.getnews()
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async updateNews(newsTitle, newsText, picture, themes,  news_id) {
            console.log(news_id)
            const picture_url_obj = {
                url: picture
            }
            const newsData = {
                newsTitle: newsTitle,
                newsText: newsText,
                picture: picture_url_obj,
                themes: themes.split(',').map(theme => ({ name: theme.trim() }))
                //themes: []
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
                    `http://localhost:8085/news/${news_id}`,
                    {
                        method: "PUT",
                        headers: new Headers({
                            Authorization: "Bearer " + jwtToken,
                            "Content-Type": "application/json",
                        }),
                        body: JSON.stringify(newsData),
                    }
                )
                if (!response.ok) {
                    alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                this.getnews()
                // this.getnews()
                //const responseData = await response.json()
                //console.log(responseData)
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        showEdit(){
            this.edit = true
        },
        closeEdit() {
            this.edit = false
        },
        showModal(){
            this.modal = true
        },
        closeModal(){
            this.modal = false
        },
    },
})
