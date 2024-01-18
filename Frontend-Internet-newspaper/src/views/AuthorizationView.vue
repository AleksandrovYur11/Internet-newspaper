<script setup>
import { ref, computed } from "vue"

import MainBlock from "@/components/MainBlock.vue"

import { useAuthStore } from "@/stores/AuthStore"
const AuthUser = useAuthStore()

const textEmail = ref(null)
const validation_email = computed(() => {
    if (textEmail.value) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        return emailRegex.test(textEmail.value)
    }
})

const textPassword = ref(null)

const isInputValid = computed(() => {
    return validation_email.value
})

const signInValidation = () => {
    if (!textEmail.value || !textPassword.value) {
        alert("Заполните все поля!")
    } else if (!isInputValid.value) {
        alert("Проверьте правильность ввода!")
    } else {
        AuthUser.authorization(textEmail.value, textPassword.value)
        textEmail.value = null
        textPassword.value = null
    }
}
</script>

<template>
    <MainBlock>
        <template #content>
            <b-form-group
                    class="mb-3"
                    label="Email"
                >
                    <b-form-input
                        v-model="textEmail"
                        type="email"
                        placeholder="user.userovich@gmail.com"
                        :state="validation_email"
                        required
                    ></b-form-input>
                    <b-form-invalid-feedback :state="validation_email">
                        Введите корректный email
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_email">
                        Ок
                    </b-form-valid-feedback>
                </b-form-group>
                <b-form-group
                    label="Password"
                >
                    <b-form-input
                        v-model="textPassword"
                        required
                        type="password"
                        aria-describedby="password-help-block"
                    ></b-form-input>
                </b-form-group>
        </template>
        <template #btn>
            <b-button
                    @click="signInValidation()"
                    variant="primary"
                    >Войти</b-button
                >
        </template>
        <template #toggle>
            <a href="/auth/sign-up">Еще не зарегестрированы?</a>
        </template>
    </MainBlock>
</template> 

