package ghostylish.ua.acefootball;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ghost on 27.12.2017.
 */

public class BoxAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Match> objects;



    BoxAdapter(Context context, ArrayList<Match> matches) {
        ctx = context;
        objects = matches;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    // кол-во элементов
    public int getCount() {
        return objects.size();
    }

    @Override
    // элемент по позиции
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    // id по позиции
    public long getItemId(int position) {
        return position;
    }

    @Override
    // пункт списка
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.match_item, parent, false);
        }
        Match p = getMatch(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        /*((TextView) view.findViewById(R.id.url)).setText(p.url);
        ((TextView) view.findViewById(R.id.title1)).setText(p.title);*/

        ((TextView) view.findViewById(R.id.commandHome)).setText(p.commandHome);
        ((TextView) view.findViewById(R.id.commandGuest)).setText(p.commandGuest);
        ((TextView) view.findViewById(R.id.liga)).setText(p.liga);
        ((TextView) view.findViewById(R.id.date)).setText(p.date);
         Picasso.with(ctx)
                .load(p.imageSrc1)
                 .into(((ImageView) view.findViewById(R.id.image1)));
        Picasso.with(ctx)
                .load(p.imageSrc2)
                .into(((ImageView) view.findViewById(R.id.image2)));

        return view;

    }






    // товар по позиции
    Match getMatch(int position) {
        return ((Match) getItem(position));
    }

}