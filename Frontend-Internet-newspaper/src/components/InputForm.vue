<script setup>
import { computed } from "vue"

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
        <div class="iput_container">
            <b-form-textarea
                v-if="textArea"
                v-model="localComputed"
                :placeholder="placeholder"
                type="text"
                :state="validation_title"
                rows="15"
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
            <span
                class="close"
                @click="localComputed = ''"
                >&times;
            </span>
        </div>
    </b-form-group>
</template>

<style scoped>
.iput_container {
    display: flex;
    flex-direction: row;
    gap: 10px;
    cursor: pointer;
    align-items: center;
}
.close {
    color: #aaa;
    font-size: 28px;
    font-weight: bold;
}
</style>
