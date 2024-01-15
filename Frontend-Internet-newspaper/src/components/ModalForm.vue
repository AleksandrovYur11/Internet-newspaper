<script setup>
import { ref } from "vue"

import InputForm from "@/components/InputForm.vue"

import { useNewsStore } from "@/stores/NewsStore"
const NewsStore = useNewsStore()

import { useFormsStore } from "@/stores/FormsStore.js"
const FormStore = useFormsStore()

const newsTitle = ref("")
const newsText = ref("")
const picture = ref("")
const themes = ref("")
</script>

<template>
    <div class="main_container modal_bg">
        <b-form class="custom_form">
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
            />
            <InputForm
                v-model="themes"
                groupLabel="Темы:"
                placeholder="природа, космос, IT..."
            />
            <InputForm
                v-model="newsText"
                groupLabel="Текст:"
                placeholder="Текст вашей новости"
                textArea="textArea"
            />
            <InputForm
                v-model="picture"
                groupLabel="Изображение:"
                placeholder=".jpeg .jpg .gif .png .bmp .svg"
            />
            <div class="footer_modal">
                <b-button
                    type="submit"
                    @click="
                        FormStore.formsValidation(
                            newsTitle,
                            newsText,
                            picture,
                            themes,
                            'modal'
                        )
                    "
                    class="submit_btn"
                    >Создать</b-button
                >
            </div>
        </b-form>
    </div>
</template>
