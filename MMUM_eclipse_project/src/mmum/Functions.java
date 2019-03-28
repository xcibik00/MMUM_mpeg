/**
 * @author Peter Cibik
 */

package mmum;

import java.util.LinkedList;

import Jama.Matrix;

public class Functions {

	
public static Matrix upsize_matrix (int size, Matrix input_matrix) {
		
		int height = input_matrix.getRowDimension();
		int width = input_matrix.getColumnDimension();
		
		int height_up = height + (2 * size);
		int width_up = width + (2 * size);
		
		double left_up_corner = input_matrix.get(0, 0);
		double right_up_corner = input_matrix.get(0, (width - 1));
		double left_down_corner = input_matrix.get((height - 1), 0);
		double right_down_corner = input_matrix.get((height - 1), (width - 1));
		
		Matrix left_up_corner_matrix = new Matrix(size, size);
		Matrix right_up_corner_matrix = new Matrix(size, size);
		Matrix left_down_corner_matrix = new Matrix(size, size);
		Matrix right_down_corner_matrix = new Matrix(size, size);
		
		Matrix up = new Matrix(size, width);
		Matrix down = new Matrix(size, width);
		Matrix left = new Matrix(width, size);
		Matrix right = new Matrix(width, size);
		
		Matrix tmp_matrix = new Matrix(height_up, width_up);
		
		// set corners matrix
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				left_up_corner_matrix.set(i, j, left_up_corner);
				right_up_corner_matrix.set(i, j, right_up_corner);
				left_down_corner_matrix.set(i, j, left_down_corner);
				right_down_corner_matrix.set(i, j, right_down_corner);
			}
		}
		
		// set up matrix
		for (int j = 0; j < width; j++) {
			for (int i = 0; i < size; i++) {
				up.set(i, j, input_matrix.get(0,j));
			}
		}
		
		// set down matrix
		for (int j = 0; j < width; j++) {
			for (int i = 0; i < size; i++) {
				down.set(i, j, input_matrix.get((height - 1),j));
			}
		}
		
		// set left matrix
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < size; j++) {
				left.set(i, j, input_matrix.get(i,0));
			}
		}
		
		// set right matrix
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < size; j++) {
				right.set(i, j, input_matrix.get(i,(width - 1)));
			}
		}
				
		// set left up corner
		tmp_matrix.setMatrix(0, (size - 1), 0, (size - 1), left_up_corner_matrix);
		// set left down corner
		tmp_matrix.setMatrix((height_up - size), (height_up - 1), 0, (size - 1), left_down_corner_matrix);
		// set right up corner
		tmp_matrix.setMatrix(0, (size - 1), (width_up - size), (width_up - 1), right_up_corner_matrix);
		// set right down corner
		tmp_matrix.setMatrix((height_up - size), (height_up - 1), (width_up - size), (width_up - 1), right_down_corner_matrix);
		// set left side
		tmp_matrix.setMatrix(size, (height_up - size - 1), 0, (size - 1), left);
		// set right side
		tmp_matrix.setMatrix(size, (height_up - size - 1), (width_up - size), (width_up - 1), right);
		// set up side
		tmp_matrix.setMatrix(0, (size - 1), size, (width_up - size - 1), up);
		// set down side
		tmp_matrix.setMatrix((height_up - size), (height_up - 1), size, (width_up - size - 1), down);
		// set base
		tmp_matrix.setMatrix(size, (height_up - size - 1), size, (width_up - size - 1), input_matrix);
		
		return tmp_matrix;
	}
	
	// fce for printing matrix in readable form
	public static void print_matrix(Matrix input_matrix) {
		int height = input_matrix.getRowDimension();
		int width = input_matrix.getColumnDimension();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(input_matrix.get(i, j) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	// fce which compute SAD
	public static double SAD(Matrix input_matrix_a, Matrix input_matrix_b) {
		int height = input_matrix_a.getRowDimension();
		int width = input_matrix_a.getColumnDimension();
		double tmp = 0.0;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tmp+= Math.abs(input_matrix_a.get(i, j) - input_matrix_b.get(i, j));
			}
		}
		
		return tmp;
	}
	
	public static Matrix DPCM(Matrix input_matrix_a, Matrix input_matrix_b) {
		int height = input_matrix_a.getRowDimension();
		int width = input_matrix_a.getColumnDimension();
		
		Matrix tmp_matrix = new Matrix(height, width);
		
		double tmp_value = 0;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tmp_value = (((input_matrix_a.get(i, j) + 255) - input_matrix_b.get(i, j)) / 2);
				tmp_matrix.set(i, j, tmp_value); 
			}
		}
		return tmp_matrix;
	}
	
}
