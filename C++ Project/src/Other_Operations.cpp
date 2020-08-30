#include "Other_Operations.h"


// Median -----------------------------------------------------------------------------------------------------------------------------------------------------------------------

// Apply ----------------------------------------------------------------

D_Pixel_Matrix Median_Operation::apply(D_Pixel_Matrix & matrix) const {
	std::vector<std::reference_wrapper<const D_Pixel>> vec;
	vec.reserve(9);

	const D_Pixel_Matrix tmp_matrix = matrix;
	const size_t m = matrix.size();
	const size_t n = matrix.begin()->size();

	for (size_t i = 0; i < m; ++i)
		for (size_t j = 0; j < n; ++j)
			find_median(extract_neighbours(vec, tmp_matrix, m, n, i, j), matrix[i][j]);

	return matrix;
}

D_Pixel_Matrix Median_Operation::apply(const Selection_Map & selections, D_Pixel_Matrix & matrix, const Coordinates & coordinates) const {
	std::vector<std::reference_wrapper<const D_Pixel>> vec;
	vec.reserve(9);

	const D_Pixel_Matrix tmp_matrix = matrix;
	const int m = matrix.size();
	const int n = matrix.begin()->size();

	for (int i = 0; i < m; ++i)
		for (int j = 0; j < n; ++j)
			if (selections.end() != std::find_if(selections.begin(), selections.end(), [i, j, &coordinates](const std::pair<std::string, Selection>& p) { return p.second.is_selected({ i + coordinates.x, j + coordinates.y }); }))
				find_median(extract_neighbours(vec, tmp_matrix, m, n, i, j), matrix[i][j]);

	return matrix;
}

// Helper methods -------------------------------------------------------------

std::vector<std::reference_wrapper<const D_Pixel>>& Median_Operation::extract_neighbours(std::vector<std::reference_wrapper<const D_Pixel>>& vec, const D_Pixel_Matrix & tmp, const int & m, const int & n, const int & i, const int & j) const {
	vec = { tmp[i][j] };

	if (i == 0) {
		if (j == 0)
			vec.insert(vec.end(), { tmp[i + 1][j], tmp[i][j + 1],tmp[i + 1][j + 1] });
		else
			if (j == n - 1)
				vec.insert(vec.end(), { tmp[i][j - 1],tmp[i + 1][j],tmp[i + 1][j - 1] });
			else
				vec.insert(vec.end(), { tmp[i][j - 1],tmp[i][j + 1],tmp[i + 1][j - 1],tmp[i + 1][j],tmp[i + 1][j + 1] });
	}
	else
		if (i == m - 1) {
			if (j == 0)
				vec.insert(vec.end(), { tmp[i - 1][j + 1],tmp[i - 1][j],tmp[i][j + 1] });
			else
				if (j == n - 1)
					vec.insert(vec.end(), { tmp[i - 1][j - 1],tmp[i - 1][j],tmp[i][j - 1] });
				else
					vec.insert(vec.end(), { tmp[i - 1][j - 1],tmp[i - 1][j],tmp[i - 1][j + 1],tmp[i][j - 1],tmp[i][j + 1] });
		}
		else {
			vec.insert(vec.end(), { tmp[i - 1][j],tmp[i + 1][j] });
			if (j == 0)
				vec.insert(vec.end(), { tmp[i - 1][j + 1],tmp[i][j + 1],tmp[i + 1][j + 1] });
			else
				if (j == n - 1)
					vec.insert(vec.end(), { tmp[i - 1][j - 1],tmp[i][j - 1],tmp[i + 1][j - 1] });
				else {
					vec.insert(vec.end(), { tmp[i - 1][j + 1],tmp[i][j + 1],tmp[i + 1][j + 1] });
					vec.insert(vec.end(), { tmp[i - 1][j - 1],tmp[i][j - 1],tmp[i + 1][j - 1] });
				}
		}
	return vec;
}

void Median_Operation::find_median(std::vector<std::reference_wrapper<const D_Pixel>>& vec, D_Pixel & pix) const {
	bool odd_size = vec.size() % 2;

	// Red
	sort(vec.begin(), vec.end(), [](const D_Pixel& d_pixel1, const D_Pixel& d_pixel2) { return d_pixel1.red < d_pixel2.red; });
	pix.red = (odd_size ? vec[vec.size() / 2].get().red : (vec[vec.size() / 2].get().red + vec[vec.size() / 2 - 1].get().red) / 2);
	// Green
	sort(vec.begin(), vec.end(), [](const D_Pixel& d_pixel1, const D_Pixel& d_pixel2) { return d_pixel1.green < d_pixel2.green; });
	pix.green = (odd_size ? vec[vec.size() / 2].get().green : (vec[vec.size() / 2].get().green + vec[vec.size() / 2 - 1].get().green) / 2);
	// Blue
	sort(vec.begin(), vec.end(), [](const D_Pixel& d_pixel1, const D_Pixel& d_pixel2) { return d_pixel1.blue < d_pixel2.blue; });
	pix.blue = (odd_size ? vec[vec.size() / 2].get().blue : (vec[vec.size() / 2].get().blue + vec[vec.size() / 2 - 1].get().blue) / 2);
	vec.clear();
}
