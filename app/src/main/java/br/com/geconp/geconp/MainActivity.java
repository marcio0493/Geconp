package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import br.com.geconp.geconp.backup.RealizarBackUp;
import br.com.geconp.geconp.imgadapter.ImageAdapter;

public class MainActivity extends Activity {

    // Lista de imagens que serão exibidas
    private int[] mThumbIds = {
            R.mipmap.ic_add, R.mipmap.ic_filtrar, R.mipmap.ic_cartao,
            R.mipmap.ic_calendario, R.mipmap.ic_poupanca, R.mipmap.ic_backup
    };
    private String[] texto={"Cadastro/Confirmação", "Extrato", "Cartão", "Calendário", "Poupança","BackUp"};
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, mThumbIds, texto));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (position == 0) {
                    intent = new Intent(getBaseContext(), CadastroActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    intent = new Intent(getBaseContext(), HistoricoActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    intent = new Intent(getBaseContext(), CartaoActivity.class);
                    startActivity(intent);
                } else if(position==3){
                    intent= new Intent(getBaseContext(), CalendarioActivity.class);
                    startActivity(intent);
                }
                else if (position == 4) {
                    intent = new Intent(getBaseContext(), PoupancaActivity.class);
                    startActivity(intent);
                } else if (position == 5) {
                    RealizarBackUp realiza= new RealizarBackUp();
                    boolean verificar= realiza.fazerBackUp();
                    if(verificar==true){
                        AlertDialog.Builder dialog= new AlertDialog.Builder(getBaseContext());
                        dialog.setTitle("Sucesso!");
                        dialog.setIcon(R.drawable.ic_action_accept);
                        dialog.setMessage("BackUp realizado com sucesso!!");
                        dialog.setNeutralButton("Ok", null);
                        dialog.show();
                    }else{
                        AlertDialog.Builder dialog= new AlertDialog.Builder(getBaseContext());
                        dialog.setTitle("Cuidado!");
                        dialog.setIcon(R.drawable.ic_action_warning);
                        dialog.setMessage("Problemas ao realizar BackUp...");
                        dialog.setNeutralButton("Ok", null);
                        dialog.show();
                    }
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
