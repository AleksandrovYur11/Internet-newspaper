<script setup>
import { ref, computed, onMounted, onBeforeMount } from "vue"

import { useAuthStore } from "@/stores/AuthStore.js"
const Auth = useAuthStore()

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import { useFormsStore } from "@/stores/FormsStore.js"
const FormStore = useFormsStore()

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

import InputForm from "@/components/InputForm.vue"


const newsTitle = ref("")
const newsText = ref("")
const picture = ref("")
const themes = ref("")

onBeforeMount(async () => {
    await FormStore.getInfoNews(FormStore.edit_post_id)
    console.log(FormStore.news_for_edit)
    if (FormStore.news_for_edit) {
        newsTitle.value = FormStore.news_for_edit.newsTitle
        newsText.value = FormStore.news_for_edit.newsText
        picture.value = FormStore.news_for_edit.picture.url
        themes.value = FormStore.news_for_edit.themes
            .map((theme) => theme.name)
            .join(", ")
    } else {
        console.log("No edit news data available")
    }
})

// const editFormValidation = () => {
//     if (
//         newsTitle.value.length === 0 ||
//         newsText.value.length === 0 ||
//         picture.value.length === 0 ||
//         themes.value.length === 0
//     ) {
//         alert("Заполните все поля!")
//         // } else if (!isInputValid.value) {
//         //     alert("Проверьте правильность ввода!")
//     } else {
//         FormStore.updateNews(
//             newsTitle,
//             newsText,
//             picture,
//             themes,
//             FormStore.edit_post_id
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
        class="main_container modal_bg"
    >
        <b-form class="custom_form">
            <div class="header_modal">
                <h4>Edit news</h4>
                <span
                    class="close"
                    @click="FormStore.closeEdit()"
                    >&times;</span
                >
            </div>
            <InputForm
                v-model="newsTitle"
                groupLabel="Название:"
            ></InputForm>
            <InputForm
                v-model="themes"
                groupLabel="Темы:"
            ></InputForm>
            <InputForm
                v-model="newsText"
                groupLabel="Текст:"
                textArea="textArea"
            ></InputForm>
            <InputForm
                v-model="picture"
                groupLabel="Изображение:"
            ></InputForm>
            <div class="footer_modal">
                <b-button
                    type="submit"
                    class="submit_btn"
                    @click="FormStore.formsValidation(newsTitle, newsText, picture, themes, 'edit', FormStore.edit_post_id)" 
                    >Редактировать</b-button
                >
                <b-button
                    type="submit"
                    @click="FormStore.deleteNews(FormStore.edit_post_id)"
                    class="submit_btn"
                >
                    Удалить
                </b-button>
            </div>
        </b-form>
    </div>
</template>

<style scoped>


h4 {
    color: #868686;
}

.footer_modal {
    display: flex;
    justify-content: space-between;
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
    cursor: pointer;
}

.close:hover,
.close:focus {
    color: black;
    text-decoration: none;
    cursor: pointer;
}
</style>
