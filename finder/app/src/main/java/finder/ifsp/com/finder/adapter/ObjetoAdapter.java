package finder.ifsp.com.finder.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import finder.ifsp.com.finder.R;

import finder.ifsp.com.finder.domain.Objeto;
import finder.ifsp.com.finder.presenter.ListarObjetosPresenter;

public class ObjetoAdapter extends BaseAdapter {

    private final Context context;
    private final List<Objeto> objetos;

    public ObjetoAdapter(Context context, List objetos) {
        this.context = context;
        this.objetos = objetos;
    }

    @Override
    public int getCount() {
        return objetos != null ? objetos.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return objetos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return objetos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_objeto_adapter, parent, false);

        TextView tDescricao = (TextView) view.findViewById(R.id.tDescricao);
        TextView tTitulo = (TextView) view.findViewById(R.id.tTitulo);
        TextView tContato = (TextView) view.findViewById(R.id.tContato);
        ImageView ivFoto = (ImageView) view.findViewById(R.id.ivFoto);

        Objeto objeto = objetos.get(position);
        tDescricao.setText(objeto.getDescricao());
        tTitulo.setText(objeto.getTitulo());
        tContato.setText(objeto.getContato());

        baixarImagem(objeto, ivFoto);

        return view;
    }

    private void baixarImagem(final Objeto objeto, final ImageView imageView) {

        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                objeto.setFoto(bitmap);
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        imageView.setTag(target);

        Picasso.with(context)
                .load(ListarObjetosPresenter.baseURL + "images/" + objeto.getId() + ".png")
                .into(target);
    }
}
