<script setup>
import { ref } from "vue"

const positive = ref("")
const negative = ref("")

import {useNewsStore} from '@/stores/NewsStore.js'
const NewsStore = useNewsStore()

const clearThemes =()=>{
  positive.value = ""
  negative.value = ""
  NewsStore.getnews()
}

const filterThemes = ()=>{
  if (positive.value.length===0 && negative.value.length === 0) {
    alert('Укажите темы')
  } else {
    NewsStore.filterThemes(positive.value, negative.value)
  }
}

</script>

<template>
    <b-dropdown id="dropdown-form" text="Dropdown with form" ref="dropdown" class="m-2">
      <b-dropdown-form>
        <b-form-group label="positive" label-for="dropdown-form-positive">
          <b-form-input
            id="dropdown-form-positive"
            size="sm"
            placeholder="positive"
            v-model = "positive"
            style = "width: 200px;"
            @click.stop
          ></b-form-input>
        </b-form-group>
        <b-form-group label="negative" label-for="dropdown-form-negative">
          <b-form-input
            id="dropdown-form-negative"
            size="sm"
            placeholder="negative"
            v-model = "negative"
            style = "width: 200px;"
            @click.stop
          ></b-form-input>
        </b-form-group>
        <b-button variant="primary" size="sm" @click="filterThemes()">Filter</b-button>
        <b-button variant="danger" size="sm" @click="clearThemes()">Clear</b-button>
      </b-dropdown-form>
    </b-dropdown>
</template>

<style scoped></style>
