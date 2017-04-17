package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import petcare.com.mypetcare.Adapter.JoinPopupListViewAdapter;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GeneralMultipartApi;
import petcare.com.mypetcare.Util.PicUtil;
import petcare.com.mypetcare.Util.VolleyMultipartRequest;
import petcare.com.mypetcare.Util.VolleySingleton;

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

//    private ImageButton ibPic1;
    private ImageView ivPic1;
    private ImageView ivPic2;
    private ImageView ivPic3;

    private ArrayList<String> dateList;

    private NumberPicker npYear;
    private NumberPicker npMonth;
    private NumberPicker npDate;

    private Calendar cal;

    private static final SimpleDateFormat SDF_YYYYMMDD = new SimpleDateFormat("yyyy년 MM월 dd일");
    private static final SimpleDateFormat SDF_YYYYMM = new SimpleDateFormat("yyyy년 MM월");

    private String petCode = null;
    private String birth = null;

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

        ivPic1 = (ImageView) findViewById(R.id.ivPic1);
        ivPic2 = (ImageView) findViewById(R.id.ivPic2);
        ivPic3 = (ImageView) findViewById(R.id.ivPic3);

        ivPic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });
        ivPic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
        ivPic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 3);
            }
        });

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
                petCode = "00" + (position + 1);
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
                birth = format;
                ageDialog.dismiss();
            }
        });

        View.OnClickListener doneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();



//                String token = global.getToken();
//                String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
//                Map params = new HashMap<>();
//                params.put("USER_EMAIL", "test@test.com");
//
//                if (StringUtils.isNotEmpty(petCode)) {
//                    params.put("PET_KND_CD", petCode);
//                }
//                if (StringUtils.isNotEmpty(birth)) {
//                    params.put("PET_BIRTH", birth);
//                }
//
//                String contentType = "application/json";
//                String serviceId = "MPMS_01002";
//
//                HttpConn saveApi = new HttpConn();
//                saveApi.setContext(global);
//
//                try {
//                    saveApi.execute(contentType, url, serviceId, params, token).get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
//                }

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

    public class MultipartApi extends GeneralMultipartApi {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private void save() {
        MultipartApi multipartApi = new MultipartApi();
        Map headers = new HashMap<>();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId = "MPMS_01002";
        String contentType = "multipart/form-data";

        headers.put("url", url);
        headers.put("serviceName", serviceId);

        Map params = new HashMap<>();
        String imagePath = (String) ivPic1.getTag();
        params.put("path", imagePath);
        multipartApi.execute(headers, params);

//        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(url, headers, new Response.Listener<NetworkResponse>() {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                String resultResponse = new String(response.data);
////                try {
////                    JSONObject result = new JSONObject(resultResponse);
////                    String status = result.getString("status");
////                    String message = result.getString("message");
////
////                    if (status.equals("0")) {
////                        Log.i("Messsage", message);
////                    } else {
////                        Log.i("Unexpected", message);
////                    }
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                NetworkResponse networkResponse = error.networkResponse;
//                String errorMessage = "Unknown error";
//                if (networkResponse == null) {
//                    if (error.getClass().equals(TimeoutError.class)) {
//                        errorMessage = "Request timeout";
//                    } else if (error.getClass().equals(NoConnectionError.class)) {
//                        errorMessage = "Failed to connect server";
//                    }
//                } else {
//                    String result = new String(networkResponse.data);
//                    try {
//                        JSONObject response = new JSONObject(result);
//                        String status = response.getString("status");
//                        String message = response.getString("message");
//
//                        Log.e("Error Status", status);
//                        Log.e("Error Message", message);
//
//                        if (networkResponse.statusCode == 404) {
//                            errorMessage = "Resource not found";
//                        } else if (networkResponse.statusCode == 401) {
//                            errorMessage = message+" Please login again";
//                        } else if (networkResponse.statusCode == 400) {
//                            errorMessage = message+ " Check your inputs";
//                        } else if (networkResponse.statusCode == 500) {
//                            errorMessage = message+" Something is getting wrong";
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                Log.i("Error", errorMessage);
//                error.printStackTrace();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("USER_EMAIL", "test@test.com");
//                params.put("PET_KND_CD", "001");
//                params.put("PET_BIRTH", "20170101");
//                params.put("name", "1");
//                params.put("filename", "q1");
////                params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
////                params.put("name", mNameInput.getText().toString());
////                params.put("location", mLocationInput.getText().toString());
////                params.put("about", mAvatarInput.getText().toString());
////                params.put("contact", mContactInput.getText().toString());
//                return params;
//            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                // file name could found file base or direct access from real path
//                // for now just get bitmap data from ImageView
//                params.put("multipart", new DataPart("file_avatar.jpg", PicUtil.getFileDataFromDrawable(getBaseContext(), ivPic1.getDrawable()), "image/jpeg"));
//
//                return params;
//            }
//        };
//
//        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), getPicture(data.getData()));

            switch (requestCode) {
                case 1:
                    ivPic1.setTag(getPathFromUri(data.getData()));
                    ivPic1.setImageDrawable(bitmapDrawable);
                    break;
                case 2:
                    ivPic2.setImageDrawable(bitmapDrawable);
                    break;
                case 3:
                    ivPic3.setImageDrawable(bitmapDrawable);
                    break;
            }
        }
    }

    public String getPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();

        return path;
    }

    public Bitmap getPicture(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return BitmapFactory.decodeFile(picturePath);
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
