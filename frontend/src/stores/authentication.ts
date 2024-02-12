import { ref, computed, reactive } from "vue";
import { defineStore } from "pinia";

import type { UserLogin } from "@/api";
import { UserApi } from "@/api";

type AuthenticationData = {
    authenticated: boolean;
    timer?: ReturnType<typeof setTimeout>;
};

export const useAuthenticationStore = defineStore("authentication", () => {
    const TIMEOUT_SECONDS = 5 * 60;
    const userApi = new UserApi();

    const authenticationData: AuthenticationData = reactive({
        authenticated: false,
        timer: undefined,
    });

    const authenticated = computed(() => authenticationData.authenticated);

    function warnDeauthentication() {
        // TODO: Allow user to refresh token
        // TODO: Localization
        alert("You will be deauthenticated in 1 minute. Refresh?");

        clearTimeout(authenticationData.timer);
        authenticationData.timer = setTimeout(deauthenticate, 60 * 1000);
    }

    function setTimer(seconds: number = 5 * 60) {
        if (authenticationData.timer) {
            clearTimeout(authenticationData.timer);
        }
        authenticationData.timer = setTimeout(
            warnDeauthentication,
            seconds * 1000
        );
    }

    function refresh(seconds: number = 5 * 60): {
        authenticationData: AuthenticationData;
        error: any;
    } {
        const error = ref<any>(null);

        userApi
            .refreshToken()
            .then(() => setTimer(seconds))
            .catch((err) => {
                error.value = err;
                deauthenticate();

                // TODO: Localization
                alert("Failed to refresh token.");
            });

        return { authenticationData, error };
    }

    function deauthenticate() {
        if (authenticationData.timer) {
            clearTimeout(authenticationData.timer);
        }
        authenticationData.authenticated = false;
        authenticationData.timer = undefined;

        // TODO: Send a request to the server to remove the cookie
    }

    function authenticate(loginDetails: UserLogin) {
        const error = ref<any>(null);

        userApi
            .login(loginDetails)
            .then(() => {
                authenticationData.authenticated = true;
                authenticationData.timer = setTimeout(
                    warnDeauthentication,
                    TIMEOUT_SECONDS * 1000
                );
            })
            .catch((err) => {
                error.value = err;
                alert("Failed to authenticate");
            });

        return { authenticationData, error };
    }

    return { refresh, deauthenticate, authenticate, authenticated };
});
