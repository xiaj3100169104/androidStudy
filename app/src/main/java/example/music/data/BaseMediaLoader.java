package example.music.data;

import java.util.ArrayList;

import example.music.entity.MediaFolder;


public class BaseMediaLoader {

    public String getParent(String path) {
        String sp[]=path.split("/");
        return sp[sp.length-2];
    }

    public int hasDir(ArrayList<MediaFolder> folders, String dirName){
        for(int i = 0; i< folders.size(); i++){
            MediaFolder folder = folders.get(i);
            if( folder.name.equals(dirName)) {
                return i;
            }
        }
        return -1;
    }


}
