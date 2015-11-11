package com.example.filteredlistview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;


public class HighlightArrayAdapter2 extends ArrayAdapter<String> {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;
    private List<String> mObjects;
    private int mFieldId = 0;
    private ArrayList<String> mOriginalValues;
    private ArrayFilter mFilter;
    private final Object mLock = new Object();
    private String mSearchText; // this var for highlight

    public HighlightArrayAdapter2(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        mObjects = Arrays.asList(objects);
        mFieldId = textViewResourceId;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    @Override
    public String getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public int getPosition(String item) {
        return mObjects.indexOf(item);
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                synchronized (mLock) {
                    mOriginalValues = new ArrayList<>(mObjects);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                mSearchText = "";
                ArrayList<String> list;
                synchronized (mLock) {
                    list = new ArrayList<>(mOriginalValues);
                }
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                mSearchText = prefixString;
                ArrayList<String> values;
                synchronized (mLock) {
                    values = new ArrayList<>(mOriginalValues);
                }

                final int count = values.size();
                final ArrayList<String> newValues = new ArrayList<>();

                final String[] searchWords = prefixString.split(" ");

                for (String searchWord : searchWords) {
                    for (int i = 0; i < count; i++) {
                        final String value = values.get(i);
                        final String valueText = value.toLowerCase();

                        // First match against the whole, non-splitted value
                        if (valueText.startsWith(searchWord) || valueText.contains(searchWord)) {
                            newValues.add(value);
                        } else {
                            final String[] words = valueText.split(" ");
                            for (String word : words) {
                                if (word.startsWith(searchWord) || word.contains(searchWord)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }

//                    // First match against the whole, non-splitted value
//                    if (valueText.startsWith(prefixString) || valueText.contains(prefixString)) {
//                        newValues.add(value);
//                    } else {
//                        final String[] words = valueText.split(" ");
//                        for (String word : words) {
//                            if (word.startsWith(prefixString) || word.contains(prefixString)) {
//                                newValues.add(value);
//                                break;
//                            }
//                        }
//                    }
                    }

                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<String>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(mResource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        // HIGHLIGHT...

        String fullText = getItem(position);
        if (mSearchText != null && !mSearchText.isEmpty()) {
            Spannable spannable = new SpannableString(fullText);
            String[] searchWords = mSearchText.split(" ");
            for (String searchWord : searchWords) {
                int startPos = fullText.toLowerCase(Locale.US).indexOf(searchWord.toLowerCase(Locale.US));
                int endPos = startPos + searchWord.length();

                if (startPos != -1) {
                    ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                    TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                    spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            text.setText(spannable);
//            int startPos = fullText.toLowerCase(Locale.US).indexOf(mSearchText);
//            int endPos = startPos + mSearchText.length();
//
//            if (startPos != -1) {
//                Spannable spannable = new SpannableString(fullText);
//                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
//                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
//                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                text.setText(spannable);
//            } else {
//                text.setText(fullText);
//            }
        } else {
            text.setText(fullText);
        }

        return view;
    }

    public String getRegEx(CharSequence elements) {
        String result = "(?i).*";
        for (String element : elements.toString().split("\\s")) {
            result += element + ".*";
        }
        result += ".*";
        return result;
    }
}
