package play.api.i18n

import play.api.mvc._
import play.api.Play.current

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
