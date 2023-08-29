package digital.moveto.botinok.client.config;

import java.util.Arrays;
import java.util.List;

public interface ClientConst {

    int DEFAULT_TIMEOUT_FOR_BROWSER = 60_000;

    String LINKEDIN_URL = "https://www.linkedin.com";
    String DEFAULT_URL_FOR_SEARCH = "https://www.linkedin.com/search/results/people/?geoUrn=%5B%22101620260%22%5D&keywords=human%20resources&origin=FACETED_SEARCH&sid=ynM&page=2";
    String DEFAULT_URL_FOR_SEARCH_WITHOUT_PARAMS = "https://www.linkedin.com/search/results/people/?";
    String DEFAULT_URL_FOR_MY_CONNECTIONS = "https://www.linkedin.com/mynetwork/invite-connect/connections/";

    int MAX_PAGE_ON_LINKEDIN = 100;
    int MAX_COUNT_PAGE_FOR_ONE_TIME = 50;
    int SLEEP_BETWEEN_START_BOT_FOR_DIFFERENT_USERS = 20 * 1000;

    int COUNT_POSITION_ON_ONE_PAGE = 25;
    int COUNT_PAGE_FOR_SEARCH_POSITIONS = 10;

    String DEFAULT_FOLDER_FOR_DEFAULT_USER_DATA_DIR = "default";

    List<String> SEARCH_KEYWORDS = Arrays.asList("human%20resources", "Talent%20Acquisition%20Specialist", "Executive%20Recruiting", "HR", "HR%20Business%20Partner", "Tech%20Recruiter");
    String SPLIT_FOR_RANDOM_KEYWORDS = ",";

    String VERSION = digital.moveto.botinok.model.Const.VERSION;

    String MOVE_TO_DIGITAL = "http://moveto.digital:8080";
}
