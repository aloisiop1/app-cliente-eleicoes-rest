package br.com.etecmam.eleicoesrest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.etecmam.eleicoes_webservice.servicos.json.CandidatoJSON;
import br.com.etecmam.eleicoes_webservice.servicos.json.ErroJSON;
import br.com.etecmam.eleicoesrest.api.EleicoesAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PrincipalActivity extends AppCompatActivity {

    Retrofit retrofit;
    EleicoesAPI api;

    EditText numero;
    Button atualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        atualizar = findViewById(R.id.botaoAtualizar);
        numero = findViewById(R.id.numeroCand);


        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.130:2018/eleicoes/")
                .addConverterFactory( GsonConverterFactory.create() )
                .build();

        api = retrofit.create(EleicoesAPI.class);

        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                int numeroDoCandidato = Integer.valueOf( numero.getText().toString() );

                api.getCandidato(numeroDoCandidato).enqueue(new Callback<CandidatoJSON>() {
                    @Override
                    public void onResponse(Call<CandidatoJSON> call, Response<CandidatoJSON> response) {

                        if( response.code() == 200 ){

                            CandidatoJSON candidato = response.body();

                            Toast.makeText(PrincipalActivity.this,
                                    response.code() + " " + candidato.gerarJSON() ,
                                    Toast.LENGTH_LONG).show();

                        }else if( response.code() == 404 ){

                            Toast.makeText(PrincipalActivity.this, response.toString() , Toast.LENGTH_LONG).show();

                        }

                    }

                    @Override
                    public void onFailure(Call<CandidatoJSON> call, Throwable t) {
                        Toast.makeText(PrincipalActivity.this, "FALHA NO SERVIÇO " + t.toString() , Toast.LENGTH_LONG).show();
                    }
                });

//                CandidatoJSON candidato = new CandidatoJSON(1,"candidato 1 ","PARTIDO 1",100);
//                Toast.makeText(PrincipalActivity.this,candidato.gerarJSON() , Toast.LENGTH_LONG).show();


            }
        });

    }
}
