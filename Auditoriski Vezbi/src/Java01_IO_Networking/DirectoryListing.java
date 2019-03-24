package Java01_IO_Networking;

import java.io.*;
import java.util.*;

public class DirectoryListing {
    public static void main(String[] args){
        File path = new File("C:\\Users\\Stefa\\Desktop\\store");
        List<String> list = new ArrayList<>();

//        if(path.exists()){
//            File[] subFiles = path.listFiles();
//            for(File f : subFiles){
//                list.add(f.getName());
//            }
//        }

       System.out.println(path);
    }
}
