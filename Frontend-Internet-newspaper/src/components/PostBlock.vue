<script setup>
import { defineProps } from "vue"
import { ref } from "vue"

const { props } = defineProps({
    post: {
        type: Object,
        required: true,
    },
})

const new_comment = ref("")


</script>

<template>
    <div class="news_container">
        <div class="themes">
            <span
                class="theme"
                v-for="(theme, index) in post.themes"
                :key="index"
            >
                # {{ theme.name }}
            </span>
        </div>
        <h3>{{ post.newsTitle }}</h3>
        <div class="post_body">
            <img
                :src="post.picture.url"
                style="margin-right: 30px"
            />
            <p>{{ post.newsText }}</p>
        </div>
        <br />
        <div class="footer">
            <div class="comment_header">
                <div class="likes_info">
                    <img
                        src="@/assets/default_like.png"
                        style="margin-right: 10px"
                    />
                    <span style="font-weight: bold">1200</span>
                </div>
                <b-button
                    v-b-toggle.collapse-1.collapse-2.collapse-3
                    class="comment_btn"
                    >Комментарии</b-button
                >
            </div>
            <b-collapse
                id="collapse-1"
                class="mt-2"
            >
                <b-card>I am collapsible content A!</b-card>
            </b-collapse>
            <b-collapse
                id="collapse-2"
                class="mt-2"
            >
                <b-card>I am collapsible content B!</b-card>
            </b-collapse>
            <b-collapse
                class="mt-2"
                id="collapse-3"
            >
                <div class="input_card">
                    <b-form-textarea
                        id="textarea-small"
                        size="sm"
                        placeholder="Ваш комментарий..."
                        v-model="new_comment"
                    ></b-form-textarea>
                    <img
                        src="@/assets/send.png"
                        style="
                            width: 24px;
                            height: 24px;
                            margin-left: 10px;
                            right: 0;
                        "
                    />
                </div>
            </b-collapse>
        </div>
    </div>
</template>

<style scoped>
img[data-v-9d1a3feb] {
    margin-right: 0px;
}

.footer {
    background-color: #fff;
}

.likes_info {
    display: flex;
    flex-direction: row;
    align-items: center;
}
.input_card {
    display: flex;
    flex-direction: row;
}
.comment_header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    background-color: #fff;
}
.news_container {
    background-color: #fff;
    color: #000;
    border-radius: 15px;
    padding: 20px 20px;
    /* margin: 30px; */
    margin-bottom: 30px;
    margin-top: 30px;
    display: flex;
    align-items: left;
}

img {
    /* height: 300px; */
    max-width: 200px;
    margin-right: 25px;
}

h3 {
    margin-top: 10px;
}

.themes {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
}
.theme {
    background-color: rgba(25, 189, 63, 0.545);
    padding: 4px 8px;
    border-radius: 15px;
    font-size: 12px;
    margin-right: 5px;
}

.comment_btn {
    padding: 4px 8px;
    border-radius: 15px;
    font-size: 12px;
}

.post_body {
    display: flex;
    align-items: flex-start;
    margin-top: 20px;
}

p {
    flex: 1;
}
</style>
