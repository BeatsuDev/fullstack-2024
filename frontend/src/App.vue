<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
</script>

<template>
    <header>
        <nav v-if="!$route.meta.hideNavbar" id="navigation-bar">
            <div id="company-name">{{ $t("company.name") }}</div>
            <div id="routes">
                <RouterLink id="about-router-link" to="">{{
                    $t("navbar.about")
                }}</RouterLink>
                <RouterLink id="contact-router-link" to="">{{
                    $t("navbar.contact")
                }}</RouterLink>
                <RouterLink id="login-router-link" to="login">{{
                    $t("navbar.login")
                }}</RouterLink>
                <select id="locale-selector" v-model="$i18n.locale">
                    <option
                        v-for="locale in $i18n.availableLocales"
                        :key="`locale-${locale}`"
                        :value="locale"
                    >
                        {{ locale }}
                    </option>
                </select>
            </div>
        </nav>
    </header>
    <RouterView />
</template>

<style scoped>
#navigation-bar {
    display: flex;
    align-items: center;
    flex-direction: row;
    padding: 1rem 1.5rem;
    color: white;
    background-color: var(--gray-800);
    font-size: 1.25em;
}

#company-name {
    font-size: 1.5em;
}

#routes > a {
    position: relative;
    color: var(--gray-200);
    text-decoration: none;
    transition: color 50ms;
}

#routes > a:hover {
    color: white;
}

#routes > a::before {
    content: "";
    display: block;
    position: absolute;
    width: 0;
    height: 2px;
    bottom: 0;
    border-radius: 2px;
    background-color: white;
    transition: width 100ms;
}

#routes > a:hover::before {
    width: 100%;
}

#routes {
    flex: 1;
    display: flex;
    justify-content: end;
    gap: 1.5rem;
}

#locale-selector {
    padding: 0.25em;
    font-size: 0.75em;
    border-radius: 0.25rem;
    background-color: var(--gray-700);
    color: white;
    border: none;
    cursor: pointer;
}

#locale-selector:focus {
    outline: none;
}
</style>
