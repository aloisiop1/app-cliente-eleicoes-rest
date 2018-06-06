package br.com.etecmam.eleicoesrest;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.etecmam.eleicoes_webservice.servicos.json.CandidatoJSON;
import br.com.etecmam.eleicoes_webservice.servicos.json.ListaDeCandidatosJSON;
import br.com.etecmam.eleicoesrest.api.EleicoesAPI;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListaActivity extends AppCompatActivity {
    EleicoesAPI api;
    ListView lista;
    List<CandidatoJSON> candidatos;
    private Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        setTitle("ELEIÇÕES 2018");
        lista = findViewById(R.id.lista);



        findViewById(R.id.botaoAtualizarLista).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                atualizarLista();
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CandidatoJSON candidato = (CandidatoJSON) lista.getItemAtPosition( position);
                Intent i = new Intent(ListaActivity.this, DadosCandidatoActivity.class);

                i.putExtra("foto", candidato.getFoto() );
                startActivity(i);

            }
        });




        iniciarConexaoComWebService();
    }

    private void iniciarConexaoComWebService() {

        final OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.130:2018/eleicoes/")
                .addConverterFactory( GsonConverterFactory.create() )
                .client(okHttpClient)
                .build();

        api = retrofit.create(EleicoesAPI.class);

        atualizarLista();

    }

    private void atualizarLista() {

        api.getCandidatos().enqueue(new Callback<ListaDeCandidatosJSON>() {
            @Override
            public void onResponse(Call<ListaDeCandidatosJSON> call, Response<ListaDeCandidatosJSON> response) {

                ListaDeCandidatosJSON listaJSON = response.body();

                if( response.code() == 200 ){

                    candidatos = listaJSON.getCandidatos();
                    Toast.makeText(ListaActivity.this, "LISTA ATUALIZADA" , Toast.LENGTH_SHORT).show();

                }else if( response.code() == 404 ){

                    candidatos = new ArrayList<>();
                    Toast.makeText(ListaActivity.this, "SERVIÇO NÃO DISPONÍVEL" , Toast.LENGTH_LONG).show();
                }

                CandidatoAdapter adapter = new CandidatoAdapter(ListaActivity.this, api, candidatos );
                lista.setAdapter( adapter );

            }

            @Override
            public void onFailure(Call<ListaDeCandidatosJSON> call, Throwable t) {

                candidatos = new ArrayList<>();
                CandidatoAdapter adapter = new CandidatoAdapter(ListaActivity.this, api, candidatos );
                lista.setAdapter( adapter );

                Toast.makeText(ListaActivity.this,
                                       t.getMessage(),
                                       Toast.LENGTH_SHORT).show();
            }

        });

    }
}
