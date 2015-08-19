package br.com.geconp.geconp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import br.com.geconp.geconp.imgadapter.ImageAddAdapter;


public class CadastroActivity extends Activity {

    private Intent intent;
    private int[] images= {
            R.mipmap.ic_add,
            R.mipmap.ic_add,
            R.mipmap.ic_add,
            R.mipmap.ic_add,
            R.mipmap.ic_add,
            R.mipmap.ic_add
    };
    private String[] texto={
            "Contas a Receber",
            "Contas a Pagar",
            "Adicionar Categoria",
            "Adicionar Conta",
            "Confirmar Recebimento",
            "Confirmar Pagamento"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        GridView gridview = (GridView) findViewById(R.id.gridviewAdd);
        gridview.setAdapter(new ImageAddAdapter(this, images, texto));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if(position==0){
                    intent= new Intent(CadastroActivity.this, TelaConReceberActivity.class);
                    startActivity(intent);
                }else if(position==1){
                    intent= new Intent(CadastroActivity.this, TelaConPagarActivity.class);
                    startActivity(intent);
                }else if(position==2){
                    intent= new Intent(CadastroActivity.this, CategoriaActivity.class);
                    startActivity(intent);
                }else if(position==3){
                    intent= new Intent(CadastroActivity.this, ContaActivity.class);
                    startActivity(intent);
                }else if(position==4){
                    intent= new Intent(CadastroActivity.this, LancarReceitaActivity.class);
                    startActivity(intent);
                }else if(position==5){
                    intent= new Intent(CadastroActivity.this, LancarDespesaActivity.class);
                    startActivity(intent);
                }

            }
        });
    }


}
