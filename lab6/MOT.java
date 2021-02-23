// Represents a mode of transportation
interface IMOT {
}

// Represents a bicycle as a mode of transportation
class Bicycle implements IMOT {
  String brand;

  Bicycle(String brand) {
    this.brand = brand;
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
}

// represents a list of modes of transportation
interface ILoMOT {
}

// represents an empty list of modes of transportation
class MtLoMOT implements ILoMOT {
}

// represents a non-empty list of modes of transportation
class ConsLoMOT implements ILoMOT {
  IMOT first;
  ILoMOT rest;

  ConsLoMOT(IMOT first, ILoMOT rest) {
    this.first = first;
    this.rest = rest;
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
}