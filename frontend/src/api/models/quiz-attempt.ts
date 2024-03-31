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

import { QuestionAttempt } from './question-attempt';
import { Quiz } from './quiz';
 /**
 * 
 *
 * @export
 * @interface QuizAttempt
 */
export interface QuizAttempt {

    /**
     * @type {string}
     * @memberof QuizAttempt
     */
    id: string;

    /**
     * @type {Array<QuestionAttempt>}
     * @memberof QuizAttempt
     */
    questionAttempts: Array<QuestionAttempt>;

    /**
     * @type {Quiz}
     * @memberof QuizAttempt
     */
    quiz?: Quiz;

    /**
     * @type {boolean}
     * @memberof QuizAttempt
     */
    complete: boolean;
}
