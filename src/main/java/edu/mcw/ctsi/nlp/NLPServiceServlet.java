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
 * 
 * NLP Service Servlet using cTakes
 */
package edu.mcw.ctsi.nlp;
 
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.NumberFormat;
import java.text.DecimalFormat;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.ctakes.core.cc.pretty.plaintext.PrettyTextWriter;
import org.apache.ctakes.typesystem.type.constants.CONST;
import org.apache.ctakes.typesystem.type.refsem.UmlsConcept;
import org.apache.ctakes.typesystem.type.textsem.IdentifiedAnnotation;
import org.apache.ctakes.typesystem.type.textspan.Sentence;
//import org.apache.ctakes.web.client.servlet.Pipeline;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.sling.commons.json.JSONObject;
 
import edu.mcw.ctsi.nlp.UmlsConceptWrapper;
 
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.fit.factory.AggregateBuilder;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.log4j.Logger;
 
 
/*
 * Servlet that wires up a cTAKES pipeline
 * NOT Thread Safe. Pipeline is shared in JVM
 */
public class NLPServiceServlet extends HttpServlet {
 
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(NLPServiceServlet.class);
    private static final NumberFormat formatter = new DecimalFormat("#0.00000");
     
    static AnalysisEngine [] pipelineArray;
    static boolean [] pipelineArrayLock;
    static int threads;
     
    public static AnalysisEngine getPipeline() {
         
        AnalysisEngine pipe = null;
        synchronized( pipelineArray ) {
            for( int i=0; i<threads; i++) {
                if( pipelineArrayLock[i] == false ) {
                    pipe = pipelineArray[i];
                    pipelineArrayLock[i] = true;
                    System.out.println("getPipeline: " + i);
                    break;
                }
            }
            if( pipe == null ) {
                try {
                    pipelineArray.wait();
                } catch (InterruptedException e) {
                    ;
                }
                for( int i=0; i<threads; i++) {
                    if( pipelineArrayLock[i] == false ) {
                        pipe = pipelineArray[i];
                        pipelineArrayLock[i] = true;
                        System.out.println("getPipeline: " + i);
                        break;
                    }
                }
            }
        }
        return pipe;
    }
     
    public static void putPipeline(AnalysisEngine pipe) {
         
        synchronized( pipelineArray ) {
            for( int i=0; i<threads; i++) {
                if( pipelineArray[i] == pipe ) {
                    pipelineArrayLock[i] = false;
                    System.out.println("putPipeline: " + i);
                    break;
                }
            }
            pipelineArray.notify();
        }
    }
     
    public static NumberFormat getNumberFormatter() {
        return formatter;
    }
     
//  AggregateBuilder aggregateBuilder;
//  try {
//      aggregateBuilder = Pipeline.getAggregateBuilder();
//      pipeline = aggregateBuilder.createAggregate();
//  } catch (Exception e) {
//      throw new ServletException(e);
//  }
 
    public void init() throws ServletException {
        LOGGER.info("Initilizing Pipeline...");
        AggregateBuilder aggregateBuilder;
        AnalysisEngine pipeline;
        
        File file = new File("org/apache/ctakes/dictionary/lookup/fast/sno_rx_16ab/sno_rx_16ab");
        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);
        
        try {
            String threadsStr = getServletConfig().getInitParameter("threads");
            threads = Integer.parseInt( threadsStr );
            String pipelineStr = getServletConfig().getInitParameter("pipeline");
            Class<?> pipelineClass = Class.forName( pipelineStr );
            pipelineArray = new AnalysisEngine [threads];
            pipelineArrayLock = new boolean[threads];
            for( int i=0; i<threads; i++) {
              Pipeline pipelineInstance = (Pipeline) pipelineClass.newInstance();
              aggregateBuilder = pipelineInstance.getAggregateBuilder();
              pipeline = aggregateBuilder.createAggregate();
//                aggregateBuilder = Pipeline.getAggregateBuilder();
//                pipeline = aggregateBuilder.createAggregate();
                pipelineArray[i] = pipeline;
                pipelineArrayLock[i] = false;
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
 
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        PrintWriter out = response.getWriter();
        String text = request.getParameter("q");
        String format = request.getParameter("format");
        LOGGER.info("###\n" + text + "###\n");
        if (text != null && text.trim().length() > 0) {
            AnalysisEngine pipeline = null;
            try {
                /*
                 * Set the document text to process And run the cTAKES pipeline
                 */
                pipeline = getPipeline();
                JCas jcas = pipeline.newJCas();
                jcas.setDocumentText(text);
                pipeline.process(jcas);
                String result = formatResults(jcas, format);
                 
                if ("html".equalsIgnoreCase(format)) {
                    response.setContentType("text/html");
                } else if ("pretty".equalsIgnoreCase(format)) {
                    response.setContentType("text/html");
                } else if ("xml".equalsIgnoreCase(format)) {
                    response.setContentType("application/xml");
                } else if ("json".equalsIgnoreCase(format)) {
                    response.setContentType("application/json");
                } else if ("ann".equalsIgnoreCase(format)) {
                    response.setContentType("application/text");
                }
                jcas.reset();
                String elapsed = getFormatter()
                        .format((System.currentTimeMillis() - start) / 1000d);
                 
                if ("html".equalsIgnoreCase(format)
                        || "pretty".equalsIgnoreCase(format)) {
                    result += "<p/><i> Full Processed in " + elapsed + " secs</i><br>";
                }
                System.out.println(result);
                out.println(result);
            } catch (Exception e) {
                throw new ServletException(e);
            }
            finally {
                if( pipeline != null ) {
                    NLPServiceServlet.putPipeline(pipeline);
                }               
            }
        }
    }
 
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
 
    public static String formatResults(JCas jcas, String format) throws Exception {
         
        StringBuffer sb = new StringBuffer();
         
        /**
         * Select the types/classes that you are interested We are selecting
         * everything including TOP for demo purposes
         */
        Collection<TOP> annotations = JCasUtil.selectAll(jcas);
         
        if ("html".equalsIgnoreCase(format)) {
//          response.setContentType("text/html");
 
            sb.append("<html><head><title></title></head><body><table>");
            for (TOP a : annotations) {
 
                sb.append("<tr>");
                sb.append("<td>" + a.getType().getShortName() + "</td>");
                extractFeatures(sb, (FeatureStructure) a);
                sb.append("</tr>");
            }
            sb.append("</table></body></html>");
        } else if ("pretty".equalsIgnoreCase(format)) {
            StringWriter sw = new StringWriter();
            BufferedWriter writer = new BufferedWriter(sw);
            Collection<Sentence> sentences = JCasUtil.select(jcas,
                    Sentence.class);
            for (Sentence sentence : sentences) {
                PrettyTextWriter.writeSentence(jcas, sentence, writer);
            }
            writer.close();
            sb.append("<html><head><title></title></head><body><table><pre>");
            sb.append(sw.toString());
            sb.append("</pre></table></body></html>");
 
        } else if ("xml".equalsIgnoreCase(format)) {
//          response.setContentType("application/xml");
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            XmiCasSerializer.serialize(jcas.getCas(), output);
            sb.append(output.toString());
            output.close();
        } else if ("json".equalsIgnoreCase(format)) {
            StringBuffer sbb = new StringBuffer();
            boolean first = true;
            sbb.append("{ \"entitylist\": [");
            for ( IdentifiedAnnotation entity : JCasUtil.select( jcas, IdentifiedAnnotation.class ) ) {
                List<UmlsConceptWrapper> list = extractInformation(entity);
                //System.out.println(list);
                if( list != null ) {
                    String jsonEntityString = createJSONEntityAnnotationList( list );
                    if( !first ) sbb.append( ", " );
                    else first = false;
                    sbb.append( jsonEntityString.toString() );
                }
            }
            sbb.append("]}");
            String jsonStr = sbb.toString();
            JSONObject jsonObject = new JSONObject(jsonStr);
            String jsonFormattedStr = jsonObject.toString( );
            sb.append(jsonFormattedStr);
 
        } else if ("ann".equalsIgnoreCase(format)) {
//          response.setContentType("application/text");
        	Map<String, String> entityMap = new HashMap<String, String>();
            int T = 1;
            for ( IdentifiedAnnotation entity : JCasUtil.select( jcas, IdentifiedAnnotation.class ) ) {
                List<UmlsConceptWrapper> list = extractInformation(entity);
                if( list != null ) {
                    for( UmlsConceptWrapper umlsConceptWrapper :  list ) {
                    		String code = umlsConceptWrapper.getPreferredText().replaceAll("\\s","_") +"_"+umlsConceptWrapper.getCui()+"_"+umlsConceptWrapper.getCode();
                            if( !entityMap.containsKey(code) ) {
                            	StringBuffer sbb = new StringBuffer();
                            	sb.append( "T"+T + " " + code + " " + umlsConceptWrapper.getBegin() + " " + umlsConceptWrapper.getEnd() + " " + umlsConceptWrapper.getCoveredText() + "<BR>");
                            	entityMap.put(code, null);
                            	T += 1;
                            }
                    }
                }
            }
        }
        return sb.toString();
    }
     
    public static String createJSONEntityAnnotationList( List<UmlsConceptWrapper> list ) {
         
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        sb.append("{");
        for( UmlsConceptWrapper umlsConceptWrapper :  list ) {
            if( first ) {
                sb.append( umlsConceptWrapper.toJSON() );
                sb.append(", \"annotations\": [");
                sb.append( "\"" + umlsConceptWrapper.getCode() + "\"");
                first = false;
            }
            else {
                sb.append(",");
                sb.append( "\"" + umlsConceptWrapper.getCode() + "\"");
            }
        }
        sb.append("]}");
        return sb.toString();
    }
 
    public static void extractFeatures(StringBuffer sb, FeatureStructure fs) {
 
        List<?> plist = fs.getType().getFeatures();
        for (Object obj : plist) {
            if (obj instanceof Feature) {
                Feature feature = (Feature) obj;
                String val = "";
                if (feature.getRange().isPrimitive()) {
                    val = fs.getFeatureValueAsString(feature);
                } else if (feature.getRange().isArray()) {
                    // Flatten the Arrays
                    FeatureStructure featval = fs.getFeatureValue(feature);
                    if (featval instanceof FSArray) {
                        FSArray valarray = (FSArray) featval;
                        for (int i = 0; i < valarray.size(); ++i) {
                            FeatureStructure temp = valarray.get(i);
                            extractFeatures(sb, temp);
                        }
                    }
                }
                if (feature.getName() != null
                        && val != null
                        && val.trim().length() > 0
                        && !"confidence".equalsIgnoreCase(feature
                                .getShortName())) {
                    sb.append("<td>" + feature.getShortName() + "</td>");
                    sb.append("<td>" + val + "</td>");
                }
            }
        }
 
    }
     
    protected static List<UmlsConceptWrapper> extractInformation(IdentifiedAnnotation t) {
         
//      extractFeatures( t );
         
        List<UmlsConceptWrapper> umlsConceptList = new ArrayList<UmlsConceptWrapper>();     
        FSArray mentions = t.getOntologyConceptArr();
         
        if(mentions == null) return null;
        for(int i = 0; i < mentions.size(); i++){
            if(mentions.get(i) instanceof UmlsConcept){
                UmlsConcept concept = (UmlsConcept) mentions.get(i);
                UmlsConceptWrapper umlsConceptWrapper = new UmlsConceptWrapper (
                        t.getSubject(),
                        t.getBegin(),
                        t.getEnd(),
                        t.getCoveredText(),
                        (t.getPolarity() == CONST.NE_POLARITY_NEGATION_PRESENT)?"-":"+",
                        concept.getCodingScheme(),
                        concept.getCode(),
                        concept.getOid(),
                        concept.getCui(),
                        concept.getTui(),
                        concept.getPreferredText(),
                        concept.getTypeIndexID()
                        );
                umlsConceptList.add( umlsConceptWrapper );
            }
        }
         
        if(umlsConceptList.size() == 0) return null;
        return umlsConceptList;
    }
 
    public static NumberFormat getFormatter() {
        return formatter;
    }
 
}