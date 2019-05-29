package playground

class VTable {

  trait Named {
    def name: String
  }

  trait Mammal {
    def speak: String
  }

  class Human
    extends Named
    with Mammal {

    override def speak = "hello"
    override def name = "Someone"
  }

  def test(m: Mammal) =
    m.speak
}
