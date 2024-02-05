<script setup lang="ts">
import { ref, reactive } from "vue";


const formElement = ref<HTMLFormElement | null>(null);
const loginData = reactive({
    email: "",
    password: "",
});

function login() {
    
}
</script>

<template>
    <main>
        <div id="login-container">
            <h1>{{ $t("login.title") }}</h1>
            <form ref="formElement" id="login-form" @submit.prevent="login">
                <div id="email-input-container" class="input-container">
                    <label for="email">{{ $t("login.email") }}</label>
                    <input id="email" type="text" v-model="loginData.email" />
                </div>
                <div id="password-input-container" class="input-container">
                    <label for="password">{{ $t("login.password") }}</label>
                    <input id="password" type="password" v-model="loginData.password" />
                </div>
                <button id="login-button" type="submit">{{ $t("login.login") }}</button>
            </form>
        </div>
    </main>
</template>

<style scoped>
#login-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

#login-form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    width: 20%;
}

#login-form input {
    padding: 0.5rem;
    font-size: 1em;
}

.input-container {
    display: flex;
    flex-direction: column;
    gap: 0.2em;
}

#login-button {
    padding: 1rem 0.5rem;
    font-size: 1em;
    background-color: var(--gray-700);
    color: white;
    border: none;
    border-radius: 0.25rem;
    cursor: pointer;
    transition: background-color 100ms;
    width: 60%;
    margin: 0 auto;
}

#login-button:hover {
    background-color: var(--gray-800);
}
</style>