<script setup>
import MainBlock from "@/components/MainBlock.vue"
import {ref, computed} from 'vue'

import {useAuthStore} from "@/stores/AuthStore.js"
const AuthStore = useAuthStore()

const regData = ref({
    textName: null, 
    textSurname: null,
    textEmail: null,
    textPassword: null,
    textRepeatPassword: null
})

const validation_name = computed(() => {
    if (regData.textName)
        return regData.textName.length > 4 && regData.textName.length < 13
})

const validation_surname = computed(() => {
    if (regData.textSurname)
        return regData.textSurname.length > 4 && regData.textSurname.length < 13
})

const validation_email = computed(() => {
    if (regData.textEmail)
        return regData.textEmail.length > 4 && regData.textEmail.length < 25
})

const validation_password = computed(() => {
    if (regData.textPassword)
        return regData.textPassword.length > 4 && regData.textPassword.length < 25
})

const validation_repeat_password = computed(() => {
    if (regData.textRepeatPassword)
        return regData.textRepeatPassword.length > 4 && regData.textRepeatPassword.length < 25
})

</script>

<template>
    <main-block>
        <template #header> </template>
        <template #container>
            <b-form
                @submit.stop.prevent
                class="custom-form"
            >
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
                    <b-form-invalid-feedback :state="validation_name">
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_name">
                        Looks Good.
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
                    <b-form-invalid-feedback :state="validation_surname">
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_surname">
                        Looks Good.
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
                    <b-form-invalid-feedback :state="validation_password">
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_password">
                        Looks Good.
                    </b-form-valid-feedback>
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
                        type="repeat_password"
                        id="text_repeat_password"
                        aria-describedby="password-help-block"
                        :state="validation_repeat_password"
                    ></b-form-input>
                    <b-form-invalid-feedback :state="validation_repeat_password">
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_repeat_password">
                        Looks Good.
                    </b-form-valid-feedback>
                </b-form-group>
                <b-button
                    type="submit"
                    variant="success"
                    class="submit_btn"
                    @click = "AuthStore.register(regData)"
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
