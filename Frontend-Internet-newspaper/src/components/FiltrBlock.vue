<script setup>
import { ref, computed } from "vue"

import { useNewsStore } from "@/stores/NewsStore.js"
const NewsStore = useNewsStore()

const positive = ref("")
const negative = ref("")


const setPositiveTheme = computed(()=>{
    return positive.value = NewsStore.positive
})

const changePositive=(theme)=>{
    positive.value = theme
}

const clearThemes = () => {
    positive.value = ""
    negative.value = ""
    NewsStore.getnews()
}

const filterThemes = () => {
    if (positive.value.length === 0 && negative.value.length === 0) {
        alert("Укажите темы")
    } else {
        NewsStore.filterThemes(positive.value, negative.value)
    }
}
</script>

<template>
    <b-dropdown
        text="Поиск по темам"
        ref="dropdown"
        class="m-2"
        variant="outline-primary"
        dropleft
    >
        <b-dropdown-form style = "width: 300px;" >
            <b-form-group class="mb-2">
                <b-form-input
                    class="input_theme"
                    id="dropdown-form-positive"
                    size="sm"
                    placeholder="Добавить"
                    @change="changePositive"
                    v-model="setPositiveTheme"
                    @click.stop
                ></b-form-input>
            </b-form-group>
            <b-form-group class="mb-2">
                <b-form-input
                    class="input_theme"
                    id="dropdown-form-negative"
                    size="sm"
                    placeholder="Исключить"
                    v-model="negative"
                    @click.stop
                ></b-form-input>
            </b-form-group>
            <b-form-group>
                <div class="filtr_btn">
                    <b-button
                        variant="primary"
                        size="sm"
                        @click="filterThemes()"
                        >Искать</b-button
                    >
                    <b-button
                        size="sm"
                        @click="clearThemes()"
                        >Очистить</b-button
                    >
                </div>
            </b-form-group>
        </b-dropdown-form>
    </b-dropdown>
</template>

<style scoped>
.filtr_btn {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
}

</style>
