package finder.ifsp.com.finder.service;

/**
 * Created by Justh Franklin on 20/11/2016.
 */

import java.util.List;

import finder.ifsp.com.finder.domain.Objeto;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ObjetoService {
    @GET("objeto/list")
    Call<List<Objeto>> listarObjetos();

    @Multipart
    @POST("objeto/new")
    Call<Objeto> criarObjeto(@Part("foto") RequestBody image,
                                   @Part("objeto") Objeto objeto);

}
