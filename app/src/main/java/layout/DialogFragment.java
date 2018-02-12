package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import ghostylish.ua.acefootball.MainActivity;
import ghostylish.ua.acefootball.R;


public class DialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {

    MainActivity ldv = new MainActivity();
    Intent intent;
    final String LOG_TAG = "myLogs";


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Выберите трансляцию");
        View v = inflater.inflate(R.layout.fragment_dialog, null);
        v.findViewById(R.id.lvDialog);

        //Конвертируем коллекцию в массив строк
        final String[] menuDialog = new String[ldv.menuListDialog.size()];
        for (int i = 0; i != ldv.menuListDialog.size(); i++) {
            menuDialog[i] = ldv.menuListDialog.get(i);
        }
        final String[] idAceStream = new String[ldv.idListAceStream.size()];
        for (int i = 0; i != ldv.idListAceStream.size(); i++) {
            idAceStream[i] = ldv.idListAceStream.get(i);
        }
        //чистим menuListDialog в классе MainActivity для того что бы удалить старые записи
        ldv.menuListDialog.clear();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, menuDialog);
        ListView listView = v.findViewById(R.id.lvDialog);
        listView.setAdapter(adapter);
        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                intent = new Intent("org.acestream.engine.service.AceStreamEngineService", Uri.parse(idAceStream[position]));
                startActivity(intent);
                Toast.makeText(getContext(), menuDialog[position], Toast.LENGTH_SHORT).show();
                dismiss();
            }
        };
        listView.setOnItemClickListener(adapterViewListener);
        return v;
    }

    public void onClick(View v) {
        Log.d(LOG_TAG, "Dialog 1: " + ((Button) v).getText());
        dismiss();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }
}
