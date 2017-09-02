package top.smartsport.www.base;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import top.smartsport.www.utils.ViewHolder;

/**
 * @author fengshuai
 * @describe EntityListAdapter
 */
public abstract class EntityListAdapter<T, VH extends ViewHolder> implements ListAdapter {

    protected List<T> mList;
    protected Context mContext;
    protected LayoutInflater inflater;

    private final DataSetObservable mDataSetObservable = new DataSetObservable();
    protected final Object mLock = new Object();

    public EntityListAdapter(Context context) {
        this(context, null);
    }

    public EntityListAdapter(Context context, List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        mList = list;
        inflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return getCount() == 0;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) != null;
    }

    public void add(T object) {
        if (object == null) {
            return;
        }
        synchronized (mLock) {
            mList.add(object);
        }
        notifyDataSetChanged();
    }

    public void removeItem(T object) {
        if (object == null) {
            return;
        }
        synchronized (mLock) {
            mList.remove(object);
        }
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        if (collection == null) {
            return;
        }
        synchronized (mLock) {
            mList.addAll(collection);
        }
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection, int index) {
        if (collection == null) {
            return;
        }
        synchronized (mLock) {
            mList.addAll(index, collection);
        }
        notifyDataSetChanged();
    }

    public void insert(T object, int index) {
        synchronized (mLock) {
            mList.add(index, object);
        }
        notifyDataSetChanged();
    }

    public void remove(View object) {
        synchronized (mLock) {
            mList.remove(object);
        }
        notifyDataSetChanged();
    }


    public void clear() {
        synchronized (mLock) {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            Collections.sort(mList, comparator);
        }
        notifyDataSetChanged();
    }

    public void replaceAll(Collection<? extends T> collection) {
        if (collection == null) {
            mList.clear();
            return;
        }
        synchronized (mLock) {
            mList.clear();
            mList.addAll(collection);
        }
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH mViewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(getAdapterRes(), parent, false);
            mViewHolder = getViewHolder(convertView);
            convertView.setTag(mViewHolder);
            //对于listview，注意添加这一行，即可在item上使用高度

        } else {
            mViewHolder = (VH) convertView.getTag();
        }
        initViewHolder(mViewHolder, position);
        AutoUtils.autoSize(convertView);
        return convertView;
    }

    protected abstract int getAdapterRes();

    protected abstract VH getViewHolder(View root);

    protected abstract void initViewHolder(VH vh, int position);

}
