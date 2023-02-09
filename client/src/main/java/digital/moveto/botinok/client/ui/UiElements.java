package digital.moveto.botinok.client.ui;

import digital.moveto.botinok.client.config.GlobalConfig;
import digital.moveto.botinok.client.config.UIConst;
import digital.moveto.botinok.client.playwright.PlaywrightService;
import digital.moveto.botinok.client.service.ClientAccountService;
import digital.moveto.botinok.client.service.ClientMadeApplyService;
import digital.moveto.botinok.client.service.ClientMadeContactService;
import digital.moveto.botinok.model.entities.Account;
import digital.moveto.botinok.model.entities.MadeApply;
import digital.moveto.botinok.model.entities.MadeContact;
import digital.moveto.botinok.model.entities.enums.LocationProperty;
import digital.moveto.botinok.model.utils.BotinokUtils;
import jakarta.annotation.PostConstruct;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
    private ClientAccountService clientAccountService;

    @Autowired
    private ClientMadeApplyService clientMadeApplyService;

    @Autowired
    private ClientMadeContactService clientMadeContactService;

    @Autowired
    private PlaywrightService browserForLinks;

    public static Stage stage;

    private final Label userNameLabel = new Label("");
    private final CheckBox workInShabatCheckBox = new CheckBox("Work in Shabat");
    private final CheckBox activeSearch = new CheckBox("Active search");
    private final AutoCompleteTextField<LocationProperty> locationAutoCompleteTextField = new AutoCompleteTextField(LocationProperty.getAllSortedLocations());
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
    private final Slider countDailyApplySlider = new Slider(0, 30, 15);
    private final Slider countDailyConnectSlider = new Slider(0, 30, 15);

    private Account selectAccount;

    private final Button saveButton = new Button("Save");

    @PostConstruct
    private void initElements() {

        getUserNameLabel().setPadding(new Insets(0, 10, 10, 10));
        getUserNameLabel().setFont(Font.font("Dialog", FontWeight.BOLD, 22));

        getWorkInShabatCheckBox().setPadding(new Insets(5, 10, 5, 10));
        getWorkInShabatCheckBox().setSelected(true);
        getWorkInShabatCheckBox().setFont(new Font(14));
        getWorkInShabatCheckBox().setTooltip(new Tooltip("If you want to work in Shabat, check this box."));
        getWorkInShabatCheckBox().setCursor(Cursor.HAND);
        getWorkInShabatCheckBox().setOnMouseClicked(e-> saveSettingForUser());


        getActiveSearch().setPadding(new Insets(5, 10, 10, 10));
        getActiveSearch().setSelected(true);
        getActiveSearch().setFont(new Font(14));
        getActiveSearch().setTooltip(new Tooltip("If you want to search for new jobs, check this box."));
        getActiveSearch().setCursor(Cursor.HAND);
        getActiveSearch().setOnMouseClicked(e -> saveSettingForUser());

        getPositionsField().setPromptText("Manager, Developer, etc...");
        getPositionsField().setPadding(new Insets(0, 10, 10, 10));
        getPositionsField().setPrefSize(UIConst.WIDTH_OF_SETTING, 16);
        getPositionsField().setFont(new Font(16));
        getPositionsField().setTooltip(new Tooltip("Enter positions you want to search. Separate by comma."));
        getPositionsField().setOnInputMethodTextChanged(e -> saveSettingForUser());
        getPositionsField().textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue.length() > 255) {
                        showAlert(Alert.AlertType.WARNING, "Many positions", "Positions can't be more than 255 characters", "Please select less positions");
                        getPositionsField().setText(oldValue);
                    }
                }
        );
        getLocationAutoCompleteTextField().setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_LABEL);
        getLocationAutoCompleteTextField().setPadding(new Insets(5, 10, 5, 10));
        getLocationAutoCompleteTextField().setPromptText("Location");
        getLocationAutoCompleteTextField().setTooltip(new Tooltip("Choose you location where you want to work"));
        getLocationAutoCompleteTextField().setOnInputMethodTextChanged(e -> saveSettingForUser());

        getLocationAutoCompleteTextField().getEntryMenu().setOnAction(e -> {
            ((MenuItem) e.getTarget()).addEventHandler(Event.ANY, event -> {
                if (getLocationAutoCompleteTextField().getLastSelectedObject() != null) {
                    getLocationAutoCompleteTextField().setText(getLocationAutoCompleteTextField().getLastSelectedObject().toString());
                }
            });
        });

        getStartButton().setCursor(Cursor.WAIT);
        getStartButton().setBackground(new Background(new BackgroundFill(Color.ORANGE, CornerRadii.EMPTY, Insets.EMPTY)));
        getStartButton().setPrefSize(UIConst.WIDTH_OF_SETTING, 40);
        getStartButton().setFont(new Font(18));
        getStartButton().setTextFill(Color.WHITE);

        getCountDailyConnectSlider().setShowTickLabels(true);
        getCountDailyConnectSlider().setMajorTickUnit(5);
        getCountDailyConnectSlider().setOnMouseClicked(e -> saveSettingForUser());
//        getCountDailyConnectSlider().setOnMouseReleased(e->saveSettingForUser());


        getCountDailyApplySlider().setShowTickLabels(true);
        getCountDailyApplySlider().setMajorTickUnit(5);
        getCountDailyApplySlider().setOnMouseClicked(e->saveSettingForUser());
//        getCountDailyApplySlider().setOnMouseReleased(e->saveSettingForUser());


        getAccountVBox().setStyle(UIConst.STYLE_OF_BACKGROUND);
        getAccountVBox().setPadding(new Insets(10, 10, 10, 10));
        getAccountVBox().setBorder(UIConst.BORDER_EMPTY);
        getAccountVBox().setPrefSize(UIConst.WIDTH_OF_SETTING - 16, UIConst.HEIGHT_OF_ACCOUNTS);

        getScrollAccountPane().setPadding(new Insets(0, 0, 0, 0));
        getScrollAccountPane().setStyle(UIConst.STYLE_OF_BACKGROUND);
        getScrollAccountPane().setPrefSize(UIConst.WIDTH_OF_SETTING, UIConst.HEIGHT_OF_ACCOUNTS);
        getScrollAccountPane().setBorder(UIConst.BORDER_EMPTY);
        getScrollAccountPane().setStyle(UIConst.STYLE_OF_BACKGROUND);
        getScrollAccountPane().setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        getScrollAccountPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        getScrollAccountPane().setContent(getAccountVBox());

        getLogArea().setPrefSize(UIConst.WIDTH_OF_MAIN_PANE - 10, UIConst.HEIGHT_OF_LOGGING_PANE);
        getLogArea().setBorder(UIConst.BORDER_EMPTY);
        getLogArea().setStyle(UIConst.STYLE_OF_BACKGROUND);
        getLogArea().setPadding(new Insets(10, 10, 10, 10));

        getScrollLogPane().setPrefSize(UIConst.WIDTH_OF_MAIN_PANE, UIConst.HEIGHT_OF_LOGGING_PANE);
        getScrollLogPane().setBorder(UIConst.BORDER_DEFAULT_SMALL);
        getScrollLogPane().setStyle(UIConst.STYLE_OF_BACKGROUND);
        getScrollLogPane().setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        getScrollLogPane().setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        getScrollLogPane().setContent(getLogArea());

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
                if (browserForLinks.getPlaywright() == null) {
                    browserForLinks.start(Paths.get(globalConfig.pathToStateFolder + getSelectAccount().getFolder()), false);
                }
                browserForLinks.openInNewPage(hyperlink.getText());
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

//        Text userNameText = new Text();
//        if (getSelectAccount() != null) {
//            userNameText.setText("{" + getSelectAccount().getShortName() + "}");
//            userNameText.setFill(Color.RED);
//            userNameText.setFont(Font.font("Helvetica", FontWeight.BOLD, 16));
//        }

        Text logText = new Text(" - " + text + "\n");
        logText.setFill(Color.BLACK);
        logText.setFont(Font.font("Helvetica", FontWeight.NORMAL, 16));

        Text separateLine = new Text("\n");
        separateLine.setFont(Font.font("Helvetica", FontWeight.NORMAL, 8));

        Platform.runLater(
                () -> {
                    logArea.getChildren().addAll(timeText, logText, separateLine);
//                    logArea.getChildren().addAll(timeText, userNameText, logText, separateLine);
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
                    countDailyApplySlider.setValue(account.getCountDailyApply());
                    countDailyConnectSlider.setValue(account.getCountDailyConnect());
                    try {
                        locationAutoCompleteTextField.setText(LocationProperty.getByName(account.getLocation()).getName());
                    } catch (IllegalArgumentException ignored) {
                    }
                    updateStatistic();
                }
        );
    }

    public boolean saveSettingForUser() {
        if (getSelectAccount() != null) {
            LocationProperty selectLocation;
            if (getLocationAutoCompleteTextField().getLastSelectedObject() == null) {
                LocationProperty searchLocation = LocationProperty.getByName(getLocationAutoCompleteTextField().getText());
                if (searchLocation != null) {
                    selectLocation = searchLocation;
                } else {
                    showAlert(Alert.AlertType.WARNING, "Location not found", "Please select location which we provided", "If you can't find location, please contact with us");
                    return false;
                }
            } else {
                selectLocation = getLocationAutoCompleteTextField().getLastSelectedObject();
            }
            if (positionsField.getText().isEmpty()
                    || positionsField.getText().length() < 2) {
                showAlert(Alert.AlertType.WARNING, "Position not found", "Please enter position", "You can enter more than one position, separate by comma");
                return false;
            }
            if (positionsField.getText().length() > 255) {
                showAlert(Alert.AlertType.WARNING, "So many positions", "Please delete some positions", "You can enter more than one position, separate by comma");
                return false;
            }

            selectAccount.setWorkInShabat(workInShabatCheckBox.isSelected());
            selectAccount.setActiveSearch(activeSearch.isSelected());
            selectAccount.setPosition(positionsField.getText());
            selectAccount.setLocation(selectLocation.getKey());
            selectAccount.setCountDailyApply((int) countDailyApplySlider.getValue());
            selectAccount.setCountDailyConnect((int) countDailyConnectSlider.getValue());

            clientAccountService.save(selectAccount);
        }
        return true;
    }

    public void updateStatistic(){
        if (getSelectAccount() != null) {
            LocalDate todayLocalDate = LocalDate.now();

            List<MadeApply> allApplyForAccount = clientMadeApplyService.findAllByAccount(getSelectAccount());
            final long finalTotalApply = allApplyForAccount.size();
            final long finalTodayApply = allApplyForAccount.parallelStream()
                    .filter(madeApply -> BotinokUtils.equalsDateAndDateTime(todayLocalDate, madeApply.getDate())).count();

            List<MadeContact> allConnectForAccount = clientMadeContactService.findAllByAccount(getSelectAccount());
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

    public void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Platform.runLater(
                () -> {
                    Alert alert = new Alert(alertType);
                    alert.setTitle(title);
                    alert.setHeaderText(headerText);
                    alert.setContentText(contentText);

                    alert.showAndWait();
                }
        );
    }
}
