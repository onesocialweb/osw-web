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
package org.onesocialweb.gwt.client.util;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;

public class FormatHelper {

	public static String getFormattedDate(Date date) {

		if (date == null) {
			return "Date unknown";
		}

		return DateTimeFormat.getMediumDateTimeFormat().format(date);
	}

	public static String implode(List<String> strings, String separator) {
		StringBuffer buffer = new StringBuffer();
		if (!strings.isEmpty()) {
			Iterator<String> i = strings.iterator();
			buffer.append(i.next());
			if (i.hasNext()) {
				buffer.append(separator);
			}
		}
		return buffer.toString();
	}
}
