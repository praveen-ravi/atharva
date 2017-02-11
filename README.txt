DB setup
1. Install MySql community


TomCat setup
1. Install Tomcat server
2. Edit tomcat/conf/tomcat-users.xml
    add:
    <role rolename="manager-gui"/>
    <user username="manager" password="manager" roles="manager-gui"/>
3. Edit tomcat/conf/server.xml
    edit:
    <Connector port="9090" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />
