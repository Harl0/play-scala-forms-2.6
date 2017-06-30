package config

import scala.collection.JavaConverters._
import com.typesafe.config.{Config, ConfigFactory}

trait AppConfig {
  val config: Config
//  lazy val httpPort: Int = config.getInt("http.port")
//  lazy val mongoDatabase = config.getString("mongodb.database")
//  lazy val mongoServers = config.getStringList("mongodb.servers").asScala

  lazy val metricHost: String = config.getString("graphite.host")
  lazy val metricPort: Int = config.getInt("graphite.port")
  lazy val metricPrefix: String = config.getString("graphite.prefix")
  lazy val metricEnabled: Boolean = config.getBoolean("graphite.enabled")
  lazy val metricRefreshUnit: String = config.getString("graphite.refreshUnit")
  lazy val metricRefreshInterval: Int = config.getInt("graphite.refreshInterval")

  lazy val clientUrl: String = config.getString("client.url")
}

object AppConfig extends AppConfig {
  lazy val config: Config = ConfigFactory.load()
}
