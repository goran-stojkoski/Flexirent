package view.flexirent;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * This method displays images chosen by the user for the given rental property.
 * It also provides the user a button to upload the image
 * 
 * @author Goran Stojoski
 * @version v.10
 * @date 20 Oct 2018
 */
public class ImageUploadNode extends StackPane {

	ImageView imageView;
	Image propertyImage;
	Button uploadImageButton;

	/**
	 * This method displays the image of the filename that is passed to it
	 * @param imageFilename
	 */
	public ImageUploadNode(String imageFilename) {
		propertyImage = new Image("file:images/" + imageFilename);
		imageView = new ImageView(propertyImage);
		uploadImageButton = new Button("Upload");
		super.getChildren().addAll(imageView, uploadImageButton);
		setLayout();
	}
	
	/**
	 * This method displays a default image when no image is available
	 */
	public ImageUploadNode() {
		this("NoImageAvailable.jpg");
	}

	public ImageView getPropertyImage() {
		return imageView;
	}

	public Button getUploadImageButton() {
		return uploadImageButton;
	}
	
	public void resetImage() {
		imageView.setImage( new Image("file:images/NoImageAvailable.jpg"));
	}

	private void setLayout() {
		imageView.setPreserveRatio(true);
		super.setMargin(uploadImageButton, new Insets(10));
		super.setAlignment(Pos.BOTTOM_RIGHT);
		super.setAlignment(imageView, Pos.BOTTOM_CENTER);
		imageView.setPreserveRatio(true);
	}

}
