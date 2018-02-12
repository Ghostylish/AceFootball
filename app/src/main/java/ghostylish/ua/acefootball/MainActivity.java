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
    final int ID_QUALITY = 0;

    DialogFragment dlg1;


   /* @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case ID_QUALITY:

                //Конвертируем коллекцию в массив строк
                final String[] menuDialog = new String[menuListDialog.size()];
                for (int i = 0; i != menuListDialog.size(); i++) {
                    menuDialog[i] = menuListDialog.get(i);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Выберите трансляцию"); // заголовок для диалога

                builder.setItems(menuDialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getApplicationContext(),
                                "Выбранный кот: " + menuDialog[item],
                                Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setCancelable(true);
                return builder.create();

            default:
                return null;
        }
    }*/

   /* @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dlg1 = new layout.DialogFragment();

        //создаем задачу
        MyTask mt = new MyTask();
        //запускаем задачу
        mt.execute();

        //пауза в 5 секунд пока загрузятся данные
        try {
            TimeUnit.SECONDS.sleep(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boxAdapter = new BoxAdapter(this, matches);
        // настраиваем список
        ListView lvMain = (ListView) findViewById(R.id.lvMain);
        lvMain.setAdapter(boxAdapter);



        /*  parent – View-родитель для нажатого пункта, в нашем случае - ListView
            view – это нажатый пункт, в нашем случае – TextView из android.R.layout.simple_list_item_1
            position – порядковый номер пункта в списке
            id – идентификатор элемента,*/
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                _position = position;
                AceObject ao = new AceObject();
                ao.execute();

                /*try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                //showDialog(ID_QUALITY);
                Log.d("Position", "Позиция" + matches.get(position).commandGuest);
               // findViewById(R.id.loading).setVisibility(View.GONE);
            }
        };
        lvMain.setOnItemClickListener(adapterViewListener);

    }
    //этот класс необходим для парсинга самой страницы с id-трансляций
    class AceObject extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPostExecute(Void aVoid) {
    //        showDialog(ID_QUALITY);
                FragmentManager manager = getSupportFragmentManager();
                dlg1.show(manager, "dlg1");
        }

        @Override
        protected Void doInBackground(Void... params) {
            //org.jsoup.nodes.Document doc;
            try {
                //Считываем выбранную страницу по индексу
                /*doc = Jsoup.connect(matches.get(_position).url).get();
                Elements elements = doc.select(".translation-item");
                Log.d("myLog", "парсим меню");
                if (doc != null) {
                    for (Element element : elements) {
                        menuListDialog.add(element.text());
                        Log.d("myLog", element.text());
                    }
                }*/



                //Считываем выбранную страницу по индексу
                doc = Jsoup.connect(matches.get(_position).url).userAgent("Mozilla").get();
                if (doc != null) {
                    //если нет трансляций то ничего не делаем
                    if (doc.select(".live-table").first() != null){
                    Element table = doc.select(".live-table").first();
                    Elements tableRows = table.select("tr");
                    tableRows.remove(0);
                    for (Element tr: tableRows){
                        if(tr.select("td").get(1).text().contains("SopCast")) continue;
                        menuListDialog.add(tr.select("td").get(3).text());
                        idListAceStream.add(tr.select("td").get(7).child(0).attr("href"));
                    }
                }

                }



                /*Log.d("myLog", "парсим таблицу");
                    Elements tableRows = table.select("tr").select("td");
                    for (Element row : tableRows) {
                        menuListDialog.add(row.text());
                       // Log.d("SUPER", row.select("td").get(3).text());
                    }
                }*/


            } catch (IOException e) {
                //Если не получилось считать
                Toast.makeText(MainActivity.this, "Что-то не так", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //Считываем заглавную страницу www.lfootball.ws
                doc = Jsoup.connect("http://www.lfootball.ws/").get();
                Elements elements = doc.select(".eventsListUl").select("li.fl");
                if (doc != null) {
                    for (Element element : elements) {

                        matches.add(new Match(element.select(".name").first().text(), element.select(".name").get(1).text(), element.select(".img").attr("src"),
                                element.select(".img").get(1).attr("src"), element.select(".date").text(), element.select(".liga").text(), element.select(".link").attr("href")));
                        //Log.d("myLog", "Матч: " + element.select(".link").attr("title"));
                        Log.d("myLog", "Ссылка: " + element.select(".link").attr("href"));
                        Log.d("myLog", "Команда дома: " + element.select(".name").first().text());
                        Log.d("myLog", "Команда гости: " + element.select(".name").get(1).text());
                        if (element.select(".img").attr("src").contains("http"))
                        {
                            Log.d("myLog", "Картинка1: " + element.select(".img").attr("src"));
                        }
                        else  Log.d("myLog", "Картинка1: " + "http://www.lfootball.ws/" + element.select(".img").attr("src"));
                        if (element.select(".img").get(1).attr("src").contains("http"))
                        {
                            Log.d("myLog", "Картинка2: " + element.select(".img").get(1).attr("src"));
                        }
                        else Log.d("myLog", "Картинка2: " + "http://www.lfootball.ws/" + element.select(".img").get(1).attr("src"));
                        Log.d("myLog", "Лига: " + element.select(".liga").text());
                        if (element.select(".date").text()!= "")
                        Log.d("myLog", "Дата: " + element.select(".date").text());
                        else Log.d("myLog", "Дата: " + "Live");
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
