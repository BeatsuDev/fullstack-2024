<script setup lang="ts">
import ValidatedInput from "@/components/ValidatedInput.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import LoadingCircle from "@/components/LoadingCircle.vue";

import { ref, reactive, toRaw } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { required, email, minLength } from "@vuelidate/validators";

import { useAuthenticationStore } from "@/stores/authentication";
import { useExecutablePromise } from "@/utils/promise";
import { displayToast } from "@/utils/toast";
import { AxiosError } from "axios";

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
const authenticationStore = useAuthenticationStore();

const { loading, execute: executeLoginRequest } = useExecutablePromise(
    authenticationStore.authenticate
);

async function login() {
    if (!formElement.value) return;

    const isValid = await v$.value.$validate();
    if (!isValid) return;

    function onError(err: Error | AxiosError) {
        console.error("Error during login:", err);

        var message = "";
        if (err instanceof AxiosError) {
            switch (err.status) {
                case 401:
                    message = "Could not log in. Bad credentials.";
                    break;
            }
        }

        displayToast({
            title: "Login error",
            message: "Could not log in. " + message || "Unkown error.",
            type: "error",
        });
    }

    function onSuccess() {
        formElement.value?.reset();
        router.push({ name: "home" });
    }

    const formData = toRaw(loginData);
    executeLoginRequest(formData).then(onSuccess).catch(onError);
}
</script>

<template>
    <main>
        <LoadingCircle v-if="loading" />
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
                    <ButtonComponent id="login-button" type="submit">
                        {{ $t("login.login") }}
                    </ButtonComponent>
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
    width: 100%;
}

#not-registered-message {
    font-size: 0.8em;
    margin-top: 0.1em;
    font-style: italic;
    text-decoration: underline;

    color: var(--gray-600);
}
</style>
@/utils/promise @/utils/toast
