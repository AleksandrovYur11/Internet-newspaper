<script setup>
import { useAuthStore } from "@/stores/AuthStore"
const AuthUser = useAuthStore()

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import {ref,  defineEmits } from 'vue';

const { emit } = defineEmits(['show-modal'])

const user_id = ref(sessionStorage.getItem('user_id'))
const user_role = ref(sessionStorage.getItem('user_role'))

</script>

<template>
    <b-navbar
        toggleable="md"
        sticky="top"
        style="box-shadow: 0px 13px 4px rgba(0, 0, 0, 0.1)"
    >
        <b-navbar-brand
            href="#"
            to="/news/fresh-news"
            style="font-weight: bold"
            >Gazeta.ru</b-navbar-brand
        >
        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
        <b-collapse
            id="nav-collapse"
            is-nav
        >
            <!-- добавить активность переключателя -->
            <b-navbar-nav>
                <div
                    v-if="
                        user_role == 'ROLE_USER' ||
                        user_role == 'ROLE_ADMIN'
                    "
                    class="d-flex flex-direction-row"
                >
                    <span>{{ AuthUser.role }}</span>
                    <b-button  v-if =  "user_role == 'ROLE_ADMIN'" @click="NewsStore.showModal()"> + </b-button>
                    <b-nav-item
                        to="/auth/sign-in"
                        @click="AuthUser.sbrosRole()"
                        >Sign Out</b-nav-item
                    >
                </div>
                <div
                    v-else
                    class="d-flex flex-direction-row"
                >
                    <b-nav-item to="/auth/sign-in">Sign In</b-nav-item>
                    <b-nav-item to="/auth/sign-up">Sign Up</b-nav-item>
                </div>
            </b-navbar-nav>
        </b-collapse>
    </b-navbar>
</template>

<style>
.navbar-expand-md .navbar-collapse {
    justify-content: flex-end;
}
</style>
