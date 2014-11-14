package kanzhihu.android.utils;

import android.util.SparseArray;
import kanzhihu.android.models.Category;

/**
 * Created by Jiahui.wen on 2014/11/13.
 */
public class Cache {

    private static SparseArray<Category> categoryCache = new SparseArray<Category>();

    public static Category getCategory(int id) {
        return categoryCache.get(id);
    }

    public static void cache(Category category) {
        categoryCache.put(category._id, category);
    }

    public static void clear() {
        categoryCache.clear();
    }
}
