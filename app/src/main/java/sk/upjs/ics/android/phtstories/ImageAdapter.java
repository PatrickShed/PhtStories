package sk.upjs.ics.android.phtstories;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Patrik on 6.6.2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private Uri[] imgIds;


    public ImageAdapter(Context c, Uri[] imgIdsSrc) {
        mContext = c;
        imgIds = imgIdsSrc;
        //System.out.println("Som v ImageAdapteri a dostal som pole: " + Arrays.toString(imgIds));
    }

    @Override
    public int getCount() {
        return imgIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }
        //System.out.println("som v TEJ metode a mam position = " + position);

        Picasso.with(mContext)
                .load(imgIds[position])
                .noFade().resize(150, 150)
                .centerCrop()
                .into(imageView);

        return imageView;
    }
}
