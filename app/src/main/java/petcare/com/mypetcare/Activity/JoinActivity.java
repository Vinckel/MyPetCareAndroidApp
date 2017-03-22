package petcare.com.mypetcare.Activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import petcare.com.mypetcare.Adapter.JoinPopupListViewAdapter;
import petcare.com.mypetcare.R;

public class JoinActivity extends AppCompatActivity {
    private Dialog dialog;
    private ListView lvPopup;
    private Integer speciesPosition;
    private Button btSpecies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.join_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionBarView = inflater.inflate(R.layout.join_custom_actionbar, null);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, lp1);

        Toolbar parent = (Toolbar) actionBarView.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        btSpecies = (Button) findViewById(R.id.btPetSpecies);
        btSpecies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JoinPopupListViewAdapter adapter = new JoinPopupListViewAdapter(getApplicationContext(), speciesPosition);
                lvPopup.setAdapter(adapter);
                adapter.addItem("소형견");
                adapter.addItem("중형견");
                adapter.addItem("대형견");
                adapter.addItem("고양이");
                adapter.addItem("기타");
                dialog.show();
            }
        });

        dialog = new Dialog(JoinActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_species);

        lvPopup = (ListView) dialog.findViewById(R.id.lv_popup_species);
        lvPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                speciesPosition = position;
                TextView tv = (TextView) view.findViewById(R.id.tv_join_inner);
                btSpecies.setText(tv.getText());
                dialog.dismiss();
            }
        });
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
