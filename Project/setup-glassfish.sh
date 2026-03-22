#!/bin/sh
set -x

echo "AS_ADMIN_PASSWORD=" > /tmp/glassfish_pwd
echo "AS_ADMIN_NEWPASSWORD=adminadmin" >> /tmp/glassfish_pwd

echo "DB_USER is: ${DB_USER}"
echo "DB_HOST is: ${DB_HOST}"
echo "DB_NAME is: ${DB_NAME}"

echo "Checking JDBC driver..."
ls -l ${GLASSFISH_HOME}/glassfish/lib/postgresql.jar

asadmin start-domain domain1

echo "Waiting for GlassFish to start..."
sleep 15

# Зміна пароля
asadmin --user admin --passwordfile /tmp/glassfish_pwd change-admin-password
echo "AS_ADMIN_PASSWORD=adminadmin" > /tmp/glassfish_pwd

asadmin --user admin --passwordfile /tmp/glassfish_pwd set server-config.admin-service.jmx-connector.system.security-enabled=false

# 3. Ігнорування перевірки хостів у SSL
asadmin --user admin --passwordfile /tmp/glassfish_pwd create-jvm-options "-Djava.rmi.server.hostname=127.0.0.1"
asadmin --user admin --passwordfile /tmp/glassfish_pwd create-jvm-options "-Dcom.sun.enterprise.admin.admin-service.no_hostname_verification=true"

# Вмикаємо Secure Admin
asadmin --user admin --passwordfile /tmp/glassfish_pwd enable-secure-admin

# Створюємо Connection Pool
asadmin --user admin --passwordfile /tmp/glassfish_pwd create-jdbc-connection-pool \
    --restype jakarta.sql.DataSource \
    --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
    --property User=${DB_USER}:Password=${DB_PASSWORD}:DatabaseName=${DB_NAME}:ServerName=${DB_HOST}:PortNumber=5432 \
    forumPool

# Створюємо JDBC Resource (JNDI)
asadmin --user admin --passwordfile /tmp/glassfish_pwd create-jdbc-resource --connectionpoolid forumPool jdbc/forumDS

# 7. Перезавантажуємо домен
asadmin stop-domain domain1
rm /tmp/glassfish_pwd

echo "GlassFish is starting with Secure Admin enabled..."
exec asadmin start-domain --verbose domain1