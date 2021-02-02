import tester.Tester;

//represents a game piece in the 2048 game
interface IGamePiece {
  // returns the value of a game piece
  int getValue();

  // combines this game piece with the given game piece to form a merged piece
  IGamePiece merge(IGamePiece other);

  // checks whether this game piece was created according to the rules of 2048:
  // only equal-valued pieces can merge.
  boolean isValid();
}

//represents a base tile in the 2048 game
class BaseTile implements IGamePiece {
  int value;

  BaseTile(int value) {
    this.value = value;
  }

  // returns the value of a base tile
  public int getValue() {
    return this.value;
  }

  // combines this base tile with the given game piece to form a merged piece
  public IGamePiece merge(IGamePiece other) {
    return new MergeTile(this, other);
  }

  // checks whether this base tile was created according to the rules of 2048:
  public boolean isValid() {
    return true;
  }

}

//represents a merged tile in the 2048 game
class MergeTile implements IGamePiece {
  IGamePiece piece1;
  IGamePiece piece2;

  MergeTile(IGamePiece piece1, IGamePiece piece2) {
    this.piece1 = piece1;
    this.piece2 = piece2;
  }

  // returns the value of a merge tile
  public int getValue() {
    return this.piece1.getValue() + this.piece2.getValue();
  }

  // combines this merge tile with the given game piece to form a merged piece
  public IGamePiece merge(IGamePiece other) {
    return new MergeTile(this, other);
  }

  // checks whether this merge tile was created according to the rules of 2048
  public boolean isValid() {
    return this.piece1.getValue() == this.piece2.getValue() && this.piece1.isValid()
        && this.piece2.isValid();
  }
}

class ExamplesGamePiece {
  IGamePiece base2 = new BaseTile(2);
  IGamePiece base4 = new BaseTile(4);
  IGamePiece base8 = new BaseTile(8);
  IGamePiece merge4 = new MergeTile(new BaseTile(2), new BaseTile(2));
  IGamePiece merge8 = new MergeTile(this.merge4, this.merge4);

  boolean testGetValue(Tester t) {
    return t.checkExpect(this.merge8.getValue(), 8) && t.checkExpect(this.merge4.getValue(), 4)
        && t.checkExpect(this.base2.getValue(), 2);
  }

  boolean testMerge(Tester t) {
    return t.checkExpect(this.base2.merge(this.base2), this.merge4)
        && t.checkExpect(this.base2.merge(this.base4), new MergeTile(this.base2, this.base4))
        && t.checkExpect(this.merge4.merge(this.merge4), this.merge8)
        && t.checkExpect(this.merge4.merge(this.base8), new MergeTile(this.merge4, this.base8));
  }

  boolean testIsValid(Tester t) {
    return t.checkExpect(this.base2.isValid(), true) && t.checkExpect(this.merge4.isValid(), true)
        && t.checkExpect(this.base2.merge(this.base4).isValid(), false)
        && t.checkExpect(this.merge4.merge(this.merge8).isValid(), false);
  }
}