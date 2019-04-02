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
	
	public static Matrix DPCM(Matrix input_matrix_1, Matrix input_matrix_2) {
		int height = input_matrix_1.getRowDimension();
		int width = input_matrix_1.getColumnDimension();
		
//		System.out.println("DPCM:");
		
		Matrix tmp_matrix = new Matrix(height, width);
		
		double tmp_value = 0;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				tmp_value = (((input_matrix_2.get(i, j) + 255) - input_matrix_1.get(i, j)) / 2);
				tmp_matrix.set(i, j, tmp_value); 
//				System.out.print(tmp_value + " ");
				
			}
//			System.out.println();
		}
		return tmp_matrix;
	}
	
public static LinkedList<int[]> FULL_search_vectors (Matrix input_matrix_1, Matrix input_matrix_2, int macroblok_size) {
		
		int height = input_matrix_2.getRowDimension();
		int width = input_matrix_2.getColumnDimension();
		
		int start_height;
		int start_width;
		
		LinkedList<int[]> tmp_list = new LinkedList<int[]>();
		
		Matrix upsized_matrix_a = upsize_matrix((macroblok_size / 2), input_matrix_1);
		
		Matrix macroblok;
		
		double tmp_SAD;
		double tmp_SAD_block = 0;
		
		int height_v = 0;
		int width_v = 0;
		
		for (int i = 0; i < (height / macroblok_size); i++) {
			
			for (int j = 0; j < (width / macroblok_size); j++) {
				
				tmp_SAD = 100000.0;
				
				macroblok = input_matrix_2.getMatrix((i * macroblok_size), (((i + 1) * macroblok_size) - 1), (j * macroblok_size), (((j + 1) * macroblok_size) - 1));
				
				start_height = i * macroblok_size;
				start_width = j * macroblok_size;
				
				for (int m = 0; m <= macroblok_size; m++) {
					for (int n = 0; n <= macroblok_size; n++) {
						
						tmp_SAD_block = SAD(macroblok, upsized_matrix_a.getMatrix((start_height + m), ((start_height + m + macroblok_size) - 1), (start_width + n), ((start_width + n + macroblok_size) - 1) ));
						
						if(tmp_SAD > tmp_SAD_block) {
							tmp_SAD = tmp_SAD_block;
							height_v = start_height + m;
							width_v = start_width + n;
						}
						
					}
				}
				
//				System.out.print("( " + height_v + " , " + width_v + " ) ; ");
				tmp_list.add(new int[] {height_v, width_v});
				
			}
			
//			System.out.println();
			
		}
		
		return tmp_list;
	}

	public static Matrix Full_search_chyba (LinkedList<int[]> vectors, Matrix input_matrix_1, int macroblok_size, Matrix input_matrix_2) {
		
		int height = input_matrix_2.getRowDimension();
		int width = input_matrix_2.getColumnDimension();
		
		Matrix tmp_matrix = new Matrix(height, width);
		
		Matrix matrix_2_obnovena = FULL_search_i(vectors, input_matrix_1, macroblok_size);
		
		tmp_matrix = input_matrix_2.minus(matrix_2_obnovena);
		
		return tmp_matrix;
	}

	public static Matrix FULL_search_i(LinkedList<int[]> vectors, Matrix input_matrix_1, int macroblok_size) {
		
		int height = input_matrix_1.getRowDimension();
		int width = input_matrix_1.getColumnDimension();
		
		int height_v;
		int width_v;
		
		int[] tmp_pole;
		
		Matrix tmp_matrix = new Matrix(height, width);
		Matrix upsized_matrix_b;
		
		for (int i = 0; i < (height / macroblok_size); i++) {
			
			for (int j = 0; j < (width / macroblok_size); j++) {
				
				Matrix macroblok;
				
				tmp_pole = vectors.poll();
				height_v = tmp_pole[0];
				width_v	= tmp_pole[1];
				
				upsized_matrix_b = upsize_matrix((macroblok_size / 2), input_matrix_1);
				
				macroblok = upsized_matrix_b.getMatrix(height_v, ((height_v + macroblok_size) - 1), width_v, ((width_v + macroblok_size) - 1));
				
				tmp_matrix.setMatrix((i * macroblok_size), (((i + 1) * macroblok_size) - 1), (j * macroblok_size), (((j + 1) * macroblok_size) - 1), macroblok);
			}
		}
		
		
		return tmp_matrix;
		
	}
	
	
	public static Matrix Full_search_chyba_i(Matrix matrix_2_obnovena, Matrix chyba) {
		
		int height = matrix_2_obnovena.getRowDimension();
		int width = matrix_2_obnovena.getColumnDimension();
		
		Matrix tmp_matrix = new Matrix(height, width);
		
		tmp_matrix = matrix_2_obnovena.plus(chyba);
		
		return tmp_matrix;
	}
	
public static LinkedList<int[]> N_step_search_vectors (Matrix input_matrix_1, Matrix input_matrix_2, int macroblok_size, int n) {
		
		int height = input_matrix_2.getRowDimension();
		int width = input_matrix_2.getColumnDimension();
		
		int step = (int) Math.pow(2,(n - 1));

		int start_height = macroblok_size - (macroblok_size / 2) - step;
		int start_width = macroblok_size - (macroblok_size / 2) - step;
		
		LinkedList<int[]> tmp_list = new LinkedList<int[]>();
		
		Matrix upsized_matrix_a = upsize_matrix((macroblok_size / 2), input_matrix_1);
		
		Matrix macroblok;
		
		int tmp_height;
		int tmp_width;

		double tmp_SAD;
		double tmp_SAD_block = 0;
		
		int height_v = 0;
		int width_v = 0;
		
		for (int i = 0; i < (height / macroblok_size); i++) {
			
			for (int j = 0; j < (width / macroblok_size); j++) {
				
				tmp_SAD = 100000.0;
				
				macroblok = input_matrix_2.getMatrix((i * macroblok_size), (((i + 1) * macroblok_size) - 1), (j * macroblok_size), (((j + 1) * macroblok_size) - 1));
				
				start_height = (i + 1) * macroblok_size;
				start_width = (j + 1) * macroblok_size;

				for (int m = 0; m < n; m++){

					tmp_height = start_height - (macroblok_size / 2);
					tmp_width = start_width - (macroblok_size / 2);

					tmp_SAD_block = SAD(macroblok, upsized_matrix_a.getMatrix(tmp_height, ((tmp_height + macroblok_size) - 1), tmp_width, (tmp_width + macroblok_size) - 1));
					
					if(tmp_SAD > tmp_SAD_block) {
						tmp_SAD = tmp_SAD_block;
						height_v = tmp_height;
						width_v = tmp_width;
					}

					for (int k = 0; k < 3; k++){
						for (int l = 0; l < 3; l++){

							tmp_height = (((k * step) + start_height) - step) - (macroblok_size / 2);  
							tmp_width = (((l * step) + start_height) - step) - (macroblok_size / 2);

							tmp_SAD_block = SAD(macroblok, upsized_matrix_a.getMatrix(tmp_height, ((tmp_height + macroblok_size) - 1), tmp_width, (tmp_width + macroblok_size) - 1));

							if(tmp_SAD > tmp_SAD_block) {
								tmp_SAD = tmp_SAD_block;
								height_v = tmp_height;
								width_v = tmp_width;
							}
						}
					}

					step = step / 2;

					start_height = height_v + (macroblok_size / 2);
					start_width = tmp_width + (macroblok_size / 2);

				}
				
//				System.out.print("( " + height_v + " , " + width_v + " ) ; ");
				tmp_list.add(new int[] {height_v, width_v});
				
			}
			
//			System.out.println();
			
		}
		
		return tmp_list;
	}
	
	
}
