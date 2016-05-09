set CLASSPATH=%CLASSPATH%;.;c:\jade\lib\jade.jar;c:\jade\lib\jadeTools.jar;c:\jade\lib\Base64.jar;c:\jade\lib\http.jar;c:\Users\gladi\Protege_3.4.8\plugins\nl.uva.psy.swi.beangenerator\beangenerator_4.1.jar;c:\jade\lib\commons-codec\commons-codec-1.3.jar;
::Seller
cd C:\Users\gladi\Documents\GitHub\JadeEnglishAuction_WithOntology\Seller\out\production\Seller

java jade.Boot -gui -agents Seller:jade.BookSellerAgent

pause