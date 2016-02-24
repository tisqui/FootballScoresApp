package barqsoft.footballscores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity
{
    public static int selected_match_id;
    public static int current_fragment = 2;
    public static String LOG_TAG = MainActivity.class.getSimpleName();
    private final String SAVE_TAG = "Save Test";
    private final String PAGER_CURRENT = "Pager_Current";
    private final String SELECTED_MATCH = "Selected_match";
    private final String MY_MAIN = "my_main";

    private PagerFragment my_main;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //add toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            my_main = new PagerFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, my_main)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about)
        {
            Intent start_about = new Intent(this,AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
//        Log.v(save_tag,"will save");
//        Log.v(save_tag,"fragment: "+String.valueOf(my_main.mPagerHandler.getCurrentItem()));
//        Log.v(save_tag,"selected id: "+selected_match_id);
        outState.putInt(PAGER_CURRENT,my_main.mPagerHandler.getCurrentItem());
        outState.putInt(SELECTED_MATCH,selected_match_id);
        getSupportFragmentManager().putFragment(outState,MY_MAIN,my_main);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
//        Log.v(save_tag,"will retrive");
//        Log.v(save_tag,"fragment: "+String.valueOf(savedInstanceState.getInt("Pager_Current")));
//        Log.v(save_tag,"selected id: "+savedInstanceState.getInt("Selected_match"));
        current_fragment = savedInstanceState.getInt(PAGER_CURRENT);
        selected_match_id = savedInstanceState.getInt(SELECTED_MATCH);
        my_main = (PagerFragment) getSupportFragmentManager().getFragment(savedInstanceState,MY_MAIN);
        super.onRestoreInstanceState(savedInstanceState);
    }
}
