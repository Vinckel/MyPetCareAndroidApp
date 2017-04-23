package petcare.com.mypetcare.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import petcare.com.mypetcare.Adapter.JoinPopupListViewAdapter;
import petcare.com.mypetcare.Model.CommonResult;
import petcare.com.mypetcare.Model.PetInfoVO;
import petcare.com.mypetcare.R;
import petcare.com.mypetcare.Util.GeneralApi;
import petcare.com.mypetcare.Util.GeneralMultipartApi;
import petcare.com.mypetcare.Util.GsonUtil;
import petcare.com.mypetcare.Util.PicUtil;
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

    private ImageView ivPic1;
    private ImageView ivPic2;
    private ImageView ivPic3;

    private ArrayList<String> dateList;

    private NumberPicker npYear;
    private NumberPicker npMonth;
    private NumberPicker npDate;

    private Calendar cal;

    private static final SimpleDateFormat SDF_YMD_FOR_SAVE = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat SDF_YM_FOR_SAVE = new SimpleDateFormat("yyyyMM");

    private String petCodeForSave = null;
    private String birthForSave = null;

    private boolean isEdit = false;
    private boolean isPicChanged = false;
    private ImageLoader imageLoader;
    private Integer no; // 수정 시 내 전체 펫 중의 순서

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.join_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        no = intent.getIntExtra("no", -1);
        imageLoader = VolleySingleton.getInstance(JoinActivity.this).getImageLoader();

        if (no > 0) {
            isEdit = true;

            PetInfoLoadApi petInfoLoadApi = new PetInfoLoadApi();

            Map headers = new HashMap<>();
            String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
            String serviceId = "MPMS_01003";
            headers.put("url", url);
            headers.put("serviceName", serviceId);

            Map body = new HashMap<>();
            body.put("PET_SN", no);

            petInfoLoadApi.execute(headers, body);
        }

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
                petCodeForSave = "00" + (position + 1);
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
                    format = SDF_YM_FOR_SAVE.format(new Date(tmpCal.getTimeInMillis()));
                    format += "00";
                } else {
                    tmpCal.set(Calendar.DAY_OF_MONTH, date);
                    format = SDF_YMD_FOR_SAVE.format(new Date(tmpCal.getTimeInMillis()));
                }

                String dateStr = year + "년 " + month + "월";
                if (date > 0) {
                    dateStr += (" " + date + "일");
                }

                btBirth.setText(dateStr);
                birthForSave = format;
                ageDialog.dismiss();
            }
        });

        View.OnClickListener doneClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    save();
                }


//                String token = global.getToken();
//                String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
//                Map params = new HashMap<>();
//                params.put("USER_EMAIL", "test@test.com");
//
//                if (StringUtils.isNotEmpty(petCodeForSave)) {
//                    params.put("PET_KND_CD", petCodeForSave);
//                }
//                if (StringUtils.isNotEmpty(birthForSave)) {
//                    params.put("PET_BIRTH", birthForSave);
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

    private boolean validate() {
        if (petCodeForSave == null) {
            Toast.makeText(getApplicationContext(), "반려동물의 종류를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (birthForSave == null) {
            Toast.makeText(getApplicationContext(), "반려동물의 생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public class PetInfoLoadApi extends GeneralApi {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            PetInfoVO petInfoVO = GsonUtil.fromJson(result, PetInfoVO.class);
            if (petInfoVO.getResultCode() != 0) {
                return;
            }

            PetInfoVO.PetInfoObject petInfo = petInfoVO.getData().get(0);
            String birth = petInfo.getBirth();
            String breed = petInfo.getBreed();
            String breedCode = petInfo.getBreedCode();
            List<PetInfoVO.PetInfoObject.PetImageObject> imgList = petInfo.getImgData();

            Integer year = Integer.parseInt(StringUtils.substring(birth, 0, 4));
            Integer month = Integer.parseInt(StringUtils.substring(birth, 4, 6));
            Integer date = Integer.parseInt(StringUtils.substring(birth, 6, 8));

            npYear.setValue(year);
            npMonth.setValue(month);
            npDate.setValue(date + 1);

            cal.set(year, month - 1, 1, 0, 0, 0);
            int maxDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            npDate.setMaxValue(maxDate + 1);

            String dateStr = year + "년 " + month + "월";
            if (date > 0) {
                dateStr += (" " + date + "일");
            }

            birthForSave = birth;
            btBirth.setText(dateStr);
            btSpecies.setText(breed);
            petCodeForSave = breedCode;
            speciesPosition = Integer.parseInt(breedCode, 10) - 1;
            for (final PetInfoVO.PetInfoObject.PetImageObject imgObj : imgList) {
                imageLoader.get(imgObj.getPetImgThumbUrl(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (response.getBitmap() != null) {
                            switch (imgObj.getNo()) {
                                case 1:
                                    ivPic1.setImageBitmap(response.getBitmap());
                                    String path1 = PicUtil.saveBitmapToJpeg(JoinActivity.this, response.getBitmap(), "pet1");
                                    ivPic1.setTag(path1);
                                    break;
                                case 2:
                                    ivPic2.setImageBitmap(response.getBitmap());
                                    String path2 = PicUtil.saveBitmapToJpeg(JoinActivity.this, response.getBitmap(), "pet2");
                                    ivPic2.setTag(path2);
                                    break;
                                case 3:
                                    ivPic3.setImageBitmap(response.getBitmap());
                                    String path3 = PicUtil.saveBitmapToJpeg(JoinActivity.this, response.getBitmap(), "pet3");
                                    ivPic3.setTag(path3);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            }
        }
    }

    public class JsonSaveApi extends GeneralApi {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder alert = new AlertDialog.Builder(JoinActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            CommonResult commonResult = GsonUtil.fromJson(result, CommonResult.class);
                if (commonResult.getResultCode() != 0) {
                    commonResult.getResultMessage();
                    alert.setMessage("등록에 실패했습니다.");
                } else {
                    alert.setMessage("등록이 완료되었습니다.");
            }

            alert.setCancelable(false);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }

    public class MultipartApi extends GeneralMultipartApi {

        @Override
        protected void onPostExecute(List<String> resultList) {
            super.onPostExecute(resultList);

            AlertDialog.Builder alert = new AlertDialog.Builder(JoinActivity.this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });

            boolean isAllOk = true;
            for (String each : resultList) {
                CommonResult result = GsonUtil.fromJson(each, CommonResult.class);
                if (result.getResultCode() != 0) {
                    result.getResultMessage();
                    isAllOk = false;
                }
            }

            if (isAllOk) {
                alert.setMessage("등록이 완료되었습니다.");
            } else {
                alert.setMessage("등록이 완료되었습니다.\n일부 이미지 등록에 실패했습니다.");
            }

            alert.setCancelable(false);
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
            Button btDone = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            btDone.setTextColor(getResources().getColor(R.color.normalFont));
        }
    }

    private void save() {
        String pic1ImagePath = (String) ivPic1.getTag();
        String pic2ImagePath = (String) ivPic2.getTag();
        String pic3ImagePath = (String) ivPic3.getTag();

        MultipartApi multipartApi = new MultipartApi();
        JsonSaveApi jsonApi = new JsonSaveApi();
        String url = "http://220.73.175.100:8080/MPMS/mob/mobile.service";
        String serviceId;
        if (isEdit) {
            serviceId = "MPMS_01005";
        } else {
            serviceId = "MPMS_01004";
        }

        Map<String, String> header = new HashMap<>();
        header.put("url", url);
        header.put("serviceName", serviceId);

        Map<String, String> body = new HashMap<>();
        body.put("PET_BIRTH", birthForSave);
        body.put("PET_KND_CD", petCodeForSave);

        if (isEdit) {
            body.put("PET_SN", String.valueOf(no));
        }

        Map<String, String> files = new HashMap<>();
        if (pic1ImagePath != null) {
            files.put("1", pic1ImagePath);
        }
        if (pic2ImagePath != null) {
            files.put("2", pic2ImagePath);
        }
        if (pic3ImagePath != null) {
            files.put("3", pic3ImagePath);
        }

        if (isPicChanged) {
            multipartApi.execute(header, body, files);
        } else {
            jsonApi.execute(header, body);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                Bitmap selectedImage = PicUtil.getPicture(getApplicationContext(), imageUri);
                String pathFromUri = PicUtil.getPathFromUri(getApplicationContext(), data.getData());
                switch (requestCode) {
                    case 1:
                        ivPic1.setTag(pathFromUri);
                        ivPic1.setImageBitmap(selectedImage);
                        break;
                    case 2:
                        ivPic2.setTag(pathFromUri);
                        ivPic2.setImageBitmap(selectedImage);
                        break;
                    case 3:
                        ivPic3.setTag(pathFromUri);
                        ivPic3.setImageBitmap(selectedImage);
                        break;
                }
                isPicChanged = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
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
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
