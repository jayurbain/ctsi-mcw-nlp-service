/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
    This file is part of "CTSI MCW NLP" for removing
    protected health information from medical records.

    "CTSI MCW NLP" is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    "CTSI MCW NLP" is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with "CTSI MCW NLP."  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @author jayurbain, jay.urbain@gmail.com
 * 
 */

/**
 * @author jayurbain
 * 2/18/2017
 * 
 * UMLs Concept Wrapper

 */
package edu.mcw.ctsi.nlp;

public class UmlsConceptWrapper {

	String subject;
	int begin;
	int end;
	String coveredText;
	String polarity;
	String codingScheme;
	String code;
	String oid;
	String cui;
	String tui;
	String preferredText;
	int typeIndex;
	
	public UmlsConceptWrapper(String subject, int begin, int end, String coveredText, String polarity, String codingScheme, String code, String oid, String cui,
			String tui, String preferredText, int typeIndex) {
		super();
		this.subject = subject;
		this.begin = begin;
		this.end = end;
		this.coveredText = coveredText;
		this.polarity = polarity;
		this.codingScheme = codingScheme;
		this.code = code;
		this.oid = oid;
		this.cui = cui;
		this.tui = tui;
		this.preferredText = preferredText;
		this.typeIndex = typeIndex;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getPolarity() {
		return polarity;
	}

	public void setPolarity(String polarity) {
		this.polarity = polarity;
	}

	public String getCodingScheme() {
		return codingScheme;
	}

	public void setCodingScheme(String codingScheme) {
		this.codingScheme = codingScheme;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getCui() {
		return cui;
	}

	public void setCui(String cui) {
		this.cui = cui;
	}

	public String getTui() {
		return tui;
	}

	public void setTui(String tui) {
		this.tui = tui;
	}

	public String getPreferredText() {
		return preferredText;
	}

	public void setPreferredText(String preferredText) {
		this.preferredText = preferredText;
	}

	public int getTypeIndex() {
		return typeIndex;
	}

	public void setTypeIndex(int typeIndex) {
		this.typeIndex = typeIndex;
	}

	public String getCoveredText() {
		return coveredText;
	}

	public void setCoveredText(String coveredText) {
		this.coveredText = coveredText;
	}

	@Override
	public String toString() {
		return 
//				subject + ", " +
				begin + ", " +
				end + ", " +
				coveredText + ", " +
				codingScheme + ", " +
				code + ", " +
//				oid + ", " +
				cui + ", " +
				tui + ", " + 
				preferredText + ", " +
				typeIndex +
				"\n";
	}
	
	public String toJSON() {
		return  //"{" +
				"\"begin\": \"" + begin +  "\", " +
				"\"end\": \"" + end +  "\", " +
				"\"coveredText\": \"" + coveredText +  "\", " +
				"\"codingScheme\": \"" + codingScheme +  "\", " +
//				"\"code\": \"" + code +  "\", " +
				"\"cui\": \"" + cui +  "\", " +
				"\"tui\": \"" + tui +  "\", " +
				"\"preferredText\": \"" + preferredText +  "\", " +
				"\"typeIndex\": \"" + typeIndex +  "\"";
//				"}";
	}
}