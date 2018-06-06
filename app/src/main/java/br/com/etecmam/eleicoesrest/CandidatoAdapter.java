package br.com.etecmam.eleicoesrest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import br.com.etecmam.eleicoes_webservice.servicos.json.CandidatoJSON;
import br.com.etecmam.eleicoesrest.api.EleicoesAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatoAdapter extends BaseAdapter {

    private Context contexto;
    private EleicoesAPI api;
    List<CandidatoJSON> candidatos = new ArrayList<>();
    private int totalDeVotos;



    public CandidatoAdapter(Context contexto, EleicoesAPI api, List<CandidatoJSON> candidatos){
        this.contexto = contexto;
        this.api = api;
        this.candidatos = candidatos;

        for( CandidatoJSON c : candidatos){
            totalDeVotos += c.getVotos();
        }

        this.totalDeVotos = totalDeVotos;
    }

    @Override
    public int getCount() {
        return candidatos.size();
    }

    @Override
    public Object getItem(int position) {
        return candidatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return candidatos.get(position).getNumero();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CandidatoJSON candidato = candidatos.get(position);
        LayoutInflater inflater = LayoutInflater.from(contexto);
        View view  = inflater.inflate(R.layout.candidato_item, null);

        final ImageView foto = view.findViewById(R.id.foto);

        TextView numero = view.findViewById(R.id.numeroCandidato);
        TextView nome = view.findViewById(R.id.nome);
        TextView votos = view.findViewById(R.id.totalVotos);
        TextView sigla = view.findViewById(R.id.sigla);
        TextView percentual = view.findViewById(R.id.percentual);

        numero.setText( String.valueOf( candidato.getNumero() ) );
        nome.setText( candidato.getNome() );
        sigla.setText( candidato.getSigla() );
        votos.setText( String.valueOf( candidato.getVotos() ) );

        DecimalFormat df = new DecimalFormat("0.0");

        Double percentualCandidato = (double)( candidato.getVotos() * 100 ) / totalDeVotos;

        if (percentualCandidato.isNaN() ) {
            percentualCandidato = 0.0 ;
        }

        percentual.setText( String.valueOf ( df.format(  percentualCandidato ) + " %") );


        /////////////////////////////////
        api.getFoto(candidato.getFoto() ).enqueue(new Callback<ResponseBody>() {
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

                    foto.setImageDrawable(drawable);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });



        return view;
    }
}
