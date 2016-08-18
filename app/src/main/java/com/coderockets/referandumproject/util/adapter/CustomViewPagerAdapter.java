package com.coderockets.referandumproject.util.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hugo.weaving.DebugLog;

public class CustomViewPagerAdapter extends PagerAdapter {

    ArrayList<View> views = new ArrayList<>();

    //-----------------------------------------------------------------------------
    // Used by ViewPager.  "Object" represents the page; tell the ViewPager where the
    // page should be displayed, from left-to-right.  If the page no longer exists,
    // return POSITION_NONE.
    @DebugLog
    @Override
    public int getItemPosition(Object object) {
        int index = views.indexOf(object);
        if (index == -1)
            return POSITION_NONE;
        else
            return index;
    }


    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager needs a page to display; it is our job
    // to add the page to the container, which is normally the ViewPager itself.  Since
    // all our pages are persistent, we simply retrieve it from our "views" ArrayList.
    @DebugLog
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = views.get(position);
        container.addView(v);
        return v;
    }


    //-----------------------------------------------------------------------------
    // Used by ViewPager.  Called when ViewPager no longer needs a page to display; it
    // is our job to remove the page from the container, which is normally the
    // ViewPager itself.  Since all our pages are persistent, we do nothing to the
    // contents of our "views" ArrayList.
    @DebugLog
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager; can be used by app as well.
    // Returns the total number of pages that the ViewPage can display.  This must
    // never be 0.
    @DebugLog
    @Override
    public int getCount() {
        return views.size();
    }

    //-----------------------------------------------------------------------------
    // Used by ViewPager.
    @DebugLog
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //-----------------------------------------------------------------------------
    // Add "view" to right end of "views".
    // Returns the position of the new view.
    // The app should call this to add pages; not used by ViewPager.
    @DebugLog
    public int addView(View v) {
        return addView(v, views.size());
    }

    //-----------------------------------------------------------------------------
    // Add "view" at "position" to "views".
    // Returns position of new view.
    // The app should call this to add pages; not used by ViewPager.,
    @DebugLog
    public int addView(View v, int position) {
        views.add(position, v);
        notifyDataSetChanged();
        return position;
    }

    //-----------------------------------------------------------------------------
    // Removes "view" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    @DebugLog
    public int removeView(ViewPager pager, View v) {
        return removeView(pager, views.indexOf(v));
    }

    //-----------------------------------------------------------------------------
    // Removes the "view" at "position" from "views".
    // Retuns position of removed view.
    // The app should call this to remove pages; not used by ViewPager.
    @DebugLog
    public int removeView(ViewPager pager, int position) {
        // ViewPager doesn't have a delete method; the closest is to set the adapter
        // again.  When doing so, it deletes all its views.  Then we can delete the view
        // from from the adapter and finally set the adapter to the pager again.  Note
        // that we set the adapter to null before removing the view from "views" - that's
        // because while ViewPager deletes all its views, it will call destroyItem which
        // will in turn cause a null pointer ref.
        pager.setAdapter(null);
        views.remove(position);
        pager.setAdapter(this);

        return position;
    }

    //-----------------------------------------------------------------------------
    // Returns the "view" at "position".
    // The app should call this to retrieve a view; not used by ViewPager.
    @DebugLog
    public View getView(int position) {
        return views.get(position);
    }

    // Other relevant methods:

    // finishUpdate - called by the ViewPager - we don't care about what pages the
    // pager is displaying so we don't use this method.


    /*
    @DebugLog
    public void addItem(ModelQuestionInformation item) {
        mListSorular.add(item);
        super.notifyDataSetChanged();
    }

    @DebugLog
    public void removeItem(ModelQuestionInformation item) {
        mListSorular.remove(item);
        super.notifyDataSetChanged();
    }

    @DebugLog
    public void removeItem(int location) {
        mListSorular.remove(location);
        super.notifyDataSetChanged();
    }

    //@DebugLog
    @Override
    public int getCount() {
        return mListSorular.size();
    }

    @DebugLog
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }*/


    /*
    @DebugLog
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ViewGroup sorularView = (ViewGroup) mInflater.inflate(R.layout.soru, container, false);
        container.addView(sorularView);

        TextView textViewSoru = (TextView) sorularView.findViewById(R.id.TextViewSoru);

        textViewSoru.setText(mListSorular.get(position).getQuestionText());
        //textViewSoru.setText(mContext.getResources().getString(R.string.soru1));

        return sorularView;
    }
*/

    /*@DebugLog
    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {


        if (mListSorular.size() > position) {
            //   mListSorular.remove(position);
            //   ((ViewPager) container).removeViewAt(position);
            //((ViewPager) container).setAdapter(this);
            //   super.notifyDataSetChanged();
        }
        //((ViewPager) container).removeAllViews();


        //container.removeViewAt(0);
        //mListSorular.remove(view);
        //super.notifyDataSetChanged();
        //((ViewPager) container).removeView((View) view);
        //finishUpdate(container);

        super.destroyItem(container, position, view);
    }*/
    /*
    @DebugLog
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        // var viewPager = container.JavaCast<ViewPager>();
        // viewPager.RemoveView(objectValue as View);
        /*
        View view = mListSorular.get(position);
        if (view != null) {
            container.removeView(view);
            views.remove(position);
        }
        ((ViewPager) container).removeView((View) object);

        super.notifyDataSetChanged();
        notifyDataSetChanged();
    }
*/
    /*
    @DebugLog
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;

        //int position = mListSorular.indexOf(object);
        //return position == -1 ? POSITION_NONE : position;
    }
    */


}
