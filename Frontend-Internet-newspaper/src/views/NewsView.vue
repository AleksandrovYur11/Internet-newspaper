<script setup>
import MainBlock from "@/components/MainBlock.vue"
import PostBlock from "@/components/PostBlock.vue"
import ModalForm from "@/components/ModalForm.vue"

import { ref, onMounted } from "vue"

// const news = ref(JSON.parse(localStorage.getItem('news')))

import { useAuthStore } from "@/stores/AuthStore"
const AuthUser = useAuthStore()

import { useNewsStore } from "@/stores/NewsStore.js"
const NewsStore = useNewsStore()

import EditForm from "@/components/EditForm.vue"

const news = ref([]);

onMounted(() => {
    NewsStore.getnews()
    news.value = JSON.parse(sessionStorage.getItem('news'))
    console.log(news)
})



//let showModal = false
</script>

<template>
    <edit-form v-if="NewsStore.edit"/>
    <modal-form v-if="NewsStore.modal" />
    <main-block>
        <template #container>
            <div class="news_container">
                <post-block
                    v-for="post in news.slice().reverse()"
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
