import { computed, reactive } from "vue";
import { defineStore } from "pinia";

import type { User, UserLogin, UserCreate } from "@/api";
import { UserApi, UserSessionApi } from "@/api";
import globalAxios from "axios";

import router from "@/router";

type AuthenticationData = {
    authenticated: boolean;
    user?: User;
    timer?: ReturnType<typeof setTimeout>;
};

export const useAuthenticationStore = defineStore("authentication", () => {
    const TIMEOUT_SECONDS = 5 * 60;

    const userApi = new UserApi();
    const userSessionApi = new UserSessionApi();

    const authenticationData: AuthenticationData = reactive({
        authenticated: false,
        user: undefined,
        timer: undefined,
    });

    globalAxios.defaults.withCredentials = true;
    globalAxios.interceptors.request.use((response) => {
        setDeauthenticationTimer();
        if (response.status == 401) {
            deauthenticate();
        }
        return response;
    });

    const authenticated = computed(() => authenticationData.authenticated);
    const loggedInUser = computed(() => authenticationData.user);

    function warnDeauthentication() {
        // TODO: Allow user to refresh token
        // TODO: Localization
        clearTimeout(authenticationData.timer);
        authenticationData.timer = setTimeout(deauthenticate, 60 * 1000);

        // Give the previous lines of code time to run
        setTimeout(
            alert,
            500,
            "You will be deauthenticated in 1 minute. Refresh?"
        );
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

    async function refresh(): Promise<ReturnType<typeof userSessionApi.loggedInUser>> {
        const promise = userSessionApi.loggedInUser();

        await promise
            .then((user) => {
                authenticationData.authenticated = true;
                authenticationData.user = user.data;
                setDeauthenticationTimer(TIMEOUT_SECONDS);
            })
            .catch(deauthenticate);

        return promise;
    }

    function deauthenticate() {
        const promise = userSessionApi.logout();

        function clearAuthenticationData() {
            if (authenticationData.timer) {
                clearTimeout(authenticationData.timer);
            }
            authenticationData.authenticated = false;
            authenticationData.timer = undefined;
            if (authenticationData.user) {

                if (router.currentRoute.value.meta?.requiresAuth) {
                    router.push({ name: "login" });
                }
                location.reload()
            }
            delete authenticationData.user ;
        }

        promise.then(clearAuthenticationData).catch((error) => {
                clearAuthenticationData();
        });

        return promise;
    }

    function authenticate(
        loginDetails: UserLogin,
        options?: Parameters<typeof userSessionApi.login>[1]
    ): ReturnType<typeof userSessionApi.login> {
        const promise = userSessionApi.login(loginDetails, options);

        promise
            .then((response) => {
                authenticationData.authenticated = true;
                authenticationData.user = response.data;
            })
            .catch(deauthenticate);

        return promise;
    }

    function register(
        loginDetails: UserCreate,
        options?: Parameters<typeof userApi.registerUser>[1]
    ): ReturnType<typeof userApi.registerUser> {
        const promise = userApi.registerUser(loginDetails, options);

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
        loggedInUser,
    };
});
