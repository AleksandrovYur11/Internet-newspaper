import { defineStore } from "pinia"
import { useNewsStore } from "@/stores/NewsStore.js"
import { useAuthStore } from "@/stores/AuthStore.js"
import router from "@/router/index.js"

export const useCommentsStore = defineStore("comments", {
    state: () => ({
        comments: [],
        news: null,
        commentsCount: 0,
        showed: false,

        commentsCountInfo: [],
        toggle: false,
    }),
    actions: {
        closeComments(post_id) {
            this.commentsCountInfo
                .filter((item) => item.post_id === post_id)
                .map((item) => item.toggleShow = false)
        },
        getCommentsInfo(post_id) {
            console.log('000')
            console.log(this.commentsCountInfo
                .filter((item) => item.post_id === post_id)
                .map((item) => item.show)[0])
            return this.commentsCountInfo
                .filter((item) => item.post_id === post_id)
                .map((item) => item.show)[0]
        },
        checkCommentsToggle(post_id){
            console.log(this.commentsCountInfo
                .filter((item) => item.post_id === post_id)
                .map((item) => item.toggleShow)[0])
            return  this.commentsCountInfo
                .filter((item) => item.post_id === post_id)
                .map((item) => item.toggleShow)[0]
        },
        async getAuthStoreMethods() {
            const AuthStore = useAuthStore()
            return await AuthStore.updateAccessToken()
        },
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
                    throw new Error("Колличество комментариев не получено")
                }

                const responseData = await response.json()
                this.commentsCount = responseData.countComment
                return responseData.countComment
            } catch (error) {
                console.error("Request error:", error)
            }
        },
        async showComments(news_id, num) {
            const commentsCount = await this.checkCommentsCount(news_id) // кол-во комментариев для поста в бд

            // информация о комментариях поста для тоглов
            const commentInfo = {
                post_id: news_id,
                commentsCount: commentsCount,
                existsComments: 0,
                show: false,
                toggleShow: true
            }

            for (let i = 0; i < 3; i++) {
                try {
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
                    console.log(responseData)

                    const com_id_news = {
                        news_id: news_id,
                        comments: responseData,
                        showed: false,
                    }

                    const isExistsComment = this.comments.find(
                        // проверяем существование выведенных комментариев для поста
                        (item) => item.news_id === news_id
                    )

                    if (isExistsComment) {
                        if (isExistsComment.comments.length < commentsCount) {
                            isExistsComment.comments.push(
                                ...com_id_news.comments
                            )
                        }
                        commentInfo.existsComments =
                            isExistsComment.comments.length
                    } else {
                        this.comments.push(com_id_news)
                        commentInfo.existsComments = 3
                    }

                    this.$patch({
                        comments: this.comments,
                    })
                } catch (error) {
                    console.error("Fetch error:", error)
                }
            }
            
            // для тогла

            const index = this.commentsCountInfo.findIndex(
                (item) => item.post_id === news_id
            )
        
            if (index !== -1) {
                this.commentsCountInfo[index] = {
                    ...this.commentsCountInfo[index],
                    existsComments: commentInfo.existsComments,
                    show: commentInfo.existsComments < commentInfo.commentsCount,
                    toggleShow: true 
                }
            } else {
                commentInfo.show = commentInfo.existsComments < commentInfo.commentsCount
                this.commentsCountInfo.push(commentInfo)
            }
            // this.$patch({
            //     commentsCountInfo: this.commentsCountInfo,
            // })
            console.log(this.commentsCountInfo)
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
                    if (response.status === 401) {
                        const result = await this.getAuthStoreMethods()
                        if (result) {
                            return this.sendcomment(id_news, new_comment)
                        } else {
                            console.log("false в update access")
                        }
                    } else {
                        console.log("какой то другой статус")
                    }
                }

                ////////////////
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
                // console.error("Authentication error:", error)
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
                        console.log("Комментарий успешно удален")
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
                        if (response.status === 401) {
                            const result = await this.getAuthStoreMethods()
                            if (result) {
                                return this.deleteComment(id_comment)
                            } else {
                                console.log("false в update access")
                            }
                        } else {
                            console.log("какой то другой статус")
                        }

                        //alert("Удаление не прошло")
                        throw new Error("Authentication failed")
                    }

                    if (response.status === 204) {
                        console.log("Комментарий успешно удален")
                    }
                }

                const updatedComments = this.comments.map((com) => {
                    const filtrComments = com.comments.filter(
                        (comment) => comment.id !== id_comment
                    )
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
