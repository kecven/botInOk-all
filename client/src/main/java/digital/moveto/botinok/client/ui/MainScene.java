package digital.moveto.botinok.client.ui;

import digital.moveto.botinok.client.config.Const;
import digital.moveto.botinok.client.config.UIConst;
import jakarta.annotation.PostConstruct;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainScene {

    @Autowired
    private UiElements uiElements;

    private Scene scene = null;

    @PostConstruct
    public void init() {

        HBox hBox = new HBox();
        hBox.setPrefSize(UIConst.WIDTH_OF_SCENE, UIConst.HEIGHT_OF_SCENE);

        Pane settingPane = initSettingPane();
        Pane mainPane = initMainPane();

        hBox.getChildren().addAll(settingPane, mainPane);

        Group root = new Group(hBox);
        scene = new Scene(root, UIConst.WIDTH_OF_SCENE, UIConst.HEIGHT_OF_SCENE);
    }

    public void finishInitialization() {
        Platform.runLater(
                () -> {
                    UiElements.stage.setScene(scene);
                }
        );
    }

    private Pane initSettingPane() {

        VBox result = new VBox();
        result.setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_SCENE);

        VBox setting = new VBox();
        setting.setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_SCENE - UIConst.HEIGHT_OF_VERSION);
        setting.setBorder(UIConst.BORDER_DEFAULT_SMALL);
        setting.setStyle(UIConst.STYLE_OF_BACKGROUND);
        setting.setCenterShape(true);


        Label settingLabel = new Label("Setting");
        settingLabel.setPadding(new Insets(10, 10, 10, 10));
        settingLabel.setFont(Font.font("Dialog", FontWeight.BOLD, 25));

        BorderPane settingLabelPane = new BorderPane();
        settingLabelPane.setCenter(settingLabel);
        settingLabelPane.setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_LABEL);

        BorderPane userNameLabelPane = new BorderPane();
        userNameLabelPane.setCenter(uiElements.getUserNameLabel());
        userNameLabelPane.setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_LABEL);

        Label position = new Label("Positions:");
        position.setTextAlignment(TextAlignment.CENTER);
        position.setPadding(new Insets(10, 10, 0, 10));


        uiElements.getAccountVBox().setStyle(UIConst.STYLE_OF_BACKGROUND);
        uiElements.getAccountVBox().setPadding(new Insets(10, 10, 10, 10));
        uiElements.getAccountVBox().setBorder(UIConst.BORDER_EMPTY);
        uiElements.getAccountVBox().setPrefSize(UIConst.WIDTH_OF_SETTING - 17, UIConst.HEIGHT_OF_ACCOUNTS);

        uiElements.getScrollAccountPane().setPadding(new Insets(0, 0, 0, 0));
        uiElements.getScrollAccountPane().setStyle(UIConst.STYLE_OF_BACKGROUND);
        uiElements.getScrollAccountPane().setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_ACCOUNTS);
        uiElements.getScrollAccountPane().setBorder(UIConst.BORDER_EMPTY);
        uiElements.getScrollAccountPane().setStyle(UIConst.STYLE_OF_BACKGROUND);
        uiElements.getScrollAccountPane().setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        uiElements.getScrollAccountPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        uiElements.getScrollAccountPane().setContent(uiElements.getAccountVBox());


        setting.getChildren().addAll(settingLabelPane, userNameLabelPane, uiElements.getWorkInShabatCheckBox(), uiElements.getActiveSearch(), uiElements.getLocation(), position, uiElements.getPositionsField(), uiElements.getStartButton(), uiElements.getScrollAccountPane());


        Label versionLabel = new Label("Version: " + Const.VERSION);
        versionLabel.setTextAlignment(TextAlignment.CENTER);
        versionLabel.setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_VERSION);
        versionLabel.setPadding(new Insets(10, 10, 10, 10));
        versionLabel.setFont(Font.font("Dialog", FontWeight.BOLD, 10));

        result.getChildren().addAll(setting, versionLabel);

        return result;
    }

    private Pane initMainPane() {
        VBox main = new VBox();

        Pane loggingPane = initLoggingPane();
        Pane statisticPane = initStatisticPane();

        main.getChildren().addAll(loggingPane, statisticPane);
        return main;
    }

    private Pane initLoggingPane() {
        Pane main = new Pane();

        main.setPrefSize(UIConst.WIDTH_OF_MAIN_PANE, UIConst.HEIGHT_OF_LOGGING_PANE);
        main.setBorder(UIConst.BORDER_DEFAULT_SMALL);
        main.setStyle(UIConst.STYLE_OF_BACKGROUND);

        uiElements.getLogArea().setPrefSize(UIConst.WIDTH_OF_MAIN_PANE - 10, UIConst.HEIGHT_OF_LOGGING_PANE - 10);
        uiElements.getLogArea().setBorder(UIConst.BORDER_EMPTY);
        uiElements.getLogArea().setStyle(UIConst.STYLE_OF_BACKGROUND);
        uiElements.getLogArea().setPadding(new Insets(10, 10, 10, 10));

        uiElements.getScrollLogPane().setPrefSize(UIConst.WIDTH_OF_MAIN_PANE, UIConst.HEIGHT_OF_LOGGING_PANE);
        uiElements.getScrollLogPane().setBorder(UIConst.BORDER_DEFAULT_SMALL);
        uiElements.getScrollLogPane().setStyle(UIConst.STYLE_OF_BACKGROUND);
        uiElements.getScrollLogPane().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        uiElements.getScrollLogPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        uiElements.getScrollLogPane().setContent(uiElements.getLogArea());

        main.getChildren().add(uiElements.getScrollLogPane());
        return main;
    }

    private Pane initStatisticPane() {
        VBox statisticVBox = new VBox();
        statisticVBox.setPrefSize(UIConst.WIDTH_OF_MAIN_PANE, UIConst.HEIGHT_OF_STATISTIC);
        statisticVBox.setBorder(UIConst.BORDER_DEFAULT_SMALL);
        statisticVBox.setStyle(UIConst.STYLE_OF_BACKGROUND);

        Label statisticLabel = new Label("Statistic");
        statisticLabel.setPadding(new Insets(10, 10, 5, 10));
        statisticLabel.setFont(Font.font("Dialog", FontWeight.BOLD, 25));

        BorderPane statisticLabelPane = new BorderPane();
        statisticLabelPane.setCenter(statisticLabel);
        statisticLabelPane.setPrefSize(UIConst.WIDTH_OF_MAIN_PANE, UIConst.HEIGHT_OF_LABEL);

        VBox todayVBox = generateStatisticPane("TODAY", uiElements.getStatisticApplyTodayLabel(), uiElements.getStatisticConnectTodayLabel());
        VBox totalVBox = generateStatisticPane("TOTAL", uiElements.getStatisticApplyTotalLabel(), uiElements.getStatisticConnectTotalLabel());

        HBox statisticHBox = new HBox(todayVBox, totalVBox);
        statisticVBox.getChildren().addAll(statisticLabelPane, statisticHBox);

        return statisticVBox;
    }

    private VBox generateStatisticPane(String name, Label applyLabel, Label connectLabel) {
        VBox totalVBox = new VBox();

        Label totalTextLabel = new Label(name);
        totalTextLabel.setPadding(new Insets(5, 10, 10, 10));
        totalTextLabel.setFont(Font.font("Dialog", FontWeight.BOLD, 18));
        BorderPane totalTextLabelPane = new BorderPane();
        totalTextLabelPane.setCenter(totalTextLabel);
        totalTextLabelPane.setPrefSize(UIConst.WIDTH_OF_MAIN_PANE / 2, UIConst.HEIGHT_OF_LABEL);

        HBox totalApplyPane = new HBox();
        totalApplyPane.setPadding(new Insets(5, 50, 5, 100));
        Label applytotalTextLabel = new Label("Applied: ");
        applytotalTextLabel.setFont(Font.font("Dialog", FontWeight.NORMAL, 16));
        applyLabel.setFont(Font.font("Dialog", FontWeight.NORMAL, 16));
        totalApplyPane.getChildren().addAll(applytotalTextLabel, applyLabel);

        HBox totalConnectPane = new HBox();
        totalConnectPane.setPadding(new Insets(5, 50, 5, 100));
        Label connecttotalTextLabel = new Label("Connect: ");
        connecttotalTextLabel.setFont(Font.font("Dialog", FontWeight.NORMAL, 16));
        connectLabel.setFont(Font.font("Dialog", FontWeight.NORMAL, 16));
        totalConnectPane.getChildren().addAll(connecttotalTextLabel, connectLabel);

        totalVBox.getChildren().addAll(totalTextLabelPane, totalApplyPane, totalConnectPane);
        return totalVBox;
    }

}
