package marketplace.tcc.usjt.br.marketplace.activity.retrofit.service.service;

import java.util.List;

import marketplace.tcc.usjt.br.marketplace.activity.triggerRecommendation.DadosRecomendacaoActivity;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by Rodrigo on 29/10/2017.
 */

public interface RecomendacaoService {

    @POST("saveDadosRecomendacao")
    Call <Void>armazenaDadosRecomendacao(@Body DadosRecomendacaoActivity dados);

    @GET("listaRecomendada/{userId}")
    Call <List<Integer>> getListaRecomendacao(@Path("userId") int userId);
}
