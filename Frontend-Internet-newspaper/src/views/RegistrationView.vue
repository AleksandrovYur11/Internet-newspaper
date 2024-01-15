<script setup>
import { ref, computed } from "vue"

import MainBlock from "@/components/MainBlock.vue"

import { useAuthStore } from "@/stores/AuthStore.js"
const AuthStore = useAuthStore()

const regData = ref({
    textName: null,
    textSurname: null,
    textEmail: null,
    textPassword: null,
    textRepeatPassword: null,
})

const validation_email = computed(() => {
    if (regData.value.textEmail) {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
        return emailRegex.test(regData.value.textEmail)
    }
})

const validation_password = computed(() => {
    if (regData.value.textPassword) {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/
        const hasLowerCase = /[a-z]/.test(regData.value.textPassword)
        const hasUpperCase = /[A-Z]/.test(regData.value.textPassword)
        const hasNumber = /\d/.test(regData.value.textPassword)
        const isLengthValid = regData.value.textPassword.length >= 8

        switch (true) {
            case !regData.value.textPassword:
                return true
            case !hasLowerCase:
                return "Добавьте строчную букву"
            case !hasUpperCase:
                return "Добавьте заглавную букву"
            case !hasNumber:
                return "Добавьте хотя бы одну цифру"
            case !isLengthValid:
                return "Пароль должен содержать не менее 8 символов"
            case !passwordRegex.test(regData.value.textPassword):
                return "Только буквы и цифры"
            default:
                return true
        }
    }
})

const validation_name = computed(() => {
    if (regData.value.textName) {
        if (regData.value.textName.length <= 1) {
            return "Имя содержит не менее 2 символов"
        } else if (regData.value.textName.length > 255) {
            return "Слишком длинное имя"
        } else {
            return true
        }
    }
})

const validation_surname = computed(() => {
    if (regData.value.textSurname) {
        if (regData.value.textSurname.length <= 1) {
            return "Фамилия содержит не менее 2 символов"
        } else if (regData.value.textSurname.length > 255) {
            return "Слишком длинная фамилия"
        } else {
            return true
        }
    }
})

const validation_repeat_password = computed(() => {
    if (regData.value.textRepeatPassword) {
        return regData.value.textPassword === regData.value.textRepeatPassword
    }
})

const isInputValid = computed(() => {
    return (
        validation_email.value &&
        validation_password.value &&
        validation_name.value &&
        validation_surname.value &&
        validation_repeat_password.value
    )
})

const signUpValidation = () => {
    if (
        !regData.value.textEmail ||
        !regData.value.textPassword ||
        !regData.value.textName ||
        !regData.value.textSurname ||
        !regData.value.textRepeatPassword
    ) {
        alert("Заполните все поля!")
    } else if (!isInputValid.value) {
        alert("Проверьте правильность ввода!")
    } else {
        AuthStore.registration(regData.value)
        regData.value.textName = null
        regData.value.textSurname = null
        regData.value.textEmail = null
        regData.value.textPassword = null
        regData.value.textRepeatPassword = null
    }
}
</script>

<template>
    <MainBlock>
        <template #content>
            <b-form-group
                class="mb-2"
                label="Name"
            >
                <b-form-input
                    v-model="regData.textName"
                    type="text"
                    placeholder="Александр"
                    :state="validation_name"
                    required
                ></b-form-input>
                <b-form-invalid-feedback :state="!validation_name">
                    {{
                        typeof validation_name === "string"
                            ? validation_name
                            : ""
                    }}
                </b-form-invalid-feedback>
                <b-form-valid-feedback :state="validation_name">
                    Ок
                </b-form-valid-feedback>
            </b-form-group>
            <b-form-group
                class="mb-2"
                label="Surname"
            >
                <b-form-input
                    v-model="regData.textSurname"
                    type="text"
                    placeholder="Александров"
                    :state="validation_surname"
                    required
                ></b-form-input>
                <b-form-invalid-feedback :state="!validation_surname">
                    {{
                        typeof validation_surname === "string"
                            ? validation_surname
                            : ""
                    }}
                </b-form-invalid-feedback>
                <b-form-valid-feedback :state="validation_surname">
                    Ок
                </b-form-valid-feedback>
            </b-form-group>
            <b-form-group
                class="mb-2"
                label="Email"
            >
                <b-form-input
                    v-model="regData.textEmail"
                    type="email"
                    placeholder="user.userovich@gmail.com"
                    :state="validation_email"
                    required
                ></b-form-input>
                <b-form-invalid-feedback :state="validation_email">
                    Некорректный email
                </b-form-invalid-feedback>
                <b-form-valid-feedback :state="validation_email">
                    Ок
                </b-form-valid-feedback>
            </b-form-group>
            <b-form-group
                label="Password"
                class="mb-2"
            >
                <b-form-input
                    v-model="regData.textPassword"
                    required
                    type="password"
                    aria-describedby="password-help-block"
                    :state="validation_password"
                ></b-form-input>
                <b-form-valid-feedback :state="validation_password">
                    Ок
                </b-form-valid-feedback>
                <b-form-invalid-feedback :state="!validation_password">
                    {{
                        typeof validation_password === "string"
                            ? validation_password
                            : ""
                    }}
                </b-form-invalid-feedback>
            </b-form-group>
            <b-form-group
                label="Repeat password"
                class="mb-2"
            >
                <b-form-input
                    v-model="regData.textRepeatPassword"
                    required
                    type="password"
                    aria-describedby="password-help-block"
                    :state="validation_repeat_password"
                ></b-form-input>
                <b-form-invalid-feedback :state="validation_repeat_password">
                    Пароли не совпадают
                </b-form-invalid-feedback>
                <b-form-valid-feedback :state="validation_repeat_password">
                    Пароли совпадают
                </b-form-valid-feedback>
            </b-form-group>
        </template>
        <template #btn>
            <b-button
                type="submit"
                variant="success"
                @click="signUpValidation()"
                >Зарегестрироваться</b-button
            >
        </template>
        <template #toggle>
                <a href="/auth/sign-in">Перейти ко входу</a>
        </template>
    </MainBlock>
</template>

