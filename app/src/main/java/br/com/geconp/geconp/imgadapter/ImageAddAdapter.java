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
 * Created by MARCIO on 09/01/15.
 */
public class ImageAddAdapter extends BaseAdapter{
    private Context contexto;
    private int[] images;
    private String[] texto;

    public ImageAddAdapter(Context contexto, int[] images, String[] texto) {
        this.contexto = contexto;
        this.texto = texto;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return contexto.getResources().getDrawable(images[position]);
    }

    @Override
    public long getItemId(int position) {
        return images[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(contexto);
            grid = inflater.inflate(R.layout.grid_cadastro, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_textAdd);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_imageAdd);
            textView.setText(texto[position]);
            imageView.setImageResource(images[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;


    }
}
