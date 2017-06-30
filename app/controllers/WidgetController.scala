package controllers

import javax.inject.Inject

import connectors.ClientConnector
import models.Widget
import play.api.Logger
import play.api.data._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import utility.metric.Metric

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * The classic WidgetController using I18nSupport.
  *
  * I18nSupport provides implicits that create a Messages instances from
  * a request using implicit conversion.
  */
class WidgetController @Inject()(cc: ControllerComponents, configuration: play.api.Configuration, clientConnector: ClientConnector) extends AbstractController(cc)
  with I18nSupport {

  import WidgetForm._

  val appSecretString = configuration.underlying.getString("play.http.secret.key")
  println("The appSecret is " + appSecretString)
  private val widgets = scala.collection.mutable.ArrayBuffer(
    Widget("Widget 1", 123),
    Widget("Widget 2", 456),
    Widget("Widget 3", 789)
  )

  private val postUrl = routes.WidgetController.createWidget()

  def index = Action {
    Ok(views.html.index())
  }

  def addWidget = Action { implicit request: Request[AnyContent] =>
    // Pass an unpopulated form to the template
    Ok(views.html.addWidgets(widgets, form, postUrl))
  }

  // This will be the action that handles our form post
  def createWidget = Action { implicit request: Request[AnyContent] =>
    val errorFunction = { formWithErrors: Form[Data] =>
      // This is the bad case, where the form had validation errors.
      // Let's show the user the form again, with the errors highlighted.
      // Note how we pass the form with errors to the template.
      Logger.info("Incrementing the mongo.writeFailure counter")
      Metric.defaultRegistry.counter("mongo.writeFailure").inc()
      BadRequest(views.html.addWidgets(widgets, formWithErrors, postUrl))
    }

    val successFunction = { data: Data =>
      // This is the good case, where the form was successfully parsed as a Data.
      val widget = Widget(name = data.name, price = data.price)
      widgets.append(widget)
      Logger.info("New widget added: " + data.name)
      Logger.info("Incrementing the mongo.writeSuccess counter")
      Metric.defaultRegistry.counter("mongo.writeSuccess").inc()
      Redirect(routes.WidgetController.addWidget()).flashing("info" -> "Widget added!")
    }

    val formValidationResult = form.bindFromRequest
    formValidationResult.fold(errorFunction, successFunction)
  }

  def retrieveAllClients: Action[AnyContent] = Action.async { implicit request =>
    clientConnector.getAllClients.map { x =>
      Ok(Json.toJson(x))
    }.recover {
      case ex: Exception => InternalServerError
    }
  }

  def retrieveAllClientsPage: Action[AnyContent] = Action.async { implicit request =>
    clientConnector.getAllClients.map { x =>
      Ok(views.html.listWidgets(x))
    }.recover {
      case ex: Exception => InternalServerError
    }
  }

}