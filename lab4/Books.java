import tester.Tester;

// represents a book in the library system
interface IBook {
  // produces the number of days this book is overdue given today
  int daysOverdue(int today);

  // returns whether this book is overdue
  boolean isOverdue(int today);
}

// represents a abstract book
abstract class ABook implements IBook {
  String title;
  int dayTaken;

  ABook(String title, int dayTaken) {
    this.title = title;
    this.dayTaken = dayTaken;
  }

  public int daysOverdue(int today) {
    return today - 14 - this.dayTaken;
  }

  public boolean isOverdue(int today) {
    return this.daysOverdue(today) > 0;
  }
}

// represents a regular book
class Book extends ABook {
  String author;

  Book(String title, String author, int dayTaken) {
    super(title, dayTaken);
    this.author = author;
  }
}

// represents a reference book
class RefBook extends ABook {

  RefBook(String title, int dayTaken) {
    super(title, dayTaken);
  }

  public int daysOverdue(int today) {
    return today - 2 - this.dayTaken;
  }
}

// represents an audio book
class AudioBook extends ABook {
  String author;

  AudioBook(String title, String author, int dayTaken) {
    super(title, dayTaken);
    this.author = author;
  }
}

class ExamplesBook {
  IBook book = new Book("Fundies II", "Matthias", 100);
  IBook refBook = new RefBook("Dictionary", 105);
  IBook audioBook = new AudioBook("Lord of the Rings", "J R Tolkien", 87);

  boolean testDaysOverdue(Tester t) {
    return t.checkExpect(this.book.daysOverdue(120), 6)
        && t.checkExpect(this.book.daysOverdue(105), -9)
        && t.checkExpect(this.refBook.daysOverdue(107), 0)
        && t.checkExpect(this.audioBook.daysOverdue(103), 2);
  }
  
  boolean testIsOverdue(Tester t) {
    return t.checkExpect(this.book.isOverdue(120), true)
        && t.checkExpect(this.book.isOverdue(105), false)
        && t.checkExpect(this.refBook.isOverdue(107), false)
        && t.checkExpect(this.audioBook.isOverdue(103), true);
  }
}