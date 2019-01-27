package com.chendaixi947.openemailappdemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.open_email_app) {
                //打开邮件客户端
                openEmail();
            } else if (v.getId() == R.id.email_send_one) {
                //发送邮件（方式一）
                sendEmail(MainActivity.this, "周报xxx", "本周工作内容如下：\n\n完成了xxxxx", "cdx.fly@gmail.com");
            } else if (v.getId() == R.id.email_send_two) {
                //发送邮件（方式二）
                sendEmail2(MainActivity.this, "周报xxx", "本周工作内容如下：\n\n完成了xxxxx", "cdx.fly@gmail.com");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.open_email_app).setOnClickListener(onClickListener);
        findViewById(R.id.email_send_one).setOnClickListener(onClickListener);
        findViewById(R.id.email_send_two).setOnClickListener(onClickListener);
    }

    /**
     * 打开邮箱客户端
     */
    private void openEmail() {
        Uri uri = Uri.parse("mailto:" + "");
        List<ResolveInfo> packageInfos = getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_SENDTO, uri), 0);
        List<String> tempPkgNameList = new ArrayList<>();
        List<Intent> emailIntents = new ArrayList<>();
        for (ResolveInfo info : packageInfos) {
            String pkgName = info.activityInfo.packageName;
            if (!tempPkgNameList.contains(pkgName)) {
                tempPkgNameList.add(pkgName);
                Intent intent = getPackageManager().getLaunchIntentForPackage(pkgName);
                emailIntents.add(intent);
            }
        }
        if (!emailIntents.isEmpty()) {
            Intent chooserIntent = Intent.createChooser(emailIntents.remove(0), "选择邮箱");
            if (chooserIntent != null) {
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, emailIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            } else {
                Toast.makeText(MainActivity.this, "没有找到可用的邮件客户端", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "没有找到可用的邮件客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 邮件分享
     *
     * @param context 上下文
     * @param title   邮件主题
     * @param content 邮件内容
     * @param address 邮件地址
     */
    public void sendEmail(Context context, String title, String content, String address) {
        Uri uri = Uri.parse("mailto:" + address);
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(emailIntent, "选择邮箱"));
    }

    /**
     * 邮件分享
     *
     * @param context 上下文
     * @param title   邮件主题
     * @param content 邮件内容
     * @param address 邮件地址
     */
    public void sendEmail2(Context context, String title, String content, String address) {
        // 调用系统发邮件
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // 设置文本格式
        emailIntent.setType("text/plain");
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address);
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(emailIntent, "选择邮箱"));
    }
}
