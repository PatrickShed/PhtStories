package sk.upjs.ics.android.phtstories;

import android.app.LoaderManager;
import android.content.AsyncQueryHandler;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import sk.upjs.ics.android.phtstories.provider.PhtStoriesContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
    private GridView mainActivityGridView;
    private SimpleCursorAdapter mainActivityGridViewAdapter;
    private View activityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityLayout = (View) findViewById(R.id.activity_main);

        mainActivityGridView = (GridView) findViewById(R.id.mainActivityGridView);

        String[] from = {PhtStoriesContract.Pstory.HEADER};
        int[] to = {R.id.cardText};

        System.out.println("Toto nema byt len picture: " + from[0]);

        mainActivityGridViewAdapter = new SimpleCursorAdapter(this,
                R.layout.story,
                Constants.NO_CURSOR,
                from,
                to,
                Constants.NO_FLAGS);
        mainActivityGridView.setAdapter(mainActivityGridViewAdapter);

        mainActivityGridView.setOnItemClickListener(this);
        mainActivityGridView.setOnItemLongClickListener(this);

        getLoaderManager().initLoader(Constants.DEFAULT_LOADER_ID,Bundle.EMPTY, this);
    }

    public void onFabClick(View view){
        startActivity(new Intent(this,NewStoryActivity.class));
        //startActivity(new Intent(this, AlbumActivity.class));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id != Constants.DEFAULT_LOADER_ID){
            throw new IllegalStateException("Invalid loader ID " + id);
        }

        CursorLoader loader = new CursorLoader(this);
        loader.setUri(PhtStoriesContract.Pstory.CONTENT_URI);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.setNotificationUri(getContentResolver(), PhtStoriesContract.Pstory.CONTENT_URI);
        mainActivityGridViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mainActivityGridViewAdapter.swapCursor(Constants.NO_CURSOR);
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to delete this story?");
        builder1.setCancelable(true);
        final long identif = id;

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        AsyncQueryHandler handler = new AsyncQueryHandler(getContentResolver()) {

                            @Override
                            protected void onDeleteComplete(int token, Object cookie, int result) {
                                Toast.makeText(MainActivity.this, "Story was deleted", Toast.LENGTH_LONG).show();
                            }
                        };
                        handler.startDelete(0, null, Uri.withAppendedPath(PhtStoriesContract.Pstory.CONTENT_URI, String.valueOf(identif)), Constants.NO_SELECTION, Constants.NO_SELECTION_ARGS);


                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.memenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.switchToAlbum){
            startActivity(new Intent(this,AlbumActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
