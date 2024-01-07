<script setup>
import MainBlock from "@/components/MainBlock.vue"
import { ref, computed } from "vue"

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

// тоже динамическая проверка! на слишком длинное
const validation_name = computed(() => {
    if (regData.value.textName) {
        if (regData.value.textName.length <= 1) {
            return "Ваше имя точно длинее одного символа"
        } else if (regData.value.textName.length > 15) {
            return "Слишком длинное имя"
        } else {
            return true
        }
    }
})

const validation_surname = computed(() => {
    if (regData.value.textSurname) {
        if (regData.value.textSurname.length <= 1) {
            return "Слишком короткая фамилия для русского"
        } else if (regData.value.textSurname.length > 15) {
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
        AuthStore.register(regData.value)
        regData.value.textName = null
        regData.value.textSurname = null
        regData.value.textEmail = null
        regData.value.textPassword = null
        regData.value.textRepeatPassword = null
    }
}
</script>

<template>
    <main-block>
        <template #header> </template>
        <template #container>
            <b-form class="custom-form">
                <h4>Sign Up</h4>
                <b-form-group
                    class="mb-2"
                    id="name_group"
                    label="Name"
                    label-for="text_name"
                >
                    <b-form-input
                        id="text_name"
                        v-model="regData.textName"
                        type="text"
                        placeholder="Александр"
                        :state="validation_name"
                        required
                    ></b-form-input>
                    <b-form-invalid-feedback :state="!validation_name">
                        {{ typeof validation_name === 'string' ? validation_name : '' }}
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_name">
                        Теперь хорошо
                    </b-form-valid-feedback>
                </b-form-group>
                <b-form-group
                    class="mb-2"
                    id="surname_group"
                    label="Surname"
                    label-for="text_surname"
                >
                    <b-form-input
                        id="text_surname"
                        v-model="regData.textSurname"
                        type="text"
                        placeholder="Александров"
                        :state="validation_surname"
                        required
                    ></b-form-input>
                    <b-form-invalid-feedback :state="!validation_surname">
                        {{ typeof validation_surname === 'string' ? validation_surname : '' }}
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_surname">
                        Так то лучше
                    </b-form-valid-feedback>
                </b-form-group>
                <b-form-group
                    class="mb-2"
                    id="email_group"
                    label="Email"
                    label-for="text_email"
                >
                    <b-form-input
                        id="text_email"
                        v-model="regData.textEmail"
                        type="email"
                        placeholder="user.userovich@gmail.com"
                        :state="validation_email"
                        required
                    ></b-form-input>
                    <b-form-invalid-feedback :state="validation_email">
                        Проверьте правильность email
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_email">
                        Выглядит хорошо!
                    </b-form-valid-feedback>
                </b-form-group>
                <b-form-group
                    id="password_group"
                    label="Password"
                    label-for="text_password"
                    class="mb-2"
                >
                    <b-form-input
                        v-model="regData.textPassword"
                        required
                        type="password"
                        id="text-password"
                        aria-describedby="password-help-block"
                        :state="validation_password"
                    ></b-form-input>
                     <b-form-valid-feedback :state="validation_password">
                        Выглядит отлично!
                    </b-form-valid-feedback>
                    <b-form-invalid-feedback :state="!validation_password">
                        {{ typeof validation_password === 'string' ? validation_password : '' }}
                    </b-form-invalid-feedback>
                </b-form-group>
                <b-form-group
                    id="password_repeat_group"
                    label="Repeat password"
                    label-for="text_repeat_password"
                    class="mb-2"
                >
                    <b-form-input
                        v-model="regData.textRepeatPassword"
                        required
                        type="password"
                        id="text_repeat_password"
                        aria-describedby="password-help-block"
                        :state="validation_repeat_password"
                    ></b-form-input>
                    <b-form-invalid-feedback
                        :state="validation_repeat_password"
                    >
                        Пароли не совпадают!
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_repeat_password">
                        Теперь совпадают!
                    </b-form-valid-feedback>
                </b-form-group>
                <b-button
                    type="submit"
                    variant="success"
                    class="submit_btn"
                    @click="signUpValidation()"
                    >Submit</b-button
                >
            </b-form>
        </template>
    </main-block>
</template>

<style scoped>
.submit_btn {
    bottom: 30px;
    align-self: center;
    position: absolute;
    width: 50%;
}

h4 {
    color: #868686;
    display: flex;
    justify-content: center;
}

.custom-form {
    color: #007bff; /* Белый цвет текста */
    background-color: #ffffff;
    min-height: 85vh;
    min-width: 30vw;
    border-radius: 15px;
    padding: 25px 30px;
    display: flex;
    flex-direction: column;
    align-content: center;
    position: relative;
    box-shadow: 10px 12px 4px rgba(0, 0, 0, 0.1);
}
</style>
