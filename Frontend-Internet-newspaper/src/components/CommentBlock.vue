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

</script>

<template>
    <div class="footer">
        <div class="comment_area">
            <b-form-textarea
                size="sm"
                placeholder="Ваш комментарий..."
                v-model="new_comment"
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
        @click.prevent="CommentsStore.showComments(post.id, 2)"
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
}

img[data-v-9d1a3feb] {
    margin-right: 0px;
}

.input_card {
    display: flex;
    flex-direction: row;
}
</style>
