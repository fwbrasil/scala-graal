package bench;

public class VTableTest {

  class Mammal {
    public void speak() {
      System.out.println("oh");
    }
  }

  class Human extends Mammal {

    @Override
    public void speak() {
      System.out.println("Hello");
    }

    // Valid overload of speak
    public void speak(String language) {
      if (language.equals("Hindi"))
        System.out.println("Namaste");
      else
        System.out.println("Hello");
    }

    @Override
    public String toString() {
      return "Human Class";
    }
  }

}

class I {

  interface Mammal {
    void speak();
  }

  interface Named {
    String name();
  }

  class Human implements Mammal, Named {

    @Override
    public String name() {
      return "Someone";
    }

    @Override
    public void speak() {
      System.out.println("hi there");
    }
  }

}
