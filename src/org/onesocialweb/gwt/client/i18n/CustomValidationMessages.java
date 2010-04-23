/*
 *  Copyright 2010 Vodafone Group Services Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *    
 */
package org.onesocialweb.gwt.client.i18n;

import com.google.gwt.core.client.GWT;

import eu.maydu.gwt.validation.client.i18n.StandardValidationMessages;
import eu.maydu.gwt.validation.client.i18n.StandardValidationMessagesImpl;

public class CustomValidationMessages implements StandardValidationMessages {

	private StandardValidationMessages defaultMessages;

	private CustomValidationMessagesImpl customMessages;

	public CustomValidationMessages() {
		defaultMessages = GWT.create(StandardValidationMessagesImpl.class);
		customMessages = GWT.create(CustomValidationMessagesImpl.class);
	}

	// Custom messages
	public String length(int minLength, int maxLength, int actualLength) {
		return customMessages.length(minLength, maxLength);
	}

	// Default messages from Validator component
	public String equal() {
		return defaultMessages.equal();
	}

	public String isNull() {
		return defaultMessages.isNull();
	}

	// Standard messages
	public String max(int maxValue, int actualValue) {
		return defaultMessages.max(maxValue, actualValue);
	}

	public String max(long maxValue, long actualValue) {
		return defaultMessages.max(maxValue, actualValue);
	}

	public String max(float maxValue, float actualValue) {
		return defaultMessages.max(maxValue, actualValue);
	}

	public String max(double maxValue, double actualValue) {
		return defaultMessages.max(maxValue, actualValue);
	}

	public String min(int minValue, int actualValue) {
		return defaultMessages.min(minValue, actualValue);
	}

	public String min(long minValue, long actualValue) {
		return defaultMessages.min(minValue, actualValue);
	}

	public String min(float minValue, float actualValue) {
		return defaultMessages.min(minValue, actualValue);
	}

	public String min(double minValue, double actualValue) {
		return defaultMessages.min(minValue, actualValue);
	}

	public String mustSelectValue() {
		return defaultMessages.mustSelectValue();
	}

	public String noDateGiven() {
		return defaultMessages.noDateGiven();
	}

	public String notADouble() {
		return defaultMessages.notADouble();
	}

	public String notAFloat() {
		return defaultMessages.notAFloat();
	}

	public String notALong() {
		return defaultMessages.notALong();
	}

	public String notARegEx() {
		return defaultMessages.notARegEx();
	}

	public String notAValidCharacter(char c) {
		return defaultMessages.notAValidCharacter(c);
	}

	public String notAValidEmail(String givenEmailInput) {
		return defaultMessages.notAValidEmail(givenEmailInput);
	}

	public String notAValidTimeWithSecondsOptionally(String parameter) {
		return defaultMessages.notAValidTimeWithSecondsOptionally(parameter);
	}

	public String notAValidTimeWithSecondsRequired(String parameter) {
		return defaultMessages.notAValidTimeWithSecondsRequired(parameter);
	}

	public String notAValidTimeWithoutSeconds(String parameter) {
		return defaultMessages.notAValidTimeWithoutSeconds(parameter);
	}

	public String notAnInteger() {
		return defaultMessages.notAnInteger();
	}

	public String notEmpty() {
		return defaultMessages.notEmpty();
	}

	public String notEqual() {
		return defaultMessages.notEqual();
	}

	public String notInRange(int min, int max, int value) {
		return defaultMessages.notInRange(min, max, value);
	}

	public String notInRange(long min, long max, long value) {
		return defaultMessages.notInRange(min, max, value);
	}

	public String notInRange(float min, float max, float value) {
		return defaultMessages.notInRange(min, max, value);
	}

	public String notInRange(double min, double max, double value) {
		return defaultMessages.notInRange(min, max, value);
	}

	public String notNull() {
		return defaultMessages.notNull();
	}

	public String regexNotFulfilled() {
		return defaultMessages.regexNotFulfilled();
	}

	public String stringsNotEqual() {
		return defaultMessages.stringsNotEqual();
	}

	public String unparsableDate() {
		return defaultMessages.unparsableDate();
	}

	public String validator_assertFalse() {
		return defaultMessages.validator_assertFalse();
	}

	public String validator_assertTrue() {
		return defaultMessages.validator_assertTrue();
	}

	public String validator_creditCard() {
		return defaultMessages.validator_creditCard();
	}

	public String validator_digits() {
		return defaultMessages.validator_digits();
	}

	public String validator_ean() {
		return defaultMessages.validator_ean();
	}

	public String validator_email() {
		return defaultMessages.validator_email();
	}

	public String validator_future() {
		return defaultMessages.validator_future();
	}

	public String validator_length() {
		return defaultMessages.validator_length();
	}

	public String validator_max() {
		return defaultMessages.validator_max();
	}

	public String validator_min() {
		return defaultMessages.validator_min();
	}

	public String validator_notEmpty() {
		return defaultMessages.validator_notEmpty();
	}

	public String validator_notNull() {
		return defaultMessages.validator_notNull();
	}

	public String validator_past() {
		return defaultMessages.validator_past();
	}

	public String validator_pattern() {
		return defaultMessages.validator_pattern();
	}

	public String validator_range() {
		return defaultMessages.validator_range();
	}

	public String validator_size() {
		return defaultMessages.validator_size();
	}

}