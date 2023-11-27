<script setup></script>

<template>
    <b-navbar
        toggleable="md"
        sticky="top"
        style = "box-shadow: 0px 13px 4px rgba(0, 0, 0, 0.1);"
    >
        <b-navbar-brand
            href="#"
            to="/"
            style = "font-weight: bold;"
            >Gazeta.ru</b-navbar-brand
        >
        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
        <b-collapse
            id="nav-collapse"
            is-nav
        >
        <!-- добавить активность переключателя -->
            <b-navbar-nav>
                <b-nav-item to="/auth/signin" >Sign In</b-nav-item>
                <b-nav-item to="/auth/signup">Sign Up</b-nav-item>
            </b-navbar-nav>
        </b-collapse>
    </b-navbar>
</template>

<style>

.navbar-expand-md .navbar-collapse {
    justify-content: flex-end;
}

</style>
