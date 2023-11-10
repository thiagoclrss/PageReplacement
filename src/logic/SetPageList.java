package logic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetPageList {
    public static String pageFile;
    public static ArrayList<Page> pages = new ArrayList<>();
    public SetPageList(String pages) {
        this.pageFile = pages;
    }
    //ArrayList<String> pageList = new ArrayList<>();
    public static String[] pageList;

    public static void setPage (){
        pageList = pageFile.split("-");

        for(int i = 0; i < pageList.length; i++){
            if(pageList[i].contains("R")){
                pageList[i] = pageList[i].replace("R", "");
                pages.add(new Page(pageList[i], "R"));

            } else {
                pageList[i] = pageList[i].replace("W", "");
                pages.add(new Page(pageList[i], "W"));
            }
        }
        System.out.println(pages);
    }
}
