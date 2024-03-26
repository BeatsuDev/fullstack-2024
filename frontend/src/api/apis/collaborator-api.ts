/* tslint:disable */
/* eslint-disable */
/**
 * Kazoot
 * An application
 *
 * OpenAPI spec version: 1.0.0
 * Contact: vkbugge@hotmail.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

import globalAxios, { AxiosResponse, AxiosInstance, AxiosRequestConfig } from 'axios';
import { Configuration } from '../configuration';
// Some imports not used depending on template conditions
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, RequestArgs, BaseAPI, RequiredError } from '../base';
import { QuizAddCollaborator } from '../models';
import { User } from '../models';
/**
 * CollaboratorApi - axios parameter creator
 * @export
 */
export const CollaboratorApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * Add a collaborator to a quiz
         * @param {string} id The ID of a quiz
         * @param {QuizAddCollaborator} [body] Collaborator information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        addCollaborator: async (id: string, body?: QuizAddCollaborator, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            if (id === null || id === undefined) {
                throw new RequiredError('id','Required parameter id was null or undefined when calling addCollaborator.');
            }
            const localVarPath = `/quiz/{id}/collaborator`
                .replace(`{${"id"}}`, encodeURIComponent(String(id)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, 'https://example.com');
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }
            const localVarRequestOptions :AxiosRequestConfig = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            localVarHeaderParameter['Content-Type'] = 'application/json';

            const query = new URLSearchParams(localVarUrlObj.search);
            for (const key in localVarQueryParameter) {
                query.set(key, localVarQueryParameter[key]);
            }
            for (const key in options.params) {
                query.set(key, options.params[key]);
            }
            localVarUrlObj.search = (new URLSearchParams(query)).toString();
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            const needsSerialization = (typeof body !== "string") || localVarRequestOptions.headers['Content-Type'] === 'application/json';
            localVarRequestOptions.data =  needsSerialization ? JSON.stringify(body !== undefined ? body : {}) : (body || "");

            return {
                url: localVarUrlObj.pathname + localVarUrlObj.search + localVarUrlObj.hash,
                options: localVarRequestOptions,
            };
        },
        /**
         * Get all collaborators of a quiz
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getCollaborators: async (id: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            if (id === null || id === undefined) {
                throw new RequiredError('id','Required parameter id was null or undefined when calling getCollaborators.');
            }
            const localVarPath = `/quiz/{id}/collaborator`
                .replace(`{${"id"}}`, encodeURIComponent(String(id)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, 'https://example.com');
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }
            const localVarRequestOptions :AxiosRequestConfig = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            const query = new URLSearchParams(localVarUrlObj.search);
            for (const key in localVarQueryParameter) {
                query.set(key, localVarQueryParameter[key]);
            }
            for (const key in options.params) {
                query.set(key, options.params[key]);
            }
            localVarUrlObj.search = (new URLSearchParams(query)).toString();
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: localVarUrlObj.pathname + localVarUrlObj.search + localVarUrlObj.hash,
                options: localVarRequestOptions,
            };
        },
        /**
         * Remove a collaborator from a quiz
         * @param {string} id The ID of a quiz
         * @param {string} collaboratorId The ID of the collaborator
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        removeCollaborator: async (id: string, collaboratorId: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            if (id === null || id === undefined) {
                throw new RequiredError('id','Required parameter id was null or undefined when calling removeCollaborator.');
            }
            // verify required parameter 'collaboratorId' is not null or undefined
            if (collaboratorId === null || collaboratorId === undefined) {
                throw new RequiredError('collaboratorId','Required parameter collaboratorId was null or undefined when calling removeCollaborator.');
            }
            const localVarPath = `/quiz/{id}/collaborator/{collaborator_id}`
                .replace(`{${"id"}}`, encodeURIComponent(String(id)))
                .replace(`{${"collaborator_id"}}`, encodeURIComponent(String(collaboratorId)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, 'https://example.com');
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }
            const localVarRequestOptions :AxiosRequestConfig = { method: 'DELETE', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            const query = new URLSearchParams(localVarUrlObj.search);
            for (const key in localVarQueryParameter) {
                query.set(key, localVarQueryParameter[key]);
            }
            for (const key in options.params) {
                query.set(key, options.params[key]);
            }
            localVarUrlObj.search = (new URLSearchParams(query)).toString();
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: localVarUrlObj.pathname + localVarUrlObj.search + localVarUrlObj.hash,
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * CollaboratorApi - functional programming interface
 * @export
 */
export const CollaboratorApiFp = function(configuration?: Configuration) {
    return {
        /**
         * Add a collaborator to a quiz
         * @param {string} id The ID of a quiz
         * @param {QuizAddCollaborator} [body] Collaborator information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async addCollaborator(id: string, body?: QuizAddCollaborator, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<void>>> {
            const localVarAxiosArgs = await CollaboratorApiAxiosParamCreator(configuration).addCollaborator(id, body, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
        /**
         * Get all collaborators of a quiz
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getCollaborators(id: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<Array<User>>>> {
            const localVarAxiosArgs = await CollaboratorApiAxiosParamCreator(configuration).getCollaborators(id, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
        /**
         * Remove a collaborator from a quiz
         * @param {string} id The ID of a quiz
         * @param {string} collaboratorId The ID of the collaborator
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async removeCollaborator(id: string, collaboratorId: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<void>>> {
            const localVarAxiosArgs = await CollaboratorApiAxiosParamCreator(configuration).removeCollaborator(id, collaboratorId, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
    }
};

/**
 * CollaboratorApi - factory interface
 * @export
 */
export const CollaboratorApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    return {
        /**
         * Add a collaborator to a quiz
         * @param {string} id The ID of a quiz
         * @param {QuizAddCollaborator} [body] Collaborator information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async addCollaborator(id: string, body?: QuizAddCollaborator, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> {
            return CollaboratorApiFp(configuration).addCollaborator(id, body, options).then((request) => request(axios, basePath));
        },
        /**
         * Get all collaborators of a quiz
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getCollaborators(id: string, options?: AxiosRequestConfig): Promise<AxiosResponse<Array<User>>> {
            return CollaboratorApiFp(configuration).getCollaborators(id, options).then((request) => request(axios, basePath));
        },
        /**
         * Remove a collaborator from a quiz
         * @param {string} id The ID of a quiz
         * @param {string} collaboratorId The ID of the collaborator
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async removeCollaborator(id: string, collaboratorId: string, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> {
            return CollaboratorApiFp(configuration).removeCollaborator(id, collaboratorId, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * CollaboratorApi - object-oriented interface
 * @export
 * @class CollaboratorApi
 * @extends {BaseAPI}
 */
export class CollaboratorApi extends BaseAPI {
    /**
     * Add a collaborator to a quiz
     * @param {string} id The ID of a quiz
     * @param {QuizAddCollaborator} [body] Collaborator information
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CollaboratorApi
     */
    public async addCollaborator(id: string, body?: QuizAddCollaborator, options?: AxiosRequestConfig) : Promise<AxiosResponse<void>> {
        return CollaboratorApiFp(this.configuration).addCollaborator(id, body, options).then((request) => request(this.axios, this.basePath));
    }
    /**
     * Get all collaborators of a quiz
     * @param {string} id The ID of a quiz
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CollaboratorApi
     */
    public async getCollaborators(id: string, options?: AxiosRequestConfig) : Promise<AxiosResponse<Array<User>>> {
        return CollaboratorApiFp(this.configuration).getCollaborators(id, options).then((request) => request(this.axios, this.basePath));
    }
    /**
     * Remove a collaborator from a quiz
     * @param {string} id The ID of a quiz
     * @param {string} collaboratorId The ID of the collaborator
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof CollaboratorApi
     */
    public async removeCollaborator(id: string, collaboratorId: string, options?: AxiosRequestConfig) : Promise<AxiosResponse<void>> {
        return CollaboratorApiFp(this.configuration).removeCollaborator(id, collaboratorId, options).then((request) => request(this.axios, this.basePath));
    }
}
