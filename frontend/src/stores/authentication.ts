import { computed, reactive } from "vue";
import { defineStore } from "pinia";

import type { UserLogin, UserCreate } from "@/api";
import { UserApi } from "@/api";
import globalAxios from "axios";

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

    globalAxios.interceptors.request.use((response) => {
        setDeauthenticationTimer();
        return response;
    });

    const authenticated = computed(() => authenticationData.authenticated);

    function warnDeauthentication() {
        // TODO: Allow user to refresh token
        // TODO: Localization
        alert("You will be deauthenticated in 1 minute. Refresh?");

        clearTimeout(authenticationData.timer);
        authenticationData.timer = setTimeout(deauthenticate, 60 * 1000);
    }

    function setDeauthenticationTimer(seconds: number = 5 * 60) {
        if (authenticationData.timer) {
            clearTimeout(authenticationData.timer);
        }
        authenticationData.timer = setTimeout(
            warnDeauthentication,
            seconds * 1000
        );
    }

    function refresh(): ReturnType<typeof userApi.refreshToken> {
        const promise = userApi.refreshToken();

        promise
            .then(() => setDeauthenticationTimer(TIMEOUT_SECONDS))
            .catch(deauthenticate);

        return promise;
    }

    function deauthenticate() {
        if (authenticationData.timer) {
            clearTimeout(authenticationData.timer);
        }
        authenticationData.authenticated = false;
        authenticationData.timer = undefined;

        // TODO: Send a request to the server to remove the cookie
    }

    function authenticate(
        loginDetails: UserLogin
    ): ReturnType<typeof userApi.login> {
        const promise = userApi.login(loginDetails);

        promise
            .then(() => {
                authenticationData.authenticated = true;
                setDeauthenticationTimer();
            })
            .catch(deauthenticate);

        return promise;
    }

    function register(
        loginDetails: UserCreate
    ): ReturnType<typeof userApi.registerUser> {
        const promise = userApi.registerUser(loginDetails);

        promise
            .then(() => {
                authenticationData.authenticated = true;
                setDeauthenticationTimer();
            })
            .catch(deauthenticate);

        return promise;
    }

    return {
        refresh,
        deauthenticate,
        authenticate,
        register,
        authenticated,
    };
});
