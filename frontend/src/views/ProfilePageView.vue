<script lang="ts" setup>
import { onMounted, reactive } from "vue";
import { useVuelidate } from "@vuelidate/core";
import { email } from "@vuelidate/validators";
import { useAuthenticationStore } from "@/stores/authentication";
import { useExecutablePromise } from "@/composables/promise";
import { UserApi } from "@/api";

import ValidatedInput from "@/components/ValidatedInput.vue";
import ButtonComponent from "@/components/ButtonComponent.vue";
import { exec } from "child_process";
import router from "@/router";
import { useNotificationStore } from "@/stores/notification";

const authenticationStore = useAuthenticationStore();

const formData = reactive({
    name: "",
    email: "",
});

const rules = {
    name: {},
    email: { email },
};

const v$ = useVuelidate(rules, formData);

onMounted(() => {
    formData.name = authenticationStore.loggedInUser?.name || "";
    formData.email = authenticationStore.loggedInUser?.email || "";
});

// Update logic
const { loading, error, execute } = useExecutablePromise(
    authenticationStore.updateUser
);

function updateUser() {
    if (v$.value.$invalid) {
        return;
    }

    if (!authenticationStore.loggedInUser) {
        authenticationStore.logout();
        router.push("/login");
        return;
    }

    execute({
        id: authenticationStore.loggedInUser.id,
        name: formData.name,
        email: formData.email,
    }).then(() => {
        useNotificationStore().addNotification({
            message: "Profile updated successfully",
            type: "success",
        });
    });
}
</script>

<template>
    <main class="profile-page-container">
        <div class="card" style="margin-top: 5rem">
            <h3>Edit your profile</h3>
            <p>Change your user information here.</p>

            <form id="change-values-container" @submit.prevent="updateUser">
                <ValidatedInput
                    id="name"
                    v-model="formData.name"
                    :validator="v$.name"
                    label="Name"
                    type="text"
                />
                <ValidatedInput
                    id="email"
                    v-model="formData.email"
                    :validator="v$.email"
                    label="Email"
                    type="text"
                />
                <ButtonComponent
                    id="change-values-button"
                    filled
                    large
                    rounded
                    type="submit"
                >
                    Edit profile
                </ButtonComponent>
            </form>
            <span v-if="loading">Loading...</span>
            <span v-if="error">{{ error }}</span>
        </div>
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
