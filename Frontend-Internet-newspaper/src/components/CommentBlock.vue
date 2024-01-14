<script setup>
import { ref, computed, defineProps } from "vue"

import { useCommentsStore } from "@/stores/CommentsStore.js"
const CommentsStore = useCommentsStore()

import CommentItem from "@/components/CommentItem.vue"

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

const new_comment = ref("")
const user_role = ref(sessionStorage.getItem("user_role"))

const sendComment = (post_id, comment)=>{
    if (comment === "") {
                alert("Комментарий пуст!")
    } else {
        CommentsStore.sendcomment(post_id, comment)
        new_comment.value = ""
    }
}
</script>

<template>
    <div class="footer">
        <div class="comment_area">
            <b-form-textarea
                v-if="user_role === 'ROLE_ADMIN' || user_role === 'ROLE_USER'"
                id="textarea"
                rows="3"
                max-rows="15"
                size="sm"
                placeholder="Ваш комментарий..."
                v-model="new_comment"
                style="
                    min-height: 73px;
                    margin-right: 10px;
                    max-height: 400px;
                    border-radius: 3px;
                "
            ></b-form-textarea>
            <span
                v-if="user_role === 'ROLE_ADMIN' || user_role === 'ROLE_USER'"
                @click="sendComment(post.id, new_comment)"
                class="icon send"
            >
            </span>
        </div>
    </div>
    <div class="comments_content">
        <comment-item
            v-for="comment in CommentsStore.comments
                .filter((obj) => obj.news_id === post.id)
                .flatMap((obj) => obj.comments)"
            :key="comment.id"
            :comment="comment"
            :post="post"
        >
        </comment-item>
    </div>
    <a
        v-if="CommentsStore.getCommentsInfo(post.id)"
        href=""
        @click.prevent="CommentsStore.showComments(post.id)"
        >Еще
    </a>
</template>

<style scoped>
.footer {
    background-color: #fff;
}

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
    /* align-items: center; */
}

img[data-v-9d1a3feb] {
    margin-right: 0px;
}

.input_card {
    display: flex;
    flex-direction: row;
}

.send {
    background-image: url("@/assets/send_icon.png");
    margin-right: 0;
}
</style>
