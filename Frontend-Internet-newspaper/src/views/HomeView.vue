<script setup>
import axios from "axios"

import { ref, computed } from "vue"

// .email("aleksandrov@yandex.ru")
//                     .name("Yurii")
//                     .surname("Aleksandrov")
//                     .password(passwordEncoder.encode("aleksandrov"))
//                     .roles(adminRole)
//                     .build());

import router from "@/router/index.js"

const signout = async () => {
    console.log("success")

    const jwtToken = localStorage.getItem("AccessToken")
    console.log(jwtToken)

    try {
        const response = await fetch("http://localhost:8085/auth/signout", {
            method: "POST",
            headers: {
                Authorization: `Bearer ${jwtToken}`,
            },
        })

        if (!response.ok) {
            throw new Error("Authentication failed")
        }

        const responseData = await response.text()

        console.log(responseData)
        localStorage.removeItem('AccessToken'); 
        router.push("/auth/sign-in")
    } catch (error) {
        console.error("OUT error:", error)
    }
}

// const name = localStorage.getItem("name")
</script>

<template>
    <div>
        <b-button  @click="signout()"> Назад </b-button>
        <h1>Снова здарвствуйте,</h1>
    </div>
</template>
