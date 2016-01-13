INSTALLATION PROCEDURE OF ECFEED AUTOMATED TESTS OF BROADLEAF COMMERCE DEMO SITE
It includes:
	- Setting Broadleaf Commerce Demo Site with the use of HSQL as database.
	- Running tests controlled by ecFeed, with the use of Selenium driver as automation interface.

- Install HSQL in any directory e.g. ~/HSQL

- Install Eclipse and ecFeed plugin.

- Download and unzip HSQL in any directory 
	For example: download and unzip hsqldb-2.3.3.zip into ~/HSQL directory

- Install Maven (recent version 3.3.9 did not work properly with broadleaf - an older version: 3.2.5 works)
	Example how to install maven 3.2.5 on Linux, in the directory: /usr/local/apache-maven-3.2.5
	  wget http://apache.arvixe.com/maven/maven-3/3.2.5/binaries/apache-maven-3.2.5-bin.tar.gz
	  tar -zxf apache-maven-3.2.5-bin.tar.gz
	  sudo cp -R apache-maven-3.2.5 /usr/local
	  sudo ln -s /usr/local/apache-maven-3.2.5/bin/mvn /usr/bin/mvn
	  mvn -version 

- Set environment variables required by Broadleaf
  - Add evironment variable: M2_HOME - this should be the directory, where maven is installed
  - Add environment variable: M2 - this should be a bin subdirectory of M2_HOME directory
  - Update PATH variable to include M2 path.
  	Example: For Linux, when path to installed maven is: /usr/local/apache-maven-3.2.5, add the following lines to your ~/.profile file:
		export M2_HOME=/usr/local/apache-maven-3.2.5
		export M2=$M2_HOME/bin
		export PATH=$M2:$PATH

- Install Broadleaf Commerce site according to the tutorial (downloaded from git repository): ecfeed.samples/selenium/broadleaf/doc/Broadleaf - Getting Started.odt. 
  Before installing take into account the following remarks:
   - Do not download demo site workspace from Broadleaf site. Instead use: ecfeed.samples/selenium/broadleaf/demo/DemoSite-4.0.5-GA-eclipse-workspace.zip
   - Broadleaf database files are located in the subdirectory: eclipse-workspace/DemoSite/site/data/broadleaf, where zip was unpacked.
   - There are some errors after loading the DemoSite project - use Automatic Quick fix to correct them. There are also many warnings.
   - Set maven.home in DemoSite/build.properties as: maven.home=[path_to_apache_maven]
	Example: maven.home=/usr/local/apache-maven-3.2.5
   - Do not start Broadleaf site - HSQL server should be started first.

- Start HSQL Server:
	- change current directory to [hsql_installation_directory]/hsqldb/lib
	  	Example: cd ~/HSQL/hsqldb-2.3.3/hsqldb/lib
	- start the server with Broadleaf database.
  		Example: java -cp hsqldb.jar org.hsqldb.server.Server --database.0 [path_to_unzipped_workspace]/eclipse-workspace/DemoSite/site/data/broadleaf --dbname.0 broadleaf

- Optionally run SQL Manager: 
	- change current directory to [hsql_installation_directory]/hsqldb/lib
		Example: cd ~/HSQL/hsqldb-2.3.3/hsqldb/lib 
	- start the manager 
	  	Example: java -cp hsqldb.jar org.hsqldb.util.DatabaseManagerSwing 

  	Example of Connection properties in Connect dialog of SQL Manager: 
		Type: HSQL Database Engine Server
		Driver: org.hsqldb.jdbcDriver
		URL: jdbc:hsqldb:hsql://localhost/broadleaf
		User: SA
		Password: (empty)


- Start Broadleaf application:
  - Package Explorer > MavenParents > DemoSite > Run as > Maven install
  - Ant script viewer (should be on the right side of Eclipse) > site > double click tomcat (this initiates SQL database)

- When all is installed and ready: 
  paste the address http://localhost:8080/ in Mozzilla Firefox. A Heat Clinic website should be visible.

- Check that the website uses HSQL as database:
  - Register any user in the site.
  - In the SQL Window paste: SELECT * FROM "PUBLIC"."BLC_CUSTOMER" click Execute SQL button. One user record with previously registered login and password should be visible.

- Load broadleaf.test project from git (ecfeed.samples/selenium/broadleaf/test). 
- Configure BuildPath to add ecfeed.jar with the same version as ecFeed plugin which was previously installed. 
  It may be necessary to correct some test source files to match the installed version of ecfeed.jar.

- Run tests from ecFeed. Remarks:
  - Before running the test, database must be in its initial state (empty). 
  - It is not recommended to cancel a running test under debugger because the database would not be cleaned then.

To restart the site after shutdown:
- Start HSQL Server.
- Start Broadleaf application (as described above).



