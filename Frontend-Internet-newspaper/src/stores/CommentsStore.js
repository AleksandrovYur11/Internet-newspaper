import { defineStore } from "pinia"
import { useAuthStore } from "@/stores/AuthStore.js"

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
        async getAuthStoreMethods() {
            const AuthStore = useAuthStore()
            return await AuthStore.updateAccessToken()
        },

        async checkCommentsCount(id_news) { // сколько комментариев хранится на сервере для конкретного поста
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
                console.error("Check comments error:", error)
            }
        },

        closeComments(post_id) {
            this.commentsCountInfo
                .filter((item) => item.post_id === post_id)
                .map((item) => (item.toggleShow = false))
        },

        getCommentsInfo(post_id) {
            return this.commentsCountInfo
                .filter((item) => item.post_id === post_id)
                .map((item) => item.show)[0]
        },

        checkCommentsToggle(post_id) {
            return this.commentsCountInfo
                .filter((item) => item.post_id === post_id)
                .map((item) => item.toggleShow)[0]
        },

        async showComments(news_id) {
            const commentsCount = await this.checkCommentsCount(news_id) // кол-во комментариев поста на сервере

            // информация о комментариях поста для тоглов
            const commentInfo = {
                post_id: news_id,
                commentsCount: commentsCount,
                existsComments: 0,
                show: false, // кнопка "eще"
                toggleShow: true, // закрыть/открыть комментарии
            }

            for (let i = 0; i < 3; i++) {
                // выводим по три комментария за один запрос
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

                    const com_id_news = {
                        // содержание комментария
                        news_id: news_id,
                        comments: responseData,
                        showed: false,
                    }

                    const isExistsComment = this.comments.find(
                        //проверяем уже выведенные ком-ии
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
                        commentInfo.existsComments = 3 // для поддержания системы вывода ком-ев по тройкам
                    }
                } catch (error) {
                    console.error("Show comments error:", error)
                }
            }

            // для переключателя открытия/закрытия ком-ев
            const index = this.commentsCountInfo.findIndex(
                (item) => item.post_id === news_id
            )

            if (index !== -1) {
                this.commentsCountInfo[index] = {
                    ...this.commentsCountInfo[index],
                    existsComments: commentInfo.existsComments,
                    show:
                        commentInfo.existsComments < commentInfo.commentsCount,
                    toggleShow: true,
                }
            } else {
                commentInfo.show =
                    commentInfo.existsComments < commentInfo.commentsCount // отображение кнопки еще зависит от кол-ва существующих ком-ев
                this.commentsCountInfo.push(commentInfo)
            }
            this.$patch({
                commentsCountInfo: this.commentsCountInfo,
            })
        },

        async sendcomment(id_news, new_comment) {
            const textComment = {
                textComment: new_comment,
            }
            const jwtToken = sessionStorage.getItem("jwtToken")
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
                            console.log("Failed to update access token")
                        }
                    }
                    throw new Error("Access token update required")
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
            } catch (error) {
                console.error("Send comment error:", error)
            }
        },

        async deleteComment(id_comment) {
            const role = sessionStorage.getItem("user_role")
            const jwtToken = sessionStorage.getItem("jwtToken")
            const url_ = "http://localhost:8085/comment/"

            try {
                const url =
                    role === "ROLE_ADMIN"
                        ? `${url_}admin/${id_comment}`
                        : `${url_}user/${id_comment}`

                const response = await fetch(url, {
                    method: "DELETE",
                    headers: new Headers({
                        Authorization: "Bearer " + jwtToken,
                        "Content-Type": "application/json",
                    }),
                })

                if (!response.ok) {
                    if (response.status === 401) {
                        const result = await this.getAuthStoreMethods()
                        if (result) {
                            return this.deleteComment(id_comment)
                        } else {
                            console.log("false в update access")
                        }
                    } else {
                        console.log("Failed to update access token")
                    }
                    throw new Error("Access token update required")
                }

                const updatedComments = this.comments.map((com) => ({
                    news_id: com.news_id,
                    comments: com.comments.filter((comment) => comment.id !== id_comment),
                }))

                this.$patch({
                    comments: updatedComments,
                })
            } catch (error) {
                console.error("Delete comment error:", error)
            }
        },
    },
})
