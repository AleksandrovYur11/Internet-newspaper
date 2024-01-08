<script setup>
import { ref, computed } from "vue"

const props = defineProps({
    modelValue: String,
    groupLabel: String,
    placeholder: String,
    textArea: String,
})
const emit = defineEmits(["update:modelValue"])

const localComputed = computed({
    get() {
        return props.modelValue
    },
    set(newValue) {
        emit("update:modelValue", newValue)
    },
})

</script>

<template>
    <b-form-group
        class="mb-1"
        :label="groupLabel"
    >
        <b-form-textarea
            v-if="textArea"
            v-model="localComputed"
            :placeholder="placeholder"
            type="text"
            :state="validation_title"
            required
        >
        </b-form-textarea>
        <b-form-input
            v-else
            v-model="localComputed"
            :placeholder="placeholder"
            type="text"
            :state="validation_title"
            required
        ></b-form-input>
    </b-form-group>
</template>
