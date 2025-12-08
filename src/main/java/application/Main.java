package application;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType; // เพิ่มอันนี้
import java.util.Optional;              // เพิ่มอันนี้
// Imports จาก Logic
import logic.board.Board;
import logic.candy.Candy;
import logic.candy.CandyColor;
import logic.candy.CandyType;
import logic.controller.GameController;
import logic.controller.GameMode;
import logic.controller.GameState;
import logic.utils.Point;
import logic.Item.*; // Import Package Item

import java.util.List;
import java.util.Set;

public class Main extends Application {

    private static final int ROWS = 9;
    private static final int COLS = 9;
    private static final int TILE_SIZE = 60;

    private GameController controller;
    private GridPane gridPane;
    private Label scoreLabel;
    private Label moveLabel;
    private BorderPane gameRoot;

    // 🔥 ประกาศปุ่มเป็น Field เพื่อให้อัปเดตได้จากทุกที่
    private Button btnIce;
    private Button btnBomb;
    private Button btnStriped;

    private boolean isAnimating = false;
    private int selectedRow = -1;
    private int selectedCol = -1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        showMainMenu(primaryStage);
    }

    // --- 🏠 MAIN MENU SCENE ---
    private void showMainMenu(Stage stage) {
        VBox menuBox = new VBox(20);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle("-fx-background-color: #f0f8ff;");

        Label titleLabel = new Label("🍬 Candy Crush OOP 🍬");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleLabel.setTextFill(Color.HOTPINK);

        Button btnNormal = createStyledButton("Play Normal Mode", Color.LIGHTGREEN);
        Button btnHard = createStyledButton("Play Hard Mode (Ice)", Color.LIGHTSKYBLUE);

        btnNormal.setOnAction(e -> startGame(stage, GameMode.NORMAL));
        btnHard.setOnAction(e -> startGame(stage, GameMode.HARD));

        menuBox.getChildren().addAll(titleLabel, btnNormal, btnHard);

        Scene menuScene = new Scene(menuBox, 400, 500);
        stage.setTitle("Candy Crush Menu");
        stage.setScene(menuScene);
        stage.show();
    }

    // --- 🎮 GAME SCENE ---
    private void startGame(Stage stage, GameMode mode) {
        controller = new GameController(ROWS, COLS, mode);

        gameRoot = new BorderPane();
        gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        // Header
        BorderPane header = new BorderPane();
        header.setStyle("-fx-background-color: #333; -fx-padding: 10;");

        scoreLabel = new Label("Score: 0");
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font(20));

        moveLabel = new Label("Moves: " + controller.getMoveLeft());
        moveLabel.setTextFill(Color.YELLOW);
        moveLabel.setFont(Font.font(20));

        header.setLeft(scoreLabel);
        header.setRight(moveLabel);
        gameRoot.setTop(header);
        gameRoot.setCenter(gridPane);

        // --- Bottom (Items) ---
        HBox itemBox = new HBox(15);
        itemBox.setAlignment(Pos.CENTER);
        itemBox.setStyle("-fx-padding: 15; -fx-background-color: #ddd;");

        // สร้างปุ่ม
        btnIce = new Button();
        btnBomb = new Button();
        btnStriped = new Button();

        // Style พื้นฐาน
        String btnStyle = "-fx-font-size: 14px; -fx-background-radius: 10; -fx-min-width: 150px; -fx-font-weight: bold;";
        btnIce.setStyle(btnStyle);
        btnBomb.setStyle(btnStyle);
        btnStriped.setStyle(btnStyle);

        // Set Action
        btnIce.setOnAction(e -> handleItemClick(new IceBreakItem()));
        btnBomb.setOnAction(e -> handleItemClick(new BombItem()));
        btnStriped.setOnAction(e -> handleItemClick(new StripedItem()));

        itemBox.getChildren().addAll(btnIce, btnBomb, btnStriped);
        gameRoot.setBottom(itemBox);

        // 🔥 เรียกอัปเดตปุ่มครั้งแรก (เพื่อใส่ข้อความและเช็คสี)
        updateItemButtons();

        updateView(null);

        Scene gameScene = new Scene(gameRoot, COLS * TILE_SIZE + 40, ROWS * TILE_SIZE + 150);
        stage.setTitle("Candy Crush - " + mode);
        stage.setScene(gameScene);
        stage.setResizable(false);
    }

    // --- 🔄 Method อัปเดตสถานะปุ่ม ---
    private void updateItemButtons() {
        // 1. Ice Breaker / Color Bomb
        int iceCount = controller.getIceItemAmount();
        String iceName = (controller.getGameMode() == GameMode.HARD) ? "❄️ Melt Ice" : "⚫ Color Bomb";
        btnIce.setText(iceName + " (" + iceCount + ")");
        btnIce.setDisable(iceCount <= 0);
        // ถ้ามีของ=สีฟ้า, หมด=สีเทา
        btnIce.setStyle("-fx-background-color: " + (iceCount > 0 ? "#87CEEB" : "#D3D3D3") +
                "; -fx-text-fill: " + (iceCount > 0 ? "black" : "gray") +
                "; -fx-font-size: 14px; -fx-background-radius: 10; -fx-min-width: 150px; -fx-font-weight: bold;");

        // 2. Bomb
        int bombCount = controller.getBombItemAmount();
        btnBomb.setText("💣 Bomb (" + bombCount + ")");
        btnBomb.setDisable(bombCount <= 0);
        // ถ้ามีของ=สีส้ม, หมด=สีเทา
        btnBomb.setStyle("-fx-background-color: " + (bombCount > 0 ? "#FF6347" : "#D3D3D3") +
                "; -fx-text-fill: " + (bombCount > 0 ? "white" : "gray") +
                "; -fx-font-size: 14px; -fx-background-radius: 10; -fx-min-width: 150px; -fx-font-weight: bold;");

        // 3. Striped
        int stripedCount = controller.getStripedItemAmount();
        btnStriped.setText("⚡ Striped (" + stripedCount + ")");
        btnStriped.setDisable(stripedCount <= 0);
        // ถ้ามีของ=สีเหลือง, หมด=สีเทา
        btnStriped.setStyle("-fx-background-color: " + (stripedCount > 0 ? "#FFD700" : "#D3D3D3") +
                "; -fx-text-fill: " + (stripedCount > 0 ? "black" : "gray") +
                "; -fx-font-size: 14px; -fx-background-radius: 10; -fx-min-width: 150px; -fx-font-weight: bold;");
    }

    private void updateView(Set<Point> hiddenCandies) {
        gridPane.getChildren().clear();
        Board board = controller.getBoard();

        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                StackPane tile = new StackPane();
                tile.setPrefSize(TILE_SIZE, TILE_SIZE);

                Rectangle bg = new Rectangle(TILE_SIZE, TILE_SIZE);
                bg.setFill((r + c) % 2 == 0 ? Color.rgb(235,235,235) : Color.rgb(210,210,210));
                bg.setStroke(Color.GRAY);
                tile.getChildren().add(bg);

                boolean shouldHide = (hiddenCandies != null && hiddenCandies.contains(new Point(r, c)));
                Candy candy = board.getCandy(r, c);

                if (candy != null && !shouldHide) {
                    Circle circle = new Circle(TILE_SIZE / 2 - 8);

                    if (candy.getType() == CandyType.COLOR_BOMB) {
                        circle.setFill(Color.BLACK);
                        circle.setStroke(Color.WHITE);
                        circle.setStrokeWidth(3);
                    } else {
                        circle.setFill(getColor(candy.getColor()));
                        circle.setStroke(Color.BLACK);
                        circle.setStrokeWidth(2);
                    }
                    tile.getChildren().add(circle);

                    if (candy.isFrozen()) {
                        Rectangle ice = new Rectangle(TILE_SIZE - 10, TILE_SIZE - 10);
                        ice.setArcWidth(15);
                        ice.setArcHeight(15);
                        ice.setFill(Color.rgb(173, 216, 230, 0.6));
                        ice.setStroke(Color.WHITE);
                        ice.setStrokeWidth(2);
                        tile.getChildren().add(ice);
                    }

                    String typeText = getTypeCode(candy.getType());
                    if (!typeText.isEmpty()) {
                        Text text = new Text(typeText);
                        text.setFill(Color.WHITE);
                        text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                        text.setStyle("-fx-effect: dropshadow(three-pass-box, black, 2, 0, 0, 0);");
                        tile.getChildren().add(text);
                    }
                }

                if (r == selectedRow && c == selectedCol) {
                    bg.setStroke(Color.YELLOW);
                    bg.setStrokeWidth(4);
                }

                int finalR = r;
                int finalC = c;
                tile.setOnMouseClicked(e -> handleTileClick(finalR, finalC));

                gridPane.add(tile, c, r);
            }
        }

        scoreLabel.setText("Score: " + controller.getScore());
        moveLabel.setText("Moves: " + controller.getMoveLeft());
    }

    private void handleTileClick(int r, int c) {
        if (isAnimating || controller.getGameState() != GameState.PLAY) return;

        if (selectedRow == -1) {
            selectedRow = r;
            selectedCol = c;
            updateView(null);
        } else {
            if (Math.abs(selectedRow - r) + Math.abs(selectedCol - c) == 1) {
                Set<Point> removes = controller.handleSwap(selectedRow, selectedCol, r, c);

                if (!removes.isEmpty()) {
                    isAnimating = true;
                    updateView(null);
                    PauseTransition wait = new PauseTransition(Duration.seconds(0.3));
                    wait.setOnFinished(e -> runGameLoop(removes));
                    wait.play();
                } else {
                    System.out.println("Invalid Move / No Match / Frozen");
                }

                selectedRow = -1;
                selectedCol = -1;
                if(removes.isEmpty()) updateView(null);
            } else {
                selectedRow = r;
                selectedCol = c;
                updateView(null);
            }
        }
    }

    private void handleItemClick(Item itemStrategy) {
        if (isAnimating || controller.getGameState() != GameState.PLAY) return;

        System.out.println("Using Item: " + itemStrategy.getClass().getSimpleName());

        // 1. Ice Breaker (Instant)
        if (itemStrategy instanceof IceBreakItem) {
            // เรียกใช้ (Controller จะเช็คโควต้าและตัดจำนวนให้เอง)
            controller.applyItemTransform(itemStrategy);

            updateView(null);
            updateItemButtons(); // 🔥 อัปเดตปุ่มทันที
            checkGameOver();
            return;
        }

        // 2. Bomb / Striped (Transform -> Explode)
        List<Point> targets = controller.applyItemTransform(itemStrategy);

        if (!targets.isEmpty()) {
            isAnimating = true;
            updateView(null);
            updateItemButtons(); // 🔥 อัปเดตปุ่มทันที (ตัวเลขลด)

            PauseTransition waitShow = new PauseTransition(Duration.seconds(0.5));
            waitShow.setOnFinished(e -> {
                Set<Point> removes = controller.activateItems(targets);
                if (!removes.isEmpty()) {
                    runGameLoop(removes);
                } else {
                    isAnimating = false;
                    updateView(null);
                    checkGameOver();
                }
            });
            waitShow.play();
        } else {
            System.out.println("Item failed (Out of stock or No targets)");
        }
    }

    private void runGameLoop(Set<Point> removes) {
        try {
            updateView(removes);

            PauseTransition waitExplosion = new PauseTransition(Duration.seconds(0.5));
            waitExplosion.setOnFinished(e -> {
                controller.boardUpdate(removes);
                updateView(null);

                PauseTransition waitGravity = new PauseTransition(Duration.seconds(0.5));
                waitGravity.setOnFinished(e2 -> {
                    Set<Point> newRemoves = controller.checkChainReaction();

                    if (!newRemoves.isEmpty()) {
                        runGameLoop(newRemoves);
                    } else {
                        isAnimating = false;

                        // เรียก Shuffle ถ้าเดินไม่ได้
                        controller.endTurn();

                        updateView(null);
                        checkGameOver();
                    }
                });
                waitGravity.play();
            });
            waitExplosion.play();
        } catch (Exception e) {
            e.printStackTrace();
            isAnimating = false;
        }
    }

    private void checkGameOver() {
        // Debug: ปริ้นท์เช็คดูว่าสถานะตอนนี้คืออะไร
        System.out.println("Checking Game Over... Current State: " + controller.getGameState());
        System.out.println("Moves Left: " + controller.getMoveLeft());

        if (controller.getGameState() == GameState.GAME_OVER) {

            // 🔥 ใช้ Platform.runLater เพื่อแก้ปัญหา UI ค้าง
            Platform.runLater(() -> {
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Game Over");

                    String header = controller.getMoveLeft() <= 0 ? "❌ Moves Out!" : "🎉 You Win!";
                    alert.setHeaderText(header);
                    alert.setContentText("Your Final Score: " + controller.getScore());

                    ButtonType btnRetry = new ButtonType("🔄 Retry");
                    ButtonType btnMenu = new ButtonType("🏠 Main Menu");
                    ButtonType btnExit = new ButtonType("🚪 Exit");

                    alert.getButtonTypes().setAll(btnRetry, btnMenu, btnExit);

                    // ดึง Stage อย่างระมัดระวัง (เช็ค null)
                    Stage currentStage = null;
                    if (gridPane.getScene() != null) {
                        currentStage = (Stage) gridPane.getScene().getWindow();
                    }

                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && currentStage != null) {
                        if (result.get() == btnRetry) {
                            System.out.println("Retrying Game...");
                            startGame(currentStage, controller.getGameMode());
                        } else if (result.get() == btnMenu) {
                            System.out.println("Back to Menu...");
                            showMainMenu(currentStage);
                        } else if (result.get() == btnExit) {
                            System.out.println("Exiting...");
                            currentStage.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private Button createStyledButton(String text, Color bg) {
        Button btn = new Button(text);
        btn.setFont(Font.font(16));
        btn.setPrefWidth(200);
        btn.setStyle("-fx-background-color: " + toHexString(bg) + "; -fx-text-fill: black; -fx-background-radius: 10;");
        return btn;
    }



    private String toHexString(Color c) {
        return String.format("#%02X%02X%02X",
                (int)(c.getRed() * 255),
                (int)(c.getGreen() * 255),
                (int)(c.getBlue() * 255));
    }

    private Color getColor(CandyColor c) {
        if (c == null) return Color.TRANSPARENT;
        switch (c) {
            case RED: return Color.RED;
            case GREEN: return Color.LIMEGREEN;
            case BLUE: return Color.DODGERBLUE;
            case YELLOW: return Color.GOLD;
            case PURPLE: return Color.MEDIUMPURPLE;
            default: return Color.BLACK;
        }
    }

    private String getTypeCode(CandyType t) {
        if(t == null) return "";
        switch (t) {
            case STRIPED_HOR: return "H";
            case STRIPED_VER: return "V";
            case BOMB: return "B";
            case COLOR_BOMB: return "C";
            default: return "";
        }
    }
}