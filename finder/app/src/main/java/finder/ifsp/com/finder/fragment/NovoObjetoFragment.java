package finder.ifsp.com.finder.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import finder.ifsp.com.finder.R;
import finder.ifsp.com.finder.activity.MainActivity;
import finder.ifsp.com.finder.domain.Objeto;
import finder.ifsp.com.finder.presenter.ListarObjetosPresenter;
import finder.ifsp.com.finder.service.ObjetoService;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NovoObjetoFragment extends Fragment {    private ImageView mImageView;

    private Context context;


    public NovoObjetoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_novo_objeto, container, false);

        Button btGravarProfessor = (Button) view.findViewById(R.id.btGravarObjeto);
        btGravarProfessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravarObjeto();
            }
        });

        Button btTirarFoto = (Button) view.findViewById(R.id.btTirarFoto);
        btTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        mImageView = (ImageView) view.findViewById(R.id.ivFoto);
        context = container.getContext();
        context = container.getContext();

        return view;
    }

    public void gravarObjeto() {
        String baseURL = "http://192.168.56.1:8090/";
        //String baseURL = "http://192.168.0.100:8090/";
        Boolean vOk = true;

        EditText eDescricao = (EditText) getView().findViewById(R.id.eDescricao);
        EditText eTitulo = (EditText) getView().findViewById(R.id.eTitulo);
        EditText eContato = (EditText) getView().findViewById(R.id.eContato);

        if (eTitulo.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(context, "Digite o titulo do Objeto encontrado.", Toast.LENGTH_SHORT);
            toast.show();
        } else if (eDescricao.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(context, "Digite a descrição do Objeto encontrado", Toast.LENGTH_SHORT);
            toast.show();
        } else if (eContato.getText().toString().isEmpty()) {
            Toast toast = Toast.makeText(context, "Digite o seu contato", Toast.LENGTH_SHORT);
            toast.show();
        }
        else
        {

            Objeto objeto = new Objeto();
            objeto.setTitulo(eTitulo.getText().toString());
            objeto.setDescricao(eDescricao.getText().toString());
            objeto.setContato(eContato.getText().toString());

            try {
                File file = new File(mCurrentPhotoPath);
                RequestBody fbody = RequestBody.create(MediaType.parse("image/*"), file);

                eTitulo.setText("");
                eDescricao.setText("");
                eContato.setText("");

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(logging);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                ObjetoService service = retrofit.create(ObjetoService.class);

                Call<Objeto> call = service.criarObjeto(fbody, objeto);


                call.enqueue(new Callback<Objeto>() {
                    @Override
                    public void onResponse(Call<Objeto> call, Response<Objeto> response) {

                    }

                    @Override
                    public void onFailure(Call<Objeto> call, Throwable t) {

                    }
                });

                Toast toast = Toast.makeText(context, "Objeto cadastrado com sucesso!", Toast.LENGTH_SHORT);
                toast.show();

            } catch (Exception e) {
                Toast toast = Toast.makeText(context, "Tire a foto do objeto", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    //Foto
    String mCurrentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    int MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.RESULT_OK) {

            File imgFile = new  File(mCurrentPhotoPath);

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                mImageView.setImageBitmap(myBitmap);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        if((ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))

        {
            ActivityCompat.requestPermissions
                    (getActivity(), new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },MY_PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE);
        }


        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

}
