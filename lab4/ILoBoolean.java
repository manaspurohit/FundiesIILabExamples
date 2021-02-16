import tester.Tester;

interface ILoBoolean {
  // appends the given list to this list
  ILoBoolean append(ILoBoolean given);
}

class MtLoBoolean implements ILoBoolean {

  public ILoBoolean append(ILoBoolean given) {
    return given;
  }
}

class ConsLoBoolean implements ILoBoolean {
  boolean first;
  ILoBoolean rest;

  ConsLoBoolean(boolean first, ILoBoolean rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoBoolean append(ILoBoolean given) {
    return new ConsLoBoolean(this.first, this.rest.append(given));
  }
}

class ExamplesILoBoolean {
  ILoBoolean mt = new MtLoBoolean();
  ILoBoolean one = new ConsLoBoolean(true, this.mt);
  ILoBoolean two = new ConsLoBoolean(false, this.one);
  ILoBoolean three = new ConsLoBoolean(false, this.two);
  
  boolean testAppend(Tester t) {
    return t.checkExpect(this.mt.append(this.mt), this.mt)
        && t.checkExpect(this.one.append(this.mt), this.one)
        && t.checkExpect(this.mt.append(this.two), this.two)
        && t.checkExpect(this.one.append(this.two), new ConsLoBoolean(true, this.two));
  }
}