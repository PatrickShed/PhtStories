package sk.upjs.ics.android.phtstories;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sk.upjs.ics.android.phtstories.provider.PhtStoriesContract;

public class AlbumActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView albumGridView;
    private SimpleCursorAdapter albumGridViewAdapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        context = this;

        albumGridView = (GridView) findViewById(R.id.albumGridView);
        //final List<Uri> pathsFromDb = new ArrayList<Uri>();

        //String[] from = {PhtStoriesContract.Pstory.PICTURE};


        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {

            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                int imageIndex = cursor.getColumnIndex(PhtStoriesContract.Pstory.PICTURE);
                Uri[] uriFrom = new Uri[cursor.getCount()];
                if (cursor == null){

                }
                else if (cursor.getCount() < 1){

                } else {
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++){
                        uriFrom[i] = Uri.parse(cursor.getString(imageIndex));
                        cursor.moveToNext();
                    }
                    //System.out.println("Pole uriciek: " + Arrays.toString(uriFrom));
                    albumGridView.setAdapter(new ImageAdapter(context,uriFrom));
                }
            }
        };
        handler.startQuery(0, null, PhtStoriesContract.Pstory.CONTENT_URI, null, null, null, null);

        albumGridView.setOnItemClickListener(this);

        //albumGridView.setAdapter(new ImageAdapter(this, uriFrom)); // prepis null na pole
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, StoryDetailActivity.class);
        Bundle bundle = new Bundle();

        //mainActivityGridView.getAdapter().getItem(id);


        bundle.putInt("getThis",position);

        intent.putExtras(bundle);

        startActivity(intent);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memenu2, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.switchToStandard){
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
