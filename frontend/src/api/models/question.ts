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

import { Image } from './image';
 /**
 * 
 *
 * @export
 * @interface Question
 */
export interface Question {

    /**
     * @type {string}
     * @memberof Question
     */
    id: string;

    /**
     * @type {string}
     * @memberof Question
     */
    question: string;

    /**
     * @type {string}
     * @memberof Question
     */
    answer?: string;

    /**
     * @type {Image}
     * @memberof Question
     */
    image: Image;

    /**
     * @type {Array<string>}
     * @memberof Question
     */
    options: Array<string>;
}
