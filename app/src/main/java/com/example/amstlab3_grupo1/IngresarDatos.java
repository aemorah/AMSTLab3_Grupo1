package com.example.amstlab3_grupo1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IngresarDatos extends AppCompatActivity {

    Button btn_guardar;
    Button btn_regresar;
    EditText ing_temp;
    EditText ing_hum;
    EditText ing_peso;
    private RequestQueue mQueue;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_datos);

        mQueue = Volley.newRequestQueue(this);
        Intent login = getIntent();
        this.token = (String)login.getExtras().get("token");

        btn_guardar = (Button) findViewById(R.id.button_guardar);
        btn_regresar = (Button) findViewById(R.id.button_regresar);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ing_temp = (EditText) findViewById(R.id.ingresoTemp);
                ing_hum = (EditText) findViewById(R.id.ingresoHum);
                ing_peso = (EditText) findViewById(R.id.ingresoPeso);

                String str_temp = ing_temp.getText().toString().trim();
                String str_hum = ing_hum.getText().toString().trim();
                String str_peso = ing_peso.getText().toString().trim();

                enviarData(str_temp,str_hum,str_peso);
            }
        });

        btn_regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent lecturaSensores = new Intent(getBaseContext(), RedSensores.class);
                startActivity(lecturaSensores);
                finish();

            }
        });

    }

    private void enviarData(String temperatura, String humedad, String peso){
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("temperatura", temperatura);
        data.put("humedad", humedad);
        data.put("peso",peso);
        JSONObject parametros = new JSONObject(data);
        System.out.println(parametros);
        String url = "https://amst-labx.herokuapp.com/api/sensores";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parametros, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Aqui viene el response response");
                System.out.println(response);
                ing_temp.getText().clear();
                ing_peso.getText().clear();
                ing_hum.getText().clear();
                Toast toast = Toast.makeText(getApplicationContext(), "Guardado", Toast.LENGTH_LONG);
                toast.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }}){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "JWT " + token);
                System.out.println(token);
                return params;
            }
        };
        mQueue.add(request);
    }
}