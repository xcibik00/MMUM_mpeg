package mmum;

import java.net.URL;
import java.util.ResourceBundle;

import Jama.Matrix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import ij.ImagePlus;

public class Process implements Initializable {
	public static final int RED = 1;
	public static final int GREEN = 2;
	public static final int BLUE = 3;
	public static final int Y = 4;
	public static final int CB = 5;
	public static final int CR = 6;
	public static final int S444 = 7;
	public static final int S422 = 8;
	public static final int S420 = 9;
	public static final int S411 = 10;
	private int vzorkovani = S444;

	private Matrix quantizationMatrix8Y;
	private Matrix quantizationMatrix8C;

	private Quality quality;

	private ImagePlus imagePlus;
	private ColorTransform colorTransform;
	private ColorTransform colorTransformOrig;

	@FXML
	private Button redButton;
	@FXML
	private Button greenButton;
	@FXML
	private Button blueButton;
	@FXML
	private Button yButton;
	@FXML
	private Button cbButton;
	@FXML
	private Button crButton;
	@FXML
	private Label psnrLabel;
	@FXML
	private Label mseLabel;
	@FXML
	private Slider qualitySlider;

	@FXML
	private RadioButton radioButton8x8;
	@FXML
	private RadioButton radioButton4x4;
	@FXML
	private RadioButton radioButton2x2;

	final ToggleGroup transformBlockSizeGroup = new ToggleGroup();
	private RadioButton selectedTransformBlockSizeRadioButton;

	@FXML
	private RadioButton radioButtonDCT;
	@FXML
	private RadioButton radioButtonWHT;
	final ToggleGroup transformTypeGroup = new ToggleGroup();
	private RadioButton selectedTransformRadioButton;



	public void test1() {
		imagePlus.show("Original Image");
		colorTransform.getRGB();
		colorTransform.convertRgbToYcbcr();
		colorTransform.convertYcbcrToRgb();
		colorTransform.setImageFromRGB(colorTransform.getRed().length,
				colorTransform.getRed()[0].length, colorTransform.getRed(),
				colorTransform.getGreen(), colorTransform.getBlue()).show(
				"Transformed Image");

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		radioButton2x2.setToggleGroup(transformBlockSizeGroup);
		radioButton4x4.setToggleGroup(transformBlockSizeGroup);
		radioButton8x8.setToggleGroup(transformBlockSizeGroup);

		radioButtonDCT.setToggleGroup(transformTypeGroup);
		radioButtonWHT.setToggleGroup(transformTypeGroup);

		nactiOrigObraz();
		this.colorTransform.convertRgbToYcbcr();
		imagePlus.setTitle("Original Image");
		imagePlus.show("Original Image");

	}

	public void nactiOrigObraz() {
		this.imagePlus = new ImagePlus("img/lena_std.jpg");
		this.colorTransformOrig = new ColorTransform(
				imagePlus.getBufferedImage());
		this.colorTransform = new ColorTransform(imagePlus.getBufferedImage());
		this.colorTransform.getRGB();
		colorTransformOrig.getRGB();
	}

	public ImagePlus getComponent(int component) {
		ImagePlus imagePlus = null;
		switch (component) {
		case RED:
			imagePlus = colorTransform.setImageFromRGB(
					colorTransform.getImageWidth(),
					colorTransform.getImageHeight(), colorTransform.getRed(),
					"RED");
			break;
		// podobn� vytvo�te case pro GREEN a BLUE
		case GREEN:
			imagePlus = colorTransform.setImageFromRGB(
					colorTransform.getImageWidth(),
					colorTransform.getImageHeight(), colorTransform.getGreen(),
					"GREEN");
			break;
		case BLUE:
			imagePlus = colorTransform.setImageFromRGB(
					colorTransform.getImageWidth(),
					colorTransform.getImageHeight(), colorTransform.getBlue(),
					"BLUE");
			break;
		case Y:
			imagePlus = colorTransform.setImageFromRGB(colorTransform.getY()
					.getColumnDimension(), colorTransform.getY()
					.getRowDimension(), colorTransform.getY(), "Y");
			break;
		case CB:
			imagePlus = colorTransform.setImageFromRGB(colorTransform.getcB()
					.getColumnDimension(), colorTransform.getcB()
					.getRowDimension(), colorTransform.getcB(), "Cb");
			break;
		case CR:
			imagePlus = colorTransform.setImageFromRGB(colorTransform.getcR()
					.getColumnDimension(), colorTransform.getcR()
					.getRowDimension(), colorTransform.getcR(), "Cr");
		default:
			break;
		}
		return imagePlus;
	}

	public void downsample(int downsampleType) {
		colorTransform.convertRgbToYcbcr();
		Matrix cB = new Matrix(colorTransform.getcB().getArray());
		Matrix cR = new Matrix(colorTransform.getcR().getArray());
		switch (downsampleType) {
		case S444:
			break;
		case S422:
			cB = colorTransform.downsample(cB);
			colorTransform.setcB(cB);

			cR = colorTransform.downsample(cR);
			colorTransform.setcR(cR);
			break;

		case S420:
			cB = colorTransform.downsample(cB);
			cB = cB.transpose();
			cB = colorTransform.downsample(cB);
			cB = cB.transpose();
			colorTransform.setcB(cB);

			cR = colorTransform.downsample(cR);
			cR = cR.transpose();
			cR = colorTransform.downsample(cR);
			cR = cR.transpose();
			colorTransform.setcR(cR);
			break;

		case S411:
			cB = colorTransform.downsample(cB);
			colorTransform.setcB(cB);
			cB = new Matrix(colorTransform.getcB().getArray());
			cB = colorTransform.downsample(cB);
			colorTransform.setcB(cB);

			cR = colorTransform.downsample(cR);
			colorTransform.setcR(cR);
			cR = new Matrix(colorTransform.getcR().getArray());
			cR = colorTransform.downsample(cR);
			colorTransform.setcR(cR);
			break;
		}
	}

	public void oversample(int oversample) {
		Matrix cB;
		Matrix cR;
		switch (oversample) {
		case S444:

			break;
		case S422:
			cB = new Matrix(colorTransform.getcB().getArray());
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);
			cR = new Matrix(colorTransform.getcR().getArray());
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			break;

		case S420:
			cB = new Matrix(colorTransform.getcB().getArray()).transpose();
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);
			cB = new Matrix(colorTransform.getcB().getArray()).transpose();
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);

			cR = new Matrix(colorTransform.getcR().getArray()).transpose();
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			cR = new Matrix(colorTransform.getcR().getArray()).transpose();
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			break;

		case S411:
			cB = new Matrix(colorTransform.getcB().getArray());
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);
			cB = new Matrix(colorTransform.getcB().getArray());
			cB = colorTransform.oversample(cB);
			colorTransform.setcB(cB);

			cR = new Matrix(colorTransform.getcR().getArray());
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			cR = new Matrix(colorTransform.getcR().getArray());
			cR = colorTransform.oversample(cR);
			colorTransform.setcR(cR);
			break;

		default:
			break;
		}
		colorTransform.convertYcbcrToRgb();
	}

	public double getMse() {
		colorTransform.convertYcbcrToRgb();
		quality = new Quality();
		double a = quality.getMse(colorTransformOrig.getRed(),
				colorTransform.getRed());
		double b = quality.getMse(colorTransformOrig.getGreen(),
				colorTransform.getGreen());
		double c = quality.getMse(colorTransformOrig.getBlue(),
				colorTransform.getBlue());
		return ((a + b + c) / 3.0);

	}

	public double getPsnr() {
		colorTransform.convertYcbcrToRgb();
		quality = new Quality();
		double result = (quality.getPsnr(colorTransformOrig.getRed(),
				colorTransform.getRed())+quality.getPsnr(colorTransformOrig.getGreen(),
						colorTransform.getGreen())+quality.getPsnr(colorTransformOrig.getBlue(),
								colorTransform.getBlue()))/3.0;
		return result;
	}

	// Ovl�d�n� grafick�ch komponent
	public void yButtonPressed(ActionEvent event) {
		getComponent(Process.Y).show("Y Component");
	}

	public void cbButtonPressed(ActionEvent event) {
		getComponent(Process.CB).show("Cb Component");
	}

	public void crButtonPressed(ActionEvent event) {
		getComponent(Process.CR).show("Cr Component");
	}

	public void rButtonPressed(ActionEvent event) {
		getComponent(Process.RED).show("Red Component");
	}

	public void gButtonPressed(ActionEvent event) {
		getComponent(Process.GREEN).show("Green Component");
	}

	public void bButtonPressed(ActionEvent event) {
		getComponent(Process.BLUE).show("Blue Component");
	}

	public void dS444ButtonPressed(ActionEvent event) {
		nactiOrigObraz();
		vzorkovani = S444;
		downsample(S444);
	}

	public void dS422ButtonPressed(ActionEvent event) {
		nactiOrigObraz();
		vzorkovani = S422;
		downsample(S422);
	}

	public void dS420ButtonPressed(ActionEvent event) {
		nactiOrigObraz();
		vzorkovani = S420;
		downsample(S420);
	}

	public void dS411ButtonPressed(ActionEvent event) {
		nactiOrigObraz();
		vzorkovani = S411;
		downsample(S411);
	}

	public void overSampleButtonPressed(ActionEvent event) {
		oversample(vzorkovani);
	}

	public void qualityButtonPressed(ActionEvent event) {
		psnrLabel.setText("PSNR = " + getPsnr());
		mseLabel.setText("MSE = " + getMse());
	}


	private void kvantuj (Matrix original, Matrix quant) {


	}



	/*public void kvantuj(ActionEvent event) {
		this.quantizationMatrix8Y = new Matrix(
				colorTransform.getQuantizationMatrix8Y());
		this.quantizationMatrix8C = new Matrix(
				colorTransform.getQuantizationMatrix8C());
		double alfa = 0;
		double quality = (int) qualitySlider.getValue();
		System.out.println(quality);
		if (quality < 50) {
			alfa = 50 / quality;
			this.quantizationMatrix8Y.timesEquals(alfa);
			this.quantizationMatrix8C.timesEquals(alfa);
		} else if (quality >= 50 && quality < 99) {

			alfa = 2 - 2 * quality / 100.0;
			this.quantizationMatrix8Y.timesEquals(alfa);
			this.quantizationMatrix8C.timesEquals(alfa);
		} else if (quality == 100) {
			this.quantizationMatrix8Y = new Matrix(8, 8, 1);
			this.quantizationMatrix8C = new Matrix(8, 8, 1);
		}

		Matrix yPom = new Matrix(512, 512);
		Matrix cbPom = new Matrix(512, 512);
		Matrix crPom = new Matrix(512, 512);

		for (int i = 0; i < colorTransform.getImageHeight() - 1; i = i + 8) {
			for (int j = 0; j < colorTransform.getImageWidth() - 1; j = j + 8) {
				yPom.setMatrix(i, i + 7, j, j + 7, (colorTransform.kvantizace(
						8, colorTransform.getY().getMatrix(i, i + 7, j, j + 7),
						this.quantizationMatrix8Y.getArray())));

				cbPom.setMatrix(i, i + 7, j, j + 7, (colorTransform.kvantizace(
						8,
						colorTransform.getcB().getMatrix(i, i + 7, j, j + 7),
						this.quantizationMatrix8C.getArray())));
				crPom.setMatrix(i, i + 7, j, j + 7, (colorTransform.kvantizace(
						8,
						colorTransform.getcR().getMatrix(i, i + 7, j, j + 7),
						this.quantizationMatrix8C.getArray())));
			}
		}
		colorTransform.setY(yPom);
		colorTransform.setcB(cbPom);
		colorTransform.setcR(crPom);
		// colorTransform.getY().print(2, 2);
	}

	public void iKvantuj(ActionEvent event) {
		Matrix yPom = new Matrix(512, 512);
		Matrix cbPom = new Matrix(512, 512);
		Matrix crPom = new Matrix(512, 512);

		for (int i = 0; i < colorTransform.getImageHeight() - 1; i = i + 8) {
			for (int j = 0; j < colorTransform.getImageWidth() - 1; j = j + 8) {
				// System.out.println(i + " , " + j);
				yPom.setMatrix(i, i + 7, j, j + 7, (colorTransform.iKvantizace(
						8, colorTransform.getY().getMatrix(i, i + 7, j, j + 7),
						this.quantizationMatrix8Y.getArray())));
				cbPom.setMatrix(i, i + 7, j, j + 7, (colorTransform
						.iKvantizace(
								8,
								colorTransform.getcB().getMatrix(i, i + 7, j,
										j + 7),
								this.quantizationMatrix8C.getArray())));
				crPom.setMatrix(i, i + 7, j, j + 7, (colorTransform
						.iKvantizace(
								8,
								colorTransform.getcR().getMatrix(i, i + 7, j,
										j + 7),
								this.quantizationMatrix8C.getArray())));
			}
		}
		colorTransform.setY(yPom);
		colorTransform.setcB(cbPom);
		colorTransform.setcR(crPom);
		// colorTransform.getY().print(2, 2);
	}*/

	public void showResult() {
		colorTransform.convertYcbcrToRgb();
		colorTransform.setImageFromRGB(colorTransform.getRed().length,
				colorTransform.getRed()[0].length, colorTransform.getRed(),
				colorTransform.getGreen(), colorTransform.getBlue()).show(
				"Transformed Image");
	}


	public void transformAndQuantize(ActionEvent event) {

		//vyber velikosti bloku pro transformaci
		int blockSize = 0;
		selectedTransformBlockSizeRadioButton = (RadioButton) transformBlockSizeGroup.getSelectedToggle();

		if (selectedTransformBlockSizeRadioButton.getText().equalsIgnoreCase("8x8"))
			blockSize = 8;
		if (selectedTransformBlockSizeRadioButton.getText().equalsIgnoreCase("4x4"))
			blockSize = 4;
		if (selectedTransformBlockSizeRadioButton.getText().equalsIgnoreCase("2x2"))
			blockSize = 2;

		//vyber transformace
		Matrix dct = null;
		selectedTransformRadioButton = (RadioButton) transformTypeGroup.getSelectedToggle();
		if (selectedTransformRadioButton.getText().equalsIgnoreCase("dct"))
			dct = new TransformMatrix().getDctMatrix(blockSize);
		if (selectedTransformRadioButton.getText().equalsIgnoreCase("wht"))
			dct = new TransformMatrix().getWhtMatrix(blockSize);

		//nastaven� kvantiza�n�ch matic
		this.quantizationMatrix8Y = new Matrix(
				colorTransform.getQuantizationMatrix8Y());
		this.quantizationMatrix8C = new Matrix(
				colorTransform.getQuantizationMatrix8C());
		double alfa = 0;
		double quality = (int) qualitySlider.getValue();
		System.out.println(quality);
		if (quality < 50) {
			alfa = 50.0 / quality;
			this.quantizationMatrix8Y.timesEquals(alfa);
			this.quantizationMatrix8C.timesEquals(alfa);
		} else if (quality >= 50 && quality < 99) {

			alfa = 2 - (2 * quality) / 100.0;
			this.quantizationMatrix8Y.timesEquals(alfa);
			this.quantizationMatrix8C.timesEquals(alfa);
		} else if (quality == 100) {
			this.quantizationMatrix8Y = new Matrix(8, 8, 1);
			this.quantizationMatrix8C = new Matrix(8, 8, 1);
		}



		//transformace
		Matrix yPom = new Matrix(colorTransform.getY().getRowDimension(), colorTransform.getY().getColumnDimension());
		Matrix cbPom = new Matrix(colorTransform.getcB().getRowDimension(), colorTransform.getcB().getColumnDimension());
		Matrix crPom = new Matrix(colorTransform.getcR().getRowDimension(), colorTransform.getcR().getColumnDimension());

		for (int i = 0; i < colorTransform.getY().getRowDimension() - 1; i = i + blockSize) {
			for (int j = 0; j < colorTransform.getY().getColumnDimension() - 1; j = j + blockSize) {

				yPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1,
						colorTransform.kvantizace(blockSize, colorTransform.transform(blockSize,dct, colorTransform.getY().getMatrix(i, i + blockSize-1, j, j + blockSize-1)), this.quantizationMatrix8Y));


			}
		}


		for (int i = 0; i < colorTransform.getcB().getRowDimension() - 1; i = i + blockSize) {
			for (int j = 0; j < colorTransform.getcB().getColumnDimension() - 1; j = j + blockSize) {
				//cbPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1, colorTransform.transform(blockSize,dct, colorTransform.getcB().getMatrix(i, i + blockSize-1, j, j + blockSize-1)));
				//crPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1, colorTransform.transform(blockSize,dct, colorTransform.getcR().getMatrix(i, i + blockSize-1, j, j + blockSize-1)));
				cbPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1,
						colorTransform.kvantizace(blockSize, colorTransform.transform(blockSize,dct, colorTransform.getcB().getMatrix(i, i + blockSize-1, j, j + blockSize-1)), this.quantizationMatrix8C));
				crPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1,
						colorTransform.kvantizace(blockSize, colorTransform.transform(blockSize,dct, colorTransform.getcR().getMatrix(i, i + blockSize-1, j, j + blockSize-1)), this.quantizationMatrix8C));
			}
		}

		colorTransform.setY(yPom);
		colorTransform.setcB(cbPom);
		colorTransform.setcR(crPom);

	}


	public void iTransformAndIQuantize(ActionEvent event) {

		//vyber velikosti bloku pro transformaci
		int blockSize = 0;
		selectedTransformBlockSizeRadioButton = (RadioButton) transformBlockSizeGroup.getSelectedToggle();

		if (selectedTransformBlockSizeRadioButton.getText().equalsIgnoreCase("8x8"))
			blockSize = 8;
		if (selectedTransformBlockSizeRadioButton.getText().equalsIgnoreCase("4x4"))
			blockSize = 4;
		if (selectedTransformBlockSizeRadioButton.getText().equalsIgnoreCase("2x2"))
			blockSize = 2;


		//vyber transformace
		Matrix dct = null;
		if (selectedTransformRadioButton.getText().equalsIgnoreCase("dct"))
			dct = new TransformMatrix().getDctMatrix(blockSize);
		if (selectedTransformRadioButton.getText().equalsIgnoreCase("wht"))
			dct = new TransformMatrix().getWhtMatrix(blockSize);

		//transformace
		Matrix yPom = new Matrix(colorTransform.getY().getRowDimension(), colorTransform.getY().getColumnDimension());
		Matrix cbPom = new Matrix(colorTransform.getcB().getRowDimension(), colorTransform.getcB().getColumnDimension());
		Matrix crPom = new Matrix(colorTransform.getcR().getRowDimension(), colorTransform.getcR().getColumnDimension());

		for (int i = 0; i < colorTransform.getY().getRowDimension() - 1; i = i + blockSize) {
			for (int j = 0; j < colorTransform.getY().getColumnDimension() - 1; j = j + blockSize) {


				yPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1, colorTransform.inverseTransform(blockSize,dct, colorTransform.iKvantizace(blockSize, colorTransform.getY().getMatrix(i, i + blockSize-1, j, j + blockSize-1), this.quantizationMatrix8Y)));
				//yPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1,
//						colorTransform.iKvantizace(blockSize, colorTransform.inverseTransform(blockSize,dct, colorTransform.getY().getMatrix(i, i + blockSize-1, j, j + blockSize-1)), this.quantizationMatrix8Y));
			}
		}

		for (int i = 0; i < colorTransform.getcB().getRowDimension() - 1; i = i + blockSize) {
			for (int j = 0; j < colorTransform.getcB().getColumnDimension() - 1; j = j + blockSize) {
				//cbPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1, colorTransform.inverseTransform(blockSize,dct, colorTransform.getcB().getMatrix(i, i + blockSize-1, j, j + blockSize-1)));
				//crPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1, colorTransform.inverseTransform(blockSize,dct, colorTransform.getcR().getMatrix(i, i + blockSize-1, j, j + blockSize-1)));
				cbPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1, colorTransform.inverseTransform(blockSize,dct, colorTransform.iKvantizace(blockSize, colorTransform.getcB().getMatrix(i, i + blockSize-1, j, j + blockSize-1), this.quantizationMatrix8C)));
				crPom.setMatrix(i, i + blockSize-1, j, j + blockSize-1, colorTransform.inverseTransform(blockSize,dct, colorTransform.iKvantizace(blockSize, colorTransform.getcR().getMatrix(i, i + blockSize-1, j, j + blockSize-1), this.quantizationMatrix8C)));

			}
		}

		colorTransform.setY(yPom);
		colorTransform.setcB(cbPom);
		colorTransform.setcR(crPom);
	}




}