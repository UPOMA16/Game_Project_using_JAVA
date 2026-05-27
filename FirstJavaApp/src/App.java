import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.ArrayList;

public class App extends Application {

    private Pane root;

    private Bird bird;
    private ArrayList<Pipe> pipes = new ArrayList<>();

    private Text scoreText;
    private Text highScoreText;
    private Text gameText;
    private Text countdownText;

    private int score = 0;
    private static int highScore = 0;

    private boolean gameOver = false;
    private long lastPipeTime = 0;

    
    private int countdown = 3;
    private long countdownStart = 0;
    private boolean isCountingDown = false;

    
    class Bird {

        ImageView img;

        double y = 300;
        double velocity = 0;

        public Bird() {

            Image image = new Image(
                    getClass().getResourceAsStream("/bird3.png")
            );

            img = new ImageView(image);
            img.setFitWidth(40);
            img.setFitHeight(30);

            img.setX(100);
            img.setY(y);
        }

        public void update() {
            velocity += 0.5;
            y += velocity;

            img.setY(y);
            img.setRotate(velocity * 3);
        }

        public void jump() {
            velocity = -8;
        }
    }

    
    class Pipe {

        Rectangle shape;

        double x;
        boolean isTop;
        boolean passed = false;

        public Pipe(double x, double y, double height, boolean isTop) {

            this.x = x;
            this.isTop = isTop;

            shape = new Rectangle(60, height);
            shape.setX(x);
            shape.setY(y);
            shape.setFill(Color.GREEN);
        }

        public void update() {
            x -= 3;
            shape.setX(x);
        }
    }

    
    @Override
    public void start(Stage stage) {

        root = new Pane();
        root.setPrefSize(400, 600);

        
        Image bgImage = new Image(
                getClass().getResourceAsStream("/bg.png")
        );

        ImageView background = new ImageView(bgImage);
        background.setFitWidth(400);
        background.setFitHeight(600);

        root.getChildren().add(background);

        bird = new Bird();
        root.getChildren().add(bird.img);

        String fontStyle =
                "-fx-font-size: 22;" +
                "-fx-fill: white;" +
                "-fx-font-family: Arial;";

        scoreText = new Text("Score: 0");
        scoreText.setX(20);
        scoreText.setY(40);
        scoreText.setStyle("-fx-font-size: 22; -fx-fill: white; -fx-font-family: Arial;");


        highScoreText = new Text("High Score: 0");
        highScoreText.setX(20);
        highScoreText.setY(70);
        highScoreText.setStyle("-fx-font-size: 22; -fx-fill: white; -fx-font-family: Arial;");

        gameText = new Text("GAME OVER\nPress R to Restart");
        gameText.setX(80);
        gameText.setY(250);
        gameText.setStyle("-fx-font-size: 25; -fx-fill: red;");
        gameText.setVisible(false);

       
        countdownText = new Text("");
        countdownText.setX(170);
        countdownText.setY(300);
        countdownText.setStyle("-fx-font-size: 50; -fx-fill: white;");
        countdownText.setVisible(false);

        root.getChildren().addAll(scoreText, highScoreText, gameText, countdownText);

        Scene scene = new Scene(root, 400, 600);

        scene.setOnKeyPressed(e -> {

            if (e.getCode() == KeyCode.SPACE && !gameOver && !isCountingDown) {
                bird.jump();
            }

            if (e.getCode() == KeyCode.R && gameOver && !isCountingDown) {
                startCountdown();
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                
                if (isCountingDown) {

                    if (countdownStart == 0) {
                        countdownStart = now;
                    }

                    long elapsed = now - countdownStart;

                    if (elapsed > 1_000_000_000L) {
                        countdown--;
                        countdownStart = now;

                        if (countdown > 0) {
                            countdownText.setText(String.valueOf(countdown));
                        } else {
                            countdownText.setVisible(false);
                            isCountingDown = false;
                            restart();
                        }
                    }

                    return;
                }

                
                if (!gameOver) {

                    bird.update();

                    if (now - lastPipeTime > 1_500_000_000L) {
                        addPipe();
                        lastPipeTime = now;
                    }

                    for (Pipe p : pipes) {

                        p.update();

                        
                        if (bird.img.getBoundsInParent()
                                .intersects(p.shape.getBoundsInParent())) {
                            endGame();
                        }

                        
                        if (!p.passed &&
                                p.x + 60 < bird.img.getX() &&
                                !p.isTop) {

                            score++;
                            scoreText.setText("Score: " + score);
                            p.passed = true;
                        }
                    }

                    pipes.removeIf(p -> {
                        if (p.x < -80) {
                            root.getChildren().remove(p.shape);
                            return true;
                        }
                        return false;
                    });

                    if (bird.img.getY() > 600 || bird.img.getY() < 0) {
                        endGame();
                    }

                    scoreText.toFront();
                    highScoreText.toFront();
                    gameText.toFront();
                    countdownText.toFront();
                }
            }
        }.start();

        stage.setTitle("Flappy Bird");
        stage.setScene(scene);
        stage.show();
    }

    
    private void addPipe() {

        double gap = 150;
        double topHeight = 50 + Math.random() * 250;

        Pipe top = new Pipe(400, 0, topHeight, true);

        Pipe bottom = new Pipe(
                400,
                topHeight + gap,
                600 - (topHeight + gap),
                false
        );

        pipes.add(top);
        pipes.add(bottom);

        root.getChildren().addAll(top.shape, bottom.shape);
    }

    
    private void endGame() {

        gameOver = true;
        gameText.setVisible(true);

        if (score > highScore) {
            highScore = score;
        }

        highScoreText.setText("High Score: " + highScore);
        gameText.toFront();
    }

   
    private void startCountdown() {

        countdown = 3;
        countdownText.setText("3");
        countdownText.setVisible(true);

        isCountingDown = true;
        countdownStart = 0;

        gameText.setVisible(false);
    }

   
    private void restart() {

        gameOver = false;
        score = 0;
        scoreText.setText("Score: 0");

        countdownText.setVisible(false);

        pipes.clear();
        root.getChildren().clear();

       
        Image bgImage = new Image(
                getClass().getResourceAsStream("/bg.png")
        );

        ImageView background = new ImageView(bgImage);
        background.setFitWidth(400);
        background.setFitHeight(600);

        root.getChildren().add(background);

        bird = new Bird();
        root.getChildren().add(bird.img);

        root.getChildren().addAll(scoreText, highScoreText, gameText, countdownText);

        lastPipeTime = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }
}