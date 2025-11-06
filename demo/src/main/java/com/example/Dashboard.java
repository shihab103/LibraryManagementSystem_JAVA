package com.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Dashboard extends Application {

    private BorderPane rootLayout;

    @Override
    public void start(Stage stage) {
        javafx.geometry.Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        
        rootLayout = new BorderPane();
        rootLayout.setStyle("-fx-background-color: #f4f4f4;");

        VBox sidebar = createSidebar(stage);
        rootLayout.setLeft(sidebar);

        loadContent(createDashboardContent());
        
        Scene scene = new Scene(rootLayout, primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
        stage.setTitle("Library Management System - Dashboard");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private void loadContent(Node contentNode) {
        rootLayout.setCenter(contentNode);
    }

    private VBox createSidebar(Stage stage) {
        VBox sidebar = new VBox(15);
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        sidebar.setPrefWidth(screenWidth * 0.15); 
        sidebar.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");
        sidebar.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("LMS Features");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");

        Button homeBtn = createSidebarButton("🏠 Dashboard");
        homeBtn.setOnAction(e -> loadContent(createDashboardContent())); 

        Button addBookBtn = createSidebarButton("➕ Add Book");
        addBookBtn.setOnAction(e -> loadContent(new AddBookPage().getPane()));

        Button viewBooksBtn = createSidebarButton("📖 View Books");
        viewBooksBtn.setOnAction(e -> loadContent(new ViewBooksPage().getPane()));
        
        Button issueBookBtn = createSidebarButton("📤 Issue Book");
        issueBookBtn.setOnAction(e -> loadContent(new IssueBookPage().getPane()));
        
        Button returnBookBtn = createSidebarButton("📥 Return Book");
        returnBookBtn.setOnAction(e -> loadContent(new ReturnBookPage().getPane()));
        
        Button viewIssuedBtn = createSidebarButton("📃 View Issued");
        viewIssuedBtn.setOnAction(e -> loadContent(createViewIssuedBooksPane())); 

        Button logoutBtn = createSidebarButton("🔒 Logout");
        logoutBtn.setOnAction(e -> {
            LoginController login = new LoginController();
            stage.setMaximized(false); 
            login.start(stage);
        });

        sidebar.getChildren().addAll(title, homeBtn, addBookBtn, viewBooksBtn, issueBookBtn, returnBookBtn, viewIssuedBtn, logoutBtn);
        return sidebar;
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(200);
        btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #1abc9c; -fx-text-fill: white; -fx-font-size: 14px;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #34495e; -fx-text-fill: white; -fx-font-size: 14px;"));
        return btn;
    }
    
    private VBox createDashboardContent() {
        VBox content = new VBox(20);
        content.setStyle("-fx-padding: 30;");
        content.setAlignment(Pos.TOP_CENTER);

        Label welcome = new Label("Welcome to Library Dashboard");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #333333;");

        VBox stats = new VBox(15);
        stats.setAlignment(Pos.CENTER);
        stats.getChildren().addAll(
            createStatCard("Total Books", BookDAO.countTotalBooks()),
            createStatCard("Total Available Books", BookDAO.countAvailableBooks()),
            createStatCard("Total Users (Students)", BookDAO.countTotalUsers()),
            createStatCard("Books Issued", IssuedBookDAO.countIssuedBooks())
        );
        
        content.getChildren().addAll(welcome, stats);
        return content;
    }

    private VBox createStatCard(String title, int count) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: white; -fx-padding: 15; -fx-border-color: #bdc3c7; -fx-border-radius: 5; -fx-background-radius: 5;");
        card.setPrefWidth(300);
        card.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        
        Label countLabel = new Label(String.valueOf(count));
        countLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #2ecc71;");
        
        card.getChildren().addAll(titleLabel, countLabel);
        return card;
    }
    
    private VBox createViewIssuedBooksPane() {
        VBox content = new VBox(10);
        content.setStyle("-fx-padding: 20;");
        
        Label title = new Label("📃 Currently Issued Books");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        ListView<String> issuedListView = new ListView<>();
        issuedListView.getItems().addAll(IssuedBookDAO.getAllIssuedBooks());
        
        content.getChildren().addAll(title, issuedListView);
        return content;
    }

    public static void main(String[] args) {
        launch(args);
    }
}