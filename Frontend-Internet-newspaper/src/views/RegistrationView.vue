<script setup>
import MainBlock from "@/components/MainBlock.vue"
import {ref, computed} from 'vue'


import router from '@/router/index.js'

const textName = ref(null)
const validation_name = computed(() => {
    if (textName.value)
        return textName.value.length > 4 && textName.value.length < 13
})

const textSurname = ref(null)
const validation_surname = computed(() => {
    if (textSurname.value)
        return textSurname.value.length > 4 && textSurname.value.length < 13
})

const textEmail = ref(null)
const validation_email = computed(() => {
    if (textEmail.value)
        return textEmail.value.length > 4 && textEmail.value.length < 25
})

const textPassword = ref(null)
const validation_password = computed(() => {
    if (textPassword.value)
        return textPassword.value.length > 4 && textPassword.value.length < 25
})

const textRepeatPassword = ref(null)
const validation_repeat_password = computed(() => {
    if (textRepeatPassword.value)
        return textRepeatPassword.value.length > 4 && textRepeatPassword.value.length < 25
})

//////////////////////////////////////
const register = async () => {
  const userData = {
    name: textName.value,
    surname: textSurname.value,
    email: textEmail.value, 
    password: textPassword.value, 
  };

  try {
    const response = await fetch("http://localhost:8085/auth/signup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userData),
    });

    if (!response.ok) {
      // Обработка ошибок при регистрации
      throw new Error("Registration failed");
    }

    router.push('/auth/signin')
   
    
  } catch (error) {
    console.error("Registration error:", error);
  }
};


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
                        v-model="textName"
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
                        v-model="textSurname"
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
                    class="mb-2"
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
                <b-form-group
                    id="password_repeat_group"
                    label="Repeat password"
                    label-for="text_repeat_password"
                    class="mb-2"
                >
                    <b-form-input
                        v-model="textRepeatPassword"
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
                    @click = "register()"
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
