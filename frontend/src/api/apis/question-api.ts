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
import { Question } from '../models';
import { QuestionCreate } from '../models';
/**
 * QuestionApi - axios parameter creator
 * @export
 */
export const QuestionApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * Create a new question
         * @param {QuestionCreate} [body] Question creation information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createQuestion: async (body?: QuestionCreate, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/question`;
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
         * Delete a question
         * @param {string} questionId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        deleteQuestion: async (questionId: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'questionId' is not null or undefined
            if (questionId === null || questionId === undefined) {
                throw new RequiredError('questionId','Required parameter questionId was null or undefined when calling deleteQuestion.');
            }
            const localVarPath = `/question/{question_id}`
                .replace(`{${"question_id"}}`, encodeURIComponent(String(questionId)));
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
        /**
         * Update a question
         * @param {QuestionCreate} body Question update information
         * @param {string} questionId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        updateQuestion: async (body: QuestionCreate, questionId: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'body' is not null or undefined
            if (body === null || body === undefined) {
                throw new RequiredError('body','Required parameter body was null or undefined when calling updateQuestion.');
            }
            // verify required parameter 'questionId' is not null or undefined
            if (questionId === null || questionId === undefined) {
                throw new RequiredError('questionId','Required parameter questionId was null or undefined when calling updateQuestion.');
            }
            const localVarPath = `/question/{question_id}`
                .replace(`{${"question_id"}}`, encodeURIComponent(String(questionId)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, 'https://example.com');
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }
            const localVarRequestOptions :AxiosRequestConfig = { method: 'PUT', ...baseOptions, ...options};
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
    }
};

/**
 * QuestionApi - functional programming interface
 * @export
 */
export const QuestionApiFp = function(configuration?: Configuration) {
    return {
        /**
         * Create a new question
         * @param {QuestionCreate} [body] Question creation information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createQuestion(body?: QuestionCreate, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<Question>>> {
            const localVarAxiosArgs = await QuestionApiAxiosParamCreator(configuration).createQuestion(body, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
        /**
         * Delete a question
         * @param {string} questionId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async deleteQuestion(questionId: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<void>>> {
            const localVarAxiosArgs = await QuestionApiAxiosParamCreator(configuration).deleteQuestion(questionId, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
        /**
         * Update a question
         * @param {QuestionCreate} body Question update information
         * @param {string} questionId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async updateQuestion(body: QuestionCreate, questionId: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<Question>>> {
            const localVarAxiosArgs = await QuestionApiAxiosParamCreator(configuration).updateQuestion(body, questionId, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
    }
};

/**
 * QuestionApi - factory interface
 * @export
 */
export const QuestionApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    return {
        /**
         * Create a new question
         * @param {QuestionCreate} [body] Question creation information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createQuestion(body?: QuestionCreate, options?: AxiosRequestConfig): Promise<AxiosResponse<Question>> {
            return QuestionApiFp(configuration).createQuestion(body, options).then((request) => request(axios, basePath));
        },
        /**
         * Delete a question
         * @param {string} questionId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async deleteQuestion(questionId: string, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> {
            return QuestionApiFp(configuration).deleteQuestion(questionId, options).then((request) => request(axios, basePath));
        },
        /**
         * Update a question
         * @param {QuestionCreate} body Question update information
         * @param {string} questionId The ID of the question
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async updateQuestion(body: QuestionCreate, questionId: string, options?: AxiosRequestConfig): Promise<AxiosResponse<Question>> {
            return QuestionApiFp(configuration).updateQuestion(body, questionId, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * QuestionApi - object-oriented interface
 * @export
 * @class QuestionApi
 * @extends {BaseAPI}
 */
export class QuestionApi extends BaseAPI {
    /**
     * Create a new question
     * @param {QuestionCreate} [body] Question creation information
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof QuestionApi
     */
    public async createQuestion(body?: QuestionCreate, options?: AxiosRequestConfig) : Promise<AxiosResponse<Question>> {
        return QuestionApiFp(this.configuration).createQuestion(body, options).then((request) => request(this.axios, this.basePath));
    }
    /**
     * Delete a question
     * @param {string} questionId The ID of the question
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof QuestionApi
     */
    public async deleteQuestion(questionId: string, options?: AxiosRequestConfig) : Promise<AxiosResponse<void>> {
        return QuestionApiFp(this.configuration).deleteQuestion(questionId, options).then((request) => request(this.axios, this.basePath));
    }
    /**
     * Update a question
     * @param {QuestionCreate} body Question update information
     * @param {string} questionId The ID of the question
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof QuestionApi
     */
    public async updateQuestion(body: QuestionCreate, questionId: string, options?: AxiosRequestConfig) : Promise<AxiosResponse<Question>> {
        return QuestionApiFp(this.configuration).updateQuestion(body, questionId, options).then((request) => request(this.axios, this.basePath));
    }
}
