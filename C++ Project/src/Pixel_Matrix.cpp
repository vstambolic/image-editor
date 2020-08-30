#include "Pixel_Matrix.h"

Pixel_Matrix::operator D_Pixel_Matrix() const {
	D_Pixel_Matrix d_mat;
	d_mat.reserve(this->size());
	for (auto vec : *this) {
		vector<D_Pixel> d_vec(vec.begin(), vec.end());
		d_mat.push_back(d_vec);
	}
	return d_mat;
}

Pixel_Matrix& Pixel_Matrix::operator=(const D_Pixel_Matrix& d_matrix) {
	const size_t m = d_matrix.size();
	const size_t n = d_matrix[0].size();

	this->resize(m);
	for (size_t i = 0; i < m; ++i) {
		(*this)[i].resize(d_matrix[i].size());
		for (size_t j = 0; j < n; ++j)
			(*this)[i][j] = d_matrix[i][j];
	}
	return (*this);
}

std::ostream& operator << (std::ostream& os, const Pixel_Matrix& matrix) {
	for (auto& vec : matrix)
		for (auto& pix : vec)
			os << pix;
	return os;
}