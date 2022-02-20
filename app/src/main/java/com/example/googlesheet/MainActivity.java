package com.example.googlesheet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn;
    public String[] status = new String[5];
    public int[] id = new int[5];
    public  String[] name = new String[5];
    public  int[] marks = new int[5];
    public int j;
    String url ="https://script.google.com/macros/s/AKfycbxuOFVTu-LuwhAnHR4hG75PvQ8CjyNmWpWSqPWHLaHg13qYfrmZmW1FpVPXOZCJ9hk/exec";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("user");
                            ArrayList<Object> listdata = new ArrayList<Object>();
                            for (int i=0;i<array.length();i++){
                                listdata.add(array.get(i));
                            }
                            System.out.println(listdata);
                            for(int i=0;i<listdata.size();i++) {
                                Object jsonObject = listdata.get(i);
                                JSONObject jsonObject1 = (JSONObject) jsonObject;
                                int studentmarks = (int) jsonObject1.get("Marks");
                                if (studentmarks < 40) {
                                    status[i] = "Fail";
                                } else {
                                    status[i] = "Pass";
                                }

                                marks[i] = studentmarks;
                                int studentid = (int) jsonObject1.get("ID");

                                id[i] = studentid;
                                String studentname = (String) jsonObject1.get("Name");

                                name[i] = studentname;

                                j=i;

                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                System.out.println("Response is: "+response);

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println(error);
                                    }
                                }){
                                    @Override
                                    public Map<String,String> getParams(){
                                        Map<String,String> params = new HashMap<String,String>();
                                        params.put("action", "addstudent");
                                        params.put("ID",String.valueOf(id[j]));
                                        params.put("Name",name[j]);
                                        params.put("Marks", String.valueOf(marks[j]));
                                        params.put("Status",status[j]);
                                        System.out.println("Success");
                                        return params;
                                    }
                                };
                                int socketTimeout1 = 50000;
                                RetryPolicy retryPolicy1 = new DefaultRetryPolicy(socketTimeout1, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                stringRequest1.setRetryPolicy(retryPolicy1);
                                RequestQueue requestQueue1 = Volley.newRequestQueue(MainActivity.this);
                                requestQueue1.add(stringRequest1);
                                Thread.sleep(500);
                            }
                        }
                        catch (JSONException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(jsonObjectRequest);
            }
        });
    }
}