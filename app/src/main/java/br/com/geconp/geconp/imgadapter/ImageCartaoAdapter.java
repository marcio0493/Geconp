package br.com.geconp.geconp.imgadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.geconp.geconp.R;

/**
 * Created by Marcio on 06/05/2015.
 */
public class ImageCartaoAdapter extends BaseAdapter {
    private Context c;
    private int[] imagens;
    private String[] texto;

    public ImageCartaoAdapter(Context c, int[] imagens, String[] texto) {
        this.c = c;
        this.imagens = imagens;
        this.texto = texto;
    }

    @Override
    public int getCount() {
        return imagens.length;
    }

    @Override
    public Object getItem(int position) {
        return c.getResources().getDrawable(imagens[position]);
    }

    @Override
    public long getItemId(int position) {
        return imagens[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(c);
            grid = inflater.inflate(R.layout.grid_cartao, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_textCartao);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_imageCartao);
            textView.setText(texto[position]);
            imageView.setImageResource(imagens[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;    }
}
