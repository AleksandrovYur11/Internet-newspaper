<script setup>
import NavBar from "@/components/NavBar.vue"
import FormBlock from "@/components/FormBlock.vue"
</script>

<template>
    <div class="main_container">
        <header>
            <slot name="header">
                <!-- <nav-bar ></nav-bar> -->
            </slot>
        </header>
        <div class="cont">
            <!-- <h1>Газета МАЯК</h1> -->
            <slot name="cont">
                <!-- <form-block></form-block> -->
            </slot>
        </div>
        <footer class="footer">
            <slot name="footer"> </slot>
        </footer>
    </div>
</template>

<style>


.main_container {
    min-height: 100vh;
    width: 100%;
    display: flex;
    flex-direction: column;
    margin: 0;
}

.cont {
    flex: 1;
    background-color: #a298d3;
    display: flex;
    justify-content: center;
    align-items: center;

    margin: 0;
    padding: 0; 

    /* background-image: url('@/assets/main_bg.jpg');
    background-repeat: no-repeat;
    background-size: cover; */

    background: linear-gradient(to bottom, #d5e6f3 20%, #a8c2d5 35%, #8aa4ba 55%, #060912 95%);

    flex-direction: column;
}

.cont h1 {
    margin-bottom: 40px;
    color: #3157CC;
}

.footer {
    background-color: #1a191f;
}
</style>
