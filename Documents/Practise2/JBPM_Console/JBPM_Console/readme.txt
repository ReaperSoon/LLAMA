1. Import the project into MyEclipse 10.
2. Update jbpm.hibernate.cfg.xml under conf folder according to the databas setting.
   The following is the setting for oracle

     <property name="hibernate.dialect">org.hibernate.dialect.OracleDialect</property>
     <property name="hibernate.connection.driver_class">oracle.jdbc.OracleDriver</property>
     <property name="hibernate.connection.url">jdbc:oracle:thin:@localhost:1521:xe</property>
     <property name="hibernate.connection.username">jbpm</property>
     <property name="hibernate.connection.password">password</property>

3. Deploy the project to tomcat
4. Use "admin" as user name, no password to login