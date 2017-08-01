# ctsi-mcw-nlp-service

### Web application and web service software to perform named entity identification and related NLP over medical records text.

Jay Urbain, PhD  
jay.urbain@gmail.com

Java Maven web service app. The application is accessible through a web page (index.jsp) or a JSON-based web service.

The web app and web service links provided below are for evaluation only and not for production use.

### Web-based user interface
[https://cis.ctsi.mcw.edu/nlp/](https://cis.ctsi.mcw.edu/nlp/)

Formats: pretty print, JSON, Brat Annotation, HTML data dump

![Web Screen Shot](https://github.com/jayurbain/ctsi-mcw-nlp-service/blob/master/src/main/webapp/img/web_screen_shot.png)

### JSON web-service interface

[https://cis.ctsi.mcw.edu/nlp/nlpservice](https://cis.ctsi.mcw.edu/nlp/nlpservice)

$curl -H "Content-Type: application/json" -X POST -d '{"recordlist":["Jay is elderly","Treated for illusions of grandeur","Family history of CAD"]}'  https://cis.ctsi.mcw.edu/nlp/nlpservice

{ "nlplist": [{"entitylist":[]}, {"entitylist":[{"begin":"12","end":"21","coveredText":"illusions","codingScheme":"SNOMEDCT_US","cui":"C0020903","tui":"T048","preferredText":"Illusions","typeIndex":"81","annotations":["5152006"]}]}, {"entitylist":[{"begin":"0","end":"14","coveredText":"Family history","codingScheme":"SNOMEDCT_US","cui":"C0241889","tui":"T033","preferredText":"Family history","typeIndex":"81","annotations":["416471007","57177007"]},{"begin":"7","end":"14","coveredText":"history","codingScheme":"SNOMEDCT_US","cui":"C0262926","tui":"T033","preferredText":"Medical History","typeIndex":"81","annotations":["392521001"]},{"begin":"18","end":"21","coveredText":"CAD","codingScheme":"SNOMEDCT_US","cui":"C1956346","tui":"T047","preferredText":"Coronary Artery Disease","typeIndex":"81","annotations":["414024009","53741008"]}]}

### License
"CTSI MCW NLP" is licensed under the GNU General Public License (v3 or later; in general "CTSI MCW NLP" code is GPL v2+, but "CTSI MCW NLP" uses several Apache-licensed libraries, and so the composite is v3+). Note that the license is the full GPL, which allows many free uses, but not its use in proprietary software which is distributed to others. For distributors of proprietary software, "CTSI MCW NLP" is also available from CTSI of Southeast Wisconsin under a commercial licensing You can contact us at jay.urbain@gmail.com. 

The application uses [Apache cTakes] (http://ctakes.apache.org/) for named entity identification.

Savova, Guergana K., et al. "Mayo clinical Text Analysis and Knowledge Extraction System (cTAKES): architecture, component evaluation and applications." Journal of the American Medical Informatics Association 17.5 (2010): 507-513. [JAMIA](https://academic.oup.com/jamia/article/17/5/507/830823/Mayo-clinical-Text-Analysis-and-Knowledge).

