set CLASSPATH=%CLASSPATH%;.;c:\jade\lib\jade.jar;c:\jade\lib\jadeTools.jar;c:\jade\lib\Base64.jar;c:\jade\lib\http.jar;c:\Users\gladi\Protege_3.4.8\plugins\nl.uva.psy.swi.beangenerator\beangenerator_4.1.jar;c:\jade\lib\commons-codec\commons-codec-1.3.jar
::Buyers
cd C:\Users\gladi\Documents\GitHub\JadeEnglishAuction_WithOntology\Buyer\out\production\Buyer

java jade.Boot -container -container-name buyer -host localhost -agents Buyer1:jade.BookBuyerAgent
