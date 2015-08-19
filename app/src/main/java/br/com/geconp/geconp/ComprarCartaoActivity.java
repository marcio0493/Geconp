package br.com.geconp.geconp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

import br.com.geconp.geconp.async.AsyncCompraCartao;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Cartao;


public class ComprarCartaoActivity extends Activity {
    static final int DATE_DIALOG_ID = 0;
    private String data;
    private EditText txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_cartao);

        Intent origem= getIntent();
        final String txtId= origem.getStringExtra("ID2");
        final String txtValor= origem.getStringExtra("VALOR2");
        final String txtDesc= origem.getStringExtra("DESC2");
        final String txtBandeira= origem.getStringExtra("BANDEIRA2");

        Button btnCompra= (Button)findViewById(R.id.buttonComprarCartao);
        btnCompra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comprar(txtId, txtValor, txtDesc, txtBandeira);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtData= (EditText)findViewById(R.id.editTextDataCompra);
        txtData.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showDialog(DATE_DIALOG_ID);
                txtData.clearFocus();
            }
        });
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        Calendar calendar= Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
        }
        return null;

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            data = String.valueOf(dayOfMonth) + "/" + String.valueOf(monthOfYear+1) + "/"
                    + String.valueOf(year);
            txtData.setText(data);

            Log.i("INFO", "DATA = " + data);
        }
    };


    public void comprar(String txtId, String txtValor, String txtDesc, String txtBandeira){
        EditText txtCompra= (EditText)findViewById(R.id.editTextValorComprado);
        EditText txtDescricao= (EditText)findViewById(R.id.editTextDescComprado);
        if(txtCompra.getText().toString().equals("") || txtDescricao.getText().toString().equals("")||
                txtData.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            String aux1= txtCompra.getText().toString();
            String aux2= aux1.replace(",", ".");
            float valorCompra= Float.parseFloat(aux2);
            Log.i("DESC", " "+ txtDesc);
            float valor= Float.parseFloat(txtValor);
            long id= Long.parseLong(txtId);
            float soma= valor + valorCompra;
            String descricao= txtDescricao.getText().toString();

            Cartao c = new Cartao(id, soma);
            DatabaseManager manager= new DatabaseManager(this);
            manager.atualizarCartao(c);
            manager.historicoCompra(valorCompra, descricao, txtBandeira, data);
            manager.close();
            txtCompra.setText(".00");
            txtDescricao.setText("");
            txtData.setText("");
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Sucesso!");
            dialog.setIcon(R.drawable.ic_action_accept);
            dialog.setMessage(R.string.mensagem_comprar_cartao);
            dialog.setNeutralButton("Ok", null);
            dialog.show();

            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comprar_cartao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
