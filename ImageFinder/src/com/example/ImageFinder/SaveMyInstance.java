package com.example.ImageFinder;

import java.util.ArrayList;

public class SaveMyInstance {
    public ImageBase imageBase;
    public ArrayList<Image> images;

    SaveMyInstance(ImageBase _imageBase,ArrayList<Image> _images){
        imageBase = _imageBase;
        images = _images;
    }
}
