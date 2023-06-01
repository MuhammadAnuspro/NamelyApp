package com.example.namelyapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

     WebView myWeb;
    ProgressBar pbr;
    android.app.ProgressDialog ProgressDialog;
    RelativeLayout relativeLayout;
    Button btnConnection;
    SwipeRefreshLayout SwipeRefresh;



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window =getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAGS_CHANGED);

        setContentView(R.layout.activity_main);


        myWeb=(WebView)findViewById(R.id.namely);

        pbr= (ProgressBar)findViewById(R.id.progressbar);

        ProgressDialog = new ProgressDialog(this);
        ProgressDialog.setMessage("Loading please Wait");
        btnConnection =(Button)findViewById(R.id.btn);
        relativeLayout =(RelativeLayout)findViewById(R.id.relativelayout);
        SwipeRefresh =(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);

        SwipeRefresh.setColorSchemeColors(Color.YELLOW,Color.BLACK,Color.MAGENTA);


        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myWeb.reload();
            }
        });



        if(savedInstanceState !=null){
            myWeb.restoreState(savedInstanceState);
        }

        else
            {
                myWeb.getSettings().setJavaScriptEnabled(true);
                myWeb.getSettings().setLoadWithOverviewMode(true);
                myWeb.getSettings().setUseWideViewPort(true);
                myWeb.getSettings().setDomStorageEnabled(true);
                myWeb.getSettings().getLoadsImagesAutomatically();
                checkConnection();

        }


        myWeb.setWebViewClient(new WebViewClient(){


           //SwipeRefresh  function
           @Override
            public void onPageFinished(WebView view, String url) {
                SwipeRefresh.setRefreshing(false);
                super.onPageFinished(view, url);
            }

            //SwipeRefresh  methode
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        myWeb.setWebChromeClient(new WebChromeClient(){

            public void onProgressChanged(WebView view, int newProgress) {

                super.onProgressChanged(view, newProgress);

                pbr.setVisibility(View.VISIBLE);
                pbr.setProgress(newProgress);
                setTitle("Loading...");
                ProgressDialog.show();



                if (newProgress == 100){

                    pbr.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                    ProgressDialog.dismiss();
                }

                super.onProgressChanged(view, newProgress);


            }
        });

        btnConnection.setOnClickListener(v -> checkConnection());

        }

    public void onBackPressed(){
        if(myWeb.canGoBack()){
            myWeb.goBack();
        }
                
           else
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to Exit?")
            .setNegativeButton("No",null)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener(){


                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            finishAffinity();
                        }
                    }

            ).show();
        }
    }

    public void checkConnection(){


        ConnectivityManager  ConnectivityManager = (ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi;
        wifi = ConnectivityManager.getNetworkInfo(android.net.ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetwork;
        mobileNetwork = ConnectivityManager.getNetworkInfo(android.net.ConnectivityManager.TYPE_MOBILE);


        String webUrl = "https://namely.pk/";
        if (wifi.isConnected()){
            myWeb.loadUrl(webUrl);
            myWeb.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

        }

        else if (mobileNetwork.isConnected()){
            myWeb.loadUrl(webUrl);
            myWeb.setVisibility(View.VISIBLE);
            relativeLayout.setVisibility(View.GONE);

        }

        else{

            myWeb.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_previous:
                onBackPressed();
                break;

            case R.id.nav_next:

                if (myWeb.canGoForward()){
                    myWeb.goForward();
                }
                break;

            case R.id.nav_reload:
                checkConnection();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        myWeb.saveState(outState);
    }
}
