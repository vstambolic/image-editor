#pragma once
#include "Operation.h"
#include <cmath>
#include <string>


class Predefined_Operation : public Operation {
public:
	D_Pixel_Matrix apply(const Selection_Map& selections, D_Pixel_Matrix& matrix, const Coordinates& coordinates) const override {
		return wrapper(selections, matrix, coordinates, [this](D_Pixel& d_pixel) { this->predefined_apply(d_pixel); });
	}
	D_Pixel_Matrix apply(D_Pixel_Matrix& matrix) const override {
		return wrapper(matrix, [this](D_Pixel& d_pixel) { this->predefined_apply(d_pixel); });
	}
protected:
	virtual void predefined_apply(D_Pixel& d_pixel) const = 0;
};

class Log_Operation : public Predefined_Operation {
public:
	std::string label() const override { return "log"; }
private:
	void predefined_apply(D_Pixel& d_pixel) const override {
		auto log = [](double& d_component) { if (d_component > 0) d_component = std::log10(d_component); };
		log(d_pixel.red), log(d_pixel.green), log(d_pixel.blue);
	}
};

class Abs_Operation : public Predefined_Operation {
public:
	std::string label() const override { return "abs"; }

private:
	void predefined_apply(D_Pixel& d_pixel) const override {
		using std::abs;
		d_pixel.red = abs(d_pixel.red), d_pixel.green = abs(d_pixel.green), d_pixel.blue = abs(d_pixel.blue);
	}
};


class Invert_Operation : public Predefined_Operation {
public:
	std::string label() const override { return "inv"; }
private:
	void predefined_apply(D_Pixel& d_pixel) const override {
		auto invert = [](double& d_component) { d_component = MAX_BYTE - d_component; };
		invert(d_pixel.red), invert(d_pixel.green), invert(d_pixel.blue);
	}
};


class Grayscale_Operation : public Predefined_Operation {
public:
	std::string label() const override { return "grs"; }
private:
	void predefined_apply(D_Pixel& d_pixel) const override {
		double average = (d_pixel.red + d_pixel.green + d_pixel.blue) / 3;
		d_pixel.red = d_pixel.green = d_pixel.blue = average;
	}
};

class BnW_Operation : public Predefined_Operation {
public:
	std::string label() const override { return "bnw"; }
private:
	void predefined_apply(D_Pixel& d_pixel) const override {
		double average = (d_pixel.red + d_pixel.green + d_pixel.blue) / 3;
		if (average < MAX_BYTE / 2)
			d_pixel.red = d_pixel.green = d_pixel.blue = 0;
		else
			d_pixel.red = d_pixel.green = d_pixel.blue = MAX_BYTE;
	}
};
