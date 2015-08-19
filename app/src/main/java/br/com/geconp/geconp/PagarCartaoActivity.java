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

import br.com.geconp.geconp.async.AsyncPagoCartao;
import br.com.geconp.geconp.dao.DatabaseManager;
import br.com.geconp.geconp.model.Cartao;

public class PagarCartaoActivity extends Activity {
    static final int DATE_DIALOG_ID = 0;
    private String data;
    private EditText txtData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_cartao);

        Intent origem= getIntent();
        final String txtId= origem.getStringExtra("ID3");
        final String txtValor= origem.getStringExtra("VALOR3");
        final String txtBandeira= origem.getStringExtra("BANDEIRA3");

        Button btnPagar= (Button)findViewById(R.id.buttonPagarCartao);
        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagar(txtId, txtValor, txtBandeira);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        txtData= (EditText)findViewById(R.id.editTextDataPago);
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


    public void pagar(String txtId, String txtValor, String txtBandeira){
        EditText txtPago= (EditText)findViewById(R.id.editTextValorPagarCartao);
        EditText txtDesc= (EditText)findViewById(R.id.editTextDescPagarCartao);
        if(txtPago.getText().toString().equals(".00") || txtPago.getText().toString().equals("") || txtDesc.getText().toString().equals("")||
                txtData.getText().toString().equals("")){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle(R.string.cuidado);
            dialog.setIcon(R.drawable.ic_action_warning);
            dialog.setMessage(R.string.campoVazio);
            dialog.setNeutralButton("Ok", null);
            dialog.show();
        }else{
            //Bloco de formatação do valor
            String aux1= txtPago.getText().toString();
            String aux2= aux1.replace(",", ".");
            float valorPagado= Float.parseFloat(aux2);

            float valor= Float.parseFloat(txtValor);
            long id= Long.parseLong(txtId);
            float pagado= valor - valorPagado;
            String descricao= txtDesc.getText().toString();

            Cartao cartao= new Cartao(id, pagado);
            Cartao c= new Cartao(id);
            DatabaseManager manager= new DatabaseManager(this);
            manager.atualizarCartao(cartao);
            manager.historicoPago(valorPagado, descricao, txtBandeira, data);
            manager.close();
            txtPago.setText(".00");
            txtDesc.setText("");
            txtData.setText("");

            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setTitle("Sucesso!");
            dialog.setIcon(R.drawable.ic_action_accept);
            dialog.setMessage(R.string.mensagem_pagar_cartao);
            dialog.setNeutralButton("Ok", null);
            dialog.show();

            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pagar_cartao, menu);
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
