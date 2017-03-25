package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import petcare.com.mypetcare.Adapter.JoinPopupListViewAdapter;
import petcare.com.mypetcare.R;

public class JoinActivity extends BaseActivity {
    private Dialog speciesDialog;
    private Dialog ageDialog;

    private ListView lvPopup;
    private Integer speciesPosition;
    private Button btSpecies;
    private Button btBirth;
    private Button btAgeDone;
    private ImageButton ibBack;
    private TextView tvDone;
    private Button btDone;

    private ArrayList<String> dateList;

    private NumberPicker npYear;
    private NumberPicker npMonth;
    private NumberPicker npDate;

    private Calendar cal;

    private static final SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy년 MM월 dd일");
    private static final SimpleDateFormat SDF_YYYYMM = new SimpleDateFormat("yyyy년 MM월");

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

        cal = Calendar.getInstance();

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
                speciesDialog.show();
            }
        });

        speciesDialog = new Dialog(JoinActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        speciesDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        speciesDialog.setContentView(R.layout.popup_species);

        lvPopup = (ListView) speciesDialog.findViewById(R.id.lv_popup_species);
        lvPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                speciesPosition = position;
                TextView tv = (TextView) view.findViewById(R.id.tv_join_inner);
                btSpecies.setText(tv.getText());
                speciesDialog.dismiss();
            }
        });

        btBirth = (Button) findViewById(R.id.btPetBirth);
        btBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ageDialog.show();
            }
        });

        ageDialog = new Dialog(JoinActivity.this, R.style.Theme_AppCompat_DayNight_Dialog);
        ageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ageDialog.setContentView(R.layout.popup_age);

        npDate = (NumberPicker) ageDialog.findViewById(R.id.np_popup_age_date);
        npYear = (NumberPicker) ageDialog.findViewById(R.id.np_popup_age_year);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DAY_OF_MONTH);
        int maxDate = cal.getMaximum(Calendar.DAY_OF_MONTH);

        npYear.setMinValue(1980);
        npYear.setMaxValue(year);
        npYear.setValue(year - 1);
        npYear.setWrapSelectorWheel(true);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(npYear, Color.parseColor("#00000000"));
        setNumberPickerTextColor(npYear, (getResources().getColor(R.color.colorPrimary)));
        npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                cal.set(Calendar.YEAR, newVal);
                int maxDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                npDate.setMaxValue(maxDate + 1);
            }
        });

        npMonth = (NumberPicker) ageDialog.findViewById(R.id.np_popup_age_month);
        npMonth.setMinValue(1);
        npMonth.setMaxValue(12);
        npMonth.setValue(month);
        npMonth.setWrapSelectorWheel(true);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setDividerColor(npMonth, Color.parseColor("#00000000"));
        setNumberPickerTextColor(npMonth, (getResources().getColor(R.color.colorPrimary)));
        npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                cal.set(Calendar.MONTH, newVal - 1);
                int maxDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                npDate.setMaxValue(maxDate + 1);
            }
        });

        dateList = new ArrayList<>();
        dateList.add("-");
        for (int i = 1; i < (maxDate + 1); i++) {
            dateList.add(String.valueOf(i));
        }
        String[] dateArr = new String[dateList.size()];
        dateArr = dateList.toArray(dateArr);
        npDate.setMinValue(1);
        npDate.setMaxValue(maxDate + 1);
        npDate.setDisplayedValues(dateArr);
        npDate.setWrapSelectorWheel(true);
        npDate.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setNumberPickerTextColor(npDate, (getResources().getColor(R.color.colorPrimary)));
        setDividerColor(npDate, Color.parseColor("#00000000"));

        btAgeDone = (Button) ageDialog.findViewById(R.id.bt_popup_age_done);
        btAgeDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = npYear.getValue();
                int month = npMonth.getValue();
                int date = npDate.getValue() - 1;

                Calendar tmpCal = Calendar.getInstance();
                tmpCal.set(Calendar.YEAR, year);
                tmpCal.set(Calendar.MONTH, month - 1);

                String format;
                if (date <= 0 || date > 31) {
                    tmpCal.set(Calendar.DAY_OF_MONTH, 1);
                    format = SDF_YYYYMM.format(new Date(tmpCal.getTimeInMillis()));
                } else {
                    tmpCal.set(Calendar.DAY_OF_MONTH, date);
                    format = SDF_YYYYMMDD.format(new Date(tmpCal.getTimeInMillis()));
                }

                Toast.makeText(getApplicationContext(), format, Toast.LENGTH_SHORT).show();
                btBirth.setText(format);
                ageDialog.dismiss();
            }
        });

        View.OnClickListener doneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(JoinActivity.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                alert.setMessage("회원가입이 완료되었습니다.");
                alert.setCancelable(false);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
                Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                btDone.setTextColor(getResources().getColor(R.color.normalFont));

            }
        };

        tvDone = (TextView) findViewById(R.id.tv_join_done);
        tvDone.setOnClickListener(doneClickListener);

        btDone = (Button) findViewById(R.id.btnOk);
        btDone.setOnClickListener(doneClickListener);

        ibBack = (ImageButton) findViewById(R.id.ib_join_back);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                } catch (IllegalAccessException e) {
                } catch (IllegalArgumentException e) {
                }
            }
        }
        return false;
    }
    private void setDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
