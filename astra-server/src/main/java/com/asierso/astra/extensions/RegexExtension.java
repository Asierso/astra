
package com.asierso.astra.extensions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Asierso
 */
public class RegexExtension {

    public static String replaceVars(String text, List<String> args) {
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
}
