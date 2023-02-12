package digital.moveto.botinok.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalConfig {

    @Value("${botinok.speedOfBot:0.3}")
    public double speedOfBot;

    @Value("${botinok.type:free}")
    public String type;

    @Value("${botinok.countConnectionForOneTime:15}")
    public int countConnectionForOneTime;

    @Value("${botinok.countParseForOneTime:50}")
    public int countParseForOneTime;

    @Value("${botinok.countApplyForOneTime:15}")
    public int countApplyForOneTime;


    @Value("${botinok.reverseAccounts:false}")
    public boolean reverseAccounts;

    @Value("${botinok.pathToStateFolder}")
    public String pathToStateFolder;

    @Value("${botinok.headlessBrowser:true}")
    public boolean headlessBrowser;

    @Value("${botinok.startByDefault:false}")
    public boolean startByDefault;

}
