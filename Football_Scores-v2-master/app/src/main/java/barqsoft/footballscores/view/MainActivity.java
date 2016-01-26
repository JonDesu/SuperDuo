package barqsoft.footballscores.view;

import android.accounts.Account;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import barqsoft.footballscores.R;
import barqsoft.footballscores.helper.AccountUtils;
import barqsoft.footballscores.data.ScoresSyncAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
{
    public static String LOG_TAG = MainActivity.class.getSimpleName();
    public static final boolean DEBUG = true;

    Account mAccount;
    DailyScoresFragmentPagerAdapter mAdapter;

    @Bind(R.id.toolbar) Toolbar mToolbarView;
    @Bind(R.id.tabs) TabLayout mTabs;
    @Bind(R.id.pager) ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAccount = AccountUtils.createSyncAccount(this);

        setSupportActionBar(mToolbarView);

        mAdapter = new DailyScoresFragmentPagerAdapter(this, getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabs.setupWithViewPager(mPager);

        if(savedInstanceState == null) {

            mTabs.getTabAt(2).select();

            ScoresSyncAdapter.syncImmediately(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Intent start_about = new Intent(this, AboutActivity.class);
            startActivity(start_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
