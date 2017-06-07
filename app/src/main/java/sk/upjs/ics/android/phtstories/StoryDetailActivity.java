package sk.upjs.ics.android.phtstories;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import sk.upjs.ics.android.phtstories.provider.PhtStoriesContract;

public class StoryDetailActivity extends AppCompatActivity {
    private TextView detailHeaderTextView;
    private TextView detailTaleTextView;
    private ImageView detailImageView;
    private Uri pathToImage;
    private Context context;
    private int getThisPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_story_detail);
        setSupportActionBar(myToolbar);

        context = this;

        Bundle bundle = getIntent().getExtras();

        final int getThis = bundle.getInt("getThis");
        getThisPos = getThis;

        detailHeaderTextView = (TextView) findViewById(R.id.detailHeaderTextView);
        detailTaleTextView = (TextView) findViewById(R.id.detailTaleTextView);
        detailImageView = (ImageView) findViewById(R.id.detailImageView);

        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                if (cursor == null){
                    detailHeaderTextView.setText("Preslo query - cursor je NULL");
                }
                else if (cursor.getCount() < 1){
                    detailHeaderTextView.setText("Preslo query - NO MATCH");
                } else {
                    cursor.moveToPosition(getThis);
                    //
                    int headerIndex = cursor.getColumnIndex(PhtStoriesContract.Pstory.HEADER);
                    int taleIndex = cursor.getColumnIndex(PhtStoriesContract.Pstory.TALE);
                    int imageIndex = cursor.getColumnIndex(PhtStoriesContract.Pstory.PICTURE);

                    String headerText = cursor.getString(headerIndex);
                    String taleText = cursor.getString(taleIndex);
                    String pictureText = cursor.getString(imageIndex);
                    pathToImage = Uri.parse(pictureText);

                    detailHeaderTextView.setText(headerText);
                    detailTaleTextView.setText(taleText);


                    //Picasso.with(context).load(pathToImage).into(detailImageView);

                    Picasso.with(context).
                            load(pathToImage).noFade().resize(500,500).centerCrop().into(detailImageView);

                }

            }
        };
        handler.startQuery(0, null, PhtStoriesContract.Pstory.CONTENT_URI, null, null, null, null);

        //System.out.println(pathToImage.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra(Intent.EXTRA_TEXT, detailTaleTextView.getText().toString());
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, this.pathToImage);

            startActivity(intent);

            return true;
        }
        if (item.getItemId() == R.id.edit){
            Intent intent = new Intent(this, EditStoryActivity.class);
            Bundle bundle = new Bundle();

            //mainActivityGridView.getAdapter().getItem(id);


            bundle.putInt("getThis",getThisPos);

            intent.putExtras(bundle);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}
