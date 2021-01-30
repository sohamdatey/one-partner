package in.co.raystech.maven.project4.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import in.co.raystech.maven.project4.exception.ImageSaveException;

public class S3Handler {
	public static void uploadProductImage(InputStream file, String imageId, String extention)
			throws ImageSaveException {
		final AmazonS3 s3 = builder();
		try {
			setCreds();
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType("image/jpeg");
			s3.putObject(getBucketName(), imageId + extention, file, objectMetadata);
		} catch (AmazonServiceException e) {
			throw e;
		}
	}

	public static void deleteImage(String imageId) {
		final AmazonS3 s3 = builder();
		try {
			setCreds();
			s3.deleteObject(getBucketName(), imageId);
		} catch (AmazonServiceException e) {
			throw e;
		}
	}

	public static String getUrl(String imageName) {
		return "http://d2jkgyfbwf6l29.cloudfront.net/"+imageName+".jpg"; 
	}

	private static AmazonS3 builder() {
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
