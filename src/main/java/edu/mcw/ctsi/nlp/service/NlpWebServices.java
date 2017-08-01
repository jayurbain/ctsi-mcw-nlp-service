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
 * Jay Urbain
 * jay.urbain@gmail.com
 * 7/24/2017
 * 
 * NLP Web Service Servlet using cTakes
 */
package edu.mcw.ctsi.nlp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.jcas.JCas;
import org.codehaus.jackson.map.ObjectMapper;

import edu.mcw.ctsi.nlp.NLPServiceServlet;

/**
 * Servlet implementation class NlpWebServices
 */
public class NlpWebServices extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NlpWebServices() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		String q = request.getParameter("q");
		processServiceRequest(q, request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuffer jb = new StringBuffer();
		  String line = null;
		  try {
		    BufferedReader reader = request.getReader();
		    while ((line = reader.readLine()) != null)
		      jb.append(line);
		  } catch (Exception e) { /*report an error*/ }
		  
		  processServiceRequest(jb.toString(), request, response);
	}
	
	protected void processServiceRequest(String q, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println(q);
		
	    response.setContentType("application/json");
	    PrintWriter out=response.getWriter();
		
		List<String> recordList = null;
		try {
			recordList = jsonToJava(q);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		StringBuffer sbb = new StringBuffer();
		AnalysisEngine pipeline = null;
		try {
			pipeline = NLPServiceServlet.getPipeline();
			JCas jcas = pipeline.newJCas();
			boolean first = true;
			sbb.append("{ \"nlplist\": [");
			for( String record : recordList) {	
				jcas.setDocumentText(record);
				pipeline.process(jcas);
				String annotation = NLPServiceServlet.formatResults(jcas, "json");
				if( !first ) {
					sbb.append( ", " );
				}
				else {
					first = false;
				}
				sbb.append( annotation.toString() );
				jcas.reset();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if( pipeline != null ) {
				NLPServiceServlet.putPipeline(pipeline);
			}
		}
//		System.out.println(q);
	    out.write(sbb.toString());
	    out.close();
	}

	
	public static List<String> jsonToJava(String json) throws JSONException {
		
		List<String> recordList = new ArrayList<String>();
		JSONObject jsonObject = new JSONObject( json );
		JSONArray records = (JSONArray) jsonObject.get("recordlist");
		for(int i=0; i<records.length(); i++) {
			String record = records.getString(i);
			recordList.add( record );
		}
		return recordList;
	}
}