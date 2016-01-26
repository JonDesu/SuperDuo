package barqsoft.footballscores.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.model.DatabaseContract;
import barqsoft.footballscores.model.DatabaseContract.FixturesTable;
import barqsoft.footballscores.model.DatabaseContract.TeamsTable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "football_scores.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Fixtures
        final String makeFixtures = "CREATE TABLE " + DatabaseContract.FIXTURES_TABLE + " ("
                + FixturesTable._ID + " INTEGER PRIMARY KEY,"
                + FixturesTable.DATE_COL + " TEXT NOT NULL,"
                + FixturesTable.TIME_COL + " INTEGER NOT NULL,"
                + FixturesTable.HOME_ID_COL + " TEXT NOT NULL,"
                + FixturesTable.HOME_NAME_COL + " TEXT NOT NULL,"
                + FixturesTable.AWAY_ID_COL + " TEXT NOT NULL,"
                + FixturesTable.AWAY_NAME_COL + " TEXT NOT NULL,"
                + FixturesTable.LEAGUE_COL + " INTEGER NOT NULL,"
                + FixturesTable.HOME_GOALS_COL + " TEXT NOT NULL,"
                + FixturesTable.AWAY_GOALS_COL + " TEXT NOT NULL,"
                + FixturesTable.MATCH_ID + " INTEGER NOT NULL,"
                + FixturesTable.MATCH_DAY + " INTEGER NOT NULL,"
                + " UNIQUE ("+ FixturesTable.MATCH_ID+") ON CONFLICT REPLACE"
                + " );";

        //Teams
        final String makeTeams = "CREATE TABLE " + DatabaseContract.TEAMS_TABLE + " ("
                + TeamsTable._ID + " INTEGER PRIMARY KEY,"
                + TeamsTable.TEAM_ID + " TEXT NOT NULL,"
                + TeamsTable.TEAM_NAME + " TEXT NOT NULL,"
                + TeamsTable.TEAM_CREST_URL + " TEXT NOT NULL,"
                + " UNIQUE ("+ TeamsTable.TEAM_ID +") ON CONFLICT REPLACE"
                + " );";


        db.execSQL(makeTeams);
        db.execSQL(makeFixtures);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TEAMS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FIXTURES_TABLE);
    }



}
