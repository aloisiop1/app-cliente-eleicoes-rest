package br.com.etecmam.eleicoesrest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import br.com.etecmam.eleicoes_webservice.servicos.json.CandidatoJSON;
import br.com.etecmam.eleicoesrest.api.EleicoesAPI;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ImageFinder extends AsyncTask<Void, Void, Void> {

    private EleicoesAPI api;
    private String foto;

    private Drawable drawable;

    public ImageFinder(EleicoesAPI api, String foto) {
        this.api = api;
        this.foto = foto;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        byte[] dados = null;

        Call<ResponseBody> resposta = api.getFoto(foto);

        try {
            Response<ResponseBody> r = resposta.execute();

            if(r.isSuccessful() ) {
                dados = r.body().bytes();

                ByteArrayInputStream is = new ByteArrayInputStream(dados);
                drawable = Drawable.createFromStream(is, "foto");

                Log.d("drw","passou");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
