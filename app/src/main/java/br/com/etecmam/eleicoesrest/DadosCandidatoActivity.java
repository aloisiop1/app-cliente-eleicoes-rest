package br.com.etecmam.eleicoesrest;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import br.com.etecmam.eleicoesrest.api.EleicoesAPI;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DadosCandidatoActivity extends AppCompatActivity {

    ImageView imv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_candidato);

        imv = findViewById(R.id.fotoCandidato);
        String foto = getIntent().getStringExtra("foto");


        final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.130:2018/eleicoes/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        EleicoesAPI api = retrofit.create(EleicoesAPI.class);

        api.getFoto(foto).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){


                    byte [] dados = new byte[0];

                    try {
                        dados = response.body().bytes();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayInputStream is = new ByteArrayInputStream(dados);
                    Drawable drawable = Drawable.createFromStream(is, "foto");

                    imv.setImageDrawable(drawable);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }
}
