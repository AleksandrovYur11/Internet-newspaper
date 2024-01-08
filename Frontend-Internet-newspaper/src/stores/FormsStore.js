import { defineStore } from "pinia"
import { useNewsStore } from "@/stores/NewsStore.js"
import { useAuthStore } from "@/stores/AuthStore.js"

export const useFormsStore = defineStore("forms", {
    state: () => ({
        news: null,
        edit: false,
        modal: false,
        news_for_edit: null,
        edit_post_id: null,
    }),
    actions: {
        async getAuthStoreMethods() {
            const AuthStore = useAuthStore()
            return await AuthStore.updateAccessToken()
        },
        async getnews() {
            const NewsStore = useNewsStore()
            return await NewsStore.getnews()
        },
        async deleteNews(news_id) {
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
                        method: "DELETE",
                        headers: new Headers({
                            Authorization: "Bearer " + jwtToken,
                            "Content-Type": "application/json",
                        }),
                    }
                )
                console.log(response)
                if (!response.ok) {
                    if (response.status === 401) {
                        const result = await this.getAuthStoreMethods()
                        if (result) {
                            return this.deleteNews(news_id)
                        } else {
                            console.log("false в update access")
                        }
                    } else {
                        console.log("какой то другой статус")
                    }
                    //alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                this.closeEdit()
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async getInfoNews(news_id) {
            console.log(news_id)
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
                console.log(response)
                if (!response.ok) {
                    if (response.status === 401) {
                        const result = await this.getAuthStoreMethods()
                        if (result) {
                            return this.getInfoNews(news_id)
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
                this.news_for_edit = responseData
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        async updateNews(newsTitle, newsText, picture, themes, news_id) {
            console.log(news_id)
            const picture_url_obj = {
                url: picture,
            }
            const newsData = {
                newsTitle: newsTitle,
                newsText: newsText,
                picture: picture_url_obj,
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
                    if (response.status === 401) {
                        const result = await this.getAuthStoreMethods()
                        if (result) {
                            return this.updateNews(newsTitle, newsText, picture, themes, news_id)
                        } else {
                            console.log("false в update access")
                        }
                    } else {
                        console.log("какой то другой статус")
                    }
                    //alert("Неправильный вход!")
                    throw new Error("Authentication failed")
                }
                this.closeEdit()
                
                //const responseData = await response.json()
                //console.log(responseData)
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        showEdit(post_id) {
            this.edit_post_id = post_id
            this.edit = true
        },
        closeEdit() {
            this.edit = false
            this.getnews()
        },
        formsValidation(newsTitle, newsText, picture, themes, formType, news_id) {
            const urlRegex = /\.(jpeg|jpg|gif|png|bmp|svg)$/i
            const maxTextLength = 255
            console.log(themes)
            const themeInvalidLength = themes
                .split(",")
                .some((theme) => theme.trim().length > maxTextLength)
            if (
                newsTitle.length === 0 ||
                newsText.length === 0 ||
                picture.length === 0 ||
                themes.length === 0
            ) {
                alert("Заполните все поля!")
            } else if (newsTitle.length > maxTextLength) {
                alert("Название не должно превышать 255 символов")
            } else if (themeInvalidLength) {
                alert("Одна тема не должна превышать 255 символов")
            } else if (!urlRegex.test(picture)) {
                console.log(picture)
                console.log(urlRegex.test(picture))
                alert("Некорректный URL картинки")
            } else {
                console.log(formType)
                if (formType === 'modal') {
                    const NewsStore = useNewsStore()
                    NewsStore.addNews(
                    newsTitle,
                    newsText,
                    picture,
                    themes
                ) 
                } else if (formType === 'edit') {
                   this.updateNews(newsTitle, newsText, picture, themes, news_id) 
                }
            }
            //     //return true
            //      // newsTitle.value = ""
            //     // newsText.value = ""
            //     // picture.value = ""
            //     // themes.value = ""
        },
    },
})
