import tester.*; // The tester library
import javalib.worldimages.*; // images, like RectangleImage or OverlayImages
import javalib.funworld.*; // the abstract World class and the big-bang library
import java.awt.Color; // general colors (as triples of red,green,blue values)
                       // and predefined colors (Color.RED, Color.GRAY, etc.)
import java.util.Random;

class MyPosn extends Posn {
  // standard constructor
  MyPosn(int x, int y) {
    super(x, y);
  }

  // constructor to convert from a Posn to a MyPosn
  MyPosn(Posn p) {
    this(p.x, p.y);
  }

  // add this posn's x and y to the given posn's x and y
  MyPosn add(MyPosn other) {
    return new MyPosn(other.x + this.x, other.y + this.y);
  }

  // determines if this position is offscreen given the width and the height
  boolean isOffscreen(int width, int height) {
    return this.x < 0 || this.x > width || this.y < 0 || this.y > height;
  }

  WorldScene placeImageOnScene(WorldScene scene, WorldImage image) {
    return scene.placeImageXY(image, this.x, this.y);
  }
}

class Circle {

  MyPosn position; // in pixels
  MyPosn velocity; // in pixels/tick

  Circle(MyPosn position, MyPosn velocity) {
    this.position = position;
    this.velocity = velocity;
  }

  Circle move() {
    return new Circle(this.position.add(this.velocity), this.velocity);
  }

  boolean isOffscreen(int width, int height) {
    return this.position.isOffscreen(width, height);
  }

  WorldImage draw() {
    return new CircleImage(15, OutlineMode.SOLID, Color.BLUE);
  }

  WorldScene place(WorldScene scene) {
    return this.position.placeImageOnScene(scene, this.draw());
  }
}

interface ILoCircle {
  ILoCircle moveAll();

  ILoCircle removeOffscreen(int width, int height);

  WorldScene placeAll(WorldScene scene);
  
  int size();
}

class MtLoCircle implements ILoCircle {

  public ILoCircle moveAll() {
    return new MtLoCircle();
  }

  public ILoCircle removeOffscreen(int width, int height) {
    return new MtLoCircle();
  }

  public WorldScene placeAll(WorldScene scene) {
    return scene;
  }

  public int size() {
    return 0;
  }
  
  

}

class ConsLoCircle implements ILoCircle {
  Circle first;
  ILoCircle rest;

  ConsLoCircle(Circle first, ILoCircle rest) {
    this.first = first;
    this.rest = rest;
  }

  public ILoCircle moveAll() {
    return new ConsLoCircle(this.first.move(), this.rest.moveAll());
  }

  public ILoCircle removeOffscreen(int width, int height) {
    if (this.first.isOffscreen(width, height)) {
      return this.rest.removeOffscreen(width, height);
    } else {
      return new ConsLoCircle(this.first, this.rest.removeOffscreen(width, height));
    }
  }

  public WorldScene placeAll(WorldScene scene) {
    WorldScene firstWithScene = this.first.place(scene);
    return this.rest.placeAll(firstWithScene);
  }

  public int size() {
    return 1 + this.rest.size();
  }

}

class CircleGame extends World {
  ILoCircle circles;
  int limitOffscreen;
  Random rand;

  CircleGame() {
    this(new MtLoCircle(), 10);
  }

  CircleGame(int limitOffscreen) {
    this(new MtLoCircle(), limitOffscreen);
  }

  CircleGame(ILoCircle circles, int limitOffscreen) {
    this(circles, limitOffscreen, new Random());
  }
  
  CircleGame(ILoCircle circles, int limitOffscreen, Random rand) {
    this.circles = circles;
    this.limitOffscreen = limitOffscreen;
    this.rand = rand;
  }

  @Override
  public WorldScene makeScene() {
    return this.circles.placeAll(this.getEmptyScene());
  }

  @Override
  public WorldEnd worldEnds() {
    if (this.limitOffscreen <= 0) {
      return new WorldEnd(true, this.makeScene());
    } else {
      return new WorldEnd(false, this.makeScene());
    }
  }

  @Override
  public World onMouseClicked(Posn pos) {
    MyPosn velocity = new MyPosn(this.rand.nextInt(10), this.rand.nextInt(10));
    return new CircleGame(
        new ConsLoCircle(new Circle(new MyPosn(pos), velocity), this.circles),
        this.limitOffscreen,
        this.rand);
  }
  
  @Override
  public World onTick() {
    WorldScene empty = this.getEmptyScene();
    ILoCircle newCircles = this.circles.moveAll().removeOffscreen(empty.width, empty.height);
    int removedCircles = this.circles.size() - newCircles.size();
    return new CircleGame(newCircles, limitOffscreen - removedCircles, this.rand);
  }
}

class ExamplesCircle {
  int seed  = 15;
  CircleGame game = new CircleGame();
  CircleGame game2 = new CircleGame(new MtLoCircle(), 10, new Random(this.seed));
  
  boolean testGame(Tester t) {
    return game.bigBang(500, 500, 1.0 / 10.0);
  }
  
  boolean testRandom(Tester t) {
    Random rand1 = new Random(this.seed);
    Random rand2 = new Random(this.seed);
    return t.checkExpect(rand1.nextInt(10), rand2.nextInt(10))
        && t.checkExpect(rand1.nextInt(10), rand2.nextInt(10));
  }
  
  
  boolean testOnMouseClicked(Tester t) {
    MyPosn click = new MyPosn(250, 250);
    World actual = game2.onMouseClicked(click);
    Random expectedRand = new Random(this.seed);
    MyPosn expectedVelocity = 
        new MyPosn(expectedRand.nextInt(10), expectedRand.nextInt(10));
    World expected = new CircleGame(
        new ConsLoCircle(new Circle(click, expectedVelocity), new MtLoCircle()),
        10
        );
    return t.checkExpect(actual, expected);
  }
}
