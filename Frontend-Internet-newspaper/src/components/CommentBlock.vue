<script setup>
import { defineProps } from "vue"
import { ref } from "vue"

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import { useAuthStore } from "@/stores/AuthStore.js"
const AuthUser = useAuthStore()

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

const user_id = ref(sessionStorage.getItem('user_id'))
const user_role = ref(sessionStorage.getItem('user_role'))


const new_comment = ref("")




</script>

<template>
    <div class="footer">
        <div class="comment_area">
            <b-form-textarea
                id="textarea-small"
                size="sm"
                placeholder="Ваш комментарий..."
                v-model="new_comment"
                @keyup.enter="NewsStore.sendcomment(post.id, new_comment)"
                style="
                    margin-right: 10px;
                    max-height: 100px;
                    border-radius: 3px;
                "
            ></b-form-textarea>
            <b-button
                @click="NewsStore.sendcomment(post.id, new_comment)"
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
    <div class="comments_content">
        <b-card
            v-for="(item, index) in post.comments"
            style="margin-top: 10px"
            :key="index"
        >
            <div class="comment_info">
                <span class="text-gray-500 uppercase text-xs"
                    ><b>{{ item.user.name + " " + item.user.surname }}</b></span
                >
                <span class="text-gray-500 uppercase text-xs">{{
                    item.datePublishedComment
                }}</span>
            </div>
            <div class="com_cont">

                <p>{{ item.textComment }}</p>
                <span
                    v-if="user_id == item.user.id || user_role == 'ROLE_ADMIN'"
                    @click="NewsStore.deleteComment( item.id)"
                    style="cursor: pointer"
                >
                    <img
                        src="@/assets/urna.svg"
                        alt="urna"
                    />
                </span>
            </div>
        </b-card>
    </div>
</template>

<style scoped>
.com_cont {
    display: flex;
    flex-direction: row;
    align-items: flex-end;
    justify-content: space-between;
}
.footer {
    background-color: #fff;
}
.comment_info {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
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

/* .footer {
    background-color: #fff;
} */

.input_card {
    display: flex;
    flex-direction: row;
}
</style>
