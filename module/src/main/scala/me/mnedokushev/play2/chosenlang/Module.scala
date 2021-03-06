package me.mnedokushev.play2.chosenlang

import play.api.mvc._
import play.api.i18n._
import play.api.Play.current
import scala.concurrent.Future

object ChosenLang {

  final val LANG_COOKIE_NAME = "PLAY_CHOSEN_LANG"

  def get[A]()(implicit request: Request[A], default: Lang = Lang("en")): Lang = {
    request.cookies.get(LANG_COOKIE_NAME) match {
      case Some(c) => Lang(c.value)
      case None    => isAvailable(request.acceptLanguages)
                        .getOrElse(Lang.availables.headOption.getOrElse(default))
    }
    
  }

  /** It's an analogue of Lang.preferred with one exception - this function
   * returns Option[Lang] instead of Lang.
   */
  private def isAvailable(langs: Seq[Lang]): Option[Lang] = 
    langs.collectFirst(Function.unlift { lang => Lang.availables.find(_.satisfies(lang)) })
  
  def changeTo(lang: Lang): Option[Cookie] = 
    isAvailable(Seq(lang)).map(l => Cookie(LANG_COOKIE_NAME, l.code))

}

case class ChosenLangRequest[A](chosenLang: Lang, request: Request[A]) extends WrappedRequest[A](request)

object ChosenLangAction extends ActionBuilder[ChosenLangRequest] {
  
  def invokeBlock[A](req: Request[A], block: (ChosenLangRequest[A]) => Future[SimpleResult]) = {
    block(ChosenLangRequest(ChosenLang.get()(request=req), req))
  }
  
}

trait ChosenLangController extends Controller {  
  override implicit def lang(implicit request: RequestHeader): Lang = {
    request match {
      case cl: ChosenLangRequest[_] => cl.chosenLang
      case _                        => Lang("en")
    }
  }
}

/**
 * Use it with play2-auth module. 
 */
import jp.t2v.lab.play2.stackc.{RequestWithAttributes, RequestAttributeKey, StackableController}

trait ChosenLangElement extends StackableController {
  this: Controller =>

  case object ChosenLangKey extends RequestAttributeKey[Lang]
  
  override def proceed[A](req: RequestWithAttributes[A])(f: RequestWithAttributes[A] => Future[Result]): Future[Result] = {
    super.proceed(req.set(ChosenLangKey, ChosenLang.get()(request=req)))(f)
  }

  override implicit def lang(implicit request: RequestHeader): Lang = {
    request match {
      case r:RequestWithAttributes[_] => r.get(ChosenLangKey).get
      case _ => Lang("en")
    }
  }

}
