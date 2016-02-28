package barqsoft.footballscores.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.data.DatabaseContract;

/**
 * Created by squirrel on 2/25/16.
 */
public class CollectionWidgetService extends RemoteViewsService {

    public final String LOG_TAG = CollectionWidgetService.class.getSimpleName();
    private static final String[] SCORE_COLUMNS = {
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.TIME_COL
    };

    private static final int INDEX_ID = 0;
    private static final int INDEX_HOME_COL = 1;
    private static final int INDEX_AWAY_COL = 2;
    private static final int INDEX_HOME_GOALS_COL = 3;
    private static final int INDEX_AWAY_GOALS_COL = 4;
    private static final int INDEX_TIME_COL = 5;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();

                data = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI,
                        //projection
                        SCORE_COLUMNS,
                        //selection
                        null,
                        null,
                        DatabaseContract.scores_table.DATE_COL + " DESC");
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_collection_list_item);


                // Extract the latest game score data from the Cursor
                String teams = data.getString(INDEX_HOME_COL) + " - " + data.getString(INDEX_AWAY_COL);
                String score = Utilies.getScores(data.getInt(INDEX_HOME_GOALS_COL), data.getInt(INDEX_AWAY_GOALS_COL));
                String time = data.getString(INDEX_TIME_COL);

                views.setTextViewText(R.id.score_text, score);
                views.setTextViewText(R.id.teams_text, teams);
                views.setTextViewText(R.id.time_text, time);

                final Intent fillInIntent = new Intent();
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }


            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_collection_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
//                if (data.moveToPosition(position))
//                    return Long.getLong(data.getString(INDEX_ID));
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
