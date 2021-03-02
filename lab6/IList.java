import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.Comparator;

import tester.Tester;

interface ILoString {
  // gives back a list with only the given String
  ILoString filterString(String given);
}

class MtLoString implements ILoString {

  @Override
  public ILoString filterString(String given) {
    return this;
  }

}

class ConsLoString implements ILoString {
  String first;
  ILoString rest;

  ConsLoString(String first, ILoString rest) {
    this.first = first;
    this.rest = rest;
  }

  @Override
  public ILoString filterString(String given) {
    if (given.equals(this.first)) {
      return new ConsLoString(this.first, this.rest.filterString(given));
    } else {
      return this.rest.filterString(given);
    }
  }
}

interface IList<T> {
  IList<T> filter(Predicate<T> pred);

  <U> IList<U> map(Function<T, U> converter);

  // foldr: [List-of X] Y [X Y -> Y] -> Y
  <U> U fold(BiFunction<T, U, U> converter, U initial);

  IList<T> sort(Comparator<T> comp);

  IList<T> insert(Comparator<T> comp, T val);
}

class MtList<T> implements IList<T> {

  MtList() {
  }

  @Override
  public IList<T> filter(Predicate<T> pred) {
    return new MtList<T>();
  }

  @Override
  public <U> IList<U> map(Function<T, U> converter) {
    return new MtList<U>();
  }

  @Override
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return initial;
  }

  @Override
  public IList<T> sort(Comparator<T> comp) {
    return this;
  }

  @Override
  public IList<T> insert(Comparator<T> comp, T val) {
    return new ConsList<T>(val, this);
  }
}

class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  @Override
  public IList<T> filter(Predicate<T> pred) {
    if (pred.test(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    } else {
      return this.rest.filter(pred);
    }
  }

  @Override
  public <U> IList<U> map(Function<T, U> converter) {
    return new ConsList<U>(converter.apply(this.first), this.rest.map(converter));
  }

  @Override
  public <U> U fold(BiFunction<T, U, U> converter, U initial) {
    return converter.apply(this.first, this.rest.fold(converter, initial));
  }

  @Override
  public IList<T> sort(Comparator<T> comp) {
    return this.rest.sort(comp).insert(comp, this.first);
  }

  @Override
  public IList<T> insert(Comparator<T> comp, T val) {
    if (comp.compare(val, this.first) < 0) {
      return new ConsList<T>(val, this);
    } else {
      return new ConsList<T>(this.first, this.rest.insert(comp, val));
    }
  }
}

class FilterString implements Predicate<String> {
  String given;

  FilterString(String given) {
    this.given = given;
  }

  @Override
  public boolean test(String t) {
    return t.equals(this.given);
  }
}

class FilterA implements Predicate<String> {

  @Override
  public boolean test(String t) {
    return t.equals("a");
  }

}

class CountMonthsEndEr implements BiFunction<String, Integer, Integer> {

  @Override
  public Integer apply(String t, Integer u) {
    if (t.endsWith("er")) {
      return u + 1;
    } else {
      return u;
    }
  }

}

class StringCompare implements Comparator<String> {

  @Override
  public int compare(String o1, String o2) {
    return o1.compareTo(o2);
  }

}

// Find how many months end with "er" (without writing a separate length() method for IList)
// -> fold

// Create a list of all the 3-letter abbreviations of each month. 
// The 3-letter abbreviation of a month is simply the first 3 letters of its name
// -> map

class ExamplesLists {
  ExamplesLists() {
  }

  ILoString example1 = new MtLoString();
  ILoString example2 = new ConsLoString("a", this.example1);
  ILoString example3 = new ConsLoString("b", this.example2);

  IList<String> list1 = new MtList<String>();
  IList<String> list2 = new ConsList<String>("a", this.list1);
  IList<String> list3 = new ConsList<String>("b", new MtList<String>());
  IList<String> list5 = new ConsList<String>("b", this.list2);

  IList<String> months = new ConsList<String>("June",
      new ConsList<String>("July", new ConsList<String>("August", new ConsList<String>("September",
          new ConsList<String>("October", new MtList<String>())))));

  // int -> Integer
  // boolean -> Boolean
  // double -> Double
  // char -> Character
  IList<Integer> list4 = new ConsList<Integer>(5, new MtList<Integer>());

  Predicate<String> filterA = new FilterString("a");
  Predicate<String> filterB = new FilterString("b");

  boolean testFilterString(Tester t) {
    return t.checkExpect(filterA.test("b"), false) && t.checkExpect(filterA.test("a"), true);
  }

  boolean testFilterStringILoString(Tester t) {
    return t.checkExpect(this.example3.filterString("a"), new ConsLoString("a", this.example1))
        && t.checkExpect(this.example3.filterString("b"), new ConsLoString("b", this.example1));
  }

  boolean testFilterStringIList(Tester t) {
    return t.checkExpect(this.list5.filter(filterA), this.list2)
        && t.checkExpect(this.list5.filter(filterB), this.list3);
  }

  boolean testFold(Tester t) {
    return t.checkExpect(this.months.fold(new CountMonthsEndEr(), 0), 2);
  }

  boolean testSort(Tester t) {
    return t.checkExpect(this.months.sort(new StringCompare()),
        new ConsList<String>("August",
            new ConsList<String>("July",
                new ConsList<String>("June", new ConsList<String>("October",
                    new ConsList<String>("September", new MtList<String>()))))));
  }

}