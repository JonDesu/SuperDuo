package barqsoft.footballscores.view;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import org.joda.time.LocalDate;

import barqsoft.footballscores.R;

public class DailyScoresFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public static final String LOG_TAG = DailyScoresFragmentPagerAdapter.class.getSimpleName();
    public static final int NUM_PAGES = 5;
    public static final boolean DEBUG = true;

    Context mContext;
    LocalDate mLocalDate;

    public DailyScoresFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        mLocalDate = new LocalDate();
    }

    @Override
    public Fragment getItem(int position) {
        LocalDate localDateForItem = getLocalDateForPosition(position);
        long millis = localDateForItem.toDateTimeAtStartOfDay().getMillis();

        return FixturesFragment.newInstance(millis);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 1:
                return mContext.getString(R.string.yesterday);

            case 2:
                return mContext.getString(R.string.today);

            case 3:
                return mContext.getString(R.string.tomorrow);

            default:
                LocalDate localDateForPosition = getLocalDateForPosition(position);
                return localDateForPosition.dayOfWeek().getAsText();
        }
    }

    public LocalDate getLocalDateForPosition(int position) {
        LocalDate localDate = new LocalDate();
        localDate = localDate.plusDays(position - 2);

        if(DEBUG)
            Log.d(LOG_TAG, "Position: " + position + " / " + localDate.toString());

        return localDate;
    }
}
