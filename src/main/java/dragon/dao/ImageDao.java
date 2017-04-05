package main.java.dragon.dao;

import java.util.List;

import main.java.dragon.pojo.Image;

public interface ImageDao {
	public void insertImage(Image image);
	public void saveImage(Image image);
	public List<Image> selectAllImage();
	public Image selectImageById(String id);
	public List<Image> selectImageByCondition(String imageName, int status, String imageOsType);
	public void deleteImage(String id);
	public List<Image> selectImageByName(String name);
}
