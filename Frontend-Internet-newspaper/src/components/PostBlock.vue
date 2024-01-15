<script setup>
import { ref, defineProps } from "vue"

import CommentBlock from "@/components/CommentBlock.vue"
import errorFile from "@/assets/error_file.png"

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import { useCommentsStore } from "@/stores/CommentsStore.js"
const CommentsStore = useCommentsStore()

import { useFormsStore } from "@/stores/FormsStore.js"
const FormStore = useFormsStore()

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

const user_id = ref(sessionStorage.getItem("user_id"))
const user_role = ref(sessionStorage.getItem("user_role"))

const formatPublishedDate = (dateString) => {
    const date = new Date(dateString)
    const formatter = new Intl.DateTimeFormat("ru", {
        year: "numeric",
        month: "2-digit",
        day: "2-digit",
        hour: "2-digit",
        minute: "2-digit",
    })
    return `Опубликовано ${formatter.format(date)}`
}

const errorImage = (event) => {
    event.target.src = errorFile
}

const openFiltr = (theme_name) => {
    // добавить открытие фильтр формы
    NewsStore.filterThemes(theme_name, "")
    NewsStore.setPositiveTheme(theme_name)
}
</script>

<template>
    <div class="block_container">
        <div class="post_container">
            <span style="color: #007bff">
                {{ formatPublishedDate(post.datePublishedNews) }}
            </span>
            <h3>{{ post.newsTitle }}</h3>
            <div class="themes">
                <span
                    class="theme"
                    v-for="(theme, index) in post.themes"
                    :key="index"
                    @click="openFiltr(theme.name)"
                >
                    #{{ theme.name }}
                </span>
            </div>
            <div class="post_body">
                <img
                    :src="post.picture.url"
                    @error="errorImage"
                    style="margin-right: 30px"
                />
                <p>&nbsp;&nbsp;&nbsp;&nbsp;{{ post.newsText }}</p>
            </div>
            <br />
            <div class="likes_info">
                <span class="likes_count">
                    <b>{{ post.likes.length || "" }}</b>
                </span>
                <span
                    @click.prevent="NewsStore.addLike(post.id, user_id)"
                    v-bind:class="
                        NewsStore.isLiked(post.id, user_id)
                            ? 'icon dislike'
                            : 'icon like'
                    "
                ></span>
                <span
                    @click.prevent="CommentsStore.showComments(post.id, 1)"
                    v-if="
                        !CommentsStore.checkCommentsToggle(post.id) ||
                        CommentsStore.checkCommentsToggle(post.id) === undefined
                    "
                    class="icon comments"
                >
                </span>
                <span
                    class="icon close"
                    v-else
                    @click.prevent="CommentsStore.closeComments(post.id)"
                ></span>
            </div>
            <CommentBlock
                :post="post"
                v-if="CommentsStore.checkCommentsToggle(post.id)"
            />
        </div>
        <span
            class="edit_btn icon"
            v-if="user_role === 'ROLE_ADMIN'"
            @click="FormStore.showEdit(post.id)"
            :post="post"
        ></span>
    </div>
</template>

<style scoped>
.likes_count {
    color: #007bff;
    margin-right: 10px;
    font-size: 22px;
}
.block_container {
    flex-direction: row;
    display: flex;
    position: relative;
    margin-top: 20px;
}
.edit_btn {
    position: absolute;
    right: -60px;
    background-image: url("@/assets/edit_icon.png");
}

.like {
    background-image: url("@/assets/like_icon.png");
}

.dislike {
    background-image: url("@/assets/dislike_icon.png");
}

.comments {
    background-image: url("@/assets/comment_icon.png");
}

.close {
    background-image: url("@/assets/close_icon.png");
}

.post_container {
    background-color: #fff;
    padding: 30px;
    display: flex;
    flex-direction: column;
    width: 100%;
}

.likes {
    display: flex;
    flex-direction: row;
    align-items: flex-start;
}
.likes_info {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: center;
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
    padding: 25px 25px;
    display: flex;
    flex-direction: column;
}

.post_body img {
    min-height: 300px;
    max-height: 400px;
    max-width: 400px;
    float: left;
}

h3 {
    margin-top: 30px;
}

.themes {
    display: flex;
    flex-direction: row;
    margin-bottom: 30px;
}
.theme {
    border-radius: 15px;
    padding-right: 5px;
    font-size: 18px;
    margin-right: 5px;
    color: #50667c;
}

.theme:hover {
    color: #007bff;
}

.comment_btn {
    padding: 4px 8px;
    border-radius: 15px;
    font-size: 12px;
}
</style>
