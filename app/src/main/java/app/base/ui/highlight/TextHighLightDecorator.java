package app.base.ui.highlight;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

public class TextHighLightDecorator extends Decorator {

	public static final int decoratorType = DECORATOR_HIGHLIGHT;
	private KeyMatcher matcher;
	private int highLightColor;

	public TextHighLightDecorator(int highLightColor) {
		super();
		this.highLightColor = highLightColor;
	}

	public KeyMatcher getMatcher() {
		return matcher;
	}

	public TextHighLightDecorator setMatcher(String key) {
		this.matcher = new KeyMatcher(key);
		return this;
	}

	public void setMatcher(KeyMatcher matcher) {
		this.matcher = matcher;
	}

	public int getHighLightColor() {
		return highLightColor;
	}

	public void setHighLightColor(int highLightColor) {
		this.highLightColor = highLightColor;
	}

	@Override
	public SpannableStringBuilder getDecorated(Object key, String name,
			String sortkey) {
		String matchStr = key.toString();
		// en head match
		// zh fulltext match
		SpannableStringBuilder style = new SpannableStringBuilder(matchStr);

		// Logs.e("=== - name "+name);
		matcher.match(matchStr, name, true);
		boolean matched = false;
		boolean oncematch = true;
		while (oncematch
				&& getHilite(style, matchStr, matcher.getStart(),
						matcher.getEnd())) {
			oncematch = matcher.match(matchStr, name, true);
			matched = true;

		}
		if (matched) {
			matcher.matcherClean();
			return style;
		}

		String matchAlpha = null;
		matcher.matcherClean();
		if (sortkey != null) {
			matchAlpha = sortkey;
		} else {
			return style;
		}
		// Logs.i("=== - " + matchAlpha);
		String[] array = matchAlpha.trim().split("\\|");
		// Logs.e("=== - "+matchAlpha+"="+array[0]+"="+array.length);
		String[] strs = array[0].split("\\*");
		String headSpell = null;
		if (array.length > 1) {
			headSpell = array[1];
		}
		// char[] chAlpha = Unicode2Alpha.toAlpha(matchStr).toCharArray();
		// char[] zh = matchStr.toCharArray();
		if (strs.length > 1) {

			try {
				for (int i = 1; i < strs.length; i++) {
					// Logs.i("== i " + zh[i] + " " + chAlpha[i]);
					if (matcher.match(strs[i], false)) {
						int j = 0;
						if (getHilite(style, matchStr,
								j = matchStr.indexOf(strs[i + 1]), j + 1)) {
							return style;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// if(headSpell!=null){

		matcher.matcherClean();
		matcher.match(matchStr, headSpell != null ? headSpell : sortkey, true);
		if (getHilite(style, matchStr, matcher.getStart(), matcher.getEnd())) {
			matcher.matcherClean();
			return style;
		}
		// }
		return new SpannableStringBuilder(matchStr);
	}

	private boolean getHilite(SpannableStringBuilder style, String matchStr,
			int start, int end) {

		if (start == 0 && end == 0 || start > end) {
			return false;
		}
		style.setSpan(new ForegroundColorSpan(highLightColor), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return true;
	}

}
