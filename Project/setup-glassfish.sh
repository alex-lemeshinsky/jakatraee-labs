#!/bin/sh
set -e

export AS_ADMIN_TRUST_ALL_CERTIFICATES=true

PWD_FILE="/opt/glassfish_pwd"
echo "AS_ADMIN_PASSWORD=" > "$PWD_FILE"
echo "AS_ADMIN_NEWPASSWORD=adminadmin" >> "$PWD_FILE"

asadmin start-domain domain1
sleep 10

# Зміна пароля
asadmin --user admin --passwordfile "$PWD_FILE" change-admin-password
echo "AS_ADMIN_PASSWORD=adminadmin" > "$PWD_FILE"

# ВИМИКАЄМО SSL
asadmin --user admin --passwordfile "$PWD_FILE" set configs.config.server-config.admin-service.jmx-connector.system.security-enabled=false

asadmin --user admin --passwordfile "$PWD_FILE" set configs.config.server-config.network-config.protocols.protocol.admin-listener.security-enabled=false

asadmin --user admin --passwordfile "$PWD_FILE" create-jvm-options "-Dcom.sun.enterprise.admin.admin-service.no_hostname_verification=true"
asadmin --user admin --passwordfile "$PWD_FILE" create-jvm-options "-Djava.rmi.server.hostname=127.0.0.1"

# Вмикаємо Secure Admin
asadmin --user admin --passwordfile "$PWD_FILE" enable-secure-admin

# Створюємо пул та ресурси
asadmin --user admin --passwordfile "$PWD_FILE" add-library /opt/glassfish7/glassfish/lib/postgresql.jar

asadmin --user admin --passwordfile "$PWD_FILE" create-jdbc-connection-pool \
    --restype javax.sql.DataSource \
    --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
    --property user=${DB_USER}:password=${DB_PASSWORD}:databaseName=${DB_NAME}:serverName=${DB_HOST}:portNumber=5432 \
    forumPool

asadmin --user admin --passwordfile "$PWD_FILE" create-jdbc-resource --connectionpoolid forumPool jdbc/forumDS
asadmin --user admin --passwordfile "$PWD_FILE" deploy --force /opt/glassfish7/glassfish/domains/domain1/autodeploy/app.war

asadmin stop-domain domain1
rm "$PWD_FILE"

echo "--- GLASSFISH SETUP SUCCESSFUL ---"
exec asadmin start-domain --verbose domain1