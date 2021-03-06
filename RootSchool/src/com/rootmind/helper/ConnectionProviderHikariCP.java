package com.rootmind.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionProviderHikariCP {

	private static final ConnectionProviderHikariCP INSTANCE;
	private final HikariDataSource hikariDataSource;


	static {
		INSTANCE = new ConnectionProviderHikariCP();
	}

	private ConnectionProviderHikariCP() {
		

		final String CLOUD_SQL_CONNECTION_NAME = System.getenv("CLOUD_SQL_INSTANCE_NAME");
		final String DB_USER = System.getenv("DB_USER");
		final String DB_PASS = System.getenv("DB_PASS");
		final String DB_NAME = System.getenv("DB_NAME");

//		System.out.println("cloudsql " + System.getProperty("cloudsql"));
//		System.out.println("CLOUD_SQL_CONNECTION_NAME " + System.getProperty("CLOUD_SQL_INSTANCE_NAME"));
		System.out.println("CLOUD_SQL_CONNECTION_NAME ENV " + System.getenv("CLOUD_SQL_INSTANCE_NAME"));

		
		HikariConfig config = new HikariConfig();

		// Configure which instance and what database user to connect with.
		config.setJdbcUrl(String.format("jdbc:mysql:///%s", DB_NAME));
		config.setUsername(DB_USER); // e.g. "root", "postgres"
		config.setPassword(DB_PASS); // e.g. "my-password"
		config.addDataSourceProperty("socketFactory", "com.google.cloud.sql.mysql.SocketFactory");
		config.addDataSourceProperty("cloudSqlInstance", CLOUD_SQL_CONNECTION_NAME);
		config.addDataSourceProperty("useSSL", "false");
		// [START cloud_sql_mysql_servlet_limit]
		// maximumPoolSize limits the total number of concurrent connections this pool
		// will keep. Ideal
		// values for this setting are highly variable on app design, infrastructure,
		// and database.
		config.setMaximumPoolSize(20);
		// minimumIdle is the minimum number of idle connections Hikari maintains in the
		// pool.
		// Additional connections will be established to meet this value unless the pool
		// is full.
		config.setMinimumIdle(5);
		// [END cloud_sql_mysql_servlet_limit]
		// [START cloud_sql_mysql_servlet_timeout]
		// setConnectionTimeout is the maximum number of milliseconds to wait for a
		// connection checkout.
		// Any attempt to retrieve a connection from this pool that exceeds the set
		// limit will throw an
		// SQLException.
		config.setConnectionTimeout(10000); // 10 seconds
		// idleTimeout is the maximum amount of time a connection can sit in the pool.
		// Connections that
		// sit idle for this many milliseconds are retried if minimumIdle is exceeded.
		config.setIdleTimeout(600000); // 10 minutes
		// [END cloud_sql_mysql_servlet_timeout]
		// [START cloud_sql_mysql_servlet_lifetime]
		// maxLifetime is the maximum possible lifetime of a connection in the pool.
		// Connections that
		// live longer than this many milliseconds will be closed and reestablished
		// between uses. This
		// value should be several minutes shorter than the database's timeout value to
		// avoid unexpected
		// terminations.
		config.setMaxLifetime(1800000); // 30 minutes
		// [END cloud_sql_mysql_servlet_lifetime]
		// Initialize the connection pool using the configuration object.
		
		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
		config.addDataSourceProperty("useServerPrepStmts", "true");
		
		hikariDataSource = new HikariDataSource(config);
		
		System.out.println("HikariConfig");

	}

	public static ConnectionProviderHikariCP getInstance() {
		return INSTANCE;
	}

	

	public Connection getConnection() throws SQLException {
			return hikariDataSource.getConnection();

	}

}
