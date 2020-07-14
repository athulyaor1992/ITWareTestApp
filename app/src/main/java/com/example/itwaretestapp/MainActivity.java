package com.example.itwaretestapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    ArrayList<DataModel> datasArray = new ArrayList<DataModel>();
    RegistrAdapter adapter;
    ListView main_list;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_list= (ListView) findViewById(R.id.main_list);
        submit= (Button) findViewById(R.id.submit);

       new DownloadFilesTask().execute();

    }

    public class DownloadFilesTask extends AsyncTask< Void, Void, String >
    {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            this.dialog.setMessage("Loading...");
            this.dialog.show();

        }
        @Override
        protected String doInBackground(Void...param)
        {
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall("https://5f0b3a919d1e150016b372f1.mockapi.io/api/data", ServiceHandler.GET);
            Log.d("res1", jsonStr);
            return jsonStr;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String response)
        {
            super.onPostExecute(response);
            Log.d("res2", response);
            dialog.dismiss();
            if (response != null)
            {
                try {
                    JSONObject jObjct = new JSONObject(response);

                    String status = jObjct.get("msg").toString();

                    if (status.equals("Success")) {

                        JSONArray arr = jObjct.getJSONArray("surveyData");
                        for (int i = 0; i < arr.length(); i++) {

                            try{
                                JSONObject c = arr.getJSONObject(i);

                                DataModel dataModel = new DataModel();

                                dataModel.setControlType(c.getString("controlType"));
                                dataModel.setQuestion(c.getString("question"));
                                dataModel.setRequiredValidator(c.getString("requiredValidator"));
                                dataModel.setRequiredValidatorMessage(c.getString("requiredValidatorMessage"));

                                if(c.getString("controlType").equals("Textbox")){

                                    dataModel.setRegularExpression(c.getString("regularExpression"));
                                }else{
                                    dataModel.setRegularExpression("Valid");
                                }


                                Log.e("the ","ARAY"+dataModel.getQuestion());
                                datasArray.add(dataModel);


                                Log.e("the ","ARAY"+datasArray.size());


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        adapter = new RegistrAdapter(MainActivity.this,datasArray,arr);
                        main_list.setAdapter(adapter);

                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                adapter.checkValidation(view);

                            }
                        });

                    }
                }
                catch (Exception e)
                {e.printStackTrace();}
            }
        }
    }

}