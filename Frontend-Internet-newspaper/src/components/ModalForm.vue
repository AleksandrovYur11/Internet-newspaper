<script setup>
import { ref, computed } from "vue"

import { useAuthStore } from "@/stores/AuthStore.js"
const Auth = useAuthStore()

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

 const newsTitle = ref('')
 const newsText= ref('')
 const picture = ref('')
 const  themes = ref('')

</script>

<template>
    <div
        class="container"
        @click.self="NewsStore.closeModal()"
    >
    <b-form class="custom-form">
        <!-- <div class="modal-content"> -->
            <div class="header_modal">
                <h4>Create news</h4>
                <span
                    class="close"
                    @click="NewsStore.closeModal()"
                    >&times;</span
                >
            </div>
            
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

                <b-form-group
                    class="mb-1"
                    id="themes_group"
                    label="Themes:"
                    label-for="microThemes"
                >
                    <b-form-input
                        id="microThemes"
                        v-model="themes"
                        type="text"
                        placeholder="Меркурий, Венера, Земля..."
                        :state="validation_themes"
                        required
                    ></b-form-input>
                    <b-form-invalid-feedback :state="validation_themes">
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_themes">
                        Looks Good.
                    </b-form-valid-feedback>
                </b-form-group>

                <b-form-group
                    class="mb-1"
                    id="text_group"
                    label="Text:"
                    label-for="postText"
                >
                    <!-- сделать ограничение и скролл + кол-во символов) -->
                    <b-form-textarea
                        id="postText"
                        v-model="newsText"
                        type="text"
                        placeholder="...название этот вид получил из-за очень непривлекательной внешности. - это не факт, это википедия"
                        :state="validation_text"
                        required
                    ></b-form-textarea>
                    <b-form-invalid-feedback :state="validation_text">
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_text">
                        Looks Good.
                    </b-form-valid-feedback>
                </b-form-group>
                <b-form-group
                    class="mb-1"
                    id="image_group"
                    label="Image URL:"
                    label-for="imageUpload"
                >
                    <b-form-input
                        id="imageUpload"
                        v-model="picture"
                        type="text"
                        placeholder="https://dikoed.ru/upload/iblock/361/16025-morskoy-chert-s-golovoy.jpg"
                        :state="validation_image"
                        required
                    ></b-form-input>
                    <b-form-invalid-feedback :state="validation_image">
                        Your user ID must be 5-12 characters long.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="validation_image">
                        Looks Good.
                    </b-form-valid-feedback>
                </b-form-group>
            <div class="footer_modal">
                <b-button
                    type="submit"
                    @click="NewsStore.addNews(newsTitle, newsText, picture, themes)"
                    class="submit_btn"
                    >Create</b-button
                >
            </div>
          </b-form>
        </div>
    
</template>

<style scoped>


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

h4 {
    color: #868686;
}

.footer_modal {
    display: flex;
    justify-content: flex-end;
}
.submit_btn {
    bottom: 20px;
    position: absolute;
    width: 25%;
}

.header_modal {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}
.container {
    z-index: 1000000;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
}

.modal {
    display: none;
    position: fixed;
    z-index: 1000;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
    background-color: #fefefe;
    padding: 20px;
    width: 40%;
    min-height: 60%;
    animation: modal-show 0.5s;
}

@keyframes modal-show {
    from {
        opacity: 0;
    }
    to {
        opacity: 1;
    }
}

.close {
    color: #aaa;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
}
</style>
