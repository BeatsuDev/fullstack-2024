<script setup lang="ts">
import { reactive, computed } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { email, sameAs } from "@vuelidate/validators";

import ValidatedInput from "@/components/ValidatedInput.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

const formData = reactive({
    name: "",
    email: "",
    password: "",
    repeatPassword: "",
});

const rules = {
    name: {},
    email: { email },
    password: {},
    repeatPassword: {
        sameAsPassword: sameAs(computed(() => formData.password)),
    },
};

const v$ = useVuelidate(rules, formData);
</script>

<template>
    <div id="profile-page-container">
        <h1>{{ $t("profile.title") }}</h1>
        <p>{{ $t("profile.information") }}</p>

        <div id="change-values-container">
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
                id="password"
                type="password"
                v-model="formData.password"
                :validator="v$.password"
                :label="$t('login.repeatPassword')"
            />
        </div>

        <ButtonComponent id="change-values-button" type="submit">
            {{ $t("profile.changeValues") }}
        </ButtonComponent>
    </div>
</template>

<style scoped>
#profile-page-container {
    width: 40%;
    margin: 0 auto;

    display: flex;
    flex-direction: column;
}

#change-values-container {
    width: 60%;
    margin: 0 auto;

    display: flex;
    flex-direction: column;
    gap: 0.5em;
}

#change-values-button {
    margin: 0 auto;
    margin-top: 1em;
}
</style>
