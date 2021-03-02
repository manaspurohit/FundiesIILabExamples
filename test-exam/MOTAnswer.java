import tester.Tester;

// Represents a mode of transportation
interface IMOT {

  // determins if this mode of tranport is more fuel efficient than the given
  boolean motIsMoreFuelEfficientThan(IMOT mot);

  // returns true if this mode of transport is less fuel efficient than the given
  // mpg
  boolean isLessFuelEfficientThan(int mpg);
}

// Represents a bicycle as a mode of transportation
class Bicycle implements IMOT {
  String brand;

  Bicycle(String brand) {
    this.brand = brand;
  }

  public boolean motIsMoreFuelEfficientThan(IMOT mot) {
    return true;
  }

  public boolean isLessFuelEfficientThan(int mpg) {
    return false;
  }
}

// Represents a car as a mode of transportation
class Car implements IMOT {
  String make;
  int mpg; // represents the fuel efficiency in miles per gallon

  Car(String make, int mpg) {
    this.make = make;
    this.mpg = mpg;
  }

  public boolean motIsMoreFuelEfficientThan(IMOT mot) {
    return mot.isLessFuelEfficientThan(this.mpg);
  }

  public boolean isLessFuelEfficientThan(int mpg) {
    return this.mpg < mpg;
  }
}

// represents a list of modes of transportation
interface ILoMOT {
  ILoMOT motsOrderedByFuelEfficiency();

  ILoMOT putMOTInOrderedList(IMOT mot);
}

// represents an empty list of modes of transportation
class MtLoMOT implements ILoMOT {

  public ILoMOT motsOrderedByFuelEfficiency() {
    return new MtLoMOT();
  }

  public ILoMOT putMOTInOrderedList(IMOT mot) {
    return new ConsLoMOT(mot, new MtLoMOT());
  }
}

// represents a non-empty list of modes of transportation
class ConsLoMOT implements ILoMOT {
  IMOT first;
  ILoMOT rest;

  ConsLoMOT(IMOT first, ILoMOT rest) {
    this.first = first;
    this.rest = rest;
  }

  @Override
  public ILoMOT motsOrderedByFuelEfficiency() {
    return this.rest.motsOrderedByFuelEfficiency().putMOTInOrderedList(this.first);
  }

  @Override
  public ILoMOT putMOTInOrderedList(IMOT mot) {
    if (mot.motIsMoreFuelEfficientThan(this.first)) {
      return new ConsLoMOT(mot, this);
    } else {
      return new ConsLoMOT(this.first, this.rest.putMOTInOrderedList(mot));
    }
  }
}

// Keeps track of how a person is transported
class Person {
  String name;
  ILoMOT mots;

  Person(String name, ILoMOT mots) {
    this.name = name;
    this.mots = mots;
  }

  ILoMOT motsOrderedByFuelEfficiency() {
    return this.mots.motsOrderedByFuelEfficiency();
  }
}

class ExamplesMOT {
  IMOT diamondback = new Bicycle("Diamondback");
  IMOT toyota = new Car("Toyota", 30);
  IMOT lamborghini = new Car("Lamborghini", 17);

  ILoMOT mt = new MtLoMOT();
  Person bob = new Person("Bob", new ConsLoMOT(this.diamondback, this.mt));
  Person ben = new Person("Ben",
      new ConsLoMOT(this.toyota, new ConsLoMOT(this.lamborghini, this.mt)));
  Person becca = new Person("Becca", new ConsLoMOT(this.diamondback,
      new ConsLoMOT(this.lamborghini, new ConsLoMOT(this.toyota, this.mt))));

  boolean testIsMoreFuelEfficient(Tester t) {
    return t.checkExpect(this.toyota.motIsMoreFuelEfficientThan(diamondback), false)
        && t.checkExpect(this.diamondback.motIsMoreFuelEfficientThan(toyota), true);
  }

  boolean test(Tester t) {
    return t.checkExpect(bob.motsOrderedByFuelEfficiency(),
        new ConsLoMOT(this.diamondback, this.mt))
        && t.checkExpect(this.ben.motsOrderedByFuelEfficiency(),
            new ConsLoMOT(this.toyota, new ConsLoMOT(this.lamborghini, this.mt)))
        && t.checkExpect(this.becca.motsOrderedByFuelEfficiency(), new ConsLoMOT(this.diamondback,
            new ConsLoMOT(this.toyota, new ConsLoMOT(this.lamborghini, this.mt))));
  }
  
  // Assumption: a bicycle will be more fuel efficient than any mode of transport you can give it
  // even another bicycle
}