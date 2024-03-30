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
import { Category } from '../models';
import { Quiz } from '../models';
import { QuizCreate } from '../models';
import { QuizOverview } from '../models';
/**
 * QuizApi - axios parameter creator
 * @export
 */
export const QuizApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * Create a new quiz
         * @param {QuizCreate} [body] Quiz creation information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createQuiz: async (body?: QuizCreate, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/quiz`;
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
         * Get first page of quizzes
         * @param {number} [limit] 
         * @param {string} [textSearch] 
         * @param {number} [minDifficulty] 
         * @param {number} [maxDifficulty] 
         * @param {Array<Category>} [category] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        quizGet: async (limit?: number, textSearch?: string, minDifficulty?: number, maxDifficulty?: number, category?: Array<Category>, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            const localVarPath = `/quiz`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, 'https://example.com');
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }
            const localVarRequestOptions :AxiosRequestConfig = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            if (limit !== undefined) {
                localVarQueryParameter['limit'] = limit;
            }

            if (textSearch !== undefined) {
                localVarQueryParameter['textSearch'] = textSearch;
            }

            if (minDifficulty !== undefined) {
                localVarQueryParameter['minDifficulty'] = minDifficulty;
            }

            if (maxDifficulty !== undefined) {
                localVarQueryParameter['maxDifficulty'] = maxDifficulty;
            }

            if (category) {
                localVarQueryParameter['category'] = category;
            }

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
         * Get all information about a quiz
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        quizIdGet: async (id: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            if (id === null || id === undefined) {
                throw new RequiredError('id','Required parameter id was null or undefined when calling quizIdGet.');
            }
            const localVarPath = `/quiz/{id}`
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
         * Update quiz info
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        quizIdPut: async (id: string, options: AxiosRequestConfig = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            if (id === null || id === undefined) {
                throw new RequiredError('id','Required parameter id was null or undefined when calling quizIdPut.');
            }
            const localVarPath = `/quiz/{id}`
                .replace(`{${"id"}}`, encodeURIComponent(String(id)));
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
 * QuizApi - functional programming interface
 * @export
 */
export const QuizApiFp = function(configuration?: Configuration) {
    return {
        /**
         * Create a new quiz
         * @param {QuizCreate} [body] Quiz creation information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createQuiz(body?: QuizCreate, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<void>>> {
            const localVarAxiosArgs = await QuizApiAxiosParamCreator(configuration).createQuiz(body, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
        /**
         * Get first page of quizzes
         * @param {number} [limit] 
         * @param {string} [textSearch] 
         * @param {number} [minDifficulty] 
         * @param {number} [maxDifficulty] 
         * @param {Array<Category>} [category] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async quizGet(limit?: number, textSearch?: string, minDifficulty?: number, maxDifficulty?: number, category?: Array<Category>, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<Array<QuizOverview>>>> {
            const localVarAxiosArgs = await QuizApiAxiosParamCreator(configuration).quizGet(limit, textSearch, minDifficulty, maxDifficulty, category, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
        /**
         * Get all information about a quiz
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async quizIdGet(id: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<Quiz>>> {
            const localVarAxiosArgs = await QuizApiAxiosParamCreator(configuration).quizIdGet(id, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
        /**
         * Update quiz info
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async quizIdPut(id: string, options?: AxiosRequestConfig): Promise<(axios?: AxiosInstance, basePath?: string) => Promise<AxiosResponse<Quiz>>> {
            const localVarAxiosArgs = await QuizApiAxiosParamCreator(configuration).quizIdPut(id, options);
            return (axios: AxiosInstance = globalAxios, basePath: string = BASE_PATH) => {
                const axiosRequestArgs :AxiosRequestConfig = {...localVarAxiosArgs.options, url: basePath + localVarAxiosArgs.url};
                return axios.request(axiosRequestArgs);
            };
        },
    }
};

/**
 * QuizApi - factory interface
 * @export
 */
export const QuizApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    return {
        /**
         * Create a new quiz
         * @param {QuizCreate} [body] Quiz creation information
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createQuiz(body?: QuizCreate, options?: AxiosRequestConfig): Promise<AxiosResponse<void>> {
            return QuizApiFp(configuration).createQuiz(body, options).then((request) => request(axios, basePath));
        },
        /**
         * Get first page of quizzes
         * @param {number} [limit] 
         * @param {string} [textSearch] 
         * @param {number} [minDifficulty] 
         * @param {number} [maxDifficulty] 
         * @param {Array<Category>} [category] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async quizGet(limit?: number, textSearch?: string, minDifficulty?: number, maxDifficulty?: number, category?: Array<Category>, options?: AxiosRequestConfig): Promise<AxiosResponse<Array<QuizOverview>>> {
            return QuizApiFp(configuration).quizGet(limit, textSearch, minDifficulty, maxDifficulty, category, options).then((request) => request(axios, basePath));
        },
        /**
         * Get all information about a quiz
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async quizIdGet(id: string, options?: AxiosRequestConfig): Promise<AxiosResponse<Quiz>> {
            return QuizApiFp(configuration).quizIdGet(id, options).then((request) => request(axios, basePath));
        },
        /**
         * Update quiz info
         * @param {string} id The ID of a quiz
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async quizIdPut(id: string, options?: AxiosRequestConfig): Promise<AxiosResponse<Quiz>> {
            return QuizApiFp(configuration).quizIdPut(id, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * QuizApi - object-oriented interface
 * @export
 * @class QuizApi
 * @extends {BaseAPI}
 */
export class QuizApi extends BaseAPI {
    /**
     * Create a new quiz
     * @param {QuizCreate} [body] Quiz creation information
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof QuizApi
     */
    public async createQuiz(body?: QuizCreate, options?: AxiosRequestConfig) : Promise<AxiosResponse<void>> {
        return QuizApiFp(this.configuration).createQuiz(body, options).then((request) => request(this.axios, this.basePath));
    }
    /**
     * Get first page of quizzes
     * @param {number} [limit] 
     * @param {string} [textSearch] 
     * @param {number} [minDifficulty] 
     * @param {number} [maxDifficulty] 
     * @param {Array<Category>} [category] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof QuizApi
     */
    public async quizGet(limit?: number, textSearch?: string, minDifficulty?: number, maxDifficulty?: number, category?: Array<Category>, options?: AxiosRequestConfig) : Promise<AxiosResponse<Array<QuizOverview>>> {
        return QuizApiFp(this.configuration).quizGet(limit, textSearch, minDifficulty, maxDifficulty, category, options).then((request) => request(this.axios, this.basePath));
    }
    /**
     * Get all information about a quiz
     * @param {string} id The ID of a quiz
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof QuizApi
     */
    public async quizIdGet(id: string, options?: AxiosRequestConfig) : Promise<AxiosResponse<Quiz>> {
        return QuizApiFp(this.configuration).quizIdGet(id, options).then((request) => request(this.axios, this.basePath));
    }
    /**
     * Update quiz info
     * @param {string} id The ID of a quiz
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof QuizApi
     */
    public async quizIdPut(id: string, options?: AxiosRequestConfig) : Promise<AxiosResponse<Quiz>> {
        return QuizApiFp(this.configuration).quizIdPut(id, options).then((request) => request(this.axios, this.basePath));
    }
}
