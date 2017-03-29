package main.java.dragon.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import main.java.dragon.dao.ImageDao;
import main.java.dragon.pojo.Image;
import main.java.dragon.service.ImageService;

@Service
@Transactional
public class ImageServiceImpl extends ConnectionUtil implements ImageService{
	@Autowired
	private ImageDao imageDao;

	public ImageServiceImpl() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addImage(Image image) {
		// TODO Auto-generated method stub
		imageDao.insertImage(image);
	}

	@Override
	public void updateImage(Image image) {
		// TODO Auto-generated method stub
		imageDao.saveImage(image);
	}

	@Override
	public List<Image> getAllImages() {
		// TODO Auto-generated method stub
		return imageDao.selectAllImage();
	}

	@Override
	public Image getImageById(String id) {
		// TODO Auto-generated method stub
		return imageDao.selectImageById(id);
	}

}
