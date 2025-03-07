val common = Def.settings(
  scalaVersion := "3.6.4"
)

def sourceCode(method: String): Seq[String] = {
  (1 to 100).map { y =>
    val values = (1 to 10000).map(n => s"def x$n: ExecutionContext = $method[ExecutionContext]").mkString("\n")
    s"""|import scala.concurrent.ExecutionContext
        |import scala.concurrent.ExecutionContext.Implicits.global
        |
        |class A$y {
        |$values
        |}""".stripMargin
  }
}

val a1 = project
  .settings(
    common,
    Compile / sourceGenerators += task {
      sourceCode("implicitly").zipWithIndex.map { case (src, n) =>
        val f = (Compile / sourceManaged).value / s"A$n.scala"
        IO.write(f, src)
        f
      }
    }
  )

val a2 = project
  .settings(
    common,
    Compile / sourceGenerators += task {
      sourceCode("summon").zipWithIndex.map { case (src, n) =>
        val f = (Compile / sourceManaged).value / s"A$n.scala"
        IO.write(f, src)
        f
      }
    }
  )
