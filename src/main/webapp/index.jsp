<!-- 

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
 * NLP Service 
 */
 
  --><!DOCTYPE html>
<html>
<head>


<title>CTSI NLP Service</title>
<meta charset="utf-8">
   <meta http-equiv="X-UA-Compatible" content="IE=edge">
   <meta name="viewport" content="width=device-width, initial-scale=1">
   <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->

<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
   
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>  

<style>
    body {
        margin: 0px;
        font-family: arial, sans-serif;
        text-align: left;
    }
    table, tr, td {
        border: 0px solid white;
        border-collapse: collapse;
        
    }
    tr, td {
        padding-left: 5px;
        padding-top: 5px;
    }
    table.base {
        width:100%;
    }
    tr.basec, td.basec {
        background-color: rgba(43,78,116,1);
    }
    td.basew {
        background-color: rgba(43,78,116,1);
        width: 960px;
    }
    table.main {
        width: 960px;
    }
    tr.pdl, td.pdl{
        padding-left: 20px;
    }
    tr.chw, td.chw{
        width: 450px;
        padding-left: 20px;
        border: 1px solid #0082c8;
        background: #0082c8;
    }
    tr.fh, td.fh {
        width: 450px;
        padding-left: 12px;
        padding-bottom: 1px;
        background-color: #F0F3F6;
    }
    a:link, a:visited {
      #  color:#666;
      #  text-decoration: none;
    }
    a.white {
        color:#FFF;
    }
    p.attd {
        padding: 8px;
        font-size: 120%;
        color: #555;
        background-color: #B0E1FF;
    }
    td.status {
        padding: 16px;
        padding-top: 0px;
        border: 1px solid #CCC;
        color: #666;
    }
    li.listGreen {
      color: Green;
      font-size: 24px;
    }
    li.listYellow {
      color: Gold;
      font-size: 24px;
    }
    li.listRed {
      color: Red;
      font-size: 24px;
    }
    li p {
      color: #666;
      font-size: 15px;
    }

</style>
</head>
    
<body>

<script>
//A $( document ).ready() block.
$( document ).ready(function() {

$('#nlpform').submit(function() { // catch the form's submit event
	$('#parse').html($(this).serialize());
    $.ajax({ // create an AJAX call...
        data: $(this).serialize(), // get the form data
        type: $(this).attr('method'), // GET or POST
        url: $(this).attr('action'), // the file to call
        success: function(response) { // on success..
        $('#parse').html(response); // update the DIV
        }
    });
    return false; // cancel original event to prevent form submitting
});

  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-41673085-2', 'auto');
  ga('send', 'pageview');
  
  console.log( "ready!" );
});
</script>
<br>


<table class="base">
  <tr>
    <td>&nbsp;</td>
    <td class="basew"><a href="https://ctri.mcw.edu/"><img src="img/ctrilogo.png" alt="Logo"></a></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td></td>
    <td class="pdl">

<div class="container">
<h3>NLP Service</h4>
<!--  
<%= java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) %>
-->
  <form id="nlpform" method="post" action="nlp/NLPServiceServlet">

  <div class="form-group">
    <label for="q">Input text:</label>
    <textarea class="form-control small" rows="8" cols="50" name="q">Jay Urbain is an elderly caucasian male suffering from illusions of grandeur and low back pain. Patient has a family history of CAD and DM. Prescribed meloxicam, and venti americano.</textarea>
  </div>
  
  <div class="form-inline">
    <label for="format">Data Format</label>
    <select name="format" class="form-control input-small">
 		<option value="pretty" selected>Pretty Print</option>
 		<option value="html">HTML</option>
 		<option value="ann">Annotation</option>
 		<option value="json">JSON</option>
 		<!--
 		<option value="xml">XML</option>
 		-->
    </select>
  </div>

  <div class="form-group">
    <input type="submit" class="btn btn-primary"></button>
  </div>
  </form>
  <hr>
  <div class="form-group">
  	<label for="q">Parsed results:</label>
  	<div id=parse class="form-group small"></div>
  </div>
    
   <!--  Version: <%=System.getProperty("ctakesversion") %> -->
   
</div>

    </td>
    <td>
    
    </td>
  </tr>
  <tr><td></td><td>&nbsp;</td><td></td></tr>
  <tr>
    <td></td>
    <td class="basew"><h4>&nbsp;&nbsp;&nbsp;&nbsp;
    <a href="mailto:crdw@mcw.edu" class="white">Email Us: <span style="font-size:90%">jay.urbain@gmail.com</span></a>
    <!--  <a href="jay.urbain@gmail.com" class="white">Email Us: <span style="font-size:90%">crdw@mcw.edu</span></a>
    -->
    </h4></td>
    <td></td>
  </tr>
  <tr>
    <td></td>
    <td>&nbsp;&nbsp;&nbsp;&nbsp;
    <i>
      This is software is based in part on <a href="http://ctakes.apache.org/">Apache cTakes</a> and is licensed under the <a href="https://www.apache.org/licenses/LICENSE-2.0">Apache License, 2.0</a>.
    </i>
    </td>
    <td></td>
  </tr>  
  
  
  
</table>
</body>
</html>