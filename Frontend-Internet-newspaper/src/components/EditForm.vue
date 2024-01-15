<script setup>
import { ref, onBeforeMount } from "vue"

import InputForm from "@/components/InputForm.vue"

import { useFormsStore } from "@/stores/FormsStore.js"
const FormStore = useFormsStore()

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

const newsTitle = ref("")
const newsText = ref("")
const picture = ref("")
const themes = ref("")

onBeforeMount(async () => {
    await FormStore.getInfoNews(FormStore.edit_post_id)

    if (FormStore.news_for_edit) {
        newsTitle.value = FormStore.news_for_edit.newsTitle
        newsText.value = FormStore.news_for_edit.newsText
        picture.value = FormStore.news_for_edit.picture.url
        themes.value = FormStore.news_for_edit.themes
            .map((theme) => theme.name)
            .join(", ")
    }
})
</script>

<template>
    <div class="main_container modal_bg">
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
            />
            <InputForm
                v-model="themes"
                groupLabel="Темы:"
            />
            <InputForm
                v-model="newsText"
                groupLabel="Текст:"
                textArea="textArea"
            />
            <InputForm
                v-model="picture"
                groupLabel="Изображение:"
            />
            <div class="footer_modal">
                <b-button
                    type="submit"
                    class="submit_btn"
                    @click="
                        FormStore.formsValidation(
                            newsTitle,
                            newsText,
                            picture,
                            themes,
                            'edit',
                            FormStore.edit_post_id
                        )
                    "
                    variant="primary"
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
.footer_modal {
    display: flex;
    justify-content: space-between;
}
</style>
