<script setup>
import { defineProps } from "vue"
import { ref } from "vue"

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import { useAuthStore } from "@/stores/AuthStore.js"
const AuthUser = useAuthStore()

import { useCommentsStore } from "@/stores/CommentsStore.js"
const CommentsStore = useCommentsStore()

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

const user_id = ref(sessionStorage.getItem("user_id"))
const user_role = ref(sessionStorage.getItem("user_role"))

const new_comment = ref("")

import { computed } from "vue"

const filteredComments = computed(() =>
    CommentsStore.comments.filter((obj) => obj.news_id === post.id)
)

import CommentItem from "@/components/CommentItem.vue"

// const getCommetsByNewsId = (comments, news_id) => {
//    const com = comments.filter(obj => obj.news_id === news_id)
//    console.log(com)
//    return com.comments
//    //console.log(com.comments)
//    //return  com
// }

// const handleCreatePost = async () => {
//     await NewsStore.sendcomment(post.id, new_comment)
// }
</script>

<template>
    <div class="footer">
        <div class="comment_area">
            <b-form-textarea
                id="textarea-small"
                size="sm"
                placeholder="Ваш комментарий..."
                v-model="new_comment"
                @keyup.enter="handleCreatePost"
                style="
                    margin-right: 10px;
                    max-height: 100px;
                    border-radius: 3px;
                "
            ></b-form-textarea>
            <b-button
                @click="CommentsStore.sendcomment(post.id, new_comment)"
                style="border-radius: 50%; width: 50px; height: 50px"
            >
                <img
                    src="@/assets/send.svg"
                    alt="Your SVG Image"
                />
            </b-button>
        </div>
    </div>
    <!-- class="text-gray-700 py-4 pb-0 rounded" -->

    <!-- v-if = "CommentsStore.showed && CommentsStore.comments.length !== 0" -->
    <div class="comments_content">
        <!-- {{
            CommentsStore.comments
                .filter((obj) => obj.news_id === post.id) // Фильтрация по news_id равному post.id
                .flatMap((obj) => obj.comments)
        }} -->

        <comment-item
            v-for="comment in CommentsStore.comments
                .filter((obj) => obj.news_id === post.id)
                .flatMap((obj) => obj.comments)"
            :key="comment.id"
            :comment="comment"
            :post="post"
        >
        </comment-item>
        <!-- </div> -->
    </div>
    <!-- v-if = "CommentsStore.commentsCount >=3" -->
    <!-- v-if = "(CommentsStore.comments.filter((obj) => obj.news_id === post.id).flatMap((obj) => obj.comments)).length < CommentsStore.commentsCount" -->

    <a
        v-if="CommentsStore.commentsCount >= 3"
        href=""
        @click.prevent="CommentsStore.showComments(post.id, 2)"
        >Еще</a
    >
</template>

<style scoped>
/* .com_cont {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: space-between;
} */
.footer {
    background-color: #fff;
}
/* .comment_info {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
} */
.comments_content {
    display: flex;
    flex-direction: column;
}
.comment_area {
    margin-top: 10px;
    display: flex;
    border: 1px solid #fff;
    flex-direction: row;
    justify-content: space-between;
    background-color: #fff;
}

img[data-v-9d1a3feb] {
    margin-right: 0px;
}

/* .footer {
    background-color: #fff;
} */

.input_card {
    display: flex;
    flex-direction: row;
}
</style>
