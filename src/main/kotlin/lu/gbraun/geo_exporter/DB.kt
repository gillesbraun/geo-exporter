package lu.gbraun.geo_exporter

import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import io.ebean.datasource.DataSourceConfig
import java.sql.Connection
import java.sql.DriverManager

lateinit var dbConn: Connection

fun connect() {
    val host = System.getenv("DB_HOST")
    val dbName = System.getenv("DB_DATABASE")
    val user = System.getenv("DB_USER")
    val password = System.getenv("DB_PASSWORD")
    val url = "jdbc:postgresql://$host/$dbName"
    dbConn = DriverManager.getConnection(url, user, password)

    val dataSourceConfig = DataSourceConfig()
    dataSourceConfig.username = user
    dataSourceConfig.password = password
    dataSourceConfig.url = url

    val config = DatabaseConfig()
    config.dataSourceConfig = dataSourceConfig

    DatabaseFactory.create(config)
}