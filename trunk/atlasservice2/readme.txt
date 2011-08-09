Administrative commands
-----------------------

With ant (buildfile = etc/build.xml. Make sure that the the buildfile's 
wepadd.dir property is properly set):

Deploying Domain data
---------------------

To deploy Swanson's 1998 Atlas

  ant loadAtlasSwanson1998
  
To deploy Swanson's 1998 Data Maps

  ant loadDatamapsSwanson1998
  
Utilities
---------

To upload an Atlas definition:

  ant -DadminCommand.file="atlases/swanson1998/Atlas.xml" load

To list stored atlas definitions:

  ant list

To delete a stored atlas:
  
  ant -DadminCommand.atlasUri="http://bmkeg.isi.edu/atlas/swanson1998" delete
  
To list loaded data maps:

  ant -DadminCommand.atlasUri="http://bmkeg.isi.edu/atlas/swanson1998" listdms

To list one data map:

  ant -DadminCommand.datamapUri="http://bmkeg.isi.edu/datamaps/ACB_PHAL" listdm

To delete a data map:

  ant -DadminCommand.datamapUri="http://bmkeg.isi.edu/datamaps/ACB_PHAL" deletedm

To upload a Data Map definition:

  ant -DadminCommand.file="datamaps/swanson1998/ACB_PHAL.xml" loaddm

To upload Data Map definitions from a directory:

  ant -DadminCommand.file="datamaps/swanson1998/" loaddms
  
To upload Brain Region definitions:

  ant -DadminCommand.file="brain_regions_definition_file.xml" loadbr



With maven:

To upload an Atlas definition:

  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="load etc/atlases/swanson1998/Atlas.xml"

To list stored atlas definitions:

  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="list"

To delete a stored atlas:
  
  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="delete http://bmkeg.isi.edu/atlas/swanson1998"
  
To upload a Data Map definition:

  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="loaddm etc/datamaps/swanson1998/LSc.d_PHAL.xml"

To upload Data Map definitions from a directory:

  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="loaddms etc/datamaps/swanson1998/"

To list loaded data maps:

  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="listdms http://bmkeg.isi.edu/atlas/swanson1998"

To list one data map:

  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="listdm http://bmkeg.isi.edu/datamaps/LSc.d_PHAL"

To delete a data map:

  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="deletedm http://bmkeg.isi.edu/datamaps/LSc.d_PHAL"

To upload Brain Region definitions:

  mvn exec:java -Dexec.mainClass="edu.isi.bmkeg.neuart.atlasserver.admin.AtlasAdmin" -Dexec.args="loadbr etc/atlases/swanson2004/BrainRegions.xml"


Deployment notes:
----------------

1. unpack atlasservice2-etc.tar in the neuart configuration directory (e.g., /usr/local/neuart/etc) and revise configuration.

1.1 copy the admin ant buildfile to the user directory (e.g., cd /usr/local/neuart; cp etc/build.xml .).

2. unpack atlasservice2-data.tar.bz2 the neuart data directory (e.g., /usr/local/neuart/data)

3. copy atlasservice2.war in the tomcat webapps directory.

4. As the tomcat user copy the content of the data/webresources directory into the application's web directory
   (e.g., cd /usr/share/tomcat5/webapps/atlasservice2; 
   cp -R /usr/local/neuart2/data/webresources/* .)
      
5. Set the neuart homedirectory in the "homedirectory" parameter
   in $CATALINA_HOME/conf/[enginename]/[hostname]/atlasservice2.xml 
   (e.g., /usr/share/tomcat5/conf/Catalina/localhost/atlasservice2.xml).
   The homedirectory is the directory that contains the configuration settings
    (e.g., file:/usr/local/neuart/etc).  

6. Deploy data in DB 
   6.1. Create the db and user indicated in the bmkeg.properties file
   
   6.2 cd to the homedirectory (e.g., /usr/local/neuart/)
   
   6.3. Revise catalina.home, webapps.dir, and the data.dir property in the build.xml file.
   
   6.4  deploy atlas definition to the DB:
        ant loadAtlasSwanson2004
        
   6.5 deploy datamaps definitions to the db:
       ant loadDatamapsSwanson2004       
          
7. Restart tomcat

Updating the War file

To update the war application only steps 3, 4, and 5 need to be performed. Before
copying the updated war file in the webapps directory save a copy of the customized
XML context file (e.g., /usr/share/tomcat5/conf/Catalina/localhost/atlasservice2.xml)
to copy it over the default XML context file that tomcat creates while expanding 
war files.


Building Distribution
---------------------

Tasks:

Cleaning:  mvn clean
  
Packaging:  

  mvn package

    Generates sources, compile, run unit tests, and  then the war file.
    
  mvn assembly:single
  
    Generates accessory packages for:
    - data (atlasservice2-data.tar)
    - configuration (atlasservice2-etc.tar)
    - client (neuartClient.tar)
    



