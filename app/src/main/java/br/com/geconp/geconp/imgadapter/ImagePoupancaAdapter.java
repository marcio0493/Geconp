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
 * Created by MARCIO on 11/01/15.
 */
public class ImagePoupancaAdapter extends BaseAdapter{
    private Context c;
    private int[] images;
    private String[] texto;

    public ImagePoupancaAdapter(Context c, int[] images, String[] texto) {
        this.c = c;
        this.images = images;
        this.texto = texto;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return c.getResources().getDrawable(images[position]);
    }

    @Override
    public long getItemId(int position) {
        return images[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(c);
            grid = inflater.inflate(R.layout.grid_poupanca, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_textPoupanca);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_imagePoupanca);
            textView.setText(texto[position]);
            imageView.setImageResource(images[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;    }
}
