<script setup>

const { props } = defineProps({
    comment: {
      type: Object,
      required: true
    },
    post: {
      type: Object,
      required: true
    }
})

import {ref, computed} from 'vue'

import { useCommentsStore } from "@/stores/CommentsStore.js"
const CommentsStore = useCommentsStore()

const user_id = ref(sessionStorage.getItem("user_id"))
const user_role = ref(sessionStorage.getItem("user_role"))

</script>

<template>
    <b-card  style="margin-top: 10px"
        >
            <div class="comment_info">
                <span class="text-gray-500 uppercase text-xs"
                    ><b>{{ comment.user.name + " " + comment.user.surname }}</b></span
                >
                <span class="text-gray-500 uppercase text-xs">{{
                    comment.datePublishedComment
                }}</span>
            </div>
            <div class="com_cont">
                <p>{{ comment.textComment }}</p>
                <span
                    v-if="user_id == comment.user.id || comment.user_role == 'ROLE_ADMIN'"
                    @click="CommentsStore.deleteComment(comment.id)"
                    style="cursor: pointer"
                >
                    <img
                        src="@/assets/urna.svg"
                        alt="urna"
                    />
                </span>
            </div>
        </b-card>
</template>

<style scoped>
.com_cont {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: space-between;
}

.comment_info {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

</style>