/*
 * Tests imported from http://code.google.com/p/json-path/ to
 * test some 'real-world' examples. json-path is licensed under
 * the Apache 2 license. http://www.apache.org/licenses/LICENSE-2.0
 */
package cc.spray.json

import org.specs2.mutable.Specification

class JsonPathTests extends Specification with SpecHelpers {
  import JsonLenses._
  import DefaultJsonProtocol._

  val json = JsonParser(
    """
      |{ "store": {
      |    "book": [
      |      { "category": "reference",
      |        "author": "Nigel Rees",
      |        "title": "Sayings of the Century",
      |        "price": 8.95
      |      },
      |      { "category": "fiction",
      |        "author": "Evelyn Waugh",
      |        "title": "Sword of Honour",
      |        "price": 12.99,
      |        "isbn": "0-553-21311-3"
      |      }
      |    ],
      |    "bicycle": {
      |      "color": "red",
      |      "price": 19.95
      |    }
      |  }
      |}
    """.stripMargin)

  "Examples" should {
    "All authors" in {
      json extract ("store" / "book" / * / "author").get[String] must be_==(Seq("Nigel Rees", "Evelyn Waugh"))
    }
    "Author of first book" in {
      json extract ("store" / "book" / element(0) / "author").get[String] must be_==("Nigel Rees")
    }
    "Books with category 'reference'" in {
      json extract ("store" / "book" / filter("category".is[String](_ == "reference")) / "title").get[String] must be_==(Seq("Sayings of the Century"))
    }
    "Books that cost more than 10 USD" in {
      json extract ("store" / "book" / filter("price".is[Double](_ >= 10)) / "title").get[String] must be_==(Seq("Sword of Honour"))
    }
    "All books that have isbn" in {
      json extract ("store" / "book" / filter("isbn".is[JsValue](_ => true)) / "title").get[String] must be_==(Seq("Sword of Honour"))
    }
    "All prices" in todo
  }
}
