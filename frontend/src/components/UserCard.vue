<template>
    <div>
        <h3>{{ props.value.username }}</h3>
        <p>{{ props.value.email }}</p>
        <ButtonComponent
            v-if="props.editable"
            filled
            @click="emit('delete', value)"


            >Delete</ButtonComponent>
    </div>

</template>
<script lang="ts" setup>
import ButtonComponent from "./ButtonComponent.vue";
import type { User } from "../api/models/user";
const props = defineProps<{
    value: User;
    editable?: boolean;
}>();

const emit = defineEmits<{
    (event: "delete", value: User): void;
}>();
</script>