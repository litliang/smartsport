package app.base.ui.highlight;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class KeyMatcher {
	
	private static boolean isRegex = false;
	private static boolean isFuzzy = false;
	
	private Pattern enPattern;
    private Pattern zhPattern;
	private int start;
	private int end;
	
	private boolean isMatchAlpha;
	
	public KeyMatcher(String regex)
	{	
		enPattern = toRegex(regex,true);
        zhPattern = toRegex(regex,false);
	}
	
	public KeyMatcher()
	{	
		enPattern = null;
	}

	public boolean match(String matchStr, String matchAlpha,boolean en)
	{
		if(match(matchStr,en))
		{
			return true;
		}
		else
		{
			if(match(matchAlpha,en))
			{
				return true;
			}
		}
		return false;
	}
    Pattern pattern;
    Matcher matcher;
	public boolean match(String matchStr,boolean en)
	{

        pattern = en?enPattern:zhPattern;
		if(matcher==null){
			
        matcher = pattern.matcher(matchStr);
		}
		if(matcher.find()) 
		{
			start = matcher.start();
			end = matcher.end();
			
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
	
	public void matcherClean()
	{
		this.start = 0;
		this.end = 0;
		matcher = null;
	}

	private static Pattern toRegex(String key,boolean en)
	{
		Pattern pattern;
		String patternKey;
		if(isRegex || key.startsWith("re:")) 
		{
			if(key.startsWith("re:")) 
			{
				patternKey = key.substring(3, key.length());
			} 
			else 
			{
				patternKey = key;
			}				
			if(!en && !patternKey.startsWith("^"))
			{
				pattern = Pattern.compile("^" + patternKey, Pattern.CASE_INSENSITIVE);
			} 
			else 
			{
				pattern = Pattern.compile(patternKey, Pattern.CASE_INSENSITIVE);
			}
		} 
		else 
		{
			patternKey = key.replace("\\", "\\u005C")
							.replace(".", "\\u002E")
							.replace("$", "\\u0024")
							.replace("^", "\\u005E")
							.replace("{", "\\u007B")
							.replace("[", "\\u005B")
							.replace("(", "\\u0028")
							.replace(")", "\\u0029")
							.replace("+", "\\u002B")
							.replace("*", "[\\s\\S]*")
							.replace("?", "[\\s\\S]");
			if(en)
			{
				pattern = Pattern.compile(patternKey, Pattern.CASE_INSENSITIVE);
			} 
			else 
			{
				pattern = Pattern.compile("^" + patternKey, Pattern.CASE_INSENSITIVE);
			}
		}
		return pattern;
	}

	public boolean isMatchAlpha() {
		return isMatchAlpha;
	}

	public void setMatchAlpha(boolean isMatchAlpha) {
		this.isMatchAlpha = isMatchAlpha;
	}
	
}
