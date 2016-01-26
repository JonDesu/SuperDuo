package it.jaschke.alexandria.ui.BookDetail;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.jaschke.alexandria.R;
import it.jaschke.alexandria.model.FullBook;
import it.jaschke.alexandria.db.AlexandriaContract;
import it.jaschke.alexandria.service.BookService;

public class BookDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = BookDetailActivity.class.getSimpleName();

    private static final String ARGS_BOOK_ID = "book_id";
    private static final int LOADER_ID = 999;

    long mBookId;
    FullBook mFullBook;
    ShareActionProvider mShareActionProvider;

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.book_cover) ImageView mBookCoverView;
    @Bind(R.id.book_title) TextView mBookTitleView;
    @Bind(R.id.book_author) TextView mBookAuthorView;
    @Bind(R.id.book_category) TextView mBookCategoryView;
    @Bind(R.id.book_description) TextView mBookDescriptionView;


    public static Intent launchIntent(Context context, long bookId) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(ARGS_BOOK_ID, bookId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        ButterKnife.bind(this);

        //Get Book Id
        mBookId = getIntent().getLongExtra(ARGS_BOOK_ID, -1);
        Log.d(LOG_TAG, "Details for Book: " + mBookId);

        //Loader Setup
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        //Action Bar Setup
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_book_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareIntent();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete) {
            BookService.deleteBook(this, Long.toString(mBookId));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Callback
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                this,
                AlexandriaContract.BookEntry.buildFullBookUri(mBookId),
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            finish();
            return;
        }

        data.moveToFirst();
        mFullBook = FullBook.getBookData(data);

        //Progress Bar
        mProgressBar.setVisibility(View.GONE);

        //Cover
        if (mFullBook.bookCoverUrl.length() > 0)
            Glide.with(this).load(mFullBook.bookCoverUrl).into(mBookCoverView);

        //Data
        mBookTitleView.setText(mFullBook.bookTitle);
        mBookAuthorView.setText(mFullBook.authorName);
        mBookCategoryView.setText(mFullBook.categoryName);
        mBookDescriptionView.setText(mFullBook.bookDescription);

        setShareIntent();
    }

    private void setShareIntent() {
        if(mShareActionProvider == null || mFullBook == null)
            return;

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + mFullBook.bookTitle);
        mShareActionProvider.setShareIntent(shareIntent);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
