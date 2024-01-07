<script setup>
import MainBlock from "@/components/MainBlock.vue"
import { ref, computed } from "vue"

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
const validation_password = computed(() => {
    if (textPassword.value) {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$/
        return passwordRegex.test(textPassword.value)
    }
})

const isInputValid = computed(() => {
    console.log(validation_email.value && validation_password.value)
    return validation_email.value && validation_password.value
})

const signInValidation = () => {
    console.log(textEmail.value)
    if (!textEmail.value || !textPassword.value) {
        alert("Заполните все поля!")
    } else if (!isInputValid.value) {
        alert("Проверьте правильность ввода!")
    } else {
        AuthUser.login(textEmail.value, textPassword.value)
        textEmail.value = null
        textPassword.value = null
    }
}

</script>

<template>
    <main-block>
        <template #header> </template>
        <template #container>
            <b-form
                class="custom-form"
            >
                <h4>Sign In</h4>
                <b-form-group
                    class="mb-3"
                    id="email_group"
                    label="Email"
                    label-for="text_email"
                >
                    <b-form-input
                        id="text_email"
                        v-model="textEmail"
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
                >
                    <b-form-input
                        v-model="textPassword"
                        required
                        type="password"
                        id="text-password"
                        aria-describedby="password-help-block"
                        :state="validation_password"
                    ></b-form-input>
                    <b-form-invalid-feedback :state="validation_password">
                        Больше символов...
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_password">
                        Выглядит отлично!
                    </b-form-valid-feedback>
                </b-form-group>
                <b-button
                    @click="signInValidation()"
                    variant="primary"
                    class="submit_btn"
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
    min-height: 50vh;
    min-width: 30vw;
    /* height: 50vh;
    width: 30vw; */
    border-radius: 15px;
    padding: 25px 30px;
    display: flex;
    flex-direction: column;
    align-content: center;
    position: relative;
    box-shadow: 10px 12px 4px rgba(0, 0, 0, 0.1);
}
</style>
