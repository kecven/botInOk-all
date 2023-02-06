package digital.moveto.botinok.client.ui;

import digital.moveto.botinok.client.config.GlobalConfig;
import digital.moveto.botinok.client.config.UIConst;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.enums.Location;
import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.entities.MadeContact;
import digital.moveto.botinok.client.service.AccountService;
import digital.moveto.botinok.client.service.MadeApplyService;
import digital.moveto.botinok.client.service.MadeContactService;
import digital.moveto.botinok.client.playwright.PlaywrightService;
import digital.moveto.botinok.client.utils.BotinokUtils;
import jakarta.annotation.PostConstruct;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Component
public class UiElements {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private GlobalConfig globalConfig;

    @Autowired
    private AccountService accountService;

    @Autowired
    private MadeApplyService madeApplyService;

    @Autowired
    private MadeContactService madeContactService;

    public static Stage stage;

    private final Label userNameLabel = new Label("");
    private final CheckBox workInShabatCheckBox = new CheckBox("Work in Shabat");
    private final CheckBox activeSearch = new CheckBox("Active search");
    private final ComboBox<Location> location = new ComboBox<>(FXCollections.observableArrayList(Location.values()));
    private final TextField positionsField = new TextField();
    private final Button startButton = new Button("Loading...");    //after finish loading text will change to start
    private final ScrollPane scrollLogPane = new ScrollPane();
    private final ScrollPane scrollAccountPane = new ScrollPane();
    private final VBox accountVBox = new VBox();
    private final TextFlow logArea = new TextFlow();
    private final Label statisticConnectTodayLabel = new Label("0");
    private final Label statisticApplyTodayLabel = new Label("0");
    private final Label statisticConnectTotalLabel = new Label("0");
    private final Label statisticApplyTotalLabel = new Label("0");

    private Account selectAccount;

    private final Button saveButton = new Button("Save");

    @PostConstruct
    private void initElements() {

        getUserNameLabel().setPadding(new Insets(10, 10, 10, 10));
        getUserNameLabel().setFont(Font.font("Dialog", FontWeight.BOLD, 22));

        getWorkInShabatCheckBox().setPadding(new Insets(5, 10, 5, 10));
        getWorkInShabatCheckBox().setSelected(true);
        getWorkInShabatCheckBox().setFont(new Font(16));
        getWorkInShabatCheckBox().setTooltip(new Tooltip("If you want to work in Shabat, check this box."));
        getWorkInShabatCheckBox().setCursor(Cursor.HAND);
        getWorkInShabatCheckBox().setOnMouseClicked(e-> saveSettingForUser());


        getActiveSearch().setPadding(new Insets(5, 10, 5, 10));
        getActiveSearch().setSelected(true);
        getActiveSearch().setFont(new Font(16));
        getActiveSearch().setTooltip(new Tooltip("If you want to search for new jobs, check this box."));
        getActiveSearch().setCursor(Cursor.HAND);
        getActiveSearch().setOnMouseClicked(e-> saveSettingForUser());

        getPositionsField().setPromptText("Manager, Developer, etc...");
        getPositionsField().setPadding(new Insets(0, 10, 10, 10));
        getPositionsField().setPrefSize(UIConst.WIDTH_OF_SETTING, 20);
        getPositionsField().setFont(new Font(16));
        getPositionsField().setTooltip(new Tooltip("Enter positions you want to search. Separate by comma."));
        getPositionsField().setOnInputMethodTextChanged(e->saveSettingForUser());

        getLocation().setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_LABEL);
        getLocation().setPadding(new Insets(5, 10, 5, 10));
        getLocation().setPromptText("Location");
        getLocation().setTooltip(new Tooltip("Choose you location where you want to work."));
        getLocation().setOnInputMethodTextChanged(e->saveSettingForUser());

        getStartButton().setCursor(Cursor.WAIT);
        getStartButton().setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        getStartButton().setPrefSize(UIConst.WIDTH_OF_SETTING, 40);
        getStartButton().setFont(new Font(18));
        getStartButton().setTextFill(Color.WHITE);

        addLogToLogArea("Loading... This may take a few minutes. Please wait.");
    }

    public void addLogToLogAreaWithLinks(String text, List<String> links) {
        Text timeText = new Text(currentBeautyTime());
        timeText.setFill(Color.BLUE);
        timeText.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));

        List<Node> result = new ArrayList<>();

        Text logText = new Text(" - ");
        logText.setFill(Color.BLACK);
        logText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));
        result.add(logText);

        List<Hyperlink> hyperlinks = FXCollections.observableArrayList();
        String[] textParts = text.split("\\$");
        for (int i = 0; i < links.size(); i++) {
            logText = new Text(textParts[i]);
            logText.setFill(Color.BLACK);
            logText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));

            Hyperlink hyperlink = new Hyperlink(links.get(i));
            hyperlink.setOnAction(event -> {
                PlaywrightService linkForBrowser = context.getBean(PlaywrightService.class);
                linkForBrowser.start(Paths.get(globalConfig.pathToStateFolder + getSelectAccount().getFolder()), false);
                linkForBrowser.open(hyperlink.getText());
            });
            hyperlink.setTextFill(Color.BLUE);
            hyperlink.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));

            result.add(logText);
            result.add(hyperlink);
        }

        logText = new Text(textParts[textParts.length - 1]);
        logText.setFill(Color.BLACK);
        logText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));
        result.add(logText);

        Text separateLine = new Text("\n");
        separateLine.setFont(Font.font("Helvetica", FontWeight.NORMAL, 10));

        Platform.runLater(
                () -> {
                    logArea.getChildren().add(timeText);
                    logArea.getChildren().addAll(result);
                    logArea.getChildren().add(separateLine);
                    getScrollLogPane().setVvalue(1.0);
                }
        );
    }

    public void addLogToLogArea(String text) {
        Text timeText = new Text(currentBeautyTime());
        timeText.setFill(Color.BLUE);
        timeText.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));

        Text logText = new Text(" - " + text + "\n");
        logText.setFill(Color.BLACK);
        logText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));

        Text separateLine = new Text("\n");
        separateLine.setFont(Font.font("Helvetica", FontWeight.NORMAL, 10));

        Platform.runLater(
                () -> {
                    logArea.getChildren().addAll(timeText, logText, separateLine);
                    getScrollLogPane().setVvalue(1.0);
                }
        );
    }

    public void clearLogArea() {
        Platform.runLater(
                () -> {
                    logArea.getChildren().clear();
                }
        );
    }

    public void setUserNameLabel(String text) {
        Platform.runLater(
                () -> {
                    getUserNameLabel().setText(text);
                }
        );
    }

    private String currentBeautyTime() {
        return "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "]";
    }

    public void changeButtonState(boolean toStart) {
        Platform.runLater(
                () -> {
                    if (toStart) {
                        getStartButton().setText("Start");
                        getStartButton().setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                    } else {
                        getStartButton().setText("Stop");
                        getStartButton().setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                }
        );
    }

    public void updateAccounts(List<Account> accounts) {
        updateAccounts(accounts, accounts.get(0).getId());
    }

    public void updateAccounts(List<Account> accounts, UUID selectAccount) {

        List<BorderPane> borderPaneList = new ArrayList<>(accounts.size());

        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            Label accountLabel = new Label(account.getFullName());


            accountLabel.setPadding(new Insets(5, 0, 5, 0));
            if (selectAccount.equals(account.getId())) {
                updateSettingAndStatisticForAccount(account);
                accountLabel.setFont(Font.font("Dialog", FontWeight.BOLD, 18));
            } else {
                accountLabel.setFont(Font.font("Dialog", FontWeight.NORMAL, 16));
            }
            BorderPane accountLabelPane = new BorderPane();
            accountLabelPane.setCenter(accountLabel);
            accountLabelPane.setPrefSize(UIConst.WIDTH_OF_SETTING - 37, UIConst.HEIGHT_OF_LABEL);
            accountLabelPane.setId("accountId-" + account.getId());
            accountLabelPane.setStyle(UIConst.STYLE_OF_BACKGROUND);

            accountLabelPane.setOnMouseClicked((event)->{
                updateAccounts(accounts, account.getId());
            });
            accountLabelPane.setCursor(Cursor.HAND);


            borderPaneList.add(accountLabelPane);
        }

        Platform.runLater(
                () -> {
                    accountVBox.getChildren().clear();
                    accountVBox.getChildren().addAll(borderPaneList);
                }
        );
    }

    public void updateSettingAndStatisticForAccount(Account account){
        saveSettingForUser();

        selectAccount = account;
        Platform.runLater(
                () -> {
                    workInShabatCheckBox.setSelected(account.getWorkInShabat());
                    activeSearch.setSelected(account.getActiveSearch());
                    positionsField.setText(account.getPosition());
                    userNameLabel.setText(account.getFullName());
                    location.setValue(account.getLocation());
                    updateStatistic();
                }
        );
    }

    public void saveSettingForUser(){
        if (getSelectAccount() != null) {
            selectAccount.setWorkInShabat(workInShabatCheckBox.isSelected());
            selectAccount.setActiveSearch(activeSearch.isSelected());
            selectAccount.setPosition(positionsField.getText());
            selectAccount.setLocation(location.getValue());

            accountService.saveAndFlush(selectAccount);
        }
    }

    public void updateStatistic(){
        if (getSelectAccount() != null) {
            LocalDate todayLocalDate = LocalDate.now();

            List<MadeApply> allApplyForAccount = madeApplyService.findAllByAccount(getSelectAccount());
            final long finalTotalApply = allApplyForAccount.size();
            final long finalTodayApply = allApplyForAccount.parallelStream()
                    .filter(madeApply -> BotinokUtils.equalsDateAndDateTime(todayLocalDate, madeApply.getDate())).count();

            List<MadeContact> allConnectForAccount = madeContactService.findAllByAccount(getSelectAccount());
            final long finalTotalConnect = allConnectForAccount.size();
            final long finalTodayConnect  = allConnectForAccount.parallelStream()
                    .filter(madeContact -> BotinokUtils.equalsDateAndDateTime(todayLocalDate, madeContact.getDate())).count();

            Platform.runLater(
                    () -> {
                        getStatisticApplyTotalLabel().setText(String.valueOf(finalTotalApply));
                        getStatisticApplyTodayLabel().setText(String.valueOf(finalTodayApply));
                        getStatisticConnectTotalLabel().setText(String.valueOf(finalTotalConnect));
                        getStatisticConnectTodayLabel().setText(String.valueOf(finalTodayConnect));
                    }
            );
        }
    }
}
