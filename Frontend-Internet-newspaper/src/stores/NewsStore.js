import { defineStore } from "pinia"

export const useNewsStore = defineStore("news", {
    state: () => ({
        news: [],
        likes: [],
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

                 //////////111111111111////////////
        
                sessionStorage.setItem('news',  JSON.stringify(responseData))
                this.news = JSON.parse(sessionStorage.getItem('news'));
                 //////////111111111111////////////
                // console.log(JSON.parse(localStorage.getItem('news')))
                // responseData.forEach((item) => {
                //     this.news.push(item)
                // })
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
                // const responseData = await response.json()
                // console.log(responseData)
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async deleteComment(id_comment) {
            const jwtToken = sessionStorage.getItem("jwtToken")
            console.log(jwtToken)

            if (!jwtToken) {
                console.error("Отсутствует JWT-токен!")
                return
            } else {
                console.log(jwtToken)
            }
            try {
                const response = await fetch(
                    `http://localhost:8085/comment/user/${id_comment}`,
                    {
                        method: "DELETE",
                        headers: new Headers({
                            Authorization: "Bearer " + jwtToken,
                            "Content-Type": "application/json",
                        }),
                        // body: JSON.stringify(id_com),
                    }
                )
                if (!response.ok) {
                    alert("Удаление не прошло")
                    throw new Error("Authentication failed")
                }
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

                // const responseData = await response.json()
                // console.log(responseData)
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        // addNews(){

        // }
    },
})
