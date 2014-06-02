play2-choosenlang
=====================================

This module allows you to choose and remember language into cookie.
I wrote it, because in play version 2.2.x there is no such functionality,
although in version 1.x it was.


Installation
-------------------------

Add dependency declarations into your build.sbt or Build.scala file:

* ___build.sbt___

        "me.mnedokushev" %% "play2-choosenlang" % "1.0-SNAPSHOT"

* ___Build.scala___

```scala
  val appDependencies = Seq(
    "me.mnedokusev" %% "play2-choosenlang" % "1.0-SNAPSHOT"
  )
```

Usage
---------------------------

```scala

    // Examples

    import play.api.i18n.ChoosenLang

    // Get choosen language
    def getLang = Action { implicit request =>
      Ok(ChoosenLang.get.code) // Prints language from cookies or first 
                               // of application.langs from application.conf.
    }

    // Change language
    def changeLang(lang: String) = Action { implicit request =>
      ChoosenLang.changeTo(Lang(lang)) match {
        case Some(c) => Ok("Language changed to " + lang).withCookies(c)
        case None    => BadRequest("Sorry, Can't change language. To solve this problem - add needed language into application.conf")
      }
    }
      
```
