// @ts-nocheck
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
import { Quiz } from '../models';
import { Revision } from '../models';
/**
 * RevisionApi - axios parameter creator
 * @export
 */
export const RevisionApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * Get revisions of a quiz
         * @param {string} quizId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getRevisions: async (quizId: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'quizId' is not null or undefined
            if (quizId === null || quizId === undefined) {
                throw new RequiredError('quizId','Required parameter quizId was null or undefined when calling getRevisions.');
            }
            const localVarPath = `/quiz/{quizId}/revision`
                .replace(`{${"quizId"}}`, encodeURIComponent(String(quizId)));
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
         * Revert to a specific revision
         * @param {string} quizId The ID of the quiz
         * @param {string} revisionId The ID of the revision
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        revertToRevision: async (quizId: string, revisionId: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'quizId' is not null or undefined
            if (quizId === null || quizId === undefined) {
                throw new RequiredError('quizId','Required parameter quizId was null or undefined when calling revertToRevision.');
            }
            // verify required parameter 'revisionId' is not null or undefined
            if (revisionId === null || revisionId === undefined) {
                throw new RequiredError('revisionId','Required parameter revisionId was null or undefined when calling revertToRevision.');
            }
            const localVarPath = `/quiz/{quizId}/revision/{revisionId}`
                .replace(`{${"quizId"}}`, encodeURIComponent(String(quizId)))
                .replace(`{${"revisionId"}}`, encodeURIComponent(String(revisionId)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, 'https://example.com');
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }
            const localVarRequestOptions :AxiosRequestConfig = { method: 'PUT', ...baseOptions, ...options};
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
 * RevisionApi - functional programming interface
 * @export
 */
export const RevisionApiFp = function(configuration?: Configuration) {
    return {
        /**
         * Get revisions of a quiz
         * @param {string} quizId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getRevisions(quizId: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<Array<Revision>>>> {
            const localVarAxiosArgs = await RevisionApiAxiosParamCreator(configuration).getRevisions(quizId, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
        /**
         * Revert to a specific revision
         * @param {string} quizId The ID of the quiz
         * @param {string} revisionId The ID of the revision
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async revertToRevision(quizId: string, revisionId: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<Quiz>>> {
            const localVarAxiosArgs = await RevisionApiAxiosParamCreator(configuration).revertToRevision(quizId, revisionId, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
    }
};

/**
 * RevisionApi - factory interface
 * @export
 */
export const RevisionApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    return {
        /**
         * Get revisions of a quiz
         * @param {string} quizId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getRevisions(quizId: string, options?: AxiosRequestConfig): Promise<AxiosResponse<Array<Revision>>> {
            return RevisionApiFp(configuration).getRevisions(quizId, options).then((request) => request(axios, basePath));
        },
        /**
         * Revert to a specific revision
         * @param {string} quizId The ID of the quiz
         * @param {string} revisionId The ID of the revision
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async revertToRevision(quizId: string, revisionId: string, options?: AxiosRequestConfig): Promise<AxiosResponse<Quiz>> {
            return RevisionApiFp(configuration).revertToRevision(quizId, revisionId, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * RevisionApi - object-oriented interface
 * @export
 * @class RevisionApi
 * @extends {BaseAPI}
 */
export class RevisionApi extends BaseAPI {
    /**
     * Get revisions of a quiz
     * @param {string} quizId The ID of the question
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof RevisionApi
     */
    public async getRevisions(quizId: string, options?: AxiosRequestConfig) : Promise<AxiosResponse<Array<Revision>>> {
        return RevisionApiFp(this.configuration).getRevisions(quizId, options).then((request) => request(this.axios, this.basePath));
    }
    /**
     * Revert to a specific revision
     * @param {string} quizId The ID of the quiz
     * @param {string} revisionId The ID of the revision
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof RevisionApi
     */
    public async revertToRevision(quizId: string, revisionId: string, options?: AxiosRequestConfig) : Promise<AxiosResponse<Quiz>> {
        return RevisionApiFp(this.configuration).revertToRevision(quizId, revisionId, options).then((request) => request(this.axios, this.basePath));
    }
}
