import { defineStore } from "pinia"
import { useNewsStore } from "@/stores/NewsStore.js"

export const useCommentsStore = defineStore("comments", {
    state: () => ({
        comments: [],
        news: null,
        commentsCount: 0,
        showed: true,
    }),
    actions: {
        async checkCommentsCount(id_news) {
            try {
                const response = await fetch(
                    `http://localhost:8085/comment/check-db?newsId=${id_news}`,
                    {
                        method: "POST",
                        headers: new Headers({
                            "Content-Type": "application/json",
                        }),
                    }
                )
                if (!response.ok) {
                    alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                const responseData = await response.json()
                this.commentsCount = responseData.countComment
                return responseData.countComment
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async showComments(news_id, num) {
            try {

                // this.showed[news_id] = true

                //const showed_post_comments = false
                //this.showed[news_id] = showed_post_comments
                //выставить всем false нафиг()
                //this.showed[news_id] =  !this.showed[news_id]

                const response = await fetch(
                    `http://localhost:8085/comment/show?newsId=${news_id}`,
                    {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json",
                        },
                    }
                )
                if (!response.ok) {
                    throw new Error("Failed to load comments")
                }

                const responseData = await response.json()

                const com_id_news = {
                    news_id: news_id,
                    comments: responseData,
                }
                const com_by_id = []

                for (const key in this.comments) {
                    if (this.comments.hasOwnProperty(key)) {
                        const obj = this.comments[key]

                        if (obj.news_id === news_id) {
                            com_by_id.push(...obj.comments)
                        }
                    }
                }

                if (com_by_id.length !== 0 ) {
                    this.checkCommentsCount(news_id)
                        .then((commentsCount) => {
                            console.log(commentsCount)
                            if (com_by_id.length < commentsCount) {
                                this.comments = [...this.comments, com_id_news]
                            }
                        })
                } else {
                    this.comments = [...this.comments, com_id_news]
                }

                // Сохраняем состояние хранилища
                this.$patch({
                    comments: this.comments,
                })
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
                    `http://localhost:8085/comment/save?newsId=${id_news}`,
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

                const responseData = await response.json()

                const updatedComments = this.comments.find(
                    (item) => item.news_id === id_news
                )
                if (updatedComments) {
                    updatedComments.comments.unshift(responseData)
                } else {
                    this.comments.unshift({
                        news_id: id_news,
                        comments: [responseData],
                    })
                }

                this.$patch({
                    comments: this.comments,
                })
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async deleteComment(id_comment) {
            const role = sessionStorage.getItem("user_role")
            const jwtToken = sessionStorage.getItem("jwtToken")
            
            if (!jwtToken) {
                console.error("Отсутствует JWT-токен!")
                return
            } else {
                console.log(jwtToken)
            }
            try {
                if (role == "ROLE_ADMIN") {
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

                    if (response.status === 204) {
                        console.log("Комментарий успешно удален");
                    }

                
                } else if (role == "ROLE_USER") {
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

                    if (response.status === 204) {
                        console.log("Комментарий успешно удален");
                    }
                }

                const updatedComments = this.comments.map(com => {
                    const filtrComments = com.comments.filter(comment => comment.id !== id_comment)
                    return { news_id: com.news_id, comments: filtrComments }
                })

                console.log(updatedComments)

                this.$patch({
                    comments: updatedComments,
                })

            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
    },
})
