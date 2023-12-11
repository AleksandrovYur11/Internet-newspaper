import { defineStore } from "pinia"
import { useNewsStore } from "@/stores/NewsStore.js"

export const useCommentsStore = defineStore("comments", {
    state: () => ({
        comments: [],
        news: null,
        commentsCount: null,
        showed: false,
    }),
    actions: {
        getExistNews() {
            const NewsStore = useNewsStore()
            return NewsStore
            //this.news.push(existNews)
        },
        getCommetsByNewsId(news_id) {
            // ну типо должно заполняться , но почему то пустое - почему?
            const com = this.comments.filter((obj) => obj.news_id === news_id)
            console.log(com)
            return com
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
                        // body: JSON.stringify(textComment),
                    }
                )
                if (!response.ok) {
                    alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                const responseData = await response.json()
                this.commentsCount = responseData.countComment
                console.log(responseData.countComment)
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async showComments(news_id, num) {
            try {
                if (num === 1) {
                    this.showed = !this.showed
                }
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

                // Добавляем новые комментарии к текущим комментариям в хранилище
                this.comments = [...this.comments, com_id_news]

                console.log(this.comments)

                // Сохраняем состояние хранилища
                this.$patch({
                    comments: this.comments,
                })
            } catch (error) {
                console.error("Fetch error:", error)
            }
        },
        // async showComments(news_id, num) {
        //     if (num === 1) {
        //         this.showed = !this.showed
        //     }
        //     try {
        //         const response = await fetch(
        //             `http://localhost:8085/comment/show?newsId=${news_id}`,
        //             {
        //                 method: "GET",
        //                 headers: {
        //                     "Content-Type": "application/json",
        //                 },
        //             }
        //         )
        //         if (!response.ok) {
        //             throw new Error("Failed to load comments")
        //         }
        //         const responseData = await response.json()
        //         console.log("Response data:", responseData)

        //         const com_id_news = {
        //             news_id: news_id,
        //             comments: responseData
        //         }

        //         //сделай так лучше везде
        //        this.comments.push(com_id_news)

        //        //тут по идеи массив не стирается , а дополняется)))
        //        //console.log(com_id_news)
        //         //this.comments.push(...com_id_news)

        //         console.log(this.comments)
        //         // const NewsStore = useNewsStore()
        //         // NewsStore.getnews()

        //         //почему он для всех все открывает((()))
        //         this.checkCommentsCount(news_id)

        //         this.$persist();

        //     } catch (error) {
        //         console.error("Fetch error:", error)
        //     }
        // },
        returnCommentsById(news_id) {
            const com = this.comments.filter((obj) => obj.news_id === news_id)
            console.log(com)
            return com.comments
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

                //console.log()
                // NewsStore.getnews()
                //const NewsStore = useNewsStore()
                //NewsStore.getnews()

                // this.checkCommentsCount(id_news)
                //if (this.commentsCount <= 3) {
                //this.comments = []

                //!!!!!!!!!!!!!!!!!!!!!!!!!!!//
                //this.showComments(id_news)

                //}

                const responseData = await response.json()
                // console.log(responseData)

                // !!!!!!!!
                const updatedComments = this.comments.find(
                    (item) => item.news_id === id_news
                )
                if (updatedComments) {
                    updatedComments.comments.push(responseData)
                } else {
                    this.comments.push({
                        news_id: id_news,
                        comments: [responseData],
                    })
                }

                // Обновляем состояние хранилища
                this.$patch({
                    comments: this.comments,
                })
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
    },
})
