package it.jaschke.alexandria.ui.AddABook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;

public class AddBookActivity extends AppCompatActivity {

    public static final String LAUNCH_SCAN = "launch_scan";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    AddBookFragment mFragment;

    public static Intent launchIntent(Context context, boolean launchScan) {
        Intent intent = new Intent(context, AddBookActivity.class);
        intent.putExtra(LAUNCH_SCAN, launchScan);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(savedInstanceState != null) {
            mFragment = (AddBookFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        } else {
            mFragment = AddBookFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragment).commit();
        }

        boolean launchScan = getIntent().getBooleanExtra(LAUNCH_SCAN, false);
        if(savedInstanceState == null && launchScan) {
            launchScanActivity();
        }

    }

    public void launchScanActivity() {

        Intent intent = new Intent(this, ScanBookActivity.class);
        startActivityForResult(intent, 999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 999 && resultCode == 999) {
            String barcode = data.getStringExtra(ScanBookActivity.BARCODE);
            mFragment.setIsbnNumberAndStartSearch(barcode);
        }

    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

}
