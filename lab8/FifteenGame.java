import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;
import tester.*;
import javalib.impworld.*;
import javalib.worldimages.*;
import java.awt.Color;

// class to hold utilities
class Utils {
  // produces new array list containing all the items of the given list that pass
  // the predicate
  <T> ArrayList<T> filter(ArrayList<T> arr, Predicate<T> pred) {
    ArrayList<T> temp = new ArrayList<T>();

    // counted for loop
//    for (int i = 0; i < arr.size(); i += 1) {
//      if (pred.test(arr.get(i))) {
//        temp.add(arr.get(i));
//      }
//    }

    // for each loop
    for (T item : arr) {
      if (pred.test(item)) {
        temp.add(item);
      }
    }

    return temp;
  }

  // modifies the given list to remove everything that fails the predicate
  <T> void removeExcept(ArrayList<T> arr, Predicate<T> pred) {

    ArrayList<T> temp = new ArrayList<T>(arr);

    for (T item : temp) {
      if (!pred.test(item)) {
        arr.remove(item);
      }
    }

//    ArrayList<T> temp = new ArrayList<T>();
//    // for each loop
//    for (T item : arr) {
//      if (!pred.test(item)) {
//        temp.add(item);
//      }
//    }
//
//    for (T item : temp) {
//      arr.remove(item);
//    }

//    
//    int index = 0;
//    
//    while (index < arr.size()) {
//      T item = arr.get(index);
//      if (!pred.test(item)) {
//        arr.remove(item);
//      } else {
//        index += 1;
//      }
//    }

  }
}

// predicate for odd numbers
class IsOdd implements Predicate<Integer> {
  @Override
  public boolean test(Integer t) {
    return t % 2 == 1;
  }
}

class ExamplesFifteenGame {
  void testFilter(Tester t) {
    ArrayList<Integer> list1 = new ArrayList<Integer>();
    list1.add(1);
    list1.add(2);
    list1.add(7);
    Utils u = new Utils();
    ArrayList<Integer> expect = new ArrayList<Integer>(Arrays.asList(1, 7));
    t.checkExpect(u.filter(list1, new IsOdd()), expect);
  }

  void testRemoveExcept(Tester t) {
    ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(1, 3, 4, 5, 7, 8));
    Utils u = new Utils();
    ArrayList<Integer> expect = new ArrayList<Integer>(Arrays.asList(1, 3, 5, 7));
    u.removeExcept(list1, new IsOdd());
    t.checkExpect(list1, expect);
  }

  void testPlusPlus(Tester t) {
    // prefer using i += 1 over i++ since plus plus gives is hard to reason
    // about in complicated scenarios
    int i = 0;
    int j = 1;
    int k = i++ + ++j; // k = 2;

    t.checkExpect(i++, 0);
    t.checkExpect(++i, 2);
  }
}