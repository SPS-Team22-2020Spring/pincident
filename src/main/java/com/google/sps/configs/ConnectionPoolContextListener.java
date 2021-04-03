/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.sps.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Arrays;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener("Creates a connection pool that is stored in the Servlet's context for later use.")
public final class ConnectionPoolContextListener implements ServletContextListener {

  // Saving credentials in environment variables is convenient, but not secure - consider a more
  // secure solution such as https://cloud.google.com/kms/ to help keep secrets safe.
  private static final SecretManager secretManager = new SecretManager();
  private static String CLOUD_SQL_CONNECTION_NAME;
  private static String DB_USER;
  private static String DB_PASS;
  private static String DB_NAME;

  public ConnectionPoolContextListener() throws Exception {
    CLOUD_SQL_CONNECTION_NAME = secretManager
        .getSecret("projects/746439854120/secrets/CLOUD_SQL_CONNECTION_NAME/versions/1");
    DB_USER = secretManager.getSecret("projects/746439854120/secrets/DB_USER/versions/2");
    DB_PASS = secretManager.getSecret("projects/746439854120/secrets/DB_PASS/versions/2");
    DB_NAME = secretManager.getSecret("projects/746439854120/secrets/DB_NAME/versions/1");
  }

  private DataSource createConnectionPool() throws RuntimeException {
    // [START cloud_sql_mysql_servlet_create]
    // Note: For Java users, the Cloud SQL JDBC Socket Factory can provide authenticated connections
    // which is preferred to using the Cloud SQL Proxy with Unix sockets.
    // See https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory for details.

    // The configuration object specifies behaviors for the connection pool.
    HikariConfig config = new HikariConfig();

    // The following URL is equivalent to setting the config options below:
    // jdbc:mysql:///<DB_NAME>?cloudSqlInstance=<CLOUD_SQL_CONNECTION_NAME>&
    // socketFactory=com.google.cloud.sql.mysql.SocketFactory&user=<DB_USER>&password=<DB_PASS>
    // See the link below for more info on building a JDBC URL for the Cloud SQL JDBC Socket Factory
    // https://github.com/GoogleCloudPlatform/cloud-sql-jdbc-socket-factory#creating-the-jdbc-url

    // Configure which instance and what database user to connect with.
    config.setJdbcUrl(String.format("jdbc:mysql:///%s", DB_NAME));
    config.setUsername(DB_USER); // e.g. "root", "mysql"
    config.setPassword(DB_PASS); // e.g. "my-password"

    config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
    config.addDataSourceProperty("cloudSqlInstance", CLOUD_SQL_CONNECTION_NAME);

    // The ipTypes argument can be used to specify a comma delimited list of preferred IP types
    // for connecting to a Cloud SQL instance. The argument ipTypes=PRIVATE will force the
    // SocketFactory to connect with an instance's associated private IP.
    config.addDataSourceProperty("ipTypes", "PUBLIC");

    // Initialize the connection pool using the configuration object.
    // [END cloud_sql_mysql_servlet_create]
    return new HikariDataSource(config);
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
    // This function is called when the Servlet is destroyed.
    HikariDataSource pool = (HikariDataSource) event.getServletContext().getAttribute("my-pool");
    if (pool != null) {
      pool.close();
    }
  }

  @Override
  public void contextInitialized(ServletContextEvent event) {
    // This function is called when the application starts and will safely create a connection pool
    // that can be used to connect to.
    ServletContext servletContext = event.getServletContext();
    DataSource pool = (DataSource) servletContext.getAttribute("my-pool");
    if (pool == null) {
      try {
        pool = createConnectionPool();
      } catch (RuntimeException e) {
        System.out.println(Arrays.toString(e.getStackTrace()));
      }
      servletContext.setAttribute("my-pool", pool);
    }
  }
}