<template>
    <GenericModal title="Who are you?" unclosable v-model="active">
        <form @submit.prevent="createAnonymousUser">
            <p>To play, please tell us your name!</p>
            <input v-model="name" id="username" />
            <button>Submit</button>
        </form>
    </GenericModal>
</template>

<script lang="ts" setup>
import { UserApi, type User, type UserCreate } from "@/api";
import GenericModal from "@/components/GenericModal.vue";
import { ref } from "vue";

const active = defineModel({
    required: true,
});
const name = ref("");

const emit = defineEmits<{
    (e: "updateUser", quiz: User): void;
}>();

const userApi = new UserApi();
async function createAnonymousUser() {
    const user = {
        name: name.value,
        email: "anonymous@example.org",
        password: "password",
    } satisfies UserCreate;
    userApi
        .registerUser(user, true)
        .then((data) => data.data)
        .then((user) => emit("updateUser", user));
}
</script>
