package finder.ifsp.com.finder.presenter;

/**
 * Created by Justh Franklin on 20/11/2016.
 */


import android.content.Context;
import android.widget.ListView;

import java.util.List;

import finder.ifsp.com.finder.adapter.ObjetoAdapter;
import finder.ifsp.com.finder.domain.Objeto;
import finder.ifsp.com.finder.service.ObjetoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListarObjetosPresenter {
    public static String baseURL = "http://192.168.56.1:8090/";
    //public static String baseURL = "http://192.168.0.100:8090/";

    public void listarObjetos(final Context context, final ListView listView){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ObjetoService service = retrofit.create(ObjetoService.class);
        Call<List<Objeto>> call = service.listarObjetos();

        call.enqueue(new Callback<List<Objeto>>() {
            @Override
            public void onResponse(Call<List<Objeto>> call, Response<List<Objeto>> response) {
                List<Objeto> list = response.body();

                listView.setAdapter(new ObjetoAdapter(context, list));
            }

            @Override
            public void onFailure(Call<List<Objeto>> call, Throwable t) {

            }
        });
    }

}
