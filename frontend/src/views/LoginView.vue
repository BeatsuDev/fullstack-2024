<script setup lang="ts">
import { ref, reactive, computed, toRaw } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { required, email, minLength } from "@vuelidate/validators";


const formElement = ref<HTMLFormElement | null>(null);
const loginData = reactive({
    email: "",
    password: "",
});

const formRules = {
    email: { required, email },
    password: { required, minLength: minLength(8) },
};

const emit = defineEmits<{
    submit: [formData: typeof loginData]
}>();

const v$ = useVuelidate(formRules, loginData);

async function login() {
    if (!formElement.value) return;

    const isValid = await v$.value.$validate();
    if (!isValid) return;

    emit("submit", toRaw(loginData));
}
</script>

<template>
    <main>
        <div id="login-container">
            <h1>{{ $t("login.title") }}</h1>
            <form ref="formElement" id="login-form" @submit.prevent="login">
                <div id="email-input-container" class="input-container" @input="v$.email.$reset()" @focusout="v$.email.$validate()">
                    <label for="email">{{ $t("login.email") }}</label>
                    <input id="email" type="text" v-model="loginData.email" :class="{ 'border-red': v$.email.$error }"/>
                    <span v-if="v$.email.$error" class="error-message">{{ v$.email.$errors[0].$message }}</span>
                </div>
                <div id="password-input-container" class="input-container" @input="v$.password.$reset()" @focusout="v$.password.$validate()">
                    <label for="password">{{ $t("login.password") }}</label>
                    <input id="password" type="password" v-model="loginData.password" :class="{ 'border-red': v$.password.$error }"/>
                    <span v-if="v$.password.$error" class="error-message">{{ v$.password.$errors[0].$message }}</span>
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
    width: 30%;
    max-width: 22rem;
    min-width: 17rem;
}

@media screen and (max-width: 768px) {
    #login-form {
        max-width: 90%;
        width: 90%;
    }   
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

.error-message {
    color: red;
    margin-left: 0.2em;
    font-size: 0.8em;
    font-style: italic;
}

.border-red {
    border: 1px solid red;
}
</style>