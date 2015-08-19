package br.com.geconp.geconp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import br.com.geconp.geconp.imgadapter.ImagePoupancaAdapter;


public class PoupancaActivity extends Activity {
    private int[] images={
            R.mipmap.ic_poupanca,
            R.mipmap.ic_poupanca,
            R.mipmap.ic_poupanca
    };
    private String[] texto={
            "Cadastrar Conta",
            "Histórico",
            "Listar Contas"
    };
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poupanca);

        GridView gridview = (GridView) findViewById(R.id.gridviewPoupanca);
        gridview.setAdapter(new ImagePoupancaAdapter(this, images, texto));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(position==0){
                    startActivity(new Intent(PoupancaActivity.this, AdicionarPoupancaActivity.class));
                }else if(position==1){
                    startActivity(new Intent(PoupancaActivity.this, HistoricoPoupancaActivity.class));
                }else if(position==2){
                    startActivity(new Intent(PoupancaActivity.this, ListarPoupancaActivity.class));
                }
            }
        });
    }
}
