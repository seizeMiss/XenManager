package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.Image;

public interface ImageService {
	public void addImage(Image image);
	public void updateImage(Image image);
	public List<Image> getAllImages();
	public Image getImageById(String id);

}
