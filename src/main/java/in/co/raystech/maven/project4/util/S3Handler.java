package in.co.raystech.maven.project4.util;

import java.io.InputStream;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import in.co.raystech.maven.project4.exception.ImageSaveException;

public class S3Handler {
	private static Logger log = Logger.getLogger(S3Handler.class);

	public static void main(String[] args) {
		deleteImage("3jpg.jpg");
	}

	public static void uploadProductImage(InputStream file, String imageId, String extention)
			throws ImageSaveException {
		log.debug("uploadProductImage Method Started");
		final AmazonS3 s3 = builder();
		try {
			setCreds();
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType("image/jpeg");
			s3.putObject(getBucketName(), imageId + extention, file, objectMetadata);
		} catch (AmazonServiceException e) {
			log.error(e);
			throw e;
		}
		log.debug("uploadProductImage Method ended");
	}

	public static void deleteImage(String imageId) {
		log.debug("deleteImage Method Started");
		final AmazonS3 s3 = builder();
		try {
			setCreds();
			s3.deleteObject(getBucketName(), imageId);
		} catch (AmazonServiceException e) {
			log.error(e);
			throw e;
		}
		log.debug("deleteImage Method ended");
	}

	public static String getUrl(String imageName) {
		log.debug("getUrl Method Started");
		return "http://d2jkgyfbwf6l29.cloudfront.net/" + imageName + ".jpg";
	}

	private static AmazonS3 builder() {
		log.debug("AmazonS3 builder Method  Started");
		return AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
	}

	private static final ResourceBundle rb = ResourceBundle
			.getBundle("in.co.raystech.maven.project4.bundle.awscredentials");

	private static String getBucketName() {
		return rb.getString("aws.bucketname");

	}

	private static void setCreds() {
		System.setProperty("aws.accessKeyId", rb.getString("aws.accessKeyId"));
		System.setProperty("aws.secretKey", rb.getString("aws.secretKey"));
	}

}
