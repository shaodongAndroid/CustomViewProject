package com.custom.view.project.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.custom.view.project.R;
import com.custom.view.project.webview.WebViewActivity;

public class SlindeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    DrawerLayout drawer ;

    private TextView textView ;

    private EditText etIntput ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slinde);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        
        initView();
    }

    private void initView() {
        textView = (TextView) findViewById(R.id.textView);
        etIntput = (EditText) findViewById(R.id.et_text);
        findViewById(R.id.btn_sure).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String text = etIntput.getText().toString().trim() ;
        if(TextUtils.isEmpty(text)){
            return ;
        }
        textView.setText(text);
        interceptHyperLink(textView);
    }

    private void interceptHyperLink(TextView tv) {
//        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        Log.d("info", "---------text = "+text.toString());
        if (text instanceof Spannable) {
            int end = text.length();
            Log.d("info", "---------end = "+end);
            Spannable spannable = (Spannable) tv.getText();
            URLSpan[] urlSpans = spannable.getSpans(0, end, URLSpan.class);
            if (urlSpans.length == 0) {
                return;
            }

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
//             循环遍历并拦截 所有http://开头的链接
            for (URLSpan uri : urlSpans) {
                String url = uri.getURL();
                Log.d("info", "interceptHyperLink: url = "+url);
                Log.d("info", "interceptHyperLink: url end = "+url.length());
                Log.d("info", "-------spannable.getSpanStart(uri) = "+spannable.getSpanStart(uri));
                Log.d("info", "-------spannable.getSpanEnd(uri) = "+spannable.getSpanEnd(uri));
                if(url.indexOf("http://") == 0){
                    CustomUrlSpan customUrlSpan = new CustomUrlSpan(this,url);
                    spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.RED), spannable.getSpanStart(uri),
                            spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    spannableStringBuilder.setSpan(customUrlSpan, spannable.getSpanStart(uri),
                            spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
//                spannableStringBuilder.setSpan(customUrlSpan, 0,
//                        url.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            tv.setText(spannableStringBuilder);
        }
    }
    public class CustomUrlSpan extends ClickableSpan {

        private Context context;
        private String url;
        public CustomUrlSpan(Context context,String url){
            this.context = context;
            this.url = url;
        }

        @Override
        public void onClick(View widget) {
            // 在这里可以做任何自己想要的处理
            Log.d("info", "onClick -------------load WebViewActivity");
            Intent intent = new Intent(context,WebViewActivity.class);
            context.startActivity(intent);
        }
    }





















    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void openMenu(View view){
        Log.d("info","---------------------openMenu");
        drawer.openDrawer(GravityCompat.START);
//        navigationView.startLayoutAnimation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.slinde, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
