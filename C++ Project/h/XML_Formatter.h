#pragma once
#include "formatter.h"
#include <string>

class Pixel;
class Image;
class Composite_Operation;

class XML_Formatter : public Formatter {
private:
	void put_pixel(std::ofstream & ofs, const Pixel & pixel) const override;
	Pixel get_pixel(std::ifstream & ifs) const override;
public:
	XML_Formatter(const std::string& _file_path) : Formatter(_file_path) {}

	void read(Image& image) override;
	void write(const Image& image) const override;

	void write(const Composite_Operation & comp_op) const;
	void read(Composite_Operation& comp_op) const;

};

