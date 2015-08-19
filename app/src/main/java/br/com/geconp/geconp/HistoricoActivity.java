package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Despesa;
import br.com.geconp.geconp.model.Receita;


public class HistoricoActivity extends Activity {
    private Button btnExt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        btnExt= (Button)findViewById(R.id.buttonGerarExtr);
        btnExt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviar();
            }
        });
    }

    public void enviar(){
        EditText txt= (EditText)findViewById(R.id.editTextCatExt);

        if(txt.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            TextView texto= (TextView)findViewById(R.id.textViewExtr);
            texto.setText("");
            String s= txt.getText().toString();
            DatabaseManager manager= new DatabaseManager(this);
            List<String> procurar= manager.listaCat(s);

            StringBuilder sb= new StringBuilder();
            int i=1;
            for(String aux: procurar){
                sb.append(aux).append("  ");
                if(i%3==0) {
                    sb.append("\n");
                    sb.append("------------------------------------------------------------");
                    sb.append("\n");
                }
                i++;
            }
            texto.setText(sb.toString());
            texto.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
