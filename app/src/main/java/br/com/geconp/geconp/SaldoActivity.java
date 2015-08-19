package br.com.geconp.geconp;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Receita;

public class SaldoActivity extends Activity {
    private List<Float> valoresReceita= new ArrayList<Float>();
    private List<Float> valoresDespesa= new ArrayList<Float>();
    private float somaReceita, somaDespesa, valorFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saldo);

        DatabaseManager manager= new DatabaseManager(this);
        SQLiteDatabase db = manager.getReadableDatabase();

        Cursor cursor = db.query("lancarReceita",
                null,
                null,//" nome = ? ", //selection
                null,
                null,
                null, "data");
        Cursor cursorDespesa= db.query("lancarDespesa",
                null,
                null,//" nome = ? ", //selection
                null,
                null,
                null, "data");

        if(cursor.moveToFirst()){
            do{
                String txtValor= cursor.getString(cursor.getColumnIndex("valor"));
                float valor= Float.parseFloat(txtValor);
                valoresReceita.add(valor);
            }while(cursor.moveToNext());
            cursor.close();
          ;
            for(Float val: valoresReceita){
                somaReceita +=val;
                Log.i("SOMA", "Soma Receita= "+ somaReceita);
            }

        }

        if(cursorDespesa.moveToFirst()){
            do{
                String txtValor= cursorDespesa.getString(cursorDespesa.getColumnIndex("valor"));
                float valor= Float.parseFloat(txtValor);
                valoresDespesa.add(valor);
            }while(cursorDespesa.moveToNext());
            cursorDespesa.close();
            ;
            for(Float valor: valoresDespesa){
                somaDespesa +=valor;
                Log.i("SOMA", "Soma Despesa= "+ somaDespesa);
            }
        }

        if(somaReceita==0.0){
            String valorDespesa= String.valueOf(somaDespesa);
            TextView despesas= (TextView)findViewById(R.id.textViewSaldoDespesa);
            despesas.setText(valorDespesa);
            TextView saldo= (TextView)findViewById(R.id.textViewSaldo);
            saldo.setText("-" + valorDespesa);
            saldo.setTextColor(Color.RED);
        }else if(somaDespesa==0.0){
            String valorReceita= String.valueOf(somaReceita);
            TextView receitas= (TextView)findViewById(R.id.textViewSaldoReceita);
            receitas.setText(valorReceita);
            TextView saldo= (TextView)findViewById(R.id.textViewSaldo);
            saldo.setText("+"+ valorReceita);
            saldo.setTextColor(Color.BLUE);
        }else {
            valorFinal = somaReceita - somaDespesa;

            if(valorFinal <0 ){
                Log.i("SUB", "- " + valorFinal);
                String valorReceita = String.valueOf(somaReceita);
                String valorDespesa = String.valueOf(somaDespesa);
                String saldoFinal = String.valueOf(valorFinal);

                TextView receitas = (TextView) findViewById(R.id.textViewSaldoReceita);
                TextView despesas = (TextView) findViewById(R.id.textViewSaldoDespesa);
                TextView saldo = (TextView) findViewById(R.id.textViewSaldo);
                receitas.setText(valorReceita);
                despesas.setText(valorDespesa);
                saldo.setText(saldoFinal);
                saldo.setTextColor(Color.RED);

            }else if(valorFinal > 0){
                Log.i("SOM", "+ " + valorFinal);
                String valorReceita = String.valueOf(somaReceita);
                String valorDespesa = String.valueOf(somaDespesa);
                String saldoFinal = String.valueOf(valorFinal);

                TextView receitas = (TextView) findViewById(R.id.textViewSaldoReceita);
                TextView despesas = (TextView) findViewById(R.id.textViewSaldoDespesa);
                TextView saldo = (TextView) findViewById(R.id.textViewSaldo);
                receitas.setText(valorReceita);
                despesas.setText(valorDespesa);
                saldo.setText(saldoFinal);
                saldo.setTextColor(Color.BLUE);
            }else{
                String valorReceita = String.valueOf(somaReceita);
                String valorDespesa = String.valueOf(somaDespesa);
                String saldoFinal = String.valueOf(valorFinal);

                TextView receitas = (TextView) findViewById(R.id.textViewSaldoReceita);
                TextView despesas = (TextView) findViewById(R.id.textViewSaldoDespesa);
                TextView saldo = (TextView) findViewById(R.id.textViewSaldo);
                receitas.setText(valorReceita);
                despesas.setText(valorDespesa);
                saldo.setText(saldoFinal);
                saldo.setTextColor(Color.BLACK);
            }
        }
    }
}
