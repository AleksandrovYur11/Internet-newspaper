<script setup>
import { defineProps } from "vue"
import { ref } from "vue"

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

const collapseItems = ref([2, 3, 4])

import { useAuthStore } from "@/stores/AuthStore.js"
const AuthUser = useAuthStore()

const new_comment = ref("")

const showComments = ref(false)

const sendcomment = async (id_news) => {
    if (!new_comment.value.trim()) {
        console.error("Комментарий пуст!")
        return
    }
    const textComment = {
        textComment: new_comment.value,
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
}

</script>

<template>
    <div class="news_container">
        <div class="themes">
            <span
                class="theme"
                v-for="(theme, index) in post.themes"
                :key="index"
            >
                # {{ theme.name }}
            </span>
        </div>
        <h3>{{ post.newsTitle }}</h3>
        <div class="post_body">
            <img
                :src="post.picture.url"
                style="margin-right: 30px"
            />
            <p>{{ post.newsText }}</p>
        </div>
        <br />
        <div class="footer">
            <a
                href=""
                @click.prevent="showComments = !showComments"
                
            >
                Комментарии</a
            >
            <div class="comments_content"  v-show="showComments">
                <div
                    class="comment_header"
                >
                    <div class="likes_info">
                        <img
                            src="@/assets/default_like.png"
                            style="margin-right: 10px"
                        />
                        <span style="font-weight: bold">1200</span>
                    </div>
                    <div class="comments_area">
                        <b-form-textarea
                            id="textarea-small"
                            size="sm"
                            placeholder="Ваш комментарий..."
                            v-model="new_comment"
                            @keyup.enter="sendcomment(post.id)"
                        ></b-form-textarea>
                    </div>
                    <b-button
                        @click="sendcomment(post.id)"
                        style="border-radius: 50%; width: 50px; height: 50px"
                    >
                        <svg
                            class="w-4 stroke-current text-indigo-500 hover:text-indigo-700"
                            xmlns="http://www.w3.org/2000/svg"
                            width="24"
                            height="24"
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="2"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                        >
                            <line
                                x1="22"
                                y1="2"
                                x2="11"
                                y2="13"
                            ></line>
                            <polygon
                                points="22 2 15 22 11 13 2 9 22 2"
                            ></polygon>
                        </svg>
                    </b-button>
                </div>
                <!-- thread -->
                <div
                    v-for="(item, index) in post.comments"
                    :key="index"
                    class="text-gray-700 py-4 pb-0 rounded"
                >
                    <!-- parent comment -->
                    <!-- class="-mx-4 px-4 bg-gray-100" -->
                    <b-card class = "card-body_">
                        <div class="comment_info">
                            <span class="text-gray-500 uppercase text-xs"
                            ><b>noNam</b>e</span
                        >
                        <span class="text-gray-500 uppercase text-xs"
                            >01.01.2001</span
                        >
                        </div>
                        <p>{{ item.textComment }}</p>
                       
                            <button @click.prevent="setReplyingTo(comment)">
                                <svg
                                    class="stroke-current text-gray-500 w-4"
                                    xmlns="http://www.w3.org/2000/svg"
                                    width="24"
                                    height="24"
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="2"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                >
                                    <path
                                        d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"
                                    ></path>
                                </svg>
                            </button>
                        
                    </b-card >
                    <div>
                        <div class="text-gray-700 rounded">
                            <span class="text-gray-500 uppercase text-xs">{{
                                index
                            }}</span>
                            <p>{{ index }}</p>
                            <div>
                                <button @click.prevent="setReplyingTo(comment)">
                                    <svg
                                        class="stroke-current text-gray-500 w-4"
                                        xmlns="http://www.w3.org/2000/svg"
                                        width="24"
                                        height="24"
                                        viewBox="0 0 24 24"
                                        fill="none"
                                        stroke="currentColor"
                                        stroke-width="2"
                                        stroke-linecap="round"
                                        stroke-linejoin="round"
                                    >
                                        <path
                                            d="M21 11.5a8.38 8.38 0 0 1-.9 3.8 8.5 8.5 0 0 1-7.6 4.7 8.38 8.38 0 0 1-3.8-.9L3 21l1.9-5.7a8.38 8.38 0 0 1-.9-3.8 8.5 8.5 0 0 1 4.7-7.6 8.38 8.38 0 0 1 3.8-.9h.5a8.48 8.48 0 0 1 8 8v.5z"
                                        ></path>
                                    </svg>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>

/* div.card-body{
    display: flex !important;
    min-height: 1px;
    padding: 1.25rem;
    flex-direction: row;
} */

/* div .card-body_ .card_body{
    display: flex !important;
    flex-direction: row;
    min-height: 1px;
    padding: 1.25rem;
} */

.comment_info{
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
.comments_content {
    display: flex;
    flex-direction: column;
}
.comments_area {
    display: flex;
    flex-direction: column;
    width: 350px;
}

img[data-v-9d1a3feb] {
    margin-right: 0px;
}

.footer {
    background-color: #fff;
}

.likes_info {
    display: flex;
    flex-direction: row;
    align-items: center;
}
.input_card {
    display: flex;
    flex-direction: row;
}
.comment_header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    background-color: #fff;
    align-items: flex-start;
}
.news_container {
    background-color: #fff;
    color: #000;
    border-radius: 15px;
    padding: 20px 20px;
    /* margin: 30px; */
    margin-bottom: 30px;
    margin-top: 30px;
    display: flex;
    align-items: left;
}

img {
    /* height: 300px; */
    max-width: 200px;
    margin-right: 25px;
}

h3 {
    margin-top: 10px;
}

.themes {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
}
.theme {
    background-color: rgba(25, 189, 63, 0.545);
    padding: 4px 8px;
    border-radius: 15px;
    font-size: 12px;
    margin-right: 5px;
}

.comment_btn {
    padding: 4px 8px;
    border-radius: 15px;
    font-size: 12px;
}

.post_body {
    display: flex;
    align-items: flex-start;
    margin-top: 20px;
}

p {
    flex: 1;
}
</style>
