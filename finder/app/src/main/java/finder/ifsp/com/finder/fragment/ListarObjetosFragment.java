package finder.ifsp.com.finder.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import finder.ifsp.com.finder.R;
import finder.ifsp.com.finder.presenter.ListarObjetosPresenter;

public class ListarObjetosFragment extends Fragment {
    private ListView mList;

    public ListarObjetosFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listar_objetos, container, false);

        mList = (ListView) view.findViewById(R.id.listView);
        ListarObjetosPresenter listarObjetosPresenter = new ListarObjetosPresenter();

        listarObjetosPresenter.listarObjetos(getContext(), mList);

        return view;
    }
}
