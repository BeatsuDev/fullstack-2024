<script setup lang="ts">
import ValidatedInput from "@/components/ValidatedInput.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

import { RouterLink } from "vue-router";
import { ref, reactive, computed, watch, toRaw } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { required, email, sameAs } from "@vuelidate/validators";
import { useExecutablePromise } from "@/utils/promise";
import { displayToast } from "@/utils/toast";

import { useAuthenticationStore } from "@/stores/authentication";
import { AxiosError } from "axios";

import router from "@/router";

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

watch(formData, (newData: typeof formData) => {
    console.log(newData);
});

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

            displayToast({
                title: "Register error",
                message: "Could not register. " + message,
                type: "error",
            });
        });
}
</script>

<template>
    <main>
        <LoadingCircle v-if="loading" />
        <div id="register-container">
            <h1>{{ $t("login.register") }}</h1>
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
                        :label="$t('login.name')"
                    />
                    <ValidatedInput
                        id="email"
                        type="text"
                        v-model="formData.email"
                        :validator="v$.email"
                        :label="$t('login.email')"
                    />
                    <ValidatedInput
                        id="password"
                        type="password"
                        v-model="formData.password"
                        :validator="v$.password"
                        :label="$t('login.password')"
                    />
                    <ValidatedInput
                        id="repeat-password"
                        type="password"
                        v-model="formData.repeatPassword"
                        :validator="v$.repeatPassword"
                        :label="$t('login.repeatPassword')"
                    />
                </div>
                <div id="register-buttons-container">
                    <ButtonComponent id="register-button" type="submit">
                        {{ $t("login.register") }}
                    </ButtonComponent>
                    <RouterLink to="login" id="registered-message">
                        {{ $t("login.alreadyRegistered") }}
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
