#pragma once

#include "formatter.h"
#include <string>

class Pixel;
class Image;

class PAM_Formatter : public Formatter {
private:
	static const unsigned RGBA_depth;
	static const unsigned RGBA_maxval;
	static const std::string RGBA_tupltype;

	unsigned long width;
	unsigned long height;
	unsigned depth = 4;

	Pixel get_pixel(std::ifstream & ifs) const override;
	void put_pixel(std::ofstream& ofs, const Pixel& pixel) const override;
public:
	PAM_Formatter(std::string file_path) : Formatter(file_path) {}

	void read(Image& image) override;
	void write(const Image& image) const override;
};