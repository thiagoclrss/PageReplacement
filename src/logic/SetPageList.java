package logic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPageList {
    public static String pages;
    public SetPageList(String pages) {
        this.pages = pages;
    }

    public static Pattern pattern = Pattern.compile("-(.*?)-");
    public static Matcher matcher = pattern.matcher(pages);




}
