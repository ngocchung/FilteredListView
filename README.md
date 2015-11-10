## Synopsis

An Android sample project that implements ListView with basic filtering.

## Code Example
```
		// HIGHLIGHT...
        String fullText = getItem(position);
        if (mSearchText != null && !mSearchText.isEmpty()) {
            int startPos = fullText.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
            int endPos = startPos + mSearchText.length();

            if (startPos != -1) {
                Spannable spannable = new SpannableString(fullText);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLUE});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.setText(spannable);
            } else {
                text.setText(fullText);
            }
        } else {
            text.setText(fullText);
        }
```
## Motivation

Please look at my answer at the following question http://stackoverflow.com/questions/33417887/how-do-i-highlight-the-searched-text-in-my-search-filter.
