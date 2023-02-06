package digital.moveto.botinok.client.config;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class UIConst {

    public final static int WIDTH_OF_SCENE = 800;
    public final static int HEIGHT_OF_SCENE = 600;

    public final static int WIDTH_OF_SETTING = 250;
    public final static int HEIGHT_OF_STATISTIC = 150;
    public final static int HEIGHT_OF_LABEL = 40;
    public final static int HEIGHT_OF_VERSION = 30;
    public final static int HEIGHT_OF_ACCOUNTS = 273;
    public final static int WIDTH_OF_MAIN_PANE = WIDTH_OF_SCENE - WIDTH_OF_SETTING;
    public final static int HEIGHT_OF_LOGGING_PANE = HEIGHT_OF_SCENE - HEIGHT_OF_STATISTIC;

    public final static String STYLE_OF_BACKGROUND = "-fx-background-color: #eeeeee";
    public final static Border BORDER_DEFAULT_SMALL = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));
    public final static Border BORDER_EMPTY = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.EMPTY));
}
