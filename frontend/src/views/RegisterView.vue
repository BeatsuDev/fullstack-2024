<script setup lang="ts">
import ValidatedInput from "@/components/ValidatedInput.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import LoadingCircle from "@/components/LoadingCircle.vue";

import { RouterLink } from "vue-router";
import { ref, reactive, computed, toRaw } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { required, email, sameAs } from "@vuelidate/validators";
import { useExecutablePromise } from "@/composables/promise";

import { useNotificationStore } from "@/stores/notification";
import { useAuthenticationStore } from "@/stores/authentication";
import { AxiosError } from "axios";

import router from "@/router";

const { addNotification } = useNotificationStore();

const formElement = ref<HTMLFormElement | null>(null);
const formData = reactive({
    name: "",
    email: "",
    password: "",
    repeatPassword: "",
});

const rules = {
    name: { required },
    email: { required, email },
    password: { required },
    repeatPassword: {
        required,
        sameAsPassword: sameAs(computed(() => formData.password)),
    },
};

const v$ = useVuelidate(rules, formData);
const authenticationStore = useAuthenticationStore();

const { loading, execute: executeRegister } = useExecutablePromise(
    authenticationStore.register
);

async function register() {
    if (!formElement.value) return;

    const isValid = await v$.value.$validate();
    if (!isValid) return;

    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const { repeatPassword, ...data } = toRaw(formData);

    executeRegister(data)
        .then(() => {
            formElement.value?.reset();
            router.push({ name: "home" });
        })
        .catch((err: Error | AxiosError) => {
            console.error("Error during login:", err);

            let message = "Unkown error.";
            if (err instanceof AxiosError) {
                switch (err.status) {
                    case 401:
                        message = "Could not register.";
                        break;
                }
            }

            addNotification({
                message: `Could not register. ${message}`,
                type: "error",
            });
        });
}
</script>

<template>
    <main>
        <LoadingCircle :loading="loading" />
        <div id="register-container">
            <h1>Register</h1>
            <form
                ref="formElement"
                id="register-form"
                @submit.prevent="register"
            >
                <div id="inputs-container">
                    <ValidatedInput
                        id="name"
                        type="text"
                        v-model="formData.name"
                        :validator="v$.name"
                        label="Name"
                    />
                    <ValidatedInput
                        id="email"
                        type="text"
                        v-model="formData.email"
                        :validator="v$.email"
                        label="Email"
                    />
                    <ValidatedInput
                        id="password"
                        type="password"
                        v-model="formData.password"
                        :validator="v$.password"
                        label="Password"
                    />
                    <ValidatedInput
                        id="repeat-password"
                        type="password"
                        v-model="formData.repeatPassword"
                        :validator="v$.repeatPassword"
                        label="Repeat password"
                    />
                </div>
                <div id="register-buttons-container">
                    <ButtonComponent
                        id="register-button"
                        type="submit"
                        rounded
                        large
                        filled
                    >
                        Register
                    </ButtonComponent>
                    <RouterLink to="login" id="registered-message">
                        Already registered? Log in here.
                    </RouterLink>
                </div>
            </form>
        </div>
    </main>
</template>

<style scoped>
#register-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

#register-form {
    width: 30%;
    max-width: 22rem;
    min-width: 17rem;
}

@media screen and (max-width: 768px) {
    #register-form {
        max-width: 90%;
        width: 90%;
    }
}

#register-form {
    display: flex;
    flex-direction: column;
    gap: 1em;
}

#register-form > div {
    display: flex;
    flex-direction: column;
    gap: 0.5em;
}

#register-form input {
    padding: 0.5rem;
    font-size: 1em;
}

#register-button {
    width: 100%;
}

#registered-message {
    font-size: 0.8em;
    margin-top: 0.1em;
    font-style: italic;
    text-decoration: underline;

    color: var(--gray-600);
}
</style>
