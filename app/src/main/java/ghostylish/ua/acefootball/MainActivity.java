package ghostylish.ua.acefootball;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.PopupMenu;
import android.util.Log;
import android.util.Property;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class MainActivity extends AppCompatActivity {

    ArrayList<Match> matches = new ArrayList<Match>();
    public static ArrayList<String> menuListDialog = new ArrayList<String>();
    public static ArrayList<String> idListAceStream = new ArrayList<String>();
    public String[] menuDialog;
    BoxAdapter boxAdapter;
    org.jsoup.nodes.Document doc;
    int _position = 0;
    ListView lvMain;

    DialogFragment dlg1;
    ProgressBar prBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prBar = (ProgressBar) findViewById(R.id.progressBar);
        dlg1 = new layout.DialogFragment();

        //создаем задачу
        MyTask mt = new MyTask();
        //запускаем задачу
        mt.execute();

        // настраиваем список
        lvMain = (ListView) findViewById(R.id.lvMain);
        prBar.setVisibility(View.VISIBLE);




          /*parent – View-родитель для нажатого пункта, в нашем случае - ListView
            view – это нажатый пункт, в нашем случае – TextView из android.R.layout.simple_list_item_1
            position – порядковый номер пункта в списке
            id – идентификатор элемента,*/
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                _position = position;
                AceObject ao = new AceObject();
                ao.execute();
                //Log.d("Position", "Позиция" + matches.get(position).commandGuest);

            }
        };
        lvMain.setOnItemClickListener(adapterViewListener);

    }
    //этот класс необходим для парсинга самой страницы с id-трансляций
    class AceObject extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {

                //Считываем выбранную страницу по индексу
                doc = Jsoup.connect(matches.get(_position).url).userAgent("Mozilla").get();
                if (doc != null) {
                    //если нет трансляций то ничего не делаем
                    if (doc.select(".live-table").first() != null) {
                        Element table = doc.select(".live-table").first();
                        Elements tableRows = table.select("tr");
                        tableRows.remove(0);
                        for (Element tr : tableRows) {
                            if (tr.select("td").get(1).text().contains("SopCast")) continue;
                            menuListDialog.add(tr.select("td").get(3).text());
                            idListAceStream.add(tr.select("td").get(7).child(0).attr("href"));
                        }
                    }

                }


            } catch (IOException e) {
                //Если не получилось считать
                Toast.makeText(MainActivity.this, "Что-то не так", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (menuListDialog.isEmpty() == false) {
                FragmentManager manager = getSupportFragmentManager();
                dlg1.show(manager, "dlg1");
            } else
                Toast.makeText(MainActivity.this, "Трансляция не началась", Toast.LENGTH_SHORT).show();
        }
    }

    class MyTask extends AsyncTask<Void, Void, ArrayList<Match>> {

        @Override
        protected ArrayList<Match> doInBackground(Void... params) {

            try {
                //Считываем заглавную страницу www.lfootball.ws
                doc = Jsoup.connect("http://www.lfootball.ws/").get();
                Elements elements = doc.select(".eventsListUl").select("li.fl");
                if (doc != null) {
                    for (Element element : elements) {

                        matches.add(new Match(element.select(".name").first().text(), element.select(".name").get(1).text(), element.select(".img").attr("src"),
                                element.select(".img").get(1).attr("src"), element.select(".date").text(), element.select(".liga").text(), element.select(".link").attr("href")));
                    }
                }

            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }

            return matches;
        }



        @Override
        protected void onPostExecute(ArrayList<Match> result) {
            super.onPostExecute(result);
            if(matches.isEmpty()==true){
                Toast.makeText(MainActivity.this, "Данные не загружены", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(MainActivity.this, "Супер", Toast.LENGTH_SHORT).show();

            boxAdapter = new BoxAdapter(MainActivity.this, result);
            lvMain.setAdapter(boxAdapter);
            prBar.setVisibility(View.GONE);

        }
    }

}
