<script setup>
import { useAuthStore } from "@/stores/AuthStore"
const AuthUser = useAuthStore()

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import FiltrBlock from "@/components/FiltrBlock.vue"

import { ref, defineEmits } from "vue"

const { emit } = defineEmits(["show-modal"])

const user_id = ref(sessionStorage.getItem("user_id"))
const user_role = ref(sessionStorage.getItem("user_role"))
const user_name = ref(sessionStorage.getItem("user_name"))
</script>

<template>
    <b-navbar
        toggleable="md"
        sticky="top"
        style="
            box-shadow: 0px 13px 4px rgba(0, 0, 0, 0.1);
            background-color: rgb(255, 255, 255);
        "
    >
        <div class="nav_item_right">
            <b-navbar-brand
                class="logo"
                href="#"
                to="/news/fresh-news"
            ></b-navbar-brand>
            <b-navbar-brand
                href="#"
                to="/news/fresh-news"
                style="font-weight: bold"
                >Маяк</b-navbar-brand
            >
        </div>
        <div
            v-if="user_role == 'ROLE_USER' || user_role == 'ROLE_ADMIN'"
            class="nav_item_right"
        >
            <span style=" font-size: 18px">{{ user_name }}</span>
            <filtr-block></filtr-block>
            <b-button
                v-if="user_role == 'ROLE_ADMIN'"
                @click="NewsStore.showModal()"
                style="
                    background-color: #007bff;
                    border: none;
                    font-weight: bolder;
                    cursor: pointer;
                "
            >
                +
            </b-button>
            <span
                class="icon exit"
                to="/auth/sign-in"
                @click="AuthUser.deleteToken()"
            >
            </span>
        </div>
        <div
            v-else
            class="d-flex flex-direction-row"
        >
            <filtr-block></filtr-block>
            <div class="nav_item_right">
                <a
                    style="color: #007bff"
                    href="/auth/sign-in"
                    >Sign In</a
                >

                <a
                    style="color: #007bff"
                    href="/auth/sign-up"
                    >Sign Up</a
                >
            </div>
        </div>
    </b-navbar>
</template>

<style>
.navbar-expand-md .navbar-collapse {
    justify-content: flex-end;
}

.logo {
    background-image: url("@/assets/logo.jpg");
    width: 70px;
    height: 70px;
    border-radius: 50%;
    background-size: cover;
}

.exit {
    background-image: url("@/assets/exit_icon.png");
}

.nav_item_right {
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 10px;
}

a {
    cursor: pointer;
}
</style>
