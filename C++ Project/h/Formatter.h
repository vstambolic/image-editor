#pragma once
#include <string>
#include <fstream>

class Image;
class Pixel;

class Formatter {
protected:
	std::string file_path;

	virtual Pixel get_pixel(std::ifstream& ifs) const = 0;
	virtual void put_pixel(std::ofstream& ofs, const Pixel& pixel) const = 0;
public:
	Formatter(std::string _file_path) : file_path(_file_path) {}
	
	virtual void read(Image& image) = 0;
	virtual void write(const Image& image) const = 0;

	static std::unique_ptr<Formatter> match_formatter(const std::string& file_path);

	virtual ~Formatter() {}
};
