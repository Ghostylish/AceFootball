package ghostylish.ua.acefootball;

/**
 * Created by ghost on 27.12.2017.
 */

public class Match {

    String url;
    String title;
    String date;
    String liga;
    String imageSrc1;
    String imageSrc2;
    String commandHome;
    String commandGuest;

    Match(String _commandHome, String _commandGuest, String _imageSrc1, String _imageSrc2){

        imageSrc1 = _imageSrc1;
        imageSrc2 = _imageSrc2;
        commandHome = _commandGuest;
        commandGuest = _commandGuest;
    }

}
