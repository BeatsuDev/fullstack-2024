<script setup lang="ts">
import { ref, reactive, computed, toRaw } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { required, email, minLength } from "@vuelidate/validators";
import ValidatedInput from "@/components/ValidatedInput.vue";


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
</style>