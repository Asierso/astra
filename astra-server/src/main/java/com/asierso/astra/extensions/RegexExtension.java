
package com.asierso.astra.extensions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Asierso
 */
public class RegexExtension {
    
    public static String replaceOutput(String text, List<String> args) {
        return replace(text,"\\$\\{(\\d+)}",args); //Make replacement of pattern ${n}
    }
    
    public static String replaceParameters(String text, List<String> args) {
        return replace(text,"\\$\\((\\d+)\\)",args); //Make replacement of pattern $(n)
    }
    
    private static String replace(String text, String regex, List<String> args) {
    	//Load pattern
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        
        //Make replacement
        StringBuilder replace = new StringBuilder();
        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group(1)); //first find index (of find sequence)
            if (index >= 0 && index < args.size()) {
                matcher.appendReplacement(replace, args.get(index));
            } else {
            	throw new IndexOutOfBoundsException("Position " + index + " is out of bounds");
            }
        }
        matcher.appendTail(replace);
        return replace.toString();
    }
}
