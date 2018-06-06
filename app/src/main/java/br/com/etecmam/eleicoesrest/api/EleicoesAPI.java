package br.com.etecmam.eleicoesrest.api;

import java.util.List;

import br.com.etecmam.eleicoes_webservice.servicos.json.CandidatoJSON;
import br.com.etecmam.eleicoes_webservice.servicos.json.ListaDeCandidatosJSON;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EleicoesAPI {

        @GET("candidato/{numero-do-candidato}")
        public Call<CandidatoJSON> getCandidato(@Path("numero-do-candidato") int numero);

        @GET("candidato/lista")
        public Call<ListaDeCandidatosJSON> getCandidatos();

        @GET("candidato/imagem/{foto-candidato}")
        public Call<ResponseBody> getFoto(@Path("foto-candidato") String foto);

}
