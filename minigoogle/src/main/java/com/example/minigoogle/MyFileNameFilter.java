package com.example.minigoogle;

import java.io.File;
import java.io.FilenameFilter;

public class MyFileNameFilter implements FilenameFilter {

    String initials;
    public MyFileNameFilter(String initials){
        this.initials = initials;
    }

    @Override
    public boolean accept(File dir, String name) {
        return name.startsWith(initials) && name.endsWith(".txt");
    }
}
