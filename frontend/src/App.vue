<script setup lang="ts">
import { RouterLink, RouterView } from "vue-router";
import NotificationsContainer from "@/components/NotificationsContainer.vue";
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
    <main>
        <NotificationsContainer />
        <RouterView v-slot="{ Component, route }">
            <Transition
                :name="
                    (route.meta.transitionName as string | undefined) || 'fade'
                "
                mode="out-in"
            >
                <Component :is="Component" />
            </Transition>
        </RouterView>
    </main>
</template>

<style scoped>
main {
    position: relative;
    flex: 1;
}

#navigation-bar {
    display: flex;
    align-items: center;
    flex-direction: row;
    padding: 1rem 1.5rem;
    color: white;
    background-color: var(--primary-800);
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
    background-color: var(--primary-700);
    color: white;
    border: none;
    cursor: pointer;
}

#locale-selector:focus {
    outline: none;
}

.fade-enter-active,
.fade-leave-active {
    transition: all 60ms cubic-bezier(0, 0.2, 0.75, 0.75);
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}

.slide-left-enter-active,
.slide-left-leave-active {
    transition: all 130ms cubic-bezier(0, 0.2, 0.75, 0.75);
}

.slide-right-enter-from,
.slide-left-leave-to {
    transform: translateX(-20px);
    opacity: 0;
}

.slide-right-enter-active,
.slide-right-leave-active {
    transition: all 130ms cubic-bezier(0, 0.2, 0.75, 0.75);
}

.slide-left-enter-from,
.slide-right-leave-to {
    transform: translateX(20px);
    opacity: 0;
}
</style>
