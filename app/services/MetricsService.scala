package services

/**
  * Created by jason on 28/06/17.
  */

import javax.inject._

import play.api.Logger
import play.api.inject.ApplicationLifecycle
import utility.metric.Graphite

@Singleton
class MetricsService @Inject()(appLifecycle: ApplicationLifecycle) {

  def start(): Unit = {
    lazy val graphite = Graphite
    graphite.startGraphite()
    Logger.info("Graphite Initialized!")
  }

  start()
}
