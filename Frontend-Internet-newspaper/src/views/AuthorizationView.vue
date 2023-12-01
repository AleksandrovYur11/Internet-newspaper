<script setup>
import MainBlock from "@/components/MainBlock.vue"
import { ref, computed } from "vue"

import router from '@/router/index.js'

import { useAuthStore } from "@/stores/AuthStore"
import { BCloseButton } from "bootstrap-vue-next";
const AuthUser = useAuthStore()

//написать валидацию для почты и пароля потом! + не давать вводить пароль некоторые символы
const textEmail = ref(null)
const validation_email = computed(() => {
    if (textEmail.value)
        return textEmail.value.length > 4 && textEmail.value.length < 30
})

const textPassword = ref(null)
const validation_password = computed(() => {
    if (textPassword.value)
        return textPassword.value.length > 4 && textPassword.value.length < 30
})

///////////////////////////////require////////////
const login = async () => {
    
    const loginData = {
        email: textEmail.value,
        password: textPassword.value,
    }

    console.log(loginData)

    try {
        const response = await fetch("http://localhost:8085/auth/signin", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(loginData),
        })

        if (!response.ok) {
            alert('Неправильный вход!')
            textEmail.value = null
            textPassword.value = null
            throw new Error("Authentication failed")
        }

        const responseData = await response.json()

        AuthUser.setAuthUser(responseData)


        const jwtToken = responseData.accessToken
        //const name = responseData.name
        console.log(responseData)
        // console.log("JWT Token:", jwtToken)

        // sessionStorage.setItem('jwtToken', 'ваш_токен');
        sessionStorage.setItem('jwtToken', jwtToken);
        
        router.push('/news/fresh-news')

    } catch (error) {
        console.error("Authentication error:", error)
    }
}

/////////////////

</script>

<template>
    <main-block>
        <template #header> </template>
        <!-- @submit.stop.prevent -->
        <template #container>
            <b-form
                @submit.prevent="login"
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
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_email">
                        Looks Good.
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
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_password">
                        Looks Good.
                    </b-form-valid-feedback>
                </b-form-group>
                <!-- type = "submit" -->
                <b-button
                    @click="login()"
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
