import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class TanksOfSDU extends Application {
    private Map map;
    private MyPlayer myPlayer;
    private Boolean move, move2;
    private LinkedList<KeyCode> codeList = new LinkedList<>();
    private LinkedList<KeyCode> codeList2 = new LinkedList<>();

    public TanksOfSDU () throws Exception {
        this.myPlayer = new MyPlayer(codeList);
        this.map = new Map(myPlayer);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        pane.getChildren().add(map);
        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tanks of SDU");
        primaryStage.show();
        primaryStage.setResizable(false);
        pane.requestFocus();
        pane.setOnKeyPressed(event -> {
            codeList.add(event.getCode());
            move = true;
            codeList2.add(event.getCode());
            move2 = true;
        });
        pane.setOnKeyReleased(event -> {
            move = false;
            if(event.getCode() == KeyCode.SPACE)
                myPlayer.shotBullet();
        });
        AnimationTimer timer =  new AnimationTimer() {
            @Override
            public void handle(long now) {
                try {
                    if(move)
                        myPlayer.move();
                }
                catch (Exception e){}
            }
        };
        timer.start();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}

class Map extends Pane {
    //Map class should be used as before to load map details from a txt file;
    //New characters:
    //        S: Steel wall
    //        B: Brick wall
    //        W: Water
    //        T: Trees
    //        P: Player (as before)
    //        A custom pane can be created to include a Map instance and add the graphical elements on top of it;

    private Scanner input,scanner;
    private char[][] obstacles;
    private ArrayList<String> rows;
    private MyPlayer myPlayer;
    private Barrier barrier;
    private int dimensionX, dimensionY;
    private int width = 42, height = 42;
    private Canvas obstacleCanvas;
    private GraphicsContext g;

    public Map(MyPlayer myPlayer) throws Exception{
        this.setStyle("-fx-background-color: green");
        this.myPlayer = myPlayer;
        init();

    }

    private void init() throws Exception{
        getFile();
        loadMap();
        setPlayer();
        setMap();
        this.getChildren().add(obstacleCanvas);
    }

    private void loadMap() throws Exception{
        obstacles = new char[dimensionY][dimensionX];
        for (int i = 0; i < dimensionY; i++) {
            String row = rows.get(i);
            for (int j = 0; j < dimensionX; j++) {
                obstacles[i][j] = row.charAt(j);
            }
        }
    }

    private void setPlayer(){
        for (int i = 0; i < dimensionY; i++) {
            for (int j = 0; j < dimensionX; j++) {
                if (obstacles[i][j] == 'P') {
                    this.getChildren().add(barrier.home(j, i));
                    myPlayer.addPane(this);
                    myPlayer.setTank(new Tank(this),j, i);
                    myPlayer.addBarrier(barrier);
                }
            }
        }
    }

    private void setMap(){
        for (int i = 0; i < dimensionY; i++) {
            for (int j = 0; j < dimensionX; j++) {
                switch (obstacles[i][j]) {
                    case 'T':
                        barrier.draw(Texture.TREE, j, i);
                        break;
                    case 'W':
                        barrier.draw(Texture.WATER, j, i);
                        break;
                    case 'B':
                        barrier.draw(Texture.BRICK, j, i);
                        break;
                    case 'S':
                        barrier.draw(Texture.STEEL, j, i);
                        break;
                    default:
                        break;
                }
                System.out.print(obstacles[i][j]+" ");
            }
            System.out.println();
        }
    }

    private void getFile(){
        try {
            String fileName = "C:\\Users\\DIAS\\Desktop\\FILES\\TANKSOFSDU\\src\\Map.txt";
            Path path = Paths.get(fileName);
            scanner = new Scanner(path);
        } catch (IOException e) {
            System.out.println("File not Found");
        }

        rows = new ArrayList<>();
        while (scanner.hasNextLine()){
            String row = scanner.nextLine();
            rows.add(row);
            dimensionX = row.length();
            dimensionY = rows.size();
        }

        obstacleCanvas = new Canvas(width*dimensionX, height*dimensionY);
        g = obstacleCanvas.getGraphicsContext2D();
        this.barrier = new Barrier(g, width*dimensionX, height*dimensionY);
    }
}

class Barrier{

//    Various barriers that appear on the map;
//    Can be implemented as related classes via Inheritance;
//    Four types of obstacles:

    private int x, y, index;
    private int width = 42, height = 42;
    private final int MAP_WIDTH, MAP_HEIGHT;
    private ArrayList<Point2D> homePosition;
    private ArrayList<Point2D> treePosition;
    private ArrayList<Point2D> waterPosition;
    private ArrayList<Point2D> brickPosition;
    private ArrayList<Integer> brickLives;
    private ArrayList<Point2D> steelPosition;
    private ArrayList<Point2D> myPosition;
    private ArrayList<Point2D> enemyPosition;
    private GraphicsContext g;
    private Image[] images = {
            new Image(new File("src/Images/home.jpg").toURI().toString()),
            new Image(new File("src/Images/trees.png").toURI().toString()),
            new Image(new File("src/Images/water.png").toURI().toString()),
            new Image(new File("src/Images/brickWall.png").toURI().toString()),
            new Image(new File("src/Images/steelWall.png").toURI().toString())
    };
    private int tankLives = 4;

    public Barrier(GraphicsContext g, int MAP_WIDTH, int MAP_HEIGHT){
        this.g = g;
        this.MAP_WIDTH = MAP_WIDTH;
        this.MAP_HEIGHT = MAP_HEIGHT;
        this.homePosition =  new ArrayList<>();
        this.treePosition =  new ArrayList<>();
        this.waterPosition = new ArrayList<>();
        this.brickPosition = new ArrayList<>();
        this.brickLives = new ArrayList<>();
        this.steelPosition = new ArrayList<>();
        this.myPosition = new ArrayList<>();
        this.enemyPosition = new ArrayList<>();
    }

    public Rectangle home(int x, int y){
        Rectangle home = new Rectangle(x*width, y*height, width,height);
        home.setFill(new ImagePattern(images[0]));
        return home;
    }

    public void addTankLives(){
        tankLives++;
    }

    public void addTankPosition(Point2D point2D){
        myPosition.add(point2D);
    }

    public void draw(Texture texture,int x, int y) {
        this.x = x*width;
        this.y = y*height;
        switch (texture){
            case HOME:  index = 0;
                homePosition.add(new Point2D(this.x, this.y));
                break;
            case TREE: index = 1;
                treePosition.add(new Point2D(this.x, this.y));
                break;
            case WATER: index = 2;
                waterPosition.add(new Point2D(this.x, this.y));
                break;
            case BRICK: index = 3;
                brickPosition.add(new Point2D(this.x, this.y));
                brickLives.add(4);
                break;
            case STEEL: index = 4;
                steelPosition.add(new Point2D(this.x, this.y));
                break;
            default:break;
        }
        g.drawImage(images[index], this.x, this.y, width, height);
    }

    public boolean checkTankCollision(int x, int y, int width, int height) {
        boolean tankCollided = false;
        for (Point2D point2D : waterPosition)
        {
            if(new Rectangle(x, y, width, height).intersects(point2D.getX(), point2D.getY(), this.width, this.height))
            {
                tankCollided = true;
                break;
            }
        }
        for (Point2D point2D : brickPosition)
        {
            if (new Rectangle(x, y, width, height).intersects(point2D.getX(), point2D.getY(), this.width, this.height))
            {
                tankCollided = true;
                break;
            }
        }
        for (Point2D point2D : steelPosition)
        {
            if (new Rectangle(x, y, width, height).intersects(point2D.getX(), point2D.getY(), this.width, this.height))
            {
                tankCollided = true;
                break;
            }
        }
        for (Point2D point2D : enemyPosition)
        {
            if (new Rectangle(x, y, width, height).intersects(point2D.getX(), point2D.getY(), width, height))
            {
                tankCollided = true;
                break;
            }
        }
        myPosition.clear();
        return tankCollided;
    }


    public boolean checkBulletCollision(int x, int y, int width, int height) {
        boolean bulletCollided = false;
        int i = 0;
        for (Point2D point2D : brickPosition)
        {
            if (new Rectangle(x, y, width, height).intersects(point2D.getX(), point2D.getY(), this.width, this.height))
            {
                bulletCollided = true;
                int live = brickLives.get(i);
                brickLives.add(i, --live);
                if (brickLives.get(i)%4 == 1 || brickLives.get(i)%4 == -1)
                {
                    g.clearRect(brickPosition.get(i).getX(),brickPosition.get(i).getY(),this.width,this.height);
                    brickPosition.remove(i);
                }
                break;
            }
            i++;
        }
        for (Point2D point2D : steelPosition)
        {
            if (new Rectangle(x, y, width, height).intersects(point2D.getX(), point2D.getY(), this.width, this.height))
            {
                bulletCollided = true;
                break;
            }
        }
        return bulletCollided;
    }

    public boolean checkTreeCollision (int x, int y, int width, int height) {
        boolean treeCollided = false;
        for (Point2D point2D : treePosition) {
            if (new Rectangle(x, y, width, height).intersects(point2D.getX(), point2D.getY(), this.width, this.height)){
                treeCollided = true;
                break;
            }
        }
        return treeCollided;
    }

    public boolean isOnMap(int x, int y, int width, int height){
        boolean collided = false;
        if(new Rectangle(x, y, width, height).intersects(0, 0, 1, MAP_HEIGHT)
                || new Rectangle(x, y, width, height).intersects(0, 0, MAP_WIDTH, 1)
                || new Rectangle(x, y, width, height).intersects(0, MAP_HEIGHT, MAP_WIDTH, 1)
                || new Rectangle(x, y, width, height).intersects(MAP_WIDTH, 0, 1, MAP_HEIGHT))
            collided = true;
        return collided;
    }
}

class MyPlayer{
    //will be extended for class Tank

    private int x, y, width = 42, height = 42;
    private int speed = 1;
    private Tank tank;
    private int tankWidth , tankHeight;
    private LinkedList<KeyCode> codeList;
    private Barrier barrier;
    private Bullet bullet;
    private Direction direction;
    private GraphicsContext g;
    private Pane pane;

    public MyPlayer(LinkedList<KeyCode> codeList){
        this.codeList = codeList;
    }

    public MyPlayer() {
    }

    public void addBarrier (Barrier barrier) {

        this.barrier = barrier;
    }

    public void addPane(Pane pane){
        this.pane = pane;
    }

    public void setTank(Tank tank, int x, int y) {
        Rectangle Tank = tank.draw(Direction.UP, x * width + 6, y * height + 4);
        this.tank = tank;
        this.x = tank.getX();
        this.y = tank.getY();
        this.tankWidth = tank.getWidth();
        this.tankHeight = tank.getHeight();
        pane.getChildren().add(Tank);
    }


    public void move(){
        int i = 2;
        while (i-->0) {
            barrier.addTankPosition(new Point2D(tank.getX(),tank.getY()));
            switch (codeList.getLast()) {
                case W:
                    tank.draw(direction = Direction.UP, x, y);
                    if (barrier.isOnMap(tank.getX(), tank.getY() - speed, tankWidth,  tankHeight)
                            || barrier.checkTankCollision(tank.getX(), tank.getY() - speed, tankWidth, tankHeight)) {
                        tank.draw(direction = Direction.UP, x, y+=speed);
                        break;
                    }
                    tank.draw(direction = Direction.UP, x, y-= speed);
                    break;
                case S:
                    tank.draw(direction = Direction.DOWN, x, y);
                    if (barrier.isOnMap(tank.getX(), tank.getY() + speed, tankWidth, tankHeight)
                            || barrier.checkTankCollision(tank.getX(), tank.getY() + speed, tankWidth, tankHeight)) {
                        tank.draw(direction = Direction.DOWN, x, y-=speed);
                        break;
                    }
                    tank.draw(direction = Direction.DOWN, x, y+=speed);
                    break;
                case A:
                    tank.draw(direction = Direction.LEFT, x, y);
                    if (barrier.isOnMap(tank.getX() - speed, tank.getY(), tankWidth, tankHeight)
                            || barrier.checkTankCollision(tank.getX() - speed, tank.getY(), tankWidth, tankHeight)) {
                        tank.draw(direction = Direction.LEFT, x+=speed, y);
                        break;
                    }
                    tank.draw(direction = Direction.LEFT, x-=speed, y);
                    break;
                case D:
                    tank.draw(direction = Direction.RIGHT, x, y);
                    if (barrier.isOnMap(tank.getX() + speed, tank.getY(), tankWidth, tankHeight)
                            || barrier.checkTankCollision(tank.getX() + speed, tank.getY(), tankWidth, tankHeight)){
                        tank.draw(direction = Direction.RIGHT, x-=speed, y);
                        break;
                    }
                    tank.draw(direction = Direction.RIGHT, x+=speed, y);
                    break;
                default:
                    break;
            }
        }
    }

    public void shotBullet(){
        this.bullet = new Bullet(direction, barrier);
        tank.shoot(bullet);
    }
}

class Tank extends MyPlayer  {
    //Extends the MyPlayer class;
    //Adds all the necessary graphical elements to make it appear like a tank;
    //Can fire a Bullet (see below) at a certain speed and frequency (on pressing space bar);
    //Has a limited number of lives (to be used later in Project-3);

    private int x, y, width = 30,height = 30;
    private int bulletStartX,bulletStartY;
    private Rectangle tank = new Rectangle(width,height);
    private Image[] images = {
            new Image(new File("src/Images/tankU.gif").toURI().toString()),
            new Image(new File("src/Images/tankD.gif").toURI().toString()),
            new Image(new File("src/Images/tankL.gif").toURI().toString()),
            new Image(new File("src/Images/tankR.gif").toURI().toString())
    };
    private Direction direction;
    private Paint paint;
    private Bullet bullet;
    private Pane pane;

    public Tank(Pane pane){
        this.pane = pane;
    }

    public Rectangle draw(Direction direction, int x, int y){
        this.direction = direction;
        this.x = x;
        this.y = y;
        switch (direction) {
            case UP:
                paint = new ImagePattern(images[0]);
                break;
            case DOWN:
                paint = new ImagePattern(images[1]);
                break;
            case LEFT:
                paint = new ImagePattern(images[2]);
                break;
            case RIGHT:
                paint = new ImagePattern(images[3]);
            default:
                break;
        }
        tank.setFill(paint);
        tank.setX(x);
        tank.setY(y);
        return tank;
    }

    public void shoot(Bullet bullet){
        this.bullet = bullet;
        switch (direction) {
            case UP:
                bulletStartX = x + (width / 2)-5;
                bulletStartY = y;
                break;
            case DOWN:
                bulletStartX = x + (width / 2)-7;
                bulletStartY = y + width;
                break;
            case LEFT:
                bulletStartX = x;
                bulletStartY = y + (width / 2)-5;
                break;
            case RIGHT:
                bulletStartX = x + width;
                bulletStartY = y + (width / 2)-6;
            default:break;
        }
        bullet.move(bulletStartX,bulletStartY,pane);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return  height;
    }
}

class Bullet{

//    Can be fired by a Tank;
//    Moves along a straight line (horizontal/vertical);
//    Moves at a certain speed;
//    Stops when hits an obstacle;
//    Can be implemented as a JavaFX animation (for now)

    private Direction direction;
    private Barrier barrier;
    private Image[] bullets = {
            new Image(new File("src/Images/bulletU.gif").toURI().toString()),
            new Image(new File("src/Images/bulletD.gif").toURI().toString()),
            new Image(new File("src/Images/bulletL.png").toURI().toString()),
            new Image(new File("src/Images/bulletR.png").toURI().toString())
    };
    private Image[] animation = {
            new Image(new File("src/Images/0.gif").toURI().toString()),
            new Image(new File("src/Images/1.gif").toURI().toString()),
            new Image(new File("src/Images/2.gif").toURI().toString()),
            new Image(new File("src/Images/3.gif").toURI().toString()),
            new Image(new File("src/Images/4.gif").toURI().toString()),
            new Image(new File("src/Images/5.gif").toURI().toString()),
            new Image(new File("src/Images/6.gif").toURI().toString()),
            new Image(new File("src/Images/7.gif").toURI().toString()),
            new Image(new File("src/Images/8.gif").toURI().toString()),
            new Image(new File("src/Images/9.gif").toURI().toString()),
            new Image(new File("src/Images/10.gif").toURI().toString())
    };
    private int x, y, speed = 2;
    private int width = 12, height = 12;
    private int animationIndex = 0;
    private boolean collide = false;
    private Rectangle bullet;
    private Paint paint;
    private AnimationTimer timer;
    private Pane pane;

    public Bullet(Direction direction, Barrier barrier) {
        this.direction = direction;
        this.barrier = barrier;
    }

    public void move(int x, int y, Pane pane){
        this.pane = pane;
        this.y = y;
        this.x = x;
        switch (direction){
            case UP: paint = new ImagePattern(bullets[0]);break;
            case DOWN: paint = new ImagePattern(bullets[1]);break;
            case LEFT: paint = new ImagePattern(bullets[2]);break;
            case RIGHT: paint = new ImagePattern(bullets[3]);break;
            default:break;
        }
        bullet = new Rectangle(this.x, this.y, width, height);
        bullet.setFill(paint);
        pane.getChildren().add(bullet);
        timer = new AnimationTimer() {

            @Override
            public void handle(long currentNanoMillis) {
                if (!collide) {
                    update();
                }
            }
        };
        timer.start();
    }

    private void update(){
        int i = 2;
        while (i-->0) {
            bullet.setOpacity(1);
            if (direction == Direction.UP) y-=speed;
            if (direction == Direction.DOWN) y+=speed;
            if (direction == Direction.LEFT) x-=speed;
            if (direction == Direction.RIGHT) x+=speed;
            bullet.setX(x);
            bullet.setY(y);

            if (barrier.checkTreeCollision(x, y, width, height)) {
                bullet.setOpacity(0);
            }

            if (barrier.isOnMap(x, y, width, height) || barrier.checkBulletCollision(x, y, width, height)) {
                pane.getChildren().remove(bullet);
                timer.stop();
            }
        }
    }
}

enum Direction {
    UP,DOWN,LEFT,RIGHT;
}

enum Texture {
    HOME,TREE,WATER,BRICK,STEEL;
}



