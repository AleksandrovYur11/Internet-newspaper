<script setup>
const { props } = defineProps({
    comment: {
        type: Object,
        required: true,
    },
    post: {
        type: Object,
        required: true,
    },
})

import { ref, computed } from "vue"

import { useCommentsStore } from "@/stores/CommentsStore.js"
const CommentsStore = useCommentsStore()

const user_id = ref(sessionStorage.getItem("user_id"))
const user_role = ref(sessionStorage.getItem("user_role"))

const dateFormating = (date) => {
    const receivedDate = new Date(date)
    const currentDate = new Date()

    const todayDate = new Date(
        currentDate.getFullYear(),
        currentDate.getMonth(),
        currentDate.getDate()
    )

    const yesterdayDate = new Date(todayDate)
    yesterdayDate.setDate(todayDate.getDate() - 1)

    function formatTime(inputTime) {
        return inputTime.toLocaleTimeString("ru-RU", {
            hour: "2-digit",
            minute: "2-digit",
        })
    }

    function formatDate(inputDate) {
        const day = inputDate.getDate().toString().padStart(2, "0")
        const month = (inputDate.getMonth() + 1).toString().padStart(2, "0")
        const year = inputDate.getFullYear()
        return `${day}.${month}.${year}`
    }

    if (receivedDate >= todayDate) {
        return `Сегодня в ${formatTime(receivedDate)}`
    } else if (receivedDate >= yesterdayDate) {
        return `Вчера в ${formatTime(receivedDate)}`
    } else {
        return `${formatDate(receivedDate)} ${formatTime(receivedDate)}`
    }
}
</script>

<template>
    <b-card style="margin-top: 10px">
        <div class="comment_info">
            <span class="text-gray-500 uppercase text-xs"
                ><b>{{
                    comment.user.name + " " + comment.user.surname
                }}</b></span
            >
            <span
                class="close"
                v-if="user_id == comment.user.id || user_role === 'ROLE_ADMIN'"
                @click="CommentsStore.deleteComment(comment.id)"
                >&times;</span
            > 
        </div>
        <div class="com_cont">
            <p>{{ comment.textComment }}</p>
            <span class="text-gray-500 uppercase text-xs" style=" color: #007bff">{{
                dateFormating(comment.datePublishedComment)
            }}</span>
        </div>
    </b-card>
</template>

<style scoped>
.close {
    color: #aaa;
    font-size: 28px;
    font-weight: bold;
    cursor: pointer;
}

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
