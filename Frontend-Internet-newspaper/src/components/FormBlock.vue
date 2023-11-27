<script setup>
// import { ref, computed } from "vue"

// //написать валидацию для почты и пароля потом!
// const textEmail = ref(null)
// const validation_email = computed(() => {
//     if (textEmail.value)
//         return textEmail.value.length > 4 && textEmail.value.length < 13
// })

// const textPassword = ref(null)
// const validation_password = computed(() => {
//     if (textPassword.value)
//         return textPassword.value.length > 4 && textPassword.value.length < 13
// })
</script>

<template>
    <!-- <b-form
        @submit.stop.prevent
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
                :state="validation"
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
                :state="validation"
            ></b-form-input>
            <b-form-invalid-feedback :state="validation_password">
                Your user ID must be 5-12 characters long.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="validation_password">
                Looks Good.
            </b-form-valid-feedback>
        </b-form-group>
        <b-button type="submit" variant="primary" class = "submit_btn">Submit</b-button>
    </b-form> -->
</template>
<!-- 
<style>

.submit_btn{
    bottom: 30px;
    align-self: center;
    position: absolute;
    width: 50%;
}

h4 {
    color: #868686;
    margin: auto;
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
}

</style> -->
