<script setup>
import MainBlock from "@/components/MainBlock.vue"
import PostBlock from "@/components/PostBlock.vue"
import ModalForm from "@/components/ModalForm.vue"
import NavBar from "@/components/NavBar.vue"

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
    <div class="main_container">
        <edit-form v-if="FormStore.edit" />
        <modal-form v-if="NewsStore.modal === true" />
        <nav-bar></nav-bar>
        <div class="cont news">
            <div class="news_container">
                <post-block
                    v-for="post in NewsStore.news"
                    :post="post"
                    :key="post.id"
                ></post-block>
            </div>
        </div>
    </div>
    <div class="footer">
        <div class="footer_container">
            <div class="info_block">
                <div class="info_title">Ваш аккаунт</div>
                <ul>
                    <li>Войти</li>
                    <li>Регистрация</li>
                </ul>
            </div>
            <div class="info_block">
                <div class="info_title">Для связи</div>
                <ul>
                    <li><a href="">Юра</a></li>
                    <li><a href="">Саша</a></li>
                </ul>
            </div>
        </div>
    </div>
</template>

<style scoped>
.news {
    background: linear-gradient(
        to bottom,
        #d5e6f3 20%,
        #a8c2d5 35%,
        #8aa4ba 55%,
        #060912 95%
    );
}

.news_container {
    width: 50%;
    margin: 30px 0px;
}

.footer {
    /* background: #303b44; */
    margin: auto 0 0;
    display: flex;
    flex-direction: row;
    justify-content: center;
    background-image: url("@/assets/footer_bg.jpg");
    background-repeat: no-repeat;
    background-size: cover;
    height: 450px;
}

.footer_container {
    display: flex;
    gap: 50px;
    padding: 20px 0px;
    width: 50%;
}

.info_block {
    width: calc(50% - 50px);
    /* margin-right: 50px; */
}

.info_title {
    -moz-osx-font-smoothing: grayscale;
    -webkit-font-smoothing: antialiased;
    border-bottom: 1px solid rgba(146, 156, 165, 0.4);
    color: #fff;
    font-size: 0.875rem;
    font-style: normal;
    font-weight: 700;
    line-height: 1.0625rem;
    padding-bottom: 16px;
}
</style>
