package com.dabai.colorpicker;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.tapadoo.alerter.Alerter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {

    ColorPickerView cpv;
    LinearLayout line1;

    String colorHex = "未选择颜色";
    String colorHex2 = "未选择颜色";
    int colorInt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cpv = findViewById(R.id.ColorPickerView1);
        line1 = findViewById(R.id.layout1);

        cpv.setFlagView(new CustomFlag(this, R.layout.flagview));

        cpv.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                line1.setBackgroundColor(envelope.getColor());
                colorHex = "#" + envelope.getHexCode().substring(2);
                colorHex2 = "#" + envelope.getHexCode();
                colorInt = envelope.getColor();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.open1:
                checkPermission();
                break;
            case R.id.copy1:
                //复制颜色
                //hex1 hex2 rgb argb 4种

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //    指定下拉列表的显示数据
                final String[] cities = {colorHex, colorHex2, ColorUtils2.hex2Rgb(colorHex), ColorUtils2.hex2aRgb(colorHex2), "0x" + colorHex2.replace("#", "").toLowerCase()};
                //    设置列表选择项
                builder.setItems(cities, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                setClipText(colorHex);
                                break;
                            case 1:
                                setClipText(colorHex2);
                                break;
                            case 2:
                                setClipText(ColorUtils2.hex2Rgb(colorHex));
                                break;
                            case 3:
                                setClipText(ColorUtils2.hex2aRgb(colorHex2));
                                break;
                            case 4:
                                setClipText("0x" + colorHex2.replace("#", "").toLowerCase());
                                break;
                        }

                    }
                });
                builder.show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setClipText(String text) {
        ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mclipData = ClipData.newPlainText("Label", text);
        clipboardManager.setPrimaryClip(mclipData);

        Alerter.create(this).setTitle("提示").setText("复制完成 : " + text).setBackgroundColor(R.color.colorAccent).show();

    }


    //检查权限
    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            //发现没有权限，调用requestPermissions方法向用户申请权限，requestPermissions接收三个参数，第一个是context，第二个是一个String数组，我们把要申请的权限
            //名放在数组中即可，第三个是请求码，只要是唯一值就行
        } else {
            openAlbum();//有权限就打开相册
        }
    }

    public void openAlbum() {
        //通过intent打开相册，使用startactivityForResult方法启动actvity，会返回到onActivityResult方法，所以我们还得复写onActivityResult方法
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 133);
    }
    //弹出窗口向用户申请权限

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 133:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnkitKat(data);//高于4.4版本使用此方法处理图片
                    } else {
                        handleImageBeforeKitKat(data);//低于4.4版本使用此方法处理图片
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnkitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android,providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }

        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }


    //获得图片路径
    public String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);   //内容提供器
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                try {
                    path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));   //获取路径
                } catch (Exception e) {

                }
            }
        }
        cursor.close();
        return path;
    }

    //展示图片
    private void displayImage(String picturePath) {
        if (picturePath != null) {

            Bitmap bitmap = getLoacalBitmap(picturePath); //从本地取图片(在cdcard中获取)
            Drawable drawable = new BitmapDrawable(bitmap);

            cpv.setPaletteDrawable(drawable);


        } else {
            Toast.makeText(this, "获取图片失败,请换一个图片选择器试试!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
