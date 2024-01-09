<script setup>
import { defineProps } from "vue"
import { ref, onMounted } from "vue"

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

import { useAuthStore } from "@/stores/AuthStore.js"
const AuthUser = useAuthStore()

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import { useCommentsStore } from "@/stores/CommentsStore.js"
const CommentsStore = useCommentsStore()

import { useFormsStore } from "@/stores/FormsStore.js"
const FormStore = useFormsStore()

import CommentBlock from "@/components/CommentBlock.vue"

const user_id = ref(sessionStorage.getItem("user_id"))
const user_role = ref(sessionStorage.getItem("user_role"))

const formatPublishedDate = (dateString) => {
  const date = new Date(dateString);
  const formatter = new Intl.DateTimeFormat('ru', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  })

  return `Опубликовано ${formatter.format(date).replace(/\./g, ':')}`
}

</script>

<template>
    <div class="cont">
        <b-button
            style="margin-right: 10px"
            v-if="user_role === 'ROLE_ADMIN'"
            @click="FormStore.showEdit(post.id)"
            :post="post"
            >red</b-button
        >
        <div class="news_container">
            <div
                style="
                    display: flex;
                    flex-direction: row;
                    justify-content: space-between;
                "
            >
                <span>
                    {{ formatPublishedDate(post.datePublishedNews) }}
                </span>

                <div class="themes">
                    <span
                        class="theme"
                        v-for="(theme, index) in post.themes"
                        :key="index"
                    >
                        # {{ theme.name }}
                    </span>
                </div>
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
            <div class="likes_info">
                <div class="likes">
                    <span @click="NewsStore.addLike(post.id, user_id)">
                        <img
                            src="@/assets/class.svg"
                            alt=""
                        />
                    </span>
                    <span
                        ><b>{{ post.likes.length || 0 }}</b></span
                    >
                </div>
                <a
                    href=""
                    @click.prevent="CommentsStore.showComments(post.id, 1)"
                    v-if="
                        !CommentsStore.checkCommentsToggle(post.id) ||
                        CommentsStore.checkCommentsToggle(post.id) === undefined
                    "
                >
                    Комментарии</a
                >
                <a
                    href=""
                    v-else
                    @click.prevent="CommentsStore.closeComments(post.id)"
                >
                    Скрыть</a
                >
            </div>
            <comment-block
                :post="post"
                v-if="CommentsStore.checkCommentsToggle(post.id)"
            ></comment-block>
        </div>
    </div>
</template>

<style scoped>
.cont {
    display: flex;
    flex-direction: row;
    margin-bottom: 30px;
    margin-top: 30px;
    align-items: flex-start;
}

.likes {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
}
.likes_info {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
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
    display: flex;
    /* align-items: left; */
    flex-direction: column;
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
