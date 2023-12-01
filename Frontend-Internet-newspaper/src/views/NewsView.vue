<script setup>
import MainBlock from "@/components/MainBlock.vue"
import PostBlock from "@/components/PostBlock.vue"



import { ref, onMounted } from "vue"

const news = ref([])

const getnews = async () => {
    try {
        const response = await fetch("http://localhost:8085/news/fresh-news", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        })

        if (!response.ok) {
            throw new Error("Failed to fetch data")
        }

        const responseData = await response.json()

        console.log(responseData)
        console.log(responseData[0].picture.url)

        responseData.forEach((item) => {
            news.value.push(item)
            console.log(item)
        })
        console.log("Response data:", responseData)
    } catch (error) {
        console.error("Fetch error:", error)
    }
}

onMounted(() => {
    getnews()
})
</script>

<template>
    <main-block>
        <template #container>
            <div class="news_container">
                <post-block v-for="post in news" :post="post" :key="post.id"></post-block>
            </div>
        </template>
    </main-block>
</template>

<style scoped>
.news_container {
    display: flex;
    flex-direction: column;
    width: 55vw;
}
</style>
