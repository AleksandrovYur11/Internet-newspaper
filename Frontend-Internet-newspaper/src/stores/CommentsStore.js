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

                if (com_by_id.length !== 0) {
                    this.checkCommentsCount(news_id).then((commentsCount) => {
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
        ///////////////////////////////////////////
        async updateAccessToken() {
            const refreshToken = {
                refreshToken: sessionStorage.getItem("jwtRefreshToken"),
            }

            console.log(refreshToken)

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

                if (!response.ok) {
                    alert("Неправильный запрос!")
                    throw new Error("Authentication failed")
                }

                const responseData = await response.json()

                // sessionStorage.setItem('jwtToken', responseData.accessToken)
                // console.log(responseData.accessToken)

                console.log("Before setting token:", responseData.accessToken)
                sessionStorage.setItem("jwtToken", responseData.accessToken)
                console.log(
                    "After setting token:",
                    sessionStorage.getItem("jwtToken")
                )

                return 

                //sessionStorage.setItem('user_role', responseData.roles[0])

                //this.user = sessionStorage.getItem('user_id')
                //this.role = sessionStorage.getItem('user_role')

                // console.log(this.user)
                // console.log(this.role)

                // const jwtToken = responseData.accessToken
                // const jwtRefreshToken = responseData.jwtRefreshToken
                // sessionStorage.setItem('jwtRefreshToken', jwtRefreshToken)

                // //const name = responseData.name
                // console.log(responseData)

                // this.selectRole(responseData.roles[0])
                // console.log(responseData.roles[0])

                // sessionStorage.setItem('jwtToken', jwtToken)
            } catch (error) {
                console.error("not refresh error:", error)
            }
        },
        //////////////////////////////////////////
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
                // const resp = response.json()
                // if (resp.message === "Full authentication is required to access this resource") {
                //     console.log(resp)
                //     console.error("Access token() error:", error)
                // }

                if (!response.ok) {
                    if (response.status === 401) {
                        // alert("Access token истек")
                        // await this.updateAccessToken()
                        await this.updateAccessToken()
                        //console.log(newAccessToken)
                        //sessionStorage.setItem("jwtToken", newAccessToken)
                        return this.sendcomment(id_news, new_comment)
                    } else {
                        throw new Error("Ошибка при запросе") // Обрабатываем другие ошибки
                    }
                }
                //омжет быть можно брать сразу из сессион сторедж)))
                //const jwtRefreshToken = sessionStorage.getItem('jwtRefreshToken')

                //метод для получения акссессс токена через рефреш

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
                        alert("Удаление не прошло")
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
