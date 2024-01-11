<script setup>
import { ref, computed } from "vue"

import InputForm from "@/components/InputForm.vue"

import { useAuthStore } from "@/stores/AuthStore.js"
const Auth = useAuthStore()

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import { useFormsStore } from "@/stores/FormsStore.js"
const FormStore = useFormsStore()

const newsTitle = ref("")
const newsText = ref("")
const picture = ref("")
const themes = ref("")

console.log(themes)

// const modalFormValidation = () => {
//     const urlRegex =  /\.(jpeg|jpg|gif|png|bmp|svg)$/i
//     const maxTextLength = 255
//     const themeInvalidLength = themes.value
//         .split(",")
//         .some((theme) => theme.trim().length > maxTextLength)
//     if (
//         newsTitle.value.length === 0 ||
//         newsText.value.length === 0 ||
//         picture.value.length === 0 ||
//         themes.value.length === 0
//     ) {
//         alert("Заполните все поля!")
//     } else if (newsTitle.value.length > maxTextLength) {
//         alert("Название не должно превышать 255 символов")
//     } else if (themeInvalidLength) {
//         alert("Одна тема не должна превышать 255 символов")
//     } else if (!urlRegex.test(picture.value)) {
//         console.log(picture)
//         console.log(urlRegex.test(picture.value))
//         alert("Некорректный URL картинки")
//     } else {
//         NewsStore.addNews(
//             newsTitle.value,
//             newsText.value,
//             picture.value,
//             themes.value
//         )
//         newsTitle.value = ""
//         newsText.value = ""
//         picture.value = ""
//         themes.value = ""
//     }
// }
</script>

<template>
    <div
        class="container"
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
            <InputForm
                v-model="newsTitle"
                groupLabel="Название:"
                placeholder="Ваша новость"
            ></InputForm>
            <InputForm
                v-model="themes"
                groupLabel="Темы:"
                placeholder="природа, космос, IT..."
            ></InputForm>
            <InputForm
                v-model="newsText"
                groupLabel="Текст:"
                placeholder="Текст вашей новости"
            ></InputForm>
            <InputForm
                v-model="picture"
                groupLabel="Изображение:"
                placeholder=".jpeg .jpg .gif .png .bmp .svg"
            ></InputForm>
            <div class="footer_modal">
                <b-button
                    type="submit"
                    @click="FormStore.formsValidation(newsTitle, newsText, picture, themes, 'modal')" 
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
