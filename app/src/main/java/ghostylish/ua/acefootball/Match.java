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

    public void setImageSrc1(String imageSrc1) {
        if (imageSrc1.contains("http")){
            this.imageSrc1 = imageSrc1;
        }
        else
        this.imageSrc1 = "http://www.lfootball.ws/" + imageSrc1;
    }
    public void setImageSrc2(String imageSrc2) {
        if (imageSrc2.contains("http")){
            this.imageSrc2 = imageSrc2;
        }
        else
        this.imageSrc2 = "http://www.lfootball.ws/" + imageSrc2;
    }

    Match(String _commandHome, String _commandGuest, String _imageSrc1, String _imageSrc2, String _date, String _liga, String _url){

        setImageSrc1(_imageSrc1);
        setImageSrc2(_imageSrc2);
        //imageSrc1 = _imageSrc1;
        //imageSrc2 = _imageSrc2;
        commandHome = _commandHome;
        commandGuest = _commandGuest;
        date = _date;
        url = _url;
        liga = _liga;
    }

}
