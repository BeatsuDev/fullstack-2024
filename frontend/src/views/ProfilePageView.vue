<script setup lang="ts">
import { reactive, computed, onMounted } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { email, sameAs } from "@vuelidate/validators";
import { useAuthenticationStore } from "@/stores/authentication";

import ValidatedInput from "@/components/ValidatedInput.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";

const authenticationStore = useAuthenticationStore();

const formData = reactive({
    name: "",
    email: "",
    password: "",
});

const rules = {
    name: {},
    email: { email },
    password: {},
};

const v$ = useVuelidate(rules, formData);

onMounted(() => {
    formData.name = authenticationStore.loggedInUser?.name || "";
    formData.email = authenticationStore.loggedInUser?.email || "";
});
</script>

<template>
    <main class="profile-page-container">
        <h1>{{ $t("profile.title") }}</h1>
        <p>{{ $t("profile.information") }}</p>

        <form id="change-values-container" @submit.prevent="">
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
            <ButtonComponent
                id="change-values-button"
                type="submit"
                rounded
                large
                filled
            >
                {{ $t("profile.changeValues") }}
            </ButtonComponent>
        </form>
    </main>
</template>

<style scoped>
main {
    height: 100%;
}

.profile-page-container > h1 {
    width: 100%;
    text-align: center;
}

.profile-page-container {
    display: flex;
    flex-direction: column;

    margin: 0 auto;
    width: 40%;
}

#change-values-container {
    max-width: 30em;
    width: 60%;
    margin: 0 auto;

    display: flex;
    flex-direction: column;
    gap: 1.5em;
}

#change-values-button {
    margin: 0 auto;
    margin-top: 1em;
}

@media screen and (max-width: 1280px) {
    .profile-page-container {
        width: 60%;
    }

    #change-values-container {
        width: 80%;
    }
}

@media screen and (max-width: 768px) {
    .profile-page-container {
        width: 90%;
    }

    #change-values-container {
        width: 100%;
    }
}
</style>
