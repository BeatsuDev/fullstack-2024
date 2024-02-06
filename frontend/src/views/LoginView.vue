<script setup lang="ts">
import { ref, reactive, toRaw } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { required, email, minLength } from "@vuelidate/validators";
import ValidatedInput from "@/components/ValidatedInput.vue";
import router from "@/router";

const formElement = ref<HTMLFormElement | null>(null);
const loginData = reactive({
    email: "",
    password: "",
});

const formRules = {
    email: { required, email },
    password: { required, minLength: minLength(8) },
};

const v$ = useVuelidate(formRules, loginData);

async function login() {
    if (!formElement.value) return;

    const isValid = await v$.value.$validate();
    if (!isValid) return;

    const data = toRaw(loginData);
    formElement.value.reset();

    // TODO: Send login request to the server
    console.log("Log in data:", data);
    setTimeout(() => alert("Logged in successfully!"), 200);

    router.push({ name: "home" });
}
</script>

<template>
    <main>
        <div id="login-container">
            <h1>{{ $t("login.title") }}</h1>
            <form ref="formElement" id="login-form" @submit.prevent="login">
                <div id="inputs-container">
                    <ValidatedInput
                        id="email"
                        type="text"
                        v-model="loginData.email"
                        :validator="v$.email"
                        :label="$t('login.email')"
                    />
                    <ValidatedInput
                        id="password"
                        type="password"
                        v-model="loginData.password"
                        :validator="v$.password"
                        :label="$t('login.password')"
                    />
                </div>
                <div id="login-buttons-container">
                    <button id="login-button" type="submit">
                        {{ $t("login.login") }}
                    </button>
                    <RouterLink to="register" id="not-registered-message">
                        {{ $t("login.notRegistered") }}
                    </RouterLink>
                </div>
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

#login-form {
    display: flex;
    flex-direction: column;
    gap: 1em;
}

#login-form > div {
    display: flex;
    flex-direction: column;
    gap: 0.5em;
}

#login-form input {
    padding: 0.5rem;
    font-size: 1em;
}

#login-button {
    border: none;
    border-radius: 0.25rem;
    padding: 1em 0.5em;
    width: 100%;
    background-color: var(--gray-700);
    transition: background-color 100ms;
    font-size: 1em;
    color: white;
    cursor: pointer;
}

#login-button:hover {
    background-color: var(--gray-800);
}

#not-registered-message {
    font-size: 0.8em;
    margin-top: 0.1em;
    font-style: italic;
    text-decoration: underline;

    color: var(--gray-600);
}
</style>
