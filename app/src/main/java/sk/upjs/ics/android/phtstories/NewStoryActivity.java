package sk.upjs.ics.android.phtstories;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.CameraEngine;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import sk.upjs.ics.android.phtstories.provider.PhtStoriesContract;

public class NewStoryActivity extends AppCompatActivity {

    private Uri imagePath;
    private ImageView imageView;
    private TextInputEditText storyHeaderInputEditText;
    private TextInputEditText storyTaleInputEditText;
    private int initPic = R.drawable.initlogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_story);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar_new_story);
        setSupportActionBar(myToolbar);

        imageView = (ImageView) findViewById(R.id.newStoryPicImageView);
        storyHeaderInputEditText = (TextInputEditText) findViewById(R.id.newStoryHeaderInputEditText);
        storyTaleInputEditText = (TextInputEditText) findViewById(R.id.newStoryTaleInputEditText);

        //Picasso.with(this).load(imagePath).into(imageView);
        imageView.setImageResource(initPic);
    }

    public void onImageViewClick(View view){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = new File(dir + "picture" + System.currentTimeMillis() + ".jpg");

        Intent intent = new CameraActivity.IntentBuilder(this)
                .skipConfirm().forceEngine(CameraEngine.ID.CLASSIC).to(image).updateMediaStore().build();

        startActivityForResult(intent, 0);
    }

    public void onFabClick(View view){
        ContentValues contentValues = new ContentValues();

        contentValues.put(PhtStoriesContract.Pstory.HEADER, storyHeaderInputEditText.getText().toString());
        contentValues.put(PhtStoriesContract.Pstory.TALE, storyTaleInputEditText.getText().toString());

        contentValues.put(PhtStoriesContract.Pstory.PICTURE, imagePath.toString());


        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {
            @Override
            protected void onInsertComplete(int token, Object cookie, Uri uri) {
                Toast.makeText(NewStoryActivity.this, "Story was saved", Toast.LENGTH_LONG).show();
            }

        };

        handler.startInsert(0, Constants.NO_COOKIE, PhtStoriesContract.Pstory.CONTENT_URI, contentValues);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            imagePath = data.getData();
            //ImageView imageView = (ImageView) findViewById(R.id.newStoryPicImageView);
            Picasso.with(this).load(imagePath).resize(500,500).noFade().centerCrop().into(imageView);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
