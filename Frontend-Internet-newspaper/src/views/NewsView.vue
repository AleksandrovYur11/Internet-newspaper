<script setup>
import { onBeforeMount } from "vue"

import PostBlock from "@/components/PostBlock.vue"
import ModalForm from "@/components/ModalForm.vue"
import NavBar from "@/components/NavBar.vue"
import EditForm from "@/components/EditForm.vue"

import { useNewsStore } from "@/stores/NewsStore.js"
const NewsStore = useNewsStore()

import { useFormsStore } from "@/stores/FormsStore.js"
const FormStore = useFormsStore()

onBeforeMount(() => {
    NewsStore.getnews()
})
</script>

<template>
    <div class="main_container">
        <EditForm v-if="FormStore.edit" />
        <ModalForm v-if="NewsStore.modal" />
        <NavBar />
        <div class="cont news">
            <div class="news_container">
                <PostBlock
                    v-for="post in NewsStore.news"
                    :post="post"
                    :key="post.id"
                />
            </div>
        </div>
    </div>
    <div class="footer">
        <div class="footer_container">
            <div class="info_block">
                <div class="info_title">Ваш аккаунт</div>
                <ul>
                    <li><a href="/auth/sign-in">Войти</a></li>
                    <li><a href="/auth/sign-up">Регистрация</a></li>
                </ul>
            </div>
            <div class="info_block">
                <div class="info_title">Для связи</div>
                <ul>
                    <li><a href="">aleksandrov@yandex.ru</a></li>
                    <li><a href="">sokolova@yandex.ru</a></li>
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
        #050b1d 95%
    );
}

.news_container {
    width: 50%;
    margin: 30px 0px;
}

.footer {
    margin: auto 0 0;
    display: flex;
    flex-direction: row;
    justify-content: center;
    background-image: url("@/assets/mayak_footer.png");
    background-repeat: no-repeat;
    background-size: cover;
}

.footer_container {
    display: flex;
    gap: 70px;
    padding: 100px 0px;
    width: 50%;
}

.info_block {
    width: calc(50% - 40px);
}

.info_title {
    -moz-osx-font-smoothing: grayscale;
    -webkit-font-smoothing: antialiased;
    border-bottom: 1px solid rgba(146, 156, 165, 0.4);
    color: #fff;
    font-size: 24px;
    font-style: normal;
    font-weight: 700;
    line-height: 1.0625rem;
    padding-bottom: 16px;
}

ul {
    list-style: none;
    padding: 0;
    margin: 0;
}

li {
    margin-top: 12px;
}

a {
    text-decoration: none;
    color: #fff;
    transition: color 0.3s ease;
}
</style>
