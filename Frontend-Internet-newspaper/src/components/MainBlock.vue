<script setup></script>

<template>
    <div class="main_container bg">
        <div class="cont">
            <b-form class="custom_form">
                <div class="custom_form_header">
                    <span class="custom_form_logo">
                    </span>
                    <h4>
                        <a href="/news/fresh-news">МАЯК</a>
                    </h4>
                    <p>современная интернет-газета</p>
                </div>
                <div class="custom_form_content">
                    <slot name="content"></slot>
                </div>
                <slot name="btn"></slot>
                <p class = "p_bottom">
                    <slot name="toggle"></slot>
                </p>
            </b-form>
        </div>
    </div>
</template>


<style scoped>
.bg {
    background-image: url("@/assets/main_bg.jpg");
    background-repeat: no-repeat;
    background-size: cover;
}

h4 {
    color: #007bff;
    margin-top: 12px;
    margin-bottom: 0;
}

p {
    color: #465e78;
    font-size: 16px;
    text-align: center;
}

a {
    cursor: pointer;
}

.p_bottom {
    margin-top: 30px;
}

.custom_form_header {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.custom_form {
    color: #007bff;
    background-color: #ffffff;
    min-height: 650px;
    min-width: 500px;
    border-radius: 15px;
    padding: 25px 50px 0px 50px;
    display: flex;
    flex-direction: column;
    position: relative;
    box-shadow: 10px 12px 4px rgba(0, 0, 0, 0.1);
    justify-content: space-between;
}

.custom_form_content {
    display: flex;
    align-items: stretch;
    flex-direction: column;
}

.custom_form_logo {
    background-image: url("@/assets/logo.jpg");
    width: 90px;
    height: 90px;
    border-radius: 50%;
    background-size: cover;
}


</style>
