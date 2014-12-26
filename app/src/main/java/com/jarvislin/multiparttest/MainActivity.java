package com.jarvislin.multiparttest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarActivity {

    private String mURL = "";
    private static final ContentType CHINESE_CHAR = ContentType.create("text/plain", Consts.UTF_8);
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
    }


    public void onSubmit(View v) {

        new UploadTask().execute("超級牛奶", Environment.getExternalStorageDirectory().getPath() + "/image/skyline.jpg");
    }

    class UploadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... input) {

            String milkName = input[0];
            String filePath = input[1];

            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(mURL);

                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                entityBuilder.addTextBody("name", milkName, CHINESE_CHAR);

                File file = new File(filePath);

                if (file != null) {
                    entityBuilder.addBinaryBody("image", file);
                }

                HttpEntity entity = entityBuilder.build();
                post.setEntity(entity);
                HttpResponse response = client.execute(post);
                HttpEntity httpEntity = response.getEntity();

                return EntityUtils.toString(httpEntity);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                Log.d("result", result);
                Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
            }
        }
    }

}
