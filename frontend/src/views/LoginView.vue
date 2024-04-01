<script setup lang="ts">
import ValidatedInput from "@/components/ValidatedInput.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import LoadingCircle from "@/components/LoadingCircle.vue";

import { reactive, ref, toRaw } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { email, minLength, required } from "@vuelidate/validators";

import { useAuthenticationStore } from "@/stores/authentication";
import { useNotificationStore } from "@/stores/notification";
import { useExecutablePromise } from "@/composables/promise";
import type { AxiosError } from "axios";
import { isAxiosError } from "axios";

import router from "@/router";

const { addNotification } = useNotificationStore();

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

    const formData = toRaw(loginData);
    executeLoginRequest(formData)
        .then(() => {
            formElement.value?.reset();
            router.push({ name: "home" });
        })
        .catch((err: Error | AxiosError) => {
            let message = "Unkown error.";
            if (isAxiosError(err)) {
                switch (err.response?.status) {
                    case 401:
                        message = "Could not log in. Bad credentials.";
                        break;
                }
            }

            addNotification({
                message: "Could not log in. " + message,
                type: "error",
            });
        });
}
</script>

<template>
    <main>
        <LoadingCircle :loading="loading" />
        <div id="login-container">
            <div class="card">
                <h3>Log in</h3>
                <form ref="formElement" id="login-form" @submit.prevent="login">
                    <div id="inputs-container">
                        <ValidatedInput
                            id="email"
                            type="text"
                            v-model="loginData.email"
                            :validator="v$.email"
                            label="Email"
                        />
                        <ValidatedInput
                            id="password"
                            type="password"
                            v-model="loginData.password"
                            :validator="v$.password"
                            label="Password"
                        />
                    </div>
                    <div id="login-buttons-container">
                        <ButtonComponent
                            id="login-button"
                            type="submit"
                            rounded
                            large
                            filled
                        >
                            Log in
                        </ButtonComponent>
                        <RouterLink to="register" id="not-registered-message">
                            Not registered yet? Register here.
                        </RouterLink>
                    </div>
                </form>
            </div>
        </div>
    </main>
</template>

<style scoped>
#login-container {
    margin-top: 5rem;
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
