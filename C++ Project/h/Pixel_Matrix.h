#pragma once
#include <vector>
#include "pixel.h"

typedef std::vector<std::vector<D_Pixel>> D_Pixel_Matrix;

class Pixel_Matrix : public std::vector<std::vector<Pixel>> {
public:
	Pixel_Matrix() = default;
	Pixel_Matrix(unsigned long height, std::vector<Pixel> vec) : std::vector<std::vector<Pixel>>(height, vec) {};
	operator D_Pixel_Matrix() const;
	Pixel_Matrix& operator=(const D_Pixel_Matrix& d_matrix); 

	friend std::ostream& operator << (std::ostream& os, const Pixel_Matrix& matrix);
};
