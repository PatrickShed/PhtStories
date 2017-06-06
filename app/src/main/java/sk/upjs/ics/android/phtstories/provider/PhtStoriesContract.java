package sk.upjs.ics.android.phtstories.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Patrik on 6.6.2017.
 */

public interface PhtStoriesContract {
    String AUTHORITY = "sk.upjs.ics.android.phtstories";

    interface Pstory extends BaseColumns{
        String TABLE_NAME = "pstory";

        Uri CONTENT_URI = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();

        String HEADER = "header";
        String TALE = "tale";
        String PICTURE = "picture";
    }

}
