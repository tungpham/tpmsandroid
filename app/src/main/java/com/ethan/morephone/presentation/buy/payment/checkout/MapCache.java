

package com.ethan.morephone.presentation.buy.payment.checkout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class MapCache implements Cache {

    @Nonnull
    private final Map<Key, Entry> mMap = new HashMap<Key, Entry>();

    MapCache() {
    }

    @Nullable
    @Override
    public Entry get(@Nonnull Key key) {
        return mMap.get(key);
    }

    @Override
    public void put(@Nonnull Key key, @Nonnull Entry entry) {
        mMap.put(key, entry);
    }

    @Override
    public void init() {
    }

    @Override
    public void remove(@Nonnull Key key) {
        mMap.remove(key);
    }

    @Override
    public void removeAll(int type) {
        final Set<Map.Entry<Key, Entry>> entries = mMap.entrySet();
        final Iterator<Map.Entry<Key, Entry>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            final Map.Entry<Key, Entry> entry = iterator.next();
            if (entry.getKey().type == type) {
                iterator.remove();
            }
        }
    }

    @Override
    public void clear() {
        mMap.clear();
    }
}
