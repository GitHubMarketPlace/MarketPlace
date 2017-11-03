package marketplace.tcc.usjt.br.marketplace.activity.retrofit.service;

import marketplace.tcc.usjt.br.marketplace.activity.retrofit.service.service.RecomendacaoService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Rodrigo on 29/10/2017.
 */

public class RetrofitInicializadorActivity {

    private final Retrofit retrofit;

    public RetrofitInicializadorActivity(){
        retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.103:8184/marketplace/").addConverterFactory(GsonConverterFactory.create()).build();
    }

    public RecomendacaoService getRecomendacaoService() {
        return retrofit.create(RecomendacaoService.class);
    }
}
