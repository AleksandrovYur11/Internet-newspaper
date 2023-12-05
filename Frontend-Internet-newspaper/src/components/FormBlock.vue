<script setup>
import { useAuthStore } from "@/stores/AuthStore.js"
const Auth = useAuthStore()

const newsTitle = ref("")
const newsText = ref("")
const picture = ref("")
const themes = ref("")
</script>

<template>
    <b-form class="custom-form">
        <header>
            <div class="header_modal">
                <h2>
                    <slot> named block</slot>
                </h2>
                <span
                    class="close"
                    @click="Auth.closeModal()"
                    >&times;</span
                >
            </div>
        </header>
        <div class="container">
            <b-form-group
                    class="mb-1"
                    id="title_group"
                    label="Title:"
                    label-for="postTitle"
                >
                    <b-form-input
                        id="postTitle"
                        v-model="newsTitle"
                        type="text"
                        placeholder="Чертила..."
                        :state="validation_title"
                        required
                    ></b-form-input>
                    <b-form-invalid-feedback :state="validation_title">
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_title">
                        Looks Good.
                    </b-form-valid-feedback>
                </b-form-group>
            <slot name="container"> </slot>
        </div>
        <footer class="footer_modal"></footer>
    </b-form>
</template>

<style scoped>
.header_modal {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
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
