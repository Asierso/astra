
package com.asierso.astra.extensions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Asierso
 */
public class RegexExtension {

    public static String replaceVarsOld(String text, List<String> args) {
    	//Load pattern "$N"
        Pattern pattern = Pattern.compile("\\$\\d+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        
        //Make replacement
        StringBuilder replace = new StringBuilder();
        while (matcher.find()) {
            int index = Integer.parseInt(matcher.group().substring(1));
            if (index >= 0 && index < args.size()) {
                matcher.appendReplacement(replace, args.get(index));
            }
        }
        matcher.appendTail(replace);
        return replace.toString();
    }
    
    public static String replaceOutput(String text, List<String> args) {
    	System.out.println("Replace " + text);
        return replace(text,"\\$\\{(\\d+)}",args);
    }
    
    public static String replaceParameters(String text, List<String> args) {
        return replace(text,"\\$\\((\\d+)\\)",args);
    }
    
    private static String replace(String text, String regex, List<String> args) {
    	//Load pattern
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        
        //Make replacement
        StringBuilder replace = new StringBuilder();
        while (matcher.find()) {
        	System.out.println("v");
            int index = Integer.parseInt(matcher.group(1));
            if (index >= 0 && index < args.size()) {
                matcher.appendReplacement(replace, args.get(index));
            }
        }
        matcher.appendTail(replace);
        return replace.toString();
    }
}
