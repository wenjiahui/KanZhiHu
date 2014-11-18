package kanzhihu.android.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
import kanzhihu.android.BuildConfig;
import kanzhihu.android.database.table.ArticleTable;
import kanzhihu.android.database.table.CategoryTable;

public class ZhihuProvider extends ContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";

    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri ARTICLE_CONTENT_URI =
        Uri.withAppendedPath(ZhihuProvider.AUTHORITY_URI, ArticleContent.CONTENT_PATH);

    public static final Uri CATEGORY_CONTENT_URI =
        Uri.withAppendedPath(ZhihuProvider.AUTHORITY_URI, CategoryContent.CONTENT_PATH);

    private static final UriMatcher URI_MATCHER;
    private ZhihuDatabase mDatabase;

    private static final int ARTICLE_DIR = 0;
    private static final int ARTICLE_ID = 1;

    private static final int CATEGORY_DIR = 2;
    private static final int CATEGORY_ID = 3;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, ArticleContent.CONTENT_PATH, ARTICLE_DIR);
        URI_MATCHER.addURI(AUTHORITY, ArticleContent.CONTENT_PATH + "/#", ARTICLE_ID);

        URI_MATCHER.addURI(AUTHORITY, CategoryContent.CONTENT_PATH, CATEGORY_DIR);
        URI_MATCHER.addURI(AUTHORITY, CategoryContent.CONTENT_PATH + "/#", CATEGORY_ID);
    }

    public static final class ArticleContent implements BaseColumns {
        public static final String CONTENT_PATH = "article";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhihu_database.article";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhihu_database.article";
    }

    public static final class CategoryContent implements BaseColumns {
        public static final String CONTENT_PATH = "category";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.zhihu_database.category";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.zhihu_database.category";
    }

    @Override
    public final boolean onCreate() {
        mDatabase = new ZhihuDatabase(getContext());
        return true;
    }

    @Override
    public final String getType(final Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ARTICLE_DIR:
                return ArticleContent.CONTENT_TYPE;
            case ARTICLE_ID:
                return ArticleContent.CONTENT_ITEM_TYPE;

            case CATEGORY_DIR:
                return CategoryContent.CONTENT_TYPE;
            case CATEGORY_ID:
                return CategoryContent.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public final Cursor query(final Uri uri, String[] projection, final String selection, final String[] selectionArgs,
        final String sortOrder) {
        final SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        final SQLiteDatabase dbConnection = mDatabase.getReadableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case ARTICLE_ID:
                queryBuilder.appendWhere(ArticleTable._ID + "=" + uri.getLastPathSegment());
            case ARTICLE_DIR:
                queryBuilder.setTables(ArticleTable.TABLE_NAME);
                break;

            case CATEGORY_ID:
                queryBuilder.appendWhere(CategoryTable._ID + "=" + uri.getLastPathSegment());
            case CATEGORY_DIR:
                queryBuilder.setTables(CategoryTable.TABLE_NAME);
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }

        Cursor cursor = queryBuilder.query(dbConnection, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public final Uri insert(final Uri uri, final ContentValues values) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case ARTICLE_DIR:
                case ARTICLE_ID:
                    final long articleId = dbConnection.insertOrThrow(ArticleTable.TABLE_NAME, null, values);
                    final Uri newArticleUri = ContentUris.withAppendedId(ARTICLE_CONTENT_URI, articleId);
                    getContext().getContentResolver().notifyChange(newArticleUri, null);

                    dbConnection.setTransactionSuccessful();
                    return newArticleUri;
                case CATEGORY_DIR:
                case CATEGORY_ID:
                    final long categoryId = dbConnection.insertOrThrow(CategoryTable.TABLE_NAME, null, values);
                    final Uri newCategoryUri = ContentUris.withAppendedId(CATEGORY_CONTENT_URI, categoryId);
                    getContext().getContentResolver().notifyChange(newCategoryUri, null);

                    dbConnection.setTransactionSuccessful();
                    return newCategoryUri;
                default:
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbConnection.endTransaction();
        }

        return null;
    }

    @Override
    public final int update(final Uri uri, final ContentValues values, final String selection,
        final String[] selectionArgs) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int updateCount = 0;
        List<Uri> joinUris = new ArrayList<Uri>();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case ARTICLE_DIR:
                    updateCount = dbConnection.update(ArticleTable.TABLE_NAME, values, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case ARTICLE_ID:
                    final long articleId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(ArticleTable.TABLE_NAME, values,
                        ArticleTable._ID + "=" + articleId + (TextUtils.isEmpty(selection) ? ""
                            : " AND (" + selection + ")"), selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;

                case CATEGORY_DIR:
                    updateCount = dbConnection.update(CategoryTable.TABLE_NAME, values, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_ID:
                    final long categoryId = ContentUris.parseId(uri);
                    updateCount = dbConnection.update(CategoryTable.TABLE_NAME, values,
                        CategoryTable._ID + "=" + categoryId + (TextUtils.isEmpty(selection) ? ""
                            : " AND (" + selection + ")"), selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (updateCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            for (Uri joinUri : joinUris) {
                getContext().getContentResolver().notifyChange(joinUri, null);
            }
        }

        return updateCount;
    }

    @Override
    public final int delete(final Uri uri, final String selection, final String[] selectionArgs) {
        final SQLiteDatabase dbConnection = mDatabase.getWritableDatabase();
        int deleteCount = 0;
        List<Uri> joinUris = new ArrayList<Uri>();

        try {
            dbConnection.beginTransaction();

            switch (URI_MATCHER.match(uri)) {
                case ARTICLE_DIR:
                    deleteCount = dbConnection.delete(ArticleTable.TABLE_NAME, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case ARTICLE_ID:
                    deleteCount = dbConnection.delete(ArticleTable.TABLE_NAME, ArticleTable.WHERE_ID_EQUALS,
                        new String[] { uri.getLastPathSegment() });

                    dbConnection.setTransactionSuccessful();
                    break;

                case CATEGORY_DIR:
                    deleteCount = dbConnection.delete(CategoryTable.TABLE_NAME, selection, selectionArgs);

                    dbConnection.setTransactionSuccessful();
                    break;
                case CATEGORY_ID:
                    deleteCount = dbConnection.delete(CategoryTable.TABLE_NAME, CategoryTable.WHERE_ID_EQUALS,
                        new String[] { uri.getLastPathSegment() });

                    dbConnection.setTransactionSuccessful();
                    break;

                default:
                    throw new IllegalArgumentException("Unsupported URI:" + uri);
            }
        } finally {
            dbConnection.endTransaction();
        }

        if (deleteCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            for (Uri joinUri : joinUris) {
                getContext().getContentResolver().notifyChange(joinUri, null);
            }
        }

        return deleteCount;
    }
}