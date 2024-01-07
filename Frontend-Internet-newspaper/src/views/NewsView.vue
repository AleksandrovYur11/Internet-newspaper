<script setup>
import MainBlock from "@/components/MainBlock.vue"
import PostBlock from "@/components/PostBlock.vue"
import ModalForm from "@/components/ModalForm.vue"

import { ref, onBeforeMount, onMounted } from "vue"

import { useAuthStore } from "@/stores/AuthStore"
const AuthUser = useAuthStore()

import { useNewsStore } from "@/stores/NewsStore.js"
const NewsStore = useNewsStore()

import EditForm from "@/components/EditForm.vue"

import { useFormsStore } from "@/stores/FormsStore.js"
const FormStore = useFormsStore()

onBeforeMount(() => {
    NewsStore.getnews()
})
</script>

<template>
    <edit-form v-if="FormStore.edit"/>
    <modal-form v-if="NewsStore.modal === true" />
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
