package main.java.dragon.service;

import java.util.List;

import main.java.dragon.pojo.Image;

public interface ImageService {
	public boolean addImage(Image image);
	public void updateImage(Image image);
	public List<Image> getAllImages();
	public Image getImageById(String id);
	public List<Image> getImagesByCondition(String imageName,String imageStatus,String imageOsType);
	public boolean deleteImages(String ids);
	public List<Image> getImagesByName(String name);
}
