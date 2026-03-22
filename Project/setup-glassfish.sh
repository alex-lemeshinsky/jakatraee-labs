#!/bin/sh
set -e

PWD_FILE="/opt/glassfish_pwd"
echo "AS_ADMIN_PASSWORD=" > "$PWD_FILE"
echo "AS_ADMIN_NEWPASSWORD=adminadmin" >> "$PWD_FILE"

asadmin start-domain domain1
sleep 10

# Встановлюємо пароль
asadmin --user admin --passwordfile "$PWD_FILE" change-admin-password
echo "AS_ADMIN_PASSWORD=adminadmin" > "$PWD_FILE"

asadmin --user admin --passwordfile "$PWD_FILE" set server-config.network-config.protocols.protocol.admin-listener.security-enabled=false
asadmin --user admin --passwordfile "$PWD_FILE" set server-config.admin-service.jmx-connector.system.security-enabled=false
asadmin --user admin --passwordfile "$PWD_FILE" create-jvm-options "-Dcom.sun.enterprise.admin.admin-service.no_hostname_verification=true"
asadmin --user admin --passwordfile "$PWD_FILE" create-jvm-options "-Djava.rmi.server.hostname=localhost"

# Вмикаємо віддалений доступ
asadmin --user admin --passwordfile "$PWD_FILE" enable-secure-admin

# Реєструємо драйвер
asadmin --user admin --passwordfile "$PWD_FILE" add-library ${GLASSFISH_HOME}/glassfish/lib/postgresql.jar

asadmin --user admin --passwordfile "$PWD_FILE" create-jdbc-connection-pool \
    --restype javax.sql.DataSource \
    --datasourceclassname org.postgresql.ds.PGSimpleDataSource \
    --property User=${DB_USER}:Password=${DB_PASSWORD}:DatabaseName=${DB_NAME}:ServerName=${DB_HOST}:PortNumber=5432 \
    forumPool

asadmin --user admin --passwordfile "$PWD_FILE" create-jdbc-resource --connectionpoolid forumPool jdbc/forumDS

asadmin stop-domain domain1
rm "$PWD_FILE"

echo "GlassFish setup finished!"
exec asadmin start-domain --verbose domain1