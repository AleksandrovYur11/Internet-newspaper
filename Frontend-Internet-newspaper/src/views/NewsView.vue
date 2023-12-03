<script setup>
import MainBlock from "@/components/MainBlock.vue"
import PostBlock from "@/components/PostBlock.vue"
import ModalForm from "@/components/ModalForm.vue"

import { ref, onMounted } from "vue"

// const news = ref(JSON.parse(localStorage.getItem('news')))

import { useAuthStore } from "@/stores/AuthStore"
const AuthUser = useAuthStore()

import {useNewsStore} from "@/stores/NewsStore.js"
const NewsStore = useNewsStore()

onMounted(() => {
    NewsStore.getnews()
})

//let showModal = false
</script>

<template>
    <modal-form
        v-if="AuthUser.modal"
    />
    <main-block>
        <template #container>
            <div class="news_container">
                <post-block
                    v-for="post in NewsStore.news"
                    :post="post"
                    :key="post.id"
                ></post-block>
            </div>
        </template>
    </main-block>
</template>

<style scoped>
.news_container {
    display: flex;
    flex-direction: column;
    width: 55vw;
}
</style>
