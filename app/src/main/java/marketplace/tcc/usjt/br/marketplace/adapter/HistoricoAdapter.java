package marketplace.tcc.usjt.br.marketplace.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import marketplace.tcc.usjt.br.marketplace.R;
import marketplace.tcc.usjt.br.marketplace.model.Historico;

public class HistoricoAdapter extends BaseAdapter{

        private final ArrayList<Historico> historicos;
        private final Activity context;

        public HistoricoAdapter(ArrayList<Historico> historicos , Activity context) {
            this.context = context;
            this.historicos = historicos;
        }

        @Override
        public int getCount() {
            return historicos.size();
        }

        @Override
        public Object getItem(int position) {
            return historicos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View recycledView, ViewGroup parent) {
            View view = recycledView;
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_lista_historico, parent, false);

            Historico historico = historicos.get(position);

            TextView dataHistorico = (TextView)view.findViewById(R.id.item_lista_historico_data);
            TextView horaHistorico = (TextView)view.findViewById(R.id.item_lista_historico_hora);

            dataHistorico.setText("Data: " + historico.getData());
            horaHistorico.setText("Horário da compra: " + historico.getHora());

            return view;
        }


}
