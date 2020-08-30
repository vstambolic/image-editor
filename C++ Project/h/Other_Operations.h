#pragma once
#include "Operation.h"
#include <vector>
#include <algorithm>

class Paint_Operation : public Operation {
private:
	const double red;
	const double green;
	const double blue;
public:
	Paint_Operation(double _red = 0, double _green = 0, double _blue=0) : red(_red), green(_green), blue(_blue) {}
	std::string label() const override { return "paint"; }

	D_Pixel_Matrix apply(const Selection_Map& selections, D_Pixel_Matrix& matrix, const Coordinates& coordinates) const override {
		return wrapper(selections, matrix,coordinates, [this](D_Pixel& d_pixel) { d_pixel.red = red; d_pixel.green = green; d_pixel.blue = blue; });
	}
	D_Pixel_Matrix apply(D_Pixel_Matrix& matrix) const override {
		return wrapper(matrix, [this](D_Pixel& d_pixel) { d_pixel.red = red; d_pixel.green = green; d_pixel.blue = blue; });

	}

};

class Median_Operation : public Operation {
public:
	D_Pixel_Matrix apply(D_Pixel_Matrix& matrix) const override;
	D_Pixel_Matrix apply(const Selection_Map& selections, D_Pixel_Matrix& matrix, const Coordinates& coordinates) const override;

	std::string label() const override { return "med"; }
private:
	std::vector<std::reference_wrapper<const D_Pixel>>& extract_neighbours(std::vector<std::reference_wrapper<const D_Pixel>>& vec, const D_Pixel_Matrix& tmp, const int& m, const int& n, const int& i, const int& j) const;
	void find_median(std::vector<std::reference_wrapper<const D_Pixel>>& vec, D_Pixel& pix) const;
};
