package utility.metric

import com.codahale.metrics.{MetricRegistry, SharedMetricRegistries}
import config.AppConfig

/**
  * Created by jason on 23/06/17.
  */
object Metric {
  lazy val config = AppConfig

  def defaultRegistry: MetricRegistry = SharedMetricRegistries.getOrCreate(config.metricPrefix)
}
