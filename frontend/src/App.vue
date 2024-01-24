<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
</script>

<template>
    <header>
        <nav id="navigation-bar">
            <div id="company-name">{{ $t("company.name") }}</div>
            <div id="routes">
                <RouterLink to="">{{ $t("navbar.about") }}</RouterLink>
                <RouterLink to="">{{ $t("navbar.contact") }}</RouterLink>
                <RouterLink to="">{{ $t("navbar.login") }}</RouterLink>
                <select v-model="$i18n.locale">
                    <option v-for="locale in $i18n.availableLocales" :key="`locale-${locale}`" :value="locale">{{ locale }}</option>
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
    color: white;
    text-decoration: none;
    transition: color 50ms;
}

#routes > a:hover {
    color: var(--gray-500);
}

#routes {
    flex: 1;
    display: flex;
    justify-content: end;
    gap: 1.5rem;
}
</style>