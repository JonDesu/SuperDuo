package it.jaschke.alexandria.ui;

import android.content.Context;

import it.jaschke.alexandria.ui.BookDetail.BookDetailActivity;
import it.jaschke.alexandria.ui.AddABook.AddBookActivity;

public class IntentLauncher {

    public static void launchISBNRegistration(Context context, boolean launchScan) {
        context.startActivity(AddBookActivity.launchIntent(context, launchScan));
    }

    public static void launchBookDetail(Context context, long bookId) {
        context.startActivity(BookDetailActivity.launchIntent(context, bookId));
    }

}
