package br.com.geconp.geconp.imgadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.geconp.geconp.R;

/**
 * Created by MARCIO on 09/01/15.
 */
public class ImageAdapter extends BaseAdapter{
    private Context mContext; /*Contexto*/
    private int[]  mThumbIds;
    private String[] texto;
    public ImageAdapter(Context c, int[]  mThumbIds, String[] texto) {
        this.mContext = c;
        this.mThumbIds= mThumbIds;
        this.texto= texto;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }
    /**
     * Retorna Imagem(Drawable) na posição
     * @param position posição da imagem na lista
     * @return Imagem na posição indicada
     */
    @Override
    public Object getItem(int position) {
        return mContext.getResources().getDrawable(mThumbIds[position]);
    }
    /**
     * Retorna id da imagem na posição
     * @param position posição da imagem
     * @return id da imagem
     */
    @Override
    public long getItemId(int position) {
        return mThumbIds[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.grid_single, null);
            TextView textView = (TextView) grid.findViewById(R.id.grid_text);
            ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
            textView.setText(texto[position]);
            imageView.setImageResource(mThumbIds[position]);
        } else {
            grid = (View) convertView;
        }
        return grid;

    }
}
