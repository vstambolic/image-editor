#pragma once

#include <functional>
#include <string>

#include "Selection.h"
#include "Pixel.h"
#include "Pixel_Matrix.h"



class Operation {

protected:
	static D_Pixel_Matrix& wrapper(const Selection_Map& selections, D_Pixel_Matrix& matrix, const Coordinates& coordinates, std::function<void(D_Pixel&)>&& func);
	static D_Pixel_Matrix& wrapper(D_Pixel_Matrix& matrix, std::function<void(D_Pixel&)>&& func);

public:
	virtual D_Pixel_Matrix apply(const Selection_Map& selections, D_Pixel_Matrix& matrix, const Coordinates& coordinates) const = 0;
	virtual D_Pixel_Matrix apply(D_Pixel_Matrix& matrix) const = 0;
	
	virtual std::string label() const = 0;
	virtual ~Operation() {}
};




