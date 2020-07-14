package com.example.itwaretestapp;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RegistrAdapter extends BaseAdapter  {
    private LayoutInflater mInflater;
    private ArrayList<DataModel> dataArray;
    private ArrayList<DataModel> editdataArray=new ArrayList<DataModel>();
    private ArrayList<EditText>  editTextArrayList = new ArrayList<>();
    private ArrayList<Spinner>  spinnerList = new ArrayList<>();
    private ArrayList<DataModel>  spinnerData = new ArrayList<>();
    Context mContext;
    JSONArray jsonArray;
    //EditText et;

    public RegistrAdapter(Context mContext, ArrayList<DataModel> dataArray, JSONArray jsonArray) {
        this.dataArray=dataArray;
        this.mContext=mContext;
        this.jsonArray=jsonArray;
        mInflater = (LayoutInflater)mContext. getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return dataArray.size();
    }

    public Object getItem(int position) {
        return dataArray.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final View result;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(
                    R.layout.register_item, null);


            holder.question = (TextView) convertView.findViewById(R.id.question);
            holder.mandatory = (TextView) convertView.findViewById(R.id.mandatory);
            holder.controlType = (LinearLayout) convertView.findViewById(R.id.controlType);

            result=convertView;

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result=convertView;
        }
        convertView.setId(position);
        final DataModel d = dataArray.get(position);
        holder.question.setText(d.getQuestion());
        if(d.getRequiredValidator().equals("Y")){
            holder.mandatory.setText("*");
        }else{
            holder.mandatory.setText("");
        }

        if(d.getControlType().equals("Textbox")){

           EditText etObj = new EditText(mContext);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            etObj.setLayoutParams(p);
            etObj.setBackgroundResource(R.drawable.et_style);
            editTextArrayList.add(etObj);
            editdataArray.add(d);
            holder.controlType.addView(etObj);
        } else if(d.getControlType().equals("Dropdown")){
            JSONArray answrArray=null;
            ArrayList<String> spinnerArray=null;
                try{
                    JSONObject c = jsonArray.getJSONObject(position);
                    answrArray = c.getJSONArray("answerMast");
                    spinnerArray = new ArrayList<String>();
                    spinnerArray.add("Tap to select");
                    for(int j=0;j<answrArray.length();j++) {
                        try {
                            JSONObject b = answrArray.getJSONObject(j);
                            String answer = b.getString("answer");
                            spinnerArray.add(answer);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            Spinner spinner = new Spinner(mContext);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
            spinner.setAdapter(spinnerArrayAdapter);
            spinner.setBackgroundResource(R.drawable.et_style);
            spinnerList.add(spinner);
            spinnerData.add(d);
            holder.controlType.addView(spinner);
        }


        return convertView;

    }

    public void checkValidation(final View view) {

                for(int j=0;j<editTextArrayList.size();j++){
                    Log.e("regularExpression",editdataArray.get(j).regularExpression);

                    if(editTextArrayList.get(j).getText().toString().length() == 0 ||
                            editTextArrayList.get(j).getText().toString().isEmpty() ||
                            !editTextArrayList.get(j).getText().toString().matches(editdataArray.get(j).regularExpression)){
                        editTextArrayList.get(j).setError(editdataArray.get(j).getRequiredValidatorMessage() );
                        editTextArrayList.get(j).requestFocus();
                    }
                }
            Log.e("editTextArrayList",spinnerList.size()+"");
            Handler handler1 = new Handler();
                for(int k=0;k<spinnerList.size();k++){
                    final int finalK = k;
                    handler1.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            if( spinnerList.get(finalK).getSelectedItem().equals("Tap to select")){
                                Log.e("dataArray",spinnerData.get(finalK).getRequiredValidatorMessage()+"");

                                Snackbar.make(view,""+spinnerData.get(finalK).getRequiredValidatorMessage(),Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }, 1000 * k);
                }

    }


    class ViewHolder {
        TextView question,mandatory;
        LinearLayout controlType;
    }


}

