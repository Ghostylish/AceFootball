package ghostylish.ua.acefootball;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {


    ArrayList<Match> matches = new ArrayList<Match>();
    BoxAdapter boxAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // создаем адаптер
        MyTask mt = new MyTask();
        mt.execute();

        //ждём пока загрухятся данные
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boxAdapter = new BoxAdapter(this, matches);
        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);


    }


    class MyTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            org.jsoup.nodes.Document doc = null;
            try {
                //Считываем заглавную страницу www.lfootball.ws
                doc = Jsoup.connect("http://www.lfootball.ws/").get();
                Elements elements = doc.select(".eventsListUl").select("li.fl");
                if (doc != null) {
                    for (Element element : elements) {

                        //matches.add(new Match(element.select(".link").attr("href"), element.select(".link").attr("title")));
                        Log.d("myLog", "Матч: " + element.select(".link").attr("title"));
                        Log.d("myLog", "Ссылка: " + element.select(".link").attr("href"));
                        if (element.select(".img").attr("src").contains("http"))
                        Log.d("myLog", "Картинка1: " + element.select(".img").attr("src"));
                        else  Log.d("myLog", "Картинка1: " + "http://www.lfootball.ws/" + element.select(".img").attr("src"));
                        if (element.select(".img").get(1).attr("src").contains("http"))
                        Log.d("myLog", "Картинка2: " + element.select(".img").get(1).attr("src"));
                        else Log.d("myLog", "Картинка2: " + "http://www.lfootball.ws/" + element.select(".img").get(1).attr("src"));
                    }
                }

            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            return null;
        }

        //рабочий вариант
       /* protected Void doInBackground(Void... params) {

            org.jsoup.nodes.Document doc = null;
            try {
                //Считываем заглавную страницу www.lfootball.ws
                doc = Jsoup.connect("http://www.lfootball.ws/").get();
                Elements elements = doc.select(".eventsListUl").select(".fl").select(".link");
                if (doc != null) {
                    for (Element element : elements) {

                       //matches.add(new Match(element.attr("href").toString(), element.attr("title")));
                        Log.d("myLog", "Матч: " + element.attr("title"));
                        Log.d("myLog", "Ссылка: " + element.attr("href").toString());
                    }
                }

            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            return null;
        }*/


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if(matches==null){
                Toast.makeText(MainActivity.this, "Данные не загружены", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(MainActivity.this, "Супер", Toast.LENGTH_SHORT).show();
            //отсюда мы можем передать наш массив с урлами картинок
            //for (int i=0;i<=titleMatch.size();i++){
             //  Log.d("Из списка", titleMatch.get(i).toString());
           // }

            //Toast.makeText(MainActivity.this, "onPostExecute", Toast.LENGTH_LONG).show();
        }
    }

}
