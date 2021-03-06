import com.google.inject.AbstractModule
import services.MetricsService

/**
  * Created by jason on 28/06/17.
  */
class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[MetricsService]).asEagerSingleton()
  }
}
