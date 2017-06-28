package utility.metric

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit._

import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.codahale.metrics.{MetricFilter, SharedMetricRegistries}
import config.AppConfig

/**
  * Created by jason on 23/06/17.
  */
object Graphite {

  lazy val config = AppConfig

  val graphite = new Graphite(new InetSocketAddress(
    config.metricHost, config.metricPort))

  lazy val prefix: String = config.metricPrefix

  val reporter = GraphiteReporter.forRegistry(
    SharedMetricRegistries.getOrCreate(prefix))
    .prefixedWith(s"$prefix.${java.net.InetAddress.getLocalHost.getHostName}")
    .convertRatesTo(SECONDS)
    .convertDurationsTo(MILLISECONDS)
    .filter(MetricFilter.ALL)
    .build(graphite)

  def startGraphite(): Unit = {
    if (config.metricEnabled) {
      reporter.start(config.metricRefreshInterval, SECONDS)
    }
  }
}
