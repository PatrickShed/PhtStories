package sk.upjs.ics.android.phtstories;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import sk.upjs.ics.android.phtstories.provider.PhtStoriesContract;

public class EditStoryActivity extends AppCompatActivity {

    private TextInputEditText storyHeaderInputEditText;
    private TextInputEditText storyTaleInputEditText;
    private ImageView editImageView;
    private Uri pathToImage;
    private Context context;
    private int getThisPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_edit_story);
        setSupportActionBar(myToolbar);

        context = this;

        Bundle bundle = getIntent().getExtras();
        final int getThis = bundle.getInt("getThis");
        getThisPos = getThis;

        editImageView = (ImageView) findViewById(R.id.editStoryPicImageView);
        storyHeaderInputEditText = (TextInputEditText) findViewById(R.id.editStoryHeaderInputEditText);
        storyTaleInputEditText = (TextInputEditText) findViewById(R.id.editStoryTaleInputEditText);

        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                if (cursor == null){
                    //detailHeaderTextView.setText("Preslo query - cursor je NULL");
                }
                else if (cursor.getCount() < 1){
                    //detailHeaderTextView.setText("Preslo query - NO MATCH");
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

                    storyHeaderInputEditText.setText(headerText);
                    storyTaleInputEditText.setText(taleText);


                    //Picasso.with(context).load(pathToImage).into(detailImageView);

                    Picasso.with(context).
                            load(pathToImage).noFade().resize(500,500).centerCrop().into(editImageView);

                }

            }
        };
        handler.startQuery(0, null, PhtStoriesContract.Pstory.CONTENT_URI, null, null, null, null);
    }

    public void onFabClick(View view){
        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {

            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(EditStoryActivity.this, "Story was changed", Toast.LENGTH_LONG).show();
            }
        };
        handler.startDelete(0,null, Uri.withAppendedPath(PhtStoriesContract.Pstory.CONTENT_URI, String.valueOf(getThisPos)),Constants.NO_SELECTION,Constants.NO_SELECTION_ARGS);



        ContentValues contentValues = new ContentValues();

        contentValues.put(PhtStoriesContract.Pstory.HEADER, storyHeaderInputEditText.getText().toString());
        contentValues.put(PhtStoriesContract.Pstory.TALE, storyTaleInputEditText.getText().toString());

        contentValues.put(PhtStoriesContract.Pstory.PICTURE, pathToImage.toString());

        /*
        AsyncQueryHandler handlerInsert = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(EditStoryActivity.this, "Story was changed", Toast.LENGTH_LONG).show();
            }

        };*/

        handler.startInsert(0, Constants.NO_COOKIE, PhtStoriesContract.Pstory.CONTENT_URI, contentValues);
    }
}
