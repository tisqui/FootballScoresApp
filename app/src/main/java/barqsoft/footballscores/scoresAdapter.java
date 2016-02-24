package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class scoresAdapter extends CursorAdapter {
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;
    public double detail_match_id = 0;
    private String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
    private final String SHARE_INTENT_TYPE = "text/plain";

    public scoresAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        String homeTeam = cursor.getString(COL_HOME);
        String awayTeam = cursor.getString(COL_AWAY);


        mHolder.home_name.setText(homeTeam);
        //For accessiblity adding the home name description
        mHolder.home_name.setContentDescription(homeTeam);

        mHolder.away_name.setText(awayTeam);
        //For accessiblity adding away name description
        mHolder.away_name.setContentDescription(awayTeam);

        mHolder.date.setText(cursor.getString(COL_MATCHTIME));
        //For accessiblity adding date description
        mHolder.date.setContentDescription(context.getString(R.string.content_description_time) + cursor.getString(COL_MATCHTIME));

        mHolder.score.setText(Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
        mHolder.match_id = cursor.getDouble(COL_ID);
        //For accessiblity adding score description
        mHolder.score.setContentDescription(context.getString(R.string.content_description_score) + Utilies.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));

        mHolder.home_crest.setImageResource(Utilies.getTeamCrestByTeamName(
                homeTeam));
        // For accessibility, add a content description to the icon field of the home_crest
        mHolder.home_crest.setContentDescription(context.getString(R.string.content_description_team_logo) + homeTeam);

        mHolder.away_crest.setImageResource(Utilies.getTeamCrestByTeamName(
                awayTeam
        ));
        // For accessibility, add a content description to the icon field of the away_crest
        mHolder.away_crest.setContentDescription(context.getString(R.string.content_description_team_logo) + awayTeam);

        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);

        if (mHolder.match_id == detail_match_id) {
            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));

            String matchDay = Utilies.getMatchDay(cursor.getInt(COL_MATCHDAY),
                    cursor.getInt(COL_LEAGUE), context);
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(matchDay);
            //For accessiblity adding the matchday description
            match_day.setContentDescription(matchDay);

            String leagueString = Utilies.getLeague(cursor.getInt(COL_LEAGUE), context);
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(leagueString);
            //For accessiblity adding the league description
            league.setContentDescription(leagueString);

            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(mHolder.home_name.getText() + " "
                            + mHolder.score.getText() + " " + mHolder.away_name.getText() + " "));
                }
            });

            //for accessibility adding the cotent description of the share button
            share_button.setContentDescription(context.getString(R.string.content_description_share_button));
        } else {
            container.removeAllViews();
        }

    }

    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType(SHARE_INTENT_TYPE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

}
